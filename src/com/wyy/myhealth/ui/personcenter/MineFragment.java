package com.wyy.myhealth.ui.personcenter;

import org.json.JSONException;

import com.tencent.tauth.Tencent;
import com.umeng.message.PushAgent;
import com.umeng.message.proguard.C.e;
import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.db.utils.CollectDatabaseUtils;
import com.wyy.myhealth.db.utils.IceDadabaseUtils;
import com.wyy.myhealth.db.utils.MsgDatabaseUtils;
import com.wyy.myhealth.db.utils.PublicChatDatabaseUtils;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.imag.utils.LoadImageUtils;
import com.wyy.myhealth.ui.absfragment.FragmentInterface;
import com.wyy.myhealth.ui.absfragment.ListBaseFragment;
import com.wyy.myhealth.ui.collect.CollectActivity;
import com.wyy.myhealth.ui.healthbar.HealthPassActivity;
import com.wyy.myhealth.ui.login.LoginActivity;
import com.wyy.myhealth.ui.setting.SettingActivity;
import com.wyy.myhealth.ui.yaoyingyang.YaoyingyangFragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MineFragment extends Fragment implements FragmentInterface {

	private static final String TAG = MineFragment.class.getSimpleName();

	private ImageView userHeadImageView;

	private TextView userName;

	private TextView userWeima;

	private PersonalInfo personalInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initFilter();
	}

	public static MineFragment newInstance(int position) {
		MineFragment mineFragment = new MineFragment();
		mineFragment.setArguments(new Bundle());
		return mineFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_mine, container,
				false);
		initView(rootView);
		initData();
		return rootView;
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
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		if (getActivity() != null) {
			getActivity().unregisterReceiver(infoChangeReceiver);
		}
	}

	@Override
	public void initView(View rootView) {
		// TODO Auto-generated method stub
		userHeadImageView = (ImageView) rootView.findViewById(R.id.userhead);
		userName = (TextView) rootView.findViewById(R.id.username);
		userWeima = (TextView) rootView.findViewById(R.id.micro_code);
		rootView.findViewById(R.id.user_info_lay).setOnClickListener(listener);
		rootView.findViewById(R.id.health_bar_lay).setOnClickListener(listener);
		rootView.findViewById(R.id.hornor_lay).setOnClickListener(listener);
		rootView.findViewById(R.id.collect_lay).setOnClickListener(listener);
		rootView.findViewById(R.id.setting_lay).setOnClickListener(listener);
		rootView.findViewById(R.id.colse_lay).setOnClickListener(listener);
		rootView.findViewById(R.id.msg_lay).setOnClickListener(listener);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		if (personalInfo == null) {
			personalInfo = WyyApplication.getInfo();
		}

		if (personalInfo == null) {
			userName.setText(R.string.click_login);
		} else {
			LoadImageUtils.loadImage4ImageV(userHeadImageView,
					HealthHttpClient.IMAGE_URL + personalInfo.getHeadimage());
			userName.setText(personalInfo.getUsername());
			userWeima.setText("ЮЂТы:" + personalInfo.getIdcode());
		}

	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.user_info_lay:
				showBaseInfo();
				break;
			case R.id.health_bar_lay:
				showHealthPass();
				break;
			case R.id.hornor_lay:
				showHornor();
				break;
			case R.id.collect_lay:
				showCollect();
				break;
			case R.id.setting_lay:
				showSetting();
				break;
			case R.id.colse_lay:
				colseDialog();
				break;
			case R.id.msg_lay:
				showMsgList();
				break;
			default:
				break;
			}
		}
	};

	private void showBaseInfo() {
		startActivity(new Intent(getActivity(), PersonInfoActivity.class));
	}

	private void showHealthPass() {
		// healthNoticeView.setVisibility(View.GONE);
		// disCoverStateBean.setHasNewHps(false);
		// sendCanelNotice();
		startActivity(new Intent(getActivity(), HealthPassActivity.class));
	}

	private void showCollect() {
		startActivity(new Intent(getActivity(), CollectActivity.class));
	}

	private void showSetting() {
		startActivity(new Intent(getActivity(), SettingActivity.class));
	}

	private void showHornor() {
		startActivity(new Intent(getActivity(), PersonHornorActivity.class));
	}

	private void initFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConstantS.ACTION_BASE_INFO_CHANGE);
		getActivity().registerReceiver(infoChangeReceiver, filter);
	}

	private BroadcastReceiver infoChangeReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			reshInfo();
		}
	};

	private void reshInfo() {
		personalInfo = WyyApplication.getInfo();
		initData();
	}

	private void colseDialog() {
		final CharSequence[] items = { getString(R.string.loginout_),
				getString(R.string.exit) };
		AlertDialog dlg = new AlertDialog.Builder(getActivity()).setItems(
				items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						if (item == 1) {
							getActivity().finish();
						} else {
							showLoginOut();
						}
					}
				}).create();
		dlg.show();
	}

	private void showLoginOut() {
		UmenAnalyticsUtility.onEvent(WyyApplication.getInstance(),
				ConstantS.UMNEG_LOGIN_OUT);
		removeAlias();
		delDataBase();
		clearPreferences();
		startActivity(new Intent(getActivity(), LoginActivity.class));
		finshAll();
	}

	private void delDataBase() {
		new CollectDatabaseUtils(WyyApplication.getInstance()).deleteAll();
		new MsgDatabaseUtils(WyyApplication.getInstance()).deleteAll();
		new IceDadabaseUtils(WyyApplication.getInstance()).deleteAll();
		new PublicChatDatabaseUtils(WyyApplication.getInstance()).deleteAll();
	}

	private void clearPreferences() {

		WyyApplication
				.getInstance()
				.getSharedPreferences(ConstantS.USER_DATA, Context.MODE_PRIVATE)
				.edit().clear().commit();
		WyyApplication
				.getInstance()
				.getSharedPreferences(ConstantS.USER_PREFERENCES,
						Context.MODE_PRIVATE).edit().clear().commit();
		WyyApplication
				.getInstance()
				.getSharedPreferences(ListBaseFragment.class.getSimpleName(),
						Context.MODE_PRIVATE).edit().clear().commit();
		WyyApplication
				.getInstance()
				.getSharedPreferences(
						YaoyingyangFragment.class.getSimpleName(),
						Context.MODE_PRIVATE).edit().clear().commit();
	}

	private void finshAll() {
		WyyApplication.getInstance().sendBroadcast(
				new Intent(ConstantS.ACTION_MAIN_FINSH));
		loginoutQQ();
	}

	private void loginoutQQ() {
		Tencent mTencent = Tencent.createInstance(ConstantS.TENCENT_APP_ID,
				WyyApplication.getInstance());
		if (mTencent != null && mTencent.isSessionValid()) {
			try {
				mTencent.logout(WyyApplication.getInstance());
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	private void showMsgList() {
		startActivity(new Intent(getActivity(),
				com.wyy.myhealth.ui.message.MsgListActivity.class));
	}

	private void removeAlias() {
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				if (null != WyyApplication.getInfo()) {

					try {
						PushAgent.getInstance(getActivity()).removeAlias(
								WyyApplication.getInfo().getIdcode(),
								ConstantS.UMNEG_USER_TYPE);
					} catch (e e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}.start();
	}

}
