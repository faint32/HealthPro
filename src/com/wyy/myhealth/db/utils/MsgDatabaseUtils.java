package com.wyy.myhealth.db.utils;


import java.util.ArrayList;
import java.util.List;

import com.wyy.myhealth.bean.ListDataBead;
import com.wyy.myhealth.bean.MsgData;
import com.wyy.myhealth.db.WyyDatebase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class MsgDatabaseUtils implements DatebaseInterface {

	public MsgDatabaseUtils(Context context){
		WyyDatebase.getMyDatabase(context);
	}
	
	@Override
	public Object update(String json, int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long update(String json, int postion, int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object delete(long id) {
		// TODO Auto-generated method stub
		int del=-1;
		 try{
			 String where =  MsgData._ID+" = ?";
	    	 String[] whereArgs = {String.valueOf(id)};
	    	 del=WyyDatebase.wyyDatabase.delete(MsgData.TABLE_NAME, where, whereArgs);
	    }catch (Exception e){
	      e.printStackTrace();
	    }
		return del;
	}

	@Override
	public long insert(String json, int postion) {
		// TODO Auto-generated method stub
		long id = -1;
    	try {
	    	ContentValues values = new ContentValues();	
	    	values.put(MsgData.JSONDATA, json);
	    	values.put(MsgData.TIME_STRING, ""+postion);
	    	//调用方法插入数据
	    	id = WyyDatebase.wyyDatabase.insert(MsgData.TABLE_NAME, null, values);	
    	} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public Object queryData() {
		// TODO Auto-generated method stub
		List<ListDataBead> list=new ArrayList<ListDataBead>();
		Cursor cursor = null;
    	try {
			//获得数据库对象,如过数据库不存在则创建
			//查询表中数据,获取游标
			cursor = WyyDatebase.wyyDatabase.query(MsgData.TABLE_NAME,null,null,null,null,null,"_ID desc");
			//获取name列的索引
			int idIndex = cursor.getColumnIndex(MsgData._ID);	    
			 //获取json列的索引
			int jsonIndex = cursor.getColumnIndex(MsgData.JSONDATA);	
			
			int postionIndex=cursor.getColumnIndex(MsgData.TIME_STRING);
      
		    //遍历查询结果集，将数据提取出来
		    for(cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()){	
					  ListDataBead listDataBead=new ListDataBead();
					  listDataBead.setJsondata(cursor.getString(jsonIndex));
					  listDataBead.setTime(cursor.getString(postionIndex));
					  listDataBead.set_id(cursor.getInt(idIndex));
					  list.add(listDataBead);
		    }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(cursor!=null){
				//关闭游标
				cursor.close();		
			}
			
		}
    	
    	return list;
	}

	@Override
	public Object queryDataId(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long deleteAll() {
		// TODO Auto-generated method stub
		
		long del=-1;
		
		try {
			del=WyyDatebase.wyyDatabase.delete(MsgData.TABLE_NAME, null, null);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return del;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		if(WyyDatebase.wyyDatabase!=null && WyyDatebase.wyyDatabase.isOpen()){
			//关闭数据库对象
			WyyDatebase.wyyDatabase.close();		
			WyyDatebase.wyyDatabase=null;
		}
		
	}

	@Override
	public long insert(String json, String time) {
		// TODO Auto-generated method stub
		long id = -1;
    	try {
	    	ContentValues values = new ContentValues();	
	    	values.put(MsgData.JSONDATA, json);
	    	values.put(MsgData.TIME_STRING, ""+time);
	    	//调用方法插入数据
	    	id = WyyDatebase.wyyDatabase.insert(MsgData.TABLE_NAME, null, values);	
    	} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public long delete() {
		// TODO Auto-generated method stub
		return 0;
	}

}
