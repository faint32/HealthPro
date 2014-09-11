package com.wyy.myhealth.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
/**
 * 
 * @author lyl
 *
 */
public class WyyDatebase {

	public static SQLiteDatabase wyyDatabase = null;

	public static SQLiteDatabase getMyDatabase(Context context) {
		if (wyyDatabase == null) {
			WyySqlHelper wyySqlHelper = new WyySqlHelper(context, null);
			wyyDatabase = wyySqlHelper.getWritableDatabase();
		}
		return wyyDatabase;

	}

}
