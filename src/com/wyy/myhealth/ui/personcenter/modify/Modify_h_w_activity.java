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

public class Modify_h_w_activity extends SubmitActivity {

	private EditText mHeight;
	private EditText mWeight;
	private PersonalInfo info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_h_w);
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
		mHeight.setError(null);
		String we = mWeight.getText().toString();
		String he = mHeight.getText().toString();

		if (TextUtils.isEmpty(mHeight.getText().toString().trim())) {
			mHeight.setError(getString(R.string.nullcontentnotice));
			mHeight.requestFocus();
			return;
		} else if (!HwUtlis.isHeight(he)) {
			mHeight.setError(getString(R.string.heightnotice));
			mHeight.requestFocus();
			return;
		}

		if (TextUtils.isEmpty(we)) {
			mWeight.setError(getString(R.string.nullcontentnotice));
			mWeight.requestFocus();
			return;
		} else if (!HwUtlis.isWeight(we)) {
			mWeight.setError(getString(R.string.weightnotice));
			mWeight.requestFocus();
			return;
		}

		HealthHttpClient.doHttpFinishPersonInfoForHeight(info, he, we,
				new ModifyHandler(context));

	}

	private void initView() {
		mHeight = (EditText) findViewById(R.id.h_editText);
		mWeight = (EditText) findViewById(R.id.w_editText);
		initData();
	}

	private void initData() {
		info = WyyApplication.getInfo();
		mHeight.setText(info.getHeight());
		mWeight.setText(info.getWeight());
	}

}
