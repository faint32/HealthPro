package com.wyy.myhealth.ui.baseactivity;

import com.wyy.myhealth.R;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseNutritionActivity extends BaseActivity {

	protected ImageView vitaminsimgs;
	protected ImageView proteinsimgs;
	protected ImageView mineralsimgs;
	protected ImageView fatimgs;
	protected ImageView sugarimgs;
	protected ImageView energyimgs;

	protected ImageView nextvitaminsimgs;
	protected ImageView nextproteinsimgs;
	protected ImageView nextmineralsimgs;
	protected ImageView nextfatimgs;
	protected ImageView nextsugarimgs;
	protected ImageView nextenergyimgs;

	protected TextView cproteinTextView;
	protected TextView cfatTextView;
	protected TextView csurgarTextView;
	protected TextView cengerTextView;

	protected Animation scaleAnimation;

	private boolean ishasScale = false;

	/**
	 * 是否出现动画
	 * 
	 * @param ishasScale
	 *            true为添加动画，否则不添加动画，默认不添加动画
	 */
	public void setIshasScale(boolean ishasScale) {
		this.ishasScale = ishasScale;
	}

	protected int vitasocre = 0, proteinsocre = 0, minerasocre = 0,
			fatsocre = 0, sugarsocre = 0, energysocre = 0;

	private static final int[] images = { R.drawable.point0, R.drawable.point1,
			R.drawable.point2, R.drawable.point3, R.drawable.point4,
			R.drawable.point5 };
	private static final int[] energyimg = { R.drawable.point0,
			R.drawable.engergy_point1, R.drawable.engergy_point2,
			R.drawable.engergy_point3, R.drawable.engergy_point4,
			R.drawable.engergy_point5 };
	private static final int[] jianyiimages = { R.drawable.point0,
			R.drawable.jianyi1, R.drawable.jianyi2, R.drawable.jianyi3,
			R.drawable.jianyi4, R.drawable.jianyi5 };

	protected void initScoreV() {
		vitaminsimgs = (ImageView) findViewById(R.id.vitaminsimg);
		proteinsimgs = (ImageView) findViewById(R.id.proteinsimg);
		mineralsimgs = (ImageView) findViewById(R.id.mineralsimg);
		fatimgs = (ImageView) findViewById(R.id.fatimg);
		sugarimgs = (ImageView) findViewById(R.id.sugarimg);
		energyimgs = (ImageView) findViewById(R.id.sao_energyimg);

		nextvitaminsimgs = (ImageView) findViewById(R.id.jianyi_vitaminsimg);
		nextproteinsimgs = (ImageView) findViewById(R.id.jianyi_proteinsimg);
		nextmineralsimgs = (ImageView) findViewById(R.id.jianyi_mineralsimg);
		nextfatimgs = (ImageView) findViewById(R.id.jianyi_fatimg);
		nextsugarimgs = (ImageView) findViewById(R.id.jianyi_sugarimg);
		nextenergyimgs = (ImageView) findViewById(R.id.jianyi_sao_energyimg);

		cproteinTextView = (TextView) findViewById(R.id.chaobiao_proteins_text);
		cfatTextView = (TextView) findViewById(R.id.chaobiao_fat_text);
		csurgarTextView = (TextView) findViewById(R.id.chaobiao_sugar_text);
		cengerTextView = (TextView) findViewById(R.id.chaobiao_energy_text);
		initScaleAnimation();
	}

	protected void getvitaminsimgs(int score) {

		vitaminsimgs.setBackgroundResource(images[score]);
		if (ishasScale) {
//			vitaminsimgs.startAnimation(scaleAnimation);
			startAnimation(vitaminsimgs);
		}

	}

	protected void getproteinsimgs(int score) {

		proteinsimgs.setBackgroundResource(images[score]);
		if (ishasScale) {
//			proteinsimgs.startAnimation(scaleAnimation);
			startAnimation(proteinsimgs);
		}

	}

	protected void getmineralsimgs(int score) {

		mineralsimgs.setBackgroundResource(images[score]);
		if (ishasScale) {
//			mineralsimgs.startAnimation(scaleAnimation);
			startAnimation(mineralsimgs);
		}

	}

	protected void getfatimgs(int score) {

		fatimgs.setBackgroundResource(images[score]);
		if (ishasScale) {
//			fatimgs.startAnimation(scaleAnimation);
			startAnimation(fatimgs);
		}

	}

	protected void getsugarimgs(int score) {

		sugarimgs.setBackgroundResource(images[score]);

		if (ishasScale) {
//			sugarimgs.startAnimation(scaleAnimation);
			startAnimation(sugarimgs);
		}

	}

	protected void getenergysimgs(int score) {
		if (score == 0) {
			return;
		}
		energyimgs.setBackgroundResource(energyimg[score]);

		if (ishasScale) {
//			energyimgs.startAnimation(scaleAnimation);
			startAnimation(energyimgs);
		}

	}

	/*************** 获取建议分数 ***************/
	protected void getnextvitaminsimgs(int score) {

		nextvitaminsimgs.setBackgroundResource(jianyiimages[score]);
		if (ishasScale) {
//			nextvitaminsimgs.startAnimation(scaleAnimation);
			startAnimation(nextvitaminsimgs);
		}

	}

	protected void getnextproteinsimgs(int score) {
		if (proteinsocre > score && score != 0) {
			cproteinTextView.setVisibility(View.VISIBLE);
		}
		nextproteinsimgs.setBackgroundResource(jianyiimages[score]);
		if (ishasScale) {
//			nextproteinsimgs.startAnimation(scaleAnimation);
			startAnimation(nextproteinsimgs);
		}

	}

	protected void getnextmineralsimgs(int score) {
		nextmineralsimgs.setBackgroundResource(jianyiimages[score]);
		if (ishasScale) {
//			nextmineralsimgs.startAnimation(scaleAnimation);
			startAnimation(nextmineralsimgs);
		}
	}

	protected void getnextfatimgs(int score) {

		if (fatsocre > score && score != 0) {
			cfatTextView.setVisibility(View.VISIBLE);
		}

		nextfatimgs.setBackgroundResource(jianyiimages[score]);

		if (ishasScale) {
//			nextfatimgs.startAnimation(scaleAnimation);
			startAnimation(nextfatimgs);
		}

	}

	protected void getnextsugarimgs(int score) {

		if (sugarsocre > score && score != 0) {
			csurgarTextView.setVisibility(View.VISIBLE);
		}

		nextsugarimgs.setBackgroundResource(jianyiimages[score]);

		if (ishasScale) {
//			nextsugarimgs.startAnimation(scaleAnimation);
			startAnimation(nextsugarimgs);
		}

	}

	protected void getnextenergysimgs(int score) {
		if (score == 0) {
			return;
		}

		if (energysocre > score) {
			cengerTextView.setVisibility(View.VISIBLE);
		}
		nextenergyimgs.setBackgroundResource(jianyiimages[score]);

		if (ishasScale) {
//			nextenergyimgs.startAnimation(scaleAnimation);
			startAnimation(nextenergyimgs);
		}

	}

	private void initScaleAnimation() {
		scaleAnimation = AnimationUtils.loadAnimation(context,
				R.anim.nutritionscale);
	
		
	}
	
	
	private void startAnimation(ImageView v){
		v.setVisibility(View.VISIBLE);
		Animation mAnimation=AnimationUtils.loadAnimation(context, R.anim.nutritionscale);
		v.startAnimation(mAnimation);
		
	}
	

}
