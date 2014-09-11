package com.wyy.myhealth.ui.icebox;

import com.wyy.myhealth.R;
import com.wyy.myhealth.bean.HealthRecoderBean;
import com.wyy.myhealth.bean.IceBoxFoodBean;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.imag.utils.LoadImageUtils;
import com.wyy.myhealth.service.MainService;
import com.wyy.myhealth.ui.absfragment.utils.TimeUtility;
import com.wyy.myhealth.ui.baseactivity.BaseNutritionActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.ui.icebox.utils.FoodTypeUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class IceBoxInfoActivity extends BaseNutritionActivity implements
		ActivityInterface {

	private IceBoxFoodBean iceBoxFoodBean;

	private TextView foodname;

	private TextView foodday;

	private TextView foodtype;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_icebox_info);
		initView();
		setIshasScale(true);
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().hide();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		foodname = (TextView) findViewById(R.id.food_name);
		foodtype = (TextView) findViewById(R.id.food_style);
		foodday = (TextView) findViewById(R.id.good_day_num);

		iceBoxFoodBean = (IceBoxFoodBean) getIntent().getSerializableExtra(
				"food");
		LoadImageUtils.loadImageCirImageV(
				(ImageView) findViewById(R.id.food_pic),
				HealthHttpClient.IMAGE_URL + iceBoxFoodBean.getFoodpic());
		initScoreV();
		mHandler.sendEmptyMessageDelayed(0, 1000);
		findViewById(R.id.wrapper).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		findViewById(R.id.delete).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendDeleteBrocast();
			}
		});

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		if (null != iceBoxFoodBean
				&& MainService.getNextHealthRecoderBeans() != null) {

			try {
				foodname.setText(iceBoxFoodBean.getName());
				foodtype.setText(FoodTypeUtils.getfoodtype(iceBoxFoodBean
						.getType()));
				foodday.setText(countDay());
			} catch (Exception e) {
				// TODO: handle exception
			}

			try {
				vitasocre = iceBoxFoodBean.getVitamin();
				getvitaminsimgs(vitasocre);
				proteinsocre = Integer.valueOf(iceBoxFoodBean.getProtein());
				getproteinsimgs(proteinsocre);
				minerasocre = Integer.valueOf(iceBoxFoodBean.getMineral());
				getmineralsimgs(minerasocre);
				fatsocre = Integer.valueOf(iceBoxFoodBean.getFat());
				getfatimgs(fatsocre);
				sugarsocre = Integer.valueOf(iceBoxFoodBean.getSugar());
				getsugarimgs(sugarsocre);
				energysocre = Integer.valueOf(iceBoxFoodBean.getEnergy());
				getenergysimgs(energysocre);
			} catch (Exception e) {
				// TODO: handle exception

			}

			if (MainService.getNextHealthRecoderBeans().size() == 0) {
				return;
			}

			HealthRecoderBean healthRecoderBean = MainService
					.getNextHealthRecoderBeans().get(
							MainService.getNextHealthRecoderBeans().size() - 1);

			try {
				getnextvitaminsimgs(Integer.valueOf(healthRecoderBean
						.getVitamin()));
				getnextproteinsimgs(Integer.valueOf(healthRecoderBean
						.getProtein()));
				getnextmineralsimgs(Integer.valueOf(healthRecoderBean
						.getMineral()));
				getnextfatimgs(Integer.valueOf(healthRecoderBean.getFat()));
				getnextsugarimgs(Integer.valueOf(healthRecoderBean.getSugar()));
				getnextenergysimgs(Integer.valueOf(healthRecoderBean
						.getEnergy()));
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	private Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			initData();
			return false;
		}
	});

	private void sendDeleteBrocast() {
		Intent intent = new Intent();
		intent.setAction(ConstantS.ACTION_SEND_DELETE_ICEFOOD);
		intent.putExtra("food", iceBoxFoodBean);
		sendBroadcast(intent);
		finish();
	}

	private String countDay() {
		int left=Integer.valueOf(iceBoxFoodBean.getNumday())
				- TimeUtility.getday2now(iceBoxFoodBean.getCreatetime());
		if (left<0) {
			return getString(R.string.expired);
		}else {
			return getString(R.string.remain) + left
					+ getString(R.string.day_);
		}
		
	}

}
