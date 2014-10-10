package com.wyy.myhealth.db.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.wyy.myhealth.bean.IceBoxData;
import com.wyy.myhealth.bean.IceBoxFoodBean;
import com.wyy.myhealth.db.WyyDatebase;

public class IceDadabaseUtils implements IceDataInterface {

	public IceDadabaseUtils(Context context) {
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
			del = WyyDatebase.wyyDatabase.delete(IceBoxData.TABLE_NAME, null,
					null);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return del;
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
	public long insert(IceBoxFoodBean iceBoxFoodBean) {
		// TODO Auto-generated method stub
		long id = -1;
		try {
			ContentValues values = new ContentValues();
			values.put(IceBoxData.NAME, iceBoxFoodBean.getName());
			values.put(IceBoxData.TIME, iceBoxFoodBean.getCreatetime());
			values.put(IceBoxData.FOOD_PIC, iceBoxFoodBean.getFoodpic());
			values.put(IceBoxData.FOOD_ID, iceBoxFoodBean.getId());
			values.put(IceBoxData.TYPE, iceBoxFoodBean.getType());
			// 调用方法插入数据
			id = WyyDatebase.wyyDatabase.insert(IceBoxData.TABLE_NAME, null,
					values);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public Object delete(long id) {
		// TODO Auto-generated method stub
		int del = -1;
		try {
			String where = IceBoxData._ID + " = ?";
			String[] whereArgs = { String.valueOf(id) };
			del = WyyDatebase.wyyDatabase.delete(IceBoxData.TABLE_NAME, where,
					whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return del;
	}

	@Override
	public Long delete(String id) {
		// TODO Auto-generated method stub
		long del = -1;
		try {
			String where = IceBoxData.FOOD_ID + " = ?";
			String[] whereArgs = { String.valueOf(id) };
			del = WyyDatebase.wyyDatabase.delete(IceBoxData.TABLE_NAME, where,
					whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return del;
	}

	@Override
	public Object queryData() {
		// TODO Auto-generated method stub
		List<IceBoxFoodBean> list = new ArrayList<IceBoxFoodBean>();
		Cursor cursor = null;
		try {
			// 获得数据库对象,如过数据库不存在则创建
			// 查询表中数据,获取游标
			cursor = WyyDatebase.wyyDatabase.query(IceBoxData.TABLE_NAME, null,
					null, null, null, null, "_ID desc");
			// 获取name列的索引
			@SuppressWarnings("unused")
			int idIndex = cursor.getColumnIndex(IceBoxData._ID);
			// 获取json列的索引
			int nameIndex = cursor.getColumnIndex(IceBoxData.NAME);

			int foodpicIndex = cursor.getColumnIndex(IceBoxData.FOOD_PIC);

			int foodidIndex = cursor.getColumnIndex(IceBoxData.FOOD_ID);

			int timeIndex = cursor.getColumnIndex(IceBoxData.TIME);

			int typeIndex = cursor.getColumnIndex(IceBoxData.TYPE);

			// 遍历查询结果集，将数据提取出来
			for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor
					.moveToNext()) {
				IceBoxFoodBean iceBoxFoodBean = new IceBoxFoodBean();
				iceBoxFoodBean.setName(cursor.getString(nameIndex));
				iceBoxFoodBean.setId(cursor.getString(foodidIndex));
				iceBoxFoodBean.setCreatetime(cursor.getString(timeIndex));
				iceBoxFoodBean.setFoodpic(cursor.getString(foodpicIndex));
				try {
					iceBoxFoodBean.setType(Integer.valueOf(cursor
							.getString(typeIndex)));
				} catch (Exception e) {
					// TODO: handle exception
				}

				list.add(iceBoxFoodBean);
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
	public boolean queryData(String foodid) {
		// TODO Auto-generated method stub
		boolean isexit = false;
		Cursor cursor = null;
		try {
			String where = IceBoxData.FOOD_ID + " = ?";
			String[] whereArgs = { String.valueOf(foodid) };
			cursor = WyyDatebase.wyyDatabase.query(IceBoxData.TABLE_NAME, null, where,
					whereArgs, null, null, null);
			if (cursor == null) {
				return isexit;
			}

			if (cursor.moveToFirst()) {
				int nameIndex = cursor.getColumnIndex(IceBoxData.NAME);
				String name = cursor.getString(nameIndex);
				if (!TextUtils.isEmpty(name)) {
					isexit = true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				// 关闭游标
				cursor.close();
			}

		}
		return isexit;
	}

}
