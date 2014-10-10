package com.wyy.myhealth.ui.personcenter;

import java.util.ArrayList;
import java.util.List;

import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.imag.utils.Bmprcy;
import com.wyy.myhealth.imag.utils.PhotoUtils;
import com.wyy.myhealth.ui.personcenter.modify.ModifyBaseInfoActivity;
import com.wyy.myhealth.ui.personcenter.modify.ModifyBodyStateActivity;
import com.wyy.myhealth.ui.personcenter.modify.ModifySummaryActivity;
import com.wyy.myhealth.ui.personcenter.modify.Modify_h_w_activity;
import com.wyy.myhealth.ui.personcenter.modify.ModityCareerActivity;
import com.wyy.myhealth.utils.BingLog;
import com.wyy.myhealth.utils.ImageUtil;
import com.wyy.myhealth.welcome.WelcomeActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonCenterFragment extends Fragment implements
		OnPageChangeListener {

	private static final String TAG = PersonCenterFragment.class
			.getSimpleName();
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

	private ViewPager bodyPager;

	private TextView tabHonor;

	private TextView tabBaseInfo;

	private View honorView;

	private View baseInfoView;

	private PersonalInfo info;

	private List<View> centerViews = new ArrayList<View>();
	// 头像
	private Bitmap headBitmap = null;

	private ImageView headbg;

	private Bitmap bgBitmap;

	private TextView money;

	public static PersonCenterFragment newInstance(int position) {
		PersonCenterFragment personCenterFragment = new PersonCenterFragment();
		personCenterFragment.setArguments(new Bundle());
		return personCenterFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initFilter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_person_center,
				container, false);
		initView(rootView);
		initPageView(rootView);
		return rootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putParcelable("headimg", headBitmap);
		outState.putSerializable("info", info);
	}

	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UmenAnalyticsUtility.onPageStart(TAG);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		UmenAnalyticsUtility.onPageEnd(TAG);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		if (headBitmap != null) {
			userHeadImageView.setImageBitmap(headBitmap);
		}

		if (savedInstanceState != null) {
			headBitmap = savedInstanceState.getParcelable("headimg");
			if (headBitmap != null) {
				userHeadImageView.setImageBitmap(headBitmap);
			}
		}
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		getActivity().unregisterReceiver(infoChangeReceiver);
	}

	private void initView(View v) {
		userHeadImageView = (ImageView) v.findViewById(R.id.home_head_ima);
		userName = (TextView) v.findViewById(R.id.home_user_name);
		userAge = (TextView) v.findViewById(R.id.user_age);

		tabBaseInfo = (TextView) v.findViewById(R.id.my_base_info_tab);
		tabHonor = (TextView) v.findViewById(R.id.my_honor_tab);

		headbg = (ImageView) v.findViewById(R.id.per_bg);

		userHeadImageView.setOnClickListener(listener);
		tabBaseInfo.setOnClickListener(listener);
		tabHonor.setOnClickListener(listener);

	}

	private void initPageView(View v) {
		bodyPager = (ViewPager) v.findViewById(R.id.person_tab_pager);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		honorView = inflater.inflate(R.layout.my_honor_tab, bodyPager, false);
		baseInfoView = inflater.inflate(R.layout.my_base_info_tab, bodyPager,false);

		money = (TextView) honorView.findViewById(R.id.moeny);

		userWeima = (TextView) baseInfoView.findViewById(R.id.weima_id);
		userCareer = (TextView) baseInfoView.findViewById(R.id.user_carrer);
		userHeight = (TextView) baseInfoView.findViewById(R.id.body_h);
		userWeight = (TextView) baseInfoView.findViewById(R.id.body_w);
		userState = (TextView) baseInfoView.findViewById(R.id.body_state);
		userBmi = (TextView) baseInfoView.findViewById(R.id.body_bmi);
		userRemarks = (TextView) baseInfoView.findViewById(R.id.body_remark);

		baseInfoView.findViewById(R.id.weima_id).setOnClickListener(listener);
		baseInfoView.findViewById(R.id.carrer_layout).setOnClickListener(
				listener);
		baseInfoView.findViewById(R.id.body_h_w_layout).setOnClickListener(
				listener);
		baseInfoView.findViewById(R.id.body_state_layout).setOnClickListener(
				listener);
		baseInfoView.findViewById(R.id.body_bmi_layout).setOnClickListener(
				listener);
		baseInfoView.findViewById(R.id.body_remarks_layout).setOnClickListener(
				listener);

		if (centerViews.size() == 0) {
			centerViews.add(honorView);
			centerViews.add(baseInfoView);
		}else {
			centerViews.clear();
			centerViews.add(honorView);
			centerViews.add(baseInfoView);
		}

		bodyPager.setAdapter(new BingPagerAdapter(centerViews));

		bodyPager.setOnPageChangeListener(this);
		bodyPager.setCurrentItem(1);
		initPersonInfo();
	}

	private void initPersonInfo() {
		info = WyyApplication.getInfo();
		// if (info == null) {
		// info = (PersonalInfo) getArguments().getSerializable("info");
		// }

		if (info == null) {
			WelcomeActivity.getPersonInfo(getActivity());
			info = WyyApplication.getInfo();
		}

		if (info == null) {
			return;
		}

		userName.setText(WyyApplication.getInfo().getUsername() + "");
		userAge.setText(WyyApplication.getInfo().getAge() + "");
		userWeima.setText(WyyApplication.getInfo().getIdcode() + "");
		userCareer.setText(WyyApplication.getInfo().getJob() + "");
		userHeight.setText(WyyApplication.getInfo().getHeight() + "cm");
		userWeight.setText(WyyApplication.getInfo().getWeight() + "kg");
		userState.setText(WyyApplication.getInfo().getBodyindex() + "");
		userBmi.setText(""
				+ PersonUtils.getBmi(WyyApplication.getInfo().getHeight(),
						WyyApplication.getInfo().getWeight()));
		userRemarks.setText(WyyApplication.getInfo().getSummary() + "");
		money.setText(WyyApplication.getInfo().getMoney() + "");

		getperBg();

		setSex();

		if (!TextUtils.isEmpty(info.getHeadimage())) {

			if (WyyApplication.getHeaderImaList().size() > 0) {
				for (int i = 0; i < WyyApplication.getHeaderImaList().size(); i++) {
					if (WyyApplication.getHeaderImaList().get(i).getImaname()
							.equals(WyyApplication.getInfo().getUsername())) {
						headBitmap = BitmapFactory.decodeFile(WyyApplication
								.getHeaderImaList().get(i).getImapath());
						userHeadImageView.setImageBitmap(headBitmap);
						return;
					}
				}
			}

			if (null == headBitmap) {
				new Thread(loadImgRunnable).start();
			}

		}
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {

			case R.id.home_head_ima:
				startActivity(new Intent(getActivity(),
						ModifyBaseInfoActivity.class));
				break;

			case R.id.my_honor_tab:
				bodyPager.setCurrentItem(0);

				break;

			case R.id.my_base_info_tab:
				bodyPager.setCurrentItem(1);
				break;

			case R.id.carrer_layout:
				startActivity(new Intent(getActivity(),
						ModityCareerActivity.class));
				break;

			case R.id.body_h_w_layout:
				startActivity(new Intent(getActivity(),
						Modify_h_w_activity.class));
				break;

			case R.id.body_state_layout:
				startActivity(new Intent(getActivity(),
						ModifyBodyStateActivity.class));
				break;

			case R.id.body_remarks_layout:
				startActivity(new Intent(getActivity(),
						ModifySummaryActivity.class));
				break;

			default:
				break;
			}
		}
	};

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		switch (arg0) {
		case 0:
			tabHonor.setTextColor(Color.WHITE);
			tabHonor.setBackgroundResource(R.drawable.tab_left_b);

			tabBaseInfo.setTextColor(Color.BLACK);
			tabBaseInfo.setBackgroundResource(R.drawable.tab_right);
			break;

		case 1:
			tabHonor.setTextColor(Color.BLACK);
			tabHonor.setBackgroundResource(R.drawable.tab_left);

			tabBaseInfo.setTextColor(Color.WHITE);
			tabBaseInfo.setBackgroundResource(R.drawable.tab_right_b);

			break;

		default:
			break;
		}
	}

	private Runnable loadImgRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				String headimage = HealthHttpClient.IMAGE_URL
						+ info.getHeadimage();
				headBitmap = ImageUtil.getBitmapFromUrl(headimage);
				headBitmap = Bmprcy.toRoundBitmap(headBitmap);
				loadBitmap.sendEmptyMessage(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	private Handler loadBitmap = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (null != headBitmap) {
				try {
					userHeadImageView.setImageBitmap(headBitmap);
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

			return false;
		}
	});

	private void initFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConstantS.ACTION_BASE_INFO_CHANGE);
		getActivity().registerReceiver(infoChangeReceiver, filter);

	}

	private BroadcastReceiver infoChangeReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			BingLog.i(TAG, "个人中心" + intent.getAction());
			initPersonInfo();
		}
	};

	private void setSex() {
		switch (WyyApplication.getInfo().getGender()) {
		case ConstantS.MAN:
			Drawable drawable0 = getResources().getDrawable(R.drawable.sex_man);
			drawable0.setBounds(0, 0, drawable0.getMinimumWidth(),
					drawable0.getMinimumHeight());

			userAge.setCompoundDrawables(drawable0, null, null, null);
			break;

		case ConstantS.WOMAN:
			Drawable drawable = getResources()
					.getDrawable(R.drawable.sex_woman);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());

			userAge.setCompoundDrawables(drawable, null, null, null);

			break;

		default:
			break;
		}
	}

	private void getperBg() {
		if (bgBitmap == null) {
			bgBitmap = PhotoUtils.getListHeadBg();
			if (null != bgBitmap) {
				headbg.setImageBitmap(bgBitmap);
			}
		} else {
			headbg.setImageBitmap(bgBitmap);
		}
	}

}
