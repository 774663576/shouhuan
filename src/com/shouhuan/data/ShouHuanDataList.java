package com.shouhuan.data;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shouhuan.db.Const;

public class ShouHuanDataList {
	private List<ShouHuanData> lists = new ArrayList<ShouHuanData>();
	private int year;
	private int month;
	private int day;
	private int hour;

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

	public List<ShouHuanData> getLists() {
		return lists;
	}

	public void setLists(List<ShouHuanData> lists) {
		this.lists = lists;
	}

	public void readShouHuanData(SQLiteDatabase db) {
		Cursor cursor = db.query(Const.SHOUHUAN_TABLE_NAME,
				new String[] { "year", "month", "day", "hour", "steps",
						"calorie", "distance" },
				"year=? and month=? and day=? ", new String[] { this.year + "",
						this.month + "", this.day + "" }, null, null, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			for (int j = 0; j < cursor.getCount(); j++) {
				int year = cursor.getInt(cursor.getColumnIndex("year"));
				int month = cursor.getInt(cursor.getColumnIndex("month"));
				int day = cursor.getInt(cursor.getColumnIndex("day"));
				int hour = cursor.getInt(cursor.getColumnIndex("hour"));
				int steps = cursor.getInt(cursor.getColumnIndex("steps"));
				double calorie = cursor.getDouble(cursor
						.getColumnIndex("calorie"));
				double distance = cursor.getDouble(cursor
						.getColumnIndex("distance"));
				ShouHuanData data = new ShouHuanData();
				data.setCalorie(calorie);
				data.setDay(day);
				data.setDistance(distance);
				data.setHour(hour);
				data.setMonth(month);
				data.setSteps(steps);
				data.setYear(year);
				lists.add(data);
				cursor.moveToNext();
			}
			cursor.close();
		}
	}

	public void readShouHuanDataByMoth(SQLiteDatabase db) {
		// Cursor cursor = db.query(Const.SHOUHUAN_TABLE_NAME,
		// new String[] { "year", "month", "day", "hour", "steps",
		// "calorie", "distance" }, "year=? and month=?",
		// new String[] { this.year + "", this.month + "" }, null, null,
		// "day asc");
		Cursor cursor = db
				.rawQuery(
						"select * ,sum(steps) steps,sum(calorie) calorie,sum(distance) distance from shouhuan where year=? and month=? group by day order by day",
						new String[] { this.year + "", this.month + "" });
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			for (int j = 0; j < cursor.getCount(); j++) {
				int year = cursor.getInt(cursor.getColumnIndex("year"));
				int month = cursor.getInt(cursor.getColumnIndex("month"));
				int day = cursor.getInt(cursor.getColumnIndex("day"));
				int hour = cursor.getInt(cursor.getColumnIndex("hour"));
				int steps = cursor.getInt(cursor.getColumnIndex("steps"));
				double calorie = cursor.getDouble(cursor
						.getColumnIndex("calorie"));
				double distance = cursor.getDouble(cursor
						.getColumnIndex("distance"));
				ShouHuanData data = new ShouHuanData();
				data.setCalorie(calorie);
				data.setDay(day);
				data.setDistance(distance);
				data.setHour(hour);
				data.setMonth(month);
				data.setSteps(steps);
				data.setYear(year);
				lists.add(data);
				cursor.moveToNext();
			}
			cursor.close();
		}
	}
}
