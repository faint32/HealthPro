package com.wyy.myhealth.ui.scan;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.ui.baseactivity.SaveActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;

public class GetFoodAddress extends SaveActivity implements ActivityInterface {

	private EditText addressedit;

	private String address = "";

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar()
				.setTitle(R.string.place);
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
		addressedit.setError(null);
		Intent intent = new Intent(context, ShareFoodActivity.class);
		address = addressedit.getText().toString();
		if (TextUtils.isEmpty(address)) {
			addressedit.setError(getString(R.string.nullcontentnotice));
			return;
		}
		intent.putExtra("address", address);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		addressedit = (EditText) findViewById(R.id.share_tag_address);
		initData();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		addressedit.setHint(R.string.inputaddress_hint);
		address = getIntent().getStringExtra("address");
		if (!TextUtils.isEmpty(address)) {
			addressedit.setText("" + address);
		}
		
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
	

}
