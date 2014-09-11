package com.wyy.myhealth.imag.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.wyy.myhealth.ui.scan.ScanView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

public class UpBmp {

	@SuppressLint("NewApi")
	public static String bitmaptoString(Bitmap bitmap) {

		// 将Bitmap转换成字符串
		String string = null;

		ByteArrayOutputStream bStream = new ByteArrayOutputStream();

		bitmap.compress(CompressFormat.JPEG, 50, bStream);

		byte[] bytes = bStream.toByteArray();

		string = Base64.encodeToString(bytes, Base64.DEFAULT);

		return string;

	}

	public String upFile(String path, String bmp,String userid) {
		String result = null;
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(7 * 1000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Charset", "utf-8");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("Connection", "Keep-Alive");

			StringBuilder sb = new StringBuilder();
			// String mbmp=bmp.replace("+", "%2B");
			// String mbmp=unescape(bmp);
			// mbmp=escape(mbmp);

			// String mbmp=java.net.URLEncoder.encode(bmp);
			// mbmp=java.net.URLDecoder.decode(mbmp);
			@SuppressWarnings("deprecation")
			String mbmp = URLEncoder.encode(bmp);
			sb.append("foodpicStr=" + mbmp+"&");
			sb.append("userid=" + userid);
//			Log.i("sb", "发送：" + sb.toString());
			DataOutputStream outputStream = new DataOutputStream(
					conn.getOutputStream());
			outputStream.write(sb.toString().getBytes());
			Log.i("kk", "运行到");
			outputStream.flush();
			int res = conn.getResponseCode();

			Log.e("数据返回", "response code:" + res);
			if (res == 200) {
				InputStream inputStream = conn.getInputStream();

				BufferedReader br = new BufferedReader(new InputStreamReader(
						inputStream, "utf-8"));

				StringBuilder sb1 = new StringBuilder();
				// int ss ;
				// while((ss=inputStream.read())!=-1)
				// {
				// sb1.append((char)ss);
				// }

				while (br.read() != -1) {
					sb1.append(br.readLine());
				}

				result = "{" + sb1.toString();

				// result=sb1.toString();
			}
			outputStream.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return result;

	}

	/**
	 * 
	 * @param context
	 *            指针
	 * @param resId
	 *            地址
	 * @return 图片
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 鑾峰彇璧勬簮鍥剧墖
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 转码
	 * 
	 * @param src
	 * @return
	 */
	public static String unescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(
							src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(
							src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	public static String escape(String src) {
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);
		for (i = 0; i < src.length(); i++) {
			j = src.charAt(i);
			if (Character.isDigit(j) || Character.isLowerCase(j)
					|| Character.isUpperCase(j)) {
				tmp.append(j);
			} else if (j < 256) {
				tmp.append("%");
				if (j < 16) {
					tmp.append("0");
				}
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	public static Bitmap readBitMap(ScanView context, int resId) {
		// TODO Auto-generated method stub
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 鑾峰彇璧勬簮鍥剧墖
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

}
