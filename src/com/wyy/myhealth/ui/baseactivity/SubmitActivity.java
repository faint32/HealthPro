package com.wyy.myhealth.ui.baseactivity;

import com.wyy.myhealth.R;

import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;

public abstract class SubmitActivity extends BaseActivity {

	protected Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			showInput();
			return false;
		}
	});

	protected static final long DELAY_TIME = 500;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.submit, menu);
		return super.onCreateOptionsMenu(menu);
	}

	protected abstract void submitMsg();

	protected void showInput() {
	}

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
