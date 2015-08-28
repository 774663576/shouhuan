package com.shouhuan.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

import com.shouhuan.Util.CrashHandler;

public class MyApplation extends Application {
	private static MyApplation instance;

	private static List<Activity> activityList = new ArrayList<Activity>();

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		// CrashHandler catchHandler = CrashHandler.getInstance();
		// catchHandler.init(this);

	}

	public static MyApplation getInstance() {
		return instance;
	}

	// ���Activity��������
	public static void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// ��������Activity��finish
	public static void exit(boolean flag) {
		for (int i = 0; i < activityList.size(); i++) {
			Activity activity = activityList.get(i);
			if (activity != null) {
				activity.finish();
			}
		}
		activityList.clear();
		if (flag) {
			System.exit(0);
		}

	}

}
