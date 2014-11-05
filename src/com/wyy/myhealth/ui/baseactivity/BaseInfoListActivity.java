package com.wyy.myhealth.ui.baseactivity;

import com.wyy.myhealth.R;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.ui.customview.BingListView;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

public class BaseInfoListActivity extends BaseActivity implements
		ActivityInterface {

	protected SwipeRefreshLayout swipeRefreshLayout;
	protected BingListView listView;
	protected String first = "0";
	protected String limit = "10";
	protected int positionIn = 0;
	protected String json = "";
	protected boolean isLoading=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_list);
		initView();
		initData();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.list_swipe_);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_blue_dark,
				android.R.color.holo_green_light,
				android.R.color.holo_green_dark);
		listView = (BingListView) findViewById(R.id.info_list);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	protected void onReshList() {

	}

	protected void onLoadMoreList() {

	}

}
