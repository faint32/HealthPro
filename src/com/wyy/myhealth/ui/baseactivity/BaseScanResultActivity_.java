package com.wyy.myhealth.ui.baseactivity;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyy.myhealth.R;
import com.wyy.myhealth.utils.BingLog;

public class BaseScanResultActivity_ extends BaseActivity {

	protected ImageView vitaminsimg;
	protected ImageView proteinsimg;
	protected ImageView mineralsimg;
	protected ImageView fatimg;
	protected ImageView sugarimg;
	protected ImageView energyimg;
	protected TextView notice_Content;
	protected ImageView notice_Img;
	protected Animation scaleAnimation;

	private boolean ishasScale = false;

	private int i = 0;

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

	private static int[][] stateImgs = new int[6][2];

	static {
		stateImgs[0][0] = R.drawable.ic_va_nor;
		stateImgs[0][1] = R.drawable.ic_va_war;
		stateImgs[1][0] = R.drawable.ic_pr_nor;
		stateImgs[1][1] = R.drawable.ic_pr_war;
		stateImgs[2][0] = R.drawable.ic_mi_nor;
		stateImgs[2][1] = R.drawable.ic_mi_war;
		stateImgs[3][0] = R.drawable.ic_fa_nor;
		stateImgs[3][1] = R.drawable.ic_fa_war;
		stateImgs[4][0] = R.drawable.ic_su_nor;
		stateImgs[4][1] = R.drawable.ic_su_war;
		stateImgs[5][0] = R.drawable.ic_en_nor;
		stateImgs[5][1] = R.drawable.ic_en_war;
	}

	private static final int[] VIEWS = { R.id.vatamin_img, R.id.protein_img,
			R.id.mine_img, R.id.fat_img, R.id.surgar_img, R.id.enger_img };

	protected void initScoreV() {
		vitaminsimg = (ImageView) findViewById(R.id.vatamin_img);
		proteinsimg = (ImageView) findViewById(R.id.protein_img);
		mineralsimg = (ImageView) findViewById(R.id.mine_img);
		fatimg = (ImageView) findViewById(R.id.fat_img);
		sugarimg = (ImageView) findViewById(R.id.surgar_img);
		energyimg = (ImageView) findViewById(R.id.enger_img);
		notice_Content = (TextView) findViewById(R.id.face_txt);
		notice_Img = (ImageView) findViewById(R.id.face_img);
		initScaleAnimation();
	}

	/*************** 获取建议分数 ***************/
	protected void getnextvitaminsimgs(int score) {

		if (vitasocre > score && score != 0) {
			vitaminsimg.setImageResource(stateImgs[0][1]);

		} else {
			vitaminsimg.setImageResource(stateImgs[0][0]);

		}

	}

	protected void getnextproteinsimgs(int score) {
		if (proteinsocre > score && score != 0) {
			proteinsimg.setImageResource(stateImgs[1][1]);
		} else {
			proteinsimg.setImageResource(stateImgs[1][0]);
		}

	}

	protected void getnextmineralsimgs(int score) {
		if (minerasocre > score && score != 0) {
			mineralsimg.setImageResource(stateImgs[2][1]);
		} else {
			mineralsimg.setImageResource(stateImgs[2][0]);
		}
	}

	protected void getnextfatimgs(int score) {

		if (fatsocre > score || (score == 0 && fatsocre > 3)) {
			fatimg.setImageResource(stateImgs[3][1]);
		} else {
			fatimg.setImageResource(stateImgs[3][0]);
		}

	}

	protected void getnextsugarimgs(int score) {

		if (sugarsocre > score || (score == 0 && sugarsocre > 3)) {
			sugarimg.setImageResource(stateImgs[4][1]);
		} else {
			sugarimg.setImageResource(stateImgs[4][0]);
		}

	}

	protected void getnextenergysimgs(int score) {
		if (energysocre > score || (score == 0 && energysocre > 3)) {
			energyimg.setImageResource(stateImgs[5][1]);
			BingLog.i("指数", "正常:" + energysocre + "::" + score);
		} else {
			energyimg.setImageResource(stateImgs[5][0]);
			BingLog.i("指数", "超标:" + energysocre + "::" + score);
		}
		
		if (ishasScale) {
			startAmin(0);
		}

	}

	private void initScaleAnimation() {
		scaleAnimation = AnimationUtils.loadAnimation(context,
				R.anim.nutritionscale);
	}

	private void startAmin(int j) {
		i = j;
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.aplha_);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				if (i < VIEWS.length) {
					findViewById(VIEWS[i]).setVisibility(View.VISIBLE);
				}
				i++;
				if (i < VIEWS.length) {
					startAmin(i);
				}
			}
		});
		findViewById(VIEWS[j]).startAnimation(animation);
	}

}
