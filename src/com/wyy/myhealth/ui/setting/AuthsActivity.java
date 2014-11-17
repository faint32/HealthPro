package com.wyy.myhealth.ui.setting;

import org.json.JSONException;
import org.json.JSONObject;

import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.http.BingHttpHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.http.utils.JsonUtils;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.utils.BingLog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.MenuItem;

@SuppressLint("NewApi")
public class AuthsActivity extends PreferenceActivity implements
		ActivityInterface {

	public static final String VISIT = "stranger_visit";

	public static final String VISIBLE = "near_visible";

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.wyy_preference);
		initView();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setTitle(R.string.quanxian_set);
			actionBar.setBackgroundDrawable(new ColorDrawable(getResources()
					.getColor(R.color.themecolor)));
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayShowHomeEnabled(false);
		}

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	@Deprecated
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		// TODO Auto-generated method stub
		if (preference.getKey().equals(VISIBLE)) {
			BingLog.i("附近人可见:"
					+ preference.getSharedPreferences().getBoolean(VISIBLE,
							false));
			updataAuthos(PreferenceManager.getDefaultSharedPreferences(this)
					.getBoolean(VISIT, false), preference
					.getSharedPreferences().getBoolean(VISIBLE, false));

		}

		if (preference.getKey().equals(VISIT)) {
			BingLog.i("可访问:"
					+ preference.getSharedPreferences()
							.getBoolean(VISIT, false));

			updataAuthos(
					preference.getSharedPreferences().getBoolean(VISIT, false),
					PreferenceManager.getDefaultSharedPreferences(this)
							.getBoolean(VISIBLE, false));

		}

		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	private void updataAuthos(boolean stranVisible, boolean nearVisible) {
		PersonalInfo personalInfo = WyyApplication.getInfo();
		personalInfo.setStranVisible(stranVisible);
		personalInfo.setNearVisible(nearVisible);
		HealthHttpClient.updataAuths(personalInfo, handler);
	}

	private BingHttpHandler handler = new BingHttpHandler() {

		@Override
		protected void onGetSuccess(JSONObject response) {
			// TODO Auto-generated method stub
			BingLog.i("权限返回:" + response);
			if (JsonUtils.isSuccess(response)) {
				try {
					PersonalInfo personalInfo = JsonUtils.getInfo(response
							.getJSONObject("user"));
					if (personalInfo != null) {
						WyyApplication.setInfo(personalInfo);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onGetFinish() {
			// TODO Auto-generated method stub

		}
	};

}
