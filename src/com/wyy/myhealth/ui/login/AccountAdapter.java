package com.wyy.myhealth.ui.login;

import java.util.List;

import com.wyy.myhealth.R;
import com.wyy.myhealth.bean.UserAccountBean;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountAdapter extends BaseAdapter {

	private List<UserAccountBean> list;
	
	private LayoutInflater inflater;
	
	private OnAccountClickListener listener;
	
	public AccountAdapter(List<UserAccountBean> list, LayoutInflater inflater) {
		super();
		this.list = list;
		this.inflater = inflater;
	}



	public void setListener(OnAccountClickListener listener) {
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
		final int adapterPosition=position;
		ViewHolder holder;
		if (convertView==null) {
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.account_item, parent, false);
			holder.useraccount=(TextView)convertView.findViewById(R.id.user_account);
			holder.delImageView=(ImageView)convertView.findViewById(R.id.del_account);
			convertView.setTag(holder);
		} else {
			holder=(ViewHolder)convertView.getTag();
		}
		
		holder.useraccount.setText(list.get(position).getUsername());
		holder.delImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (listener!=null) {
					listener.ondeleteAccount(adapterPosition);
				}
			}
		});
		
		return convertView;
	}

	
	
	public class ViewHolder{
		public TextView useraccount;
		public ImageView delImageView;
	}
	
	
	public interface OnAccountClickListener{
		public void ondeleteAccount(int position);
	}
	
}
