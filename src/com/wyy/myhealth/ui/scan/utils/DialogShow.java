package com.wyy.myhealth.ui.scan.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.wyy.myhealth.R;

public class DialogShow {

	public static void showHelpDialog(Context context, String message) {

		new AlertDialog.Builder(context).setTitle(R.string.use_help)
				.setMessage(message)
				.setPositiveButton(R.string.ok, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).show();

	}
	
}
