package com.shouhuan.data;

import java.io.Serializable;

import android.database.sqlite.SQLiteDatabase;

public abstract class AbstractData implements IData, Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Override
	public void read(SQLiteDatabase db) {
		return;
	}

	@Override
	public void write(SQLiteDatabase db) {
		return;
	}

	@Override
	public void update(SQLiteDatabase db) {
		return;
	}

}
