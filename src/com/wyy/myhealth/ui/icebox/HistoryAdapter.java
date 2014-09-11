package com.wyy.myhealth.ui.icebox;

import java.util.List;

import com.wyy.myhealth.R;
import com.wyy.myhealth.bean.IceBoxFoodBean;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.imag.utils.LoadImageUtils;
import com.wyy.myhealth.ui.icebox.utils.FoodTypeUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HistoryAdapter extends BaseAdapter {

	private List<IceBoxFoodBean> list;

	private LayoutInflater inflater;

	public HistoryAdapter(List<IceBoxFoodBean> list, Context context) {
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
			convertView = inflater.inflate(R.layout.ice_history_item, parent,
					false);
			holder.foodpic = (ImageView) convertView.findViewById(R.id.foodpic);
			holder.fooddate = (TextView) convertView
					.findViewById(R.id.food_time);
			holder.foodname = (TextView) convertView
					.findViewById(R.id.food_type_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		LoadImageUtils.loadImage4ImageV(holder.foodpic,
				HealthHttpClient.IMAGE_URL + list.get(position).getFoodpic());

		holder.fooddate.setText(list.get(position).getCreatetime());
		holder.foodname.setText(""
				+ FoodTypeUtils.getfoodtype(list.get(position).getType()) + ":"
				+ list.get(position).getName());

		return convertView;
	}

	public class ViewHolder {

		public ImageView foodpic;

		public TextView foodname;

		public TextView fooddate;

	}

}
