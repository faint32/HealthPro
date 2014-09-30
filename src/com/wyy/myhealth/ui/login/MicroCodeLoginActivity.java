package com.wyy.myhealth.ui.login;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wyy.myhealth.MainActivity;
import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.http.utils.JsonUtils;
import com.wyy.myhealth.imag.utils.Bmprcy;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.utils.ImageUtil;
import com.wyy.myhealth.utils.SavePersonInfoUtlis;

public class MicroCodeLoginActivity extends BaseActivity implements
		ActivityInterface {

	private ImageView userHeadImageView;

	private EditText microcodEditText;

	private Button loginButton;

	// Í·Ïñ
	private Bitmap headBitmap = null;

	private PersonalInfo info;

	private ProgressDialog loginProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_micro_login);
		initView();
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.weimalogin);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		userHeadImageView = (ImageView) findViewById(R.id.home_head_ima);
		loginButton = (Button) findViewById(R.id.micro_code_login);
		microcodEditText = (EditText) findViewById(R.id.micro_code_edit);
		loginProgressDialog = new ProgressDialog(context);
		loginProgressDialog.setMessage(getString(R.string.logining));
		loginButton.setOnClickListener(listener);
		initData();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		info = SavePersonInfoUtlis.getPersonInfo(context);
		if (info == null) {
			return;
		}

		if (!TextUtils.isEmpty(info.getIdcode())) {
			microcodEditText.setText("" + info.getIdcode());
		}

		if (!TextUtils.isEmpty(info.getHeadimage())) {

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

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.micro_code_login:
				attempt();
				break;

			default:
				break;
			}
		}
	};

	private void attempt() {
		String idcode = microcodEditText.getText().toString();
		microcodEditText.setError(null);
		if (TextUtils.isEmpty(idcode)) {
			microcodEditText.setError(getString(R.string.nullcontentnotice));
			microcodEditText.requestFocus();
			return;
		}

		HealthHttpClient.doHttpLogin(idcode, handler);

	}

	private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

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

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			parseJson(content);
		}

		public void onFailure(Throwable error, String content) {
			Toast.makeText(context, R.string.loginfailure, Toast.LENGTH_LONG)
					.show();
		};

	};

	private void parseJson(String content) {
		if (TextUtils.isEmpty(content)) {
			Toast.makeText(context, R.string.loginfailure, Toast.LENGTH_LONG)
					.show();
		}
		try {
			JSONObject jsonObject = new JSONObject(content);
			if (JsonUtils.isSuccess(jsonObject)) {
				JSONObject object = jsonObject.getJSONObject("user");
				PersonalInfo info = JsonUtils.getInfo(object);
				if (null != info) {
					WyyApplication.setInfo(info);
					SavePersonInfoUtlis.setPersonInfo(info, context);
					startMainActivity();
				}

			} else {
				Toast.makeText(context, R.string.loginfailure,
						Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(context, R.string.loginfailure, Toast.LENGTH_LONG)
					.show();
		}
	}

	private void startMainActivity() {
		startActivity(new Intent(context, MainActivity.class));
		finish();
	}

}
