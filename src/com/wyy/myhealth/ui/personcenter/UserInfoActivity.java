package com.wyy.myhealth.ui.personcenter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import u.aly.ca;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.MoodaFoodBean;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.BingHttpHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.http.utils.JsonUtils;
import com.wyy.myhealth.imag.utils.LoadImageUtils;
import com.wyy.myhealth.support.bitmap.BitmapUtility;
import com.wyy.myhealth.ui.absfragment.adapter.ShaiYiSaiAdapter2;
import com.wyy.myhealth.ui.absfragment.utils.TimeUtility;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.ui.shaiyishai.ShaiGridAdapter;

public class UserInfoActivity extends BaseActivity implements
		ActivityInterface, OnPageChangeListener {

	private ViewPager pager;
	private View listwrraper;
	private View gridwrraper;
	private ImageView userHeadView;
	private TextView dynamicTextView;
	private TextView fanscountView;
	private TextView followcountView;
	private ImageView tab_grid;
	private ImageView tab_list;
	private GridView gridView;
	private ListView listView;
	private BingPagerAdapter bingPagerAdapter;
	private List<View> views = new ArrayList<>();
	private List<MoodaFoodBean> moodaFoodBeans = new ArrayList<>();
	private ShaiYiSaiAdapter2 shaiYiSaiAdapter2;
	private ShaiGridAdapter shaiGridAdapter;
	private String uid = "";
	private ProgressBar loadingBar;
	private ProgressBar loadingridgBar;
	private ProgressBar loadinglistBar;
	private PersonalInfo personalInfo;
	private static final int[] grids = { R.drawable.tab_grid_focus,
			R.drawable.tab_grid_unfocus };
	private static final int[] lists = { R.drawable.tab_list_focus,
			R.drawable.tab_list_unfocus };

	private static final long DELAY_TIME = 1000;

	private boolean isfollow = false;

	private ImageButton followButton;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		initView();
		initData();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		pager = (ViewPager) findViewById(R.id.user_info_dy_pager);
		tab_grid = (ImageView) findViewById(R.id.tab_grid);
		tab_list = (ImageView) findViewById(R.id.tab_list);
		userHeadView = (ImageView) findViewById(R.id.userhead);
		dynamicTextView = (TextView) findViewById(R.id.dynamic_count);
		fanscountView = (TextView) findViewById(R.id.fans_count);
		followcountView = (TextView) findViewById(R.id.follow_count);
		followButton = (ImageButton) findViewById(R.id.follow_btn);
		listwrraper = getLayoutInflater().inflate(R.layout.user_info_full_list,
				null);
		gridwrraper = getLayoutInflater().inflate(
				R.layout.user_info_grid_wrapper, null);
		gridView = (GridView) gridwrraper.findViewById(R.id.user_info_grid);
		listView = (ListView) listwrraper.findViewById(R.id.fullListView);
		loadingridgBar = (ProgressBar) gridwrraper
				.findViewById(R.id.grid_progressBar);
		loadinglistBar = (ProgressBar) listwrraper
				.findViewById(R.id.list_progressBar);
		views.add(gridwrraper);
		views.add(listwrraper);
		bingPagerAdapter = new BingPagerAdapter(views);
		pager.setAdapter(bingPagerAdapter);
		tab_grid.setOnClickListener(listener);
		tab_list.setOnClickListener(listener);
		followButton.setOnClickListener(listener);
		findViewById(R.id.dynamic_lay).setOnClickListener(listener);
		findViewById(R.id.fans_lay).setOnClickListener(listener);
		findViewById(R.id.follow_lay).setOnClickListener(listener);
		pager.setOnPageChangeListener(this);
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		ActionBar actionBar = getSupportActionBar();
		// actionBar.setTitle(R.string.title_activity_food_details);
		loadingBar = new ProgressBar(this);
		actionBar.setCustomView(loadingBar, new ActionBar.LayoutParams(
				Gravity.RIGHT));
		actionBar.setDisplayShowCustomEnabled(true);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			actionBar.setBackgroundDrawable(new ColorDrawable(getResources()
					.getColor(R.color.transparent)));
		}
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		shaiYiSaiAdapter2 = new ShaiYiSaiAdapter2(moodaFoodBeans, context);
		shaiGridAdapter = new ShaiGridAdapter(moodaFoodBeans, context);
		listView.setAdapter(shaiYiSaiAdapter2);
		gridView.setAdapter(shaiGridAdapter);
		getUserInfo();
		listView.setVisibility(View.GONE);
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int id = v.getId();
			switch (id) {
			case R.id.tab_grid:
				pager.setCurrentItem(0);
				break;

			case R.id.tab_list:
				pager.setCurrentItem(1);
				break;

			case R.id.follow_btn:
				followUser();
				break;
			case R.id.fans_lay:
				showFansActivity();
				break;
			case R.id.follow_lay:
				showFollowsActivity();
				break;

			default:
				break;
			}
		}
	};

	private void setGridPager_() {
		tab_grid.setImageResource(grids[0]);
		tab_list.setImageResource(lists[1]);
		loadingHandler.sendEmptyMessageDelayed(0, DELAY_TIME);
	}

	private void setListPager_() {
		tab_grid.setImageResource(grids[1]);
		tab_list.setImageResource(lists[0]);
		gridView.setVisibility(View.GONE);
		listView.setVisibility(View.VISIBLE);
		loadingHandler.sendEmptyMessageDelayed(1, DELAY_TIME);
	}

	private void getUserInfo() {
		uid = getIntent().getStringExtra("id");
		if (TextUtils.isEmpty(uid)) {
			return;
		}
		if (WyyApplication.getInfo() == null) {
			return;
		}
		HealthHttpClient.checkUserInfo(uid, WyyApplication.getInfo().getId(),
				"0", "9", handler);

	}

	private BingHttpHandler handler = new BingHttpHandler() {

		@Override
		protected void onGetSuccess(JSONObject response) {
			// TODO Auto-generated method stub
			parseJson(response);
		}

		@Override
		protected void onGetFinish() {
			// TODO Auto-generated method stub
			loadingBar.setVisibility(View.GONE);
		}

		public void onStart() {
			loadingBar.setVisibility(View.VISIBLE);
		};

	};

	private void parseJson(JSONObject response) {
		if (response == null) {
			return;
		}

		try {
			personalInfo = JsonUtils.getInfo(response.getJSONObject("user"));
			JSONArray array = response.getJSONArray("foods");
			int length = array.length();
			moodaFoodBeans.clear();
			for (int i = 0; i < length; i++) {
				MoodaFoodBean moodaFoodBean = JsonUtils.getMoodaFoodBean(array
						.getJSONObject(i));
				moodaFoodBean.setCn_time(TimeUtility.getListTime(moodaFoodBean
						.getCreatetime()));
				moodaFoodBeans.add(moodaFoodBean);
			}
			shaiYiSaiAdapter2.notifyDataSetChanged();
			shaiGridAdapter.notifyDataSetChanged();
			loadinglistBar.setVisibility(View.GONE);
			loadingridgBar.setVisibility(View.GONE);
			isfollow = response.getBoolean("isfollow");
			setTitleView();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setTitleView() {
		if (personalInfo == null) {
			return;
		}

		getSupportActionBar().setTitle(personalInfo.getUsername());
		LoadImageUtils.loadWebImageV_Min(userHeadView,
				HealthHttpClient.IMAGE_URL + personalInfo.getHeadimage(),
				imageLoadingListener);
		dynamicTextView.setText("" + personalInfo.getAiredcount());
		fanscountView.setText("" + personalInfo.getFollowmecount());
		followcountView.setText("" + personalInfo.getFollowcount());
		if (isfollow) {
			followButton.setImageResource(R.drawable.btn_follow_canel_sec);
		} else {
			followButton.setImageResource(R.drawable.btn_follow_sec);
		}
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
		if (arg0 == 0) {
			setGridPager_();
		} else {
			setListPager_();
		}
	}

	private Handler loadingHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int what = msg.what;
			switch (what) {
			case 0:
				gridView.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
				loadinglistBar.setVisibility(View.VISIBLE);
				loadingridgBar.setVisibility(View.GONE);
				break;
			case 1:
				gridView.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				loadinglistBar.setVisibility(View.GONE);
				loadingridgBar.setVisibility(View.VISIBLE);
			default:
				break;
			}
			return false;
		}
	});

	private void followUser() {
		if (isfollow) {
			isfollow = false;
			followButton.setImageResource(R.drawable.btn_follow_sec);
			if (null != WyyApplication.getInfo()) {
				HealthHttpClient.followCanelUser(WyyApplication.getInfo()
						.getId(), uid, new AsyncHttpResponseHandler());
			}
		} else {
			isfollow = true;
			followButton.setImageResource(R.drawable.btn_follow_canel_sec);
			if (null != WyyApplication.getInfo()) {
				HealthHttpClient.followUser(WyyApplication.getInfo().getId(),
						uid, new AsyncHttpResponseHandler());
			}
		}

	}

	private OnItemClickListener gridClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub

		}
	};

	private void showFansActivity() {
		Intent intent = new Intent();
		intent.putExtra("id", uid);
		intent.setClass(context, FansListActivity.class);
		startActivity(intent);
	}

	private void showFollowsActivity() {
		Intent intent = new Intent();
		intent.putExtra("id", uid);
		intent.setClass(context, FollowUserActivity.class);
		startActivity(intent);
	}

	private ImageLoadingListener imageLoadingListener = new ImageLoadingListener() {

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			// TODO Auto-generated method stub
			new Thread(new BlurRunnble(loadedImage)).start();
		}

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			// TODO Auto-generated method stub

		}
	};

	private class BlurRunnble implements Runnable {

		private Bitmap bitmap;

		public BlurRunnble(Bitmap bitmap) {
			super();
			this.bitmap = bitmap;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			bitmap = BitmapUtility.fastblur(bitmap, 30);
			Message msg = new Message();
			msg.obj = bitmap;
			msg.what = 0;
			loadBitmapHandler.sendMessage(msg);
		}

	}

	private Handler loadBitmapHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int what = msg.what;
			switch (what) {
			case 0:
				Bitmap bitmap = (Bitmap) msg.obj;
				if (bitmap != null) {
					ImageView imageView = (ImageView) findViewById(R.id.user_bg);
					imageView.setImageBitmap(bitmap);
				}
				break;

			default:
				break;
			}
			return false;
		}
	});
}
