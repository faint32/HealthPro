package com.wyy.myhealth.db.utils;

public interface BaseDateInterface {

	public long delete();
	
	public long deleteAll();
	
	public Object queryData();
	
	public void close();
	
}
