package com.wyy.myhealth.ui.fooddetails;

import java.util.List;

import com.wyy.myhealth.R;
import com.wyy.myhealth.bean.Comment;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.imag.utils.LoadImageUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {

	private List<Comment> list;

	private LayoutInflater inflater;

	public CommentAdapter(Context context, List<Comment> list) {
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
			convertView = inflater.inflate(R.layout.details_comment_item,
					parent, false);
			holder.name = (TextView) convertView.findViewById(R.id.user_name);
			holder.content = (TextView) convertView
					.findViewById(R.id.comment_content);
			holder.timeTextView = (TextView) convertView
					.findViewById(R.id.comment_time_txt);
			holder.userHeadView = (ImageView) convertView
					.findViewById(R.id.user_head);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Comment comment = list.get(position);
		if (comment != null) {
			holder.name.setText(comment.getUsername());
			holder.content.setText("" + comment.getContent());
			holder.timeTextView.setText(comment.getCn_time());
			LoadImageUtils.loadImage4ImageV(holder.userHeadView,
					HealthHttpClient.IMAGE_URL + comment.getHeadimage());
		}

		return convertView;
	}

	private class ViewHolder {

		public ImageView userHeadView;

		public TextView name;

		public TextView content;

		public TextView timeTextView;
	}

}
