package com.shouhuan.activity;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ble.ble_fastcode.bluetooth.InewAgreementListener;
import com.ble.ble_fastcode.bluetooth.WristBandDevice;
import com.ble.ble_fastcode.model.DailySport;
import com.ble.ble_fastcode.model.FMdeviceInfo;
import com.ble.ble_fastcode.model.LocalSport;
import com.ble.ble_fastcode.task.NewAgreementBackgroundThreadManager;
import com.ble.ble_fastcode.task.WriteOneDataTask;
import com.google.gson.Gson;
import com.shouhuan.R;
import com.shouhuan.Util.Constants;
import com.shouhuan.Util.SharedUtils;
import com.shouhuan.data.ShouHuanData;
import com.shouhuan.db.DBUtils;
import com.shouhuan.view.ResideMenuListener;

public class JianKangFragment extends Fragment implements OnClickListener,
		InewAgreementListener {
	private ImageView img_open;
	private ResideMenuListener menuListener;
	private TextView btn_yundong;
	private TextView btn_lishi;
	private YunDongFragment ydFragment;
	private LiShiFragment lsFragment;
	private FragmentTransaction fraTra = null;
	private FragmentManager manager;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (menuListener == null) {
			menuListener = (ResideMenuListener) activity;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.jiankang_layout, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// if (getWristBand().isConnected()) {
		// SharedUtils.setConnectState(true);
		// } else {
		// SharedUtils.setConnectState(false);
		// }
		initView();
		initFragment();
		registerBoradcastReceiver();
		// getData();
	}

	private void initView() {
		img_open = (ImageView) getView().findViewById(R.id.img_open);
		btn_yundong = (TextView) getView().findViewById(R.id.btn_yundong);
		btn_lishi = (TextView) getView().findViewById(R.id.btn_lishi);
		setListener();
	}

	private void setListener() {
		img_open.setOnClickListener(this);
		btn_lishi.setOnClickListener(this);
		btn_yundong.setOnClickListener(this);

	}

	private void initFragment() {
		manager = getChildFragmentManager();
		fraTra = manager.beginTransaction();
		ydFragment = new YunDongFragment();
		fraTra.add(R.id.main_layout, ydFragment);
		fraTra.commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_open:
			// if (menuListener != null) {
			// menuListener.openMenu();
			// }
			getData();
			break;
		case R.id.btn_yundong:
			fraTra = getChildFragmentManager().beginTransaction();
			btn_yundong.setTextColor(getResources().getColor(R.color.blue));
			btn_yundong.setBackgroundColor(getResources().getColor(
					R.color.white));
			btn_lishi.setTextColor(getResources().getColor(R.color.white));
			btn_lishi.setBackgroundColor(getResources().getColor(R.color.blue));
			if (lsFragment != null) {
				fraTra.hide(lsFragment);
			}
			fraTra.show(ydFragment);
			fraTra.commit();

			break;
		case R.id.btn_lishi:
			fraTra = getChildFragmentManager().beginTransaction();
			btn_lishi.setTextColor(getResources().getColor(R.color.blue));
			btn_lishi
					.setBackgroundColor(getResources().getColor(R.color.white));
			btn_yundong.setTextColor(getResources().getColor(R.color.white));
			btn_yundong.setBackgroundColor(getResources()
					.getColor(R.color.blue));

			if (lsFragment == null) {
				lsFragment = new LiShiFragment();
				fraTra.add(R.id.main_layout, lsFragment);
			} else {
				lsFragment.onResume();
			}
			if (ydFragment != null) {
				fraTra.hide(ydFragment);
			}
			fraTra.show(lsFragment);
			fraTra.commit();
			break;
		default:
			break;
		}
	}

	private void getData() {
		/**
		 * 向手环发送读取分冻结命令 分冻结最后一条数据年月日分别为FFFFFF
		 * */
		byte[] data = getWristBand().writeWristBandMintunue();
		/**
		 * 将数据放到队列中
		 */
		WriteOneDataTask task = new WriteOneDataTask(getActivity(), data);
		NewAgreementBackgroundThreadManager.getInstance().addTask(task);
	}

	/**
	 * SDK中正常发现service 和characteristics 回调
	 */
	@Override
	public void onCharacteristicWriteData() {
	}

	/**
	 * 日冻结数据回调 日冻结数据格式封装在DailySport sport.getSteps() 日步数 sport.getDistance() 日距离
	 * 单位：米 sport.getCalorie() 日卡路里 单位：大卡 sport.getYear() 年 sport.getMonth() 月
	 * sport.getDay() 日 其中今天的年月日 格式是ffffff
	 * 
	 */
	@Override
	public void onWristBandReadDailyData(final DailySport sport) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
			}
		});
	}

	/**
	 * 
	 */

	/**
	 * 分冻结数据回调 sport.getSteps() 5分钟的步数 sport.getDistance() 5分钟的距离 单位：米
	 * sport.getCalorie() 5分钟的卡路里 单位：大卡 sport.getYear() 年 sport.getMonth() 月
	 * sport.getDay() 日 sport.getHour() 时 sport.getMinute() 分
	 */
	@Override
	public void onWristBandReading(final LocalSport sport) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String result = toJson(sport);
				getJsonData(result);
			}
		});
	}

	private void getJsonData(String result) {
		Gson gson = new Gson();
		ShouHuanData sh = gson.fromJson(result, ShouHuanData.class);
		int count = sh.getCurrentHourCount(DBUtils.getDBsa(1));
		if (count > 0) {
			ShouHuanData oldData = sh;
			oldData.read(DBUtils.getDBsa(2));
			sh.setCalorie(sh.getCalorie() + oldData.getCalorie());
			sh.setDistance(sh.getDistance() + oldData.getDistance());
			sh.setSteps(sh.getSteps() + oldData.getSteps());
			sh.update(DBUtils.getDBsa(2));
		} else {
			sh.write(DBUtils.getDBsa(2));
		}
		System.out.println("json:::::::::::" + sh.toString());

	}

	/**
	 * model to json 使用Gson工具
	 * 
	 * @param obj
	 * @return
	 */
	public String toJson(Object obj) {
		Gson gson = new Gson();
		String jsonStr = gson.toJson(obj);
		return jsonStr;
	}

	/**
	 * SDK中找到iwown手环回调
	 */
	@Override
	public void onWristBandFindNewAgreement(BluetoothDevice arg0) {

	}

	/**
	 * 获取WristBandDevice 对象
	 * 
	 * @return
	 */
	private WristBandDevice getWristBand() {
		WristBandDevice.getInstance(getActivity(), null, this);
		return WristBandDevice.getInstance(getActivity());
	}

	@Override
	public void deviceInfoMessage(FMdeviceInfo arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * 注册该广播
	 */
	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Constants.BIND_FINISH);// 断开广播
		myIntentFilter.addAction("com.WRISTBAND_DISCONNECT");
		// 注册广播
		getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	/**
	 * 定义广播
	 */
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Constants.BIND_FINISH)) {
				ydFragment.setConnectState();
				getData();
			} else if ("com.WRISTBAND_DISCONNECT".equals(intent.getAction())) {
				SharedUtils.setConnectState(false);
				ydFragment.setConnectState();
			}
		}
	};

	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(mBroadcastReceiver);
	};
}
