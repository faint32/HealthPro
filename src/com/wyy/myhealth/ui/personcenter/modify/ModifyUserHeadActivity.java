package com.wyy.myhealth.ui.personcenter.modify;

import android.annotation.TargetApi;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Window;

import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.imag.utils.LoadImageUtils;
import com.wyy.myhealth.imag.utils.PhoneUtlis;
import com.wyy.myhealth.ui.baseactivity.SubmitActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.ui.photoview.PhotoView;
import com.wyy.myhealth.utils.BingLog;

public class ModifyUserHeadActivity extends SubmitActivity implements
		ActivityInterface {

	private PhotoView photoView;

	private String path;

	private String headstr;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_head);
		initView();
		initData();
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(R.string.modify_head);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			actionBar.setBackgroundDrawable(new ColorDrawable(getResources()
					.getColor(R.color.themetranscolor)));
			actionBar.setSplitBackgroundDrawable(new ColorDrawable(
					getResources().getColor(R.color.themetranscolor)));
		}
	}

	@Override
	protected void submitMsg() {
		// TODO Auto-generated method stub
		sendModify();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		photoView = (PhotoView) findViewById(R.id.photo);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		path = getIntent().getStringExtra("path");
		LoadImageUtils.loadSdImage4ImageV(photoView, path);
		new Thread(runnable).start();
	}

	private void sendModify() {
		try {
			PersonalInfo personalInfo = WyyApplication.getInfo();
			HealthHttpClient.doHttpFinishPersonInfoForHead(personalInfo,
					headstr, new ModifyPicHandler(context));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			BingLog.e(ModifyBaseInfoActivity.class.getSimpleName(), "·¢ËÍ³ö´í");
		}
	}

	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			headstr = PhoneUtlis.bitmapNCutToString(path);
		}
	};

}
