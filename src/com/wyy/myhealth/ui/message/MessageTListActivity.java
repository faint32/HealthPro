package com.wyy.myhealth.ui.message;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.bean.PublicMsgBean;
import com.wyy.myhealth.db.utils.PublicChatDatabaseUtils;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.ui.customview.BingListView;
import com.wyy.myhealth.ui.customview.BingListView.IXListViewListener;

public class MessageTListActivity extends BaseActivity implements ActivityInterface, IXListViewListener, OnRefreshListener{
	
	private BingListView msgListView;
	
	private SwipeRefreshLayout mSwipeRefreshLayout;
	
	private PublicMsgAdapter mPublicMsgAdapter;
	
	private List<PublicMsgBean> msgLists=new ArrayList<>();
	
	private String chatname="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg_list_t);
		initView();
	}
	
	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.wei_team);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		context=this;
		msgListView=(BingListView)findViewById(R.id.msg_listview);
		mSwipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.list_swipe);
		msgListView.setXListViewListener(this);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		initData();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		getLastData();
		mPublicMsgAdapter=new PublicMsgAdapter(context, msgLists);
		msgListView.setAdapter(mPublicMsgAdapter);
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
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}
	
	private void getLastData(){
		PublicChatDatabaseUtils publicChatDatabaseUtils=new PublicChatDatabaseUtils(context);
		@SuppressWarnings("unchecked")
		List<PublicMsgBean> list=(List<PublicMsgBean>) publicChatDatabaseUtils.querybyName(chatname);
		if (null!=list) {
			msgLists=list;
		}
	}
	
	
}
