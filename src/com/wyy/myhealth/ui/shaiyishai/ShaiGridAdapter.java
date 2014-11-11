package com.wyy.myhealth.ui.shaiyishai;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyy.myhealth.R;
import com.wyy.myhealth.bean.MoodaFoodBean;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.imag.utils.LoadImageUtils;

public class ShaiGridAdapter extends BaseAdapter {

	private List<MoodaFoodBean> list = new ArrayList<>();

	private LayoutInflater inflater;

	public ShaiGridAdapter(List<MoodaFoodBean> list, Context context) {
		super();
		this.list = list;
		this.inflater = LayoutInflater.from(context);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.shai_grid_item, parent,
					false);
			holder.like = (TextView) convertView.findViewById(R.id.like_count);
			holder.foodimg = (ImageView) convertView
					.findViewById(R.id.food_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		MoodaFoodBean moodaFoodBean = list.get(position);
		if (null != moodaFoodBean) {
			holder.like.setText(moodaFoodBean.getLandcount());
			List<String> imgsList = moodaFoodBean.getImg();
			if (imgsList != null && imgsList.size() > 0) {
				LoadImageUtils.loadWebImageV_Min(holder.foodimg,
						HealthHttpClient.IMAGE_URL + imgsList.get(0));
			}

		}
		return convertView;
	}

	public class ViewHolder {
		public TextView like;
		public ImageView foodimg;
	}

}
