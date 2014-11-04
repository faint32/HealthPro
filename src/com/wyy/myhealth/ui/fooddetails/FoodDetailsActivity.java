package com.wyy.myhealth.ui.fooddetails;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wyy.myhealth.MainActivity;
import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.app.PreferencesFoodsInfo;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.Comment;
import com.wyy.myhealth.bean.NearFoodBean;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.file.utils.FileUtils;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.imag.utils.SavePic;
import com.wyy.myhealth.support.collect.CollectUtils;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;
import com.wyy.myhealth.ui.mapfood.CommercialMapActivity;
import com.wyy.myhealth.ui.yaoyingyang.YaoyingyangFragment;
import com.wyy.myhealth.utils.BingLog;
import com.wyy.myhealth.utils.DistanceUtils;
import com.wyy.myhealth.utils.ShareUtils;

public class FoodDetailsActivity extends BaseActivity {

	private static final String TAG = FoodDetailsActivity.class.getSimpleName();
	private ProgressBar loadingBar;
	private NearFoodBean foods;
	private ArrayList<Comment> comments;

	/**
	 * ��Ʒ����
	 */
	private ArrayList<Comment> menuComlist = new ArrayList<Comment>();

	private PersonalInfo info;

	@SuppressWarnings("unused")
	private int starsindex = 0;
	@SuppressWarnings("unused")
	private int goodsindex = 0;

	private ImageView foodpic;

	private TextView foodtag;

	private ImageView tasteLevel;
	@SuppressWarnings("unused")
	private ImageView ishanghu;

	private TextView distance;

	private ImageView userHead;

	private TextView userName;

	private TextView userTag;

	private TextView userSummary;

	private ImageLoader imageLoader = ImageLoader.getInstance();

	private DisplayImageOptions options;

	private ScrollView scrollView;

	private FrameLayout commentInfos;

	private FrameLayout healthzs;

	private String mydiatance = "";

	private String foodid = "";

	public static String imgurl = "";

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		}
		super.onCreate(savedInstanceState);
		context = this;
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			FadingActionBarHelper fadingActionBarHelper = new FadingActionBarHelper()
					.actionBarBackground(
							new ColorDrawable(getResources().getColor(
									R.color.themecolor)))
					.headerLayout(R.layout.foodhead)
					.contentLayout(R.layout.activity_food_details);
			setContentView(fadingActionBarHelper.createView(context));
			fadingActionBarHelper.initActionBar(context);
		} else {
			setContentView(R.layout.activity_food_details);
		}
		initView();
		foodid = PreferencesFoodsInfo.getfoodId(this);
		foodsDetail(foodid);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UmenAnalyticsUtility.onPageStart(TAG);
		UmenAnalyticsUtility.onResume(context);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		UmenAnalyticsUtility.onPageEnd(TAG);
		UmenAnalyticsUtility.onPause(context);
		BingLog.i(TAG, "===========onPause============");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		BingLog.i(TAG, "===========onStop============");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		BingLog.i(TAG, "===========onDestroy============");
	}

	private void initView() {
		mydiatance = getIntent().getStringExtra("distance");
		scrollView = (ScrollView) findViewById(R.id.details_scroll);
		foodpic = (ImageView) findViewById(R.id.foodpic);
		foodtag = (TextView) findViewById(R.id.food_tag);
		tasteLevel = (ImageView) findViewById(R.id.food_taste_level);
		ishanghu = (ImageView) findViewById(R.id.isshanghu);
		distance = (TextView) findViewById(R.id.food_distance);
		userHead = (ImageView) findViewById(R.id.userheadimg);
		userName = (TextView) findViewById(R.id.username);
		userTag = (TextView) findViewById(R.id.usertag);
		userSummary = (TextView) findViewById(R.id.user_summary_txt);

		commentInfos = (FrameLayout) findViewById(R.id.comment_fra);
		healthzs = (FrameLayout) findViewById(R.id.healthzs_fra);

		commentInfos.setOnClickListener(listener);
		healthzs.setOnClickListener(listener);

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.pic_loading_)
				.showImageForEmptyUri(R.drawable.pic_empty)
				.showImageOnFail(R.drawable.pic_failure).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true).build();
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(R.string.title_activity_food_details);
		loadingBar = new ProgressBar(this);
		actionBar.setCustomView(loadingBar, new ActionBar.LayoutParams(
				Gravity.RIGHT));
		actionBar.setDisplayShowCustomEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.food_details, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case R.id.comment:
			loopCommentActivity();
			break;

		case R.id.collect:
			CollectUtils.collectFood(foodid, context);
			UmenAnalyticsUtility.onEvent(context, ConstantS.UMNEG_COLLECT_FOOD);
			break;

		case R.id.resh:
			foodsDetail(foodid);
			break;

		case R.id.call:
			callPhone();
			break;

		case R.id.action_share:
			shareFood();
			break;

		case R.id.loopmap:

			loopCommercialMap();

			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.comment_fra:
				loopCommentInfo();
				break;

			case R.id.healthzs_fra:
				loopNutritionInfo();
				break;

			default:
				break;
			}

		}
	};

	private void foodsDetail(String foodid) {

		AsyncHttpResponseHandler res = new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				loadingBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onSuccess(String response) {
				BingLog.i(TAG, "����:" + response);
				parseFoods(response);
				// initData();
				// InitViewPager();
				// initList();

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				loadingBar.setVisibility(View.GONE);
			}

		};
		if (WyyApplication.getInfo() != null) {
			HealthHttpClient.getFoodDetails(foodid, WyyApplication.getInfo()
					.getId(), res);
		} else {
			HealthHttpClient.getFoodDetails(foodid, res);
		}

		// FoodsUtil.doHttpDetails(foodid, res);
	}

	private void parseFoods(String response) {

		try {

			JSONObject result = new JSONObject(response);

			JSONObject obj = result.getJSONObject("foods");

			try {
				Gson gson = new Gson();
				foods = gson.fromJson(obj.toString(), NearFoodBean.class);
				foods.setDistance(""
						+ DistanceUtils.getDistance(MainActivity.Wlongitude,
								MainActivity.Wlatitude,
								foods.getCommercialLon(),
								foods.getCommercialLat()));
			} catch (Exception e) {
				// TODO: handle exception
				BingLog.e(TAG, "��������");
			}

			comments = new ArrayList<Comment>();
			JSONArray json = result.getJSONArray("overviews");// �Ķ�
			JSONArray commentsArray = result.getJSONArray("comments");

			int length = json.length();
			for (int i = 0; i < length; i++) {
				Comment mcomment = new Comment();
				obj = json.getJSONObject(i);

				try {
					Gson gson = new Gson();
					mcomment = gson.fromJson(obj.toString(), Comment.class);
					foods.setEnergy(mcomment.getEnergy());
					foods.setReasonable(mcomment.getReasonable());
					comments.add(mcomment);
				} catch (Exception e) {
					// TODO: handle exception
					BingLog.e(TAG, "��������");
				}
			}

			int commlength = commentsArray.length();
			menuComlist.clear();
			/*********** �������� ************/
			for (int i = 0; i < commlength; i++) {
				Log.i(TAG, "����:" + commentsArray.getJSONObject(i));
				Comment mComment = new Comment();
				try {
					Gson gson = new Gson();
					mComment = gson.fromJson(commentsArray.getJSONObject(i)
							.toString(), Comment.class);
					mComment.setUserheadimage(HealthHttpClient.IMAGE_URL
							+ commentsArray.getJSONObject(i).getString(
									"headimage"));
					menuComlist.add(mComment);
				} catch (Exception e) {
					// TODO: handle exception
					BingLog.e(TAG, "��������");
				}

			}

			info = new PersonalInfo();
			JSONObject obj1 = result.getJSONObject("user");
			try {
				Gson gson = new Gson();
				info = gson.fromJson(obj1.toString(), PersonalInfo.class);
			} catch (Exception e) {
				// TODO: handle exception
			}

			try {
				starsindex = Integer.valueOf(result.getInt("collectCount"));
				goodsindex = Integer.valueOf(result.getInt("laudCount"));
			} catch (Exception e) {
				// TODO: handle exception
				BingLog.e(TAG, "��������");
			}

			setView();

		} catch (JSONException e) {
			e.printStackTrace();
			BingLog.e(TAG, "��������");
		}

	}

	private void setView() {
		imgurl = HealthHttpClient.IMAGE_URL + foods.getFoodpic();
		imageLoader.displayImage(
				HealthHttpClient.IMAGE_URL + foods.getFoodpic(), foodpic,
				options);
		new Thread() {
			public void run() {
				SavePic.saveFoodPic2Example(imageLoader.loadImageSync(imgurl));
			};
		}.start();

		foodtag.setText("" + foods.getTags());
		try {
			tasteLevel.setImageResource(ConstantS.LEVEL_POINT[Integer
					.valueOf(foods.getTastelevel())]);
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (TextUtils.isEmpty(mydiatance)) {
			distance.setText("" + foods.getDistance() + "km");
		} else {
			distance.setText(mydiatance + "km");
		}

		imageLoader.displayImage(
				HealthHttpClient.IMAGE_URL + info.getHeadimage(), userHead,
				options);
		userName.setText("" + info.getUsername());
		userSummary.setText("" + foods.getSummary());
		userTag.setText("" + info.getBodyindex());
		scrollView.setVisibility(View.VISIBLE);
		findViewById(R.id.foodhead).setVisibility(View.VISIBLE);

	}

	private void loopCommentInfo() {
		try {
			Intent intent = new Intent();
			intent.setClass(context, FoodCommentInfoActivity.class);
			intent.putExtra("comment", (Serializable) menuComlist);
			startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			BingLog.e(TAG, "д�����");
		}

	}

	private void loopCommentActivity() {
		try {
			Intent intent = new Intent();
			intent.setClass(context, FoodCommentActivity.class);
			intent.putExtra(ConstantS.ID, foodid);
			startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			BingLog.e(TAG, "д�����");
		}
	}

	private void loopCommercialMap() {
		try {

			YaoyingyangFragment.isdingwei = true;
			Intent intent = new Intent();
			intent.setClass(context, CommercialMapActivity.class);
			intent.putExtra("foods", (Serializable) foods);
			startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			BingLog.e(TAG, "д�����");
		}
	}

	private void loopNutritionInfo() {
		try {

			Intent intent = new Intent();
			intent.setClass(context, FoodNutritionActivity.class);
			if (comments != null && comments.size() > 0) {
				intent.putExtra("comment",
						(Serializable) comments.get(comments.size() - 1));
			}
			startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			BingLog.e(TAG, "д�����");
		}
	}

	private void shareFood() {
		try {
			ShareUtils
					.shareFood(context, getString(R.string.share_content_),
							Uri.fromFile(new File(FileUtils.HEALTH_IMAG, "chc"
									+ ".png")));
			UmenAnalyticsUtility.onEvent(context, ConstantS.UMNEG_SHARE_FOOD);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private void callPhone() {
		if (foods != null && foods.getCommercialPhone() != null) {
			Intent intent = new Intent("android.intent.action.CALL",
					Uri.parse("tel:" + foods.getCommercialPhone()));
			startActivity(intent);
		} else {
			Toast.makeText(context, R.string.tel_error, Toast.LENGTH_SHORT)
					.show();
		}

	}

}
