package com.shouhuan.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.shouhuan.Util.SharedUtils;
import com.shouhuan.activity.MyApplation;

public class DataBaseHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "tf";
	private static DataBaseHelper instance;
	private static final int DATABASE_VERSION_1 = 1;
	private static final int DATABASE_VERSION = DATABASE_VERSION_1;

	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public static DataBaseHelper getInstance() {
		return getInstance(MyApplation.getInstance());
	}

	public static DataBaseHelper getInstance(Context context) {
		String uid = SharedUtils.getUid();
		return getInstance(context, uid);
	}

	public static DataBaseHelper getInstance(Context context, String postfix) {
		if (instance == null) {
			instance = new DataBaseHelper(context, DATABASE_NAME + postfix,
					null, DATABASE_VERSION);

		}
		return instance;
	}

	public static void setIinstanceNull() {
		instance = null;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createDB(db);
	}

	private void createDB(SQLiteDatabase db) {
		db.execSQL("create table IF NOT EXISTS " + Const.SHOUHUAN_TABLE_NAME
				+ "( " + Const.SHOUHUAN_TABLE_STRUCTURE + " )");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
