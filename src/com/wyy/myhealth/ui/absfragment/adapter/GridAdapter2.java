package com.wyy.myhealth.ui.absfragment.adapter;

import java.util.List;

import com.wyy.myhealth.R;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.imag.utils.LoadImageUtils;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter2 extends BaseAdapter {

	private List<String> list;

	private LayoutInflater inflater;

	private String foodtag="";
	
	public void setFoodtag(String foodtag) {
		this.foodtag = foodtag;
	}

	public GridAdapter2(Context context, List<String> list) {
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
			holder=new ViewHolder();
			switch (list.size()) {
			case 1:
				convertView = inflater.inflate(R.layout.pic_grid_, null);
				holder.tagTextView=(TextView)convertView.findViewById(R.id.tag);
				break;

			case 2:
				convertView = inflater.inflate(R.layout.pic_grid_t, null);
				break;

			case 3:
				convertView = inflater.inflate(R.layout.pic_gird, null);
				break;

			default:

				convertView = inflater.inflate(R.layout.pic_grid_f, null);

				break;
			}

			holder.pic = (ImageView) convertView
					.findViewById(R.id.shai_pic_muilt);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		LoadImageUtils.loadWebImageV_Min(holder.pic, HealthHttpClient.IMAGE_URL
				+ list.get(position));
		if (null!=holder.tagTextView) {
			if (!TextUtils.isEmpty(foodtag)) {
				holder.tagTextView.setText(foodtag);
				holder.tagTextView.setVisibility(View.VISIBLE);
			}else {
				holder.tagTextView.setVisibility(View.GONE);
			}
		}
		
		return convertView;
	}

	public class ViewHolder {
		public ImageView pic;

		public TextView tagTextView;
	}

}
