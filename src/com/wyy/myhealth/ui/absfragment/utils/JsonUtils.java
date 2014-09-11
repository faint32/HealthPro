package com.wyy.myhealth.ui.absfragment.utils;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wyy.myhealth.bean.Foods;
import com.wyy.myhealth.bean.PersonalInfo;
/**
 * @deprecated
 * @author lyl
 *
 */
public class JsonUtils {

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
	public static PersonalInfo getInfo(JSONObject object) {
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
	
}
