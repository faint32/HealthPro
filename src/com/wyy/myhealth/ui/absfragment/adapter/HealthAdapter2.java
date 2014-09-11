package com.wyy.myhealth.ui.absfragment.adapter;

import java.util.List;

import com.wyy.myhealth.R;
import com.wyy.myhealth.bean.MoodaFoodBean;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.utils.ListUtils;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class HealthAdapter2 extends BaseAdapter {

	private static final int TYPE_FOOD = 1;

	private static final int TYPE_MOOD = 2;

	private List<MoodaFoodBean> list;
	private LayoutInflater inflater;
	private Context context;
	private ShaiItemOnclickListener listener;

	public void setListener(ShaiItemOnclickListener listener) {
		this.listener = listener;
	}

	public HealthAdapter2(List<MoodaFoodBean> list, Context context) {
		this.list = list;
		this.inflater = LayoutInflater.from(context);
		this.context = context;
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
			convertView = inflater.inflate(R.layout.healthpass_item, parent,
					false);
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
		public ListView commenlist;
		public ImageView shai_level_imgImageView;
		public GridView picGridView;
		public GridAdapter2 gridAdapter;
		public CommentAdapter commentAdapter;

	}

	private void setFoodView(ViewHolder holder, int position) {
		holder.shaitagTextView.setText(R.string.share_food);
		if (!TextUtils.isEmpty(list.get(position).getTags())) {
			holder.foodtagTextView.setVisibility(View.VISIBLE);
			holder.foodtagTextView.setText(context.getString(R.string.tags_)
					+ list.get(position).getTags());
		} else {
			holder.foodtagTextView.setVisibility(View.GONE);
		}
		holder.levelTextView.setText(R.string.hps_share_list_2);
		holder.shai_level_imgImageView.setImageResource(ConstantS.levels[list
				.get(position).getTastelevel()]);
		holder.reasonTextView.setText(list.get(position).getSummary());

	}

	private void setMoodView(ViewHolder holder, int position) {
		holder.shaitagTextView.setText(R.string.health_mood);
		holder.foodtagTextView.setVisibility(View.GONE);
		holder.levelTextView.setText(R.string.moodindex);
		try {

			holder.shai_level_imgImageView
					.setImageResource(ConstantS.levels[list.get(position)
							.getMoodindex()]);
		} catch (Exception e) {
			// TODO: handle exception
		}

		holder.reasonTextView.setText(list.get(position).getContext());

	}

	private void setView(ViewHolder holder, final int position) {
		holder.timeTextView.setVisibility(View.GONE);
		if (list.get(position).getImg() != null) {
			setGridView(holder.picGridView, list.get(position).getImg().size());
			holder.gridAdapter = new GridAdapter2(context, list.get(position)
					.getImg());
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
								listener.onPicClick(position, mposition);
							}

						}
					});
		}

		holder.commenImageView.setFocusable(false);
		holder.commenImageView.setFocusableInTouchMode(false);

		holder.day.setText(list.get(position).getDay());
		holder.month.setText(list.get(position).getMonth());
		holder.commenImageView.setVisibility(View.GONE);
		if (null != list.get(position).getComment()) {
			holder.commentAdapter = new CommentAdapter(context, list.get(
					position).getComment());
			holder.commenlist.setAdapter(holder.commentAdapter);
			// ListUtils.setListViewHeightBasedOnChildren(holder.commenlist);
		}

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
