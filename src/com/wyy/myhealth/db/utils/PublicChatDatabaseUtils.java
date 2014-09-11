package com.wyy.myhealth.db.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.wyy.myhealth.bean.PublicMsgBean;
import com.wyy.myhealth.bean.PublicMsgData;
import com.wyy.myhealth.db.WyyDatebase;

public class PublicChatDatabaseUtils implements PmsgInterface {

	public PublicChatDatabaseUtils(Context context) {
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
			del = WyyDatebase.wyyDatabase.delete(PublicMsgData.TABLE_NAME,
					null, null);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return del;
	}

	@Override
	public Object queryData() {
		// TODO Auto-generated method stub
		return null;
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
	public List<?> querybyName(String chatname) {
		// TODO Auto-generated method stub
		List<PublicMsgBean> list = new ArrayList<PublicMsgBean>();
		Cursor cursor = null;
		try {
			// 获得数据库对象,如过数据库不存在则创建
			// 查询表中数据,获取游标

			String selection = PublicMsgData.CHAT_NAME + " =?";

			String[] selectionArgs = { chatname };

			cursor = WyyDatebase.wyyDatabase.query(PublicMsgData.TABLE_NAME,
					null, selection, selectionArgs, null, null, "_ID desc");
			// 获取name列的索引
			int idIndex = cursor.getColumnIndex(PublicMsgData._ID);
			// 获取json列的索引
			int contentIndex = cursor.getColumnIndex(PublicMsgData.CONTENT);

			int timeIndex = cursor.getColumnIndex(PublicMsgData.TIME);

			int realtimeIndex = cursor.getColumnIndex(PublicMsgData.REAL_TIME);

			int typeIndex = cursor.getColumnIndex(PublicMsgData.MSGTYPE);

			// 遍历查询结果集，将数据提取出来
			for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor
					.moveToNext()) {
				PublicMsgBean publicMsgBean = new PublicMsgBean();
				
				publicMsgBean.set_id(cursor.getInt(idIndex));
				publicMsgBean.setContent(cursor.getString(contentIndex));
				publicMsgBean.setRealtime(cursor.getLong(realtimeIndex));
				publicMsgBean.setMsgtype(cursor.getInt(typeIndex));
				publicMsgBean.setTime(cursor.getString(timeIndex));
				publicMsgBean.setChatname(chatname);
				
				list.add(publicMsgBean);
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
	public long insert(String time, long realtime, String content, int msgtype,
			String chatname) {
		// TODO Auto-generated method stub
		long id = -1;
		try {
			ContentValues values = new ContentValues();
			values.put(PublicMsgData.TIME, time);
			values.put(PublicMsgData.REAL_TIME, realtime);
			values.put(PublicMsgData.CONTENT, content);
			values.put(PublicMsgData.MSGTYPE, msgtype);
			values.put(PublicMsgData.CHAT_NAME, chatname);
			// 调用方法插入数据
			id = WyyDatebase.wyyDatabase.insert(PublicMsgData.TABLE_NAME, null,
					values);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public long update(String time, long realtime, String content, int msgtype,
			String chatname, int _id) {
		// TODO Auto-generated method stub
		long id = -1;
		try {
			ContentValues values = new ContentValues();
			values.put(PublicMsgData.TIME, time);
			values.put(PublicMsgData.REAL_TIME, realtime);
			values.put(PublicMsgData.CONTENT, content);
			values.put(PublicMsgData.MSGTYPE, msgtype);
			values.put(PublicMsgData.CHAT_NAME, chatname);

			String where = PublicMsgData._ID + " =?";
			String[] whereValue = { String.valueOf(_id) };

			// 调用方法插入数据
			id = WyyDatebase.wyyDatabase.update(PublicMsgData.TABLE_NAME,
					values, where, whereValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public long deleteAll(String chatname) {
		// TODO Auto-generated method stub
		long del = -1;
		try {
			String where = PublicMsgData.CHAT_NAME + " = ?";
			String[] whereArgs = { String.valueOf(chatname) };
			del = WyyDatebase.wyyDatabase.delete(PublicMsgData.TABLE_NAME,
					where, whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return del;
	}

	@Override
	public long delete(String chatname, String time) {
		// TODO Auto-generated method stub
		int del = -1;
		try {
			String where = PublicMsgData.REAL_TIME + " = ?,"
					+ PublicMsgData.CHAT_NAME + " =?";
			String[] whereArgs = { String.valueOf(time), chatname };
			del = WyyDatebase.wyyDatabase.delete(PublicMsgData.TABLE_NAME, where,
					whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return del;
	}

	@Override
	public long delete(long _id) {
		// TODO Auto-generated method stub
		int del = -1;
		try {
			String where = PublicMsgData._ID + " = ?";
			String[] whereArgs = { String.valueOf(_id) };
			del = WyyDatebase.wyyDatabase.delete(PublicMsgData.TABLE_NAME,
					where, whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return del;
	}

}
