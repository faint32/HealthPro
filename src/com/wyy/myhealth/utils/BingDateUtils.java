package com.wyy.myhealth.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;

public class BingDateUtils {

	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private static SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(
			WyyApplication.getInstance().getString(R.string.year_format_));

	private static Calendar calendar = Calendar.getInstance();

	public static String changeDate(String time) {
		String dateString = "";
		try {
			Date date = new Date(getTime(time));
			dateString = simpleDateFormat2.format(date);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return dateString;

	}

	@SuppressWarnings("deprecation")
	public static int getMonth(String date) {

		try {
			return simpleDateFormat.parse(date).getMonth() + 1;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 1;

	}

	@SuppressWarnings("deprecation")
	public static int getDay(String date) {

		try {
			return simpleDateFormat.parse(date).getDate();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	public static long getTime(String date) {
		try {
			return simpleDateFormat.parse(date).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return -1;

	}

	public static String getDateStr(Date date) {
		return simpleDateFormat.format(date);
	}

	/**
	 * 获得当前月份
	 * 
	 * @return
	 */
	public static String getPhoneMonth() {
		return "" + (calendar.get(Calendar.MONTH) + 1);
	}

	/**
	 * 日
	 * 
	 * @return
	 */
	public static String getPhoneDay() {
		return "" + (calendar.get(Calendar.DATE));
	}
}
