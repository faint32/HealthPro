package com.wyy.myhealth.ui.icebox;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Toast;

import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.bean.IceBoxFoodBean;
import com.wyy.myhealth.db.utils.IceDadabaseUtils;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;

public class IceBoxHistory extends BaseActivity implements ActivityInterface {

	private ListView listView;
	private HistoryAdapter historyAdapter;
	private List<IceBoxFoodBean> list = new ArrayList<>();
	private IceDadabaseUtils iceDadabaseUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ice_box_history);
		initView();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		iceDadabaseUtils.close();
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
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.ice_box_history);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		listView = (ListView) findViewById(R.id.ice_box_history);
		registerForContextMenu(listView);
		initData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.history, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.history_list, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.delete:
			deleteHistory(info.position);
			break;

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.deleteall:
			deleteHistory();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		getHistory();
	}

	@SuppressWarnings("unchecked")
	private void getHistory() {
		iceDadabaseUtils = new IceDadabaseUtils(context);
		list = (List<IceBoxFoodBean>) iceDadabaseUtils.queryData();
		if (list == null) {
			list = new ArrayList<>();
		}

		historyAdapter = new HistoryAdapter(list, context);
		listView.setAdapter(historyAdapter);

	}

	private void deleteHistory(int position) {
		long del = iceDadabaseUtils.delete(list.get(position).getId());
		if (del > 0) {
			list.remove(position);
			historyAdapter.notifyDataSetChanged();
		} else {
			Toast.makeText(context, R.string.delfailure, Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void deleteHistory() {
		long del = iceDadabaseUtils.deleteAll();
		if (del > 0) {
			list.clear();
			historyAdapter.notifyDataSetChanged();
		} else {
			Toast.makeText(context, R.string.delfailure, Toast.LENGTH_SHORT)
					.show();
		}
	}

}
