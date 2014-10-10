package com.wyy.myhealth.analytics;

import com.umeng.analytics.MobclickAgent;
import com.wyy.myhealth.config.Config;

import android.content.Context;

public class UmenAnalyticsUtility {

	public static void onResume(Context context) {
		if (Config.ANALYTY_ENABLE) {
			MobclickAgent.onResume(context);
		}
	}

	public static void onPause(Context context) {
		if (Config.ANALYTY_ENABLE) {
			MobclickAgent.onPause(context);
		}
	}

	public static void onPageStart(String packet) {
		if (Config.ANALYTY_ENABLE) {
			MobclickAgent.onPageStart(packet);
		}
	}
	
	public static void onPageEnd(String packet) {
		if (Config.ANALYTY_ENABLE) {
			MobclickAgent.onPageEnd(packet);
		}
	}

}
