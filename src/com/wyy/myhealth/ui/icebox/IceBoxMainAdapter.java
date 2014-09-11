package com.wyy.myhealth.ui.icebox;

import java.util.List;

import com.wyy.myhealth.R;
import com.wyy.myhealth.bean.IceBoxFoodBean;
import com.wyy.myhealth.ui.customview.BingGridView;
import com.wyy.myhealth.ui.icebox.IceBoxChildAdapter.DelPicClickListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class IceBoxMainAdapter extends BaseAdapter {

	private List<List<IceBoxFoodBean>> list;

	private LayoutInflater inflater;

	private Context context;

	private DelPicClickListener picClickListener;
	
	private GridCilckListener gridCilckListener;
	
	

	public void setGridCilckListener(GridCilckListener gridCilckListener) {
		this.gridCilckListener = gridCilckListener;
	}

	public void setDelpicClickListener(DelPicClickListener picClickListener) {
		this.picClickListener = picClickListener;
	}

	public IceBoxMainAdapter(List<List<IceBoxFoodBean>> iceBoxFoodBeansList,
			Context context) {
		this.list = iceBoxFoodBeansList;
		this.inflater = LayoutInflater.from(context);
		this.context = context;
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
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.ice_box_fruit_platfrom,
					parent, false);
			holder.bingGridView = (BingGridView) convertView
					.findViewById(R.id.ice_box_grid_v);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.iceBoxChildAdapter = new IceBoxChildAdapter(list.get(position),
				context);
//		setGridView(holder.bingGridView, list.get(position).size());
		holder.bingGridView.setAdapter(holder.iceBoxChildAdapter);
		
		holder.bingGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (gridCilckListener!=null) {
					gridCilckListener.getFoodInfo(adapterPosition, position);
				}
			}
		});
		
		holder.iceBoxChildAdapter.setDelListener(new DelPicClickListener() {

			@Override
			public void delFood(int postion, int type) {
				// TODO Auto-generated method stub
				if (picClickListener != null) {
					picClickListener.delFood(postion, type);
				}

			}
		});

		return convertView;
	}

	public class ViewHolder {
		public BingGridView bingGridView;
		public IceBoxChildAdapter iceBoxChildAdapter;
	}

	
	private void setGridView(GridView gridView,int total){
		switch (total) {
		case 1:
			gridView.setNumColumns(1);
			break;
		case 2:
			gridView.setNumColumns(2);
			break;
		case 3:
			gridView.setNumColumns(3);
			break;

		default:
			break;
		}
	}
	
	
	public interface GridCilckListener{
		public void getFoodInfo(int adapterposition,int position);
	}
	
}
