package com.wyy.myhealth.ui.scan;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.wyy.myhealth.R;
import com.wyy.myhealth.ui.baseactivity.SaveActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;

public class GetCommercial extends SaveActivity implements ActivityInterface {

	private EditText nameEdit;

	private String nameString;

	private EditText telEdit;

	private String telString;

	@Override
	protected void submitMsg() {
		// TODO Auto-generated method stub
		nameEdit.setError(null);
		Intent intent = new Intent(context, ShareFoodActivity.class);
		nameString = nameEdit.getText().toString();
		if (TextUtils.isEmpty(nameString)) {
			nameEdit.setError(getString(R.string.nullcontentnotice));
			nameEdit.requestFocus();
			return;
		}
		telString = telEdit.getText().toString();

		intent.putExtra("name", nameString);
		intent.putExtra("tel", telString);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commercial);
		initView();
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.commercialname);
	}
	
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		nameEdit = (EditText) findViewById(R.id.commercial_name);
		telEdit = (EditText) findViewById(R.id.commercial_tel);
		initData();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		nameString = getIntent().getStringExtra("name");
		telString = getIntent().getStringExtra("tel");

		if (!TextUtils.isEmpty(nameString)) {
			nameEdit.setText(""+nameString);
		}

		if (!TextUtils.isEmpty(telString)) {
			telEdit.setText(""+telString);
		}

	}

}
