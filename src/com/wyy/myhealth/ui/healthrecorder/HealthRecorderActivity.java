package com.wyy.myhealth.ui.healthrecorder;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyy.myhealth.R;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.utils.ReshUserDataUtlis;
/**
 * @deprecated
 * @author lyl
 *
 */
public class HealthRecorderActivity extends BaseActivity implements
		ActivityInterface {

	private static final String TAG = HealthRecorderActivity.class
			.getSimpleName();

	private static final int TAB_LENGTH = 7;

	private LinearLayout[] recorderLay = new LinearLayout[TAB_LENGTH];

	private ImageView[] recorderimg = new ImageView[TAB_LENGTH];

	private TextView[] recordertxt = new TextView[TAB_LENGTH];

	private static final int[][] imageBg = new int[2][TAB_LENGTH];

	static {
		imageBg[0][0] = R.drawable.yinshiheli_icon;
		imageBg[1][0] = R.drawable.yinshiheli_icon_sec;
		imageBg[0][1] = R.drawable.yundongrel_icon;
		imageBg[1][1] = R.drawable.yundongrel_icon_sec;
		imageBg[0][2] = R.drawable.zhifang_icon;
		imageBg[1][2] = R.drawable.zhifang_icon_sec;
		imageBg[0][3] = R.drawable.tanglei_icon;
		imageBg[1][3] = R.drawable.tanglei_icon_sec;
		imageBg[0][4] = R.drawable.danbaizhi_icon;
		imageBg[1][4] = R.drawable.danbaizhi_icon_sec;
		imageBg[0][5] = R.drawable.weishengsu_icon;
		imageBg[1][5] = R.drawable.weishengsu_icon_sec;
		imageBg[0][6] = R.drawable.kuangwuzhi_icon;
		imageBg[1][6] = R.drawable.kuangwuzhi_icon_sec;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_recorder);
		initView();
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.health_recoder);
	}

	@Override
	protected void onInitFragment() {
		// TODO Auto-generated method stub
		super.onInitFragment();
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.health_fra_lay,
						RecorderChatFragment.newInstance(ConstantS.YINSHI))
				.commit();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		recorderLay[0] = (LinearLayout) findViewById(R.id.home0_yinshiheli_layout);
		recorderLay[1] = (LinearLayout) findViewById(R.id.home1_yundongreliang_layout);
		recorderLay[2] = (LinearLayout) findViewById(R.id.home2_zhifang_layout);
		recorderLay[3] = (LinearLayout) findViewById(R.id.home3_tanglei_layout);
		recorderLay[4] = (LinearLayout) findViewById(R.id.home4_danbaizhi_layout);
		recorderLay[5] = (LinearLayout) findViewById(R.id.home5_vshengsu_layout);
		recorderLay[6] = (LinearLayout) findViewById(R.id.home6_kuangwuzhi_layout);

		recorderimg[0] = (ImageView) findViewById(R.id.home0_yinshiheli_img);
		recorderimg[1] = (ImageView) findViewById(R.id.home1_yundongreliang_img);
		recorderimg[2] = (ImageView) findViewById(R.id.home2_zhifang_img);
		recorderimg[3] = (ImageView) findViewById(R.id.home3_tanglei_img);
		recorderimg[4] = (ImageView) findViewById(R.id.home4_danbaizhi_img);
		recorderimg[5] = (ImageView) findViewById(R.id.home5_vshengsu_img);
		recorderimg[6] = (ImageView) findViewById(R.id.home6_kuangwuzhi_img);

		recordertxt[0] = (TextView) findViewById(R.id.home0_yinshiheli_txt);
		recordertxt[1] = (TextView) findViewById(R.id.home1_yundongreliang_txt);
		recordertxt[2] = (TextView) findViewById(R.id.home2_zhifang_txt);
		recordertxt[3] = (TextView) findViewById(R.id.home3_tanglei_txt);
		recordertxt[4] = (TextView) findViewById(R.id.home4_danbaizhi_txt);
		recordertxt[5] = (TextView) findViewById(R.id.home5_vshengsu_txt);
		recordertxt[6] = (TextView) findViewById(R.id.home6_kuangwuzhi_txt);

		for (int i = 0; i < TAB_LENGTH; i++) {
			recorderLay[i].setOnClickListener(listener);
		}

		initData();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		showRecorderFragment(getPreferences());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.health_recorder, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.resh:
			ReshUserDataUtlis.reshUserRecorder(context);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.home0_yinshiheli_layout:
				showRecorderFragment(0);
				break;

			case R.id.home1_yundongreliang_layout:
				showRecorderFragment(1);
				break;

			case R.id.home2_zhifang_layout:
				showRecorderFragment(2);
				break;

			case R.id.home3_tanglei_layout:
				showRecorderFragment(3);
				break;

			case R.id.home4_danbaizhi_layout:
				showRecorderFragment(4);
				break;

			case R.id.home5_vshengsu_layout:
				showRecorderFragment(5);
				break;

			case R.id.home6_kuangwuzhi_layout:
				showRecorderFragment(6);
				break;

			default:
				break;
			}
		}
	};

	private void showRecorderFragment(int id) {

		for (int i = 0; i < TAB_LENGTH; i++) {
			if (i == id) {
				recorderLay[i].setBackgroundColor(Color.BLACK);
				recordertxt[i].setTextColor(Color.WHITE);
				recorderimg[i].setImageResource(imageBg[1][i]);
			} else {
				recorderLay[i].setBackgroundColor(Color.WHITE);
				recordertxt[i].setTextColor(Color.BLACK);
				recorderimg[i].setImageResource(imageBg[0][i]);
			}
		}

		swithRecorder(id);

	}

	private void swithRecorder(int id) {

		savePreferences(id);

		FragmentManager fragmentManager = getSupportFragmentManager();
		switch (id) {
		case 0:
			fragmentManager
					.beginTransaction()
					.replace(R.id.health_fra_lay,
							RecorderChatFragment.newInstance(ConstantS.YINSHI))
					.commit();
			break;

		case 1:

			fragmentManager
					.beginTransaction()
					.replace(R.id.health_fra_lay,
							RecorderChatFragment.newInstance(ConstantS.YUNDONG))
					.commit();

			break;

		case 2:

			fragmentManager
					.beginTransaction()
					.replace(R.id.health_fra_lay,
							RecorderChatFragment.newInstance(ConstantS.ZHIFANG))
					.commit();

			break;

		case 3:

			fragmentManager
					.beginTransaction()
					.replace(R.id.health_fra_lay,
							RecorderChatFragment.newInstance(ConstantS.TANGLEI))
					.commit();

			break;

		case 4:

			fragmentManager
					.beginTransaction()
					.replace(
							R.id.health_fra_lay,
							RecorderChatFragment
									.newInstance(ConstantS.DANGBAIZHI))
					.commit();

			break;

		case 5:

			fragmentManager
					.beginTransaction()
					.replace(
							R.id.health_fra_lay,
							RecorderChatFragment
									.newInstance(ConstantS.WEISHENGSU))
					.commit();

			break;

		case 6:

			fragmentManager
					.beginTransaction()
					.replace(
							R.id.health_fra_lay,
							RecorderChatFragment
									.newInstance(ConstantS.KUANGWUZHI))
					.commit();

			break;

		default:
			break;
		}
	}

	private void savePreferences(int id) {
		getSharedPreferences(ConstantS.USER_PREFERENCES, Context.MODE_PRIVATE)
				.edit().putInt(TAG, id).commit();
	}

	private int getPreferences() {
		return getSharedPreferences(ConstantS.USER_PREFERENCES,
				Context.MODE_PRIVATE).getInt(TAG, 0);
	}

}
