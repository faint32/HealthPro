package com.wyy.myhealth.ui.healthbar;

import java.util.List;

import com.wyy.myhealth.R;
import com.wyy.myhealth.bean.MoodaFoodBean;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.imag.utils.LoadImageUtils;
import com.wyy.myhealth.utils.BingLog;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HealthBarAdapter extends BaseAdapter {

	private static final int TYPE_FOOD = 1;

	private static final int TYPE_MOOD = 2;

	private List<MoodaFoodBean> list;
	private LayoutInflater inflater;
	private Context context;
	private ShaiItemOnclickListener listener;

	public void setListener(ShaiItemOnclickListener listener) {
		this.listener = listener;
	}

	public HealthBarAdapter(List<MoodaFoodBean> list, Context context) {
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
			convertView = inflater.inflate(R.layout.health_bar_item, parent,
					false);
			holder.timeTextView = (TextView) convertView
					.findViewById(R.id.time_txt);
			holder.tagTextView = (TextView) convertView
					.findViewById(R.id.content_tag);
			holder.levelTagTextView = (TextView) convertView
					.findViewById(R.id.content_level_tag);
			holder.contentTextView = (TextView) convertView
					.findViewById(R.id.content_txt);
			holder.level_imgImageView = (ImageView) convertView
					.findViewById(R.id.content_level_img);
			holder.contentImg = (ImageView) convertView
					.findViewById(R.id.hpb_img);
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

		public TextView timeTextView;
		public TextView tagTextView;
		public TextView levelTagTextView;
		public ImageView contentImg;
		public TextView contentTextView;
		public ImageView level_imgImageView;

	}

	private void setFoodView(ViewHolder holder, int position) {
		holder.tagTextView.setText(R.string.share_food);
		holder.levelTagTextView.setText(R.string.hps_share_list_2);
		holder.level_imgImageView.setImageResource(ConstantS.levels[list.get(
				position).getTastelevel()]);
		holder.contentTextView.setText(list.get(position).getSummary());

	}

	private void setMoodView(ViewHolder holder, int position) {
		holder.tagTextView.setText(R.string.health_mood);
		holder.levelTagTextView.setText(R.string.moodindex);
		try {
			holder.level_imgImageView.setImageResource(ConstantS.levels[list
					.get(position).getMoodindex()]);
			BingLog.i("心情指数:" + list.get(position).getMoodindex()
					+ list.get(position).getContext());
		} catch (Exception e) {
			// TODO: handle exception
			BingLog.e("心情错误:" + e.getMessage());
		}

		holder.contentTextView.setText(list.get(position).getContext());

	}

	private void setView(ViewHolder holder, final int position) {
		MoodaFoodBean moodaFoodBean = list.get(position);
		if (TextUtils.isEmpty(moodaFoodBean.getDay())) {
			holder.timeTextView.setVisibility(View.GONE);
		} else {
			holder.timeTextView.setVisibility(View.VISIBLE);
			holder.timeTextView.setText(moodaFoodBean.getMonth()
					+ moodaFoodBean.getDay());
		}
		if (moodaFoodBean.getImg() != null && moodaFoodBean.getImg().size() > 0) {
			holder.contentImg.setVisibility(View.VISIBLE);
			LoadImageUtils.loadImage4ImageV(holder.contentImg,
					HealthHttpClient.IMAGE_URL + moodaFoodBean.getImg().get(0));
		} else {
			holder.contentImg.setVisibility(View.GONE);
		}

		// holder.day.setText(list.get(position).getDay());
		// holder.month.setText(list.get(position).getMonth());

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
