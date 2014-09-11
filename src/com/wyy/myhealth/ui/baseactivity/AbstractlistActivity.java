package com.wyy.myhealth.ui.baseactivity;

import java.lang.reflect.Field;

import com.wyy.myhealth.R;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.ViewConfiguration;

public class AbstractlistActivity extends ActionBarActivity {

	protected Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wrapper);
		context = this;
		onInitFragment();
		onInitActionBar();
		setCusmenutome();
	}

	protected void onInitFragment() {

	}

	protected void onInitActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources()
				.getColor(R.color.themecolor)));
//		actionBar.setBackgroundDrawable(getResources().getDrawable(
//				R.drawable.actionbar_g_bg));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	
	private void setCusmenutome() {
		try {
			ViewConfiguration mconfig = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(mconfig, false);
			}
		} catch (Exception ex) {
		}
	}
	
}
