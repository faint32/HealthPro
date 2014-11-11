package com.wyy.myhealth.ui.fooddetails;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.bean.HealthRecoderBean;
import com.wyy.myhealth.bean.NearFoodBean;
import com.wyy.myhealth.imag.utils.LoadImageUtils;
import com.wyy.myhealth.service.MainService;
import com.wyy.myhealth.ui.baseactivity.BaseNutritionActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.ui.navigation.NutritionNavActivity;
import com.wyy.myhealth.utils.BingLog;

public class FoodNutritionActivity extends BaseNutritionActivity implements
		ActivityInterface {

	private static final String TAG = FoodNutritionActivity.class
			.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_food_nutri_info);
		initView();
		setIshasScale(true);
		initNutriNav();
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
	
	
	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.nutritionnfo);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		LoadImageUtils.loadImage4ImageV(
				(ImageView) findViewById(R.id.food_pic),
				FoodDetailsActivity.imgurl);
		initScoreV();
		// initData();
		mHandler.sendEmptyMessageDelayed(0, 1000);

		findViewById(R.id.wrapper).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

		NearFoodBean nearFoodBean = (NearFoodBean) getIntent().getSerializableExtra("comment");
		BingLog.i(TAG, "���:" + nearFoodBean);
		if (null != nearFoodBean && MainService.getNextHealthRecoderBeans() != null) {

			try {
				vitasocre = Integer.valueOf(nearFoodBean.getVitamin());
				getvitaminsimgs(vitasocre);
				proteinsocre = Integer.valueOf(nearFoodBean.getProtein());
				getproteinsimgs(proteinsocre);
				minerasocre = Integer.valueOf(nearFoodBean.getMineral());
				getmineralsimgs(minerasocre);
				fatsocre = Integer.valueOf(nearFoodBean.getFat());
				getfatimgs(fatsocre);
				sugarsocre = Integer.valueOf(nearFoodBean.getSugar());
				getsugarimgs(sugarsocre);
				energysocre = Integer.valueOf(nearFoodBean.getEnergy());
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

	private void initNutriNav() {
		if (NutritionNavActivity.getIsFirstUse(context)) {
			startActivity(new Intent(context, NutritionNavActivity.class));
		}
	}
	
}
