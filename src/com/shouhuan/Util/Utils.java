package com.shouhuan.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;

import com.shouhuan.R;

public class Utils {
	// 设置切换动画，从右边进入，左边退出

	public static void leftOutRightIn(Context context) {
		((Activity) context).overridePendingTransition(R.anim.in_from_right,
				R.anim.out_to_left);
	}

	/**
	 * 右侧退出
	 * 
	 * @param context
	 */
	public static void rightOut(Context context) {
		((Activity) context).overridePendingTransition(R.anim.right_in,
				R.anim.right_out);

	}

	public static int[] getCurrentTime() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		return new int[] { year, month + 1, day, hour };
	}

	public static int[] getDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd");
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 获取本周一的日期
		String day_of_monday = df.format(cal.getTime());
		// 这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		// 增加一个星期，才是我们中国人理解的本周日的日期
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		String day_of_sunday = df.format(cal.getTime());
		return new int[] { Integer.valueOf(day_of_monday),
				Integer.valueOf(day_of_sunday) };

	}
}
