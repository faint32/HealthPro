package com.wyy.myhealth.ui.shaiyishai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.imag.utils.PhoneUtlis;
import com.wyy.myhealth.imag.utils.PhotoUtils;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;
import com.wyy.myhealth.ui.shaiyishai.PublishAdapter.PicClickListener;
import com.wyy.myhealth.utils.BingLog;
import com.wyy.myhealth.utils.NoticeUtils;
import com.wyy.myhealth.welcome.WelcomeActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

public class PublishActivity extends BaseActivity implements PicClickListener {

	private static final String TAG = PublishActivity.class.getSimpleName();
	private GridView publishGridView;
	private PublishAdapter publishAdapter;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private EditText moodEditText;
	private String moodid = "";
	private RatingBar moodRatingBar;
	private String moodIndex = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish);
		context = this;
		initView();
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(R.string.publish);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.publish_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case R.id.action_send:
			sendMoodaPic2Shai();
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
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
						try {
							moodIndex = "" + (int) rating;
						} catch (Exception e) {
							// TODO: handle exception
						}

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

		
		Intent intent = getIntent();
		handelIntent(intent);
		
	}

	@Override
	public void onPicClick(int position) {
		// TODO Auto-generated method stub
		if (position == list.size() - 1 && position < 4) {
			PhotoUtils.secPic(PublishActivity.this);
		}
	}

	private void sendMoodaPic2Shai() {

		if (WyyApplication.getInfo()==null) {
			WelcomeActivity.getPersonInfo(context);
		}
		
		if (WyyApplication.getInfo()==null) {
			startActivity(new Intent(context, WelcomeActivity.class));
			return;
		}
		
		moodEditText.setError(null);
		if (!TextUtils.isEmpty(moodEditText.getText().toString())) {

			if (TextUtils.isEmpty(moodIndex)) {
				HealthHttpClient.shaiPostAired(
						WyyApplication.getInfo().getId(), moodEditText
								.getText().toString(), postMoodHandler);
			} else {
				HealthHttpClient.shaiPostAired(
						WyyApplication.getInfo().getId(), moodEditText
								.getText().toString(), moodIndex,
						postMoodHandler);
			}

		} else {
			moodEditText.setError(getString(R.string.nullcontentnotice));
			moodEditText.requestFocus();
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
			BingLog.i(TAG, "发送返回:"+content);
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

	private void handleSendMultipleImages(Intent intent) {
		// TODO Auto-generated method stub

		String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
		if (!TextUtils.isEmpty(sharedText)) {
			moodEditText.setText(sharedText);
		}

		ArrayList<Uri> imageUris = intent
				.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
		if (imageUris != null) {
			// Update UI to reflect multiple images being shared
			for (int i = 0; i < imageUris.size(); i++) {
				String path = PhotoUtils.getPicPathFromUri(imageUris.get(i),
						this);
				BingLog.i(TAG, "路径:" + path);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("url", path);
				list.add(0, map);
			}

		}

		publishAdapter.notifyDataSetChanged();

	}

	private void handelIntent(Intent intent) {
		String action = intent.getAction();
		String type = intent.getType();
		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if ("text/plain".equals(type)) {
				handleSendText(intent); // Handle text being sent
			} else if (type.startsWith("image/")) {
				handleSendImage(intent); // Handle single image being sent
			}
		} else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
			if (type.startsWith("image/")) {
				handleSendMultipleImages(intent); // Handle multiple images
													// being sent
			}
		} else {
			// Handle other intents, such as being started from the home screen
		}
	}

	private void handleSendImage(Intent intent) {
		// TODO Auto-generated method stub
		handleSendText(intent);
		Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
		if (imageUri != null) {
			// Update UI to reflect image being shared
			String path = PhotoUtils.getPicPathFromUri(imageUri, this);
			BingLog.i(TAG, "路径:" + path);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("url", path);
			list.add(0, map);
			publishAdapter.notifyDataSetChanged();
		}
	}

	private void handleSendText(Intent intent) {
		// TODO Auto-generated method stub
		String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
		if (!TextUtils.isEmpty(sharedText)) {
			moodEditText.setText(sharedText);
		}
	}

}
