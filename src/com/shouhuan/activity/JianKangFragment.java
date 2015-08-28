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
		 * ���ֻ����Ͷ�ȡ�ֶ������� �ֶ������һ�����������շֱ�ΪFFFFFF
		 * */
		byte[] data = getWristBand().writeWristBandMintunue();
		/**
		 * �����ݷŵ�������
		 */
		WriteOneDataTask task = new WriteOneDataTask(getActivity(), data);
		NewAgreementBackgroundThreadManager.getInstance().addTask(task);
	}

	/**
	 * SDK����������service ��characteristics �ص�
	 */
	@Override
	public void onCharacteristicWriteData() {
	}

	/**
	 * �ն������ݻص� �ն������ݸ�ʽ��װ��DailySport sport.getSteps() �ղ��� sport.getDistance() �վ���
	 * ��λ���� sport.getCalorie() �տ�·�� ��λ���� sport.getYear() �� sport.getMonth() ��
	 * sport.getDay() �� ���н���������� ��ʽ��ffffff
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
	 * �ֶ������ݻص� sport.getSteps() 5���ӵĲ��� sport.getDistance() 5���ӵľ��� ��λ����
	 * sport.getCalorie() 5���ӵĿ�·�� ��λ���� sport.getYear() �� sport.getMonth() ��
	 * sport.getDay() �� sport.getHour() ʱ sport.getMinute() ��
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
	 * model to json ʹ��Gson����
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
	 * SDK���ҵ�iwown�ֻ��ص�
	 */
	@Override
	public void onWristBandFindNewAgreement(BluetoothDevice arg0) {

	}

	/**
	 * ��ȡWristBandDevice ����
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
	 * ע��ù㲥
	 */
	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Constants.BIND_FINISH);// �Ͽ��㲥
		myIntentFilter.addAction("com.WRISTBAND_DISCONNECT");
		// ע��㲥
		getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	/**
	 * ����㲥
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
