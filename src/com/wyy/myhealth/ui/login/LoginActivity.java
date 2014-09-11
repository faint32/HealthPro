package com.wyy.myhealth.ui.login;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.wyy.myhealth.MainActivity;
import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.bean.UserAccountBean;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.db.utils.AccountUtils;
import com.wyy.myhealth.http.JsonHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.http.utils.JsonUtils;
import com.wyy.myhealth.ui.login.AccountAdapter.OnAccountClickListener;
import com.wyy.myhealth.utils.BingLog;
import com.wyy.myhealth.utils.CharacterParser;
import com.wyy.myhealth.utils.ListUtils;
import com.wyy.myhealth.utils.MD5;
import com.wyy.myhealth.utils.SavePersonInfoUtlis;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity implements
		OnItemClickListener, OnAccountClickListener {

	private static final String TAG = LoginActivity.class.getSimpleName();

	private Button loginButton;

	private EditText accounEditText;

	private EditText passwordEditText;

	private ListView accountListV;

	private List<UserAccountBean> realList = new ArrayList<>();

	private List<UserAccountBean> filterList = new ArrayList<>();

	private AccountUtils accountUtils;

	private AccountAdapter accountAdapter;

	private CharacterParser characterParser;

	private ProgressDialog loginProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(getResources().getColor(R.color.themecolor)));
		initUI();
		initFilter();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	private void initUI() {
		loginButton = (Button) findViewById(R.id.login_btn);
		accounEditText = (EditText) findViewById(R.id.account_edit);
		passwordEditText = (EditText) findViewById(R.id.password_edit);
		accountListV = (ListView) findViewById(R.id.account_list);
		loginProgressDialog = new ProgressDialog(LoginActivity.this);
		loginProgressDialog.setMessage(getString(R.string.logining));

		accounEditText.addTextChangedListener(accountWatcher);
		loginButton.setOnClickListener(listener);
		findViewById(R.id.weimalogin).setOnClickListener(listener);
		findViewById(R.id.regist).setOnClickListener(listener);
		findViewById(R.id.login_ScrollView).setOnClickListener(listener);
		findViewById(R.id.login_Liner).setOnClickListener(listener);
		passwordEditText.setOnClickListener(listener);
		accountListV.setOnItemClickListener(this);

		getAccount();

	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.login_btn:
				attemptLogin();
				break;

			case R.id.weimalogin:
				startActivity(new Intent(LoginActivity.this,
						MicroCodeLoginActivity.class));
				break;

			case R.id.regist:
				startActivity(new Intent(LoginActivity.this,
						RegistActivity.class));
				break;

			case R.id.login_ScrollView:
			case R.id.password_edit:
			case R.id.login_Liner:
				hideList();
				break;

			default:
				break;
			}
		}
	};

	private void initFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConstantS.ACTION_LOGIN_FINISH);
		registerReceiver(receiver, filter);
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			LoginActivity.this.finish();
		}
	};

	@SuppressWarnings("unchecked")
	private void getAccount() {
		if (accountUtils == null) {
			accountUtils = new AccountUtils(LoginActivity.this);
		}
		realList = (List<UserAccountBean>) accountUtils.queryData();
		if (realList != null) {
			filterList = realList;
		}

		accountAdapter = new AccountAdapter(filterList, getLayoutInflater());
		accountListV.setAdapter(accountAdapter);
		accountAdapter.setListener(this);
	}

	private TextWatcher accountWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			search4Adapter(s.toString());
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

	private void search4Adapter(String key) {
		if (!accountListV.isShown()) {
			accountListV.setVisibility(View.VISIBLE);
		}
		if (characterParser == null) {
			characterParser = CharacterParser.getInstance();
		}
		filterList = new ArrayList<>();
		if (TextUtils.isEmpty(key)) {
			filterList = realList;
		} else {
			filterList.clear();
			for (UserAccountBean userAccountBean : realList) {
				String name = userAccountBean.getUsername();
				if (name.indexOf(key.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								key.toString())) {
					filterList.add(userAccountBean);
				}
			}
		}

		accountAdapter = new AccountAdapter(filterList, getLayoutInflater());
		accountListV.setAdapter(accountAdapter);
		ListUtils.setListViewHeightBasedOnChildren(accountListV);
		accountAdapter.notifyDataSetChanged();
		accountAdapter.setListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		try {
			passwordEditText.setText(filterList.get(position).getPassword());
			accounEditText.setText(filterList.get(position).getUsername());
			accountListV.setVisibility(View.GONE);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void deleteAccount(UserAccountBean userAccountBean) {
		for (int i = 0; i < realList.size(); i++) {
			if (realList.get(i).getUsername()
					.equals(userAccountBean.getUsername())) {
				realList.remove(i);
				break;
			}
		}

		for (int i = 0; i < filterList.size(); i++) {
			if (filterList.get(i).getUsername()
					.equals(userAccountBean.getUsername())) {
				filterList.remove(i);
				break;
			}
		}

		long del = accountUtils.delete(userAccountBean);
		BingLog.i(TAG, "É¾³ý:" + del);
		accountAdapter.notifyDataSetChanged();
	}

	@Override
	public void ondeleteAccount(int position) {
		// TODO Auto-generated method stub
		BingLog.i(TAG, "É¾³ý:" + position);
		deleteAccount(filterList.get(position));
	}

	private void hideList() {
		if (accountListV.isShown()) {
			accountListV.setVisibility(View.GONE);
		}
	}

	private void attemptLogin() {
		String accountString = accounEditText.getText().toString();
		accounEditText.setError(null);
		if (TextUtils.isEmpty(accountString)) {
			accounEditText.setError(getString(R.string.nullcontentnotice));
			accounEditText.requestFocus();
			return;
		}

		String password = passwordEditText.getText().toString();
		passwordEditText.setError(null);
		if (TextUtils.isEmpty(password)) {
			passwordEditText.setError(getString(R.string.nullcontentnotice));
			passwordEditText.requestFocus();
			return;
		}

		BingLog.i(TAG, "ÃÜÂë:" + password);

		HealthHttpClient.loginByphone(accountString, MD5.MD5jm(password),
				handler);

	}

	private JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(response);
			BingLog.i(TAG, "µÇÂ½·µ»Ø:" + response);
			parseJson(response);
		}

		@Override
		public void onFailure(Throwable e, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			super.onFailure(e, errorResponse);
			Toast.makeText(LoginActivity.this, R.string.net_erro,
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			loginProgressDialog.show();
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			super.onFinish();
			loginProgressDialog.dismiss();
		}

	};

	private void parseJson(JSONObject response) {
		try {
			if (JsonUtils.isSuccess(response)) {
				JSONObject object = response.getJSONObject("user");
				PersonalInfo info = JsonUtils.getInfo(object);
				if (null != info) {
					WyyApplication.setInfo(info);
					SavePersonInfoUtlis.setPersonInfo(info, LoginActivity.this);
					startMainActivity();
				}

			} else {
				Toast.makeText(LoginActivity.this, R.string.loginfailure,
						Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(LoginActivity.this, R.string.loginfailure,
					Toast.LENGTH_LONG).show();
		}
	}

	private void startMainActivity() {
		startActivity(new Intent(LoginActivity.this, MainActivity.class));
		finish();
	}

}
