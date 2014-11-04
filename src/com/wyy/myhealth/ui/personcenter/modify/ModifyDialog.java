package com.wyy.myhealth.ui.personcenter.modify;

import com.wyy.myhealth.R;
import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.PersonalInfo;
import com.wyy.myhealth.http.utils.HealthHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class ModifyDialog {

	public static void modifySex(final Activity context, int index) {
		final String[] itemsId = { context.getString(R.string.boy),
				context.getString(R.string.gilr) };
		final int checkedItem = index;
		AlertDialog dlg = new AlertDialog.Builder(context)
				.setTitle(R.string.sex)
				.setSingleChoiceItems(itemsId, checkedItem,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								PersonalInfo info = WyyApplication.getInfo();
								if (info != null) {
									if (which == 0) {
										which = 1;
									} else {
										which = 0;
									}
									info.setGender("" + which);
									HealthHttpClient.doHttpFinishPersonInfo(
											info, new ModifyHandler_(context));
								}
								dialog.dismiss();
							}
						}).create();
		dlg.show();
	}

}
