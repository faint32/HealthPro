package com.wyy.myhealth.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.telephony.TelephonyManager;

public class PhoneInfoUtils {

	public static String getPhoneNum(Context context) {

		String telnum = "";
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		telnum = telephonyManager.getLine1Number();
		return telnum;

	}

	public static String getPhoneNumCut86(Context context) {

		String telnum = "";
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		telnum = telephonyManager.getLine1Number();
		if (telnum.indexOf("+86") != -1) {
			telnum = telnum.substring(3);
		}
		return telnum;

	}

	public static String getPhoneIme(Context context) {

		String ime = "";
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		ime = telephonyManager.getDeviceId();
		return ime;

	}

	public static boolean isPhoneNum(String num) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(num);
		return m.matches();
	}

}
