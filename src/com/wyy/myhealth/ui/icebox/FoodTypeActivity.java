package com.wyy.myhealth.ui.icebox;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.wyy.myhealth.R;
import com.wyy.myhealth.ui.baseactivity.SaveActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.utils.BingLog;

public class FoodTypeActivity extends SaveActivity implements ActivityInterface {

	private int type = 2;
	private RadioGroup radioGroup;
	private static final String TAG=FoodTypeActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_food_tyoe);
		initView();
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.food_type);
	}
	
	@Override
	protected void submitMsg() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context, IceBoxAddFood.class);
		intent.putExtra("type", type);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				
				case R.id.vgetablis:
					type = 1;
					break;

				case R.id.fruit:
					type = 2;
					BingLog.i(TAG, "食物类型:"+type);
					break;

				case R.id.meat:
					type = 3;
					break;

				case R.id.stable_food:
					type = 4;
					break;

				case R.id.other:
					type = 5;
					break;

				default:
					break;
				}
			}
		});

		initData();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		type = getIntent().getIntExtra("type", 0);
		if (type > 0) {
			switch (type) {
			case 0:
				break;

			case 1:

				break;

			case 2:

				break;

			case 3:

				break;

			case 4:

				break;

			default:
				break;
			}
		}
	}

}
