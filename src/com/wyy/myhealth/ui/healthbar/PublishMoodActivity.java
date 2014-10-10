package com.wyy.myhealth.ui.healthbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;

import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.imag.utils.PhoneUtlis;
import com.wyy.myhealth.imag.utils.PhotoUtils;
import com.wyy.myhealth.ui.baseactivity.SubmitActivity;
import com.wyy.myhealth.ui.shaiyishai.PublishAdapter;
import com.wyy.myhealth.ui.shaiyishai.PublishAdapter.PicClickListener;
import com.wyy.myhealth.utils.NoticeUtils;

public class PublishMoodActivity extends SubmitActivity implements
		PicClickListener {

	private static final String TAG = PublishMoodActivity.class.getSimpleName();
	private GridView publishGridView;
	private PublishAdapter publishAdapter;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private EditText moodEditText;
	private String moodid = "";
	private RatingBar moodRatingBar;
	private String moodIndex = "";

	@Override
	protected void submitMsg() {
		// TODO Auto-generated method stub
		moodEditText.setError(null);
		if (!TextUtils.isEmpty(moodEditText.getText().toString())) {
			HealthHttpClient.doHttpPostMood(WyyApplication.getInfo().getId(),
					moodEditText.getText().toString(), moodIndex,
					postMoodHandler);
		} else {
			moodEditText.setError(getString(R.string.nullcontentnotice));
			moodEditText.requestFocus();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish);
		context = this;
		initView();
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UmenAnalyticsUtility.onResume(context);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		UmenAnalyticsUtility.onPause(context);
	}
	
	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();

		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(R.string.publish_mood);

	}

	private void initView() {

		moodRatingBar = (RatingBar) findViewById(R.id.mood_index);
		moodEditText = (EditText) findViewById(R.id.edit_publish);
		moodRatingBar
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						// TODO Auto-generated method stub
						moodIndex = "" + (int)rating;

						Log.i(TAG, "指数:" + rating);
					}
				});

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("url", R.drawable.publish_sec);
		list.add(map);
		publishGridView = (GridView) findViewById(R.id.publish_gridView1);
		publishAdapter = new PublishAdapter(this, list);
		publishGridView.setAdapter(publishAdapter);
		publishAdapter.setPicClickListerter(this);

	}

	@Override
	public void onPicClick(int position) {
		// TODO Auto-generated method stub
		if (position == list.size() - 1 && position < 4) {
			PhotoUtils.secPic(context);
		}
	}

	/**
	 * 发送返回处理
	 */
	private AsyncHttpResponseHandler postMoodHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			finish();
			NoticeUtils.notice(context,
					getResources().getString(R.string.publishshaiyishai),
					ConstantS.PUBLISH_SHAI_ID);
		}

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			parseJson(content);
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);

			NoticeUtils.removeNotice(ConstantS.PUBLISH_SHAI_ID, context);
			NoticeUtils.showFailePublish(context);

		}

		public void onFinish() {
			super.onFinish();
		};

	};

	/**
	 * 处理发送结果
	 * 
	 * @param content
	 */
	private void parseJson(String content) {
		JSONObject moodJsonObject;
		try {
			moodJsonObject = new JSONObject(content);
			if ("1".equals(moodJsonObject.getString("result"))) {
				moodid = moodJsonObject.getJSONObject("mood").getString("id");
				shaiPic(moodid);
			} else {

				NoticeUtils.removeNotice(ConstantS.PUBLISH_SHAI_ID, context);
				NoticeUtils.showFailePublish(context);

				Toast.makeText(context, R.string.publish_faliure,
						Toast.LENGTH_LONG).show();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			NoticeUtils.removeNotice(ConstantS.PUBLISH_SHAI_ID, context);
			NoticeUtils.showFailePublish(context);
			Toast.makeText(context, R.string.publish_faliure, Toast.LENGTH_LONG)
					.show();
		}

	}

	/**
	 * 发送照片
	 * 
	 * @param moodid
	 */
	private void shaiPic(final String moodid) {
		if (list.size() < 2) {
			NoticeUtils.removeNotice(ConstantS.PUBLISH_SHAI_ID, context);
			NoticeUtils.showSuccessfulNotification(context);
			return;
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				NoticeUtils.removeNotice(ConstantS.PUBLISH_SHAI_ID, context);
				NoticeUtils.showProgressPublish(context, 0, list.size() - 1,
						ConstantS.PUBLISH_SHAI_ID);
				HealthHttpClient.doHttpPostMoodPic(
						moodid,
						PhoneUtlis.bitmapNCutToString(list.get(0).get("url")
								.toString()), new UpPicHandler(0));
			}
		}).start();

	}

	public class UpPicHandler extends AsyncHttpResponseHandler {
		private int index = 0;

		public UpPicHandler(int index) {
			this.index = index;
		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
		}

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			NoticeUtils.showProgressPublish(context, index + 1,
					list.size() - 1, ConstantS.PUBLISH_SHAI_ID);
			if (index < list.size() - 2) {
				index++;
				HealthHttpClient.doHttpPostMoodPic(
						moodid,
						PhoneUtlis.bitmapNCutToString(list.get(index)
								.get("url").toString()),
						new UpPicHandler(index));
			} else {

				NoticeUtils.removeNotice(ConstantS.PUBLISH_SHAI_ID, context);
				NoticeUtils.showSuccessfulNotification(context);

				// confirShai();
			}

		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);
			NoticeUtils.removeNotice(ConstantS.PUBLISH_SHAI_ID, context);
			NoticeUtils.showFailePublish(context);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (resultCode != RESULT_OK) {
			return;
		}

		if (requestCode == 0) {
			try {

				final String str;
				Uri localUri = data.getData();
				String[] arrayOfString = new String[1];
				arrayOfString[0] = "_data";
				Cursor localCursor = getContentResolver().query(localUri,
						arrayOfString, null, null, null);
				if (localCursor == null)
					return;
				localCursor.moveToFirst();
				str = localCursor.getString(localCursor
						.getColumnIndex(arrayOfString[0]));
				localCursor.close();

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("url", str);
				list.add(0, map);
				Log.i(TAG, "地址:" + str);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (requestCode == 1) {
			try {
				String path = PhotoUtils.getPicPathFromUri(
						PhotoUtils.imageFileUri, this);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("url", path);
				list.add(0, map);
				Log.i(TAG, "地址:" + path);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		publishAdapter.notifyDataSetChanged();

		super.onActivityResult(requestCode, resultCode, data);
	}

}
