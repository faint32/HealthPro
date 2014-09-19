package com.wyy.myhealth.ui.collect;

import android.view.KeyEvent;
import android.view.View;

import com.wyy.myhealth.R;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;

public class CollectActivity extends BaseActivity {

	private CollectFragment collectFragment;
	
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
		collectFragment=new CollectFragment();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.wrapper, collectFragment).commit();

	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (collectFragment.getSendView().isShown()) {
				collectFragment.getSendView().setVisibility(View.GONE);
				return true;
			}

		}

		return super.onKeyDown(keyCode, event);
	}
	
}
