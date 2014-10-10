package com.wyy.myhealth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.HealthRecoderBean;
import com.wyy.myhealth.bean.MoodaFoodBean;
import com.wyy.myhealth.bean.RecorderInfoBean;
import com.wyy.myhealth.config.Config;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.JsonHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.http.utils.JsonUtils;
import com.wyy.myhealth.ui.shaiyishai.utils.ShaiUtility;
import com.wyy.myhealth.utils.BingLog;
import com.wyy.myhealth.welcome.WelcomeActivity;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;

public class MainService extends Service {

	private static final String TAG = MainService.class.getSimpleName();

	private static List<HealthRecoderBean> thHealthRecoderBeans = new ArrayList<>();

	private static List<HealthRecoderBean> nextHealthRecoderBeans = new ArrayList<>();

	private static RecorderInfoBean recorderInfoBean = new RecorderInfoBean();

	public static RecorderInfoBean getRecorderInfoBean() {
		return recorderInfoBean;
	}

	private static List<String> sports = new ArrayList<>();

	private ScheduledExecutorService service;

	public ScheduledExecutorService getService() {
		return service;
	}

	private String time = "";

	private Context context;

	public static List<HealthRecoderBean> getNextHealthRecoderBeans() {
		return nextHealthRecoderBeans;
	}

	public static List<HealthRecoderBean> getThHealthRecoderBeans() {
		return thHealthRecoderBeans;
	}

	public static List<String> getSports() {
		return sports;
	}

	private final Binder mBinder = new Wibingder();

	public class Wibingder extends Binder {

		public MainService getBingder() {
			return MainService.this;
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		BingLog.i(TAG, "Service===onCreate===");
		context = this;
		initFilter();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mainServiceReceiver);
		stopRunable();
	}

	private void getUserRecorder() {
		if (WyyApplication.getInfo() == null) {
			WelcomeActivity.getPersonInfo(this);
		}
		if (null == WyyApplication.getInfo()) {
			return;
		}
		getHealthRecored();
		getFoots();
	}

	private void getHealthRecored() {

		HealthHttpClient.getHealthRecordert(WyyApplication.getInfo().getId(),
				"0", handler);
	}

	private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			super.onFinish();
		}

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			BingLog.i(TAG, "返回:" + content);
			parseJson(content);
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);
			parseJson(getUserRecored(context));
		}

	};

	private void parseJson(String content) {
		if (TextUtils.isEmpty(content)) {
			return;
		}
		try {
			JSONObject jsonObject = new JSONObject(content);
			recorderInfoBean = JsonUtils.getRecorderInfoBean(jsonObject);
			int length0 = jsonObject.getJSONArray("nutritions").length();
			thHealthRecoderBeans.clear();
			for (int i = 0; i < length0; i++) {
				HealthRecoderBean healthRecoderBean = JsonUtils
						.getHealthRecoder(jsonObject.getJSONArray("nutritions")
								.getJSONObject(i));

				if (null != healthRecoderBean) {
					thHealthRecoderBeans.add(healthRecoderBean);
				}

			}

			int length1 = jsonObject.getJSONArray("nutritionsNext").length();
			nextHealthRecoderBeans.clear();
			for (int i = 0; i < length1; i++) {

				HealthRecoderBean healthRecoderBean = JsonUtils
						.getHealthRecoder(jsonObject.getJSONArray(
								"nutritionsNext").getJSONObject(i));

				if (null != healthRecoderBean) {
					nextHealthRecoderBeans.add(healthRecoderBean);
				}

			}

			saveRecored(context, content);
			saveRecoredsize(context, nextHealthRecoderBeans.size());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			if (Config.DEVELOPER_MODE) {
				e.printStackTrace();
			}

		}
	}

	private void getFoots() {
		HealthHttpClient.doHttpGetMyFoots(WyyApplication.getInfo().getId(),
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String content) {
						// TODO Auto-generated method stub
						super.onSuccess(content);
						parseFoots(content);

					}

					@Override
					public void onFailure(Throwable error, String content) {
						// TODO Auto-generated method stub
						super.onFailure(error, content);
						parseFoots(getUserFoots(context));
					}

				});
	}

	private void parseFoots(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			int length = jsonObject.getJSONArray("foots").length();
			sports.clear();
			for (int i = 0; i < length; i++) {
				String level = jsonObject.getJSONArray("foots")
						.getJSONObject(i).getString("level");
				sports.add(level);
			}

			saveFoots(context, json);
			saveFootssize(context, sports.size());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConstantS.ACTION_RESH_USER_DATA);
		registerReceiver(mainServiceReceiver, filter);
	}

	private BroadcastReceiver mainServiceReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals(ConstantS.ACTION_RESH_USER_DATA)) {
				getUserRecorder();
			}
		}
	};

	@SuppressLint("NewApi")
	public void initRunable() {
		service = Executors.newScheduledThreadPool(2);
		service.scheduleAtFixedRate(checkRunnable, ConstantS.DELAY_TIME,
				ConstantS.PERIOD_TIME, TimeUnit.SECONDS);
		service.scheduleAtFixedRate(userDataRunnable, ConstantS.DELAY_TIME,
				ConstantS.PERIOD_TIME, TimeUnit.SECONDS);
	}

	private Runnable checkRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			time = ShaiUtility.getShaiJsonTime(context);
			BingLog.i(TAG, "执行:" + time);
			HealthHttpClient.aired20(WyyApplication.getInfo().getId(),
					ConstantS.FIRST, ConstantS.LIMIT, checkHandler);
		}
	};

	private Runnable userDataRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			getUserRecorder();
		}
	};

	private JsonHttpResponseHandler checkHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(response);
			parseJson(response);
		}

	};

	private void parseJson(JSONObject response) {
		MoodaFoodBean moodaFoodBean = ShaiUtility.getParseInfo(response);
		BingLog.i(TAG, "执行:" + response);
		if (moodaFoodBean != null
				&& !moodaFoodBean.getCreatetime().equals(time)) {
			sendMoodaFoodBean2Discover(moodaFoodBean);
		}
	}

	private void sendMoodaFoodBean2Discover(MoodaFoodBean moodaFoodBean) {
		Intent intent = new Intent(ConstantS.ACTION_SEND_SHAI);
		intent.putExtra("shai", moodaFoodBean);
		sendBroadcast(intent);
		BingLog.i(TAG, "执行:" + moodaFoodBean.getUser().getUsername());
	}

	private void stopRunable() {
		try {
			if (service != null && !service.isShutdown()) {
				service.shutdown();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	/**
	 * 获得记录数据
	 * @param context
	 * @return
	 */
	public static String getUserRecored(Context context) {
		String mString = "";
		if (context != null) {
			SharedPreferences preferences = context.getSharedPreferences(TAG,
					MODE_PRIVATE);
			mString = preferences.getString("recored", "");
		}
		return mString;
	}
	
	/**
	 * 获得记录次数
	 * @param context
	 * @return
	 */
	public static int getUserRecoredsize(Context context) {
		int size = 0;
		if (context != null) {
			SharedPreferences preferences = context.getSharedPreferences(TAG,
					MODE_PRIVATE);
			try {
				size = preferences.getInt("recoredsize", 0);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		return size;
	}
	/**
	 * 保存记录数据
	 * @param context
	 * @param json
	 */
	private static void saveRecored(Context context, String json) {
		if (context == null) {
			return;
		}

		SharedPreferences preferences = context.getSharedPreferences(TAG,
				MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("recored", json);
		editor.commit();
	}
	
	/**
	 * 保存记录次数
	 * @param context
	 * @param size
	 */
	private static void saveRecoredsize(Context context, int size) {
		if (context == null) {
			return;
		}

		if (size>getUserRecoredsize(context)) {
			sendNewRecoredShai(context);
		}

		SharedPreferences preferences = context.getSharedPreferences(TAG,
				MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt("recoredsize", size);
		editor.commit();
	}
	
	/**
	 * 获得步伐数据
	 * @param context
	 * @return
	 */
	public static String getUserFoots(Context context) {
		String mString = "";
		if (context != null) {
			SharedPreferences preferences = context.getSharedPreferences(TAG,
					MODE_PRIVATE);
			mString = preferences.getString("foots", "");
		}
		return mString;
	}
	/**
	 * 获得步伐次数
	 * @param context
	 * @return
	 */
	public static int getUserFootssize(Context context) {
		int size=0;
		if (context != null) {
			SharedPreferences preferences = context.getSharedPreferences(TAG,
					MODE_PRIVATE);
			try {
				size = preferences.getInt("footssize", 0);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		return size;
	}
	
	/**
	 * 保存步伐数据
	 * @param context
	 * @param json
	 */
	private static void saveFoots(Context context, String json) {
		if (context == null) {
			return;
		}

		SharedPreferences preferences = context.getSharedPreferences(TAG,
				MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("foots", json);
		editor.commit();
	}
	/**
	 * 保存步伐次数
	 * @param context
	 * @param json
	 */
	private static void saveFootssize(Context context, int size) {
		if (context == null) {
			return;
		}

		if (size>getUserFootssize(context)) {
			sendNewRecoredShai(context);
		}

		SharedPreferences preferences = context.getSharedPreferences(TAG,
				MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt("footssize", size);
		editor.commit();
	}
	

	private static void sendNewRecoredShai(Context context) {
		context.sendBroadcast(new Intent(ConstantS.ACTION_RECORED_NOTICE));
	}

}
