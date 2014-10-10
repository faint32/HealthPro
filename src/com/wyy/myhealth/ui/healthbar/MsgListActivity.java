package com.wyy.myhealth.ui.healthbar;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.app.PreferencesFoodsInfo;
import com.wyy.myhealth.baidu.utlis.Utils;
import com.wyy.myhealth.bean.ListDataBead;
import com.wyy.myhealth.db.utils.MsgDatabaseUtils;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.http.utils.JsonUtils;
import com.wyy.myhealth.ui.absfragment.utils.TimeUtility;
import com.wyy.myhealth.ui.baseactivity.BaseListActivity;
import com.wyy.myhealth.ui.fooddetails.FoodDetailsActivity;
import com.wyy.myhealth.ui.mood.MoodDetailsActivity;
import com.wyy.myhealth.utils.BingLog;

public class MsgListActivity extends BaseListActivity {

	private static final String TAG=MsgListActivity.class.getSimpleName();
	private MessageAdapter messageAdapter;
	private List<Map<String, Object>> list = new ArrayList<>();
	private List<ListDataBead> list2 = new ArrayList<ListDataBead>();
	
	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.msglist);
	}
	

	@Override
	protected void onInitView() {
		// TODO Auto-generated method stub
		
		registerForContextMenu(baseListV);
		
		baseListV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (list.get(position).containsKey("foodid")) {
					String foodsid = list.get(position).get("foodid")
							.toString();
					PreferencesFoodsInfo.setfoodId(context,
							foodsid);
					Intent intent = new Intent();
					intent.setClass(context,
							FoodDetailsActivity.class);
					startActivity(intent);
				} else if (list.get(position).containsKey("moodid")) {
					String moodid = list.get(position).get("moodid").toString();
					Intent intent = new Intent();
					intent.putExtra("moodid", moodid);
					intent.setClass(context, MoodDetailsActivity.class);
					startActivity(intent);
				}

			}
		});
		messageAdapter=new MessageAdapter(list, context);
		baseListV.setAdapter(messageAdapter);
		
		getLastData();
		initList();
		
		
		
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UmenAnalyticsUtility.onResume(context);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		UmenAnalyticsUtility.onPause(context);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.msg, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		switch (item.getItemId()) {
		case R.id.delete:
			messageAdapter.notifyDataSetChanged();
			delete4db(Long.valueOf(list.get(info.position).get("_id").toString()));
			list.remove(info.position);
			break;

		case R.id.deleteall:
			list.clear();
			messageAdapter.notifyDataSetChanged();
			delAll4db();
			break;

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	@SuppressWarnings("unchecked")
	private void getLastData() {
		MsgDatabaseUtils msgDatabaseUtils = new MsgDatabaseUtils(
				getApplicationContext());
		list2 = (List<ListDataBead>) msgDatabaseUtils.queryData();
		BingLog.i(TAG, "数据:"+list2);
		msgDatabaseUtils.close();
		if (list2 == null) {
			return;
		}
		int length = list2.size();
		for (int i = 0; i < length; i++) {
			initLastData(list2.get(i));
		}

		messageAdapter.notifyDataSetChanged();

	}
	
	
	
	private void initList() {
		for (int i = 0; i < Utils.mstlList.size(); i++) {
			String json = Utils.mstlList.get(i);
			HashMap<String, Object> map = new HashMap<String, Object>();
			try {
				JSONObject jsonObject = new JSONObject(json);
				JSONObject content = jsonObject.getJSONObject("content");
				JSONObject food = content.getJSONObject("foods");
				String foodpic = HealthHttpClient.IMAGE_URL
						+ food.getString("foodpic");
				String foodid = food.getString("id");
				JSONObject user = content.getJSONObject("user");
				String username = user.getString("username");
				String userHead = HealthHttpClient.IMAGE_URL
						+ user.getString("headimage");

				map.put("username", username);
				map.put("userhead", userHead);
				map.put("foodpic", foodpic);
				map.put("foodid", foodid);
				map.put("time", "刚刚");


				// messageAdapter = new MessageAdapter(NewsMessageActivity.this,
				// list);
				// msgListView.setAdapter(messageAdapter);

				long _id = saveJSON(Utils.mstlList.get(i));

				map.put("_id", _id);

				if (i == Utils.mstlList.size() - 1) {
					Utils.mstlList.clear();
					Utils.add_Foods_Comm = "";
				}

				// list.add(map);
				list.add(0, map);
				messageAdapter.notifyDataSetChanged();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				initMoodsList();
			}

		}

	}
	
	private void initLastData(ListDataBead listDataBead) {
		String json = listDataBead.getJsondata();
		BingLog.i(TAG, "数据:"+json);
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONObject content = jsonObject.getJSONObject("content");
			JSONObject food = content.getJSONObject("foods");
			String foodpic = HealthHttpClient.IMAGE_URL
					+ food.getString("foodpic");
			String foodid = food.getString("id");
			JSONObject user = content.getJSONObject("user");
			String username = user.getString("username");
			String userHead = HealthHttpClient.IMAGE_URL
					+ user.getString("headimage");

			map.put("username", username);
			map.put("userhead", userHead);
			map.put("foodpic", foodpic);
			map.put("foodid", foodid);
			map.put("_id", listDataBead.get_id());
			map.put("time", getRelateTime(listDataBead.getTime()));


			list.add(map);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			initMoodsList(listDataBead);
		}

	}
	
	private void initMoodsList() {
		int length = Utils.mstlList.size();
		BingLog.d("得到", "心情消息:" + Utils.mstlList.get(0));
		for (int i = 0; i < length; i++) {
			String json = Utils.mstlList.get(i);
			HashMap<String, Object> map = new HashMap<String, Object>();
			try {
				JSONObject jsonObject = new JSONObject(json);
				JSONObject content = jsonObject.getJSONObject("content");
				JSONObject mood = content.getJSONObject("mood");
				String moodid = mood.getString("id");
				JSONObject comment = content.getJSONObject("comment");
				String comcontent = comment.getString("content");
				JSONObject user = content.getJSONObject("user");
				String username = user.getString("username");
				String userHead = HealthHttpClient.IMAGE_URL
						+ JsonUtils.getKey(user, "headimage");

				map.put("username", username);
				map.put("userhead", userHead);
				map.put("commentcon", comcontent);
				map.put("moodid", moodid);
				map.put("time", "刚刚");


				long _id = saveJSON(Utils.mstlList.get(i));

				map.put("_id", _id);

				if (i == Utils.mstlList.size() - 1) {
					Utils.mstlList.clear();
					Utils.add_Foods_Comm = "";
				}
				// list.add(map);
				list.add(0, map);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			messageAdapter.notifyDataSetChanged();

		}
	}
	
	private void initMoodsList(ListDataBead listDataBead) {
		String json = listDataBead.getJsondata();
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONObject content = jsonObject.getJSONObject("content");
			JSONObject mood = content.getJSONObject("mood");
			String moodid = mood.getString("id");
			JSONObject comment = content.getJSONObject("comment");
			String comcontent = comment.getString("content");
			JSONObject user = content.getJSONObject("user");
			String username = user.getString("username");
			String userHead = HealthHttpClient.IMAGE_URL
					+ JsonUtils.getKey(user, "headimage");

			map.put("username", username);
			map.put("userhead", userHead);
			map.put("commentcon", comcontent);
			map.put("moodid", moodid);
			map.put("_id", listDataBead.get_id());
			map.put("time", getRelateTime(listDataBead.getTime()));

			list.add(map);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	private String getRelateTime(String time) {
		try {
			long t = Long.valueOf(time);
			return "" + TimeUtility.getListTime(t);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return "";

	}
	
	
	private void delete4db(long id) {
		MsgDatabaseUtils msgDatabaseUtils = new MsgDatabaseUtils(
				context);
		msgDatabaseUtils.delete(id);
		msgDatabaseUtils.close();
	}

	private void delAll4db() {
		MsgDatabaseUtils msgDatabaseUtils = new MsgDatabaseUtils(
				context);
		msgDatabaseUtils.deleteAll();
		msgDatabaseUtils.close();
	}
	
	private long saveJSON(String json) {
		MsgDatabaseUtils msgDatabaseUtils = new MsgDatabaseUtils(
				context);
		long _id = msgDatabaseUtils.insert(json,
				"" + System.currentTimeMillis());
		msgDatabaseUtils.close();
		return _id;
	}
	
}
