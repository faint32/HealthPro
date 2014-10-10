package com.wyy.myhealth.ui.icebox;






import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.imag.utils.PhoneUtlis;
import com.wyy.myhealth.imag.utils.PhotoUtils;
import com.wyy.myhealth.ui.baseactivity.SubmitActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.ui.icebox.utils.FoodTypeUtils;

public class IceBoxAddFood extends SubmitActivity implements ActivityInterface {

	private ImageView foodpic;

	private EditText content;

	private String contentString;

	private int foodtype = 1;

	private int numday = 0;

	private Bitmap icebitmap;

	private TextView foodtypetxt;

	private TextView foodday;

	private String foodpicstr = "";
	
	private ProgressBar waitProgressBar;

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		waitProgressBar=new ProgressBar(context);
		getSupportActionBar().setCustomView(waitProgressBar, new ActionBar.LayoutParams(
				Gravity.RIGHT));
		getSupportActionBar().setTitle(R.string.ice_add);
		waitProgressBar.setVisibility(View.GONE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ice_add_food);
		initView();
	}

	@Override
	protected void submitMsg() {
		// TODO Auto-generated method stub
		content.setError(null);
		contentString = content.getText().toString();
		if (TextUtils.isEmpty(contentString)) {
			content.setError(getString(R.string.nullcontentnotice));
			return;
		}

		if (TextUtils.isEmpty(foodpicstr)) {
			Toast.makeText(context, R.string.empty_img_notice,
					Toast.LENGTH_SHORT).show();
			return;
		}

		postMyFood();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		content = (EditText) findViewById(R.id.share_content);
		content.setHint(R.string.input_name_hint);
		foodpic = (ImageView) findViewById(R.id.food_pic);
		foodtypetxt = (TextView) findViewById(R.id.food_type);
		foodday = (TextView) findViewById(R.id.food_numday);
		findViewById(R.id.ice_type_fra).setOnClickListener(listener);
		findViewById(R.id.ice_numday_fra).setOnClickListener(listener);
		foodpic.setOnClickListener(listener);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

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
	
	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.ice_type_fra:
				showSecType();
				break;

			case R.id.ice_numday_fra:
				showInputGuday();
				break;

			case R.id.food_pic:
				PhotoUtils.secPic(context);
				break;

			default:
				break;
			}

		}
	};

	private void showSecType() {
		Intent intent = new Intent();
		intent.putExtra("type", foodtype);
		intent.setClass(context, FoodTypeActivity.class);
		startActivityForResult(intent, 2);

	}

	private void showInputGuday() {
		Intent intent = new Intent();
		intent.putExtra("numday", numday);
		intent.setClass(context, IceFoodGarDayActivity.class);
		startActivityForResult(intent, 3);

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);

		if (arg1 != RESULT_OK) {
			return;
		}

		if (arg0 == 0) {
			final String str;
			Uri localUri = arg2.getData();
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
			icebitmap = PhotoUtils.getScaledBitmap(str, 600);
			foodpic.setImageBitmap(icebitmap);
			waitProgressBar.setVisibility(View.VISIBLE);
			new Thread(swithbmp2str).start();
		} else if (arg0 == 1) {
			try {
				String path = PhotoUtils.getPicPathFromUri(
						PhotoUtils.imageFileUri, this);
				icebitmap = PhotoUtils.getScaledBitmap(path, 600);
				foodpic.setImageBitmap(icebitmap);
				waitProgressBar.setVisibility(View.VISIBLE);
				new Thread(swithbmp2str).start();
//				Bundle extras = arg2.getExtras();
//				icebitmap = (Bitmap) extras.get("data");
//				ByteArrayOutputStream baos = new ByteArrayOutputStream();
//				icebitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//				foodpic.setImageBitmap(icebitmap);// 把拍摄的照片转成圆角显示在预览控件上
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (arg0 == 2) {

			Bundle extras = arg2.getExtras();
			foodtype = extras.getInt("type");
			foodtypetxt.setText(FoodTypeUtils.getfoodtype(foodtype));

		} else if (arg0 == 3) {
			Bundle extras = arg2.getExtras();
			numday = extras.getInt("numday");
			foodday.setText(numday + "天");
		}

	}

	private void postMyFood() {
		HealthHttpClient.addFood2IceBox(WyyApplication.getInfo().getId(),
				contentString, "" + numday, foodpicstr, "" + foodtype,
				new IceHttpHander(context));
	}

	private Runnable swithbmp2str = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			foodpicstr = PhoneUtlis.bitmapToString(icebitmap);
			getFoodpic.sendEmptyMessage(0);
		}
	};

	
	private Handler getFoodpic=new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			waitProgressBar.setVisibility(View.GONE);
			return false;
		}
	}) ;
	
}
