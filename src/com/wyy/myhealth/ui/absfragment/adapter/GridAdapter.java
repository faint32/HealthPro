package com.wyy.myhealth.ui.absfragment.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wyy.myhealth.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridAdapter extends BaseAdapter {

	private List<String> list;
	
	private LayoutInflater inflater;
	
//	private ImageFetcher imageFetcher;
	
	private DisplayImageOptions options;
	
	private ImageLoader imageLoader;
	
	public GridAdapter(Context context,List<String> list){
		this.list=list;
		this.inflater=LayoutInflater.from(context);
//		this.imageFetcher=new ImageFetcher(context, 200,200);
//		imageFetcher.setLoadingImage(R.drawable.camera);
		this.imageLoader=ImageLoader.getInstance();
		this.options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.pic_loading_)
		.showImageForEmptyUri(R.drawable.pic_empty)
		.showImageOnFail(R.drawable.pic_failure)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
//		.displayer(new RoundedBitmapDisplayer(20))
		.build();
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
		ImageView mImageView;
		if (convertView==null) {
			switch (list.size()) {
			case 1:
				convertView=inflater.inflate(R.layout.pic_grid_, null);
				break;
				
			case 2:
				convertView=inflater.inflate(R.layout.pic_grid_t, null);
				break;

			case 3:
				convertView=inflater.inflate(R.layout.pic_gird, null);
				break;
				
			default:
				
				convertView=inflater.inflate(R.layout.pic_grid_f, null);
				
				break;
			}
			
			mImageView=(ImageView)convertView.findViewById(R.id.shai_pic_muilt);
			convertView.setTag(mImageView);
		}else {
			mImageView=(ImageView)convertView.getTag();
		}
		
		imageLoader.displayImage(list.get(position), mImageView,options);
		
		return convertView;
	}

}
