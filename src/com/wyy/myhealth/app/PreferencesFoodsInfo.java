package com.wyy.myhealth.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.test.AndroidTestCase;

public class PreferencesFoodsInfo extends AndroidTestCase {
	// private static final String TAG = "PreferencesFoodsInfo";
	private static final String FOODSINFO = "foodsinfo";
	private static final String FOODID = "foodid";

	public void testAccessSharedPreferences() throws NameNotFoundException {
		// 通过本应该程序的上下文来建想要访问App的上下文对象
		// sn.len.sharedpreferences 存放commercialinfo.xml那个包名称
		// CONTEXT_IGNORE_SECURITY 取消跨App的的访问安全
		Context accessSharePreferences = getContext().createPackageContext(
				"sn.len.sharedpreferences", Context.CONTEXT_IGNORE_SECURITY);
		@SuppressWarnings("unused")
		SharedPreferences shared = accessSharePreferences.getSharedPreferences(
				FOODSINFO, Context.MODE_PRIVATE);

	}

	public static void setfoodId(Activity activity, String foodid) {
		SharedPreferences preferences = activity.getSharedPreferences(
				FOODSINFO, Context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		edit.putString(FOODID, foodid);
		edit.commit();
	}

	public static String getfoodId(Activity activity) {
		SharedPreferences ferences = activity
				.getSharedPreferences(FOODSINFO, 0);
		String foodid = ferences.getString(FOODID, "");
		return foodid;
	}

}
