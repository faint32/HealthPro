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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 用户列表适配器
 * 
 * @author lyl
 * @version 1.0
 */
public class UserInfoAdapter extends BaseAdapter {

	private List<PersonalInfo> list;
	private LayoutInflater inflater;
	private Context context;
	private InfoClickListener listener;

	public UserInfoAdapter(List<PersonalInfo> list, Context context) {
		super();
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public void setListener(InfoClickListener listener) {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		final int realPosition = position;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.user_info_list_item,
					parent, false);
			holder.userhead = (ImageView) convertView
					.findViewById(R.id.userhead);
			holder.follow = (TextView) convertView
					.findViewById(R.id.follow_btn);
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
		}

		if (personalInfo.isIsfollow()) {
			holder.follow.setText(R.string.followed);
			holder.follow.setTextColor(context.getResources().getColor(
					R.color.white));
			holder.follow.setBackgroundResource(R.drawable.btn_is_followed);
		} else {
			holder.follow.setText(R.string.follow);
			holder.follow.setTextColor(context.getResources().getColor(
					R.color.gray));
			holder.follow.setBackgroundResource(R.drawable.follow_item_sec);
		}

		holder.follow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (listener != null) {
					listener.follow(realPosition);
				}
			}
		});

		return convertView;
	}

	private class ViewHolder {
		public ImageView userhead;
		public TextView follow;
		public TextView name;
	}

	public interface InfoClickListener {
		public void follow(int position);
	}

}
