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
import com.wyy.myhealth.ui.personcenter.modify.utils.HwUtlis;
import com.wyy.myhealth.utils.InputUtlity;

public class ModifyWeightActivity extends SubmitActivity {
	private EditText mWeight;
	private PersonalInfo info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_weight);
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
		getSupportActionBar().setTitle(R.string.heriawei);
	}

	@Override
	protected void submitMsg() {
		// TODO Auto-generated method stub
		mWeight.setError(null);
		String we = mWeight.getText().toString();

		if (TextUtils.isEmpty(we)) {
			mWeight.setError(getString(R.string.nullcontentnotice));
			mWeight.requestFocus();
			return;
		} else if (!HwUtlis.isWeight(we)) {
			mWeight.setError(getString(R.string.weightnotice));
			mWeight.requestFocus();
			return;
		}
		info.setWeight(we);
		HealthHttpClient.doHttpFinishPersonInfo(info,
				new ModifyHandler(context));
	}

	private void initView() {
		mWeight = (EditText) findViewById(R.id.w_editText);
		initData();
	}

	private void initData() {
		info = WyyApplication.getInfo();
		mWeight.setText(info.getWeight());
		mWeight.setSelection(mWeight.getText().toString().length());
		mHandler.sendEmptyMessageDelayed(0, DELAY_TIME);
	}
	
	@Override
	protected void showInput() {
		// TODO Auto-generated method stub
		super.showInput();
		InputUtlity.showInputWindow(context, mWeight);
	}
	
}
