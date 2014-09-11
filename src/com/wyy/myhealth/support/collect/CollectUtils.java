package com.wyy.myhealth.support.collect;


import android.content.Context;
import android.widget.Toast;

import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.utils.BingLog;

public class CollectUtils {

	/**
	 * 收藏方法
	 * 
	 * @param foodid
	 *            食物ID
	 */
	public static void collectFood(String foodid, final Context context) {
		com.wyy.myhealth.http.AsyncHttpResponseHandler handler = new com.wyy.myhealth.http.AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
			}

			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				BingLog.i(CollectUtils.class.getSimpleName(), "收藏" + content);
				Toast.makeText(context, R.string.collectsuccess,
						Toast.LENGTH_LONG).show();
				// Log.i(TAG, "收藏:" + content);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
				Toast.makeText(context, R.string.collectsuccess,
						Toast.LENGTH_LONG).show();
			}

		};
		HealthHttpClient.doHttpPostCollect(WyyApplication.getInfo().getId(),
				foodid, handler);
	}

	public static void delCollectFood(String foodid, final Context context) {

		com.wyy.myhealth.http.AsyncHttpResponseHandler handler = new com.wyy.myhealth.http.AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
			}

			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				Toast.makeText(context, R.string.collectsuccess,
						Toast.LENGTH_LONG).show();
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
				Toast.makeText(context, R.string.collectsuccess,
						Toast.LENGTH_LONG).show();
			}

		};

		HealthHttpClient.doHttpdelFoods(foodid, handler);
	}

	public static void delCollectFood(String userid,String foodid, final Context context) {

		com.wyy.myhealth.http.AsyncHttpResponseHandler handler = new com.wyy.myhealth.http.AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
			}

			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				Toast.makeText(context, R.string.delsuccess,
						Toast.LENGTH_LONG).show();
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
				Toast.makeText(context, R.string.delfailure,
						Toast.LENGTH_LONG).show();
			}

		};

		HealthHttpClient.delCollect(userid, foodid, handler);
	}

	
	/**
	 * 收藏心情
	 * 
	 * @param moodsid
	 *            心情ID
	 * @param context
	 *            上下文
	 */
	public static void postMoodCollect(String moodsid, final Context context) {
		com.wyy.myhealth.http.AsyncHttpResponseHandler handler = new com.wyy.myhealth.http.AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
			}

			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				BingLog.i(CollectUtils.class.getSimpleName(), "收藏" + content);
				Toast.makeText(context, R.string.collectsuccess,
						Toast.LENGTH_LONG).show();
				// Log.i(TAG, "收藏:" + content);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
				Toast.makeText(context, R.string.collectsuccess,
						Toast.LENGTH_LONG).show();
			}

		};
		HealthHttpClient.postMoodCollect(WyyApplication.getInfo().getId(),
				moodsid, handler);

	}
	
}
