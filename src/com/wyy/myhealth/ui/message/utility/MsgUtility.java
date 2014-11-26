package com.wyy.myhealth.ui.message.utility;

import java.util.List;

import com.wyy.myhealth.bean.MsgBean;

public class MsgUtility {

	public static boolean isHasByMsg(List<MsgBean> list, MsgBean msgBean) {
		int length = list.size();
		for (int i = 0; i < length; i++) {
			if (list.get(i).equals(msgBean)) {
				list.add(i, msgBean);
				return true;
			}
		}
		return false;
	}

}
