package com.wyy.myhealth.ui.icebox;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.IceBoxFoodBean;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.JsonHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.http.utils.JsonUtils;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.ui.customview.BingListView;
import com.wyy.myhealth.ui.icebox.IceBoxChildAdapter.DelPicClickListener;
import com.wyy.myhealth.ui.icebox.IceBoxMainAdapter.GridCilckListener;
import com.wyy.myhealth.utils.BingLog;

public class IceBoxActivity extends BaseActivity implements ActivityInterface,
		OnRefreshListener, DelPicClickListener, GridCilckListener {

	private static final String TAG = IceBoxActivity.class.getSimpleName();

	private ImageView door_left;

	private ImageView door_right;

	private LinearLayout doorlayout;

	private BingListView iceListv;

	private SwipeRefreshLayout mSwipeRefreshLayout;

	private List<List<IceBoxFoodBean>> iceBoxFoodBeansList = new ArrayList<>();

	private List<IceBoxFoodBean> fruitsList = new ArrayList<>();

	private List<IceBoxFoodBean> vegetablesList = new ArrayList<>();

	private List<IceBoxFoodBean> meatList = new ArrayList<>();

	private List<IceBoxFoodBean> staplefoodsList = new ArrayList<>();

	private List<IceBoxFoodBean> otherList = new ArrayList<>();

	private String json = "";

	private IceBoxMainAdapter iceBoxMainAdapter;

	private boolean isLoading = false;

	private static boolean isDeletevisible = false;
	
	private int infodex=0;

	public static boolean isIsvisible() {
		return isDeletevisible;
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.my_ice_box);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ice_box);
		initFilter();
		initView();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.ice_box, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.ice_add:
			showAddFood();
			break;

		case R.id.ice_manager:
			showDelete();
			break;

		case R.id.ice_re:
			showHistory();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		saveCurrent_Result(json);
		unregisterReceiver(iceboxReceiver);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		door_left = (ImageView) findViewById(R.id.door_l);
		door_right = (ImageView) findViewById(R.id.door_r);
		doorlayout = (LinearLayout) findViewById(R.id.ice_box_door);
		animationHandler.sendEmptyMessageDelayed(0, 1000);
		iceListv = (BingListView) findViewById(R.id.ice_box_list_v);
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.list_swipe);
		mSwipeRefreshLayout.setColorScheme(R.color.deepskyblue,
				R.color.darkslategrey, R.color.themecolor, R.color.home_txt);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		initData();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		iceBoxMainAdapter = new IceBoxMainAdapter(iceBoxFoodBeansList, context);
		iceListv.setAdapter(iceBoxMainAdapter);
		json = getLast_Result();
		if (TextUtils.isEmpty(json)) {
			reshIceBoxFood();
		} else {
			paresJson(json);
			reshIceBoxFood();
		}

		iceBoxMainAdapter.setDelpicClickListener(this);
		iceBoxMainAdapter.setGridCilckListener(this);

	}

	private void startOpenAnimation() {
		TranslateAnimation translateAnimation = new TranslateAnimation(0,
				-door_left.getWidth(), 0, 0);
		translateAnimation.setDuration(3000);
		door_left.startAnimation(translateAnimation);

		TranslateAnimation translateAnimation1 = new TranslateAnimation(0,
				door_right.getWidth(), 0, 0);
		translateAnimation1.setDuration(3000);
		door_right.startAnimation(translateAnimation1);

		translateAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				doorlayout.setVisibility(View.GONE);
			}
		});

	}

	private Handler animationHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			startOpenAnimation();
			return false;
		}
	});

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (!isLoading) {
			reshIceBoxFood();
		}

	}

	private void reshIceBoxFood() {
		HealthHttpClient.getIceBoxFood(WyyApplication.getInfo().getId(), "0",
				"100", responseHandler);
	}

	private JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {

		public void onSuccess(String content) {
			super.onSuccess(content);
			BingLog.i(TAG, "冰箱数据:" + content);
			if (json.equals(content)) {
				Toast.makeText(context, R.string.nonewmsg, Toast.LENGTH_LONG)
						.show();
			} else {
				paresJson(content);
			}

		};

		@Override
		public void onFailure(Throwable e, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			super.onFailure(e, errorResponse);
		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			isLoading = true;
			mSwipeRefreshLayout.setRefreshing(true);
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			super.onFinish();
			isLoading = false;
			mSwipeRefreshLayout.setRefreshing(false);
		}

	};

	private void paresJson(String content) {
		try {

			JSONObject object = new JSONObject(content);

			if (JsonUtils.isSuccess(object)) {

				iceBoxFoodBeansList.clear();
				vegetablesList.clear();
				fruitsList.clear();
				meatList.clear();
				staplefoodsList.clear();
				otherList.clear();

				JSONArray foods = object.getJSONArray("foods");
				int length = foods.length();
				for (int i = 0; i < length; i++) {
					IceBoxFoodBean iceBoxFoodBean = JsonUtils
							.getIceBoxFoodBean(foods.getJSONObject(i));
					switch (iceBoxFoodBean.getType()) {
					case 1:
						vegetablesList.add(iceBoxFoodBean);
						break;

					case 2:
						fruitsList.add(iceBoxFoodBean);
						break;

					case 3:
						meatList.add(iceBoxFoodBean);
						break;

					case 4:
						staplefoodsList.add(iceBoxFoodBean);
						break;

					case 5:
						otherList.add(iceBoxFoodBean);
						break;

					default:
						break;
					}

				}

				iceBoxFoodBeansList.add(fruitsList);
				iceBoxFoodBeansList.add(vegetablesList);
				iceBoxFoodBeansList.add(meatList);
				iceBoxFoodBeansList.add(staplefoodsList);
				iceBoxFoodBeansList.add(otherList);
				iceBoxMainAdapter.notifyDataSetChanged();
				json = content;
			} else {
				Toast.makeText(context, R.string.nodata, Toast.LENGTH_LONG)
						.show();
			}
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(context, R.string.parseerror, Toast.LENGTH_LONG)
					.show();
		}
	}

	/**
	 * 保存此次数据
	 * 
	 * @param result
	 */
	private void saveCurrent_Result(String result) {
		if (TextUtils.isEmpty(result)) {
			return;
		}
		SharedPreferences preferences = getSharedPreferences(TAG,
				Context.MODE_PRIVATE);

		Editor editor = preferences.edit();

		editor.putString(ConstantS.RESULT, result);
		editor.commit();

	}

	private String getLast_Result() {
		String result = getSharedPreferences(TAG, Context.MODE_PRIVATE)
				.getString(ConstantS.RESULT, "");
		return result;
	}

	private void showAddFood() {
		startActivity(new Intent(context, IceBoxAddFood.class));
	}

	private void showHistory() {
		startActivity(new Intent(context, IceBoxHistory.class));
	}

	public class DelFood4Ice extends AsyncHttpResponseHandler {
		int position;
		int type;

		public DelFood4Ice(int position, int type) {
			this.position = position;
			this.type = type;
		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			super.onFinish();
		}

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			if (JsonUtils.isSuccess(content)) {
				delFood4Ice(position, type);
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);
		}

	}

	private void delFood4Ice(int position, int type) {
		switch (type) {
		case 1:
			type = 1;
			break;

		case 2:
			type = 0;
			break;

		case 3:
			type = 2;
			break;

		case 4:
			type = 3;
			break;

		case 5:
			type = 4;
			break;

		default:
			break;
		}
		iceBoxFoodBeansList.get(type).remove(position);
		iceBoxMainAdapter.notifyDataSetChanged();
	}

	private int getTypePostion(int type) {
		switch (type) {
		case 1:
			type = 1;
			break;

		case 2:
			type = 0;
			break;

		case 3:
			type = 2;
			break;

		case 4:
			type = 3;
			break;

		case 5:
			type = 4;
			break;

		default:
			break;
		}

		return type;

	}

	private void showDelete() {
		if (isDeletevisible) {
			isDeletevisible = false;
		} else {
			isDeletevisible = true;
		}

		iceBoxMainAdapter.notifyDataSetChanged();
	}

	@Override
	public void delFood(int postion, int type) {
		// TODO Auto-generated method stub
		HealthHttpClient.delFood4Icebox(
				iceBoxFoodBeansList.get(getTypePostion(type)).get(postion)
						.getId(), new DelFood4Ice(postion, type));
	}

	@Override
	public void getFoodInfo(int adapterposition, int position) {
		// TODO Auto-generated method stub
		infodex=position;
		try {
			loopIceBoxInfo(iceBoxFoodBeansList.get(adapterposition).get(position));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	private void loopIceBoxInfo(IceBoxFoodBean iceBoxFoodBean) {
		if (iceBoxFoodBean == null) {
			return;
		}
		Intent intent = new Intent(context, IceBoxInfoActivity.class);
		intent.putExtra("food", iceBoxFoodBean);
		startActivity(intent);
//		overridePendingTransition(R.anim.zoom_enter,
//				R.anim.zoom_exit);
	}

	
	
	private void initFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConstantS.ACTION_SEND_DELETE_ICEFOOD);
		registerReceiver(iceboxReceiver, filter);
	}

	private BroadcastReceiver iceboxReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals(ConstantS.ACTION_SEND_DELETE_ICEFOOD)) {
				IceBoxFoodBean iceBoxFoodBean=(IceBoxFoodBean) intent.getSerializableExtra("food");
				delIceFood(iceBoxFoodBean);
			}
		}
	};

	private void delIceFood(IceBoxFoodBean iceBoxFoodBean){
		if (iceBoxFoodBean!=null) {
			HealthHttpClient.delFood4Icebox(
					iceBoxFoodBean.getId(), new DelFood4Ice(infodex, iceBoxFoodBean.getType()));
		}
	}
	
}
