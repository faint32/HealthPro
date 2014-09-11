package com.wyy.myhealth.ui.shaiyishai;

import java.util.List;
import java.util.Map;

import com.wyy.myhealth.R;
import com.wyy.myhealth.imag.utils.PhoneUtlis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class PublishAdapter extends BaseAdapter {

	private List<Map<String, Object>> list;
	private LayoutInflater inflater;
	private PicClickListener picClickListener;
//	private Context context;
	
	public PublishAdapter(Context context,List<Map<String, Object>> list){
		this.list=list;
		inflater=LayoutInflater.from(context);
//		this.context=context;
	}
	
	public void setPicClickListerter(PicClickListener picClickListener){
		this.picClickListener=picClickListener;
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
		final int picPosition=position;
		if (convertView==null) {
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.pic, null);
			holder.pic=(ImageView)convertView.findViewById(R.id.pic_img);
			convertView.setTag(holder);
		} else {
			holder=(ViewHolder)convertView.getTag();
		}
		if (list.get(position).get("url").equals(R.drawable.publish_sec)) {
			holder.pic.setImageResource(R.drawable.publish_sec);
			if (picPosition>3) {
				holder.pic.setVisibility(View.GONE);
			}else {
				holder.pic.setVisibility(View.VISIBLE);
			}
			
		}else {
			holder.pic.setImageBitmap(PhoneUtlis.getNoCutSmallBitmap(list.get(position).get("url").toString()));
		}
		
		
		holder.pic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				picClickListener.onPicClick(picPosition);
			}
		});
		
		
		return convertView;
	}

	
	public class ViewHolder{
		public ImageView pic;
	}
	
	
	public interface PicClickListener {

		public void onPicClick(int position);
		
	}
	
}
