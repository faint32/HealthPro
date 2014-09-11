package com.wyy.myhealth.ui.message;

import java.util.List;

import com.wyy.myhealth.R;
import com.wyy.myhealth.bean.PublicMsgBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PublicMsgAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<PublicMsgBean> list;
	
	public PublicMsgAdapter(Context context, List<PublicMsgBean> list){
		this.list=list;
		inflater=LayoutInflater.from(context);
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
			convertView=inflater.inflate(R.layout.msg_public_ada_, parent, false);
			holder.time=(TextView)convertView.findViewById(R.id.time);
			holder.content=(TextView)convertView.findViewById(R.id.msg_content);
			convertView.setTag(holder);
			
		} else {
			holder=(ViewHolder)convertView.getTag();
		}
		
		
		holder.content.setText(""+list.get(position).getContent());
		holder.time.setText(""+list.get(position).getTime());
		
		return convertView;
	}

	
	public class ViewHolder{
		public TextView time;
		
		public TextView content;
	}
	
}
