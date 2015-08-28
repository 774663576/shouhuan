package com.shouhuan.activity;

import java.util.ArrayList;
import java.util.List;

import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.listener.OnEntryClickListener;
import com.db.chart.model.Bar;
import com.db.chart.model.BarSet;
import com.db.chart.view.BarChartView;
import com.db.chart.view.XController;
import com.db.chart.view.YController;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.easing.BaseEasingMethod;
import com.db.chart.view.animation.easing.quint.QuintEaseOut;
import com.shouhuan.R;
import com.shouhuan.Util.Utils;
import com.shouhuan.data.ShouHuanData;
import com.shouhuan.data.ShouHuanDataList;
import com.shouhuan.db.DBUtils;

public class LiShiBuShu extends LiShiStep {

	private int max_step;
	private double max_juli;
	private double max_kaluli;
	private ShouHuanDataList list = new ShouHuanDataList();

	private BarChartView mBarChart, mBarChartMoth;
	private Paint mBarGridPaint;
	private TextView mBarTooltip;
	/**
	 * Bar
	 */
	private final static int BAR_MAX = 10;
	private final static int BAR_MIN = 0;
	private final static int BAR_MAX_MONTH = 10;
	private final static int BAR_MIN_MONTH = 0;
	private final static String[] barLabels = { "周一", "周二", "周三", "周四", "周五",
			"周六", "周日" };
	private final static float[][] barValues = {
			{ 6.5f, 7.5f, 3.5f, 3.5f, 10f, 4.5f, 5.5f },
			{ 0, 0, 0, 0, 0, 0, 0 } };
	private final static String[] barLabelsMoth = { "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
			"18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
			"29", "30", "31" };
	private final static float[][] barValuesMoth = {
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0 } };
	private final static String[] barLabelsMothByJuLi = { "1", "2", "3", "4",
			"5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16",
			"17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27",
			"28", "29", "30", "31" };
	private final static float[][] barValuesMothByJuLi = {
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0 } };
	private final static String[] barLabelsMothByKaLuLi = { "1", "2", "3", "4",
			"5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16",
			"17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27",
			"28", "29", "30", "31" };
	private final static float[][] barValuesMothByKaLuLi = {
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0 } };
	private final TimeInterpolator enterInterpolator = new DecelerateInterpolator(
			1.5f);
	private final TimeInterpolator exitInterpolator = new AccelerateInterpolator();
	/**
	 * Order
	 */

	private static float mCurrOverlapFactor;
	private static int[] mCurrOverlapOrder;
	private static float mOldOverlapFactor;
	private static int[] mOldOverlapOrder;
	/**
	 * Alpha
	 */
	private static int mCurrAlpha;
	private static int mOldAlpha;

	/**
	 * Ease
	 */
	private static BaseEasingMethod mCurrEasing;
	private static BaseEasingMethod mOldEasing;
	/**
	 * Enter
	 */
	private static float mCurrStartX;
	private static float mCurrStartY;
	private static float mOldStartX;
	private static float mOldStartY;

	public LiShiBuShu(LiShiFragment activity, View contentRootView) {
		super(activity, contentRootView);
		int time[] = Utils.getCurrentTime();
		list.setYear(time[0]);
		list.setMonth(time[1]);
		list.setDay(time[2]);
		list.setHour(time[3]);
		List<ShouHuanData> lists = new ArrayList<ShouHuanData>();
		list.readShouHuanDataByMoth(DBUtils.getDBsa(1));
		lists = list.getLists();
		int step;
		double distance;
		double calorie;
		for (int i = 0; i < lists.size(); i++) {
			ShouHuanData data = lists.get(i);
			step = data.getSteps();
			distance = data.getDistance();
			calorie = data.getCalorie();
			if (step > max_step) {
				max_step = step;
			}
			if (distance > max_juli) {
				max_juli = distance;
			}
			if (calorie > max_kaluli) {
				max_kaluli = calorie;
			}
			if (step > 100000) {
				step = 100000;
			}
			barValuesMoth[0][data.getDay()] = step;
			barValuesMoth[1][data.getDay()] = step;
			barValuesMothByJuLi[0][data.getDay()] = (float) distance;
			barValuesMothByJuLi[1][data.getDay()] = (float) distance;
			barValuesMothByKaLuLi[0][data.getDay()] = (float) calorie;
			barValuesMothByKaLuLi[1][data.getDay()] = (float) calorie;

		}
		updateBarChartMonth();
	}

	@Override
	public void initView() {
		mCurrOverlapFactor = 1;
		mCurrEasing = new QuintEaseOut();
		mCurrStartX = -1;
		mCurrStartY = 0;
		mCurrAlpha = -1;

		mOldOverlapFactor = 1;
		mOldEasing = new QuintEaseOut();
		mOldStartX = -1;
		mOldStartY = 0;
		mOldAlpha = -1;
		initBarChart();
		updateBarChart();
		initBarChartMonth();
		updateBarChartMonth();
	}

	@Override
	public void setListener() {

	}
	/*------------------------------------*
		 *              BARCHART              *
		 *------------------------------------*/

	private void initBarChart() {

		mBarChart = (BarChartView) findViewById(R.id.barchart);
		mBarChart.setOnEntryClickListener(barEntryListener);
		mBarChart.setOnClickListener(barClickListener);

		mBarGridPaint = new Paint();
		mBarGridPaint.setColor(mActivity.getResources().getColor(
				R.color.bar_grid));
		mBarGridPaint.setStyle(Paint.Style.STROKE);
		mBarGridPaint.setAntiAlias(true);
		mBarGridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));
	}

	private void initBarChartMonth() {
		mBarChartMoth = (BarChartView) findViewById(R.id.barchart_moth);
		mBarChartMoth.setOnEntryClickListener(barEntryListener);
		mBarChartMoth.setOnClickListener(barClickListener);

	}

	private void updateBarChart() {

		mBarChart.reset();

		BarSet barSet = new BarSet();
		Bar bar;
		for (int i = 0; i < barLabels.length; i++) {
			bar = new Bar(barLabels[i], barValues[0][i]);
			if (i == 4)
				bar.setColor(mActivity.getResources().getColor(
						R.color.bar_highest));
			else
				bar.setColor(mActivity.getResources().getColor(
						R.color.bar_fill1));
			barSet.addBar(bar);
		}
		mBarChart.addData(barSet);

		barSet = new BarSet();
		barSet.addBars(barLabels, barValues[1]);
		barSet.setColor(mActivity.getResources().getColor(R.color.bar_fill2));
		// mBarChart.addData(barSet);

		mBarChart.setSetSpacing(Tools.fromDpToPx(3));
		mBarChart.setBarSpacing(Tools.fromDpToPx(14));

		mBarChart.setBorderSpacing(0).setAxisBorderValues(BAR_MIN, BAR_MAX, 2)
				.setGrid(BarChartView.GridType.FULL, mBarGridPaint)
				.setYAxis(true).setXLabels(XController.LabelPosition.OUTSIDE)
				.setYLabels(YController.LabelPosition.OUTSIDE)
				.show(getAnimation(true));

	}

	private void updateBarChartMonth() {

		mBarChartMoth.reset();

		BarSet barSet = new BarSet();
		Bar bar;
		for (int i = 0; i < barLabelsMoth.length; i++) {
			bar = new Bar(barLabelsMoth[i], barValuesMoth[0][i]);
			if (i == 4)
				bar.setColor(mActivity.getResources().getColor(
						R.color.bar_highest));
			else
				bar.setColor(mActivity.getResources().getColor(
						R.color.bar_fill1));
			barSet.addBar(bar);
		}
		mBarChartMoth.addData(barSet);

		barSet = new BarSet();
		barSet.addBars(barLabelsMoth, barValuesMoth[1]);
		barSet.setColor(mActivity.getResources().getColor(R.color.bar_fill2));
		// mBarChart.addData(barSet);

		mBarChartMoth.setSetSpacing(Tools.fromDpToPx(3));
		mBarChartMoth.setBarSpacing(Tools.fromDpToPx(14));

		mBarChartMoth.setBorderSpacing(0)
				.setAxisBorderValues(BAR_MIN_MONTH, BAR_MAX_MONTH, 2)
				.setGrid(BarChartView.GridType.FULL, mBarGridPaint)
				.setYAxis(true).setXLabels(XController.LabelPosition.OUTSIDE)
				.setYLabels(YController.LabelPosition.OUTSIDE)
				.show(getAnimation(true));

	}

	@SuppressLint("NewApi")
	private void showBarTooltip(int setIndex, int entryIndex, Rect rect) {

		mBarTooltip = (TextView) mActivity.getActivity().getLayoutInflater()
				.inflate(R.layout.bar_tooltip, null);
		mBarTooltip.setText(Integer
				.toString((int) barValues[setIndex][entryIndex]));

		LayoutParams layoutParams = new LayoutParams(rect.width(),
				rect.height());
		layoutParams.leftMargin = rect.left;
		layoutParams.topMargin = rect.top;
		mBarTooltip.setLayoutParams(layoutParams);

		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			mBarTooltip.setAlpha(0);
			mBarTooltip.setScaleY(0);
			mBarTooltip.animate().setDuration(200).alpha(1).scaleY(1)
					.setInterpolator(enterInterpolator);
		}

		mBarChart.showTooltip(mBarTooltip);
	}

	@SuppressLint("NewApi")
	private void dismissBarTooltip(final int setIndex, final int entryIndex,
			final Rect rect) {

		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			mBarTooltip.animate().setDuration(100).scaleY(0).alpha(0)
					.setInterpolator(exitInterpolator)
					.withEndAction(new Runnable() {
						@Override
						public void run() {
							mBarChart.removeView(mBarTooltip);
							mBarTooltip = null;
							if (entryIndex != -1)
								showBarTooltip(setIndex, entryIndex, rect);
						}
					});
		} else {
			mBarChart.dismissTooltip(mBarTooltip);
			mBarTooltip = null;
			if (entryIndex != -1)
				showBarTooltip(setIndex, entryIndex, rect);
		}
	}

	private void updateValues(BarChartView chartView) {

		chartView.updateValues(0, barValues[1]);
		chartView.updateValues(1, barValues[0]);
		chartView.notifyDataUpdate();
	}

	private final OnEntryClickListener barEntryListener = new OnEntryClickListener() {
		@Override
		public void onClick(int setIndex, int entryIndex, Rect rect) {
			if (mBarTooltip == null)
				showBarTooltip(setIndex, entryIndex, rect);
			else
				dismissBarTooltip(setIndex, entryIndex, rect);
		}
	};

	private final OnClickListener barClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mBarTooltip != null)
				dismissBarTooltip(-1, -1, null);
		}
	};

	/*------------------------------------*
	 *               GETTERS              *
	 *------------------------------------*/

	private Animation getAnimation(boolean newAnim) {
		if (newAnim)
			return new Animation().setAlpha(mCurrAlpha).setEasing(mCurrEasing)
					.setOverlap(mCurrOverlapFactor, mCurrOverlapOrder)
					.setStartPoint(mCurrStartX, mCurrStartY);
		else
			return new Animation().setAlpha(mOldAlpha).setEasing(mOldEasing)
					.setOverlap(mOldOverlapFactor, mOldOverlapOrder)
					.setStartPoint(mOldStartX, mOldStartY);
	}

}
