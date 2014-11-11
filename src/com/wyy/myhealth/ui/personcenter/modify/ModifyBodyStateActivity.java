package com.wyy.myhealth.ui.personcenter.modify;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.ui.baseactivity.SubmitActivity;
import com.wyy.myhealth.utils.InputUtlity;

public class ModifyBodyStateActivity extends SubmitActivity {

	private EditText mBodyIndex;
	private PersonalInfo info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_tag);
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
		getSupportActionBar().setTitle(R.string.body_state);
	}

	@Override
	protected void submitMsg() {
		// TODO Auto-generated method stub

		if (TextUtils.isEmpty(mBodyIndex.getText().toString().trim())) {
			mBodyIndex.setError(getString(R.string.nullcontentnotice));
			mBodyIndex.requestFocus();
			return;
		}

		com.wyy.myhealth.http.utils.HealthHttpClient
				.doHttpFinishPersonInfoForTag(info, mBodyIndex.getText()
						.toString(), new ModifyHandler(context));

	}

	private void initData() {
		if (WyyApplication.getInfo() == null) {
			return;
		}
		info = WyyApplication.getInfo();
		mBodyIndex.setText(info.getBodyindex());
		mBodyIndex.setSelection(mBodyIndex.getText().toString().length());
		mHandler.sendEmptyMessageDelayed(0, DELAY_TIME);
	}

	private void initView() {
		mBodyIndex = (EditText) findViewById(R.id.tab_editText1);
		initData();
	}

	@Override
	protected void showInput() {
		// TODO Auto-generated method stub
		super.showInput();
		InputUtlity.showInputWindow(context, mBodyIndex);
	}

}
