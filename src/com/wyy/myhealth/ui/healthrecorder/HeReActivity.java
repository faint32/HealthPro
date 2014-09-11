package com.wyy.myhealth.ui.healthrecorder;

import android.os.Bundle;

import com.wyy.myhealth.R;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import android.support.v4.app.Fragment;

public class HeReActivity extends BaseActivity implements ActivityInterface {
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_re);
//		initView();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
//		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
//				.findFragmentById(R.id.navigation_drawer);
//		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
//				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
