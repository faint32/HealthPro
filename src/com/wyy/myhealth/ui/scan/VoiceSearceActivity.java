package com.wyy.myhealth.ui.scan;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.speech.ErrorCode;
import com.iflytek.speech.ISpeechModule;
import com.iflytek.speech.InitListener;
import com.iflytek.speech.RecognizerListener;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConstant;
import com.iflytek.speech.SpeechRecognizer;
import com.iflytek.speech.SpeechUtility;
import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.HealthRecoderBean;
import com.wyy.myhealth.bean.NearFoodBean;
import com.wyy.myhealth.bean.ScanMoodBean;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.http.utils.JsonUtils;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;
import com.wyy.myhealth.ui.photoview.utils.Utility;
import com.wyy.myhealth.ui.scan.utils.JsonParser;
import com.wyy.myhealth.utils.BingLog;

public class VoiceSearceActivity extends BaseActivity implements
		ActivityInterface {
	private static final String TAG = VoiceSearceActivity.class.getSimpleName();

	private boolean hasengine = false;

	private SpeechRecognizer speechRecognizer;

	private EditText inputEditText;

	private View submintView;

	private FrameLayout wrapper;

	private ImageView voiceView;

	private NearFoodBean samefood;

	private RotateAnimation voiceReAnimation;

	private boolean isRecognizer = false;

	private TextView waitNotice;

	private ScanMoodBean scanMoodBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice_search);
		initView();
	}

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.voice_search);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.voice, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.submit_recongise:
			submitKeyWord();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		sendChangeUI();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		context = this;
		wrapper = (FrameLayout) findViewById(R.id.wrapper);
		inputEditText = (EditText) findViewById(R.id.input_food_edit);
		submintView = getLayoutInflater().inflate(R.layout.submit_layout, null);
		voiceView = (ImageView) findViewById(R.id.voice);
		waitNotice = (TextView) findViewById(R.id.voice_notice);
		voiceView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startVoiceRecognise();
			}
		});
		initData();
		sendChangeUI();

		voiceReAnimation = new RotateAnimation(0, 359,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		LinearInterpolator linearInterpolator = new LinearInterpolator();
		voiceReAnimation.setInterpolator(linearInterpolator);
		voiceReAnimation.setDuration(1000);

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

		if (checkSpeechServiceInstall()) {
			SpeechUtility.getUtility(context).setAppid("53848fa3");
			speechRecognizer = new SpeechRecognizer(this, mInitListener);
			hasengine = true;
		}

	}

	private void startVoiceRecognise() {
		if (!hasengine) {
			Toast.makeText(context, R.string.voiceengineerro, Toast.LENGTH_LONG)
					.show();
			return;
		}

		if (!Utility.isConnected(context)) {
			Toast.makeText(context, R.string.neterro, Toast.LENGTH_LONG).show();
			return;
		}
		voiceView.setImageResource(R.drawable.voice_re_press);
		setParam();
		speechRecognizer.startListening(mRecognizerListener);
	}

	// 判断手机中是否安装了讯飞语音+
	private boolean checkSpeechServiceInstall() {
		String packageName = "com.iflytek.speechcloud";
		List<PackageInfo> packages = getPackageManager()
				.getInstalledPackages(0);
		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			if (packageInfo.packageName.equals(packageName)) {
				return true;
			} else {
				continue;
			}
		}
		return false;
	}

	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(ISpeechModule module, int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code == ErrorCode.SUCCESS) {
				Toast.makeText(context, "初始化引擎成功", Toast.LENGTH_SHORT).show();
			}
		}
	};

	private RecognizerListener mRecognizerListener = new RecognizerListener.Stub() {

		@Override
		public void onVolumeChanged(int v) throws RemoteException {

		}

		@Override
		public void onResult(final RecognizerResult result, boolean isLast)
				throws RemoteException {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (null != result) {

						BingLog.d(TAG,
								"recognizer result" + result.getResultString());
						String iattext = JsonParser.parseIatResult(result
								.getResultString());
						iattext = iattext.replaceAll("。", "");
						String text = inputEditText.getText().toString()
								+ iattext;
						inputEditText.setText(text);
						speechRecognizer.stopListening(mRecognizerListener);
						stopAnimation();
					} else {
						Log.d(TAG, "recognizer result : null");
					}
				}
			});

		}

		@Override
		public void onError(int errorCode) throws RemoteException {
			endHandler.sendEmptyMessage(1);
		}

		@Override
		public void onEndOfSpeech() throws RemoteException {
			endHandler.sendEmptyMessage(0);
		}

		@Override
		public void onBeginOfSpeech() throws RemoteException {

		}
	};

	public void setParam() {

		speechRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		speechRecognizer.setParameter(SpeechConstant.VAD_BOS, "4000");
		speechRecognizer.setParameter(SpeechConstant.VAD_EOS, "1000");

		String param = null;
		param = "asr_ptt=" + "1";
		speechRecognizer.setParameter(SpeechConstant.PARAMS, param
				+ ",asr_audio_path=/sdcard/iflytek/wavaudio.pcm");

	}

	private Handler endHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 0:
				startAnimation();
				break;

			case 1:
				stopAnimation();
				break;

			default:
				break;
			}

		};
	};

	private void submitKeyWord() {
		inputEditText.setError(null);
		if (TextUtils.isEmpty(inputEditText.getText().toString())) {
			inputEditText.setError(getString(R.string.nullcontentnotice));
			return;
		}

		// HealthHttpClient.cmpFoodWords(inputEditText.getText().toString(),
		// mhHandler);
		HealthHttpClient.cmpFoodWords(WyyApplication.getInfo().getId(),
				inputEditText.getText().toString(), mhHandler);

	}

	private AsyncHttpResponseHandler mhHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			wrapper.addView(submintView);
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			super.onFinish();
			wrapper.removeView(submintView);
		}

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			BingLog.i(TAG, "搜寻结果:" + content);
			parseResult(content);
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);
			Toast.makeText(context, R.string.neterro, Toast.LENGTH_LONG).show();
		}

	};

	private void parseResult(String content) {
		try {
			JSONObject mJsonObject = new JSONObject(content);
			String result = mJsonObject.getString("result");
			if ("1".equals(result)) {
				JSONArray comments = mJsonObject.getJSONArray("comments");

				try {
					getSameFood(mJsonObject.getJSONObject("samefood"));
				} catch (Exception e) {
					// TODO: handle exception
				}

				try {
					getFaceBean(mJsonObject.getJSONObject("face"));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				if (comments != null && comments.length() > 0) {

					HealthRecoderBean healthRecoderBean = JsonUtils
							.getHealthRecoder(comments.getJSONObject(0));

					logindialog(healthRecoderBean);
				} else {
					Toast.makeText(context, R.string.noresult,
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(context, R.string.noresult, Toast.LENGTH_LONG)
						.show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(context, R.string.noresult, Toast.LENGTH_LONG)
					.show();
		}
	}

	public void logindialog(HealthRecoderBean healthRecoderBean) {
		Intent intent = new Intent();
		intent.putExtra("samefood", samefood);
		intent.putExtra("foods", healthRecoderBean);
		intent.putExtra("face", scanMoodBean);
		intent.setClass(context, ScanResultActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();

	}

	private void sendChangeUI() {
		sendBroadcast(new Intent(ConstantS.ACTION_HIDE_UI_CHANGE));
	}

	private void getSameFood(JSONObject jsonObject) {
		samefood = JsonUtils.getNearFoodBean(jsonObject);
	}

	private void getFaceBean(JSONObject jsonObject) {
		scanMoodBean = JsonUtils.getScanMoodBean(jsonObject);
	}

	private void startAnimation() {
		isRecognizer = true;
		voiceView.setImageResource(R.drawable.voice_re_loding);
		waitNotice.setVisibility(View.VISIBLE);
		voiceReAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				if (isRecognizer) {
					voiceView.startAnimation(voiceReAnimation);
				} else {
					voiceView.setImageResource(R.drawable.voice_re);
					waitNotice.setVisibility(View.GONE);
				}

			}
		});
		voiceView.startAnimation(voiceReAnimation);
	}

	private void stopAnimation() {
		isRecognizer = false;
	}

}
