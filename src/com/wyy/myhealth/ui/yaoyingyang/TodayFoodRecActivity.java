package com.wyy.myhealth.ui.yaoyingyang;

import android.os.Bundle;

import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.ui.baseactivity.AbstractlistActivity;

public class TodayFoodRecActivity extends AbstractlistActivity {

	public static boolean isTopten=false;
	
	@Override
	protected void onInitFragment() {
		// TODO Auto-generated method stub
		super.onInitFragment();
		
	}

	@Override
	protected void onInitFragment(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onInitFragment(savedInstanceState);
		if (savedInstanceState==null) {
			isTopten=true;
			getSupportFragmentManager().beginTransaction()
			.add(R.id.wrapper, new TodayFoodRecFragment()).commit();
		}else {
			isTopten=savedInstanceState.getBoolean("isTopten", true);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putBoolean("isTopten", isTopten);
	}
	
	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.today_food);
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isTopten=false;
	}
	
}
