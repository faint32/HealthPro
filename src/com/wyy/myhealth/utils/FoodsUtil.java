package com.wyy.myhealth.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.http.utils.HttpUtil;

public class FoodsUtil {
	// 方框查询地址
	public final static String urlFindByBox = HealthHttpClient.BASE_URL+"boxFoods";
	// 文本查询地址
	public final static String urlFindByText = HealthHttpClient.BASE_URL+"searchFoods";
	// 临近查询地址
	public final static String urlFindByCircle = HealthHttpClient.BASE_URL+"nearbyFoods";
	// 临近查询地址
	public final static String urlMiniImage = HealthHttpClient.BASE_URL+"miniImage?id=";
	// 详情
	public final static String urldetails = HealthHttpClient.BASE_URL+"showFoodInfo";
	// 图片地址
	public final static String urlFoodpic = HealthHttpClient.BASE_URL+"upload";
	// 健康轴
	public final static String urlzhou = HealthHttpClient.BASE_URL+"userFoodsAndMoods";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void findByBox(double minx, double miny, double maxx, double maxy,
			AsyncHttpResponseHandler res) {

		RequestParams params = new RequestParams();
		String url = urlFindByBox + "?" + "minx=" + minx + "&miny=" + miny
				+ "&maxx=" + maxx + "&maxy=" + maxy;
		System.out.println(url);
		HttpUtil.get(url, params, res);
	}

	public void findByCircle(double lon, double lat,
			AsyncHttpResponseHandler res) {
		RequestParams params = new RequestParams();
		String url = urlFindByCircle + "?lon=" + lon + "&lat=" + lat
				+ "&radius=600";
		System.out.println(url);
		HttpUtil.get(url, params, res);
	}

	public void findByText(String str, AsyncHttpResponseHandler res) {

		RequestParams params = new RequestParams();
		try {
			HttpUtil.get(
					urlFindByText + "?txt=" + URLEncoder.encode(str, "UTF-8"),
					params, res);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void doHttpDetails(String foodid, AsyncHttpResponseHandler res) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		System.out.println("uri" + urldetails + "?foodid=" + foodid);
		HttpUtil.get(urldetails + "?foodid=" + foodid, params, res);
	}

	public static void doHttpUserFoodsAndMoods(String userId,
			AsyncHttpResponseHandler res) {
		RequestParams params = new RequestParams();
		System.out.println("uri" + urlzhou + "?userId=" + userId);
		HttpUtil.get(urlzhou + "?userId=" + userId, params, res);
	}

	/**
	 * 从view 得到图片
	 * 
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
		view.destroyDrawingCache();
		view.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache(true);
		return bitmap;
	}

	public static Drawable getDrawable(String url) {
		InputStream in = null;
		try {
			in = getUrlInputStream(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bitmap bitmap = getMyBitmap(in);
		@SuppressWarnings("deprecation")
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		return bd;
	}

	/**
	 * 
	 * 根据imagepath获取bitmap
	 * 
	 * @param imagePath
	 * 
	 * @return
	 */

	public static Bitmap getBitmap(String imagePath) {

		if (!(imagePath.length() > 5)) {

			return null;

		}

		File cache_file = new File(new File(
				Environment.getExternalStorageDirectory(), "xxxx"),
				"cachebitmap");

		cache_file = new File(cache_file, getMD5(imagePath));

		if (cache_file.exists()) {

			return BitmapFactory.decodeFile(getBitmapCache(imagePath));

		} else {

			try {

				URL url = new URL(imagePath);

				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();

				conn.setConnectTimeout(5000);

				if (conn.getResponseCode() == 200) {

					InputStream inStream = conn.getInputStream();

					File file = new File(new File(
							Environment.getExternalStorageDirectory(), "xxxx"),
							"cachebitmap");

					if (!file.exists())
						file.mkdirs();

					file = new File(file, getMD5(imagePath));

					FileOutputStream out = new FileOutputStream(file);

					byte buff[] = new byte[1024];

					int len = 0;

					while ((len = inStream.read(buff)) != -1) {

						out.write(buff, 0, len);

					}

					out.close();

					inStream.close();

					return BitmapFactory.decodeFile(getBitmapCache(imagePath));

				}

			} catch (Exception e) {
			}

		}

		return null;

	}

	/**
	 * 
	 * 获取缓存
	 * 
	 * @param url
	 * 
	 * @return
	 */

	public static String getBitmapCache(String url) {

		File file = new File(new File(
				Environment.getExternalStorageDirectory(), "xxxx"),
				"cachebitmap");

		file = new File(file, getMD5(url));

		if (file.exists()) {

			return file.getAbsolutePath();

		}

		return null;

	}

	// 加密为MD5

	public static String getMD5(String content) {

		try {

			MessageDigest digest = MessageDigest.getInstance("MD5");

			digest.update(content.getBytes());

			return getHashString(digest);

		} catch (Exception e) {

		}

		return null;

	}

	private static String getHashString(MessageDigest digest) {

		StringBuilder builder = new StringBuilder();

		for (byte b : digest.digest()) {

			builder.append(Integer.toHexString((b >> 4) & 0xf));

			builder.append(Integer.toHexString(b & 0xf));

		}

		return builder.toString().toLowerCase();

	}

	public static InputStream getUrlInputStream(String urlPath)
			throws IOException {
		URL url = new URL(urlPath);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		InputStream in = conn.getInputStream();
		if (in != null) {
			return in;
		} else {
			Log.i("test", "输入流对象为空");
			return null;
		}
	}

	public static Bitmap getMyBitmap(InputStream in) {
		Bitmap bitmap = null;
		if (in != null) {
			bitmap = BitmapFactory.decodeStream(in);
			// BitmapFactory的作用：create Bitmap objects from various
			// sources,including files,streams and byte-arrays;
			return bitmap;
		} else {
			Log.i("test", "输入流对象in为空");
			return null;
		}
	}

	public static boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = "/mnt/sdcard/baidu/tempdata/ls.db";
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			// database does't exist yet.

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

}
