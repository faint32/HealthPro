package com.wyy.myhealth.ui.scan;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.wyy.myhealth.MainActivity;
import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.NearFoodBean;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.file.utils.FileUtils;
import com.wyy.myhealth.file.utils.SdUtils;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.imag.utils.PhoneUtlis;
import com.wyy.myhealth.ui.baseactivity.SubmitActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.utils.BingLog;
import com.wyy.myhealth.utils.NoticeUtils;

public class ShareFoodActivity extends SubmitActivity implements
		ActivityInterface {

	private static final String TAG = ShareFoodActivity.class.getSimpleName();

	private RatingBar tastRating;

	private String tasteStr;

	private TextView tags;

	private String tagString="";

	private TextView commercainame;

	private String commercainamestr;

	private TextView commercaitel;

	private String commercaitelstr;

	private TextView place;

	private String placeString;

	private ImageView foodpic;

	private EditText content;

	private String contentString;

	private CheckBox shaiCheck;

	private boolean ishai = true;

	private NearFoodBean samefood;

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.share);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_food);
		initView();
	}

	@Override
	protected void submitMsg() {
		// TODO Auto-generated method stub
		shareFood();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		context = this;
		tags = (TextView) findViewById(R.id.food_tag);
		content = (EditText) findViewById(R.id.share_content);
		foodpic = (ImageView) findViewById(R.id.food_pic);
		tastRating = (RatingBar) findViewById(R.id.taste_index);
		commercainame = (TextView) findViewById(R.id.food_commercial_name);
		commercaitel = (TextView) findViewById(R.id.food_commercial_tel);
		place = (TextView) findViewById(R.id.food_place);
		shaiCheck = (CheckBox) findViewById(R.id.isshaiyishai);
		shaiCheck.setOnCheckedChangeListener(checkedChangeListener);

		findViewById(R.id.food_commercial_fra).setOnClickListener(listener);
		findViewById(R.id.food_place_fra).setOnClickListener(listener);
		findViewById(R.id.food_tag_fra).setOnClickListener(listener);
		tastRating.setOnRatingBarChangeListener(ratingBarChangeListener);

		initData();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

		Bitmap mBitmap = null;
		if (SdUtils.ExistSDCard()) {
			mBitmap = PhoneUtlis.getSmall8ZoomBitmap(FileUtils.PIC_PATH);
		} else {
			mBitmap = PhoneUtlis.getBitmap(context);
		}
		if (mBitmap != null) {
			foodpic.setImageBitmap(mBitmap);
		}

		place.setText("" + MainActivity.address);

		samefood = (NearFoodBean) getIntent().getSerializableExtra("samefood");
		if (samefood == null) {
			return;
		}

		tasteStr = samefood.getTastelevel();
		try {
			tastRating.setRating(Float.valueOf(tasteStr));
		} catch (Exception e) {
			// TODO: handle exception

		}

		// tags.setText(""+samefood.getTags());

	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.food_tag_fra:
				showinputTag();
				break;

			case R.id.food_commercial_fra:
				showinputCommerical();
				break;

			case R.id.food_place_fra:

				showinputAddress();
				break;

			default:
				break;
			}
		}
	};

	private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			ishai = isChecked;
		}
	};

	private void showinputTag() {
		Intent intent = new Intent();
		tagString = tags.getText().toString();
		intent.putExtra("tag", tagString);
		intent.setClass(context, GetfoodTagActivity.class);
		startActivityForResult(intent, 0);

	}

	private void showinputAddress() {
		Intent intent = new Intent();
		placeString = place.getText().toString();
		intent.putExtra("address", placeString);
		intent.setClass(context, GetFoodAddress.class);
		startActivityForResult(intent, 1);

	}

	private void showinputCommerical() {
		Intent intent = new Intent();
		commercainamestr = commercainame.getText().toString();
		commercaitelstr = commercaitel.getText().toString();
		intent.putExtra("name", commercainamestr);
		intent.putExtra("tel", commercaitelstr);
		intent.setClass(context, GetCommercial.class);
		startActivityForResult(intent, 2);

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);

		if (arg1 != RESULT_OK) {
			return;
		}

		if (arg0 == 0) {
			Bundle extras = arg2.getExtras();
			String tag = extras.getString("tag");
			tagString = tag;
			tags.setText("" + tagString);

		} else if (arg0 == 1) {

			Bundle extras = arg2.getExtras();
			String address = extras.getString("address");
			placeString = address;
			place.setText("" + placeString);

		} else if (arg0 == 2) {
			Bundle extras = arg2.getExtras();
			commercainamestr = extras.getString("name");
			commercaitelstr = extras.getString("tel");
			commercainame.setText("" + commercainamestr);
			commercaitel.setText("" + commercaitelstr);
		}

	}

	private OnRatingBarChangeListener ratingBarChangeListener = new OnRatingBarChangeListener() {

		@Override
		public void onRatingChanged(RatingBar ratingBar, float rating,
				boolean fromUser) {
			// TODO Auto-generated method stub
			try {
				tasteStr = "" + (int) rating;
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	};

	private void shareFood() {
		contentString = content.getText().toString();
		placeString = place.getText().toString();
		String foodpicStr = "";
		if (TextUtils.isEmpty(tagString)) {
			tagString="";
		}
		foodpicStr = PhoneUtlis.bitmap_Small_ZoomToString(FileUtils.PIC_PATH);
		HealthHttpClient.doHttpPostFoods(WyyApplication.getInfo().getId(),
				foodpicStr, tagString, contentString, tasteStr,
				commercainamestr, commercaitelstr, placeString, ""
						+ MainActivity.Wlatitude, "" + MainActivity.Wlongitude,
				shareHandler);
	}

	private AsyncHttpResponseHandler shareHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			NoticeUtils.notice(context, getString(R.string.jiluyingyang),
					ConstantS.PUBLISH_FOOD_ID);
			finish();
		}

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			parseJson(content);
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);
			NoticeUtils.removeNotice(ConstantS.PUBLISH_FOOD_ID, context);
			NoticeUtils.showFailePublish(context);
		}

	};

	private void parseJson(String content) {
		BingLog.i(TAG, "·µ»Ø:" + content);
		try {
			JSONObject jsonObject = new JSONObject(content);
			int result = 0;
			result = jsonObject.getInt(ConstantS.RESULT);
			if (result == 1) {
				String foodid = jsonObject.getJSONObject("foods").getString(
						"id");
				if (ishai) {
					submitShai(foodid);
				} else {
					NoticeUtils
							.removeNotice(ConstantS.PUBLISH_FOOD_ID, context);
					NoticeUtils.showSuccessfulNotification(context);
				}
			} else {
				NoticeUtils.removeNotice(ConstantS.PUBLISH_FOOD_ID, context);
				NoticeUtils.showFailePublish(context);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			NoticeUtils.removeNotice(ConstantS.PUBLISH_FOOD_ID, context);
			NoticeUtils.showFailePublish(context);
		}
	}

	private void submitShai(String foodid) {
		HealthHttpClient.FoodShaiYiShai(foodid, shaiHandler);
	}

	private AsyncHttpResponseHandler shaiHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			NoticeUtils.removeNotice(ConstantS.PUBLISH_FOOD_ID, context);
			NoticeUtils.showSuccessfulNotification(context);
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);
			NoticeUtils.removeNotice(ConstantS.PUBLISH_FOOD_ID, context);
			NoticeUtils.showFailePublish(context);
		}

	};

}
