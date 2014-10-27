package com.wyy.myhealth.ui.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.ui.baseactivity.SubmitActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.utils.NoticeUtils;
import com.wyy.myhealth.utils.PhoneInfoUtils;

public class FeedbackActivity extends SubmitActivity implements
		ActivityInterface {

	private EditText content;

	private EditText contacts;

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.feedback);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_back);
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
	protected void submitMsg() {
		// TODO Auto-generated method stub
		String contentstr = content.getText().toString();
		String contactstr = contacts.getText().toString();

		if (TextUtils.isEmpty(contentstr)) {
			content.setError(getString(R.string.nullcontentnotice));
			content.requestFocus();
			return;
		}

		if (TextUtils.isEmpty(contactstr)) {
			contacts.setError(getString(R.string.nullcontentnotice));
			contacts.requestFocus();
			return;
		}

		HealthHttpClient.doHttpFeedBack(WyyApplication.getInfo().getId(),
				contentstr, contactstr, mHandler);

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		context = this;
		contacts = (EditText) findViewById(R.id.contacts);
		content = (EditText) findViewById(R.id.content);
		initData();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		try {
			contacts.setText("" + PhoneInfoUtils.getPhoneNum(context));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			NoticeUtils.notice(context,
					context.getResources().getString(R.string.send),
					ConstantS.PUBLISH_COMMENT);
			finish();
		}

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			NoticeUtils.removeNotice(ConstantS.PUBLISH_COMMENT, context);
			NoticeUtils.showSuccessfulNotification(context);
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);
			NoticeUtils.removeNotice(ConstantS.PUBLISH_COMMENT, context);
			NoticeUtils.showFailePublish(context);
		}

	};

}
