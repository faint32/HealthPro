package com.wyy.myhealth.support.report;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.http.BingHttpHandler;
import com.wyy.myhealth.http.utils.HealthHttpClient;
import com.wyy.myhealth.http.utils.JsonUtils;

public class ReportUtility {

	public static void report(final Context context, String linkid, String type) {

		HealthHttpClient.reportUserMf(WyyApplication.getInfo().getId(), linkid,
				type, new BingHttpHandler() {

					@Override
					protected void onGetSuccess(JSONObject response) {
						// TODO Auto-generated method stub
						if (JsonUtils.isSuccess(response)) {
							Toast.makeText(context, R.string.complaint_success,
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(context, R.string.complaint_faliure,
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					protected void onGetFinish() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFailure(Throwable e, JSONObject errorResponse) {
						// TODO Auto-generated method stub
						super.onFailure(e, errorResponse);
						Toast.makeText(context, R.string.complaint_faliure,
								Toast.LENGTH_SHORT).show();
					}

				});

	}

	public static void reportDialog(final Context context, final String linkid,
			int index) {
		final String[] itemsId = {
				context.getString(R.string.complaint_type_1),
				context.getString(R.string.complaint_type_2),
				context.getString(R.string.complaint_type_3),
				context.getString(R.string.complaint_type_4) };
		final int checkedItem = index;
		new AlertDialog.Builder(context)
				.setTitle(R.string.complaint)
				.setSingleChoiceItems(itemsId, checkedItem,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								report(context, linkid, "" + (which + 1));
								dialog.dismiss();
							}
						}).show();
	}

}
