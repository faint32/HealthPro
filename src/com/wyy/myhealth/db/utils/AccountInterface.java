package com.wyy.myhealth.db.utils;

import com.wyy.myhealth.bean.UserAccountBean;

public interface AccountInterface extends BaseDateInterface {

	public long insert(UserAccountBean userAccountBean);
	
	public long update(UserAccountBean userAccountBean);
	
	public long delete(UserAccountBean userAccountBean);
	
	
}
