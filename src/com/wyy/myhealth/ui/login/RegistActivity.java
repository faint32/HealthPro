package com.wyy.myhealth.ui.login;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.wyy.myhealth.MainActivity;
import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.bean.UserAccountBean;
import com.wyy.myhealth.db.utils.AccountUtils;
import com.wyy.myhealth.http.JsonHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.http.utils.JsonUtils;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.utils.BingLog;
import com.wyy.myhealth.utils.MD5;
import com.wyy.myhealth.utils.PhoneInfoUtils;
import com.wyy.myhealth.utils.SavePersonInfoUtlis;

public class RegistActivity extends BaseActivity implements ActivityInterface {

	private static final String TAG = RegistActivity.class.getSimpleName();

	private EditText phone_edit;

	private EditText passWordo;

	private EditText passWordt;

	private ProgressDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		initView();
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.regeit);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		loadingDialog = new ProgressDialog(context);
		loadingDialog.setMessage(getString(R.string.regeiting));
		phone_edit = (EditText) findViewById(R.id.phone_edit);
		passWordo = (EditText) findViewById(R.id.one_password_edit);
		passWordt = (EditText) findViewById(R.id.tow_password_edit);

		findViewById(R.id.regist_btn).setOnClickListener(listener);

		passWordo.addTextChangedListener(passwordoWatcher);

		passWordt.addTextChangedListener(passwordtWatcher);

		initData();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		try {
			phone_edit.setText("" + PhoneInfoUtils.getPhoneNumCut86(context));
		} catch (Exception e) {
			// TODO: handle exception
		}
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
	
	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.regist_btn:
				registByPhone();
				break;

			default:
				break;
			}
		}
	};

	
	
	
	private TextWatcher passwordoWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			BingLog.i(TAG, "daxiao:" + s.length());
			if (s.length() < 6) {
				passWordo.setError(getString(R.string.password_notice_o));
			} else {
				passWordo.setError(null);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}
	};

	private TextWatcher passwordtWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			if (TextUtils.isEmpty(passWordo.getText().toString())) {
				passWordo.setError(getString(R.string.password_notice_null));
				passWordo.requestFocus();
			} else {
				if (s.toString().equals(passWordo.getText().toString())) {
					passWordt.setError(null);
				} else {
					passWordt.setError(getString(R.string.password_notice_t));
				}
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}
	};

	private void registByPhone() {
		String phone = phone_edit.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			phone_edit.setError(getString(R.string.nullcontentnotice));
			phone_edit.requestFocus();
			return;
		} else if (!PhoneInfoUtils.isPhoneNum(phone)) {
			phone_edit.setError(getString(R.string.phone_notice_));
			phone_edit.requestFocus();
			return;
		}

		String passwordostr = passWordo.getText().toString();
		String passwordtstr = passWordt.getText().toString();
		if (TextUtils.isEmpty(passwordostr)) {
			passWordo.setError(getString(R.string.nullcontentnotice));
			passWordo.requestFocus();
			return;
		}

		if (!passwordostr.equals(passwordtstr)) {
			passWordt.setError(getString(R.string.password_notice_t));
			passWordt.requestFocus();
			return;
		}

		HealthHttpClient.registerByphone(phone, MD5.MD5jm(passwordtstr),
				handler);

	}

	private JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(response);
			parseJson(response);
		}

		@Override
		public void onFailure(Throwable e, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			super.onFailure(e, errorResponse);
			Toast.makeText(context, R.string.net_erro, Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			loadingDialog.show();
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			super.onFinish();
			loadingDialog.dismiss();
		}

	};

	private void parseJson(JSONObject response) {
		BingLog.i(TAG, "×¢²á·µ»Ø:"+response);
		if (JsonUtils.isSuccess(response)) {
			JSONObject object;
			try {
				object = response.getJSONObject("user");
				PersonalInfo info = JsonUtils.getInfo(object);
				if (null != info) {
					WyyApplication.setInfo(info);
					SavePersonInfoUtlis.setPersonInfo(info, context);
					startMainActivity();
				} else {
					Toast.makeText(context, R.string.regeit_failure,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(context, R.string.regeit_failure,
						Toast.LENGTH_SHORT).show();
			}

		} else {
			Toast.makeText(context, R.string.regeit_failure, Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void startMainActivity() {

		UserAccountBean userAccountBean = new UserAccountBean();
		userAccountBean.setPassword(passWordt.getText().toString());
		userAccountBean.setUsername(phone_edit.getText().toString());
		
		long update=new AccountUtils(context).update(userAccountBean);
		BingLog.i(TAG, "¸üÐÂ:"+update);
		BingLog.i(TAG, "×¢²áÃÜÂë:"+passWordt.getText().toString());
		if (update<1) {
			new AccountUtils(context).insert(userAccountBean);
		}

		startActivity(new Intent(context, MainActivity.class));
		finish();
	}

}
