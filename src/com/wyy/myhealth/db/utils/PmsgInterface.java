package com.wyy.myhealth.db.utils;

import java.util.List;

public interface PmsgInterface extends BaseDateInterface {

	public List<?> querybyName(String chatname);
	
	public long insert(String time,long realtime ,String content,int msgtype,String chatname);
	
	public long update(String time,long realtime ,String content,int msgtype,String chatname,int _id);
	
	public long deleteAll(String chatname);
	
	public long delete(String chatname,String time);
	
	public long delete(long _id);
	
}
