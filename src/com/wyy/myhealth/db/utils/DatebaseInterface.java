package com.wyy.myhealth.db.utils;


public interface DatebaseInterface extends BaseDateInterface{

	public Object update(String json,int id);
	
	public long update(String json,int postion ,int id);
	
	public Object delete(long id);
	
	public long insert(String json,int postion);
	
	public long insert(String json,String time);
	
	public Object queryData();
	
	public Object queryDataId(long id);
	
	public void close();
	
}
