package com.wyy.myhealth.imag.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.wyy.myhealth.file.utils.FileUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

public class SavePic {

	/**
	 * 保存图片名为wyy.png
	 * 
	 * @param mBitmap
	 */
	public static void saveFoodPic(Bitmap mBitmap) {
		File file = new File(FileUtils.HEALTH_IMAG, "wyy" + ".png");
		BufferedOutputStream bos;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(file));
			mBitmap.compress(CompressFormat.PNG, 100, bos);
			bos.flush();
			bos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 保存图片示例
	 * 
	 * @param mBitmap
	 */
	public static void saveFoodPic2Example(final Bitmap mBitmap) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				File file = new File(FileUtils.HEALTH_IMAG, "chc" + ".png");
				BufferedOutputStream bos;
				try {
					bos = new BufferedOutputStream(new FileOutputStream(file));
					mBitmap.compress(CompressFormat.PNG, 100, bos);
					bos.flush();
					bos.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();

	}
	
	/**
	 * 保存图片示例
	 * 
	 * @param mBitmap
	 */
	public static void saveRecoredPic2Example(final Bitmap mBitmap) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				File file = new File(FileUtils.HEALTH_IMAG, "recored" + ".png");
				BufferedOutputStream bos;
				try {
					bos = new BufferedOutputStream(new FileOutputStream(file));
					mBitmap.compress(CompressFormat.PNG, 100, bos);
					bos.flush();
					bos.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();

	}
	

	/**
	 * 将拍下来的照片存放在SD卡中
	 * 
	 * @param data
	 * @throws IOException
	 */
	public static void saveToSDCard(byte[] data) {
		File jpgFile = new File(FileUtils.HEALTH_IMAG, "wyy" + ".png");
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(jpgFile);
			outputStream.write(data); // 写入sd卡中
			outputStream.close(); // 关闭输出流
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 文件输出流

	}

}
