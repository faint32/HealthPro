package com.wyy.myhealth.ui.absfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wyy.myhealth.R;
import com.wyy.myhealth.ui.customview.BingListView;
import com.wyy.myhealth.ui.customview.BingReshGridView;

public class ListBaseFragYP extends Fragment {

	protected BingListView bingListView;

	protected BingReshGridView bingReshGridView;

	protected SwipeRefreshLayout mRefreshLayout;
	
	protected SwipeRefreshLayout nRefreshLayout;

	// µ±«∞Œª÷√
	protected int currtuindex = 0;

	protected String limit = "10";

	protected String lastJson = "";

	protected boolean isLoaing = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_yaoyingyang, null);
		initView(rootView);
		onRegisterForContextMenu();
		onInitAdapter();
		return rootView;
	}

	protected void initView(View v) {
		mRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.list_swipe);
		nRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.grid_swipe);
		bingListView = (BingListView) v.findViewById(R.id.m_listview);
		bingReshGridView = (BingReshGridView) v.findViewById(R.id.yao_grid_v);
		mRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_blue_dark,
				android.R.color.holo_green_light,
				android.R.color.holo_green_dark);
		nRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_blue_dark,
				android.R.color.holo_green_light,
				android.R.color.holo_green_dark);
	}

	protected void onInitAdapter() {

	}

	protected void onRegisterForContextMenu() {

	}
}
