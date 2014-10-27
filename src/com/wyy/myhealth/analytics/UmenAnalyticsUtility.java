package com.wyy.myhealth.analytics;

import java.util.Map;

import com.umeng.analytics.MobclickAgent;
import com.wyy.myhealth.config.Config;

import android.content.Context;

public class UmenAnalyticsUtility {

	public static void updateOnlineConfig(Context context) {
		if (Config.ANALYTY_ENABLE) {
			MobclickAgent.updateOnlineConfig(context);
		}
	}

	public static void setDebugMode(boolean debug) {
		if (Config.ANALYTY_ENABLE) {
			MobclickAgent.setDebugMode(debug);
		}
	}

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

	public static void onEvent(Context context, String eventId) {
		if (Config.ANALYTY_ENABLE) {
			MobclickAgent.onEvent(context, eventId);
		}
	}

	public static void onEvent(Context context, String eventId,
			String map) {
		if (Config.ANALYTY_ENABLE) {
			MobclickAgent.onEvent(context, eventId, map);
		}
	}
	
	public static void onEvent(Context context, String eventId,
			Map<String, String> map) {
		if (Config.ANALYTY_ENABLE) {
			MobclickAgent.onEvent(context, eventId, map);
		}
	}

}
