package com.wyy.myhealth.ui.scan;

import android.support.v7.app.ActionBar;

import com.wyy.myhealth.R;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;

public class ScanFoodActivity extends BaseActivity {

	public static int angle = 0;// Í¼ÏñÐý×ª½Ç¶È

	@Override
	protected void onInitFragment() {
		// TODO Auto-generated method stub
		super.onInitFragment();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.wrapper, ScanFragment.newInstance(0)).commit();
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(R.string.saoyy);
	}

}
