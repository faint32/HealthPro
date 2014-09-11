package com.wyy.myhealth.ui.collect;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.wyy.myhealth.R;
import com.wyy.myhealth.app.PreferencesFoodsInfo;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.ListDataBead;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.db.utils.CollectDatabaseUtils;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.ui.absfragment.ListBaseFragment;
import com.wyy.myhealth.ui.absfragment.adapter.ShaiYiSaiAdapter2.ShaiItemOnclickListener;
import com.wyy.myhealth.ui.absfragment.utils.TimeUtility;
import com.wyy.myhealth.ui.customview.BingListView.IXListViewListener;
import com.wyy.myhealth.ui.fooddetails.FoodDetailsActivity;
import com.wyy.myhealth.ui.photoPager.PhotoPagerActivity;

public class CollectFragment extends ListBaseFragment implements
		OnItemClickListener, IXListViewListener, OnRefreshListener,
		ShaiItemOnclickListener {

	@Override
	protected void initView(View v) {
		// TODO Auto-generated method stub
		super.initView(v);
		mAdapter2.setListener(this);
		mListView.setOnItemClickListener(this);
		mListView.setXListViewListener(this);
		mRefreshLayout.setOnRefreshListener(this);
	}

	@Override
	protected void registerForContextMenu() {
		// TODO Auto-generated method stub
		super.registerForContextMenu();
		registerForContextMenu(mListView);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = new MenuInflater(getActivity());
		inflater.inflate(R.menu.collect, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.delcollect:
			if (thList2.get(info.position).getType() == ConstantS.TYPE_MOOD) {
				HealthHttpClient.doHttpdelMood(thList2.get(info.position)
						.getId(), new DelAsyHander(info.position));

			}

			if (thList2.get(info.position).getType() == ConstantS.TYPE_FOOD) {
				HealthHttpClient.doHttpdelFoods(thList2.get(info.position)
						.getId(), new DelAsyHander(info.position));
			}

			break;

		case R.id.shaiyisai:

			if (thList2.get(info.position).getType() == ConstantS.TYPE_MOOD) {
				shaiMoodsshai(thList2.get(info.position).getId());

			}

			if (thList2.get(info.position).getType() == ConstantS.TYPE_FOOD) {
				shaiFoodsshai(thList2.get(info.position).getId());
			}

			break;

		default:
			break;
		}

		return super.onContextItemSelected(item);
	}

	private void shaiMoodsshai(String moodid) {
		HealthHttpClient.MoodShaiYIShai(moodid, shaiHandler);
	}

	private void shaiFoodsshai(String foodsid) {
		HealthHttpClient.FoodShaiYiShai(foodsid, shaiHandler);
	}

	private AsyncHttpResponseHandler shaiHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			parseResult(content);
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);
			Toast.makeText(getActivity(), R.string.publish_faliure,
					Toast.LENGTH_LONG).show();
		}

	};

	private void parseResult(String result) {
		try {
			JSONObject object = new JSONObject(result);
			String issuccess = object.getString("result");
			if ("1".equals(issuccess)) {
				Toast.makeText(getActivity(), R.string.publish_sucess,
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getActivity(), R.string.publish_faliure,
						Toast.LENGTH_LONG).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(getActivity(), R.string.publish_faliure,
					Toast.LENGTH_LONG).show();
		}
	}

	public class DelAsyHander extends AsyncHttpResponseHandler {
		private int postion;

		public DelAsyHander(int postion) {
			this.postion = postion;
		}

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			thList2.remove(this.postion);
			mAdapter2.notifyDataSetChanged();
			try {
				Toast.makeText(getActivity(), R.string.delsuccess,
						Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);
			Toast.makeText(getActivity(), R.string.delfailure,
					Toast.LENGTH_LONG).show();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onGetLastData() {
		// TODO Auto-generated method stub
		super.onGetLastData();
		CollectDatabaseUtils collectDatabaseUtils = new CollectDatabaseUtils(
				getActivity());
		lastDataBeads = (List<ListDataBead>) collectDatabaseUtils.queryData();
		if (null != lastDataBeads) {
			if (lastDataBeads.size() > 0) {
				json = lastDataBeads.get(0).getJsondata();
				postion = lastDataBeads.get(0).getPostion();
				id = lastDataBeads.get(0).get_id();
				Log.i(TAG, "id:" + id);
			}

			for (int i = 0; i < lastDataBeads.size(); i++) {
				Log.i(TAG, "json[" + i + "]"
						+ lastDataBeads.get(i).getJsondata());
			}

		}
		collectDatabaseUtils.close();

		initCollectList();

	}

	@Override
	protected void saveJsontoDb(String json, int postion) {
		// TODO Auto-generated method stub
		super.saveJsontoDb(json, postion);
		CollectDatabaseUtils collectDatabaseUtils = new CollectDatabaseUtils(
				getActivity());
		long m;
		if (lastDataBeads.size() == 0) {
			m = collectDatabaseUtils.insert(json, postion);
			Log.i(TAG, "²åÈë:" + m);
		} else {
			m = collectDatabaseUtils.update(json, postion, 1);
			if (m == 0) {
				m = collectDatabaseUtils.update(json, postion, id);
			}
			Log.i(TAG, "¸üÐÂ:" + m);
		}

		collectDatabaseUtils.close();
	}

	private void initCollectList() {
		if (TextUtils.isEmpty(json)) {
			reshShayiSai("0", limit);
		} else {
			try {
				reshParseJson(new JSONObject(json));
			} catch (Exception e) {
				// TODO: handle exception
			}
			reshShayiSai("0", limit);
		}
	}

	@Override
	public void onUserPicClick(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCommentClick(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCollectClick(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPicClick(int listPostino, int picPostion) {
		// TODO Auto-generated method stub
		if (thList2.get(listPostino).getImg() != null) {
			List<String> list = (List<String>) thList2.get(listPostino)
					.getImg();
			Intent intent = new Intent();
			intent.putStringArrayListExtra("imgurls", (ArrayList<String>) list);
			intent.putExtra("postion", picPostion);
			intent.setClass(getActivity(), PhotoPagerActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (thList2.get(position).getType() == ConstantS.TYPE_FOOD) {
			PreferencesFoodsInfo.setfoodId(getActivity(), thList2.get(position)
					.getId());
			startActivity(new Intent(getActivity(), FoodDetailsActivity.class));
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (!loadflag) {
			getLoadMore("" + first, limit);
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (!loadflag) {
			reshShayiSai("0", limit);
		}
	}

	@Override
	protected void getLoadMore(String first, String limit) {
		// TODO Auto-generated method stub
		super.getLoadMore(first, limit);
		// HealthHttpClient.userCollects(WyyApplication.getInfo().getId(),
		// first, limit, parseHandler);
		HealthHttpClient.userCollects20(WyyApplication.getInfo().getId(),
				first, limit, reshHandler2);
	}

	@Override
	protected void reshShayiSai(String first, String limit) {
		// TODO Auto-generated method stub
		super.reshShayiSai(first, limit);
		// HealthHttpClient.userCollects(WyyApplication.getInfo().getId(),
		// first, limit, reshHandler);
		HealthHttpClient.userCollects20(WyyApplication.getInfo().getId(),
				first, limit, loadMoreHandler);
	}

	@Override
	protected void arrangeDayMonth() {
		// TODO Auto-generated method stub
		super.arrangeDayMonth();
		int length = thList2.size();
		for (int i = 0; i < length; i++) {
			thList2.get(i).setCn_time(
					TimeUtility.getListTime(thList2.get(i).getCreatetime()));
		}
	}

}
