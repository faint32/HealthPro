package com.wyy.myhealth.ui.absfragment.adapter;

import java.util.List;

import javax.print.attribute.standard.Fidelity;

import com.wyy.myhealth.R;
import com.wyy.myhealth.bean.MoodaFoodBean;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.imag.utils.LoadImageUtils;
import com.wyy.myhealth.utils.BingLog;
import com.wyy.myhealth.utils.ListUtils;

import android.R.integer;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ShaiYiSaiAdapter2 extends BaseAdapter {

	private static final String TAG = ShaiYiSaiAdapter2.class.getSimpleName();

	private static final int TYPE_FOOD = 1;

	private static final int TYPE_MOOD = 2;
	private List<MoodaFoodBean> list;
	private LayoutInflater inflater;
	private Context context;
	private PopupWindow popupWindow;
	private View popView;
	private ImageView collectImageView;
	private ImageView commentImageView;
	private int realPostion = 0;
	private static int pop_Width = 0;
	int[] location = new int[2];
	private ShaiItemOnclickListener listener;

	public void setListener(ShaiItemOnclickListener listener) {
		this.listener = listener;
	}

	public ShaiYiSaiAdapter2(List<MoodaFoodBean> list, Context context) {
		this.list = list;
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		popView = inflater.inflate(R.layout.shai_menu, null);
		commentImageView = (ImageView) popView.findViewById(R.id.shai_pinglun);
		collectImageView = (ImageView) popView.findViewById(R.id.shai_shoucang);
		popupWindow = new PopupWindow(popView,
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(false);
		commentImageView.setFocusable(false);
		collectImageView.setFocusable(false);
		float screen_Width=context.getResources().getDisplayMetrics().widthPixels;
		pop_Width=(int) (254*(screen_Width/720));
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

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return list.get(position).getType();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.shai_item, parent, false);
			holder.userheadImageView = (ImageView) convertView
					.findViewById(R.id.shai_head_img);
			holder.usernameTextView = (TextView) convertView
					.findViewById(R.id.shai_username_txt);
			holder.timeTextView = (TextView) convertView
					.findViewById(R.id.shai_time_txt);
			holder.shaitagTextView = (TextView) convertView
					.findViewById(R.id.shai_top_txt);
			holder.foodtagTextView = (TextView) convertView
					.findViewById(R.id.shai_center_txt);
			holder.levelTextView = (TextView) convertView
					.findViewById(R.id.shai_bottom_txt);
			holder.shai_level_imgImageView = (ImageView) convertView
					.findViewById(R.id.shai_start_img);
			holder.commenImageView = (ImageButton) convertView
					.findViewById(R.id.shai_pinglun_img);
			holder.reasonTextView = (TextView) convertView
					.findViewById(R.id.shai_reason_txt);
			holder.commenlist = (ListView) convertView
					.findViewById(R.id.comment_list);
			holder.picGridView = (GridView) convertView
					.findViewById(R.id.shai_gridView1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		switch (getItemViewType(position)) {
		case TYPE_FOOD:
			setFoodView(holder, position);
			break;
		case TYPE_MOOD:
			setMoodView(holder, position);
			break;

		default:
			break;
		}

		setView(holder, position);

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

		if (list.get(position).isAdv()) {
			convertView.setBackgroundResource(R.drawable.ic_tap);
		} else {
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.transparent));
		}

		return convertView;
	}

	public static class ViewHolder {

		public ImageView userheadImageView;
		public TextView usernameTextView;
		public TextView timeTextView;
		public TextView shaitagTextView;
		public TextView foodtagTextView;
		public TextView levelTextView;
		public ImageButton commenImageView;
		public TextView reasonTextView;
		public ListView commenlist;
		public ImageView shai_level_imgImageView;
		public GridView picGridView;
		public GridAdapter2 gridAdapter;
		public CommentAdapter commentAdapter;

	}

	private void setFoodView(ViewHolder holder, int position) {
		holder.shaitagTextView.setText(R.string.share_food);
//		if (!TextUtils.isEmpty(list.get(position).getTags())) {
//			holder.foodtagTextView.setVisibility(View.VISIBLE);
//			holder.foodtagTextView.setText(context.getString(R.string.tags_)
//					+ list.get(position).getTags());
//		} else {
//			holder.foodtagTextView.setVisibility(View.GONE);
//		}
		holder.levelTextView.setText(R.string.hps_share_list_2);
		holder.shai_level_imgImageView.setImageResource(ConstantS.levels[list
				.get(position).getTastelevel()]);
		holder.reasonTextView.setText(list.get(position).getSummary());

	}

	private void setMoodView(ViewHolder holder, int position) {
		holder.shaitagTextView.setText(R.string.share_mood);
		holder.foodtagTextView.setVisibility(View.GONE);
		holder.levelTextView.setText(R.string.moodindex);
		try {
			holder.levelTextView.setVisibility(View.VISIBLE);
			holder.shai_level_imgImageView.setVisibility(View.VISIBLE);
			BingLog.i(TAG, "指数:" + list.get(position).getMoodindex());
			holder.shai_level_imgImageView
					.setImageResource(ConstantS.levels[list.get(position)
							.getMoodindex()]);
		} catch (Exception e) {
			// TODO: handle exception
			holder.levelTextView.setVisibility(View.GONE);
			holder.shai_level_imgImageView.setVisibility(View.GONE);
		}

		holder.reasonTextView.setText(list.get(position).getContext());

	}

	private void setView(ViewHolder holder, int position) {
		final int adaposition = position;
		LoadImageUtils.loadImage4ImageV(holder.userheadImageView,
				HealthHttpClient.IMAGE_URL
						+ list.get(position).getUser().getHeadimage());
		holder.timeTextView.setText(list.get(position).getCn_time());
		holder.usernameTextView.setText(list.get(position).getUser()
				.getUsername());

		if (list.get(position).getImg() != null) {
			setGridView(holder.picGridView, list.get(position).getImg().size());
			holder.gridAdapter = new GridAdapter2(context, list.get(position)
					.getImg());
			holder.picGridView.setAdapter(holder.gridAdapter);
			if (!TextUtils.isEmpty(list.get(position).getTags())) {
				holder.gridAdapter.setFoodtag(list.get(position).getTags());
			}else {
				holder.gridAdapter.setFoodtag("");
			} 
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
								listener.onPicClick(adaposition, mposition);
							}

						}
					});
		}

		if (null != list.get(position).getComment()) {
			holder.commentAdapter = new CommentAdapter(context, list.get(
					position).getComment());
			holder.commenlist.setAdapter(holder.commentAdapter);
			// ListUtils.setListViewHeightBasedOnChildren(holder.commenlist);
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
					realPostion = adaposition;
					int width = popView.getWidth();
					if (width == 0) {
						width = pop_Width;
					}

					if (popupWindow.isShowing()) {
						popupWindow.dismiss();
						v.getLocationOnScreen(location);
						popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
								location[0] - width, location[1]);
					} else {
						BingLog.i(TAG, "window:" + width);
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
					listener.onUserPicClick(adaposition);
				}

			}
		});

		collectImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
				if (listener != null) {
					listener.onCollectClick(realPostion);
				}

			}
		});

		commentImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
				if (listener != null) {
					listener.onCommentClick(realPostion);
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

}
