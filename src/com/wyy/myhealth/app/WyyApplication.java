package com.wyy.myhealth.app;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.widget.Toast;

import com.baidu.frontia.FrontiaApplication;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.wyy.myhealth.BuildConfig;
import com.wyy.myhealth.R;
import com.wyy.myhealth.bean.Foods;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.file.ImageInfo;
import com.wyy.myhealth.file.utils.FileUtils;
import com.wyy.myhealth.file.utils.SdUtils;

public class WyyApplication extends FrontiaApplication {

	private static WyyApplication wInstance;
	public boolean m_bKeyRight = true;
	public BMapManager mBMapManager = null;
	private static final String BAIDU_DUBUG_KEY = "mcGXuNClGvdQamQ7fHdaIouX";// debug
																				// key
	private static final String BAIDU_REALSE_KEY = "3ZvcwhjsKy1696GLSwp0uFac";// realse
																				// key
	public static String BAIDU_KEY = "3ZvcwhjsKy1696GLSwp0uFac";
	static {
		if (BuildConfig.DEBUG) {
			BAIDU_KEY = BAIDU_DUBUG_KEY;
		} else {
			BAIDU_KEY = BAIDU_REALSE_KEY;
		}
	}
	private static PersonalInfo info;
	private static Foods foods;
	private static List<ImageInfo> headerImaList = new ArrayList<ImageInfo>();

	public static ImageLoader imageLoader = ImageLoader.getInstance();

	public static DisplayImageOptions options;

	public static DisplayImageOptions optionscir;

	public static DisplayImageOptions options_small;

	public static List<ImageInfo> getHeaderImaList() {
		return headerImaList;
	}

	public static WyyApplication getInstance() {
		return wInstance;
	}

	public static PersonalInfo getInfo() {
		return info;
	}

	public static void setInfo(PersonalInfo info) {
		WyyApplication.info = info;
	}

	public static Foods getFoods() {
		return WyyApplication.foods;
	}

	public static void setFoods(Foods foods) {
		WyyApplication.foods = foods;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub

		super.onCreate();

		wInstance = this;
		try {
			initEngineManager(this);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			initImageLoader(this);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (SdUtils.ExistSDCard()) {
			try {
				LoadDate();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				// .memoryCache(new LruMemoryCache(maxSize))
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// .writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);

		WyyApplication.options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.pic_loading_)
				.showImageForEmptyUri(R.drawable.pic_empty)
				.showImageOnFail(R.drawable.pic_failure).cacheInMemory(false)
				.bitmapConfig(Config.ARGB_8888).cacheOnDisc(true)
				.considerExifParams(true).build();

		options_small = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.pic_loading)
				.showImageForEmptyUri(R.drawable.pic_empty)
				.showImageOnFail(R.drawable.pic_failure_).cacheInMemory(false)
				.bitmapConfig(Config.ARGB_8888).cacheOnDisc(true)
				.considerExifParams(true).build();

		optionscir = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.pic_loading_)
				.showImageForEmptyUri(R.drawable.pic_empty)
				.showImageOnFail(R.drawable.pic_failure_).cacheInMemory(false)
				.displayer(new RoundedBitmapDisplayer(50)).cacheOnDisc(true)
				.bitmapConfig(Config.ARGB_8888).considerExifParams(true)
				.build();

	}

	/**
	 * ��ʼ����ͼ����
	 * 
	 * @param context
	 */
	public void initEngineManager(Context context) throws Exception {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
		}

		if (!mBMapManager.init(BAIDU_KEY, new MyGeneralListener())) {
			Toast.makeText(WyyApplication.getInstance(),
					getString(R.string.neterro), Toast.LENGTH_LONG).show();
		}
	}

	// �����¼���������������ͨ�������������Ȩ��֤�����
	public static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(
						WyyApplication.getInstance().getApplicationContext(),
						WyyApplication.getInstance()
								.getString(R.string.neterro), Toast.LENGTH_LONG)
						.show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(
						WyyApplication.getInstance().getApplicationContext(),
						"������ȷ�ļ���������", Toast.LENGTH_LONG).show();
			}
			// ...
		}

		@Override
		public void onGetPermissionState(int iError) {
			// ����ֵ��ʾkey��֤δͨ��
			if (iError != 0) {
				WyyApplication.getInstance().m_bKeyRight = false;
			} else {
				WyyApplication.getInstance().m_bKeyRight = true;
			}
		}
	}

	/**
	 * ����ͷ������
	 */
	public static void LoadDate() {
		FileUtils.createPath();
		File[] files = new File(FileUtils.HEALTH_IMAG).listFiles();
		File f;
		String name;
		String path;

		if (null == files) {
			return;
		}
		int length = files.length;
		if (length == 0) {
			return;
		}
		for (int i = 0; i < length; i++) {
			ImageInfo info = new ImageInfo();
			f = files[i];
			if (!f.canRead()) {
				return;
			}

			name = f.getName().substring(f.getName().indexOf("_") + 1,
					f.getName().length() - 4);
			path = FileUtils.HEALTH_IMAG + "/" + f.getName();
			info.setImaname(name);
			info.setImapath(path);
			headerImaList.add(info);
		}

	}

}
