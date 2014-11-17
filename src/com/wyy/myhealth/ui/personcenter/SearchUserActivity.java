package com.wyy.myhealth.ui.personcenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.wyy.myhealth.R;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.utils.BingLog;

public class SearchUserActivity extends BaseActivity implements
		ActivityInterface {

	private MultiAutoCompleteTextView searchView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_user);
		initView();
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.search_);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.search_user, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.check_near) {
			showNearuser();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		searchView = (MultiAutoCompleteTextView) findViewById(R.id.search_user);
		searchView.setOnEditorActionListener(searchActionListener);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	private OnEditorActionListener searchActionListener = new OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			// TODO Auto-generated method stub
			BingLog.i("action:" + actionId);
			if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
				showUserSearch();
			}
			return false;
		}
	};

	private void showNearuser() {
		startActivity(new Intent(context, NearUserActivity.class));
	}

	private void showUserSearch() {
		Intent intent = new Intent();
		intent.putExtra("txt", searchView.getText().toString());
		intent.setClass(context, UserSearchResultActivity.class);
		startActivity(intent);
	}

}
