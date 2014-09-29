package com.wyy.myhealth.ui.absfragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.wyy.myhealth.R;
import com.wyy.myhealth.bean.ListDataBead;
import com.wyy.myhealth.bean.MoodaFoodBean;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.bean.ShaiFoods;
import com.wyy.myhealth.bean.ShaiMoods;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.JsonHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.http.utils.JsonUtils;
import com.wyy.myhealth.ui.absfragment.adapter.HealthAdapter2;
import com.wyy.myhealth.ui.absfragment.adapter.ShaiYiSaiAdapter;
import com.wyy.myhealth.ui.absfragment.adapter.ShaiYiSaiAdapter2;
import com.wyy.myhealth.ui.absfragment.utils.ListAddUtils;
import com.wyy.myhealth.ui.absfragment.utils.SortUtils;
import com.wyy.myhealth.ui.absfragment.utils.TimeUtils;
import com.wyy.myhealth.ui.customview.BingListView;
import com.wyy.myhealth.utils.BingLog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ListBaseFragment extends Fragment {

	protected static final String TAG = ListBaseFragment.class.getSimpleName();
	protected ArrayList<ShaiFoods> userfoodsList = new ArrayList<ShaiFoods>();
	protected ArrayList<ShaiMoods> usermoodsList = new ArrayList<ShaiMoods>();
	protected BingListView mListView;
	protected SwipeRefreshLayout mRefreshLayout;
	protected ShaiYiSaiAdapter mAdapter;
	protected ShaiYiSaiAdapter2 mAdapter2;
	protected RelativeLayout titleLayout;
	// 个人信息
	protected PersonalInfo info;
	// 晒一晒数据表
	protected List<Map<String, Object>> thList = new ArrayList<Map<String, Object>>();

	// 晒一晒新数据
	protected List<Map<String, Object>> tempshaiList = new ArrayList<Map<String, Object>>();

	protected List<MoodaFoodBean> thList2 = new ArrayList<>();

	protected List<MoodaFoodBean> tempLists = new ArrayList<>();

	protected int first = 0;

	protected String limit = "10";

	protected String json = "";

	// listView位置
	protected int postion = 0;

	protected List<ListDataBead> lastDataBeads = new ArrayList<ListDataBead>();

	protected boolean loadflag = false;
	// 数据库位置
	protected int id = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.base_frag_lay,container, false);
		initView(rootView);
		registerForContextMenu();
		onGetLastData();
		return rootView;
	}

	protected void initView(View v) {
		mRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.list_swipe);
		mListView = (BingListView) v.findViewById(R.id.m_listview);
		mListView.setCacheColorHint(Color.TRANSPARENT);
		mRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_blue_dark,
				android.R.color.holo_green_light,
				android.R.color.holo_green_dark);
		mAdapter2 = new ShaiYiSaiAdapter2(thList2, getActivity());
		mListView.setAdapter(mAdapter2);
	}

	protected void onGetLastData() {

	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		saveJsontoDb(json, postion);
	}

	/**
	 * @deprecated
	 */
	protected AsyncHttpResponseHandler parseHandler = new AsyncHttpResponseHandler() {

		public void onStart() {
			super.onStart();
			loadflag = true;
			mRefreshLayout.setRefreshing(true);
		};

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			Log.i(TAG, content);
			if (first == 0) {
				json = content;
			}
			pareseJson(content);

		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			super.onFinish();
			loadflag = false;
			mRefreshLayout.setRefreshing(false);
		}

	};
	/**
	 * @deprecated
	 */
	protected AsyncHttpResponseHandler reshHandler = new AsyncHttpResponseHandler() {

		public void onStart() {
			super.onStart();
			loadflag = true;
			mRefreshLayout.setRefreshing(true);
		};

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			Log.i(TAG, "" + content);

			if (content.equals(json) && !TextUtils.isEmpty(json)) {
				if (null != getActivity()) {

					Toast.makeText(getActivity(), R.string.nonewmsg,
							Toast.LENGTH_SHORT).show();
				}
			} else {
				reshpareseJson(content);
				json = content;
			}

		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			super.onFinish();
			loadflag = false;
			mRefreshLayout.setRefreshing(false);
		}

	};

	protected JsonHttpResponseHandler loadMoreHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(response);
			BingLog.i(TAG, "" + response);
			if (first == 0) {
				json = response.toString();
			}
			loadmoreJson(response);
		}

		@Override
		public void onFailure(Throwable e, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			super.onFailure(e, errorResponse);
		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			loadflag = true;
			mRefreshLayout.setRefreshing(true);
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			super.onFinish();
			loadflag = false;
			mRefreshLayout.setRefreshing(false);
		}

	};

	protected JsonHttpResponseHandler reshHandler2 = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(response);
			if (response.toString().equals(json) && !TextUtils.isEmpty(json)) {
				if (null != getActivity()) {
					Toast.makeText(getActivity(), R.string.nonewmsg,
							Toast.LENGTH_SHORT).show();
				}
			} else {
				reshParseJson(response);
				json = response.toString();
			}

		}

		@Override
		public void onFailure(Throwable e, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			super.onFailure(e, errorResponse);
		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			loadflag = true;
			mRefreshLayout.setRefreshing(true);
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			super.onFinish();
			loadflag = false;
			mRefreshLayout.setRefreshing(false);
		}

	};

	protected void reshParseJson(JSONObject response) {
		BingLog.i(TAG, "最新" + response);
		if (JsonUtils.isSuccess(response)) {
			try {
				tempLists.clear();
				JSONArray array = response.getJSONArray("foods");
				int length = array.length();
				for (int i = 0; i < length; i++) {
					MoodaFoodBean moodaFoodBean = JsonUtils
							.getMoodaFoodBean(array.getJSONObject(i));

					tempLists.add(moodaFoodBean);
				}

				if (0 != thList2.size()) {
					ListAddUtils.compleAMearge2(tempLists, thList2);
				} else {
					thList2.addAll(tempLists);
				}
				arrangeDayMonth();
				first = thList2.size();
				addGg();
				try {
					mAdapter2.notifyDataSetChanged();
				} catch (Exception e) {
					// TODO: handle exception
					BingLog.e(TAG, "错误:"+e.getMessage());
				}
				

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void loadmoreJson(JSONObject response) {
		BingLog.i(TAG, "最新" + response);
		if (JsonUtils.isSuccess(response)) {
			try {
				JSONArray array = response.getJSONArray("foods");
				int length = array.length();
				if (length == 0) {
					Toast.makeText(getActivity(), R.string.nomore,
							Toast.LENGTH_SHORT).show();
				}else {
					for (int i = 0; i < length; i++) {
						MoodaFoodBean moodaFoodBean = JsonUtils
								.getMoodaFoodBean(array.getJSONObject(i));
						thList2.add(moodaFoodBean);
					}

					arrangeDayMonth();
					first = thList2.size();
					addGg();
					mAdapter2.notifyDataSetChanged();
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @deprecated
	 * @param json
	 */
	protected void pareseJson(String json) {
		userfoodsList.clear();
		usermoodsList.clear();
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray foodsArray = jsonObject.getJSONArray("foods");
			int foodlength = foodsArray.length();
			for (int i = 0; i < foodlength; i++) {
				JSONObject object = foodsArray.getJSONObject(i);
				ShaiFoods foods = new ShaiFoods();
				foods.setId(object.getString("id"));
				foods.setUserid(getKey(object, "userid")/*
														 * object.getString("userid"
														 * )
														 */);
				foods.setFoodpic(object.getString("foodpic"));
				foods.setTastelevel(object.getString("tastelevel"));
				foods.setCommentcount(object.getString("commentcount"));
				foods.setCollectcount(getKey(object, "collectcount"));/*
																	 * object.
																	 * getString
																	 * (
																	 * "collectcount"
																	 * )
																	 */
				foods.setLaudcount(object.getString("laudcount"));
				foods.setTime(object.getString("createtime"));
				foods.setSummary(object.getString("summary"));
				foods.setTags(object.getString("tags"));
				JSONObject userObject = object.getJSONObject("user");
				foods.setHeadimage(getKey(userObject, "headimage"));
				foods.setUsername(getKey(userObject, "username"));

				int commentlength = object.getJSONArray("comment").length();
				String comString = "";
				for (int j = 0; j < commentlength; j++) {
					JSONObject comment = object.getJSONArray("comment")
							.getJSONObject(j);
					foods.setComment_content(getKey(comment, "content"));
					JSONObject comment_user = comment.getJSONObject("user");
					foods.setComment_user_name(getKey(comment_user, "username"));
					if (commentlength > 1) {
						if (j == commentlength - 1) {
							comString = comString
									+ getKey(comment_user, "username") + ":"
									+ getKey(comment, "content");
						} else {
							comString = comString
									+ getKey(comment_user, "username") + ":"
									+ getKey(comment, "content") + "\n";
						}

					} else {
						comString = comString
								+ getKey(comment_user, "username") + ":"
								+ getKey(comment, "content");
					}

					foods.setComment_content(comString);
				}

				if (commentlength == 0) {
					foods.setComment_content("");
				}

				userfoodsList.add(foods);
			}

			JSONArray moodsArray = jsonObject.getJSONArray("moods");
			int moodlength = moodsArray.length();
			for (int i = 0; i < moodlength; i++) {
				ShaiMoods moods = new ShaiMoods();
				JSONObject object = moodsArray.getJSONObject(i);
				moods.setCollectcount(object.getString("collectcount"));
				moods.setCommentcount(object.getString("commentcount"));
				moods.setContext(object.getString("context"));
				moods.setTime(object.getString("createtime"));
				moods.setCollectcount(object.getString("collectcount"));
				moods.setLaudcount(object.getString("laudcount"));
				moods.setMoodindex(object.getString("moodindex"));
				moods.setUserid(object.getString("userid"));
				JSONObject userObject = object.getJSONObject("user");
				moods.setHeadimage(getKey(userObject, "headimage"));
				moods.setUsername(getKey(userObject, "username"));
				moods.setId(getKey(object, "id"));
				JSONArray imgs = object.getJSONArray("img");
				// String [] imgs=(String [])object.get("img");
				List<String> list = new ArrayList<String>();
				for (int j = 0; j < imgs.length(); j++) {
					list.add(HealthHttpClient.IMAGE_URL + imgs.get(j));
				}
				moods.setImgs(list);

				int commentlength = object.getJSONArray("comment").length();
				String comString = "";
				for (int j = 0; j < commentlength; j++) {
					JSONObject comment = object.getJSONArray("comment")
							.getJSONObject(j);
					moods.setComment_content(getKey(comment, "content"));
					JSONObject comment_user = comment.getJSONObject("user");
					moods.setComment_user_name(getKey(comment_user, "username"));
					if (commentlength > 1) {
						if (j == commentlength - 1) {
							comString = comString
									+ getKey(comment_user, "username") + ":"
									+ getKey(comment, "content");
						} else {
							comString = comString
									+ getKey(comment_user, "username") + ":"
									+ getKey(comment, "content") + "\n";
						}

					} else {
						comString = comString
								+ getKey(comment_user, "username") + ":"
								+ getKey(comment, "content");
					}

					moods.setComment_content(comString);
				}

				if (commentlength == 0) {
					moods.setComment_content("");
				}

				usermoodsList.add(moods);

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(TAG, "foods长度:" + userfoodsList.size());
		Log.i(TAG, "moods长度:" + usermoodsList.size());
		Log.i(TAG, "长度:" + thList.size());

		arrangeData();
		Log.i(TAG, "长度:" + thList.size());
		SortUtils.bingSort(thList);

		TimeUtils.getCnTime(thList);

		addGg();
		Log.i(TAG, "适配:" + mAdapter);
		mAdapter.notifyDataSetChanged();
		first = thList.size();

		if (userfoodsList.size() + usermoodsList.size() == 0) {
			if (null != getActivity()) {
				Toast.makeText(getActivity(), R.string.nomore,
						Toast.LENGTH_LONG).show();
			}
		}

	}

	/**
	 * 刷新数据
	 * @deprecated
	 * @param json
	 */
	private void reshpareseJson(String json) {
		tempshaiList.clear();
		userfoodsList.clear();
		usermoodsList.clear();
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray foodsArray = null;
			int foodlength = 0;
			if (isHasiJsonArray(jsonObject, "foods")) {
				foodsArray = jsonObject.getJSONArray("foods");
				foodlength = foodsArray.length();
			}

			for (int i = 0; i < foodlength; i++) {
				JSONObject object = foodsArray.getJSONObject(i);
				ShaiFoods foods = new ShaiFoods();
				foods.setId(object.getString("id"));
				foods.setUserid(getKey(object, "userid")/*
														 * object.getString("userid"
														 * )
														 */);
				foods.setFoodpic(object.getString("foodpic"));
				foods.setTastelevel(object.getString("tastelevel"));
				foods.setCommentcount(object.getString("commentcount"));
				foods.setCollectcount(object.getString("collectcount"));
				foods.setLaudcount(object.getString("laudcount"));
				foods.setTime(object.getString("createtime"));
				foods.setSummary(object.getString("summary"));
				foods.setTags(object.getString("tags"));
				JSONObject userObject = object.getJSONObject("user");
				foods.setHeadimage(getKey(userObject, "headimage"));
				foods.setUsername(getKey(userObject, "username"));
				int commentlength = object.getJSONArray("comment").length();
				String comString = "";
				for (int j = 0; j < commentlength; j++) {
					JSONObject comment = object.getJSONArray("comment")
							.getJSONObject(j);
					foods.setComment_content(getKey(comment, "content"));
					JSONObject comment_user = comment.getJSONObject("user");
					foods.setComment_user_name(getKey(comment_user, "username"));
					if (commentlength > 1) {
						if (j == commentlength - 1) {
							comString = comString
									+ getKey(comment_user, "username") + ":"
									+ getKey(comment, "content");
						} else {
							comString = comString
									+ getKey(comment_user, "username") + ":"
									+ getKey(comment, "content") + "\n";
						}

					} else {
						comString = comString
								+ getKey(comment_user, "username") + ":"
								+ getKey(comment, "content");
					}

					foods.setComment_content(comString);
				}

				if (commentlength == 0) {
					foods.setComment_content("");
				}

				userfoodsList.add(foods);
			}

			JSONArray moodsArray = null;
			int moodlength = 0;

			if (isHasiJsonArray(jsonObject, "moods")) {
				moodsArray = jsonObject.getJSONArray("moods");
				moodlength = moodsArray.length();
			} else if (isHasiJsonArray(jsonObject, "mood")) {
				moodsArray = jsonObject.getJSONArray("mood");
				moodlength = moodsArray.length();
			}

			for (int i = 0; i < moodlength; i++) {
				ShaiMoods moods = new ShaiMoods();
				JSONObject object = moodsArray.getJSONObject(i);
				moods.setCollectcount(object.getString("collectcount"));
				moods.setCommentcount(object.getString("commentcount"));
				moods.setContext(object.getString("context"));
				moods.setTime(object.getString("createtime"));
				moods.setCollectcount(object.getString("collectcount"));
				moods.setLaudcount(object.getString("laudcount"));
				moods.setMoodindex(object.getString("moodindex"));
				moods.setUserid(object.getString("userid"));
				JSONObject userObject = object.getJSONObject("user");
				moods.setHeadimage(JsonUtils.getKey(userObject, "headimage"));
				moods.setUsername(getKey(userObject, "username"));
				moods.setId(getKey(object, "id"));
				JSONArray imgs = object.getJSONArray("img");
				// String [] imgs=(String [])object.get("img");
				List<String> list = new ArrayList<String>();
				for (int j = 0; j < imgs.length(); j++) {
					list.add(HealthHttpClient.IMAGE_URL + imgs.get(j));
				}
				moods.setImgs(list);

				// JSONArray comment = object.getJSONArray("comment");

				int commentlength = object.getJSONArray("comment").length();
				String comString = "";
				for (int j = 0; j < commentlength; j++) {
					JSONObject comment = object.getJSONArray("comment")
							.getJSONObject(j);
					moods.setComment_content(getKey(comment, "content"));
					JSONObject comment_user = comment.getJSONObject("user");
					moods.setComment_user_name(getKey(comment_user, "username"));
					if (commentlength > 1) {
						if (j == commentlength - 1) {
							comString = comString
									+ getKey(comment_user, "username") + ":"
									+ getKey(comment, "content");
						} else {
							comString = comString
									+ getKey(comment_user, "username") + ":"
									+ getKey(comment, "content") + "\n";
						}

					} else {
						comString = comString
								+ getKey(comment_user, "username") + ":"
								+ getKey(comment, "content");
					}

					moods.setComment_content(comString);
				}

				if (commentlength == 0) {
					moods.setComment_content("");
				}

				usermoodsList.add(moods);

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(TAG, "foods长度:" + userfoodsList.size());
		Log.i(TAG, "moods长度:" + usermoodsList.size());
		Log.i(TAG, "长度:" + tempshaiList.size());
		arrangenewData();
		Log.i(TAG, "长度:" + tempshaiList.size());
		SortUtils.bingSort(tempshaiList);
		TimeUtils.getCnTime(tempshaiList);
		if (0 != thList.size()) {
			ListAddUtils.compleAMearge(tempshaiList, thList);
		} else {
			thList.addAll(tempshaiList);
		}

		SortUtils.bingSort(thList);
		TimeUtils.getCnTime(thList);
		addGg();
		mAdapter.notifyDataSetChanged();
		first = thList.size();
	}

	/**
	 * 数据处理
	 */
	private void arrangeData() {
		int foodlength = userfoodsList.size();
		for (int i = 0; i < foodlength; i++) {
			ShaiFoods foods = userfoodsList.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			List<String> imgurl = new ArrayList<String>();
			imgurl.add(HealthHttpClient.IMAGE_URL + foods.getFoodpic());
			map.put("grid_pic", imgurl);
			map.put("style", "分享健康美味");
			map.put("headimage",
					HealthHttpClient.IMAGE_URL + foods.getHeadimage());
			map.put("time", foods.getTime());

			map.put("contentpic",
					HealthHttpClient.IMAGE_URL + foods.getFoodpic());
			map.put("userid", foods.getUserid());
			map.put("tastelevel", foods.getTastelevel());
			map.put("commentcount", foods.getCommentcount());
			map.put("collectcount", foods.getCollectcount());
			map.put("summary", foods.getSummary());
			map.put("laudcount", foods.getLaudcount());
			map.put("comment", foods.getComment_content());
			map.put("username", foods.getUsername());
			map.put("com_username", foods.getComment_user_name());
			map.put("tags", foods.getTags());
			map.put("foodsid", foods.getId());
			thList.add(map);
		}

		int moodlegth = usermoodsList.size();
		for (int i = 0; i < moodlegth; i++) {
			ShaiMoods moods = usermoodsList.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("style", "分享心情");
			map.put("headimage",
					HealthHttpClient.IMAGE_URL + moods.getHeadimage());
			if (!TextUtils.isEmpty(moods.getMoodpic())) {
				map.put("contentpic",
						HealthHttpClient.IMAGE_URL + moods.getMoodpic());
			}

			map.put("userid", moods.getUserid());
			map.put("time", moods.getTime());
			map.put("commentcount", moods.getCommentcount());
			map.put("collectcount", moods.getCollectcount());
			map.put("laudcount", moods.getLaudcount());
			map.put("username", moods.getUsername());
			map.put("moodindex", moods.getMoodindex());
			if ("-1".equals(moods.getMoodindex())) {
				map.put("style", "晒一下");
			}
			map.put("context", moods.getContext());
			map.put("moodsid", moods.getId());
			map.put("comment", moods.getComment_content());
			// map.put("comment", moods.getComment_content());
			List<String> imgurl = moods.getImgs();
			map.put("grid_pic", imgurl);

			thList.add(map);
		}

	}

	private void arrangenewData() {
		tempshaiList.clear();
		int foodlength = userfoodsList.size();
		for (int i = 0; i < foodlength; i++) {
			ShaiFoods foods = userfoodsList.get(i);
			Map<String, Object> map = new HashMap<String, Object>();

			List<String> imgurl = new ArrayList<String>();
			imgurl.add(HealthHttpClient.IMAGE_URL + foods.getFoodpic());
			map.put("grid_pic", imgurl);

			map.put("style", "分享健康美味");
			map.put("headimage",
					HealthHttpClient.IMAGE_URL + foods.getHeadimage());
			map.put("time", foods.getTime());
			map.put("contentpic",
					HealthHttpClient.IMAGE_URL + foods.getFoodpic());
			map.put("userid", foods.getUserid());
			map.put("tastelevel", foods.getTastelevel());
			map.put("commentcount", foods.getCommentcount());
			map.put("collectcount", foods.getCollectcount());
			map.put("summary", foods.getSummary());
			map.put("laudcount", foods.getLaudcount());
			map.put("comment", foods.getComment_content());
			map.put("username", foods.getUsername());
			map.put("foodsid", foods.getId());
			map.put("com_username", foods.getComment_user_name());
			map.put("tags", foods.getTags());
			tempshaiList.add(map);
		}

		int moodlegth = usermoodsList.size();
		for (int i = 0; i < moodlegth; i++) {
			ShaiMoods moods = usermoodsList.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("style", "分享心情");
			map.put("headimage",
					HealthHttpClient.IMAGE_URL + moods.getHeadimage());
			if (!TextUtils.isEmpty(moods.getMoodpic())) {
				map.put("contentpic",
						HealthHttpClient.IMAGE_URL + moods.getMoodpic());
			}
			map.put("userid", moods.getUserid());
			map.put("time", moods.getTime());
			map.put("commentcount", moods.getCommentcount());
			map.put("collectcount", moods.getCollectcount());
			map.put("laudcount", moods.getLaudcount());
			map.put("username", moods.getUsername());
			map.put("moodindex", moods.getMoodindex());
			if ("-1".equals(moods.getMoodindex())) {
				map.put("style", "晒一下");
			}
			map.put("context", moods.getContext());
			map.put("comment", moods.getComment_content());
			map.put("moodsid", moods.getId());
			List<String> imgurl = moods.getImgs();
			map.put("grid_pic", imgurl);
			// map.put("comment", moods.getComment_content());
			tempshaiList.add(map);
		}

	}

	/**
	 * 返回结果
	 * 
	 * @param jsonObject
	 *            json对象
	 * @param key
	 *            键值
	 * @return
	 */
	public static String getKey(JSONObject jsonObject, String key) {
		if (jsonObject.has(key)) {
			try {
				return jsonObject.getString(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
		}

		return "";

	}

	public static boolean isHasiJsonArray(JSONObject jsonObject, String key) {
		boolean has = false;
		try {
			jsonObject.getJSONArray(key);
			has = true;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return has;
	}

	/**
	 * 退出保存数据
	 * 
	 * @param json
	 * @param postion
	 */
	protected void saveJsontoDb(String json, int postion) {
		if (TextUtils.isEmpty(json)) {
			return;
		}

		if (null == thList) {
			return;
		}

		if (thList.size() == 0) {
			return;
		}

	}

	protected void registerForContextMenu() {

	}

	/**
	 * 刷新数据
	 * 
	 * @param first
	 * @param limit
	 */
	protected void reshShayiSai(String first, String limit) {

	}

	/**
	 * 分页加载
	 * 
	 * @param first
	 *            首项
	 * @param limit
	 *            页数
	 */
	protected void getLoadMore(String first, String limit) {

	}

	/**
	 * 数据添加
	 */
	protected void addGg() {

	}
	
	
	protected void arrangeDayMonth() {

	}
	
}
