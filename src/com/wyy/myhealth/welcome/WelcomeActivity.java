package com.wyy.myhealth.welcome;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.wyy.myhealth.MainActivity;
import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.baidu.utlis.Utils;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.ui.login.utils.LoginUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

public class WelcomeActivity extends Activity {

	boolean isFirstIn = false;
	private static final int GO_HOME = 1000;
	private static final int GO_LOGIN = 1001;
	private static final long SPLASH_DELAY_MILLIS = 1500;
	private static final long SPLASH_DELAY_MILLIS_ = 8000;
	private ProgressBar wait_Onbind;
	private boolean isEnter=false;

	/**
	 * Handler:跳转到不同界面
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			case GO_LOGIN:
				goLogin();
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);

		initReceiveraPush();
		initUI();
		UmenAnalyticsUtility.setDebugMode(false);
		UmenAnalyticsUtility.updateOnlineConfig(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UmenAnalyticsUtility.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		UmenAnalyticsUtility.onPause(this);
	}
	

	private void initReceiveraPush() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConstantS.BAIDU_ONBIND);
		registerReceiver(onbindReceiver, filter);

		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY,
				Utils.getMetaValue(WelcomeActivity.this, "api_key"));
		// Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
		PushManager.enableLbs(getApplicationContext());
	}

	private void initUI() {
		getPersonInfo(WelcomeActivity.this);
		wait_Onbind = (ProgressBar) findViewById(R.id.welcome_pro);
		if (WyyApplication.getInfo() != null) {
			wait_Onbind.setVisibility(View.GONE);
			unregisterReceiver(onbindReceiver);
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
			LoginUtils.login(WelcomeActivity.this, WyyApplication.getInfo()
					.getIdcode());
		} else {
			mHandler.sendEmptyMessageDelayed(GO_LOGIN, SPLASH_DELAY_MILLIS_);
		}

	}

	private void goHome() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private void goLogin() {
		if (isEnter) {
			return;
		}
		try {
			Intent intent = new Intent(this, BootPagerActivity.class);
			startActivity(intent);
			finish();
			isEnter=true;
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private BroadcastReceiver onbindReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// if (TextUtils.isEmpty(Utils.channelId)) {
			// ToastUtils.showLong(context, "未知错误");
			// }

			goLogin();
			unregisterReceiver(onbindReceiver);
		}
	};

	/**
	 * 获取信息
	 */
	public static void getPersonInfo(Context context) {
		if (context==null) {
			return;
		}
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConstantS.USER_DATA, Context.MODE_PRIVATE);
		String personstr = sharedPreferences.getString("personinfo", "");
		if (!TextUtils.isEmpty(personstr)) {
			byte[] bytepersonbase64 = Base64.decode(personstr.getBytes(),
					Base64.DEFAULT);
			ByteArrayInputStream bis = new ByteArrayInputStream(
					bytepersonbase64);
			ObjectInputStream ois;
			try {
				ois = new ObjectInputStream(bis);
				try {
					PersonalInfo personalInfo = (PersonalInfo) ois.readObject();
					WyyApplication.setInfo(personalInfo);

				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (StreamCorruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
