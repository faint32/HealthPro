package com.wyy.myhealth.ui.personcenter;

import java.util.List;

import com.wyy.myhealth.R;
import com.wyy.myhealth.bean.PersonalInfo;
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

public class NearUserAdapter extends BaseAdapter {

	private List<PersonalInfo> list;
	private LayoutInflater inflater;
	private Context context;

	public NearUserAdapter(List<PersonalInfo> list, Context context) {
		super();
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
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
			convertView = inflater.inflate(R.layout.near_user_item, parent,
					false);
			holder.userhead = (ImageView) convertView
					.findViewById(R.id.userhead);
			holder.distance = (TextView) convertView
					.findViewById(R.id.distance);
			holder.name = (TextView) convertView.findViewById(R.id.username);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		PersonalInfo personalInfo = list.get(position);
		if (personalInfo != null) {
			LoadImageUtils.loadImage4ImageV_Small(holder.userhead,
					HealthHttpClient.IMAGE_URL + personalInfo.getHeadimage());
			holder.name.setText(personalInfo.getUsername());
			if (!TextUtils.isEmpty(personalInfo.getDistance())) {
				holder.distance.setText(personalInfo.getDistance());
			}
		}

		return convertView;
	}

	private class ViewHolder {
		public ImageView userhead;
		public TextView distance;
		public TextView name;
	}

}
