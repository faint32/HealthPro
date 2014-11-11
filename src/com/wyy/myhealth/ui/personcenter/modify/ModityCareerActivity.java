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

public class ModityCareerActivity extends SubmitActivity {

	private EditText Job;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carrer);
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
		Job = (EditText) findViewById(R.id.career_editText);
		if (WyyApplication.getInfo() == null) {
			return;
		}
		Job.setText("" + WyyApplication.getInfo().getJob());
		Job.setSelection(Job.getText().toString().length());
		mHandler.sendEmptyMessageDelayed(0, DELAY_TIME);
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.carre);
	}

	@Override
	protected void submitMsg() {
		// TODO Auto-generated method stub
		Job.setError(null);
		final PersonalInfo info = WyyApplication.getInfo();
		if (TextUtils.isEmpty(Job.getText().toString().trim())) {
			Job.setError(getString(R.string.nullcontentnotice));
			return;
		}

		HealthHttpClient.doHttpFinishPersonInfoForJob(info, Job.getText()
				.toString().trim(), new ModifyHandler(context));

	}

	@Override
	protected void showInput() {
		// TODO Auto-generated method stub
		super.showInput();
		InputUtlity.showInputWindow(context, Job);
	}

}
