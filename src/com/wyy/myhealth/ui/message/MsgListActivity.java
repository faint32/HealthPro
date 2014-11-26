package com.wyy.myhealth.ui.message;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.ContextMenu;
import android.view.Menu;
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
import com.wyy.myhealth.bean.MoodaFoodBean;
import com.wyy.myhealth.bean.MsgBean;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.http.BingHttpHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.http.utils.JsonUtils;
import com.wyy.myhealth.ui.absfragment.utils.TimeUtility;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.ui.customview.BingListView;
import com.wyy.myhealth.ui.customview.BingListView.IXListViewListener;
import com.wyy.myhealth.ui.fooddetails.FoodDetailsActivity;
import com.wyy.myhealth.ui.message.utility.MsgUtility;
import com.wyy.myhealth.ui.mood.MoodDetailsActivity2;
import com.wyy.myhealth.utils.BingLog;

public class MsgListActivity extends BaseActivity implements ActivityInterface,
		IXListViewListener, OnRefreshListener, OnItemClickListener {

	private BingListView msgListView;

	private SwipeRefreshLayout mSwipeRefreshLayout;

	private List<MsgBean> list = new ArrayList<>();

	private MsgAdapter msgAdapter;

	private String json = "";

	private boolean isloading = false;

	private int size = 0;

	private String first = "0";

	private String limit = "10";

	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg_list_t);
		initView();
		initData();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		msgListView = (BingListView) findViewById(R.id.msg_listview);
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.list_swipe);
		mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_blue_dark,
				android.R.color.holo_green_light,
				android.R.color.holo_green_dark);
		msgListView.setXListViewListener(this);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		msgListView.setOnItemClickListener(this);
		progressDialog = new ProgressDialog(context);
		registerForContextMenu(msgListView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.msg_box, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.clear) {
			clearMsgList();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.msg_list_context, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo aContextMenuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		if (item.getItemId() == R.id.delete) {
			deleteMsg(aContextMenuInfo.position);
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.msg_box);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		msgAdapter = new MsgAdapter(list, context);
		msgListView.setAdapter(msgAdapter);
		reshMgsList();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (!isloading) {
			loadMoreList();
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (!isloading) {
			reshMgsList();
		}
	}

	private void reshMgsList() {
		if (WyyApplication.getInfo() == null) {
			return;
		}
		HealthHttpClient.checkUserMsgList(WyyApplication.getInfo().getId(),
				first, limit, new BingHttpHandler() {

					@Override
					protected void onGetSuccess(JSONObject response) {
						// TODO Auto-generated method stub
						BingLog.i("ÏûÏ¢·µ»Ø:" + response);
						if (JsonUtils.isSuccess(response)) {
							if (json.equals(response.toString())) {
								Toast.makeText(context, R.string.nonewmsg,
										Toast.LENGTH_SHORT).show();
								return;
							}

							json = response.toString();
							try {
								JSONObject msg = response
										.getJSONObject("message");
								JSONArray array = msg
										.getJSONArray("resultlist");
								int length = array.length();
								for (int i = 0; i < length; i++) {
									MsgBean msgBean = JsonUtils
											.getMsgBean(array.getJSONObject(i));
									if (msgBean != null) {
										msgBean.setCn_time(TimeUtility
												.getListTime(msgBean
														.getCreatetime()));
									}
									if (!MsgUtility.isHasByMsg(list, msgBean)) {
										list.add(msgBean);
									}

								}
								msgAdapter.notifyDataSetChanged();
								size = list.size();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						isloading = true;
						mSwipeRefreshLayout.setRefreshing(true);
					}

					@Override
					protected void onGetFinish() {
						// TODO Auto-generated method stub
						isloading = false;
						mSwipeRefreshLayout.setRefreshing(false);
					}
				});
	}

	private void loadMoreList() {
		if (WyyApplication.getInfo() == null) {
			return;
		}
		HealthHttpClient.checkUserMsgList(WyyApplication.getInfo().getId(),
				size + "", limit, new BingHttpHandler() {

					@Override
					protected void onGetSuccess(JSONObject response) {
						// TODO Auto-generated method stub
						if (JsonUtils.isSuccess(response)) {
							try {
								JSONObject msg = response
										.getJSONObject("message");
								JSONArray array = msg
										.getJSONArray("resultlist");
								int length = array.length();
								for (int i = 0; i < length; i++) {
									MsgBean msgBean = JsonUtils
											.getMsgBean(array.getJSONObject(i));
									if (msgBean != null) {
										msgBean.setCn_time(TimeUtility
												.getListTime(msgBean
														.getCreatetime()));
									}
									list.add(msgBean);

								}
								msgAdapter.notifyDataSetChanged();
								size = list.size();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						isloading = true;
						mSwipeRefreshLayout.setRefreshing(true);
					}

					@Override
					protected void onGetFinish() {
						// TODO Auto-generated method stub
						isloading = false;
						mSwipeRefreshLayout.setRefreshing(false);
					}
				});
	}

	private void clearMsgList() {
		if (WyyApplication.getInfo() == null) {
			return;
		}
		HealthHttpClient.clearMsgList(WyyApplication.getInfo().getId(),
				new BingHttpHandler() {

					@Override
					protected void onGetSuccess(JSONObject response) {
						// TODO Auto-generated method stub
						if (JsonUtils.isSuccess(response)) {
							Toast.makeText(context,
									R.string.clear_cache_success,
									Toast.LENGTH_SHORT).show();
							list.clear();
							msgAdapter.notifyDataSetChanged();
						} else {

						}
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						progressDialog.show();
					}

					@Override
					protected void onGetFinish() {
						// TODO Auto-generated method stub
						progressDialog.dismiss();
					}
				});
	}

	private void deleteMsg(int position) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		MsgBean msgBean = list.get(position);
		showDetails(msgBean);
	}

	private void showDetails(MsgBean msgBean) {
		if (msgBean != null) {
			if (msgBean.getObjtype() == ConstantS.MESSAGE_OBJ_TYPE_FOOD) {
				showFoodDetails(msgBean.getObjid());
			} else if (msgBean.getObjtype() == ConstantS.MESSAGE_OBJ_TYPE_MOOD) {
				showMoodDetails(msgBean.getObjid());
			}
		}
	}

	private void showMoodDetails(String id) {
		Intent intent = new Intent();
		intent.setClass(context, MoodDetailsActivity2.class);
		intent.putExtra("moodid", id);
		startActivity(intent);
	}

	private void showFoodDetails(String id) {
		PreferencesFoodsInfo.setfoodId(context, id);
		startActivity(new Intent(context, FoodDetailsActivity.class));
	}

}
