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
/**
 * 扫描功能提示信息
 * @author lyl
 *
 */
public class ScanNavActivity extends Activity implements ActivityInterface {

	private static final String TAG = ScanNavActivity.class.getSimpleName();
	private FrameLayout scan_nav_0;
	private FrameLayout scan_nav_1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_nav);
		initView();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		setIsFristUse(ScanNavActivity.this, false);
	}
	
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		scan_nav_0 = (FrameLayout) findViewById(R.id.scan_nav_0);
		scan_nav_1 = (FrameLayout) findViewById(R.id.scan_nav_1);
		initData();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		findViewById(R.id.scan_nav).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (scan_nav_0.isShown()) {
					scan_nav_0.setVisibility(View.GONE);
					scan_nav_1.setVisibility(View.VISIBLE);
				} else {
					finish();
				}
			}
		});
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

}
