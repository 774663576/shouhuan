package com.shouhuan.activity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.Image;
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
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.listener.OnEntryClickListener;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.db.chart.view.XController;
import com.db.chart.view.YController;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.easing.BaseEasingMethod;
import com.db.chart.view.animation.easing.quint.QuintEaseOut;
import com.db.chart.view.animation.style.DashAnimation;
import com.shouhuan.R;
import com.shouhuan.Util.SharedUtils;
import com.shouhuan.Util.Utils;
import com.shouhuan.data.ShouHuanData;
import com.shouhuan.data.ShouHuanDataList;
import com.shouhuan.db.DBUtils;

public class YunDongFragment extends Fragment implements OnClickListener {

	private ImageView img_step;
	private ImageView img_time;
	private ImageView img_kaluli;
	private ImageView img_juli;

	private TextView txt_step, txt_time, txt_kaluli, txt_juli;

	private ImageView current_img;
	private TextView txt_current_danwei;
	private TextView txt_current_count;

	private TextView txt_tishi;
	private ImageView img_tishi;

	private TextView txt_connect_state;

	private int all_step;
	private int all_time;
	private double all_kaluli;
	private double all_juli;
	private ShouHuanDataList list = new ShouHuanDataList();
	private final TimeInterpolator enterInterpolator = new DecelerateInterpolator(
			1.5f);
	private final TimeInterpolator exitInterpolator = new AccelerateInterpolator();
	/**
	 * Line
	 */
	private int LINE_MAX = 1000;
	private int LINE_MIN = 0;
	private int LINE_STEP = 100;

	private int max_step;
	private double max_juli;
	private double max_kaluli;
	private int max_time;

	private final static String[] lineLabels = { "0:00", "1:00", "2:00",
			"3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00", "10:00",
			"11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00",
			"18:00", "19:00", "20:00", "21:00" };
	private float[][] lineValues = {
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
	private float[][] stepValues = {
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
	private float[][] juliValues = {
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
	private float[][] kaluliValues = {
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
	private float[][] timeValues = {
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
	private static LineChartView mLineChart;
	private Paint mLineGridPaint;
	private TextView mLineTooltip;
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
	private final Runnable mEnterEndAction = new Runnable() {
		@Override
		public void run() {
			// mPlayBtn.setEnabled(true);
		}
	};
	private final OnEntryClickListener lineEntryListener = new OnEntryClickListener() {
		@Override
		public void onClick(int setIndex, int entryIndex, Rect rect) {

			if (mLineTooltip == null)
				showLineTooltip(setIndex, entryIndex, rect);
			else
				dismissLineTooltip(setIndex, entryIndex, rect);
		}
	};

	private final OnClickListener lineClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mLineTooltip != null)
				dismissLineTooltip(-1, -1, null);
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.yundong_layout, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		int time[] = Utils.getCurrentTime();
		list.setYear(time[0]);
		list.setMonth(time[1]);
		list.setDay(time[2]);
		list.setHour(time[3]);
		List<ShouHuanData> lists = new ArrayList<ShouHuanData>();
		list.readShouHuanData(DBUtils.getDBsa(1));
		lists = list.getLists();
		int step;
		double distance;
		double calorie;
		int time_;
		for (int i = 0; i < lists.size(); i++) {
			ShouHuanData data = lists.get(i);
			step = data.getSteps();
			distance = data.getDistance();
			calorie = data.getCalorie();
			time_ = data.getTime();
			if (step > max_step) {
				max_step = step;
			}
			if (distance > max_juli) {
				max_juli = distance;
			}
			if (calorie > max_kaluli) {
				max_kaluli = calorie;
			}
			if (time_ > max_time) {
				max_time = time_;
			}
			stepValues[0][data.getHour()] = step;
			stepValues[1][data.getHour()] = step;
			juliValues[0][data.getHour()] = (float) distance;
			juliValues[1][data.getHour()] = (float) distance;
			kaluliValues[0][data.getHour()] = (float) calorie;
			kaluliValues[1][data.getHour()] = (float) calorie;
			timeValues[0][data.getHour()] = time_;
			timeValues[1][data.getHour()] = time_;
			all_step += data.getSteps();
			all_juli += data.getDistance();
			all_kaluli += data.getCalorie();
			all_time += time_;
		}
		lineValues = stepValues;
		getLineMaxAndStep(max_step);
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

		initView();
		initLineChart();
		updateLineChart();

	}

	private void initView() {
		txt_connect_state = (TextView) getView().findViewById(
				R.id.txt_connect_state);
		setConnectState();
		img_tishi = (ImageView) getView().findViewById(R.id.img_tishi);
		txt_tishi = (TextView) getView().findViewById(R.id.txt_tishi);
		img_tishi.setImageResource(R.drawable.step_small);
		txt_tishi.setText("步数/步");
		current_img = (ImageView) getView().findViewById(R.id.current_img);
		current_img.setImageResource(R.drawable.steps_big);
		txt_current_count = (TextView) getView().findViewById(
				R.id.txt_current_count);
		txt_current_count.setText(all_step + "");
		txt_current_danwei = (TextView) getView().findViewById(
				R.id.txt_current_danwei);
		txt_current_danwei.setText("步数");
		txt_step = (TextView) getView().findViewById(R.id.txt_step);
		txt_kaluli = (TextView) getView().findViewById(R.id.txt_kaluli);
		txt_juli = (TextView) getView().findViewById(R.id.txt_juli);
		txt_time = (TextView) getView().findViewById(R.id.txt_time);
		img_juli = (ImageView) getView().findViewById(R.id.img_juli);
		img_kaluli = (ImageView) getView().findViewById(R.id.img_kaluli);
		img_step = (ImageView) getView().findViewById(R.id.img_step);
		img_step.setSelected(true);
		txt_step.setSelected(true);
		img_time = (ImageView) getView().findViewById(R.id.img_time);
		txt_step.setText(all_step + "");
		BigDecimal b = new BigDecimal(all_juli);
		double df = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		txt_juli.setText(df + "");
		b = new BigDecimal(all_kaluli);
		df = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		txt_kaluli.setText(df + "");
		txt_time.setText(all_time + "");
		setListener();
	}

	public void setConnectState() {
		if (SharedUtils.getConnectState()) {
			txt_connect_state.setText("已连接");
		} else {
			txt_connect_state.setText("已断开");
		}
	}

	private void setListener() {
		img_juli.setOnClickListener(this);
		img_kaluli.setOnClickListener(this);
		img_step.setOnClickListener(this);
		img_time.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_juli:
			img_juli.setSelected(true);
			img_time.setSelected(false);
			img_kaluli.setSelected(false);
			img_step.setSelected(false);
			txt_juli.setSelected(true);
			txt_time.setSelected(false);
			txt_kaluli.setSelected(false);
			txt_step.setSelected(false);
			getLineMaxAndStep((int) max_juli);
			lineValues = juliValues;
			updateLineChart();
			current_img.setImageResource(R.drawable.distance_big);
			BigDecimal b = new BigDecimal(all_juli);
			double df = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			txt_current_count.setText(df + "");
			txt_current_danwei.setText("距离");
			img_tishi.setImageResource(R.drawable.distance_small);
			txt_tishi.setText("距离/米");
			break;
		case R.id.img_step:
			img_juli.setSelected(false);
			img_time.setSelected(false);
			img_kaluli.setSelected(false);
			img_step.setSelected(true);

			txt_step.setSelected(true);
			txt_juli.setSelected(false);
			txt_kaluli.setSelected(false);
			txt_time.setSelected(false);
			getLineMaxAndStep(max_step);
			lineValues = stepValues;
			updateLineChart();
			txt_current_count.setText(all_step + "");
			txt_current_danwei.setText("步数");
			img_tishi.setImageResource(R.drawable.step_small);
			txt_tishi.setText("步数/步");
			break;
		case R.id.img_kaluli:
			img_juli.setSelected(false);
			img_time.setSelected(false);
			img_kaluli.setSelected(true);
			img_step.setSelected(false);

			txt_step.setSelected(false);
			txt_juli.setSelected(false);
			txt_kaluli.setSelected(true);
			txt_time.setSelected(false);
			getLineMaxAndStep((int) max_kaluli);
			lineValues = kaluliValues;
			updateLineChart();
			current_img.setImageResource(R.drawable.calory_big);
			BigDecimal bb = new BigDecimal(all_kaluli);
			double dff = bb.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			txt_current_count.setText(dff + "");
			txt_current_danwei.setText("卡路里");
			img_tishi.setImageResource(R.drawable.calories_small);
			txt_tishi.setText("卡路里/千卡");
			break;
		case R.id.img_time:
			img_juli.setSelected(false);
			img_time.setSelected(true);
			img_kaluli.setSelected(false);
			img_step.setSelected(false);
			txt_step.setSelected(false);
			txt_juli.setSelected(false);
			txt_kaluli.setSelected(false);
			txt_time.setSelected(true);
			getLineMaxAndStep(max_time);
			lineValues = timeValues;
			updateLineChart();
			txt_current_count.setText(all_time + "");
			txt_current_danwei.setText("运动时间");
			img_tishi.setImageResource(R.drawable.time_small);
			txt_tishi.setText("运动时间/分");
			break;
		default:
			break;
		}
	}

	private void getLineMaxAndStep(int max) {
		if (max < 10) {
			LINE_MAX = 10;
			LINE_STEP = 1;
		} else if (max < 100) {
			LINE_MAX = 100;
			LINE_STEP = 10;
		} else if (max < 1000) {
			LINE_MAX = 1000;
			LINE_STEP = 100;
		} else if (max < 10000) {
			LINE_MAX = 10000;
			LINE_STEP = 1000;
		} else if (max < 100000) {
			LINE_MAX = 100000;
			LINE_STEP = 10000;
		}
	}

	/*------------------------------------*
	 *              LINECHART             *
	 *------------------------------------*/

	private void initLineChart() {

		mLineChart = (LineChartView) getView().findViewById(R.id.linechart);
		// mLineChart.setOnEntryClickListener(lineEntryListener);
		// mLineChart.setOnClickListener(lineClickListener);

		mLineGridPaint = new Paint();
		mLineGridPaint.setColor(this.getResources().getColor(R.color.yellow));
		mLineGridPaint
				.setPathEffect(new DashPathEffect(new float[] { 5, 5 }, 0));
		mLineGridPaint.setStyle(Paint.Style.STROKE);
		mLineGridPaint.setAntiAlias(true);
		mLineGridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));
	}

	private void updateLineChart() {

		mLineChart.reset();

		LineSet dataSet = new LineSet();
		dataSet.addPoints(lineLabels, lineValues[0]);
		dataSet.setDots(true)
				.setDotsColor(this.getResources().getColor(R.color.white))
				.setDotsRadius(Tools.fromDpToPx(2))
				.setDotsStrokeThickness(Tools.fromDpToPx(1))
				.setDotsStrokeColor(
						this.getResources().getColor(R.color.yellow))
				.setLineColor(this.getResources().getColor(R.color.yellow))
				.setLineThickness(Tools.fromDpToPx(1)).beginAt(0)
				.endAt(lineLabels.length);
		mLineChart.addData(dataSet);

		dataSet = new LineSet();
		dataSet.addPoints(lineLabels, lineValues[1]);
		dataSet.setLineColor(this.getResources().getColor(R.color.yellow))
				.setLineThickness(Tools.fromDpToPx(1)).setSmooth(false)
				.setDashed(false);
		mLineChart.addData(dataSet);

		mLineChart.setBorderSpacing(Tools.fromDpToPx(4))
				.setGrid(LineChartView.GridType.HORIZONTAL, mLineGridPaint)
				.setXAxis(false).setXLabels(XController.LabelPosition.OUTSIDE)
				.setLabelColor(this.getResources().getColor(R.color.black))
				.setYAxis(false).setYLabels(YController.LabelPosition.OUTSIDE)
				.setAxisBorderValues(LINE_MIN, LINE_MAX, LINE_STEP)
				.setLabelsFormat(new DecimalFormat("##'.0'"))
				.show(getAnimation(true).setEndAction(mEnterEndAction));
		mLineChart.animateSet(1, new DashAnimation());
	}

	@SuppressLint("NewApi")
	private void showLineTooltip(int setIndex, int entryIndex, Rect rect) {

		mLineTooltip = (TextView) getActivity().getLayoutInflater().inflate(
				R.layout.circular_tooltip, null);
		mLineTooltip.setText(Integer
				.toString((int) lineValues[setIndex][entryIndex]));

		LayoutParams layoutParams = new LayoutParams(
				(int) Tools.fromDpToPx(35), (int) Tools.fromDpToPx(35));
		layoutParams.leftMargin = rect.centerX() - layoutParams.width / 2;
		layoutParams.topMargin = rect.centerY() - layoutParams.height / 2;
		mLineTooltip.setLayoutParams(layoutParams);

		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			mLineTooltip.setPivotX(layoutParams.width / 2);
			mLineTooltip.setPivotY(layoutParams.height / 2);
			mLineTooltip.setAlpha(0);
			mLineTooltip.setScaleX(0);
			mLineTooltip.setScaleY(0);
			mLineTooltip.animate().setDuration(150).alpha(1).scaleX(1)
					.scaleY(1).rotation(360).setInterpolator(enterInterpolator);
		}

		mLineChart.showTooltip(mLineTooltip);
	}

	@SuppressLint("NewApi")
	private void dismissLineTooltip(final int setIndex, final int entryIndex,
			final Rect rect) {

		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			mLineTooltip.animate().setDuration(100).scaleX(0).scaleY(0)
					.alpha(0).setInterpolator(exitInterpolator)
					.withEndAction(new Runnable() {
						@Override
						public void run() {
							mLineChart.removeView(mLineTooltip);
							mLineTooltip = null;
							if (entryIndex != -1)
								showLineTooltip(setIndex, entryIndex, rect);
						}
					});
		} else {
			mLineChart.dismissTooltip(mLineTooltip);
			mLineTooltip = null;
			if (entryIndex != -1)
				showLineTooltip(setIndex, entryIndex, rect);
		}
	}/*------------------------------------*
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
