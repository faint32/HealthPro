package com.wyy.myhealth.ui.fooddetails;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wyy.myhealth.R;
import com.wyy.myhealth.bean.Comment;
import com.wyy.myhealth.utils.ListUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FoodCommentAdapter extends BaseAdapter {

	private List<Comment> list;

	private LayoutInflater inflater;

	private Context context;

	private DisplayImageOptions options;

	private ImageLoader imageLoader;

	private AdapterListener adapterListener;

	public FoodCommentAdapter(List<Comment> list, Context context) {
		this.list = list;
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.imageLoader = ImageLoader.getInstance();
		this.options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.pic_loading_)
				.showImageForEmptyUri(R.drawable.pic_empty)
				.showImageOnFail(R.drawable.pic_failure).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true).build();
	}

	public void setListener(AdapterListener adapterListener) {
		this.adapterListener = adapterListener;
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
		final int finaPosition = position;
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.comment_lay, parent, false);
			holder.userhead = (ImageView) convertView
					.findViewById(R.id.userhead);
			holder.userName = (TextView) convertView
					.findViewById(R.id.username);
			holder.commenContent = (TextView) convertView
					.findViewById(R.id.commentinfo);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.huifuContent = (ListView) convertView
					.findViewById(R.id.comments);
			holder.comment = (ImageButton) convertView
					.findViewById(R.id.pinglun_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		imageLoader.displayImage(list.get(position).getUserheadimage(),
				holder.userhead, options);

		holder.userName.setText("" + list.get(position).getUsername());
		holder.commenContent.setText("" + list.get(position).getContent());
		holder.time.setText("" + list.get(position).getCreatetime());

		holder.comment.setFocusable(false);
		holder.comment.setFocusableInTouchMode(false);
		holder.comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (adapterListener != null) {
					adapterListener.comment(finaPosition);
				}

			}
		});

		holder.huifuContent.setFocusable(false);
		holder.huifuContent.setFocusableInTouchMode(false);

		List<Comment> comments = list.get(position).getComment();

		holder.commentsonAdapter = new CommentsonAdapter(context, comments);

		holder.huifuContent.setAdapter(holder.commentsonAdapter);

		ListUtils.setListViewHeightBasedOnChildren(holder.huifuContent);

		return convertView;
	}

	public class ViewHolder {
		public ImageView userhead;
		public TextView userName;
		public TextView commenContent;
		public TextView time;
		public ListView huifuContent;
		public ImageButton comment;
		public CommentsonAdapter commentsonAdapter;
	}

	public interface AdapterListener {
		public void comment(int position);
	}

}
