package com.wyy.myhealth.ui.scan;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.wyy.myhealth.R;
import com.wyy.myhealth.ui.baseactivity.SaveActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;

public class GetfoodTagActivity extends SaveActivity implements
		ActivityInterface {

	private EditText tagedit;

	private String tag = "";

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar()
				.setTitle(R.string.abc_action_bar_home_description);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_tag);
		initView();
	}

	@Override
	protected void submitMsg() {
		// TODO Auto-generated method stub
		tagedit.setError(null);
		Intent intent = new Intent(context, ShareFoodActivity.class);
		tag = tagedit.getText().toString();
		if (TextUtils.isEmpty(tag)) {
			tagedit.setError(getString(R.string.nullcontentnotice));
			return;
		}
		intent.putExtra("tag", tag);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		tagedit = (EditText) findViewById(R.id.share_tag_address);
		initData();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		tag = getIntent().getStringExtra("tag");
		if (TextUtils.isEmpty(tag)) {
			return;
		}
		tagedit.setText("" + tag);
	}

}
