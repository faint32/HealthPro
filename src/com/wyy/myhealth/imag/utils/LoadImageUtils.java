package com.wyy.myhealth.imag.utils;

import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

public class LoadImageUtils {

	public static void loadImage4ImageV(ImageView imageView, String url) {
		WyyApplication.imageLoader.displayImage(url, imageView,
				WyyApplication.options);
	}

	public static void loadImage4ImageV_Small(ImageView imageView, String url) {
		WyyApplication.imageLoader.displayImage(url, imageView,
				WyyApplication.options_small);
	}

	public static void loadImageCirImageV(ImageView imageView, String url) {
		WyyApplication.imageLoader.displayImage(url, imageView,
				WyyApplication.optionscir);
	}

	public static void loadSdImage4ImageV(ImageView imageView, String url) {
		WyyApplication.imageLoader.displayImage("file://" + url, imageView,
				WyyApplication.options);
	}

	public static void loadWebImageV_Min(ImageView imageView, String url) {
		WyyApplication.imageLoader.displayImage(url, imageView,
				WyyApplication.options_min);
	}

	public static void loadWebImageV_Min(ImageView imageView, String url,
			ImageLoadingListener listener) {
		WyyApplication.imageLoader.displayImage(url, imageView,
				WyyApplication.options_min, listener);
	}

	public static void clear_Coach(Context context) {

		try {
			WyyApplication.imageLoader.clearDiscCache();
			WyyApplication.imageLoader.clearMemoryCache();
			Toast.makeText(context, R.string.clear_cache_success,
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
