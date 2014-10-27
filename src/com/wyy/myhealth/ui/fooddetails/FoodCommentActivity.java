package com.wyy.myhealth.ui.fooddetails;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.ui.baseactivity.SubmitActivity;
import com.wyy.myhealth.utils.InputUtlity;
import com.wyy.myhealth.utils.NoticeUtils;

public class FoodCommentActivity extends SubmitActivity {

	private EditText content_text;
	private String foodid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_food);
		initView();
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.comment);
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
	
	
	private void initView() {
		foodid = getIntent().getStringExtra(ConstantS.ID);
		content_text = (EditText) findViewById(R.id.comment_edit);
		content_text.requestFocus();
		InputUtlity.showInputWindow(context, content_text);
	}

	@Override
	protected void submitMsg() {
		// TODO Auto-generated method stub
		submit();
	}

	private void submit() {
		// TODO Auto-generated method stub
		content_text.setError(null);
		String content = content_text.getText().toString();
		if (!TextUtils.isEmpty(content)) {
			HealthHttpClient.doHttpPostComment(foodid, content, "5",
					WyyApplication.getInfo().getId(), PostCommentHandler);
		} else {
			content_text.setError(getResources().getString(
					R.string.nullcontentnotice));
		}
	}

	/**
	 * 发送返回处理
	 */
	private AsyncHttpResponseHandler PostCommentHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			finish();
			NoticeUtils.notice(context,
					getResources().getString(R.string.send),
					ConstantS.PUBLISH_COMMENT);
		}

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			Log.i("还好还好", "返回:"+content);
			parseJson(content);
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);

			NoticeUtils.removeNotice(ConstantS.PUBLISH_COMMENT, context);
			NoticeUtils.showFailePublish(context);

		}

		public void onFinish() {
			super.onFinish();
		};

	};

	private void parseJson(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString(ConstantS.RESULT);
			if (result.equals("1")) {
				NoticeUtils.removeNotice(ConstantS.PUBLISH_COMMENT, context);
				NoticeUtils.showSuccessfulNotification(context);
			} else {
				NoticeUtils.removeNotice(ConstantS.PUBLISH_COMMENT, context);
				NoticeUtils.showFailePublish(context);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			NoticeUtils.removeNotice(ConstantS.PUBLISH_COMMENT, context);
			NoticeUtils.showFailePublish(context);
		}

	}

}
