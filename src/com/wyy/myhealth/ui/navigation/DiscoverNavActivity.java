package com.wyy.myhealth.ui.navigation;

import com.wyy.myhealth.R;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

public class DiscoverNavActivity extends Activity implements ActivityInterface{

	private static final String TAG = DiscoverNavActivity.class.getSimpleName();
	private FrameLayout nav_hrLayout;
	private FrameLayout nav_icLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discover_nav);
		initView();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		setIsFristUse(DiscoverNavActivity.this, false);
	}

	public static boolean getIsFirstUse(Context context) {
		boolean isFirstUse = false;
		SharedPreferences sharedPreferences = context.getSharedPreferences(TAG,
				Context.MODE_PRIVATE);
		isFirstUse = sharedPreferences.getBoolean("isfirstuse", true);
		return isFirstUse;

	}

	public static void setIsFristUse(Context context, boolean isFirstUse) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(TAG,
				Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("isfirstuse", isFirstUse);
		editor.commit();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		nav_hrLayout=(FrameLayout)findViewById(R.id.discover_hr);
		nav_icLayout=(FrameLayout)findViewById(R.id.dis_nav_ice);
		initData();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		findViewById(R.id.discover_nav).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (nav_hrLayout.isShown()) {
					nav_hrLayout.setVisibility(View.GONE);
					nav_icLayout.setVisibility(View.VISIBLE);
				}else {
					finish();
				}
			}
		});
	}

}
