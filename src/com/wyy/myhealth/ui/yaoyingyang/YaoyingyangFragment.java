package com.wyy.myhealth.ui.yaoyingyang;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wyy.myhealth.MainActivity;
import com.wyy.myhealth.R;
import com.wyy.myhealth.app.PreferencesFoodsInfo;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.NearFoodBean;
import com.wyy.myhealth.config.Config;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.file.GeographyLocation;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.support.collect.CollectUtils;
import com.wyy.myhealth.ui.absfragment.ListBaseFragYP;
import com.wyy.myhealth.ui.absfragment.utils.ListAddUtils;
import com.wyy.myhealth.ui.customview.BingListView.IXListViewListener;
import com.wyy.myhealth.ui.fooddetails.FoodDetailsActivity;
import com.wyy.myhealth.ui.mapfood.MapFoodsActivity;
import com.wyy.myhealth.ui.yaoyingyang.YaoyingyangAdapter.LocationListener;
import com.wyy.myhealth.utils.BingLog;
import com.wyy.myhealth.utils.DistanceUtils;

public class YaoyingyangFragment extends ListBaseFragYP implements
		OnRefreshListener, IXListViewListener, LocationListener,
		OnItemClickListener {

	private static final String TAG = YaoyingyangFragment.class.getSimpleName();

	private List<NearFoodBean> list = new ArrayList<>();

	private List<NearFoodBean> list2 = new ArrayList<>();

	private static String postiontag = "postion";

	private YaoyingyangAdapter yaoyingyangAdapter;

	private boolean searchFlag = false;

	private String key = "";

	public YaoyingyangFragment() {

	}

	public static YaoyingyangFragment newInstance(int postion) {
		YaoyingyangFragment yaoyingyangFragment = new YaoyingyangFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(postiontag, postion);
		yaoyingyangFragment.setArguments(bundle);
		return yaoyingyangFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		BingLog.i(TAG, "=====onCreate======");
		initFilter();
		new Thread(WaitLocation).start();
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	protected void onInitAdapter() {
		// TODO Auto-generated method stub
		super.onInitAdapter();

		yaoyingyangAdapter = new YaoyingyangAdapter(list, getActivity());
		bingListView.setAdapter(yaoyingyangAdapter);
		mRefreshLayout.setOnRefreshListener(this);
		bingListView.setXListViewListener(this);
		yaoyingyangAdapter.setClickListener(this);
		getNerabyFoods();
		bingListView.setOnItemClickListener(this);
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		if (!TextUtils.isEmpty(lastJson)) {
			try {
				saveLie_Current_Result(lastJson);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		getActivity().unregisterReceiver(searchReceiver);

	}

	private void getNerabyFoods() {
		lastJson = getLast_Result();
		BingLog.i(TAG, "上次数据:" + lastJson);
		if (!TextUtils.isEmpty(lastJson)) {
			parseFoodsReshList(lastJson);
		}
		// HealthHttpClient.doHttpGetFoodsList("" + MainActivity.Wlatitude, ""
		// + MainActivity.Wlongitude, "" + currtuindex, limit,
		// responseHandler, WyyApplication.getInfo().getId());
	}

	private void LoadModreNerabyFoods() {
		HealthHttpClient.doHttpGetFoodsList("" + MainActivity.Wlatitude, ""
				+ MainActivity.Wlongitude, "" + currtuindex, limit,
				responseHandler, WyyApplication.getInfo().getId());
	}

	private void ReshNerabyFoods() {
		HealthHttpClient.doHttpGetFoodsList("" + MainActivity.Wlatitude, ""
				+ MainActivity.Wlongitude, "0", limit, reShResponseHandler,
				WyyApplication.getInfo().getId());
	}

	private AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {

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
			BingLog.i(TAG, "错误:" + content);
			Toast.makeText(getActivity(), R.string.neterro, Toast.LENGTH_LONG)
					.show();
		}

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			parseFoodsList(content);
		}

	};

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
				if (getActivity()!=null) {
					Toast.makeText(getActivity(), R.string.nomore,
							Toast.LENGTH_LONG).show();
				}
				
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
	private void parseFoodsList(String content) {
		JSONObject result;
		JSONArray resultlist;
		try {
			if (TextUtils.isEmpty(content)) {
				return;
			}
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
						// nearFoodBean.setFoodpic(HealthHttpClient.MINIIMAGE +
						// jsonObject.get("id"));
						nearFoodBean.setFoodpic(HealthHttpClient.IMAGE_URL
								+ nearFoodBean.getFoodpic());
						list.add(nearFoodBean);
						currtuindex = list.size();
						yaoyingyangAdapter.notifyDataSetChanged();
					} catch (Exception e) {
						// TODO: handle exception
						BingLog.w(TAG, "解析异常");
					}

				}

			} else {
				Toast.makeText(getActivity(), getString(R.string.parseerror),
						Toast.LENGTH_LONG).show();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

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
			if (TextUtils.isEmpty(content)) {
				return;
			}
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
				saveLie_Current_Result(content);

			} else {
				Toast.makeText(getActivity(), getString(R.string.parseerror),
						Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (!isLoaing) {
			if (!searchFlag && TextUtils.isEmpty(key)) {
				ReshNerabyFoods();
			} else {
				searchReshFoodbyKey(key, "0");
			}

		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (!isLoaing) {
			if (!searchFlag && !TextUtils.isEmpty(key)) {
				LoadModreNerabyFoods();
			} else {
				searchLoadMordFoodbyKey(key, "" + currtuindex);
			}

		}

	}

	private boolean waitlc = false;

	private Runnable WaitLocation = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (!waitlc) {

				if (MainActivity.Wlatitude > 0) {
					waitlc = true;

					ReshNerabyFoods();
				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	public static boolean isdingwei = false;

	@Override
	public void onLocationClick(int postion) {
		// TODO Auto-generated method stub
		try {
			isdingwei = true;
			Intent intent = new Intent();
			intent.setClass(getActivity(), MapFoodsActivity.class);
			double lat = Double.valueOf(list.get(postion).getCommercialLat());
			double lon = Double.valueOf(list.get(postion).getCommercialLon());
			String foodsid = list.get(postion).getId();
			PreferencesFoodsInfo.setfoodId(getActivity(), foodsid);
			intent.putExtra("lat", lat);
			intent.putExtra("lon", lon);
			startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 保存此次数据
	 * 
	 * @param result
	 */
	private void saveLie_Current_Result(String result) {
		if (TextUtils.isEmpty(result)) {
			return;
		}
		BingLog.i(TAG, "保存此次数据:" + result);
		try {
			
			SharedPreferences preferences = getActivity()
					.getSharedPreferences(
							TAG,
							Context.MODE_PRIVATE);
			Editor editor = preferences.edit();

			editor.putString(ConstantS.RESULT, result);
			editor.commit();
		} catch (Exception e) {
			// TODO: handle exception
		}
		

	}

	/**
	 * 获取上次缓存数据
	 * 
	 * @return
	 */
	private String getLast_Result() {
		String result = getActivity()
				.getSharedPreferences(
						YaoyingyangFragment.class.getSimpleName(),
						Context.MODE_PRIVATE).getString(ConstantS.RESULT, "");
		return result;
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

	private void searchReshFoodbyKey(String key, String first) {
		HealthHttpClient
				.doHttpGetFoodsList(key, "" + MainActivity.Wlatitude, ""
						+ MainActivity.Wlongitude, first, limit,
						reShResponseHandler);
	}

	private void searchLoadMordFoodbyKey(String key, String first) {
		HealthHttpClient.doHttpGetFoodsList(key, "" + MainActivity.Wlatitude,
				"" + MainActivity.Wlongitude, first, limit, responseHandler);
	}

	private void initFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConstantS.ACTION_SEARCH_FOOD);
		filter.addAction(ConstantS.ACTION_RECOOMEND_TODAY_FOOD);
		getActivity().registerReceiver(searchReceiver, filter);
	}

	private BroadcastReceiver searchReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals(ConstantS.ACTION_SEARCH_FOOD)) {
				key = intent.getStringExtra("key");
				currtuindex = 0;
				if (TextUtils.isEmpty(key)) {
					searchFlag = false;
					ReshNerabyFoods();
				} else {
					searchFlag = true;
					list.clear();
					yaoyingyangAdapter.notifyDataSetChanged();
					searchReshFoodbyKey(key, "" + currtuindex);
				}
			} else if (action.equals(ConstantS.ACTION_RECOOMEND_TODAY_FOOD)) {
				startActivity(new Intent(getActivity(),
						TodayFoodRecActivity.class));
			}

		}
	};

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
