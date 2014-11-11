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
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.ui.personcenter.modify.utils.HwUtlis;
import com.wyy.myhealth.utils.InputUtlity;

public class ModifyHeightActivity extends SubmitActivity implements
		ActivityInterface {

	private EditText mHeight;
	private PersonalInfo info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_height);
		initView();
	}

	@Override
	protected void submitMsg() {
		// TODO Auto-generated method stub
		mHeight.setError(null);
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

		info.setHeight(he);

		HealthHttpClient.doHttpFinishPersonInfo(info,
				new ModifyHandler(context));
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.height);
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
	public void initView() {
		// TODO Auto-generated method stub
		mHeight = (EditText) findViewById(R.id.h_editText);
		initData();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		if (WyyApplication.getInfo() == null) {
			return;
		}
		info = WyyApplication.getInfo();
		mHeight.setText(info.getHeight());
		mHeight.setSelection(mHeight.getText().toString().length());
		mHandler.sendEmptyMessageDelayed(0, DELAY_TIME);
	}
	
	@Override
	protected void showInput() {
		// TODO Auto-generated method stub
		super.showInput();
		InputUtlity.showInputWindow(context, mHeight);
	}
	

}
