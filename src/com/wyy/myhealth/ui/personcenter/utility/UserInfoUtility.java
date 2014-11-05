package com.wyy.myhealth.ui.personcenter.utility;

import java.util.List;

import com.wyy.myhealth.bean.PersonalInfo;

public class UserInfoUtility {

	public static void reshPersonInfo(PersonalInfo personalInfo,
			List<PersonalInfo> list) {
		if (list == null) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			if (personalInfo.getId().equals(list.get(i).getId())) {
				list.set(i, personalInfo);
			}
		}
	}

	public static boolean isExitPersonInfo(PersonalInfo personalInfo,
			List<PersonalInfo> list) {
		if (list == null) {
			return false;
		}

		for (int i = 0; i < list.size(); i++) {
			if (personalInfo.getId().equals(list.get(i).getId())) {
				return true;
			}
		}

		return false;

	}

}
