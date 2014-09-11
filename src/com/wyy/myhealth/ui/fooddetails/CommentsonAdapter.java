package com.wyy.myhealth.ui.fooddetails;

import java.util.List;

import com.wyy.myhealth.R;
import com.wyy.myhealth.bean.Comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentsonAdapter extends BaseAdapter {

	private List<Comment> list;

	private LayoutInflater inflater;

	public CommentsonAdapter(Context context, List<Comment> list) {
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
			convertView = inflater.inflate(R.layout.coment_item, parent, false);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(list.get(position).getUsername() + ":");
		holder.content.setText("" + list.get(position).getContent());

		return convertView;
	}

	public class ViewHolder {
		public TextView name;

		public TextView content;
	}

}
