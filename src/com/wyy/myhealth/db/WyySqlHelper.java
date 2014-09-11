package com.wyy.myhealth.db;

import com.wyy.myhealth.bean.CollectData;
import com.wyy.myhealth.bean.IceBoxData;
import com.wyy.myhealth.bean.MsgData;
import com.wyy.myhealth.bean.PublicMsgData;
import com.wyy.myhealth.bean.ShaiData;
import com.wyy.myhealth.db.table.UserAccountTable;
import com.wyy.myhealth.db.upgrade.UpgradeUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class WyySqlHelper extends SQLiteOpenHelper {

	private static final String NAME = "wyy.db";

	private static final int VERSION = 6;

	private static final String CREATE_SHAI_SQL = "CREATE TABLE "
			+ ShaiData.TABLE_NAME + "(" + ShaiData._ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ ShaiData.JSONDATA + " TEXT," + ShaiData.SHAI_POSTION + " INTEGER"
			+ ");";

	private static final String CREATE_COLLECT_SQL = "CREATE TABLE "
			+ CollectData.TABLE_NAME + "(" + CollectData._ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ CollectData.JSONDATA + " TEXT," + CollectData.COLLECT_POSTION
			+ " INTEGER" + ");";

	private static final String CREATE_MSG_MOOENT_SQL = "CREATE TABLE "
			+ MsgData.TABLE_NAME + "(" + MsgData._ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + MsgData.JSONDATA
			+ " TEXT," + MsgData.TIME_STRING + " LONG" + ");";

	private static final String CREATE_PUBLIC_MSG_SQL = "CREATE TABLE "
			+ PublicMsgData.TABLE_NAME + "(" + PublicMsgData._ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ PublicMsgData.CONTENT + " TEXT," + PublicMsgData.REAL_TIME
			+ " LONG," + PublicMsgData.CHAT_NAME + " TEXT,"
			+ PublicMsgData.MSGTYPE + " INTEGER," + PublicMsgData.TIME
			+ " TEXT" + ");";

	private static final String CREATE_ICE_BOX_SQL = "CREATE TABLE "
			+ IceBoxData.TABLE_NAME + "(" + IceBoxData._ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + IceBoxData.NAME
			+ " TEXT," + IceBoxData.TIME + " TEXT," + IceBoxData.TYPE
			+ " TEXT," + " TEXT," + IceBoxData.FOOD_PIC + " TEXT,"
			+ IceBoxData.FOOD_ID + " TEXT" + ");";

	public static final String CREATE_USER_ACCOUNT = "CREATE TABLE "
			+ UserAccountTable.TABLE_NAME + "(" + UserAccountTable._ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ UserAccountTable.USER_NAME + " TEXT," + UserAccountTable.PASSWORD
			+ " TEXT" + ");";;

	public WyySqlHelper(Context context, CursorFactory factory) {
		super(context, NAME, factory, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_COLLECT_SQL);
		db.execSQL(CREATE_SHAI_SQL);
		db.execSQL(CREATE_MSG_MOOENT_SQL);
		db.execSQL(CREATE_PUBLIC_MSG_SQL);
		db.execSQL(CREATE_ICE_BOX_SQL);
		db.execSQL(CREATE_USER_ACCOUNT);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if (oldVersion<=5) {
			UpgradeUtils.upgrade5to6(db);
		}
	}

}
