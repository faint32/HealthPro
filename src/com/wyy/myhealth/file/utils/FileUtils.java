package com.wyy.myhealth.file.utils;

import android.os.Environment;

import java.io.File;

public class FileUtils {

	public static final String HEALTHMA = Environment.getExternalStorageDirectory()+"/wyy";
	public static final String HEALTH_IMAG = Environment.getExternalStorageDirectory()+"/wyy/image";
	public static final String PIC_PATH = HEALTH_IMAG + "/wyy.png";
	public static final String HEAD_PATH="wyyhead";
	// 手机路径
	public static final String WYY_PIC = "bing";

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 */
	public static void createAll_Path() {
		File file = new File(HEALTHMA);
		if (!file.exists()) {
			file.mkdir();
		}
	}

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 */
	public static void createPath() {
		createAll_Path();
		File file = new File(HEALTH_IMAG);
		if (!file.exists()) {
			file.mkdir();
		}
	}

}
