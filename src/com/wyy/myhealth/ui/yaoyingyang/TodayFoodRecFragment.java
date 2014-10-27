package com.wyy.myhealth.ui.yaoyingyang;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wyy.myhealth.MainActivity;
import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.app.PreferencesFoodsInfo;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.NearFoodBean;
import com.wyy.myhealth.config.Config;
import com.wyy.myhealth.file.GeographyLocation;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.support.collect.CollectUtils;
import com.wyy.myhealth.ui.absfragment.ListBaseTodayFrag;
import com.wyy.myhealth.ui.absfragment.utils.ListAddUtils;
import com.wyy.myhealth.ui.fooddetails.FoodDetailsActivity;
import com.wyy.myhealth.ui.mapfood.MapFoodsActivity;
import com.wyy.myhealth.ui.yaoyingyang.YaoyingyangAdapter.LocationListener;
import com.wyy.myhealth.utils.BingLog;
import com.wyy.myhealth.utils.DistanceUtils;
import com.wyy.myhealth.welcome.WelcomeActivity;

public class TodayFoodRecFragment extends ListBaseTodayFrag implements
		OnRefreshListener, OnItemClickListener, LocationListener {

	private static final String TAG = TodayFoodRecFragment.class
			.getSimpleName();

	private List<NearFoodBean> list = new ArrayList<>();

	private List<NearFoodBean> list2 = new ArrayList<>();

	private YaoyingyangAdapter yaoyingyangAdapter;

	@Override
	protected void onInitAdapter() {
		// TODO Auto-generated method stub
		super.onInitAdapter();
		yaoyingyangAdapter = new YaoyingyangAdapter(list, getActivity());
		bingListView.setAdapter(yaoyingyangAdapter);
		mRefreshLayout.setOnRefreshListener(this);
		ReshNerabyFoods();
		// bingListView.setXListViewListener(this);
		yaoyingyangAdapter.setClickListener(this);
		// getNerabyFoods();
		bingListView.setOnItemClickListener(this);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (!isLoaing) {
			ReshNerabyFoods();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UmenAnalyticsUtility.onPageStart(TAG);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		UmenAnalyticsUtility.onPageEnd(TAG);
	}

	private void ReshNerabyFoods() {
		if (null == WyyApplication.getInfo()) {
			WelcomeActivity.getPersonInfo(getActivity());
		}
		if (null != WyyApplication.getInfo()) {
			HealthHttpClient.gettop10dayBefore(
					WyyApplication.getInfo().getId(), reShResponseHandler);
		}

	}

	private AsyncHttpResponseHandler reShResponseHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			super.onFinish();
			isLoaing = false;
			mRefreshLayout.setRefreshing(false);
		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			isLoaing = true;
			mRefreshLayout.setRefreshing(true);
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);
			BingLog.i(TAG, "错误:" + content + "error:" + error.getMessage());
			Toast.makeText(getActivity(), R.string.neterro, Toast.LENGTH_LONG)
					.show();
		}

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			BingLog.i(TAG, "附近数据:" + content);
			if (lastJson.equals(content)) {
				Toast.makeText(getActivity(), R.string.nomore,
						Toast.LENGTH_LONG).show();
			} else {
				parseFoodsReshList(content);
			}

		}

	};

	/**
	 * 解析数据
	 * 
	 * @param content
	 */
	private void parseFoodsReshList(String content) {
		JSONObject result;
		JSONArray resultlist;
		list2.clear();
		try {
			result = new JSONObject(content);
			if (result.get("result").toString().equals("1")) {
				resultlist = result.getJSONObject("foods").getJSONArray(
						"resultlist");
				int length = resultlist.length();
				Gson gson = new Gson();
				for (int i = 0; i < length; i++) {
					JSONObject jsonObject = resultlist.getJSONObject(i);

					double distance = -1;
					GeographyLocation gLocation = new GeographyLocation();
					if (null != jsonObject.getString("commercialLat")) {
						String latstr = jsonObject.get("commercialLat")
								.toString();
						String lonstr = jsonObject.get("commercialLon")
								.toString();
						try {
							double lat = Double.valueOf(latstr);
							double lon = Double.valueOf(lonstr);
							if (MainActivity.Wlatitude > 0) {
								distance = DistanceUtils.getDistance(lon, lat,
										MainActivity.Wlongitude,
										MainActivity.Wlatitude);
							}
							distance = DistanceUtils.changep2(distance / 1000);

							gLocation.setLat(lat);
							gLocation.setLon(lon);
						} catch (Exception e) {
							// TODO: handle exception
							BingLog.i(TAG, "地址解析错误");
						}

					}

					NearFoodBean nearFoodBean = null;
					try {
						nearFoodBean = gson.fromJson(jsonObject.toString(),
								NearFoodBean.class);
						nearFoodBean.setDistance("" + distance);
						nearFoodBean.setFoodpic(HealthHttpClient.IMAGE_URL
								+ nearFoodBean.getFoodpic());
						list2.add(nearFoodBean);

					} catch (Exception e) {
						// TODO: handle exception
						if (Config.DEVELOPER_MODE) {
							BingLog.w(TAG, "解析异常");
						}

					}

				}

				if (0 != list.size()) {
					ListAddUtils.compleANearMearge(list2, list);
				} else {
					list.addAll(list2);
				}

				currtuindex = list.size();
				yaoyingyangAdapter.notifyDataSetChanged();
				lastJson = content;

			} else {
				Toast.makeText(getActivity(), getString(R.string.parseerror),
						Toast.LENGTH_LONG).show();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		String foodsid = list.get(position).getId();
		PreferencesFoodsInfo.setfoodId(getActivity(), foodsid);
		Intent intent = new Intent();
		try {
			intent.putExtra("distance", list.get(position).getDistance());
		} catch (Exception e) {
			// TODO: handle exception
		}

		intent.setClass(getActivity(), FoodDetailsActivity.class);
		startActivity(intent);
	}

	@Override
	public void onLocationClick(int postion) {
		// TODO Auto-generated method stub
		try {
			YaoyingyangFragment.isdingwei = true;
			Intent intent = new Intent();
			intent.setClass(getActivity(), MapFoodsActivity.class);
			double lat = Double.valueOf(list.get(postion).getCommercialLat());
			double lon = Double.valueOf(list.get(postion).getCommercialLon());
			intent.putExtra("lat", lat);
			intent.putExtra("lon", lon);
			startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onCollectClick(int postion, boolean isCollect) {
		// TODO Auto-generated method stub
		try {

			if (isCollect) {
				CollectUtils.delCollectFood(WyyApplication.getInfo().getId(),
						list.get(postion).getId(), getActivity());
				list.get(postion).setIscollect(false);
			} else {
				CollectUtils.collectFood(list.get(postion).getId(),
						getActivity());
				list.get(postion).setIscollect(true);
			}

			yaoyingyangAdapter.notifyDataSetChanged();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void on3Click() {
		// TODO Auto-generated method stub
		startActivity(new Intent(getActivity(), TodayFoodRecActivity.class));
	}

}
