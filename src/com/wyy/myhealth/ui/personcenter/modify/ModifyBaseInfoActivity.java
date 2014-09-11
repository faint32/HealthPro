package com.wyy.myhealth.ui.personcenter.modify;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.imag.utils.PhoneUtlis;
import com.wyy.myhealth.imag.utils.PhotoUtils;
import com.wyy.myhealth.ui.baseactivity.SubmitActivity;
import com.wyy.myhealth.ui.personcenter.modify.utils.AgeUtils;

public class ModifyBaseInfoActivity extends SubmitActivity implements
		OnCheckedChangeListener {

	private PersonalInfo info;

	private EditText usernamEditText;

	private EditText userageEditText;

	private ImageView userheadView;

	private RadioGroup mRadioGroup;

	private String headimage;

	private String headstr = "";

	private String gender = "";

	private String username = "";

	private String userage = "";

	private ImageLoader imageLoader = ImageLoader.getInstance();

	private DisplayImageOptions options;

	private Bitmap myHead;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_base_info);
		initView();
	}

	private void initView() {
		context = this;
		this.options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.pic_preview)
				.showImageForEmptyUri(R.drawable.pic_preview)
				.showImageOnFail(R.drawable.pic_preview).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true).build();

		userheadView = (ImageView) findViewById(R.id.user_head);
		usernamEditText = (EditText) findViewById(R.id.niname_edit);
		userageEditText = (EditText) findViewById(R.id.age_edit);

		mRadioGroup = (RadioGroup) findViewById(R.id.Sex_RadioGroup);

		mRadioGroup.setOnCheckedChangeListener(this);

		userheadView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PhotoUtils.secPic(context);
			}
		});

		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		info = WyyApplication.getInfo();
		if (info == null) {
			return;
		}
		headimage = HealthHttpClient.IMAGE_URL + info.getHeadimage();
		gender = info.getGender();
		userage = info.getAge();
		username = info.getUsername();
		switch (gender) {
		case ConstantS.MAN:
			mRadioGroup.check(R.id.man_radio);
			break;

		case ConstantS.WOMAN:
			mRadioGroup.check(R.id.woman_radio);
			break;
		default:
			break;
		}

		imageLoader.displayImage(headimage, userheadView, options);
		usernamEditText.setText("" + username);
		userageEditText.setText("" + userage);

	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.edit_file);
	}

	@Override
	protected void submitMsg() {
		// TODO Auto-generated method stub
		usernamEditText.setError(null);
		userageEditText.setError(null);

		username = usernamEditText.getText().toString();
		userage = userageEditText.getText().toString();
		if (TextUtils.isEmpty(username)) {
			usernamEditText.setError(getString(R.string.nichengnotice));
			usernamEditText.requestFocus();
			return;
		}
		if (TextUtils.isEmpty(userage)) {
			userageEditText.setError(getString(R.string.agenullnotice));
			userageEditText.requestFocus();
			return;
		} else if (!AgeUtils.isAge(userage)) {
			userageEditText.setError(getString(R.string.agenotice));
			userageEditText.requestFocus();
			return;
		}
		try {
			headstr = PhoneUtlis.bitmapToString(myHead);
		} catch (Exception e) {
			// TODO: handle exception
		}

		sendModify();
		// new Thread(sendModifyRunnable).start();

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.man_radio:
			gender = ConstantS.MAN;
			break;

		case R.id.woman_radio:
			gender = ConstantS.WOMAN;
			break;

		default:
			break;
		}

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

				myHead = PhoneUtlis.getNoCutSmallBitmap(str);

				userheadView.setImageBitmap(myHead);
				headimage = str;
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (requestCode == 1) {
			try {
				String path = PhotoUtils.getPicPathFromUri(
						PhotoUtils.imageFileUri, this);
				myHead = PhoneUtlis.getNoCutSmallBitmap(path);
				userheadView.setImageBitmap(myHead);
				headimage = path;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@SuppressWarnings("unused")
	private Runnable sendModifyRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				HealthHttpClient
						.doHttpFinishPersonInfoForName(info, username, userage,
								gender, headstr, new ModifyPicHandler(context));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Log.e(ModifyBaseInfoActivity.class.getSimpleName(), "发送出错");
			}
		}
	};

	private void sendModify() {
		try {
			HealthHttpClient.doHttpFinishPersonInfoForName(info, username,
					userage, gender, headstr, new ModifyPicHandler(context));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.e(ModifyBaseInfoActivity.class.getSimpleName(), "发送出错");
		}
	}

}
