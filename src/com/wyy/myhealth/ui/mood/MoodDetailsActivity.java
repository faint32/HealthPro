package com.wyy.myhealth.ui.mood;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.CommentBean;
import com.wyy.myhealth.bean.MoodaFoodBean;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.JsonHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.http.utils.JsonUtils;
import com.wyy.myhealth.imag.utils.LoadImageUtils;
import com.wyy.myhealth.support.collect.CollectUtils;
import com.wyy.myhealth.ui.absfragment.adapter.CommentAdapter;
import com.wyy.myhealth.ui.absfragment.adapter.GridAdapter2;
import com.wyy.myhealth.ui.absfragment.adapter.ShaiYiSaiAdapter2.ShaiItemOnclickListener;
import com.wyy.myhealth.ui.absfragment.adapter.ShaiYiSaiAdapter2.ViewHolder;
import com.wyy.myhealth.ui.absfragment.utils.TimeUtility;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.ui.photoPager.PhotoPagerActivity;
import com.wyy.myhealth.utils.BingLog;

/**
 * @deprecated 已过期
 * @author lyl
 * 
 */
public class MoodDetailsActivity extends BaseActivity implements
		ActivityInterface {

	private static final String TAG = MoodDetailsActivity.class.getSimpleName();
	private ProgressBar loadingBar;
	private ViewHolder holder;
	private MoodaFoodBean moodaFoodBean;
	private PopupWindow popupWindow;
	private View popView;
	private ImageView collectImageView;
	private ImageView commentImageView;
	int[] location = new int[2];
	private ShaiItemOnclickListener listener;
	// 评论View
	private View sendView;
	// 评论编辑
	private EditText sendEditText;
	// 评论按钮
	private Button sendButton;

	private String moodid = "";

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.mood_datails);
		loadingBar = new ProgressBar(this);
		getSupportActionBar().setCustomView(loadingBar,
				new ActionBar.LayoutParams(Gravity.RIGHT));
		getSupportActionBar().setDisplayShowCustomEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mood_details);
		initView();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		holder = new ViewHolder();
		holder.userheadImageView = (ImageView) findViewById(R.id.shai_head_img);
		holder.usernameTextView = (TextView) findViewById(R.id.shai_username_txt);
		holder.timeTextView = (TextView) findViewById(R.id.shai_time_txt);
		holder.shaitagTextView = (TextView) findViewById(R.id.shai_top_txt);
		holder.foodtagTextView = (TextView) findViewById(R.id.shai_center_txt);
		holder.levelTextView = (TextView) findViewById(R.id.shai_bottom_txt);
		holder.shai_level_imgImageView = (ImageView) findViewById(R.id.shai_start_img);
		holder.commenImageView = (ImageButton) findViewById(R.id.shai_pinglun_img);
		holder.reasonTextView = (TextView) findViewById(R.id.shai_reason_txt);
		holder.commenlist = (ListView) findViewById(R.id.comment_list);
		holder.picGridView = (GridView) findViewById(R.id.shai_gridView1);

		popView = getLayoutInflater().inflate(R.layout.shai_menu, null);
		commentImageView = (ImageView) popView.findViewById(R.id.shai_pinglun);
		collectImageView = (ImageView) popView.findViewById(R.id.shai_shoucang);
		popupWindow = new PopupWindow(popView,
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.WRAP_CONTENT);
		initListener();
		sendButton = (Button) findViewById(R.id.send_msg_btn);
		sendEditText = (EditText) findViewById(R.id.send_msg_editText);
		sendButton.setOnClickListener(listener2);
		initData();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		moodid = getIntent().getStringExtra("moodid");
		HealthHttpClient.getMoodInfo(WyyApplication.getInfo().getId(), moodid,
				handler);

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

	private JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(response);
			parseJson(response);
		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			loadingBar.setVisibility(View.VISIBLE);
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);
		}

		public void onFinish() {
			loadingBar.setVisibility(View.GONE);
		};

	};

	private void parseJson(JSONObject response) {
		BingLog.i(TAG, "心情详情:" + response);
		if (JsonUtils.isSuccess(response)) {
			JSONArray array;
			try {
				array = response.getJSONArray("foods");
				int length = array.length();
				for (int i = 0; i < length; i++) {
					moodaFoodBean = JsonUtils.getMoodaFoodBean(array
							.getJSONObject(i));
					moodaFoodBean.setCn_time(TimeUtility
							.getListTime(moodaFoodBean.getCreatetime()));
				}

				if (moodaFoodBean != null) {
					setView(holder);
					setMoodView(holder);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private void setView(ViewHolder holder) {
		LoadImageUtils.loadImage4ImageV(holder.userheadImageView,
				HealthHttpClient.IMAGE_URL
						+ moodaFoodBean.getUser().getHeadimage());
		holder.timeTextView.setText(moodaFoodBean.getCn_time());
		holder.usernameTextView.setText(moodaFoodBean.getUser().getUsername());

		if (moodaFoodBean.getImg() != null) {
			setGridView(holder.picGridView, moodaFoodBean.getImg().size());
			holder.gridAdapter = new GridAdapter2(context,
					moodaFoodBean.getImg());
			holder.picGridView.setAdapter(holder.gridAdapter);
			holder.picGridView.setVisibility(View.VISIBLE);
			holder.picGridView.setFocusable(false);
			holder.picGridView.setFocusableInTouchMode(false);
			holder.picGridView
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int mposition, long id) {
							// TODO Auto-generated method stub
							if (listener != null) {
								listener.onPicClick(0, mposition);
							}

						}
					});
		}

		if (null != moodaFoodBean.getComment()) {
			holder.commentAdapter = new CommentAdapter(context,
					moodaFoodBean.getComment());
			holder.commenlist.setAdapter(holder.commentAdapter);
		}

		holder.commenImageView.setFocusable(false);
		holder.commenImageView.setFocusableInTouchMode(false);
		holder.commenImageView.setFocusable(false);
		holder.commenImageView.setFocusableInTouchMode(false);
		holder.commenImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (listener != null) {
					int width = popView.getWidth();
					if (width == 0) {
						width = 254;
					}

					if (popupWindow.isShowing()) {
						popupWindow.dismiss();
						v.getLocationOnScreen(location);
						popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
								location[0] - width, location[1]);
					} else {
						v.getLocationOnScreen(location);
						popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
								location[0] - width, location[1]);
					}
				}

			}
		});

		holder.userheadImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (listener != null) {
					listener.onUserPicClick(0);
				}

			}
		});

		collectImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
				if (listener != null) {
					listener.onCollectClick(0);
				}

			}
		});

		commentImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
				if (listener != null) {
					listener.onCommentClick(0);
				}
			}
		});

	}

	private void setGridView(GridView gridView, int m) {
		switch (m) {
		case 1:

			gridView.setNumColumns(1);

			break;

		case 2:

			gridView.setNumColumns(2);

			break;

		case 3:

			gridView.setNumColumns(3);

			break;

		case 4:

			gridView.setNumColumns(2);
			gridView.setVerticalSpacing(20);

			break;

		default:
			break;
		}
	}

	private void setMoodView(ViewHolder holder) {
		holder.shaitagTextView.setText(R.string.share_mood);
		holder.foodtagTextView.setVisibility(View.GONE);
		holder.levelTextView.setText(R.string.moodindex);
		try {
			holder.levelTextView.setVisibility(View.VISIBLE);
			holder.shai_level_imgImageView.setVisibility(View.VISIBLE);
			BingLog.i(TAG, "指数:" + moodaFoodBean.getMoodindex());
			holder.shai_level_imgImageView
					.setImageResource(ConstantS.levels[moodaFoodBean
							.getMoodindex()]);
		} catch (Exception e) {
			// TODO: handle exception
			holder.levelTextView.setVisibility(View.GONE);
			holder.shai_level_imgImageView.setVisibility(View.GONE);
		}

		holder.reasonTextView.setText(moodaFoodBean.getContext());

	}

	private void initListener() {
		this.listener = new ShaiItemOnclickListener() {

			@Override
			public void onUserPicClick(int position) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPicClick(int listPostino, int picPostion) {
				// TODO Auto-generated method stub
				try {
					if (moodaFoodBean.getImg() != null) {
						List<String> list = moodaFoodBean.getImg();
						Intent intent = new Intent();
						intent.putStringArrayListExtra("imgurls",
								(ArrayList<String>) list);
						intent.putExtra("postion", picPostion);
						intent.setClass(context, PhotoPagerActivity.class);
						startActivity(intent);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			@Override
			public void onCommentClick(int position) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCollectClick(int position) {
				// TODO Auto-generated method stub
				if (moodaFoodBean.getType() == ConstantS.TYPE_FOOD) {
					String foodsid = moodaFoodBean.getId();
					CollectUtils.collectFood(foodsid, context);

				} else if (moodaFoodBean.getType() == ConstantS.TYPE_MOOD) {
					String moodsid = moodaFoodBean.getId();
					CollectUtils.postMoodCollect(moodsid, context);
				}
			}
		};
	}

	private OnClickListener listener2 = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {

			case R.id.send_msg_btn:
				if (!TextUtils.isEmpty(moodid)) {
					sendMoodComment(moodid);
				}

				break;

			default:
				break;
			}
		}
	};

	/**
	 * 发送评论
	 * 
	 * @param foodsid
	 *            食物ID
	 */
	private void sendMoodComment(String moodsid) {
		if (TextUtils.isEmpty(sendEditText.getText().toString())) {
			Toast.makeText(context, R.string.nullcontentnotice,
					Toast.LENGTH_LONG).show();
			return;
		}
		HealthHttpClient.postMoodComment(WyyApplication.getInfo().getId(),
				moodsid, sendEditText.getText().toString(), commentHandler);
	}

	private AsyncHttpResponseHandler commentHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();

			CommentBean commentBean = new CommentBean();
			commentBean.setContent(sendEditText.getText().toString());
			commentBean.setUser(WyyApplication.getInfo());
			if (moodaFoodBean == null) {
				return;
			}
			moodaFoodBean.getComment().add(commentBean);
			holder.commentAdapter.notifyDataSetChanged();
			sendEditText.setText("");
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
			BingLog.i(TAG, "返回:" + content);
			Toast.makeText(context, R.string.comment_success_,
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);
			Toast.makeText(context, R.string.comment_faliure, Toast.LENGTH_LONG)
					.show();
		}

	};

}
