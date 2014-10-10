package com.wyy.myhealth.ui.healthbar;

import java.util.List;
import java.util.Map;

import com.wyy.myhealth.R;
import com.wyy.myhealth.imag.utils.LoadImageUtils;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {

	private List<Map<String, Object>> list;
	
	private LayoutInflater inflater;
	
	public MessageAdapter (List<Map<String, Object>> list,Context context){
		this.list=list;
		this.inflater=LayoutInflater.from(context);
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
		if (convertView==null) {
			holder=new ViewHolder();
			
			convertView=inflater.inflate(R.layout.new_msg_list, parent, false);
			holder.content=(TextView)convertView.findViewById(R.id.content);
			holder.foodima=(ImageView)convertView.findViewById(R.id.msg_pic);
			holder.timeTextView=(TextView)convertView.findViewById(R.id.time);
			holder.typemsg=(TextView)convertView.findViewById(R.id.msg_type);
			holder.userName=(TextView)convertView.findViewById(R.id.username);
			holder.headima=(ImageView)convertView.findViewById(R.id.head_pic);
			convertView.setTag(holder);
			
		}else {
			holder=(ViewHolder)convertView.getTag();
		}
		
		
		holder.userName.setText("" + list.get(position).get("username"));
		if (list.get(position).containsKey("foodpic")) {
			if (!TextUtils
					.isEmpty(list.get(position).get("foodpic")+"")) {
				LoadImageUtils.loadImage4ImageV(holder.foodima, list.get(position).get("foodpic")+"");
				holder.foodima.setVisibility(View.VISIBLE);
			}else {
				holder.foodima.setVisibility(View.INVISIBLE);
			}
		} else {
			holder.foodima.setVisibility(View.INVISIBLE);
		}

		try {
			LoadImageUtils.loadImage4ImageV(holder.headima, list.get(position).get("userhead")+"");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
		if (list.get(position).containsKey("time")) {
			holder.timeTextView.setText("" + list.get(position).get("time"));
		}

		if (list.get(position).containsKey("commentcon")) {
			holder.content.setText("" + list.get(position).get("commentcon"));
		}
		
		return convertView;
	}

	
	private class ViewHolder {
		public ImageView headima;
		@SuppressWarnings("unused")
		public TextView typemsg;//消息类型
		public TextView userName;// 用户名称
		public ImageView foodima;// 食物图片
		public TextView content;// 评价内容
		public TextView timeTextView;// 时间
	}
	
}
