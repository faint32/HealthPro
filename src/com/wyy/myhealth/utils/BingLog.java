package com.wyy.myhealth.utils;

import com.wyy.myhealth.config.Config;

import android.util.Log;

public class BingLog {
	private static final String TAG = "DEBUG";

	public static void i(String msg) {
		if (Config.DEVELOPER_MODE) {
			Log.i(TAG, msg);
		}

	}

	public static void i(String tag, String msg) {
		if (Config.DEVELOPER_MODE) {
			Log.i(tag, msg);
		}

	}

	public static void w(String msg) {
		if (Config.DEVELOPER_MODE) {
			Log.w(TAG, msg);
		}

	}

	public static void w(String tag, String msg) {
		if (Config.DEVELOPER_MODE) {
			Log.w(tag, msg);
		}

	}

	public static void d(String msg) {
		if (Config.DEVELOPER_MODE) {
			Log.d(TAG, msg);
		}

	}

	public static void d(String tag, String msg) {
		if (Config.DEVELOPER_MODE) {
			Log.d(tag, msg);
		}
	}

	public static void e(String msg) {
		if (Config.DEVELOPER_MODE) {
			Log.e(TAG, msg);
		}

	}

	public static void e(String tag, String msg) {
		if (Config.DEVELOPER_MODE) {
			Log.e(tag, msg);
		}
	}

	public static void v(String msg) {
		if (Config.DEVELOPER_MODE) {
			Log.v(TAG, msg);
		}

	}

	public static void v(String tag, String msg) {
		if (Config.DEVELOPER_MODE) {
			Log.v(tag, msg);
		}
	}

	public static void wtf(String msg) {
		if (Config.DEVELOPER_MODE) {
			Log.wtf(TAG, msg);
		}

	}

	public static void wtf(String tag, String msg) {
		if (Config.DEVELOPER_MODE) {
			Log.wtf(tag, msg);
		}
	}

}
