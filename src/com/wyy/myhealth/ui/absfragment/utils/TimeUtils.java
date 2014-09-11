package com.wyy.myhealth.ui.absfragment.utils;

import java.util.List;
import java.util.Map;

import android.util.Log;

import com.wyy.myhealth.bean.Comment;
import com.wyy.myhealth.utils.BingDateUtils;

public class TimeUtils {

	private static final String TAG=TimeUtility.class.getSimpleName();
	
	public static void getCnTime(List<Map<String, Object>> list) {
		int length = list.size();
		for (int i = 0; i < length; i++) {
			long time = BingDateUtils.getTime(list.get(i).get("time")
					.toString());
			list.get(i).put("new_time", TimeUtility.getListTime(time));
		}
	}

	public static void getCOmmentTime(List<Comment> list) {
		int length = list.size();
		try {
			for (int i = 0; i < length; i++) {
				long time = BingDateUtils.getTime(list.get(i).getCreatetime()
						.toString());
				list.get(i).setCreatetime("" + TimeUtility.getListTime(time));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void getDayMoth(List<Map<String, Object>> list) {
		int length = list.size();
		for (int i = 0; i < length; i++) {
			String timestr = "" + list.get(i).get("time");
			int day=BingDateUtils.getDay(timestr);
			int month=BingDateUtils.getMonth(timestr);
			Log.i(TAG, month+"ÔÂ"+day);
			if (i == 0) {
				list.get(i).put("day", day);
				list.get(i).put("month", month+"ÔÂ");
			} else {
				
				String atimestr = "" + list.get(i-1).get("time");
				
				int aday = BingDateUtils.getDay(atimestr);
				int amonth=BingDateUtils.getMonth(atimestr);
				
				if (amonth!=month||aday!=day) {
					list.get(i).put("day", day);
					list.get(i).put("month", month+"ÔÂ");
				}
				
			}

		}
	}

}
