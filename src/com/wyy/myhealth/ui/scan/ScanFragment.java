package com.wyy.myhealth.ui.scan;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mpi.cbg.fly.Feature;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.HealthRecoderBean;
import com.wyy.myhealth.bean.NearFoodBean;
import com.wyy.myhealth.bean.ScanMoodBean;
import com.wyy.myhealth.config.Config;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.file.utils.FileUtils;
import com.wyy.myhealth.file.utils.SdUtils;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.http.utils.JsonUtils;
import com.wyy.myhealth.imag.utils.PhoneUtlis;
import com.wyy.myhealth.imag.utils.PhotoUtils;
import com.wyy.myhealth.imag.utils.SavePic;
import com.wyy.myhealth.support.bitmap.BitmapRatioUtils;
import com.wyy.myhealth.support.picfeure.Align;
import com.wyy.myhealth.ui.photoview.utils.Utility;
import com.wyy.myhealth.utils.BingLog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.SurfaceHolder.Callback;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ScanFragment extends Fragment {

	private static final String TAG = ScanFragment.class.getSimpleName();
	private static final int SPLASH_DELAY_MILLIS = 1000;
	private Camera mCamera;
	private Camera.Parameters parameters;// 照相机参数集
	private FrameLayout mFrameLayout;
	private Activity context;
	public static int angle = 0;// 图像旋转角度
	private int cameraID;
	private boolean threadflag = false;
	private boolean takpic = false;

	private boolean isfit = false;
	// 计算完成标志
	private boolean iscomple = false;
	// 阈值计算是否完成
	private boolean isplace = false;
	// 上传返回完成标志
	private boolean isfasongcm = true;

	private boolean isplacefit = false;

	private boolean voiceflage = false;

	private int count = 0;// 识别计时

	private boolean foucs = false;

	public ImageView saoImageView;
	// 发挥json数据
	@SuppressWarnings("unused")
	private String json = "";

	private cameraView cView;

	private boolean isSurface = false;

	private ScanView scanView;

	private ImageView takepic;

	private ImageView openlight;

	private ImageView voiceSearch;

	private FrameLayout bottomLayout;

	private TextView scantTextView;

	private NearFoodBean sameNearFoodBean;
	// 特征点
	private int feture = 0;
	// 阈值
	private double cw = 0;
	
	private ScanMoodBean scanMoodBean;

	public static ScanFragment newInstance(int postion) {
		ScanFragment scanFragment = new ScanFragment();
		scanFragment.setArguments(new Bundle());
		return scanFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		BingLog.i(TAG, "======onCreateView======");

		initFilter();
		View rootView = inflater.inflate(R.layout.scan_lay, container, false);
		initView(rootView);
		return rootView;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		BingLog.i(TAG, "======onCreate======");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		BingLog.i(TAG, "======onDestroy======");
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		BingLog.i(TAG, "======onDetach======");
		getActivity().unregisterReceiver(pageIndexReceiver);
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		BingLog.i(TAG, "======onLowMemory======");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		BingLog.i(TAG, "======onPause======");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		BingLog.i(TAG, "======onResume======");
		takepic.setEnabled(true);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		BingLog.i(TAG, "======onSaveInstanceState======");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		BingLog.i(TAG, "======onStart======");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		BingLog.i(TAG, "======onActivityCreated======");
		context = getActivity();
		if (count == 0) {
			initCameraView();
			count = 1;
		}

	}

	private void initView(View v) {
		saoImageView = (ImageView) v.findViewById(R.id.saomiao_k_img);
		mFrameLayout = (FrameLayout) v.findViewById(R.id.camera_view);

		takepic = (ImageView) v.findViewById(R.id.take_pic);
		openlight = (ImageView) v.findViewById(R.id.open_ligth);
		voiceSearch = (ImageView) v.findViewById(R.id.loop_yuyin);
		scanView = (ScanView) v.findViewById(R.id.scanView0);
		bottomLayout = (FrameLayout) v.findViewById(R.id.take_bottom_lay);
		scantTextView = (TextView) v.findViewById(R.id.scan_notice_txt);

		ViewTreeObserver vto2 = saoImageView.getViewTreeObserver();
		vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				saoImageView.getViewTreeObserver()
						.removeGlobalOnLayoutListener(this);
				ScanView.saoWidth = saoImageView.getWidth();
				ScanView.saoHeigth = saoImageView.getHeight();
			}
		});

		takepic.setOnClickListener(listener);
		openlight.setOnClickListener(listener);
		voiceSearch.setOnClickListener(listener);

	}

	private void initCameraView() {
		if (mCamera == null) {
			mCamera = Camera.open();
		}

		cView = new cameraView(context, mCamera);
		mFrameLayout.addView(new cameraView(context, mCamera));

	}

	class cameraView extends SurfaceView implements Callback {
		Size mPreviewSize;
		List<Size> mSupportedPreviewSizes;

		@SuppressWarnings("deprecation")
		public cameraView(Context context, Camera camera) {
			super(context);
			// TODO Auto-generated constructor stub
			SurfaceHolder surfaceHolder;
			mCamera = camera;
			surfaceHolder = getHolder();
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			surfaceHolder.addCallback(this);

			mSupportedPreviewSizes = mCamera.getParameters()
					.getSupportedPreviewSizes();
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
			if (holder.getSurface() == null) {
				return;
			}
			mCamera.stopPreview();

			mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width,
					height);
			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
			mCamera.setParameters(parameters);
			// CameraUtils.setCamera(mCamera, width, height, this);
			requestLayout();
			try {
				setCameraDisplayOrientation(context, cameraID, mCamera);
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
			} catch (Exception e) {
				// TODO: handle exception
			}

			// mCamera.takePicture(null, null,BingCamera.this );
			new Thread(face_recon).start();

			// mCamera.setPreviewCallback(previewCallback);

		}

		private Size getOptimalPreviewSize(List<Size> sizes, int width,
				int height) {
			// TODO Auto-generated method stub
			final double ASPECT_TOLERANCE = 0.2;
			double targetRatio = (double) width / height;
			if (sizes == null)
				return null;

			Size optimalSize = null;
			double minDiff = Double.MAX_VALUE;

			int targetHeight = height;

			// Try to find an size match aspect ratio and size
			for (Size size : sizes) {
				double ratio = (double) size.width / size.height;
				if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
					continue;
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}

			// Cannot find the one match the aspect ratio, ignore the
			// requirement
			if (optimalSize == null) {
				minDiff = Double.MAX_VALUE;
				for (Size size : sizes) {
					if (Math.abs(size.height - targetHeight) < minDiff) {
						optimalSize = size;
						minDiff = Math.abs(size.height - targetHeight);
					}
				}
			}
			return optimalSize;
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub

			isSurface = true;

			try {
				// Log.i(TAG, "Camera:" + mCamera);
				if (null == mCamera) {
					mCamera = Camera.open();
				}
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			isSurface = false;
			if (mCamera != null) {
				mCamera.setPreviewCallback(null);
				mCamera.stopPreview();
				mCamera.release(); // release the camera for other applications
				mCamera = null;

			}
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			// We purposely disregard child measurements because act as a
			// wrapper to a SurfaceView that centers the camera preview instead
			// of stretching it.
			final int width = resolveSize(getSuggestedMinimumWidth(),
					widthMeasureSpec);
			final int height = resolveSize(getSuggestedMinimumHeight(),
					heightMeasureSpec);
			setMeasuredDimension(width, height);

			if (mSupportedPreviewSizes != null) {
				mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes,
						width, height);
			}
		}

	}

	@SuppressLint("NewApi")
	public static void setCameraDisplayOrientation(Activity activity,
			int cameraId, android.hardware.Camera camera) {
		android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		angle = result;
		ScanFoodActivity.angle = result;
		camera.setDisplayOrientation(result);
	}

	/**
	 * 识别线程
	 */
	Runnable face_recon = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (!threadflag) {

				if (takpic) {
					try {
						mCamera.autoFocus(bingFocusCallback);
						takpic = false;
					} catch (Exception e) {
						// TODO: handle exception
						takpic = false;
					}

				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	};

	AutoFocusCallback bingFocusCallback = new AutoFocusCallback() {

		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			// TODO Auto-generated method stub
			foucs = success;
			if (success) {
				mCamera.takePicture(null, null, pictureCallback);
				takpic = false;
			} else {
				Toast.makeText(context, R.string.autofousnotice,
						Toast.LENGTH_LONG).show();
				takepic.setEnabled(true);
				scantTextView.setVisibility(View.GONE);
				voiceflage = false;
			}

		}
	};

	PictureCallback pictureCallback = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			Matrix matrix = new Matrix();
			matrix.setRotate(angle);
			BingLog.d(TAG, "是否可读:" + SdUtils.ExistSDCard());
			if (SdUtils.ExistSDCard()) {
				SavePic.saveToSDCard(data);
			} else {
				PhotoUtils.saveChatCode(data, context);
			}

			mCamera.startPreview();
			if (!voiceflage) {
				compareFood();
			} else {
				showVoiceSearch();

			}

		}
	};

	private void compareFood() {
		ExecutorService tExecutorService = Executors.newFixedThreadPool(2);
		tExecutorService.execute(cmpPicRunnable);
		// tExecutorService.execute(sendbmp);
		tExecutorService.execute(cmpPlaceRunnable);
	}

	/**
	 * 阈值计算
	 */
	Runnable cmpPlaceRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			isplacefit = isExitPlace();
			if (isfasongcm && iscomple) {
				if (isfit && isplacefit /* && !TextUtils.isEmpty(json) */) {
					mHandler.sendEmptyMessage(2);
					// parseJson(json);
				} else {
					logindialogfire();
				}
			}

		}
	};

	// 上传比较线程
	Runnable sendbmp = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			BingLog.i(TAG, "=============发送线程============");
			isfasongcm = false;
			String mybmp = "";
			if (SdUtils.ExistSDCard()) {

				mybmp = PhoneUtlis.bitmapzoomToString(FileUtils.HEALTH_IMAG
						+ "/wyy.png");
				HealthHttpClient.cmpFoodPic(mybmp, WyyApplication.getInfo()
						.getId(), feture, cw, handler);

			} else {
				mybmp = PhoneUtlis.bitmapToString(context);
				HealthHttpClient.cmpFoodPic(mybmp, WyyApplication.getInfo()
						.getId(), feture, cw, handler);
			}

		}
	};

	// 本地计算线程
	private Runnable cmpPicRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			BingLog.i(TAG, "=============计算线程============");
			isfit = isComfortAble();
			if (isfasongcm && isplace) {
				if (isfit && /* !TextUtils.isEmpty(json) && */isplacefit) {
					BingLog.i(TAG, "=============计算处理============");
					mHandler.sendEmptyMessage(2);
					// parseJson(json);
				} else {
					logindialogfire();
				}
			}
		}
	};

	// 上传结果处理
	private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			isfasongcm = true;
			BingLog.i(TAG, "返回结果:" + content);
			json = content;
			if (iscomple && isplace) {
				BingLog.i(TAG, "=============发送处理============");
				if (isfit && isplacefit) {
					parseJson(content);
				} else {
					logindialogfire();
				}

			}

		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);
			isfasongcm = true;
			logindialogfire();
		}

		public void onFinish() {
			super.onFinish();

			takepic.setEnabled(true);
		};

	};

	private void parseJson(String content) {
		JSONObject mJsonObject = null;

		if (content != null) {
			try {
				mJsonObject = new JSONObject(content);

				try {
					getFoodTag(mJsonObject.getJSONObject("samefood"));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				try {
					getFaceBean(mJsonObject.getJSONObject("face"));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				try {
					Config.log = mJsonObject.getString("log");
				} catch (Exception e) {
					// TODO: handle exception
				}

				if (JsonUtils.isSuccess(mJsonObject)) {
					JSONArray comments = mJsonObject.getJSONArray("comments");
					if (comments != null && comments.length() > 0) {
						BingLog.i(TAG, "得到食物:" + comments.getJSONObject(0));
						HealthRecoderBean healthRecoderBean = JsonUtils
								.getHealthRecoder(comments.getJSONObject(0));
						logindialog(healthRecoderBean);

					} else {
						logindialogfire();
					}

				} else {
					logindialogfire();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				BingLog.i(TAG, "异常");

				// 扫描 失败和成功的 写死测试
				logindialogfire();
			}

		} else {
			logindialogfire();

		}

		json = "";

	}

	// 比较失败
	public void logindialogfire() {
		takpic = false;
		isfit = false;
		Intent intent = new Intent();
		intent.setClass(context, ScanResultActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		json = "";
	}

	// 比较成功
	public void logindialog(HealthRecoderBean healthRecoderBean) {
		json = "";
		takpic = false;
		isfit = false;
		BingLog.i("=========", "扫描成功");
		Intent intent = new Intent();
		intent.putExtra("foods", healthRecoderBean);
		intent.putExtra("samefood", sameNearFoodBean);
		intent.putExtra("face", scanMoodBean);
		intent.setClass(context, ScanResultActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

	}

	private int secFood() {
		long time = System.currentTimeMillis();
		Align align = new Align();
		List<Feature> fs1;
		fs1 = align.createHistogram(PhoneUtlis
				.getSmall100ZoomBitmap(FileUtils.HEALTH_IMAG + "/wyy.png"));
		int featureNumber = fs1.size();
		BingLog.i(TAG,
				"指数:" + featureNumber + "耗时:"
						+ ((System.currentTimeMillis() - time) / 1000.00) + "s");
		return featureNumber;
	}

	private boolean isComfortAble() {
		iscomple = false;
		int feturenum = secFood();
		BingLog.i(TAG, "计算:" + feturenum);
		Config.feture_Value = "特征值:" + feturenum;
		feture = feturenum;
		if (feturenum > ConstantS.FOOD_FETURE_MIN
				&& feturenum < ConstantS.FOOD_FETURE_MAX) {
			iscomple = true;
			return true;
		}
		iscomple = true;
		return false;
	}

	private boolean isExitPlace() {
		isplace = false;
		double k = 0;
		k = BitmapRatioUtils.ratio(PhoneUtlis
				.getSmall40ZoomBitmap(FileUtils.HEALTH_IMAG + "/wyy.png"));
		BingLog.i(TAG, "阈值计算:" + k);
		Config.placeValue = "阈值:" + k;
		isplace = true;
		cw = k;
		if (k > ConstantS.THRESHOLD_INDEX) {
			return true;
		} else {
			return false;
		}
	}

	private BroadcastReceiver pageIndexReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals(ConstantS.PAGE_INDEX_CHANG)) {
				if (!isSurface) {
					mHandler.sendEmptyMessageDelayed(0, SPLASH_DELAY_MILLIS);
				}
			} else if (action.equals(ConstantS.ACTION_HIDE_UI_CHANGE)) {
				changeUI();
			}

		}
	};

	private void initFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConstantS.PAGE_INDEX_CHANG);
		filter.addAction(ConstantS.ACTION_HIDE_UI_CHANGE);
		getActivity().registerReceiver(pageIndexReceiver, filter);
	}

	/**
	 * Handler:跳转到不同界面
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				initCameraView();
				break;

			case 1:
				mFrameLayout.addView(cView);
				break;

			case 2:
				new Thread(sendbmp).start();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.take_pic:
				takepic2web();
				break;

			case R.id.open_ligth:
				openlight();
				break;

			case R.id.loop_yuyin:
				voiceflage = true;
				takepic2web();
				break;

			default:
				break;
			}
		}
	};

	private void takepic2web() {

		if (Utility.isConnected(getActivity())) {
			takpic = true;
			takepic.setEnabled(false);
			scanView.setScroll(true);

			if (!voiceflage) {
				scantTextView.setVisibility(View.VISIBLE);
			}

		} else {
			Toast.makeText(getActivity(), R.string.neterro, Toast.LENGTH_SHORT)
					.show();
		}

	}

	private void openlight() {
		if (mCamera.getParameters().getFlashMode()
				.equals(Parameters.FLASH_MODE_TORCH)) {
			try {
				parameters = mCamera.getParameters();
				parameters.setFlashMode(Parameters.FLASH_MODE_OFF);// 关闭闪光灯
				mCamera.setParameters(parameters);
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else {
			try {
				parameters = mCamera.getParameters();
				parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);// 打开闪光灯
				mCamera.setParameters(parameters);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	private void showVoiceSearch() {
		voiceflage = false;
		startActivity(new Intent(context, VoiceSearceActivity.class));
	}

	private void changeUI() {

		if (scanView.isShown()) {
			scanView.setVisibility(View.INVISIBLE);
			saoImageView.setVisibility(View.INVISIBLE);
			bottomLayout.setVisibility(View.GONE);//设置INVISIBLE会出现返回时无法显示的情况
			scantTextView.setVisibility(View.GONE);
			try {
				mCamera.stopPreview();
			} catch (Exception e) {
				// TODO: handle exception
			}

		} else {
			scanView.setVisibility(View.VISIBLE);
			saoImageView.setVisibility(View.INVISIBLE);
			bottomLayout.setVisibility(View.VISIBLE);
			scanView.setScroll(false);
			bottomLayout.requestFocus();
			BingLog.d(TAG, "显示tab_");
			try {
				mCamera.startPreview();
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

	@SuppressWarnings("unused")
	private PreviewCallback previewCallback = new PreviewCallback() {

		@Override
		public void onPreviewFrame(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			if (foucs) {
				if (SdUtils.ExistSDCard()) {
					SavePic.saveToSDCard(data);
				} else {
					PhotoUtils.saveChatCode(data, context);
				}
				foucs = false;
			}
		}
	};

	private void getFoodTag(JSONObject jsonObject) {
		sameNearFoodBean = JsonUtils.getNearFoodBean(jsonObject);
	}
	
	private void getFaceBean(JSONObject jsonObject) {
		scanMoodBean = JsonUtils.getScanMoodBean(jsonObject);
	}
	
}
