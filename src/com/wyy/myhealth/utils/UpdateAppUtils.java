package com.wyy.myhealth.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import com.wyy.myhealth.R;
import com.wyy.myhealth.http.AsyncHttpResponseHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;

/**
 * 应用更新工具类
 * 
 * @author lyl
 * 
 */
public class UpdateAppUtils {

	public static long id = 0;

	public static Context context;

	private static String getLoVersion() {

		PackageManager packageManager = UpdateAppUtils.context
				.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					UpdateAppUtils.context.getPackageName(), 0);
			String version = packageInfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	private static AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			parseUrl(content);
			// ToastUtils.showLong(AboutActivity.this,
			// getString(R.string.is_alreadly_version));
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);
			Toast.makeText(context,
					context.getResources().getString(R.string.net_erro),
					Toast.LENGTH_SHORT).show();
		}

	};

	private static void parseUrl(String content) {

		try {
			JSONObject jsonObject = new JSONObject(content);
			String url = HealthHttpClient.APP_URL
					+ jsonObject.getJSONArray("versions").getJSONObject(0)
							.getString("clientPath");
			String versionNum = jsonObject.getJSONArray("versions")
					.getJSONObject(0).getString("versionNum");

			String updateinfo = "更新信息:"
					+ "\n"
					+ jsonObject.getJSONArray("versions").getJSONObject(0)
							.getString("updateInfo");

			if (!versionNum.equals(getLoVersion())) {
				Toast.makeText(
						context,
						context.getResources()
								.getString(R.string.hasnewversion),
						Toast.LENGTH_SHORT).show();
				downDialog(url, updateinfo);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void downDialog(final String url, String info) {
		new AlertDialog.Builder(UpdateAppUtils.context)
				.setTitle(
						UpdateAppUtils.context.getResources().getString(
								R.string.downnotice))
				.setMessage(info)
				.setPositiveButton(R.string.download,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								downLoad(url);

							}
						})
				.setNegativeButton(R.string.canel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						}).show();
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private static void downLoad(String url) {
		DownloadManager downloadManager = (DownloadManager) UpdateAppUtils.context
				.getSystemService(Context.DOWNLOAD_SERVICE);
		Uri uri = Uri.parse(url);
		Request request = new Request(uri);
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
				| DownloadManager.Request.NETWORK_WIFI);
		request.setTitle(UpdateAppUtils.context.getResources().getString(
				R.string.app_name));
		id = downloadManager.enqueue(request);
	}

	/**
	 * 更新应用
	 * 
	 * @param context
	 */
	public static void upDateApp(Context context) {
		UpdateAppUtils.context = context;
		HealthHttpClient.doHttpUpdateApp(handler);
	}

}
