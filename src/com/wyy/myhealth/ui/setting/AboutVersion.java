package com.wyy.myhealth.ui.setting;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.wyy.myhealth.R;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.utils.UpdateAppUtils;

public class AboutVersion extends BaseActivity implements ActivityInterface{

	private TextView version;
	
	private TextView version_notice;
	
	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.versioninfo);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_version);
		initView();
	}

	
	private String getLoVersion() {

		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			String version = packageInfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		version=(TextView)findViewById(R.id.app_ver);
		version_notice=(TextView)findViewById(R.id.app_check_msg);
		findViewById(R.id.check_update_fr).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UpdateAppUtils.upDateApp(context);
			}
		});
		initData();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		version.setText(getString(R.string.app_name)+getLoVersion());
		HealthHttpClient.doHttpUpdateApp(handler);
	}
	
	
	private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			parseUrl(content);
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);
			Toast.makeText(context, R.string.net_erro, Toast.LENGTH_LONG).show();
		}

	};
	
	
	private void parseUrl(String content) {

		try {
			JSONObject jsonObject = new JSONObject(content);
			String versionNum = jsonObject.getJSONArray("versions")
					.getJSONObject(0).getString("versionNum");

			if (!versionNum.equals(getLoVersion())) {
				Toast.makeText(context, R.string.hasnewversion, Toast.LENGTH_LONG).show();
				version_notice.setText(R.string.hasnewversion);
//				newVersion.setText("×îÐÂ°æ±¾V" + versionNum);
			}else {
				Toast.makeText(context, R.string.is_alreadly_version, Toast.LENGTH_LONG).show();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
}
