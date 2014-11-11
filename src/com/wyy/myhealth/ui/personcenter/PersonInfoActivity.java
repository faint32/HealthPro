package com.wyy.myhealth.ui.personcenter;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.imag.utils.LoadImageUtils;
import com.wyy.myhealth.imag.utils.PhotoUtils;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.ui.personcenter.modify.ModifyAgeActivity;
import com.wyy.myhealth.ui.personcenter.modify.ModifyBodyStateActivity;
import com.wyy.myhealth.ui.personcenter.modify.ModifyDialog;
import com.wyy.myhealth.ui.personcenter.modify.ModifyHeightActivity;
import com.wyy.myhealth.ui.personcenter.modify.ModifySummaryActivity;
import com.wyy.myhealth.ui.personcenter.modify.ModifyUserHeadActivity;
import com.wyy.myhealth.ui.personcenter.modify.ModifyUsernameActivity;
import com.wyy.myhealth.ui.personcenter.modify.ModifyWeightActivity;
import com.wyy.myhealth.ui.personcenter.modify.ModityCareerActivity;
import com.wyy.myhealth.ui.personcenter.modify.ShowBmiDialog;
import com.wyy.myhealth.ui.photoPager.PhotoPagerActivity;
import com.wyy.myhealth.utils.BingLog;

public class PersonInfoActivity extends BaseActivity implements
		ActivityInterface {
	private static final String TAG = PersonInfoActivity.class.getSimpleName();
	private ImageView userHeadImageView;

	private TextView userName;

	private TextView userAge;

	private TextView userWeima;

	private TextView userCareer;

	private TextView userHeight;

	private TextView userWeight;

	private TextView userState;

	private TextView userBmi;

	private TextView userRemarks;

	private TextView userSex;

	private PersonalInfo personalInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_info);
		initView();
		initData();
		initFilter();
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.base_info);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(infoChangeReceiver);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		userHeadImageView = (ImageView) findViewById(R.id.userhead);
		userName = (TextView) findViewById(R.id.username_txt);
		userAge = (TextView) findViewById(R.id.age_txt);
		userWeima = (TextView) findViewById(R.id.micro_code);

		userCareer = (TextView) findViewById(R.id.carre_txt);
		userHeight = (TextView) findViewById(R.id.height_txt);
		userWeight = (TextView) findViewById(R.id.weight_txt);
		userState = (TextView) findViewById(R.id.body_state_txt);
		userBmi = (TextView) findViewById(R.id.bmi_txt);
		userRemarks = (TextView) findViewById(R.id.body_remark_txt);
		userSex = (TextView) findViewById(R.id.sex_txt);
		findViewById(R.id.user_info_lay).setOnClickListener(listener);
		findViewById(R.id.userhead).setOnClickListener(listener);
		findViewById(R.id.niname_lay).setOnClickListener(listener);
		findViewById(R.id.sex_lay).setOnClickListener(listener);
		findViewById(R.id.age_lay).setOnClickListener(listener);
		findViewById(R.id.height_lay).setOnClickListener(listener);
		findViewById(R.id.bmi_lay).setOnClickListener(listener);
		findViewById(R.id.weight_lay).setOnClickListener(listener);
		findViewById(R.id.carre_lay).setOnClickListener(listener);
		findViewById(R.id.body_state_lay).setOnClickListener(listener);
		findViewById(R.id.body_remark_lay).setOnClickListener(listener);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		if (personalInfo == null) {
			personalInfo = WyyApplication.getInfo();
		}

		if (personalInfo == null) {
			return;
		}

		userName.setText(personalInfo.getUsername());
		userWeima.setText(personalInfo.getIdcode());
		userCareer.setText(personalInfo.getJob());
		userAge.setText(personalInfo.getAge());
		userHeight.setText(personalInfo.getHeight() + "cm");
		userWeight.setText(personalInfo.getWeight() + "kg");
		userState.setText(personalInfo.getBodyindex());
		userBmi.setText(""
				+ PersonUtils.getBmi(personalInfo.getHeight(),
						personalInfo.getWeight()));
		userRemarks.setText(personalInfo.getSummary());
		LoadImageUtils.loadImage4ImageV(userHeadImageView,
				HealthHttpClient.IMAGE_URL + personalInfo.getHeadimage());
		setSex();
	}

	private void setSex() {
		switch (WyyApplication.getInfo().getGender()) {
		case ConstantS.MAN:
			userSex.setText(R.string.boy);
			break;

		case ConstantS.WOMAN:
			userSex.setText(R.string.gilr);
			break;

		default:
			break;
		}
	}

	private void reshInfo() {
		personalInfo = WyyApplication.getInfo();
		initData();
	}

	private void initFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConstantS.ACTION_BASE_INFO_CHANGE);
		registerReceiver(infoChangeReceiver, filter);

	}

	private BroadcastReceiver infoChangeReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			BingLog.i(TAG, "个人中心" + intent.getAction());
			reshInfo();
		}
	};

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.userhead:
				showUserHead();
				break;
			case R.id.user_info_lay:
				PhotoUtils.secPic(context);
				break;

			case R.id.niname_lay:
				showModifyName();
				break;

			case R.id.sex_lay:
				showModifySex();
				break;

			case R.id.age_lay:
				showModifyAge();
				break;

			case R.id.height_lay:
				showModifyHeight();
				break;

			case R.id.weight_lay:
				showModifyWeight();
				break;

			case R.id.bmi_lay:
				ShowBmiDialog.showBmi(context);
				break;

			case R.id.carre_lay:
				showModifyCareer();
				break;

			case R.id.body_state_lay:
				showModifyBodyState();
				break;
			case R.id.body_remark_lay:
				showModifyRemark();
				break;

			default:
				break;
			}
		}
	};

	private void showUserHead() {
		List<String> list = new ArrayList<>();
		list.add(personalInfo.getHeadimage());
		Intent intent = new Intent();
		intent.putStringArrayListExtra("imgurls", (ArrayList<String>) list);
		intent.putExtra("postion", 0);
		intent.setClass(context, PhotoPagerActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != RESULT_OK) {
			return;
		}

		if (requestCode == 0) {
			try {

				final String str;
				Uri localUri = data.getData();
				String[] arrayOfString = new String[1];
				arrayOfString[0] = "_data";
				Cursor localCursor = getContentResolver().query(localUri,
						arrayOfString, null, null, null);
				if (localCursor == null)
					return;
				localCursor.moveToFirst();
				str = localCursor.getString(localCursor
						.getColumnIndex(arrayOfString[0]));
				localCursor.close();
				showModifyHead(str);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (requestCode == 1) {
			try {
				String path = PhotoUtils.getPicPathFromUri(
						PhotoUtils.imageFileUri, this);
				showModifyHead(path);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showModifyHead(String path) {
		Intent intent = new Intent();
		intent.putExtra("path", path);
		intent.setClass(context, ModifyUserHeadActivity.class);
		startActivity(intent);
	}

	private void showModifyName() {
		startActivity(new Intent(context, ModifyUsernameActivity.class));
	}

	private void showModifySex() {
		int index = 0;
		try {
			index = Integer.valueOf(WyyApplication.getInfo().getGender());
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (index == 0) {
			index = 1;
		} else {
			index = 0;
		}
		ModifyDialog.modifySex(context, index);
	}

	private void showModifyAge() {
		startActivity(new Intent(context, ModifyAgeActivity.class));
	}

	private void showModifyHeight() {
		startActivity(new Intent(context, ModifyHeightActivity.class));
	}

	private void showModifyWeight() {
		startActivity(new Intent(context, ModifyWeightActivity.class));
	}

	private void showModifyCareer() {
		startActivity(new Intent(context, ModityCareerActivity.class));
	}

	private void showModifyBodyState() {
		startActivity(new Intent(context, ModifyBodyStateActivity.class));
	}

	private void showModifyRemark() {
		startActivity(new Intent(context, ModifySummaryActivity.class));
	}

}
