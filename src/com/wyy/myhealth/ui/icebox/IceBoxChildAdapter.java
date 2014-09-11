package com.wyy.myhealth.ui.icebox;

import java.util.List;

import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.IceBoxFoodBean;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.imag.utils.LoadImageUtils;
import com.wyy.myhealth.ui.absfragment.utils.TimeUtility;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IceBoxChildAdapter extends BaseAdapter {

	private List<IceBoxFoodBean> list;

	private LayoutInflater inflater;

	private Context context;

	private DelPicClickListener picClickListener;

	private static final int colors[] = {
			WyyApplication.getInstance().getResources()
					.getColor(R.color.iceline1),
			WyyApplication.getInstance().getResources()
					.getColor(R.color.iceline2),
			WyyApplication.getInstance().getResources()
					.getColor(R.color.iceline3),
			WyyApplication.getInstance().getResources()
					.getColor(R.color.iceline4),
			WyyApplication.getInstance().getResources()
					.getColor(R.color.iceline5) };

	private static final int[] heats = { R.drawable.heat1, R.drawable.heat2,
			R.drawable.heat3, R.drawable.heat4, R.drawable.heat5 };

	public void setDelListener(DelPicClickListener picClickListener) {
		this.picClickListener = picClickListener;
	}

	public IceBoxChildAdapter(List<IceBoxFoodBean> list, Context context) {
		// TODO Auto-generated constructor stub
		this.list = list;
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
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return list.get(position).getType();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int realPosition = position;
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater
					.inflate(R.layout.ice_box_food, parent, false);
			holder.foodpic = (ImageView) convertView
					.findViewById(R.id.food_case);
			holder.foodname = (TextView) convertView
					.findViewById(R.id.foodname);
			holder.fooddate = (TextView) convertView
					.findViewById(R.id.ice_food_date);
			holder.datenotice = (ImageView) convertView
					.findViewById(R.id.ice_food_date_img);
			holder.foodlable = (ImageView) convertView
					.findViewById(R.id.ice_box_food_lable);
			holder.line = (ImageView) convertView.findViewById(R.id.ImageView1);
			holder.energytxt = (TextView) convertView.findViewById(R.id.enager);
			holder.delete = (ImageView) convertView.findViewById(R.id.delete);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (IceBoxActivity.isIsvisible()) {
			holder.delete.setVisibility(View.VISIBLE);
		} else {
			holder.delete.setVisibility(View.GONE);
		}

		int left = leftday(list.get(position));
		if (left < 0) {
			holder.datenotice.setImageResource(R.drawable.ic_error);
		} else if (left == 0) {
			holder.datenotice.setImageResource(R.drawable.ic_warn);
		} else {
			holder.datenotice.setImageResource(R.drawable.ic_normal);
		}

		holder.foodname.setText("" + list.get(position).getName());
		holder.fooddate.setText(countDay(left));
		try {

			int point = list.get(position).getEnergy();
			Drawable drawable = null;
			switch (point) {
			case 0:
				drawable = context.getResources().getDrawable(heats[0]);
				break;

			case 1:
				drawable = context.getResources().getDrawable(heats[0]);

				break;

			case 2:
				drawable = context.getResources().getDrawable(heats[1]);
				break;

			case 3:
				drawable = context.getResources().getDrawable(heats[2]);
				break;

			case 4:
				drawable = context.getResources().getDrawable(heats[3]);
				break;

			case 5:
				drawable = context.getResources().getDrawable(heats[4]);
				break;

			default:
				break;
			}

			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			holder.energytxt.setCompoundDrawables(null, null, drawable, null);
		} catch (Exception e) {
			// TODO: handle exception
		}
		LoadImageUtils.loadImageCirImageV(holder.foodpic,
				HealthHttpClient.IMAGE_URL + list.get(position).getFoodpic());

		final int type = getItemViewType(position);
		switch (type) {
		case 5:
			holder.foodlable.setImageResource(R.drawable.ic_ice_box_other);
			holder.line.setImageResource(R.drawable.line5);
			holder.foodname.setTextColor(colors[4]);
			holder.energytxt.setTextColor(colors[4]);
			break;

		case 1:

			holder.foodlable.setImageResource(R.drawable.ic_ice_box_vegetables);
			holder.line.setImageResource(R.drawable.line2);
			holder.foodname.setTextColor(colors[1]);
			holder.energytxt.setTextColor(colors[1]);
			break;

		case 2:
			holder.foodlable.setImageResource(R.drawable.ic_ice_box_fruit);
			holder.line.setImageResource(R.drawable.line1);
			holder.foodname.setTextColor(colors[0]);
			holder.energytxt.setTextColor(colors[0]);
			break;

		case 3:
			holder.foodlable.setImageResource(R.drawable.ic_ice_box_meat);
			holder.line.setImageResource(R.drawable.line3);
			holder.foodname.setTextColor(colors[2]);
			holder.energytxt.setTextColor(colors[2]);
			break;

		case 4:
			holder.foodlable.setImageResource(R.drawable.ic_ice_box_stable);
			holder.line.setImageResource(R.drawable.line4);
			holder.foodname.setTextColor(colors[3]);
			holder.energytxt.setTextColor(colors[3]);
			break;

		default:
			break;
		}

		holder.delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (picClickListener != null) {
					picClickListener.delFood(realPosition, type);
				}

			}
		});

		return convertView;
	}

	public class ViewHolder {
		public ImageView foodpic;

		public TextView foodname;

		public TextView fooddate;

		public ImageView datenotice;

		public ImageView foodlable;

		public ImageView line;

		public TextView energytxt;

		public ImageView delete;
	}

	public interface DelPicClickListener {
		public void delFood(int postion, int type);
	}

	private String countDay(int left) {
		if (left < 0) {
			return context.getString(R.string.expired);
		} else {
			return left + context.getString(R.string.day_);
		}

	}

	private int leftday(IceBoxFoodBean iceBoxFoodBean) {
		int left = Integer.valueOf(iceBoxFoodBean.getNumday())
				- TimeUtility.getday2now(iceBoxFoodBean.getCreatetime());
		return left;
	}

}
