package com.wyy.myhealth.ui.yaoyingyang;

import java.util.ArrayList;
import java.util.List;

import com.wyy.myhealth.R;
import com.wyy.myhealth.bean.NearFoodBean;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.imag.utils.LoadImageUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class YaoGridAdapter extends BaseAdapter {

	private List<NearFoodBean> list = new ArrayList<>();

	private LayoutInflater inflater;

	public YaoGridAdapter(List<NearFoodBean> list, Context context) {
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
			convertView = inflater.inflate(R.layout.yao_grid_item, parent,
					false);
			holder.distance = (TextView) convertView
					.findViewById(R.id.diatance);
			holder.foodimg = (ImageView) convertView
					.findViewById(R.id.food_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		NearFoodBean nearFoodBean = list.get(position);
		if (null != nearFoodBean) {
			holder.distance.setText(nearFoodBean.getDistance() + "km");
			LoadImageUtils.loadImage4ImageV(holder.foodimg,
					nearFoodBean.getFoodpic());
		}
		return convertView;
	}

	public class ViewHolder {
		public TextView distance;
		public ImageView foodimg;
	}

}
