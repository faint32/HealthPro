package com.wyy.myhealth.ui.personcenter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.BingHttpHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.http.utils.JsonUtils;
import com.wyy.myhealth.ui.baseactivity.BaseInfoListActivity;
import com.wyy.myhealth.ui.customview.BingListView.IXListViewListener;
import com.wyy.myhealth.ui.personcenter.UserInfoAdapter.InfoClickListener;
import com.wyy.myhealth.ui.personcenter.utility.UserInfoUtility;

public class FansListActivity extends BaseInfoListActivity implements
		OnItemClickListener, OnRefreshListener, IXListViewListener,
		InfoClickListener {

	private List<PersonalInfo> fansList = new ArrayList<>();
	private UserInfoAdapter adapter;
	private String uid = "";

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.fans);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		super.initView();
		listView.setOnItemClickListener(this);
		swipeRefreshLayout.setOnRefreshListener(this);
		listView.setXListViewListener(this);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		adapter = new UserInfoAdapter(fansList, context);
		listView.setAdapter(adapter);
		adapter.setListener(this);
		uid = getIntent().getStringExtra("id");
		onReshList();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		loopUserInfo(position);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (!isLoading) {
			onReshList();
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (!isLoading) {
			onLoadMoreList();
		}
	}

	@Override
	protected void onReshList() {
		// TODO Auto-generated method stub
		super.onReshList();
		if (TextUtils.isEmpty(uid) || null == WyyApplication.getInfo()) {
			swipeRefreshLayout.setRefreshing(false);
			return;
		}
		HealthHttpClient.followMeList(WyyApplication.getInfo().getId(), uid,
				first, limit, reshHandler);
	}

	@Override
	protected void onLoadMoreList() {
		// TODO Auto-generated method stub
		super.onLoadMoreList();
		if (TextUtils.isEmpty(uid) || null == WyyApplication.getInfo()) {
			return;
		}
		HealthHttpClient.followMeList(WyyApplication.getInfo().getId(), uid,
				positionIn + "", limit, loadMoreHandler);
	}

	private BingHttpHandler reshHandler = new BingHttpHandler() {

		@Override
		protected void onGetSuccess(JSONObject response) {
			// TODO Auto-generated method stub
			parseResh(response);
		}

		@Override
		protected void onGetFinish() {
			// TODO Auto-generated method stub
			swipeRefreshLayout.setRefreshing(false);
			isLoading = false;
		}

		public void onStart() {
			isLoading = true;
			if (swipeRefreshLayout != null) {
				if (!swipeRefreshLayout.isRefreshing()) {
					swipeRefreshLayout.setRefreshing(true);
				}
			}
		};

	};

	private BingHttpHandler loadMoreHandler = new BingHttpHandler() {

		@Override
		protected void onGetSuccess(JSONObject response) {
			// TODO Auto-generated method stub
			parseLoadMore(response);
		}

		@Override
		protected void onGetFinish() {
			// TODO Auto-generated method stub
			swipeRefreshLayout.setRefreshing(false);
			isLoading = false;
		}

		public void onStart() {
			isLoading = true;
			if (swipeRefreshLayout != null) {
				if (!swipeRefreshLayout.isRefreshing()) {
					swipeRefreshLayout.setRefreshing(true);
				}
			}
		};

	};

	private void parseResh(JSONObject response) {
		if (JsonUtils.isSuccess(response)) {
			if (!json.equals(response.toString())) {
				json = response.toString();
				try {
					JSONArray array = response.getJSONArray("users");
					int length = array.length();
					for (int i = 0; i < length; i++) {
						PersonalInfo personalInfo = JsonUtils.getInfo(array
								.getJSONObject(i));
						if (UserInfoUtility.isExitPersonInfo(personalInfo,
								fansList)) {
							UserInfoUtility.reshPersonInfo(personalInfo,
									fansList);
						} else {
							fansList.add(personalInfo);
						}

					}
					adapter.notifyDataSetChanged();
					positionIn = fansList.size();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				Toast.makeText(context, R.string.nonewmsg, Toast.LENGTH_SHORT)
						.show();
			}
		}

	}

	private void parseLoadMore(JSONObject response) {
		if (JsonUtils.isSuccess(response)) {
			try {
				JSONArray array = response.getJSONArray("users");
				int length = array.length();
				for (int i = 0; i < length; i++) {
					PersonalInfo personalInfo = JsonUtils.getInfo(array
							.getJSONObject(i));
					fansList.add(personalInfo);
				}
				adapter.notifyDataSetChanged();
				positionIn = fansList.size();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void loopUserInfo(int position) {
		PersonalInfo personalInfo = fansList.get(position);
		if (personalInfo == null) {
			return;
		}
		String userid = personalInfo.getId();
		Intent intent = new Intent();
		intent.putExtra("id", userid);
		intent.setClass(context, UserInfoActivity.class);
		startActivity(intent);
	}

	@Override
	public void follow(int position) {
		// TODO Auto-generated method stub
		PersonalInfo personalInfo = fansList.get(position);
		if (personalInfo != null && !personalInfo.isIsfollow()
				&& null != WyyApplication.getInfo()) {
			followUser(personalInfo.getId());
			fansList.get(position).setIsfollow(true);
			adapter.notifyDataSetChanged();
		}
	}

	private void followUser(String uid) {
		HealthHttpClient.followUser(WyyApplication.getInfo().getId(), uid,
				new AsyncHttpResponseHandler());
	}

}
