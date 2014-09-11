package com.wyy.myhealth.db.utils;

import com.wyy.myhealth.bean.IceBoxFoodBean;

public interface IceDataInterface extends BaseDateInterface {

	public long insert(IceBoxFoodBean iceBoxFoodBean);
	
	public Object delete(long id);
	
	public Long delete(String id);
	
	public Object queryData();
	
	public boolean queryData(String foodid);
}
