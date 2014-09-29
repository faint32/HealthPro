package com.wyy.myhealth.stepcount;

import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.contants.ConstantS;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.welcome.WelcomeActivity;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

/**
 * @author Jackie 2012.12.11 记步服务
 */
public class StepService extends Service {

	private Double distance = 0.0;// 路程：米
	private Double calories = 0.0;// 热量：卡路里
	private Double velocity = 0.0;// 速度：米每秒

	private int step_length = 70;
	private int weight = 54;
	public static int total_step = 0;
	private long timer = 0;// 运动时间

	private Thread thread;

	private static long startTimer = 0;// 开始时间

	private static long tempTime = 0;

	public static Boolean FLAG = false;// 服务运行标志

	private SensorManager mSensorManager;// 传感器服务
	private StepDetector detector;// 传感器监听对象

	private PowerManager mPowerManager;// 电源管理服务
	private WakeLock mWakeLock;// 屏幕灯

	public static String userID = "";
	
	private Intent postIntent;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i("service", "=======onCreate=======");
		postIntent=new Intent();
		postIntent.setAction(ConstantS.POST_FOOT_ACTION);
		// stopForeground(true);// 提升StepService的优先级别

		FLAG = true;// 标记为服务正在运行
		// userID=BApplication.getPersonInfo().getId();

		// 实例化监听对象
		detector = new StepDetector(this);

		// 获取传感器的服务
		mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		// 注册传感器
		mSensorManager.registerListener(detector,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_FASTEST);

		// 电源管理服务
		mPowerManager = (PowerManager) this
				.getSystemService(Context.POWER_SERVICE);
		mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP, "Jackie");
		mWakeLock.acquire();

		initThread();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i("service", "=======onStartCommand=======");
		FLAG = true;
		if (null != WyyApplication.getInfo()) {
			userID = WyyApplication.getInfo().getId();
		} else {
			WelcomeActivity.getPersonInfo(this);
			if (null!=WyyApplication.getInfo()) {
				userID = WyyApplication.getInfo().getId();
			}
			
		}

		Log.i("service", "=======userID=======" + userID);
		return START_STICKY;
		// return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("service", "=======onDestroy=======");
		FLAG = false;// 服务停止
		if (detector != null) {
			mSensorManager.unregisterListener(detector);
		}

		if (mWakeLock != null) {
			mWakeLock.release();
		}
	}

	/**
	 * 计算行走的步数
	 */
	private void countDistance() {
		if (StepDetector.CURRENT_SETP % 2 == 0) {
			distance = (StepDetector.CURRENT_SETP / 2) * 3 * step_length * 0.01;
		} else {
			distance = ((StepDetector.CURRENT_SETP / 2) * 3 + 1) * step_length
					* 0.01;
		}
	}

	/**
	 * 实际的步数
	 */
	private void countStep() {
		if (StepDetector.CURRENT_SETP % 2 == 0) {
			total_step = StepDetector.CURRENT_SETP / 2 * 3;
		} else {
			total_step = StepDetector.CURRENT_SETP / 2 * 3 + 1;
		}

	}

	private void initThread() {
		if (thread == null) {

			thread = new Thread() {// 子线程用于监听当前步数的变化

				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					int temp = 0;
					while (FLAG) {
						try {
							Thread.sleep(300);
							// Log.i("====", "运行"+StepService.FLAG);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (StepService.FLAG) {
							if (temp != StepDetector.CURRENT_SETP) {
								temp = StepDetector.CURRENT_SETP;
							}

							if (startTimer != System.currentTimeMillis()) {
								timer = tempTime + System.currentTimeMillis()
										- startTimer;
							}
							countStep();
						}
					}
				}
			};
			thread.start();
		}
	}

	public static void startService(Activity context) {
		if (!FLAG) {
			Intent intent = new Intent();
			intent.setClass(context, StepService.class);
			context.startService(intent);
		}
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		Log.i("service", "=======onLowMemory=======");
	}

	@Override
	public void onTrimMemory(int level) {
		// TODO Auto-generated method stub
		super.onTrimMemory(level);
		Log.i("service", "=======onTrimMemory=======");
	}

}
