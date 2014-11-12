package com.wyy.myhealth.ui.personcenter;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.LevelBean;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.http.BingHttpHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.http.utils.JsonUtils;
import com.wyy.myhealth.imag.utils.LoadImageUtils;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;

public class PersonHornorActivity extends BaseActivity implements
		ActivityInterface {

	private ImageView hornorHead;

	private TextView hornorName;

	private TextView hornorMoney;

	private ProgressBar loadingBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hornor);
		initView();
		initData();
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(R.string.myhonor);
		loadingBar = new ProgressBar(this);
		actionBar.setCustomView(loadingBar, new ActionBar.LayoutParams(
				Gravity.RIGHT));
		actionBar.setDisplayShowCustomEnabled(true);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		hornorHead = (ImageView) findViewById(R.id.hornor_img);
		hornorName = (TextView) findViewById(R.id.hornor_name);
		hornorMoney = (TextView) findViewById(R.id.hornor_value);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		if (WyyApplication.getInfo() == null) {
			return;
		}
		LevelBean levelBean = WyyApplication.getInfo().getLevelBean();
		if (levelBean != null) {
			LoadImageUtils.loadWebImageV_Min(hornorHead,
					HealthHttpClient.IMAGE_URL + levelBean.getImg());
			hornorName.setText(levelBean.getName());
		}

		hornorMoney.setText(WyyApplication.getInfo().getMoney());
		getMyLevel();
	}

	private void getMyLevel() {
		HealthHttpClient.getUserLevel(WyyApplication.getInfo().getId(),
				new BingHttpHandler() {

					@Override
					protected void onGetSuccess(JSONObject response) {
						// TODO Auto-generated method stub
						if (JsonUtils.isSuccess(response)) {
							PersonalInfo personalInfo = WyyApplication
									.getInfo();
							try {
								LevelBean levelBean = JsonUtils
										.getLevelBean(response
												.getJSONObject("level"));
								personalInfo.setLevelBean(levelBean);
								WyyApplication.setInfo(personalInfo);
								if (levelBean != null) {
									LoadImageUtils.loadWebImageV_Min(
											hornorHead,
											HealthHttpClient.IMAGE_URL
													+ levelBean.getImg());
									hornorName.setText(levelBean.getName());
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
						loadingBar.setVisibility(View.GONE);
					}
				});
	}

}
