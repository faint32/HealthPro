package com.wyy.myhealth.ui.fooddetails;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
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
import com.wyy.myhealth.http.BingHttpHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.http.utils.JsonUtils;
import com.wyy.myhealth.imag.utils.SavePic;
import com.wyy.myhealth.support.collect.CollectUtils;
import com.wyy.myhealth.ui.absfragment.utils.TimeUtility;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.ui.customview.FullListView;
import com.wyy.myhealth.ui.mapfood.CommercialMapActivity;
import com.wyy.myhealth.ui.yaoyingyang.YaoyingyangFragment;
import com.wyy.myhealth.utils.BingLog;
import com.wyy.myhealth.utils.DistanceUtils;
import com.wyy.myhealth.utils.ShareUtils;

public class FoodDetailsActivity extends BaseActivity implements
		ActivityInterface {

	private static final String TAG = FoodDetailsActivity.class.getSimpleName();
	private ProgressBar loadingBar;
	private NearFoodBean foods;
	// private ArrayList<Comment> comments;

	/**
	 * 菜品评论
	 */
	private ArrayList<Comment> menuComlist = new ArrayList<Comment>();

	private PersonalInfo info;

	@SuppressWarnings("unused")
	private int starsindex = 0;
	@SuppressWarnings("unused")
	private int goodsindex = 0;

	private ImageView foodpic;
	/**
	 * 食物标签
	 */
	private TextView foodtag;

	private ImageView tasteLevel;
	/**
	 * 是否合理
	 */
	private ImageView isHealthImageView;
	/**
	 * 距离显示
	 */
	private TextView distance;
	/**
	 * 用户头像
	 */
	private ImageView userHead;
	/**
	 * 用户名称
	 */
	private TextView userName;
	/**
	 * 用户标签
	 */
	private TextView userTag;
	/**
	 * 用户suqiu
	 */
	private TextView userSummary;
	/**
	 * 创建时间
	 */
	private TextView createTimeTextView;
	/**
	 * 点赞数量
	 */
	private TextView likenumTextView;
	/**
	 * 评论数量
	 */
	private TextView commentnumTextView;

	private ImageLoader imageLoader = ImageLoader.getInstance();

	private DisplayImageOptions options;

	private ScrollView scrollView;

	private FrameLayout commentInfos;

	private FrameLayout healthzs;

	private String mydiatance = "";

	private String foodid = "";

	private boolean ishealth = false;

	public static String imgurl = "";

	private FullListView commentListView;

	private CommentAdapter commentAdapter;

	private static final int[] isHealths = { R.drawable.ic_food_details_normal,
			R.drawable.ic_food_details_warn };

	private String createTime = "";

	private boolean isLike = false;

	private List<PersonalInfo> laudUser = new ArrayList<>();
	/**
	 * 点赞用户显示
	 */
	private TextView likers;

	private EditText commentEditText;

	private Button sendCommentButton;

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
			fadingActionBarHelper
					.setCustomeViewResId(R.layout.send_msg_edit_layout);
			fadingActionBarHelper
					.setCustomeParams(new FrameLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT, Gravity.BOTTOM));
			setContentView(fadingActionBarHelper.createView(context));
			fadingActionBarHelper.initActionBar(context);
		} else {
			setContentView(R.layout.activity_food_details);
		}
		initView();
		initData();
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

	@Override
	public void initView() {
		mydiatance = getIntent().getStringExtra("distance");
		scrollView = (ScrollView) findViewById(R.id.details_scroll);
		foodpic = (ImageView) findViewById(R.id.foodpic);
		foodtag = (TextView) findViewById(R.id.food_tag);
		tasteLevel = (ImageView) findViewById(R.id.food_taste_level);
		isHealthImageView = (ImageView) findViewById(R.id.ishealth);
		distance = (TextView) findViewById(R.id.food_distance);
		userHead = (ImageView) findViewById(R.id.userheadimg);
		userName = (TextView) findViewById(R.id.username);
		userTag = (TextView) findViewById(R.id.usertag);
		userSummary = (TextView) findViewById(R.id.user_summary_txt);
		createTimeTextView = (TextView) findViewById(R.id.create_time_txt);
		commentInfos = (FrameLayout) findViewById(R.id.comment_fra);
		healthzs = (FrameLayout) findViewById(R.id.healthzs_fra);
		commentListView = (FullListView) findViewById(R.id.comment_list);
		likers = (TextView) findViewById(R.id.likers);
		commentInfos.setOnClickListener(listener);
		healthzs.setOnClickListener(listener);

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.pic_loading_)
				.showImageForEmptyUri(R.drawable.pic_empty)
				.showImageOnFail(R.drawable.pic_failure).cacheInMemory(false)
				.cacheOnDisc(true).considerExifParams(true).build();
		likenumTextView = (TextView) findViewById(R.id.like_num);
		commentnumTextView = (TextView) findViewById(R.id.com_num);
		sendCommentButton = (Button) findViewById(R.id.send_msg_btn);
		commentEditText = (EditText) findViewById(R.id.send_msg_editText);
		findViewById(R.id.like_lay).setOnClickListener(listener);
		sendCommentButton.setOnClickListener(listener);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		commentAdapter = new CommentAdapter(context, menuComlist);
		commentListView.setAdapter(commentAdapter);
		foodid = PreferencesFoodsInfo.getfoodId(this);
		foodsDetail(foodid);
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
		getMenuInflater().inflate(R.menu.food_details_, menu);
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

			case R.id.like_lay:
				like2food();
				break;

			case R.id.send_msg_btn:
				sendcomment();
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
				BingLog.i(TAG, "返回:" + response);
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
			isLike = result.getBoolean("isLaud");
			try {
				foods = new Gson().fromJson(obj.toString(), NearFoodBean.class);
				foods.setDistance(""
						+ DistanceUtils.getDistance(MainActivity.Wlongitude,
								MainActivity.Wlatitude,
								foods.getCommercialLon(),
								foods.getCommercialLat()));
				createTime = TimeUtility.getListTime(foods.getCreatetime());
			} catch (Exception e) {
				// TODO: handle exception
				BingLog.e(TAG, "解析错误");
			}

			// comments = new ArrayList<Comment>();
			JSONArray json = result.getJSONArray("overviews");// 改动
			JSONArray commentsArray = result.getJSONArray("comments");

			// int length = json.length();
			// for (int i = 0; i < length; i++) {
			// Comment mcomment = new Comment();
			// obj = json.getJSONObject(i);
			//
			// try {
			// Gson gson = new Gson();
			// mcomment = gson.fromJson(obj.toString(), Comment.class);
			// // foods.setEnergy(mcomment.getEnergy());
			// // foods.setReasonable(mcomment.getReasonable());
			// comments.add(mcomment);
			// } catch (Exception e) {
			// // TODO: handle exception
			// BingLog.e(TAG, "解析错误");
			// }
			// }

			int commlength = commentsArray.length();
			menuComlist.clear();
			/*********** 评论数量 ************/
			for (int i = 0; i < commlength; i++) {
				Log.i(TAG, "评论:" + commentsArray.getJSONObject(i));
				Comment mComment = new Comment();
				try {
					Gson gson = new Gson();
					mComment = gson.fromJson(commentsArray.getJSONObject(i)
							.toString(), Comment.class);
					mComment.setUserheadimage(HealthHttpClient.IMAGE_URL
							+ commentsArray.getJSONObject(i).getString(
									"headimage"));
					mComment.setCn_time(TimeUtility.getListTime(mComment
							.getCreatetime()));
					menuComlist.add(mComment);
				} catch (Exception e) {
					// TODO: handle exception
					BingLog.e(TAG, "解析错误");
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
				BingLog.e(TAG, "解析错误:" + e.getMessage());
			}

			try {
				JSONArray array = result.getJSONArray("laudUser");
				int length = array.length();
				laudUser.clear();
				for (int i = 0; i < length; i++) {
					PersonalInfo info = JsonUtils.getInfo(array
							.getJSONObject(i));
					laudUser.add(info);
				}
			} catch (Exception e) {
				// TODO: handle exception
				BingLog.i("解析错误:" + e.getMessage());
			}

			ishealth = result.getBoolean("ishealth");

			setView();

		} catch (JSONException e) {
			e.printStackTrace();
			BingLog.e(TAG, "解析错误");
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
		createTimeTextView.setText(createTime + "");
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

		if (ishealth) {
			isHealthImageView.setImageResource(isHealths[0]);
		} else {
			isHealthImageView.setImageResource(isHealths[1]);
		}

		likenumTextView.setText(foods.getLaudcount());
		commentnumTextView.setText(foods.getCommentcount());

		imageLoader.displayImage(
				HealthHttpClient.IMAGE_URL + info.getHeadimage(), userHead,
				options);
		userName.setText("" + info.getUsername());
		userSummary.setText("" + foods.getSummary());
		userTag.setText("" + info.getBodyindex());
		scrollView.setVisibility(View.VISIBLE);
		findViewById(R.id.foodhead).setVisibility(View.VISIBLE);
		commentAdapter.notifyDataSetChanged();
		if (laudUser != null) {
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < laudUser.size(); i++) {
				if (i == laudUser.size() - 1) {
					stringBuilder.append(laudUser.get(i).getUsername());
				} else {
					stringBuilder.append(laudUser.get(i).getUsername() + "、");
				}

			}
			likers.setText(stringBuilder.toString());
		}
		setLike();
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
			BingLog.e(TAG, "写入错误");
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
			BingLog.e(TAG, "写入错误");
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
			BingLog.e(TAG, "写入错误");
		}
	}

	private void loopNutritionInfo() {
		try {

			Intent intent = new Intent();
			intent.setClass(context, FoodNutritionActivity.class);
			if (foods != null) {
				intent.putExtra("comment", (Serializable) foods);
			}
			startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			BingLog.e(TAG, "写入错误");
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

	private void setLike() {
		Drawable drawable = null;
		if (isLike) {
			drawable = getResources().getDrawable(R.drawable.ic_shai_liked);
		} else {
			drawable = getResources().getDrawable(R.drawable.ic_shai_like);
		}
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		likenumTextView.setCompoundDrawables(drawable, null, null, null);
	}

	private void like2food() {
		if (isLike) {
			noLikeThis();
		} else {
			likeThis();
		}
	}

	private void likeThis() {
		if (WyyApplication.getInfo() == null) {
			return;
		}
		HealthHttpClient.doHttppostFoodsLand(WyyApplication.getInfo().getId(),
				foodid, new BingHttpHandler() {

					@Override
					protected void onGetSuccess(JSONObject response) {
						// TODO Auto-generated method stub
						if (JsonUtils.isSuccess(response)) {
							isLike = true;
							try {
								foods.setLaudcount(""
										+ (Integer.valueOf(foods.getLaudcount()) + 1));
							} catch (Exception e) {
								// TODO: handle exception
							}
							laudUser.add(WyyApplication.getInfo());
							setView();
							Toast.makeText(context, R.string.like_success,
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(context, R.string.like_failure,
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					protected void onGetFinish() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFailure(Throwable e, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(e, errorResponse);
						Toast.makeText(context, R.string.like_failure,
								Toast.LENGTH_SHORT).show();
					}

				});
	}

	private void noLikeThis() {
		if (WyyApplication.getInfo() == null) {
			return;
		}
		HealthHttpClient.delFoodsLand(WyyApplication.getInfo().getId(), foodid,
				new BingHttpHandler() {

					@Override
					protected void onGetSuccess(JSONObject response) {
						// TODO Auto-generated method stub
						if (JsonUtils.isSuccess(response)) {
							isLike = false;
							try {
								foods.setLaudcount(""
										+ (Integer.valueOf(foods.getLaudcount()) - 1));
							} catch (Exception e) {
								// TODO: handle exception
							}
							delLikeUser();
							setView();
							Toast.makeText(context, R.string.no_like_success,
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(context, R.string.no_like_failure,
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					protected void onGetFinish() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFailure(Throwable e, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(e, errorResponse);
						Toast.makeText(context, R.string.no_like_failure,
								Toast.LENGTH_SHORT).show();
					}

				});
	}

	private void delLikeUser() {
		if (laudUser == null || WyyApplication.getInfo() == null) {
			return;
		}
		for (int i = 0; i < laudUser.size(); i++) {

			if (laudUser.get(i).getUsername()
					.equals(WyyApplication.getInfo().getUsername())) {
				laudUser.remove(i);
				return;
			}
		}
	}

	private void sendcomment() {
		commentEditText.setError(null);
		if (TextUtils.isEmpty(commentEditText.getText().toString())) {
			commentEditText.setError(getString(R.string.nullcontentnotice));
			commentEditText.requestFocus();
			return;
		}

		if (WyyApplication.getInfo() == null) {
			return;
		}
		HealthHttpClient.postFoodComment(WyyApplication.getInfo().getId(),
				foodid, commentEditText.getText().toString(),
				new AsyncHttpResponseHandler());

		Comment comment = new Comment();
		comment.setHeadimage(WyyApplication.getInfo().getHeadimage());
		comment.setUserid(WyyApplication.getInfo().getId());
		comment.setUsername(WyyApplication.getInfo().getUsername());
		comment.setContent(commentEditText.getText().toString());
		comment.setCn_time(getString(R.string.justnow));
		menuComlist.add(comment);
		commentEditText.setText("");
		commentAdapter.notifyDataSetChanged();
	}

}
