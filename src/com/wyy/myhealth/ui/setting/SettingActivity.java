package com.wyy.myhealth.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.tencent.tauth.Tencent;
import com.wyy.myhealth.R;
import com.wyy.myhealth.analytics.UmenAnalyticsUtility;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.baidu.utlis.TagUtils;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.db.utils.CollectDatabaseUtils;
import com.wyy.myhealth.db.utils.IceDadabaseUtils;
import com.wyy.myhealth.db.utils.MsgDatabaseUtils;
import com.wyy.myhealth.db.utils.PublicChatDatabaseUtils;
import com.wyy.myhealth.imag.utils.LoadImageUtils;
import com.wyy.myhealth.ui.absfragment.ListBaseFragment;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.ui.login.LoginActivity;
import com.wyy.myhealth.ui.yaoyingyang.YaoyingyangFragment;
import com.wyy.myhealth.utils.ShareUtils;

public class SettingActivity extends BaseActivity implements ActivityInterface {

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.action_settings);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initView();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		context = this;
		findViewById(R.id.feed_back_).setOnClickListener(listener);
		findViewById(R.id.app_version_).setOnClickListener(listener);
		findViewById(R.id.clear_coach_).setOnClickListener(listener);
		findViewById(R.id.login_out).setOnClickListener(listener);
		findViewById(R.id.function_intro).setOnClickListener(listener);
		findViewById(R.id.tell_lay).setOnClickListener(listener);
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
			case R.id.feed_back_:
				showFeedback();
				break;

			case R.id.app_version_:
				showVersion();
				break;

			case R.id.clear_coach_:
				LoadImageUtils.clear_Coach(context);
				break;

			case R.id.login_out:
				showLoginOut();
				break;

			case R.id.function_intro:
				showFuncIntro();
				break;

			case R.id.tell_lay:
				ShareUtils.share2Fre(context);
				break;
				
			default:
				break;
			}
		}
	};

	private void showFeedback() {
		startActivity(new Intent(context, FeedbackActivity.class));
	}

	private void showVersion() {
		startActivity(new Intent(context, AboutVersion.class));
	}

	private void showFuncIntro() {
		startActivity(new Intent(context, FunctionIntroActivity.class));
	}

	private void showLoginOut() {
		UmenAnalyticsUtility.onEvent(context, ConstantS.UMNEG_LOGIN_OUT);
		if (null!=WyyApplication.getInfo()) {
			TagUtils.delTag(WyyApplication.getInfo().getId(), context);
		}
		delDataBase();
		clearPreferences();
		startActivity(new Intent(context, LoginActivity.class));
		finshAll();
		finish();
	}

	private void delDataBase() {
		new CollectDatabaseUtils(context).deleteAll();
		new MsgDatabaseUtils(context).deleteAll();
		new IceDadabaseUtils(context).deleteAll();
		new PublicChatDatabaseUtils(context).deleteAll();
	}

	private void clearPreferences() {

		getSharedPreferences(ConstantS.USER_DATA, MODE_PRIVATE).edit().clear()
				.commit();
		getSharedPreferences(ConstantS.USER_PREFERENCES, MODE_PRIVATE).edit()
				.clear().commit();
		getSharedPreferences(ListBaseFragment.class.getSimpleName(),
				MODE_PRIVATE).edit().clear().commit();
		getSharedPreferences(YaoyingyangFragment.class.getSimpleName(),
				MODE_PRIVATE).edit().clear().commit();
	}

	private void finshAll() {
		sendBroadcast(new Intent(ConstantS.ACTION_MAIN_FINSH));
		loginoutQQ();
	}

	private void loginoutQQ() {
		Tencent mTencent = Tencent.createInstance(ConstantS.TENCENT_APP_ID,
				getApplicationContext());
		if (mTencent != null && mTencent.isSessionValid()) {
			try {
				mTencent.logout(context);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

}
