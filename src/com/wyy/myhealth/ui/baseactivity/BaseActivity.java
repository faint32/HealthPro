package com.wyy.myhealth.ui.baseactivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.umeng.message.PushAgent;
import com.wyy.myhealth.R;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;

public class BaseActivity extends ActionBarActivity {

	protected Activity context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		context=this;
		PushAgent.getInstance(context).onAppStart();
		onInitActionBar();
		onInitUI();
		onInitFragment();
		setCusmenutome();
	}

	protected void onInitFragment() {

	}
	
	protected  void onInitUI(){
		
	}

	protected void onInitActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources()
				.getColor(R.color.themecolor)));
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
	
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// TODO Auto-generated method stub

		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);

				} catch (Exception e) {
				}
			}
		}

		return super.onMenuOpened(featureId, menu);
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
