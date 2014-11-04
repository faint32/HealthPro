package com.wyy.myhealth;

import java.lang.reflect.Field;

import com.astuetz.PagerSlidingTabStrip;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.pager.utils.SuperAwesomeCardFragment;
import com.wyy.myhealth.service.MainService;
import com.wyy.myhealth.stepcount.StepService;
import com.wyy.myhealth.ui.discover.DiscoverFragment;
import com.wyy.myhealth.ui.navigation.DiscoverNavActivity;
import com.wyy.myhealth.ui.navigation.PersonalNavActivity;
import com.wyy.myhealth.ui.navigation.YaoNavActivity;
import com.wyy.myhealth.ui.personcenter.MineFragment;
import com.wyy.myhealth.ui.personcenter.PersonCenterFragment;
import com.wyy.myhealth.ui.scan.ScanFragment;
import com.wyy.myhealth.ui.scan.utils.DialogShow;
import com.wyy.myhealth.ui.setting.SettingActivity;
import com.wyy.myhealth.ui.yaoyingyang.YaoyingyangFragment;
import com.wyy.myhealth.utils.AlarmUtils;
import com.wyy.myhealth.utils.BingLog;
import com.wyy.myhealth.utils.UpdateAppUtils;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends ActionBarActivity implements
		OnQueryTextListener, OnPageChangeListener {

	private static final String TAG = MainActivity.class.getSimpleName();
	private static final String POSTION = "postion";
	private static int curpostion = 0;
	private PagerSlidingTabStrip tabs;
	private ViewPager mainPager;
	private MyPagerAdapter adapter;
	/**
	 * ��λ
	 */
	private LocationClient mLocationClient;
	/**
	 * ����
	 */
	public static double Wlongitude = 0;
	/**
	 * γ��
	 */
	public static double Wlatitude = 0;

	public static String address = "";

	private MainService mainService;

	SearchView searchView;

	ImageView help;

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		initActionBar();
		initService();
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		tabs.setShouldExpand(true);
		tabs.setChangeTextColor(true);
		mainPager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager());

		mainPager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		mainPager.setPageMargin(pageMargin);

		tabs.setViewPager(mainPager);
		tabs.setOnPageChangeListener(this);

		initLocation();

		initFilter();

		finshLogin();

		setCusmenutome();

		initPostFoot();
		UpdateAppUtils.upDateApp(context);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UmenAnalyticsUtility.onResume(context);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		UmenAnalyticsUtility.onPause(context);
	}

	private void initActionBar() {
		View actionView = getLayoutInflater().inflate(R.layout.main_menu_v,
				null);
		searchView = (SearchView) actionView.findViewById(R.id.search_view);
		settingSearchView();
		help = (ImageView) actionView.findViewById(R.id.help);
		getSupportActionBar().setCustomView(actionView,
				new ActionBar.LayoutParams(Gravity.RIGHT));
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(getResources().getColor(R.color.themecolor)));
		searchView.setOnQueryTextListener(this);
		help.setOnClickListener(listener);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopLocation();
		unbindService(serviceConnection);
		unregisterReceiver(mainReceiver);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		curpostion = mainPager.getCurrentItem();
		outState.putInt(POSTION, curpostion);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState != null) {
			curpostion = savedInstanceState.getInt(POSTION, 0);
			if (mainPager != null) {
				try {
					mainPager.setCurrentItem(curpostion);
				} catch (Exception e) {
					// TODO: handle exception
					BingLog.e(TAG, "����:" + e.getMessage());
				}

			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_setting) {
			showSetting();
			return true;
		} else if (id == R.id.exit) {
			exitMain();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { getString(R.string.saoyy),
				getString(R.string.yaoyy), getString(R.string.find),
				getString(R.string.me) };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:

				// return SuperAwesomeCardFragment.newInstance(position);

				return ScanFragment.newInstance(position);

			case 1:
				return YaoyingyangFragment.newInstance(position);

			case 2:

				return DiscoverFragment.newInstance(position);

			case 3:

				return MineFragment.newInstance(position);

			default:
				break;
			}
			return SuperAwesomeCardFragment.newInstance(position);
		}

	}

	/**
	 * ֹͣ��λ
	 */
	private void stopLocation() {
		if (mLocationClient != null) {
			mLocationClient.stop();
			mLocationClient = null;
		}
	}

	/**
	 * ��ʼ���ٶȶ�λ
	 */
	private void initLocation() {
		mLocationClient = new LocationClient(MainActivity.this);
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceivePoi(BDLocation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onReceiveLocation(BDLocation arg0) {
				// TODO Auto-generated method stub
				Wlongitude = arg0.getLongitude();
				Wlatitude = arg0.getLatitude();
				address = arg0.getAddrStr();

			}
		});

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		// ��Ҫ��ַ��Ϣ������Ϊ�����κ�ֵ��string���ͣ��Ҳ���Ϊnull��ʱ������ʾ�޵�ַ��Ϣ��
		option.setAddrType("all");

		// �����Ƿ񷵻�POI�ĵ绰�͵�ַ����ϸ��Ϣ��Ĭ��ֵΪfalse����������POI�ĵ绰�͵�ַ��Ϣ��
		option.setPoiExtraInfo(true);

		// ���ò�Ʒ�����ơ�ǿ�ҽ�����ʹ���Զ���Ĳ�Ʒ�����ƣ����������Ժ�Ϊ���ṩ����Ч׼ȷ�Ķ�λ����
		option.setProdName("ͨ��GPS��λ�ҵ�ǰ��λ��");

		// ��ѯ��Χ��Ĭ��ֵΪ500�����Ե�ǰ��λλ��Ϊ���ĵİ뾶��С��
		option.setPoiDistance(500);
		// �������û��涨λ����
		option.disableCache(true);
		option.setScanSpan(5000);
		mLocationClient.setLocOption(option);

		mLocationClient.start();

		mLocationClient.requestLocation();

	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		sendFoodkey("" + arg0);
		return true;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		curpostion = arg0;
		if (curpostion == 0) {
			searchView.setVisibility(View.GONE);
			help.setVisibility(View.VISIBLE);
		} else {
			searchView.setVisibility(View.VISIBLE);
			help.setVisibility(View.GONE);
		}
		if (arg0 == 0) {
			sendPageIndex(arg0);
		} else if (arg0 == 2) {
			startUpdateData();
			initDisNav();
		} else if (arg0 == 3) {
			initPersonNav();
		} else if (arg0 == 1) {
			initYaoNav();
		}

	}

	private void sendPageIndex(int index) {
		Intent intent = new Intent();
		intent.setAction(ConstantS.PAGE_INDEX_CHANG);
		intent.putExtra("index", index);
		sendBroadcast(intent);
	}

	private void sendFoodkey(String key) {
		Intent intent = new Intent();
		intent.setAction(ConstantS.ACTION_SEARCH_FOOD);
		intent.putExtra("key", key);
		sendBroadcast(intent);
		mainPager.setCurrentItem(1);
		UmenAnalyticsUtility.onEvent(context, ConstantS.UMNEG_SEARCH_FOOD);
	}

	private void initService() {
		bindService(new Intent(MainActivity.this, MainService.class),
				serviceConnection, Context.BIND_AUTO_CREATE);
	}

	private ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			mainService = ((MainService.Wibingder) service).getBingder();

		}
	};

	private void initFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConstantS.ACTION_CHANEG_PAGER_INDEX);
		filter.addAction(ConstantS.ACTION_HIDE_UI_CHANGE);
		filter.addAction(ConstantS.ACTION_MAIN_FINSH);
		filter.addAction(ConstantS.ACTION_SEND_SHAI);
		filter.addAction(ConstantS.ACTION_SEND_CANEL_NOTICE);
		filter.addAction(ConstantS.NEW_FOOD_COMMENT);
		filter.addAction(ConstantS.ACTION_RECORED_NOTICE);
		registerReceiver(mainReceiver, filter);
	}

	private BroadcastReceiver mainReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			BingLog.i(TAG, "main:" + action);
			if (action.equals(ConstantS.ACTION_HIDE_UI_CHANGE)) {
				changeUI();
			} else if (action.equals(ConstantS.ACTION_MAIN_FINSH)) {
				finish();
			} else if (action.equals(ConstantS.ACTION_SEND_SHAI)
					|| action.equals(ConstantS.ACTION_RECORED_NOTICE)) {
				showNotice();
			} else if (action.equals(ConstantS.NEW_FOOD_COMMENT)) {
				showNotice();
			} else if (action.equals(ConstantS.ACTION_SEND_CANEL_NOTICE)) {
				canelNotice();
			} else if (action.equals(ConstantS.ACTION_CHANEG_PAGER_INDEX)) {
				swipePager(intent);
			}
		}
	};

	private void changeUI() {
		if (tabs.isShown()) {
			tabs.setVisibility(View.INVISIBLE);
		} else {
			tabs.setVisibility(View.VISIBLE);
			BingLog.d(TAG, "��ʾtab");
		}
	}

	private void showSetting() {
		startActivity(new Intent(MainActivity.this, SettingActivity.class));
	}

	private void finshLogin() {
		sendBroadcast(new Intent(ConstantS.ACTION_LOGIN_FINISH));
	}

	private void showNotice() {
		tabs.updateTextDrawNotice(2, R.drawable.ic_tab_notice);
	}

	private void canelNotice() {
		tabs.updateTextDrawNotice(2, 0);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	private void exitMain() {
		finish();
		// System.exit(0);
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.help:
				DialogShow.showHelpDialog(MainActivity.this,
						getString(R.string.sao_help));
				break;

			default:
				break;
			}
		}
	};

	private void settingSearchView() {
		ImageView icon = (ImageView) searchView
				.findViewById(android.support.v7.appcompat.R.id.search_button);
		icon.setImageResource(R.drawable.ic_search);
	}

	private void startUpdateData() {
		if (mainService != null && mainService.getService() == null) {
			mainService.initRunable();
		}
	}

	private void setCusmenutome() {
		try {
			ViewConfiguration mconfig = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(mconfig, false);
			}
		} catch (Exception ex) {
		}
	}

	private void swipePager(Intent intent) {
		int postion;
		postion = intent.getIntExtra("index", 0);
		try {
			if (postion == 1) {
				sendBroadcast(new Intent(ConstantS.ACTION_RECOOMEND_TODAY_FOOD));
			}
			mainPager.setCurrentItem(postion);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void initYaoNav() {
		if (YaoNavActivity.getIsFirstUse(context)) {
			startActivity(new Intent(context, YaoNavActivity.class));
		}
	}

	private void initDisNav() {
		if (DiscoverNavActivity.getIsFirstUse(context)) {
			startActivity(new Intent(context, DiscoverNavActivity.class));
		}
	}

	private void initPersonNav() {
		if (PersonalNavActivity.getIsFirstUse(context)) {
			startActivity(new Intent(context, PersonalNavActivity.class));
		}
	}

	private void initPostFoot() {
		try {
			AlarmUtils.setAler(MainActivity.this);
			StepService.startService(this);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
