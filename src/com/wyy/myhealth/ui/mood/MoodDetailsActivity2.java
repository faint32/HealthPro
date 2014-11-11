package com.wyy.myhealth.ui.mood;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.MoodInfoBean;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.http.BingHttpHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.http.utils.JsonUtils;
import com.wyy.myhealth.imag.utils.LoadImageUtils;
import com.wyy.myhealth.imag.utils.SavePic;
import com.wyy.myhealth.ui.absfragment.utils.TimeUtility;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.ui.customview.FullListView;
import com.wyy.myhealth.ui.fooddetails.CommentAdapter;

public class MoodDetailsActivity2 extends BaseActivity implements
		ActivityInterface {

	private static final String TAG = MoodDetailsActivity2.class
			.getSimpleName();
	private ProgressBar loadingBar;
	private MoodInfoBean moodInfoBean = new MoodInfoBean();

	private ImageView foodpic;

	private ImageView tasteLevel;
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

	private ScrollView scrollView;

	private FrameLayout commentInfos;

	private FrameLayout healthzs;

	private FullListView commentListView;

	private CommentAdapter commentAdapter;

	/**
	 * 点赞用户显示
	 */
	private TextView likers;

	private EditText commentEditText;

	private Button sendCommentButton;

	private String moodid = "";

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
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(R.string.mood_datails);
		loadingBar = new ProgressBar(this);
		actionBar.setCustomView(loadingBar, new ActionBar.LayoutParams(
				Gravity.RIGHT));
		actionBar.setDisplayShowCustomEnabled(true);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		findViewById(R.id.food_head_bottom_lay).setVisibility(View.GONE);
		scrollView = (ScrollView) findViewById(R.id.details_scroll);
		foodpic = (ImageView) findViewById(R.id.foodpic);
		tasteLevel = (ImageView) findViewById(R.id.food_taste_level);
		userHead = (ImageView) findViewById(R.id.userheadimg);
		userName = (TextView) findViewById(R.id.username);
		userTag = (TextView) findViewById(R.id.usertag);
		userSummary = (TextView) findViewById(R.id.user_summary_txt);
		createTimeTextView = (TextView) findViewById(R.id.create_time_txt);
		commentInfos = (FrameLayout) findViewById(R.id.comment_fra);
		healthzs = (FrameLayout) findViewById(R.id.healthzs_fra);
		commentListView = (FullListView) findViewById(R.id.comment_list);
		likers = (TextView) findViewById(R.id.likers);
		// commentInfos.setOnClickListener(listener);
		// healthzs.setOnClickListener(listener);

		likenumTextView = (TextView) findViewById(R.id.like_num);
		commentnumTextView = (TextView) findViewById(R.id.com_num);
		sendCommentButton = (Button) findViewById(R.id.send_msg_btn);
		commentEditText = (EditText) findViewById(R.id.send_msg_editText);
		// findViewById(R.id.like_lay).setOnClickListener(listener);
		// sendCommentButton.setOnClickListener(listener);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(moodid)) {
			moodid = getIntent().getStringExtra("moodid");
		}
		if (TextUtils.isEmpty(moodid) || WyyApplication.getInfo() == null) {
			return;
		}

		commentAdapter = new CommentAdapter(context, moodInfoBean.getComments());
		commentListView.setAdapter(commentAdapter);

		getMoodInfo();

	}

	private void getMoodInfo() {
		HealthHttpClient.getMoodInfo2(WyyApplication.getInfo().getId(), moodid,
				new BingHttpHandler() {

					@Override
					protected void onGetSuccess(JSONObject response) {
						// TODO Auto-generated method stub
						if (JsonUtils.isSuccess(response)) {
							moodInfoBean = JsonUtils.getMoodInfoBean(response);
							moodInfoBean.setCn_time(TimeUtility
									.getListTime(moodInfoBean.getMood()
											.getCreatetime()));
							setView();
						}
					}

					@Override
					protected void onGetFinish() {
						// TODO Auto-generated method stub
						loadingBar.setVisibility(View.GONE);
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						loadingBar.setVisibility(View.VISIBLE);
					}
				});
	}

	private void setView() {
		if (moodInfoBean == null) {
			return;
		}

		if (moodInfoBean.getImg() != null && moodInfoBean.getImg().size() > 0) {
			LoadImageUtils.loadWebImageV_Min(foodpic,
					HealthHttpClient.IMAGE_URL + moodInfoBean.getImg().get(0),
					imageLoadingListener);
		}

		createTimeTextView.setText(moodInfoBean.getCn_time());
		try {
			tasteLevel.setImageResource(ConstantS.LEVEL_POINT[Integer
					.valueOf(moodInfoBean.getMood().getMoodindex())]);
		} catch (Exception e) {
			// TODO: handle exception
		}

		likenumTextView.setText(moodInfoBean.getMood().getLaudcount());
		commentnumTextView.setText(moodInfoBean.getMood().getCommentcount());
		LoadImageUtils.loadWebImageV_Min(userHead, HealthHttpClient.IMAGE_URL
				+ moodInfoBean.getUser().getHeadimage());
		userName.setText("" + moodInfoBean.getUser().getUsername());
		userSummary.setText("" + moodInfoBean.getMood().getContext());
		userTag.setText("" + moodInfoBean.getUser().getBodyindex());
		scrollView.setVisibility(View.VISIBLE);
		findViewById(R.id.foodhead).setVisibility(View.VISIBLE);
		commentAdapter.notifyDataSetChanged();
		if (moodInfoBean.getLaudUser() != null) {
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < moodInfoBean.getLaudUser().size(); i++) {
				if (i == moodInfoBean.getLaudUser().size() - 1) {
					stringBuilder.append(moodInfoBean.getLaudUser().get(i)
							.getUsername());
				} else {
					stringBuilder.append(moodInfoBean.getLaudUser().get(i)
							.getUsername()
							+ "、");
				}

			}
			likers.setText(stringBuilder.toString());
		}
		setLike();

	}

	private void setLike() {
		Drawable drawable = null;
		if (moodInfoBean.isLaud()) {
			drawable = getResources().getDrawable(R.drawable.ic_shai_liked);
		} else {
			drawable = getResources().getDrawable(R.drawable.ic_shai_like);
		}
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		likenumTextView.setCompoundDrawables(drawable, null, null, null);
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
			new Thread(new SaveBitmap(loadedImage)).start();
		}

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			// TODO Auto-generated method stub

		}
	};

	private class SaveBitmap implements Runnable {

		private Bitmap bitmap;

		public SaveBitmap(Bitmap bitmap) {
			super();
			this.bitmap = bitmap;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			SavePic.saveFoodPic2Example(bitmap);
		}

	}

}
