package com.wyy.myhealth.ui.login;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.wyy.myhealth.MainActivity;
import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.bean.TencentTokenBean;
import com.wyy.myhealth.bean.TencentUserInfoBean;
import com.wyy.myhealth.bean.UserAccountBean;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.db.utils.AccountUtils;
import com.wyy.myhealth.http.JsonHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.http.utils.JsonUtils;
import com.wyy.myhealth.imag.utils.Bmprcy;
import com.wyy.myhealth.imag.utils.PhoneUtlis;
import com.wyy.myhealth.imag.utils.PhotoUtils;
import com.wyy.myhealth.ui.login.AccountAdapter.OnAccountClickListener;
import com.wyy.myhealth.utils.BingLog;
import com.wyy.myhealth.utils.CharacterParser;
import com.wyy.myhealth.utils.ImageUtil;
import com.wyy.myhealth.utils.ListUtils;
import com.wyy.myhealth.utils.MD5;
import com.wyy.myhealth.utils.SavePersonInfoUtlis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

	private ImageView userHeadImageView;

	private Bitmap headBitmap = null;

	private PersonalInfo info;

	private Activity context;

	private Tencent mTencent;

	private UserInfo userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		context = this;
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

	private void initUI() {
		userHeadImageView = (ImageView) findViewById(R.id.head_img);
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
		findViewById(R.id.qq_login_btn).setOnClickListener(listener);
		passwordEditText.setOnClickListener(listener);
		accountListV.setOnItemClickListener(this);

		getAccount();
		initData();
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
			case R.id.qq_login_btn:
				qqLogin();
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
			accounEditText.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake));
			return;
		}

		String password = passwordEditText.getText().toString();
		passwordEditText.setError(null);
		if (TextUtils.isEmpty(password)) {
			passwordEditText.setError(getString(R.string.nullcontentnotice));
			passwordEditText.requestFocus();
			passwordEditText.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake));
			return;
		}

		BingLog.i(TAG, "ÃÜÂë:" + password);

		HealthHttpClient.loginByphone(accountString, MD5.MD5jm(password),
				handler);

	}

	public class RegistHander extends JsonHttpResponseHandler {

		@Override
		public void onSuccess(JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(response);
		}

		@Override
		public void onFailure(Throwable e, JSONArray errorResponse) {
			// TODO Auto-generated method stub
			super.onFailure(e, errorResponse);
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			super.onFinish();
			startMainActivity();
		}

	};

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

	private JsonHttpResponseHandler handler2 = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(response);
			BingLog.i(TAG, "µÇÂ½·µ»Ø:" + response);
			parseJson2(response);
		}

		@Override
		public void onFailure(Throwable e, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			super.onFailure(e, errorResponse);
			Toast.makeText(LoginActivity.this, R.string.net_erro,
					Toast.LENGTH_SHORT).show();
			loginProgressDialog.dismiss();
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

	private void parseJson2(JSONObject response) {
		try {
			if (JsonUtils.isSuccess(response)) {
				JSONObject object = response.getJSONObject("user");
				PersonalInfo info = JsonUtils.getInfo(object);
				WyyApplication.setInfo(info);
				SavePersonInfoUtlis.setPersonInfo(info, LoginActivity.this);
				if (null != info) {
					if (TextUtils.isEmpty(info.getUsername())
							|| TextUtils.isEmpty(info.getHeadimage())) {
						updateUserInfo();
					} else {
						startMainActivity();
					}

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

	private void initData() {
		info = SavePersonInfoUtlis.getPersonInfo(context);
		if (info == null) {
			return;
		}

		if (!TextUtils.isEmpty(info.getHeadimage())) {
			if (WyyApplication.getHeaderImaList() == null) {
				return;
			}
			if (WyyApplication.getHeaderImaList().size() > 0) {
				for (int i = 0; i < WyyApplication.getHeaderImaList().size(); i++) {
					if (WyyApplication.getHeaderImaList().get(i).getImaname()
							.equals(info.getUsername())) {
						headBitmap = BitmapFactory.decodeFile(WyyApplication
								.getHeaderImaList().get(i).getImapath());
						userHeadImageView.setImageBitmap(headBitmap);
						return;
					}
				}
			}

			if (null == headBitmap) {
				new Thread(loadImgRunnable).start();
			}

		}
	}

	private Runnable loadImgRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				String headimage = HealthHttpClient.IMAGE_URL
						+ info.getHeadimage();
				headBitmap = ImageUtil.getBitmapFromUrl(headimage);
				headBitmap = Bmprcy.toRoundBitmap(headBitmap);
				loadBitmap.sendEmptyMessage(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	private Handler loadBitmap = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (null != headBitmap) {
				try {
					userHeadImageView.setImageBitmap(headBitmap);
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

			return false;
		}
	});

	private void qqLogin() {
		if (mTencent == null) {
			mTencent = Tencent.createInstance(ConstantS.TENCENT_APP_ID,
					getApplicationContext());
		}
		if (mTencent != null && !mTencent.isSessionValid()) {
			mTencent.login(context, "all", loginIUiListener);
		}
	}

	private IUiListener loginIUiListener = new LoginQQListenter() {

		@Override
		protected void doComplete(JSONObject values) {
			// TODO Auto-generated method stub
			super.doComplete(values);
			initOpenidAndToken(values);
		}

	};

	private class LoginQQListenter implements IUiListener {

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onComplete(Object arg0) {
			// TODO Auto-generated method stub
			if (null == arg0) {
				Toast.makeText(context, R.string.loginfailure,
						Toast.LENGTH_SHORT).show();
				return;
			}

			JSONObject jsonResponse = (JSONObject) arg0;
			if (null != jsonResponse && jsonResponse.length() == 0) {
				Toast.makeText(context, R.string.loginfailure,
						Toast.LENGTH_SHORT).show();
				return;
			}
			doComplete((JSONObject) arg0);
		}

		protected void doComplete(JSONObject values) {

		}

		@Override
		public void onError(UiError arg0) {
			// TODO Auto-generated method stub

		}

	}

	private void initOpenidAndToken(JSONObject values) {
		try {
			TencentTokenBean tencentTokenBean = JsonUtils
					.getTencentTokenBean(values);
			if (!TextUtils.isEmpty(tencentTokenBean.getAccess_token())
					&& !TextUtils.isEmpty(tencentTokenBean.getExpires_in())
					&& !TextUtils.isEmpty(tencentTokenBean.getOpenid())) {
				mTencent.setAccessToken(tencentTokenBean.getAccess_token(),
						tencentTokenBean.getExpires_in());
				mTencent.setOpenId(tencentTokenBean.getOpenid());
				HealthHttpClient.registViaQQ(mTencent.getOpenId(), handler2);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void updateUserInfo() {
		if (mTencent != null && mTencent.isSessionValid()) {
			userInfo = new UserInfo(context, mTencent.getQQToken());
			userInfo.getUserInfo(getUserInfoListener);
		}
	}

	private IUiListener getUserInfoListener = new LoginQQListenter() {

		@Override
		protected void doComplete(JSONObject values) {
			// TODO Auto-generated method stub
			super.doComplete(values);
			TencentUserInfoBean tencentUserInfoBean = JsonUtils
					.geTencentUserInfoBean(values);
			getInfo2WyyInfo(tencentUserInfoBean);
		}

	};

	private void getInfo2WyyInfo(final TencentUserInfoBean tencentUserInfoBean) {
		if (tencentUserInfoBean == null) {
			startMainActivity();
			return;
		}
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {
					Bitmap mBitmap = PhotoUtils.getbitmap(tencentUserInfoBean
							.getFigureurl_qq_2());
					PersonalInfo personalInfo = WyyApplication.getInfo();
					personalInfo.setUsername(tencentUserInfoBean.getNickname());
					personalInfo.setHeadimage(PhoneUtlis
							.bitmapToString(mBitmap));
					HealthHttpClient.doHttpFinishPersonInfoForName_(
							personalInfo, handler);
				} catch (Exception e) {
					// TODO: handle exception
					startMainActivity();
				}

			}

		}.start();

	}

}
