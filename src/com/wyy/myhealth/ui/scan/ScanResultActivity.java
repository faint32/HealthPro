package com.wyy.myhealth.ui.scan;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

import com.wyy.myhealth.R;
import com.wyy.myhealth.bean.HealthRecoderBean;
import com.wyy.myhealth.bean.NearFoodBean;
import com.wyy.myhealth.bean.ScanMoodBean;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.file.utils.FileUtils;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.imag.utils.LoadImageUtils;
import com.wyy.myhealth.imag.utils.SavePic;
import com.wyy.myhealth.service.MainService;
import com.wyy.myhealth.ui.baseactivity.BaseScanResultActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.ui.scan.utils.DialogShow;
import com.wyy.myhealth.utils.BingLog;
import com.wyy.myhealth.utils.ShareUtils;

public class ScanResultActivity extends BaseScanResultActivity implements
		ActivityInterface {

	private static final String TAG = ScanResultActivity.class.getSimpleName();

	private FrameLayout successlay;

	private FrameLayout failurelay;

	private NearFoodBean samefood;

	private ScanMoodBean scanMoodBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan__success);
		initView();
		sendChangeUI();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		sendChangeUI();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.scan_, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		if (!failurelay.isShown()) {
			menu.findItem(R.id.help).setIcon(android.R.drawable.ic_menu_share);
		}
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.help:
			if (failurelay.isShown()) {
				DialogShow.showHelpDialog(context, getString(R.string.sao_help));
			}else {
				saveScreen(successlay);
			}
			
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.scanresult);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		context = this;
		initScoreV();

		setIshasScale(true);
		successlay = (FrameLayout) findViewById(R.id.scan_success_lay);
		failurelay = (FrameLayout) findViewById(R.id.scan_failure_lay);

		findViewById(R.id.open_ligth).setOnClickListener(listener);
		findViewById(R.id.take_pic).setOnClickListener(listener);
		findViewById(R.id.take_bottom_lay).setOnClickListener(listener);
		findViewById(R.id.loop_yuyin).setOnClickListener(listener);
		findViewById(R.id.jilufood).setOnClickListener(listener);

		initData();

		// DialogShowFeture.showFetureDialog(context, Config.log + "\n"
		// + Config.feture_Value + "\n" + Config.placeValue);

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		HealthRecoderBean healthRecoderBean = (HealthRecoderBean) getIntent()
				.getSerializableExtra("foods");

		samefood = (NearFoodBean) getIntent().getSerializableExtra("samefood");
		scanMoodBean = (ScanMoodBean) getIntent().getSerializableExtra("face");

		if (scanMoodBean != null) {
			notice_Content.setText(scanMoodBean.getContent());
			LoadImageUtils.loadImage4ImageV(notice_Img,
					HealthHttpClient.IMAGE_URL + scanMoodBean.getImg());
		}

		if (null != healthRecoderBean
				&& MainService.getNextHealthRecoderBeans() != null) {

			try {
				vitasocre = Integer.valueOf(healthRecoderBean.getVitamin());
				proteinsocre = Integer.valueOf(healthRecoderBean.getProtein());
				minerasocre = Integer.valueOf(healthRecoderBean.getMineral());
				fatsocre = Integer.valueOf(healthRecoderBean.getFat());
				sugarsocre = Integer.valueOf(healthRecoderBean.getSugar());
				energysocre = Integer.valueOf(healthRecoderBean.getEnergy());

			} catch (Exception e) {
				// TODO: handle exception

			}

			try {

				HealthRecoderBean healthRecoderBeann = MainService
						.getNextHealthRecoderBeans()
						.get(MainService.getNextHealthRecoderBeans().size() - 1);
				getnextvitaminsimgs(Integer.valueOf(healthRecoderBeann
						.getVitamin()));
				getnextproteinsimgs(Integer.valueOf(healthRecoderBeann
						.getProtein()));
				getnextmineralsimgs(Integer.valueOf(healthRecoderBeann
						.getMineral()));
				getnextfatimgs(Integer.valueOf(healthRecoderBeann.getFat()));
				getnextsugarimgs(Integer.valueOf(healthRecoderBean.getSugar()));
				getnextenergysimgs(Integer.valueOf(healthRecoderBeann
						.getEnergy()));
			} catch (Exception e) {
				// TODO: handle exception
			}

		} else {
			successlay.setVisibility(View.GONE);
			failurelay.setVisibility(View.VISIBLE);
		}
	}

	private void sendChangeUI() {
		sendBroadcast(new Intent(ConstantS.ACTION_HIDE_UI_CHANGE));
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.loop_yuyin:
				showVoiceSearch();
				break;

			case R.id.jilufood:
				showShareFood();
				break;

			default:
				finish();
				break;
			}

		}
	};

	private void showVoiceSearch() {
		startActivity(new Intent(context, VoiceSearceActivity.class));
		finish();
	}

	private void showShareFood() {
		Intent intent = new Intent();
		intent.putExtra("samefood", samefood);
		intent.setClass(context, ShareFoodActivity.class);
		startActivity(intent);
	}

	
	
	private void saveScreen(final View graphicalView) {
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {
					graphicalView.setDrawingCacheEnabled(true);
					SavePic.saveRecoredPic2Example(Bitmap
							.createBitmap(graphicalView.getDrawingCache()));
					graphicalView.setDrawingCacheEnabled(false);
					ShareUtils
					.shareFood(context, getString(R.string.share_content_),
							Uri.fromFile(new File(FileUtils.HEALTH_IMAG, "recored"
									+ ".png")));
				} catch (Exception e) {
					// TODO: handle exception
					BingLog.e(TAG, "±£´æ´íÎó:" + e.getMessage());
				}

			}

		}.start();
	}
	
}
