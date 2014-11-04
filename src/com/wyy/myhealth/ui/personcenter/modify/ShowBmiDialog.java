package com.wyy.myhealth.ui.personcenter.modify;

import com.wyy.myhealth.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class ShowBmiDialog {

	public static void showBmi(final Activity context) {
		new AlertDialog.Builder(context).setTitle(R.string.bmi)
				.setMessage(R.string.bmi_indro)
				.setPositiveButton(android.R.string.ok, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).show();
	}

}
