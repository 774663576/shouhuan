package com.shouhuan.activity;

import java.util.ArrayList;
import java.util.List;

import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

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

public class LiShiFragment extends Fragment implements OnClickListener {
	private ImageView img_bushu;
	private ImageView img_kaluli;
	private ImageView img_time;
	private ImageView img_juli;
	private int max_step;
	private double max_juli;
	private double max_kaluli;
	private int max_step_week;
	private double max_juli_week;
	private double max_kaluli_week;
	private ShouHuanDataList list = new ShouHuanDataList();

	private BarChartView mBarChart, mBarChartMoth;
	private Paint mBarGridPaint;
	private TextView mBarTooltip;
	/**
	 * Bar
	 */
	private int BAR_MAX = 10;
	private int BAR_MIN = 0;
	private int BAR_STEP = 1;
	private int BAR_MAX_MONTH = 100;
	private int BAR_MIN_MONTH = 0;
	private int BAR_STEP_MONTH = 10;
	private String[] barLabels = { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };
	private float[][] barValues = { { 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0 } };
	private float[][] barValues_bushu = { { 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0 } };
	private float[][] barValues_juli = { { 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0 } };
	private float[][] barValues_kaluli = { { 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0 } };
	private String[] barLabelsMoth = { "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
			"20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
			"31" };
	private float[][] barValuesMoth = {
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0 } };

	private float[][] barValuesMoth_BuShu = {
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0 } };

	private float[][] barValuesMoth_JuLi = {
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0 } };
	private float[][] barValuesMoth_KaLuLi = {
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0 } };
	private TimeInterpolator enterInterpolator = new DecelerateInterpolator(
			1.5f);
	private TimeInterpolator exitInterpolator = new AccelerateInterpolator();
	/**
	 * Order
	 */

	private float mCurrOverlapFactor;
	private int[] mCurrOverlapOrder;
	private float mOldOverlapFactor;
	private int[] mOldOverlapOrder;
	/**
	 * Alpha
	 */
	private int mCurrAlpha;
	private int mOldAlpha;

	/**
	 * Ease
	 */
	private BaseEasingMethod mCurrEasing;
	private BaseEasingMethod mOldEasing;
	/**
	 * Enter
	 */
	private float mCurrStartX;
	private float mCurrStartY;
	private float mOldStartX;
	private float mOldStartY;

	private int week_index = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.lishi_fragment_layout, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		int day_of_week[] = Utils.getDayOfWeek();
		int monday = day_of_week[0];
		int sunday = day_of_week[1];
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
			if (distance > 100000) {
				distance = 100000;
			}
			if (calorie > 100000) {
				calorie = 100000;
			}
			if (data.getDay() >= monday && data.getDay() <= sunday) {
				if (step > max_step_week) {
					max_step_week = step;
				}
				if (distance > max_juli_week) {
					max_juli_week = distance;
				}
				if (calorie > max_kaluli_week) {
					max_kaluli_week = calorie;
				}
				barValues_bushu[0][week_index] = step;
				barValues_juli[0][week_index] = (float) distance;
				barValues_kaluli[0][week_index] = (float) calorie;
				week_index++;
			}
			barValuesMoth_BuShu[0][data.getDay() - 1] = step;
			barValuesMoth_JuLi[0][data.getDay() - 1] = (float) distance;
			barValuesMoth_KaLuLi[0][data.getDay() - 1] = (float) calorie;

		}
		if (max_step > 100000) {
			max_step = 100000;
		}
		getLineMaxAndStep(max_step);
		getLineMaxAndStepByWeek(max_step_week);
		barValues = barValues_bushu;
		barValuesMoth = barValuesMoth_BuShu;
		initView();
	}

	private void initView() {
		img_bushu = (ImageView) getView().findViewById(R.id.img_bushu);
		img_kaluli = (ImageView) getView().findViewById(R.id.img_kaluli);
		img_time = (ImageView) getView().findViewById(R.id.img_time);
		img_juli = (ImageView) getView().findViewById(R.id.img_juli);
		img_bushu.setSelected(true);
		setListener();
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

	private void setListener() {
		img_bushu.setOnClickListener(this);
		img_kaluli.setOnClickListener(this);
		img_time.setOnClickListener(this);
		img_juli.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_bushu:
			img_bushu.setSelected(true);
			img_kaluli.setSelected(false);
			img_time.setSelected(false);
			img_juli.setSelected(false);
			barValues = barValues_bushu;
			barValuesMoth = barValuesMoth_BuShu;
			getLineMaxAndStep(max_step);
			getLineMaxAndStepByWeek(max_step_week);
			updateBarChart();
			updateBarChartMonth();
			break;
		case R.id.img_kaluli:
			img_bushu.setSelected(false);
			img_kaluli.setSelected(true);
			img_time.setSelected(false);
			img_juli.setSelected(false);
			barValues = barValues_kaluli;
			barValuesMoth = barValuesMoth_KaLuLi;
			getLineMaxAndStep((int) max_kaluli);
			getLineMaxAndStepByWeek((int) max_kaluli_week);
			updateBarChart();
			updateBarChartMonth();
			break;
		case R.id.img_time:
			img_bushu.setSelected(false);
			img_kaluli.setSelected(false);
			img_time.setSelected(true);
			img_juli.setSelected(false);
			break;
		case R.id.img_juli:
			img_bushu.setSelected(false);
			img_kaluli.setSelected(false);
			img_time.setSelected(false);
			img_juli.setSelected(true);
			barValues = barValues_juli;
			barValuesMoth = barValuesMoth_JuLi;
			getLineMaxAndStep((int) max_juli);
			getLineMaxAndStepByWeek((int) max_juli_week);
			updateBarChart();
			updateBarChartMonth();
			break;
		default:
			break;
		}
	}

	/*------------------------------------*
	 *              BARCHART              *
	 *------------------------------------*/

	private void initBarChart() {

		mBarChart = (BarChartView) getView().findViewById(R.id.barchart);
		mBarChart.setOnEntryClickListener(barEntryListener);
		mBarChart.setOnClickListener(barClickListener);

		mBarGridPaint = new Paint();
		mBarGridPaint.setColor(getActivity().getResources().getColor(
				R.color.white));

		mBarGridPaint.setStyle(Paint.Style.STROKE);
		mBarGridPaint.setAntiAlias(true);
		mBarGridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));
	}

	private void initBarChartMonth() {
		mBarChartMoth = (BarChartView) getView().findViewById(
				R.id.barchart_moth);
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
				bar.setColor(getActivity().getResources().getColor(
						R.color.yellow));
			else
				bar.setColor(getActivity().getResources().getColor(
						R.color.yellow));
			barSet.addBar(bar);
		}
		mBarChart.addData(barSet);

		barSet = new BarSet();
		barSet.addBars(barLabels, barValues[1]);
		barSet.setColor(getActivity().getResources().getColor(R.color.yellow));
		// mBarChart.addData(barSet);

		mBarChart.setSetSpacing(Tools.fromDpToPx(3));
		mBarChart.setBarSpacing(Tools.fromDpToPx(14));

		mBarChart.setBorderSpacing(0)
				.setAxisBorderValues(BAR_MIN, BAR_MAX, BAR_STEP)
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
				bar.setColor(getActivity().getResources().getColor(
						R.color.yellow));
			else
				bar.setColor(getActivity().getResources().getColor(
						R.color.yellow));
			barSet.addBar(bar);
		}
		mBarChartMoth.addData(barSet);

		barSet = new BarSet();
		barSet.addBars(barLabelsMoth, barValuesMoth[1]);
		barSet.setColor(getActivity().getResources().getColor(R.color.yellow));
		// mBarChart.addData(barSet);

		mBarChartMoth.setSetSpacing(Tools.fromDpToPx(3));
		mBarChartMoth.setBarSpacing(Tools.fromDpToPx(14));

		mBarChartMoth
				.setBorderSpacing(0)
				.setAxisBorderValues(BAR_MIN_MONTH, BAR_MAX_MONTH,
						BAR_STEP_MONTH)
				.setGrid(BarChartView.GridType.FULL, mBarGridPaint)
				.setYAxis(true).setXLabels(XController.LabelPosition.OUTSIDE)
				.setYLabels(YController.LabelPosition.OUTSIDE)
				.show(getAnimation(true));

	}

	@SuppressLint("NewApi")
	private void showBarTooltip(int setIndex, int entryIndex, Rect rect) {
		mBarTooltip = (TextView) getActivity().getLayoutInflater().inflate(
				R.layout.bar_tooltip, null);
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

	private void getLineMaxAndStep(int max) {
		if (max <= 10) {
			BAR_MAX_MONTH = 10;
			BAR_STEP_MONTH = 1;
		} else if (max <= 100) {
			BAR_MAX_MONTH = 100;
			BAR_STEP_MONTH = 10;
		} else if (max <= 1000) {
			BAR_MAX_MONTH = 1000;
			BAR_STEP_MONTH = 100;
		} else if (max <= 10000) {
			BAR_MAX_MONTH = 10000;
			BAR_STEP_MONTH = 1000;
		} else if (max <= 100000) {
			BAR_MAX_MONTH = 100000;
			BAR_STEP_MONTH = 10000;
		}
	}

	private void getLineMaxAndStepByWeek(int max) {
		if (max <= 10) {
			BAR_MAX = 10;
			BAR_STEP = 1;
		} else if (max <= 100) {
			BAR_MAX = 100;
			BAR_STEP = 10;
		} else if (max <= 1000) {
			BAR_MAX = 1000;
			BAR_STEP = 100;
		} else if (max <= 10000) {
			BAR_MAX = 10000;
			BAR_STEP = 1000;
		} else if (max <= 100000) {
			BAR_MAX = 100000;
			BAR_STEP = 10000;
		}
	}

}
