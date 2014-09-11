package com.wyy.myhealth;

import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.service.MainService;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.ui.healthrecorder.RecorderChatFragment;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class HealthReActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks, ActivityInterface {

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

	private TextView recored_num_txt;

	private TextView array_num_txt;

	private TextView reason_num_txt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_re);
		initActionBar();
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		initView();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.health_recorder, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_share) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources()
				.getColor(R.color.themecolor)));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setLogo(new ColorDrawable(R.color.transparent));
//		actionBar.setDisplayUseLogoEnabled(false);
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		swithRecorder(position);
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case ConstantS.YINSHI:
			mTitle = getString(R.string.yinshihelijilu);
			break;
		case ConstantS.YUNDONG:
			mTitle = getString(R.string.yundongreliangjilu);
			break;

		case ConstantS.TANGLEI:
			mTitle = getString(R.string.tanglleisherujilu);
			break;

		case ConstantS.ZHIFANG:
			mTitle = getString(R.string.zhifangsherujilu);
			break;

		case ConstantS.DANGBAIZHI:
			mTitle = getString(R.string.danbaizhisherujilu);
			break;

		case ConstantS.WEISHENGSU:
			mTitle = getString(R.string.vsehgnlusherujilu);
			break;

		case ConstantS.KUANGWUZHI:
			mTitle = getString(R.string.kuangwuzhisherujilu);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	private void swithRecorder(int id) {

		FragmentManager fragmentManager = getSupportFragmentManager();
		switch (id) {
		case 0:
			fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							RecorderChatFragment.newInstance(ConstantS.YINSHI))
					.commit();
			// getSupportActionBar().setTitle(R.string.yinshihelijilu);
			// mTitle = getString(R.string.yinshihelijilu);
			break;

		case 1:

			fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							RecorderChatFragment.newInstance(ConstantS.YUNDONG))
					.commit();
			// mTitle = getString(R.string.yundongreliangjilu);
			// getSupportActionBar().setTitle(R.string.yundongreliangjilu);
			break;

		case 2:

			fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							RecorderChatFragment.newInstance(ConstantS.ZHIFANG))
					.commit();
			// mTitle = getString(R.string.zhifangsherujilu);
			// getSupportActionBar().setTitle(R.string.zhifangsherujilu);
			break;

		case 3:

			fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							RecorderChatFragment.newInstance(ConstantS.TANGLEI))
					.commit();
			// mTitle = getString(R.string.tanglleisherujilu);
			// getSupportActionBar().setTitle(R.string.tanglleisherujilu);
			break;

		case 4:

			fragmentManager
					.beginTransaction()
					.replace(
							R.id.container,
							RecorderChatFragment
									.newInstance(ConstantS.DANGBAIZHI))
					.commit();
			// mTitle = getString(R.string.danbaizhisherujilu);
			// getSupportActionBar().setTitle(R.string.danbaizhisherujilu);
			break;

		case 5:

			fragmentManager
					.beginTransaction()
					.replace(
							R.id.container,
							RecorderChatFragment
									.newInstance(ConstantS.WEISHENGSU))
					.commit();
			// mTitle = getString(R.string.vsehgnlusherujilu);
			// getSupportActionBar().setTitle(R.string.vsehgnlusherujilu);
			break;

		case 6:

			fragmentManager
					.beginTransaction()
					.replace(
							R.id.container,
							RecorderChatFragment
									.newInstance(ConstantS.KUANGWUZHI))
					.commit();
			// mTitle = getString(R.string.kuangwuzhisherujilu);
			// getSupportActionBar().setTitle(R.string.kuangwuzhisherujilu);
			break;

		default:
			break;
		}
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		recored_num_txt = (TextView) findViewById(R.id.recored_num_txt);
		array_num_txt = (TextView) findViewById(R.id.array_num_txt);
		reason_num_txt = (TextView) findViewById(R.id.reason_num_txt);
		findViewById(R.id.recored_reommend_btn).setOnClickListener(listener);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		try {
			recored_num_txt.setText(getString(R.string.conti_recored) + "\n"
					+ MainService.getRecorderInfoBean().getNoSnapDays()
					+ getString(R.string.time));

			array_num_txt.setText(getString(R.string.total_array) + "\n"
					+ MainService.getRecorderInfoBean().getRank()
					+ getString(R.string.time_));

			reason_num_txt.setText(getString(R.string.total_reason) + "\n"
					+ MainService.getRecorderInfoBean().getReasonableNum()
					+ getString(R.string.time));

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.recored_reommend_btn:
				gotoYaopager();
				break;

			default:
				break;
			}
		}
	};

	private void gotoYaopager() {
		Intent intent = new Intent();
		intent.setAction(ConstantS.ACTION_CHANEG_PAGER_INDEX);
		intent.putExtra("index", 1);
		sendBroadcast(intent);
		finish();
	}

}
