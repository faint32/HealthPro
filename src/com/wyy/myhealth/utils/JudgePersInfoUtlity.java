package com.wyy.myhealth.utils;

import android.text.TextUtils;

import com.wyy.myhealth.app.WyyApplication;
import com.wyy.myhealth.bean.PersonalInfo;

public class JudgePersInfoUtlity {

	public static boolean isComplete() {
		boolean commplete = false;
		PersonalInfo personalInfo = WyyApplication.getInfo();
		if (personalInfo == null) {
			return commplete;
		}

		String name = personalInfo.getUsername();
		String age = personalInfo.getAge();
		String body = personalInfo.getBodyindex();
		if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(age)
				&& !TextUtils.isEmpty(body)) {
			commplete = true;
		}

		return commplete;
	}

}
