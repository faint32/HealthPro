package com.wyy.myhealth.http.utils;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wyy.myhealth.bean.Foods;
import com.wyy.myhealth.bean.HealthRecoderBean;
import com.wyy.myhealth.bean.IceBoxFoodBean;
import com.wyy.myhealth.bean.LevelBean;
import com.wyy.myhealth.bean.MoodInfoBean;
import com.wyy.myhealth.bean.MoodaFoodBean;
import com.wyy.myhealth.bean.NearFoodBean;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.bean.RecorderInfoBean;
import com.wyy.myhealth.bean.ScanMoodBean;
import com.wyy.myhealth.bean.TencentTokenBean;
import com.wyy.myhealth.bean.TencentUserInfoBean;
import com.wyy.myhealth.config.Config;
import com.wyy.myhealth.utils.BingLog;

public class JsonUtils {

	private static final String TAG = JsonUtils.class.getSimpleName();

	/**
	 * 返回结果
	 * 
	 * @param jsonObject
	 *            json对象
	 * @param key
	 *            键值
	 * @return
	 */
	public static String getKey(JSONObject jsonObject, String key) {
		if (jsonObject.has(key)) {
			try {
				return jsonObject.getString(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
		}

		return "";

	}

	/**
	 * 解析对象
	 * 
	 * @param object
	 * @return
	 */
	public static PersonalInfo getInfo(Object object) {
		PersonalInfo info = null;
		try {
			Gson gson = new Gson();
			info = gson.fromJson(object.toString(), PersonalInfo.class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info;
	}

	/**
	 * 解析对象
	 * 
	 * @param object
	 * @return
	 */
	public static Foods getfoods(JSONObject object) {
		Foods foods = null;
		try {
			Gson gson = new Gson();
			foods = gson.fromJson(object.toString(), Foods.class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return foods;
	}

	/**
	 * 判断请求是否正确
	 * 
	 * @param object
	 * @return
	 */
	public static Boolean isSuccess(JSONObject object) {
		try {
			if (object.has("result")) {
				int result = object.getInt("result");
				if (result == 1) {
					return true;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 判断请求是否正确
	 * 
	 * @param object
	 * @return
	 */
	public static Boolean isSuccess(String content) {
		try {
			JSONObject object = new JSONObject(content);
			if (object.has("result")) {
				int result = object.getInt("result");
				if (result == 1) {
					return true;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public static HealthRecoderBean getHealthRecoder(JSONObject object) {
		HealthRecoderBean healthRecoderBean = new HealthRecoderBean();
		try {
			Gson gson = new Gson();
			healthRecoderBean = gson.fromJson(object.toString(),
					HealthRecoderBean.class);
		} catch (Exception e) {
			// TODO: handle exception
			if (Config.DEVELOPER_MODE) {
				e.printStackTrace();
				BingLog.e(TAG, "解析错误");
			}
		}

		return healthRecoderBean;
	}

	public static NearFoodBean getNearFoodBean(JSONObject object) {
		NearFoodBean nearFoodBean = new NearFoodBean();
		try {
			Gson gson = new Gson();
			nearFoodBean = gson.fromJson(object.toString(), NearFoodBean.class);
		} catch (Exception e) {
			// TODO: handle exception
			if (Config.DEVELOPER_MODE) {
				e.printStackTrace();
				BingLog.e(TAG, "解析错误");
			}
		}

		return nearFoodBean;
	}

	public static IceBoxFoodBean getIceBoxFoodBean(JSONObject object) {
		IceBoxFoodBean iceBoxFoodBean = new IceBoxFoodBean();
		try {
			iceBoxFoodBean = new Gson().fromJson(object.toString(),
					IceBoxFoodBean.class);
		} catch (Exception e) {
			// TODO: handle exception
			if (Config.DEVELOPER_MODE) {
				e.printStackTrace();
				BingLog.e(TAG, "解析错误");
			}
		}

		return iceBoxFoodBean;

	}

	public static IceBoxFoodBean getIceBoxFoodBean(String content) {
		IceBoxFoodBean iceBoxFoodBean = new IceBoxFoodBean();
		try {
			JSONObject object = new JSONObject(content);
			iceBoxFoodBean = new Gson().fromJson(object.toString(),
					IceBoxFoodBean.class);
		} catch (Exception e) {
			// TODO: handle exception
			if (Config.DEVELOPER_MODE) {
				e.printStackTrace();
				BingLog.e(TAG, "解析错误");
			}
		}

		return iceBoxFoodBean;

	}

	public static IceBoxFoodBean getIceBox4ProFoodBean(String content) {
		IceBoxFoodBean iceBoxFoodBean = new IceBoxFoodBean();
		try {
			JSONObject jsonObject = new JSONObject(content);
			JSONObject object = jsonObject.getJSONObject("foods");
			iceBoxFoodBean = new Gson().fromJson(object.toString(),
					IceBoxFoodBean.class);
		} catch (Exception e) {
			// TODO: handle exception
			if (Config.DEVELOPER_MODE) {
				e.printStackTrace();
				BingLog.e(TAG, "解析错误");
			}
		}

		return iceBoxFoodBean;

	}

	public static MoodaFoodBean getMoodaFoodBean(JSONObject object) {
		MoodaFoodBean moodaFoodBean = new MoodaFoodBean();
		try {
			moodaFoodBean = new Gson().fromJson(object.toString(),
					MoodaFoodBean.class);
		} catch (Exception e) {
			// TODO: handle exception
			if (Config.DEVELOPER_MODE) {
				e.printStackTrace();
				BingLog.e(TAG, "解析错误");
			}
		}

		return moodaFoodBean;

	}

	public static RecorderInfoBean getRecorderInfoBean(JSONObject object) {
		RecorderInfoBean recorderInfoBean = new RecorderInfoBean();
		try {
			recorderInfoBean = new Gson().fromJson(object.toString(),
					RecorderInfoBean.class);
		} catch (Exception e) {
			// TODO: handle exception
			BingLog.w(TAG, "解析出错:" + e.getMessage());
		}

		return recorderInfoBean;

	}

	public static ScanMoodBean getScanMoodBean(JSONObject object) {
		ScanMoodBean scanMoodBean = new ScanMoodBean();
		try {
			scanMoodBean = new Gson().fromJson(object.toString(),
					ScanMoodBean.class);
		} catch (Exception e) {
			// TODO: handle exception
			BingLog.w(TAG, "解析出错:" + e.getMessage());
		}

		return scanMoodBean;
	}

	public static TencentTokenBean getTencentTokenBean(JSONObject object) {
		TencentTokenBean tencentTokenBean = new TencentTokenBean();
		try {
			tencentTokenBean = new Gson().fromJson(object.toString(),
					TencentTokenBean.class);
		} catch (Exception e) {
			// TODO: handle exception
			BingLog.w(TAG, "解析出错:" + e.getMessage());
		}
		return tencentTokenBean;
	}

	public static TencentUserInfoBean geTencentUserInfoBean(JSONObject object) {
		TencentUserInfoBean tencentUserInfoBean = new TencentUserInfoBean();
		try {
			tencentUserInfoBean = new Gson().fromJson(object.toString(),
					TencentUserInfoBean.class);
		} catch (Exception e) {
			// TODO: handle exception
			BingLog.w(TAG, "解析出错:" + e.getMessage());
		}
		return tencentUserInfoBean;
	}

	public static MoodInfoBean getMoodInfoBean(Object object) {
		MoodInfoBean moodInfoBean = new MoodInfoBean();
		try {
			moodInfoBean = new Gson().fromJson(object.toString(),
					MoodInfoBean.class);
		} catch (Exception e) {
			// TODO: handle exception
			BingLog.w(TAG, "解析出错:" + e.getMessage());
		}

		return moodInfoBean;

	}

	public static LevelBean getLevelBean(Object object) {
		LevelBean levelBean = new LevelBean();
		try {
			levelBean = new Gson().fromJson(object.toString(), LevelBean.class);
		} catch (Exception e) {
			// TODO: handle exception
			BingLog.w(TAG, "解析出错:" + e.getMessage());
		}

		return levelBean;
	}

}
