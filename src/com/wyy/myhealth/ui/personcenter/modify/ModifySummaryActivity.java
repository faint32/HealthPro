package com.wyy.myhealth.ui.personcenter.modify;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.ui.baseactivity.SubmitActivity;
import com.wyy.myhealth.utils.InputUtlity;

public class ModifySummaryActivity extends SubmitActivity {

	private EditText summary_edit;
	private PersonalInfo info;
	private String summary;

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.remarks);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_summary);
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

	private void initView() {
		context = this;
		summary_edit = (EditText) findViewById(R.id.summary_editText);
		initData();
	}

	private void initData() {
		if (WyyApplication.getInfo() == null) {
			return;
		}
		info = WyyApplication.getInfo();
		summary_edit.setText("" + info.getSummary());
		summary_edit.setSelection(summary_edit.getText().toString().length());
		mHandler.sendEmptyMessageDelayed(0, DELAY_TIME);
	}

	@Override
	protected void submitMsg() {
		// TODO Auto-generated method stub

		summary_edit.setError(null);
		summary = summary_edit.getText().toString();

		if (TextUtils.isEmpty(summary)) {
			summary_edit.setError(getString(R.string.nullcontentnotice));
			summary_edit.requestFocus();
			return;
		}

		HealthHttpClient.doHttpFinishPersonInfoForSummary(info, summary,
				new ModifyHandler(context));

	}

	@Override
	protected void showInput() {
		// TODO Auto-generated method stub
		super.showInput();
		InputUtlity.showInputWindow(context, summary_edit);
	}

}
