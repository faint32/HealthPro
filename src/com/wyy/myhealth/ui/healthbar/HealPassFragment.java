package com.wyy.myhealth.ui.healthbar;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wyy.myhealth.R;
import com.wyy.myhealth.app.PreferencesFoodsInfo;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.MoodaFoodBean;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.file.utils.FileUtils;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.imag.utils.PhotoUtils;
import com.wyy.myhealth.ui.absfragment.HealthPassBase;
import com.wyy.myhealth.ui.absfragment.adapter.HealthAdapter2.ShaiItemOnclickListener;
import com.wyy.myhealth.ui.customview.BingListView.IXListViewListener;
import com.wyy.myhealth.ui.fooddetails.FoodDetailsActivity;
import com.wyy.myhealth.ui.mood.MoodDetailsActivity;
import com.wyy.myhealth.ui.photoPager.PhotoPagerActivity;
import com.wyy.myhealth.utils.BingDateUtils;
import com.wyy.myhealth.utils.BingLog;
import com.wyy.myhealth.welcome.WelcomeActivity;

public class HealPassFragment extends HealthPassBase implements
		OnRefreshListener, IXListViewListener, OnItemClickListener,
		ShaiItemOnclickListener {

	private ImageLoader imageLoader = ImageLoader.getInstance();

	private DisplayImageOptions options;

	private Bitmap headbg;

	private PersonalInfo personalInfo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.pic_preview)
				.showImageForEmptyUri(R.drawable.pic_preview)
				.showImageOnFail(R.drawable.pic_preview).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true).build();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	protected void onGetLastData() {
		// TODO Auto-generated method stub
		super.onGetLastData();
		json = getLast_Result();
		Log.i(TAG, "返回健康:" + json);
		if (TextUtils.isEmpty(json)) {
			reshShayiSai("0", limit);
		} else {
			try {
				reshParseJson(new JSONObject(json));
			} catch (Exception e) {
				// TODO: handle exception
			}

			reshShayiSai("0", limit);

		}
	}

	@Override
	protected void initView(View v) {
		// TODO Auto-generated method stub
		super.initView(v);
		mRefreshLayout.setOnRefreshListener(this);
		mListView.setXListViewListener(this);

		publishV.setOnClickListener(listener);
		bgImageView.setOnClickListener(listener);
		personalInfo=WyyApplication.getInfo();
		if (null==personalInfo) {
			WelcomeActivity.getPersonInfo(getActivity());
			personalInfo=WyyApplication.getInfo();
		}
		if (personalInfo!=null) {
			imageLoader.displayImage(HealthHttpClient.IMAGE_URL
					+ personalInfo.getHeadimage(), userhead, options);

			username.setText("" + personalInfo.getUsername());
		}
		
		headbg = PhotoUtils.getListHeadBg();
		if (headbg != null) {
			bgImageView.setImageBitmap(headbg);
		}

		mListView.setOnItemClickListener(this);
		// mAdapter.setOnClickListener(this);
		mAdapter2.setListener(this);

	}

	@Override
	protected void registerForContextMenu() {
		// TODO Auto-generated method stub
		super.registerForContextMenu();
		registerForContextMenu(mListView);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		getActivity().getMenuInflater().inflate(R.menu.hps_menu, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.shaiyisai:

			if (thList2.get(info.position - 1).getType() == ConstantS.TYPE_MOOD) {
				shaiMoodsshai(thList2.get(info.position - 1).getId());

			}

			if (thList2.get(info.position - 1).getType() == ConstantS.TYPE_FOOD) {
				shaiFoodsshai(thList2.get(info.position - 1).getId());
			}

			break;

		case R.id.delete:

			if (thList2.get(info.position - 1).getType() == ConstantS.TYPE_MOOD) {
				HealthHttpClient.doHttpdelMood(thList2.get(info.position - 1)
						.getId(), new DelAsyHander(info.position - 1));

			}

			if (thList2.get(info.position - 1).getType() == ConstantS.TYPE_FOOD) {
				HealthHttpClient.doHttpdelFoods(thList2.get(info.position - 1)
						.getId(), new DelAsyHander(info.position - 1));
			}

			break;

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void reshShayiSai(String first, String limit) {
		// TODO Auto-generated method stub
		super.reshShayiSai(first, limit);
		if (null == WyyApplication.getInfo().getId()) {
			return;
		}
		// HealthHttpClient.userFoodsAndMoods2(WyyApplication.getInfo().getId(),
		// first, limit, reshHandler);
		HealthHttpClient.userAired20(WyyApplication.getInfo().getId(), first,
				limit, reshHandler2);
	}

	@Override
	protected void getLoadMore(String first, String limit) {
		// TODO Auto-generated method stub
		super.getLoadMore(first, limit);
		// HealthHttpClient.userFoodsAndMoods2(WyyApplication.getInfo().getId(),
		// first, limit, parseHandler);
		HealthHttpClient.userAired20(WyyApplication.getInfo().getId(), first,
				limit, loadMoreHandler);
	}

	@Override
	protected void saveJsontoDb(String json, int postion) {
		// TODO Auto-generated method stub
		super.saveJsontoDb(json, postion);
		saveCurrent_Result(json);
	}

	private String getLast_Result() {
		String result = getActivity().getSharedPreferences(TAG,
				Context.MODE_PRIVATE).getString("result", "");
		return result;
	}

	/**
	 * 保存此次数据
	 * 
	 * @param result
	 */
	private void saveCurrent_Result(String result) {
		SharedPreferences preferences = getActivity().getSharedPreferences(TAG,
				Context.MODE_PRIVATE);

		Editor editor = preferences.edit();

		editor.putString("result", result);
		editor.commit();

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (!loadflag) {
			reshShayiSai("0", limit);
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (!loadflag) {
			getLoadMore("" + first, limit);
		}
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.take_pic:
				if (hps_no_content.isShown()) {
					hps_no_content.setVisibility(View.GONE);
					HealthPassActivity.setIsFristUse(getActivity(), false);
				}
				
				startActivity(new Intent(getActivity(),
						PublishMoodActivity.class));
				break;

			case R.id.user_bg:
				changeHeadBg();
				break;

			default:
				break;
			}
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		if (requestCode == 0) {
			try {

				final String str;
				Uri localUri = data.getData();
				String[] arrayOfString = new String[1];
				arrayOfString[0] = "_data";
				Cursor localCursor = getActivity().getContentResolver().query(
						localUri, arrayOfString, null, null, null);
				if (localCursor == null)
					return;
				localCursor.moveToFirst();
				str = localCursor.getString(localCursor
						.getColumnIndex(arrayOfString[0]));
				localCursor.close();
				headbg = PhotoUtils.getScaledBitmap(str, 600);

				// //把得到的图片绑定在控件上显示
				bgImageView.setImageBitmap(headbg);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (requestCode == 1) {
			try {

				Bundle extras = data.getExtras();
				headbg = (Bitmap) extras.get("data");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				headbg.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				bgImageView.setImageBitmap(headbg);// 把拍摄的照片转成圆角显示在预览控件上
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 把得到的图片绑定在控件上显示

		}

		saveCurrent_ResultBitmap(headbg);

	};

	// 图片上传选择途径
	private void changeHeadBg() {
		final CharSequence[] items = { getString(R.string.photo),
				getString(R.string.takepic) };
		AlertDialog dlg = new AlertDialog.Builder(getActivity())
				.setTitle(getString(R.string.changebg))
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						// 这里item是根据选择的方式，
						// 在items数组里面定义了两种方式，拍照的下标为1所以就调用拍照方法
						if (item == 1) {
							Intent getImageByCamera = new Intent(
									"android.media.action.IMAGE_CAPTURE");
							startActivityForResult(getImageByCamera, 1);
						} else {
							Intent getImage = new Intent(
									Intent.ACTION_GET_CONTENT);
							getImage.addCategory(Intent.CATEGORY_OPENABLE);
							getImage.setType("image/jpeg");
							startActivityForResult(getImage, 0);
						}
					}
				}).create();
		dlg.show();
	}

	/**
	 * 保存此次背景
	 * 
	 * @param result
	 */
	private void saveCurrent_ResultBitmap(Bitmap bitmap) {
		BingLog.i(TAG, "开始保存");
		File file = new File(FileUtils.HEALTH_IMAG, WyyApplication.getInfo()
				.getUsername() + "hps" + ".jpg");
		BufferedOutputStream bos;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BingLog.i(TAG, "保存成功");
	}

	private void shaiMoodsshai(String moodid) {
		HealthHttpClient.MoodShaiYIShai(moodid, shaiHandler);
	}

	private void shaiFoodsshai(String foodsid) {
		HealthHttpClient.FoodShaiYiShai(foodsid, shaiHandler);
	}

	public class DelAsyHander extends AsyncHttpResponseHandler {
		private int postion;

		public DelAsyHander(int postion) {
			this.postion = postion;
		}

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			thList2.remove(this.postion);
			mAdapter2.notifyDataSetChanged();
			Toast.makeText(getActivity(), R.string.delsuccess,
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);
			Toast.makeText(getActivity(), R.string.delfailure,
					Toast.LENGTH_LONG).show();
		}

	}

	private AsyncHttpResponseHandler shaiHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			parseResult(content);
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);
			Toast.makeText(getActivity(), R.string.publish_faliure,
					Toast.LENGTH_LONG).show();
		}

	};

	private void parseResult(String result) {
		try {
			JSONObject object = new JSONObject(result);
			String issuccess = object.getString("result");
			if ("1".equals(issuccess)) {
				Toast.makeText(getActivity(), R.string.publish_sucess,
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getActivity(), R.string.publish_faliure,
						Toast.LENGTH_LONG).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(getActivity(), R.string.publish_faliure,
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (thList2.get(position - 1).getType() == ConstantS.TYPE_FOOD) {
			PreferencesFoodsInfo.setfoodId(getActivity(),
					thList2.get(position - 1).getId());
			startActivity(new Intent(getActivity(), FoodDetailsActivity.class));
		} else if (thList2.get(position - 1).getType() == ConstantS.TYPE_MOOD) {
			try {
				showMoodDetails(thList2.get(position - 1));
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	@Override
	public void onPicClick(int listPostino, int picPostion) {
		// TODO Auto-generated method stub
		if (thList2.get(listPostino).getImg() != null) {
			List<String> list = (List<String>) thList2.get(listPostino)
					.getImg();
			Intent intent = new Intent();
			intent.putStringArrayListExtra("imgurls", (ArrayList<String>) list);
			intent.putExtra("postion", picPostion);
			intent.setClass(getActivity(), PhotoPagerActivity.class);
			startActivity(intent);
		}
	}

	@Override
	protected void arrangeDayMonth() {
		// TODO Auto-generated method stub
		super.arrangeDayMonth();
		int length = thList2.size();
		for (int i = 0; i < length; i++) {
			String createtime = thList2.get(i).getCreatetime();
			String day = "" + BingDateUtils.getDay(createtime);
			String month = BingDateUtils.getMonth(createtime) + "月";
			if (i == 0) {
				thList2.get(i).setDay(day);
				thList2.get(i).setMonth(month);
			} else {
				String lastcreatetime = thList2.get(i - 1).getCreatetime();
				String lastday = "" + BingDateUtils.getDay(lastcreatetime);
				String lastmonth = BingDateUtils.getMonth(lastcreatetime) + "月";
				if (lastday.equals(day) && lastmonth.equals(month)) {
					thList2.get(i).setDay(" ");
					thList2.get(i).setMonth("  ");
				} else {
					thList2.get(i).setDay(day);
					thList2.get(i).setMonth(month);
				}
			}
		}
	}

	private void showMoodDetails(MoodaFoodBean moodaFoodBean) {
		if (moodaFoodBean == null) {
			return;
		}
		Intent intent = new Intent();
		intent.setClass(getActivity(), MoodDetailsActivity.class);
		intent.putExtra("moodid", moodaFoodBean.getId());
		startActivity(intent);
	}

}
