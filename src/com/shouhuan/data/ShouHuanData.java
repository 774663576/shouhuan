package com.shouhuan.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shouhuan.db.Const;

public class ShouHuanData extends AbstractData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int year;
	private int month;
	private int day;
	private int hour;
	private int steps;// 步数
	private double calorie;// 卡路里
	private double distance;// 距离
	private int time;// 距离

	@Override
	public String toString() {
		return "time:" + year + "-" + month + "-" + day + " :" + hour
				+ "  step:" + steps;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public double getCalorie() {
		return calorie;
	}

	public void setCalorie(double calorie) {
		this.calorie = calorie;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getCurrentHourCount(SQLiteDatabase db) {
		Cursor cursor = db
				.rawQuery(
						"select * from shouhuan where year=? and month=? and day=? and hour=?",
						new String[] { this.year + "", this.month + "",
								this.day + "", this.hour + "" });
		if (cursor == null) {
			return 0;
		}
		return cursor.getCount();
	}

	@Override
	public void write(SQLiteDatabase db) {
		String dbName = Const.SHOUHUAN_TABLE_NAME;
		ContentValues cv = new ContentValues();
		cv.put("year", this.year);
		cv.put("month", this.month);
		cv.put("day", this.day);
		cv.put("hour", this.hour);
		cv.put("steps", this.steps);
		cv.put("calorie", this.calorie);
		cv.put("distance", this.distance);
		cv.put("time", this.time);
		db.insert(dbName, null, cv);
	}

	@Override
	public void read(SQLiteDatabase db) {
		Cursor cursor = db.query(Const.SHOUHUAN_TABLE_NAME, new String[] {
				"year", "month", "day", "hour", "steps", "calorie", "distance",
				"time" }, "year=? and month=? and day=? ", new String[] {
				this.year + "", this.month + "", this.day + "" }, null, null,
				null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			this.year = cursor.getInt(cursor.getColumnIndex("year"));
			this.month = cursor.getInt(cursor.getColumnIndex("month"));
			this.day = cursor.getInt(cursor.getColumnIndex("day"));
			this.hour = cursor.getInt(cursor.getColumnIndex("hour"));
			this.steps = cursor.getInt(cursor.getColumnIndex("steps"));
			this.calorie = cursor.getDouble(cursor.getColumnIndex("calorie"));
			this.distance = cursor.getDouble(cursor.getColumnIndex("distance"));
			this.time = cursor.getInt(cursor.getColumnIndex("time"));

		}
	}

	@Override
	public void update(SQLiteDatabase db) {
		String dbName = Const.SHOUHUAN_TABLE_NAME;
		ContentValues cv = new ContentValues();
		cv.put("year", this.year);
		cv.put("month", this.month);
		cv.put("day", this.day);
		cv.put("hour", this.hour);
		cv.put("steps", this.steps);
		cv.put("calorie", this.calorie);
		cv.put("distance", this.distance);
		cv.put("time", this.time);
		db.update(dbName, cv, "year=? and month=? and day=? and hour=?",
				new String[] { this.year + "", this.month + "", this.day + "",
						this.hour + "" });
	}
}
