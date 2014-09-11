package com.wyy.myhealth.ui.baseactivity;

import android.view.Menu;
import android.view.MenuItem;

import com.wyy.myhealth.R;

public abstract class SaveActivity extends BaseActivity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.save, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	protected abstract void submitMsg();
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.save:
			submitMsg();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
