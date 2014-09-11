package com.wyy.myhealth.db.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.wyy.myhealth.bean.UserAccountBean;
import com.wyy.myhealth.db.WyyDatebase;
import com.wyy.myhealth.db.table.UserAccountTable;

public class AccountUtils implements AccountInterface{
	
	public AccountUtils(Context context){
		WyyDatebase.getMyDatabase(context);
	}

	@Override
	public long delete() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long deleteAll() {
		// TODO Auto-generated method stub
		long del = -1;

		try {
			del = WyyDatebase.wyyDatabase.delete(UserAccountTable.TABLE_NAME, null,
					null);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return del;
	}

	@Override
	public Object queryData() {
		// TODO Auto-generated method stub
		List<UserAccountBean> list = new ArrayList<>();
		Cursor cursor = null;
		try {
			// 获得数据库对象,如过数据库不存在则创建
			// 查询表中数据,获取游标
			cursor = WyyDatebase.wyyDatabase.query(UserAccountTable.TABLE_NAME, null,
					null, null, null, null, "_ID desc");
			int idIndex = cursor.getColumnIndex(UserAccountTable._ID);
			int nameIndex = cursor.getColumnIndex(UserAccountTable.USER_NAME);
			int passwordIndex = cursor.getColumnIndex(UserAccountTable.PASSWORD);
			for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor
					.moveToNext()) {
				UserAccountBean userAccountBean=new UserAccountBean();
				userAccountBean.set_id(cursor.getInt(idIndex));
				userAccountBean.setUsername(cursor.getString(nameIndex));
				userAccountBean.setPassword(cursor.getString(passwordIndex));

				list.add(userAccountBean);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				// 关闭游标
				cursor.close();
			}

		}

		return list;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		if (WyyDatebase.wyyDatabase != null && WyyDatebase.wyyDatabase.isOpen()) {
			WyyDatebase.wyyDatabase.close();
			WyyDatebase.wyyDatabase = null;
		}
	}

	@Override
	public long insert(UserAccountBean userAccountBean) {
		// TODO Auto-generated method stub
		long id = -1;
		try {
			ContentValues values = new ContentValues();
			values.put(UserAccountTable.USER_NAME, userAccountBean.getUsername());
			values.put(UserAccountTable.PASSWORD, userAccountBean.getPassword());
			// 调用方法插入数据
			id = WyyDatebase.wyyDatabase.insert(UserAccountTable.TABLE_NAME, null,
					values);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public long update(UserAccountBean userAccountBean) {
		// TODO Auto-generated method stub
		long updatepostion=0;
    	try {
	    	ContentValues values = new ContentValues();	
	    	values.put(UserAccountTable.USER_NAME, userAccountBean.getUsername());
	    	values.put(UserAccountTable.PASSWORD, userAccountBean.getPassword());
	    	String where =  UserAccountTable.USER_NAME+" = ?";
		    String[] whereValue = { String.valueOf(userAccountBean.getUsername()) };
	    	updatepostion=WyyDatebase.wyyDatabase.update(UserAccountTable.TABLE_NAME, values, where, whereValue);
    	} catch (Exception e) {
			e.printStackTrace();
		}
		return updatepostion;
	}

	@Override
	public long delete(UserAccountBean userAccountBean) {
		// TODO Auto-generated method stub
		int del = -1;
		try {
			String where = UserAccountTable._ID + " = ?";
			String[] whereArgs = { String.valueOf(userAccountBean.get_id()) };
			del = WyyDatebase.wyyDatabase.delete(UserAccountTable.TABLE_NAME, where,
					whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return del;
	}

	

}
