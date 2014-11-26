package com.wyy.myhealth.ui.message;

import java.util.List;

import com.wyy.myhealth.R;
import com.wyy.myhealth.bean.MsgBean;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.imag.utils.LoadImageUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MsgAdapter extends BaseAdapter {

	private List<MsgBean> list;

	private Context context;

	private LayoutInflater inflater;

	public MsgAdapter(List<MsgBean> list, Context context) {
		super();
		this.list = list;
		this.context = context;
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
			convertView = inflater.inflate(R.layout.msg_item, parent, false);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.contentImg = (ImageView) convertView
					.findViewById(R.id.content_img);
			holder.mgsImg = (ImageView) convertView.findViewById(R.id.like_tag);
			holder.msgType = (TextView) convertView.findViewById(R.id.msg_type);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.username = (TextView) convertView
					.findViewById(R.id.username);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		MsgBean msgBean = list.get(position);
		if (msgBean != null) {
			holder.content.setText(msgBean.getContent());
			if (msgBean.getType() == ConstantS.MESSAGE_TYPE_COMMENT) {
				holder.mgsImg.setVisibility(View.GONE);
				holder.content.setVisibility(View.VISIBLE);
				holder.msgType.setText(R.string.msg_type_comment);
			} else if (msgBean.getType() == ConstantS.MESSAGE_TYPE_LAUD) {
				holder.mgsImg.setVisibility(View.VISIBLE);
				holder.content.setVisibility(View.GONE);
				holder.msgType.setText(R.string.msg_type_like);
			}

			holder.content.setText(msgBean.getContent());
			holder.time.setText(msgBean.getCn_time());
			if (LoadImageUtils.isImage(msgBean.getObjimg())) {
				LoadImageUtils.loadImage4ImageV_Small(holder.contentImg,
						HealthHttpClient.IMAGE_URL + msgBean.getObjimg());
			} else {
				holder.contentImg.setVisibility(View.INVISIBLE);
			}
			holder.username.setText(msgBean.getSendusername());
		}

		return convertView;
	}

	private class ViewHolder {
		public TextView username;

		public TextView content;

		public TextView msgType;

		public TextView time;

		public ImageView mgsImg;

		public ImageView contentImg;
	}

}
