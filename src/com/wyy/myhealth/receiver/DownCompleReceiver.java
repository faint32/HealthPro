package com.wyy.myhealth.receiver;

import java.io.File;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class DownCompleReceiver extends BroadcastReceiver {
	private DownloadManager downloadManager;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);

		Query query = new Query();
		query.setFilterById(id);
		downloadManager = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);
		Cursor cursor = downloadManager.query(query);
		int columnCount = cursor.getColumnCount();
		String path = null; // TODO 这里把所有的列都打印一下，有什么需求，就怎么处理,文件的本地路径就是path
		while (cursor.moveToNext()) {
			for (int j = 0; j < columnCount; j++) {
				String columnName = cursor.getColumnName(j);
				String string = cursor.getString(j);
				if (columnName.equals("local_filename")) {
					path = string;
				}
				if (string != null) {
					System.out.println(columnName + ": " + string);
				} else {
					System.out.println(columnName + ": null");
				}
			}
		}
		cursor.close();

		if (!TextUtils.isEmpty(path)) {
			String fileName = path;

			Uri uri = Uri.fromFile(new File(fileName));
			Intent intent1 = new Intent();
			intent1.setAction(Intent.ACTION_VIEW);
			intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent1.setDataAndType(uri,
					"application/vnd.android.package-archive");
			context.startActivity(intent1);
		}

	}

}
