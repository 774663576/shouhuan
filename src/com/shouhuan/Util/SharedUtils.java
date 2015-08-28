package com.shouhuan.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.shouhuan.activity.MyApplation;

/**
 * * SharedPreferences 的公具类
 * 
 * @author teeker_bin
 * 
 */
public class SharedUtils {
	private static final String SP_NAME = "tf";
	private static SharedPreferences sharedPreferences = MyApplation
			.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
	private static Editor editor = sharedPreferences.edit();
	public static final String SP_UID = "uid";

	public static String getString(String key, String defaultValue) {
		return sharedPreferences.getString(key, defaultValue);
	}

	public static int getInt(String key, int defaultValue) {
		return sharedPreferences.getInt(key, defaultValue);
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		return sharedPreferences.getBoolean(key, defaultValue);
	}

	public static void setString(String key, String value) {
		editor.putString(key, value);
		editor.commit();

	}

	public static long getLong(String key, long defaultValue) {
		return sharedPreferences.getLong(key, defaultValue);

	}

	public static void setLong(String key, long value) {
		editor.putLong(key, value);
		editor.commit();
	}

	public static void setInt(String key, int value) {
		editor.putInt(key, value);
		editor.commit();
	}

	public static void setBoolean(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void setUid(String uid) {
		setString(SP_UID, uid);
	}

	public static String getUid() {
		return getString(SP_UID, "0");
	}

	public static int getIntUid() {
		String uid = getUid();
		if (uid.length() > 0) {
			return Integer.parseInt(uid);
		}
		return 0;
	}

	public static void settingNewVersion(boolean isNewVersion) {
		editor.putBoolean("new_version", isNewVersion);
		editor.commit();
	}

	public static boolean getNewVersion() {
		return sharedPreferences.getBoolean("new_version", false);

	}

	public static void setConnectState(boolean isNewVersion) {
		editor.putBoolean("connect_state", isNewVersion);
		editor.commit();
	}

	public static boolean getConnectState() {
		return sharedPreferences.getBoolean("connect_state", false);

	}

	public static void setPhoneState(boolean state) {
		editor.putBoolean("phone_state", state);
		editor.commit();
	}

	public static boolean getPhoneState() {
		return sharedPreferences.getBoolean("phone_state", false);

	}

	public static void setSMSState(boolean state) {
		editor.putBoolean("sms_state", state);
		editor.commit();
	}

	public static boolean getSMSState() {
		return sharedPreferences.getBoolean("sms_state", false);

	}

	public static void clearData() {
		editor.clear();
		editor.commit();
	}
}
