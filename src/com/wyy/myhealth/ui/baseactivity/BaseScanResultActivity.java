package com.wyy.myhealth.ui.baseactivity;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyy.myhealth.R;
import com.wyy.myhealth.utils.BingLog;

public class BaseScanResultActivity extends BaseActivity {
	protected TextView vitaminstxt;
	protected TextView proteinstxt;
	protected TextView mineralstxt;
	protected TextView fatitxt;
	protected TextView sugartxt;
	protected TextView energytxt;
	protected TextView notice_Content;
	protected ImageView notice_Img;

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

	private static final int[] stateImgs={R.drawable.ic_nutrition_normal,R.drawable.ic_nutrition_warn};

	protected void initScoreV() {
		vitaminstxt = (TextView) findViewById(R.id.vatamin_txt);
		proteinstxt = (TextView) findViewById(R.id.protein_txt);
		mineralstxt = (TextView) findViewById(R.id.mine_txt);
		fatitxt = (TextView) findViewById(R.id.fat_txt);
		sugartxt = (TextView) findViewById(R.id.surgar_txt);
		energytxt = (TextView) findViewById(R.id.enger_txt);
		notice_Content=(TextView)findViewById(R.id.face_txt);
		notice_Img=(ImageView)findViewById(R.id.face_img);
		initScaleAnimation();
	}


	/*************** 获取建议分数 ***************/
	protected void getnextvitaminsimgs(int score) {

		if (vitasocre > score && score != 0) {
			vitaminstxt.setBackgroundResource(stateImgs[1]);
			
		}else {
			vitaminstxt.setBackgroundResource(stateImgs[0]);
			
		}
		if (ishasScale) {
			startAnimation(vitaminstxt);
		}

	}

	protected void getnextproteinsimgs(int score) {
		if (proteinsocre > score && score != 0) {
			proteinstxt.setBackgroundResource(stateImgs[1]);
		}else {
			proteinstxt.setBackgroundResource(stateImgs[0]);
		}
		if (ishasScale) {
			startAnimation(proteinstxt);
		}

	}

	protected void getnextmineralsimgs(int score) {
		if (minerasocre > score && score != 0) {
			mineralstxt.setBackgroundResource(stateImgs[1]);
		}else {
			mineralstxt.setBackgroundResource(stateImgs[0]);
		}
		if (ishasScale) {
			startAnimation(mineralstxt);
		}
	}

	protected void getnextfatimgs(int score) {

		if (fatsocre > score ||(score == 0&&fatsocre>3)) {
			fatitxt.setBackgroundResource(stateImgs[1]);
		}else {
			fatitxt.setBackgroundResource(stateImgs[0]);
		}

		if (ishasScale) {
			startAnimation(fatitxt);
		}

	}

	protected void getnextsugarimgs(int score) {

		if (sugarsocre > score ||(score == 0&&sugarsocre>3) ) {
			sugartxt.setBackgroundResource(stateImgs[1]);
		}else {
			sugartxt.setBackgroundResource(stateImgs[0]);
		}

		if (ishasScale) {
			startAnimation(sugartxt);
		}

	}

	protected void getnextenergysimgs(int score) {
		if (energysocre > score ||(score == 0&&energysocre>3)) {
			energytxt.setBackgroundResource(stateImgs[1]);
			BingLog.i("指数", "正常:"+energysocre+"::"+score);
		}else {
			energytxt.setBackgroundResource(stateImgs[0]);
			BingLog.i("指数", "超标:"+energysocre+"::"+score);
		}

		if (ishasScale) {
			startAnimation(energytxt);
		}

	}

	private void initScaleAnimation() {
		scaleAnimation = AnimationUtils.loadAnimation(context,
				R.anim.nutritionscale);
	}

	private void startAnimation(TextView v) {
		Animation mAnimation = AnimationUtils.loadAnimation(context,
				R.anim.nutritionset);
		v.startAnimation(mAnimation);
	}
}
