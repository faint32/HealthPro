package com.wyy.myhealth.ui.collect;

import com.wyy.myhealth.R;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;

public class CollectActivity extends BaseActivity {

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.mycollect);
	}

	@Override
	protected void onInitUI() {
		// TODO Auto-generated method stub
		super.onInitUI();
		setContentView(R.layout.wrapper);
	}

	@Override
	protected void onInitFragment() {
		// TODO Auto-generated method stub
		super.onInitFragment();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.wrapper, new CollectFragment()).commit();

	}

}
