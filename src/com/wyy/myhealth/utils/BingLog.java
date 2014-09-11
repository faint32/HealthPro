package com.wyy.myhealth.utils;

import com.wyy.myhealth.config.Config;

import android.util.Log;


public class BingLog  {
	
	
	public static void i(String tag,String msg){
		if (Config.DEVELOPER_MODE) {
			Log.i(tag, msg);
		}
		
	}
	
	public static void w(String tag,String msg){
		if (Config.DEVELOPER_MODE) {
			Log.w(tag, msg);
		}
		
	}
	
	public static void d(String tag,String msg){
		if (Config.DEVELOPER_MODE) {
			Log.d(tag, msg);
		}
	}
	
	public static void e(String tag,String msg){
		if (Config.DEVELOPER_MODE) {
			Log.e(tag, msg);
		}
	}
	
	public static void v(String tag,String msg){
		if (Config.DEVELOPER_MODE) {
			Log.v(tag, msg);
		}
	}
	
	public static void wtf(String tag,String msg){
		if (Config.DEVELOPER_MODE) {
			Log.wtf(tag, msg);
		}
	}
	
	

	
	
}
