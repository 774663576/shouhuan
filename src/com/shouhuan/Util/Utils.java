package com.shouhuan.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;

import com.shouhuan.R;

public class Utils {
	// �����л����������ұ߽��룬����˳�

	public static void leftOutRightIn(Context context) {
		((Activity) context).overridePendingTransition(R.anim.in_from_right,
				R.anim.out_to_left);
	}

	/**
	 * �Ҳ��˳�
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
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // ��ȡ����һ������
		String day_of_monday = df.format(cal.getTime());
		// ������������ϸ��������յ����ڣ���Ϊ�����Ǳ߰����յ��ɵ�һ��
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		// ����һ�����ڣ����������й������ı����յ�����
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		String day_of_sunday = df.format(cal.getTime());
		return new int[] { Integer.valueOf(day_of_monday),
				Integer.valueOf(day_of_sunday) };

	}
}
