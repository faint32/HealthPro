package com.wyy.myhealth.ui.baseactivity;

import com.wyy.myhealth.R;

import android.view.Menu;
import android.view.MenuItem;

public abstract class SubmitActivity extends BaseActivity {

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.submit, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	protected abstract void submitMsg();
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.submit:
			submitMsg();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
