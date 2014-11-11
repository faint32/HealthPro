package com.wyy.myhealth.ui.personcenter.modify;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.ui.baseactivity.SubmitActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.ui.personcenter.modify.utils.AgeUtils;
import com.wyy.myhealth.utils.BingLog;
import com.wyy.myhealth.utils.InputUtlity;

public class ModifyAgeActivity extends SubmitActivity implements
		ActivityInterface {
	private static final String TAG = ModifyAgeActivity.class.getSimpleName();
	private PersonalInfo info;
	private EditText userageEditText;
	private String userage = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_age);
		initView();
		initData();
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.age);
	}

	@Override
	protected void submitMsg() {
		// TODO Auto-generated method stub
		userageEditText.setError(null);
		userage = userageEditText.getText().toString();
		if (TextUtils.isEmpty(userage)) {
			userageEditText.setError(getString(R.string.agenullnotice));
			userageEditText.requestFocus();
			return;
		} else if (!AgeUtils.isAge(userage)) {
			userageEditText.setError(getString(R.string.agenotice));
			userageEditText.requestFocus();
			return;
		}
		info.setAge(userage);
		try {
			HealthHttpClient.doHttpFinishPersonInfo(info, new ModifyPicHandler(
					context));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			BingLog.e(TAG, "·¢ËÍ³ö´í");
		}
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		userageEditText = (EditText) findViewById(R.id.age_edit);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		info = WyyApplication.getInfo();
		if (info == null) {
			return;
		}
		userageEditText.setText(info.getAge());
		userageEditText.setSelection(userageEditText.getText().toString()
				.length());
		mHandler.sendEmptyMessageDelayed(0, DELAY_TIME);
	}

	@Override
	protected void showInput() {
		// TODO Auto-generated method stub
		super.showInput();
		InputUtlity.showInputWindow(context, userageEditText);
	}

}
