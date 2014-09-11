package com.wyy.myhealth.ui.healthbar;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.wyy.myhealth.R;
import com.wyy.myhealth.baidu.utlis.Utils;
import com.wyy.myhealth.ui.baseactivity.AbstractlistActivity;
import com.wyy.myhealth.ui.collect.CollectActivity;

public class HealthPassActivity extends AbstractlistActivity {

	@Override
	protected void onInitFragment() {
		// TODO Auto-generated method stub
		super.onInitFragment();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.wrapper, new HealPassFragment()).commit();
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(R.string.healthpass);
		if (Utils.mstlList != null && Utils.mstlList.size() > 0) {
			actionBar.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.action_bar_notice));
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.healthpass, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		if (Utils.mstlList.size() > 0) {
			menu.findItem(R.id.msglist).setTitle(
					Utils.mstlList.size() + getString(R.string.msg_unit));
		} else {
			menu.findItem(R.id.msglist).setTitle(R.string.msglist);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {

		case R.id.msglist:
			showMsgList();
			break;

		case R.id.mycollect:
			showCollect();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showMsgList() {
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(getResources().getColor(R.color.themecolor)));
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		startActivity(new Intent(context, MsgListActivity.class));
	}

	private void showCollect() {
		startActivity(new Intent(context, CollectActivity.class));
	}

}
