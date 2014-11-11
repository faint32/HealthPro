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
import com.wyy.myhealth.utils.InputUtlity;

public class ModifyUsernameActivity extends SubmitActivity implements
		ActivityInterface {

	private static final String TAG = ModifyUsernameActivity.class
			.getSimpleName();
	private EditText usernamEditText;
	private String username = "";
	private PersonalInfo info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_username);
		initView();
		initData();
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.modify_niname);
	}

	@Override
	protected void submitMsg() {
		// TODO Auto-generated method stub
		usernamEditText.setError(null);
		username = usernamEditText.getText().toString();
		if (TextUtils.isEmpty(username)) {
			usernamEditText.setError(getString(R.string.nichengnotice));
			usernamEditText.requestFocus();
			return;
		}
		info.setUsername(username);
		HealthHttpClient.doHttpFinishPersonInfo(info, new ModifyPicHandler(
				context));
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		usernamEditText = (EditText) findViewById(R.id.niname_edit);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		info = WyyApplication.getInfo();
		if (info == null) {
			return;
		}
		username = info.getUsername();
		usernamEditText.setText("" + username);
		usernamEditText.setSelection(usernamEditText.getText().toString()
				.length());
		mHandler.sendEmptyMessageDelayed(0, DELAY_TIME);
	}

	@Override
	protected void showInput() {
		// TODO Auto-generated method stub
		super.showInput();
		InputUtlity.showInputWindow(context, usernamEditText);
	}

}
