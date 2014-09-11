package com.wyy.myhealth.db.upgrade;

import com.wyy.myhealth.db.WyySqlHelper;

import android.database.sqlite.SQLiteDatabase;

public class UpgradeUtils {

	public static void upgrade5to6(SQLiteDatabase db) {
		db.execSQL(WyySqlHelper.CREATE_USER_ACCOUNT);
	}
	
}
