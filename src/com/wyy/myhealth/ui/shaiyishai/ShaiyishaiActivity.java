package com.wyy.myhealth.ui.shaiyishai;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.wyy.myhealth.R;
import com.wyy.myhealth.bean.MoodaFoodBean;
import com.wyy.myhealth.http.JsonHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.http.utils.JsonUtils;
import com.wyy.myhealth.ui.absfragment.utils.TimeUtility;
import com.wyy.myhealth.ui.baseactivity.AbstractlistActivity;
import com.wyy.myhealth.ui.collect.CollectActivity;
import com.wyy.myhealth.utils.BingLog;

public class ShaiyishaiActivity extends AbstractlistActivity implements
		OnQueryTextListener {

	private static final String TAG = ShaiyishaiActivity.class.getSimpleName();
	private ShaiyishaiFragment shaiyishaiFragment;
	private SearchView searchView;
	private static final String LIMIT = "10";

	@Override
	protected void onInitFragment() {
		// TODO Auto-generated method stub
		super.onInitFragment();
		getPushFood();
		
		
	}

	
	@Override
	protected void onInitFragment(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onInitFragment(savedInstanceState);
		if (savedInstanceState==null) {
			shaiyishaiFragment = new ShaiyishaiFragment();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.wrapper, shaiyishaiFragment).commit();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putBoolean("islive", true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.shai_menu, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		searchView.setOnQueryTextListener(this);
		ImageView icon = (ImageView) searchView
				.findViewById(android.support.v7.appcompat.R.id.search_button);
		icon.setImageResource(R.drawable.ic_search);
		searchView.setQueryHint(getString(R.string.search));
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(R.string.shayishai);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.publis_shai:
			startActivity(new Intent(context, PublishActivity.class));
			break;

		case R.id.mycollect:
			startActivity(new Intent(context, CollectActivity.class));
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (shaiyishaiFragment.getSendView().isShown()) {
				shaiyishaiFragment.getSendView().setVisibility(View.GONE);
				return true;
			}

		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		shaiyishaiFragment.setKey(arg0);
		shaiyishaiFragment.reshShayiSai("0", LIMIT);
		return true;
	}

	private void getPushFood() {
		HealthHttpClient.pushFoods(handler);
	}

	private JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(response);
			parseJson(response);
		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
		}

	};

	private void parseJson(JSONObject response) {
		BingLog.i(TAG, "¹ã¸æ:" + response);
		if (JsonUtils.isSuccess(response)) {
			try {
				// JSONObject food = response.getJSONObject("foods");
				JSONArray foodlist = response.getJSONArray("foods");
				int length = foodlist.length();
				for (int i = 0; i < length&&i<1; i++) {
					MoodaFoodBean moodaFoodBean = JsonUtils
							.getMoodaFoodBean(foodlist.getJSONObject(i));
					moodaFoodBean.setAdv(true);
					moodaFoodBean.setCn_time(TimeUtility
							.getListTime(moodaFoodBean.getCreatetime()));
					shaiyishaiFragment.addAdversie(moodaFoodBean);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				BingLog.i(TAG, "´íÎó:" + e.getMessage());
			}
		}
	}

}
