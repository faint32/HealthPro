package com.wyy.myhealth.ui.icebox;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;

import com.wyy.myhealth.R;
import com.wyy.myhealth.ui.baseactivity.SaveActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.ui.scan.ShareFoodActivity;

public class IceFoodGarDayActivity extends SaveActivity implements
		ActivityInterface {

	private EditText numdayedit;

	private int numday = 0;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_tag);
		initView();
	}
	
	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.good_day);
	}
	
	@Override
	protected void submitMsg() {
		// TODO Auto-generated method stub
		numdayedit.setError(null);
		Intent intent = new Intent(context, ShareFoodActivity.class);
		String numdays = numdayedit.getText().toString();
		if (TextUtils.isEmpty(numdays)) {
			numdayedit.setError(getString(R.string.nullcontentnotice));
			return;
		}

		try {
			numday = Integer.valueOf(numdays);
			intent.putExtra("numday", numday);
			setResult(RESULT_OK, intent);
			finish();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		numdayedit = (EditText) findViewById(R.id.share_tag_address);
		numdayedit.setInputType(InputType.TYPE_CLASS_NUMBER);
		numdayedit.setHint(R.string.input_day_hint);
		initData();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		numday = getIntent().getIntExtra("numday", 0);
		numdayedit.setText(""+numday);
	}

}
