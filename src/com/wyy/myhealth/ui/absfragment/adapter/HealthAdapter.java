package com.wyy.myhealth.ui.absfragment.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wyy.myhealth.R;
import com.wyy.myhealth.ui.absfragment.utils.TextViewUtils;
import com.wyy.myhealth.utils.BingLog;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
/**
 * @deprecated
 * @author lyl
 *
 */
public class HealthAdapter extends BaseAdapter {

	private List<Map<String, Object>> list;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	private PopupWindow popupWindow;
	private View popView;
	private ImageView collectImageView;
	private ImageView commentImageView;
	int[] location = new int[2];

	public ImageLoader getImageLoader() {
		return imageLoader;
	}

	private Context context;
	private static int[] levels = { R.drawable.shai_star1,
			R.drawable.shai_star2, R.drawable.shai_star3,
			R.drawable.shai_star4, R.drawable.shai_star5 };

	private ShaiItemOnclickListener listener;

	public HealthAdapter(Context context, List<Map<String, Object>> list) {
		this.list = list;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.imageLoader = ImageLoader.getInstance();

		popView = inflater.inflate(R.layout.shai_menu, null);
		commentImageView = (ImageView) popView.findViewById(R.id.shai_pinglun);
		collectImageView = (ImageView) popView.findViewById(R.id.shai_shoucang);
		popupWindow = new PopupWindow(popView,
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(false);
		commentImageView.setFocusable(false);
		collectImageView.setFocusable(false);

	}

	public void setOnClickListener(ShaiItemOnclickListener listener) {
		this.listener = listener;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int newposition = position;

		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.healthpass_ada_,parent, false);
			holder.usernameTextView = (TextView) convertView
					.findViewById(R.id.shai_username_txt);
			holder.timeTextView = (TextView) convertView
					.findViewById(R.id.shai_time_txt);
			holder.day = (TextView) convertView.findViewById(R.id.hps_day);
			holder.month = (TextView) convertView.findViewById(R.id.hps_month);
			holder.shaitagTextView = (TextView) convertView
					.findViewById(R.id.shai_top_txt);
			holder.foodtagTextView = (TextView) convertView
					.findViewById(R.id.shai_center_txt);
			holder.levelTextView = (TextView) convertView
					.findViewById(R.id.shai_bottom_txt);
			holder.shai_level_imgImageView = (ImageView) convertView
					.findViewById(R.id.shai_start_img);
			// holder.shoucangImageView = (ImageView) convertView
			// .findViewById(R.id.shai_shoucang_img);
			// holder.shoucangcounTextView = (TextView) convertView
			// .findViewById(R.id.shai_shoucang_count_txt);
			// holder.zanImageView = (ImageView) convertView
			// .findViewById(R.id.shai_zan_img);
			// holder.zanTextView = (TextView) convertView
			// .findViewById(R.id.shai_zan_count_txt);
			holder.commenImageView = (ImageButton) convertView
					.findViewById(R.id.shai_pinglun_img);
			// holder.commencounTextView = (TextView) convertView
			// .findViewById(R.id.shai_pinglun_count_txt);
			holder.reasonTextView = (TextView) convertView
					.findViewById(R.id.shai_reason_txt);
			holder.commeninfoTextView = (TextView) convertView
					.findViewById(R.id.shai_comment_txt);

			// holder.shai_centerLayout = (LinearLayout) convertView
			// .findViewById(R.id.shai_center_layout);

			// gridView
			holder.picGridView = (GridView) convertView
					.findViewById(R.id.shai_gridView1);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (list.get(position).containsKey("style")) {
			holder.shaitagTextView
					.setText("" + list.get(position).get("style"));
		}
		if (list.get(position).containsKey("tags")) {
			holder.foodtagTextView.setText("标签:"
					+ list.get(position).get("tags"));
			if (TextUtils.isEmpty(list.get(position).get("tags").toString())) {
				holder.foodtagTextView.setVisibility(View.GONE);
			} else {
				holder.foodtagTextView.setVisibility(View.VISIBLE);
			}

		} else {
			holder.foodtagTextView.setVisibility(View.GONE);
		}
		if (list.get(position).containsKey("headimage")
				&& !TextUtils.isEmpty(list.get(position).get("headimage")
						.toString())) {
		}
		if (list.get(position).containsKey("time")) {
			holder.timeTextView
					.setText("" + list.get(position).get("new_time"));
		}
		if (list.get(position).containsKey("tastelevel")) {
			int index = Integer.valueOf(list.get(position).get("tastelevel")
					.toString());

			holder.levelTextView.setText("口味:");
			if (index > 0) {
				holder.shai_level_imgImageView
						.setImageResource(levels[index - 1]);
			} else {
				holder.shai_level_imgImageView.setImageResource(levels[0]);
			}

		}

		if (list.get(position).containsKey("day")) {
			holder.day.setText("" + list.get(position).get("day"));
			holder.day.setVisibility(View.VISIBLE);
		} else {
			holder.day.setVisibility(View.INVISIBLE);
		}

		if (list.get(position).containsKey("month")) {
			holder.month.setText("" + list.get(position).get("month"));
			holder.month.setVisibility(View.VISIBLE);
		} else {
			holder.month.setVisibility(View.INVISIBLE);
		}

		if (list.get(position).containsKey("moodindex")) {
			int index = Integer.valueOf(list.get(position).get("moodindex")
					.toString());
			holder.levelTextView.setText("指数:");
			Log.e("字数", "指数个数:" + index);
			if (index > 0) {
				holder.shai_level_imgImageView.setVisibility(View.VISIBLE);
				holder.shai_level_imgImageView
						.setImageResource(levels[index - 1]);
			} else {
				holder.shai_level_imgImageView.setVisibility(View.GONE);
				holder.shai_level_imgImageView.setImageResource(levels[0]);
				holder.levelTextView.setVisibility(View.GONE);
			}
		}
		if (list.get(position).containsKey("summary")) {
			holder.reasonTextView.setText(""
					+ list.get(position).get("summary"));
		}

		if (list.get(position).containsKey("context")) {
			holder.reasonTextView.setText(""
					+ list.get(position).get("context"));
		}
		if (list.get(position).containsKey("comment")) {
			holder.commeninfoTextView.setText(""
					+ list.get(position).get("comment"));
			holder.commeninfoTextView.setText(TextViewUtils
					.getSpannableStringBuilder(""
							+ list.get(position).get("comment")));
			holder.commeninfoTextView.setVisibility(View.VISIBLE);
		} else {
			holder.commeninfoTextView.setVisibility(View.GONE);
		}

		// if (list.get(position).containsKey("username")) {
		// holder.usernameTextView.setText(""
		// + list.get(position).get("username"));
		// }

		if (list.get(position).containsKey("grid_pic")) {
			List<String> picList = new ArrayList<String>();
			picList = (List<String>) list.get(position).get("grid_pic");
			if (null != picList) {
				if (picList.size() > 0) {

					setGridView(holder.picGridView, picList.size());
					holder.gridAdapter = new GridAdapter(context, picList);
					holder.picGridView.setAdapter(holder.gridAdapter);
					holder.picGridView.setVisibility(View.VISIBLE);
				} else {
					holder.picGridView.setVisibility(View.GONE);
				}
			} else {
				holder.picGridView.setVisibility(View.GONE);
			}

		} else {
			holder.picGridView.setVisibility(View.GONE);
		}

		holder.picGridView.setFocusable(false);
		holder.picGridView.setFocusableInTouchMode(false);
		holder.picGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (listener != null) {
					listener.onPicClick(newposition, position);
				}

			}
		});

		holder.commenImageView.setFocusable(false);
		holder.commenImageView.setFocusableInTouchMode(false);
		holder.commenImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (listener != null) {
					BingLog.i(ShaiYiSaiAdapter.class.getSimpleName(), "宽度:"
							+ popView.getWidth());
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

		collectImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
				if (listener != null) {
					listener.onCollectClick(newposition);
				}

			}
		});

		commentImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
				if (listener != null) {
					listener.onCommentClick(newposition);
				}
			}
		});

		// holder.zanImageView.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// if (listener!=null) {
		// listener.onZanClick(newposition);
		// }
		//
		// }
		// });

		convertView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
					return true;
				} else {
					return false;
				}

			}
		});

		return convertView;
	}

	public class ViewHolder {

		public TextView usernameTextView;
		public TextView timeTextView;
		public TextView shaitagTextView;
		public TextView foodtagTextView;
		public TextView levelTextView;
		public TextView day;
		public TextView month;
		public ImageButton commenImageView;
		public TextView reasonTextView;
		public TextView commeninfoTextView;
		public ImageView shai_level_imgImageView;
		public GridView picGridView;
		public GridAdapter gridAdapter;

	}

	/**
	 * 点击接口
	 * 
	 * @author lyl
	 * 
	 */
	public interface ShaiItemOnclickListener {
		/**
		 * 食物点击方法
		 */
		public void onUserPicClick(int position);

		/**
		 * 评论点击
		 * 
		 * @param position
		 *            位置
		 */
		public void onCommentClick(int position);

		/**
		 * 赞点击
		 * 
		 * @param position
		 *            点击位置
		 */
		public void onZanClick(int position);

		/**
		 * 收藏点击
		 * 
		 * @param position
		 *            点击位置
		 */
		public void onCollectClick(int position);

		/**
		 * 图片点击
		 * 
		 * @param listPostino
		 *            list位置
		 * @param picPostion
		 *            图片位置
		 */
		public void onPicClick(int listPostino, int picPostion);

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

}
