package com.shouhuan.activity;

import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ble.ble_fastcode.bluetooth.InewAgreementListener;
import com.ble.ble_fastcode.bluetooth.WristBandDevice;
import com.ble.ble_fastcode.model.DailySport;
import com.ble.ble_fastcode.model.FMdeviceInfo;
import com.ble.ble_fastcode.model.LocalSport;
import com.google.gson.Gson;
import com.shouhuan.R;
import com.shouhuan.Util.Constants;
import com.shouhuan.Util.SharedUtils;
import com.shouhuan.adapter.DeviceSearchAdapter;
import com.shouhuan.view.ProgressWheel;

public class BangDingActivity extends BaseActivity implements
		InewAgreementListener {
	private ImageView back;
	private TextView txt_title;
	private ProgressWheel progressWheel;
	private ListView mListView;
	private List<BluetoothDevice> devs = new ArrayList<BluetoothDevice>();
	private DeviceSearchAdapter mAdapter;
	private TextView txt_search_state;
	private RelativeLayout layout_connect_device_state;
	private TextView txt_connect_device_name;
	private String current_device_name = "";
	private MyBrocastreceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bang_ding);
		initView();
		initResiter();
	}

	private void initView() {
		layout_connect_device_state = (RelativeLayout) findViewById(R.id.layout_device_state);
		layout_connect_device_state.setVisibility(View.GONE);
		txt_connect_device_name = (TextView) findViewById(R.id.txt_device_name);
		txt_search_state = (TextView) findViewById(R.id.txt_search_state);
		findViewById(R.id.back).setOnClickListener(this);
		txt_title = (TextView) findViewById(R.id.title_txt);
		txt_title.setText("�豸����");
		progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
		progressWheel.setOnClickListener(this);
		mListView = (ListView) findViewById(R.id.listView1);
		mAdapter = new DeviceSearchAdapter(this, devs);
		mListView.setAdapter(mAdapter);
		// progressWheel.setBarColor(Color.RED);
		progressWheel.setRimColor(Color.GRAY);
		progressWheel.stopSpinning();
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				getWristBand().stopLeScan();
				BluetoothDevice d = (BluetoothDevice) mAdapter
						.getItem(position);
				/**
				 * ���ӷ��� device ΪbluetoothDevice ����
				 */
				WristBandDevice.getInstance(BangDingActivity.this).connect(d);
				progressWheel.stopSpinning();
				txt_search_state.setText("�������");
				current_device_name = d.getName();

			}
		});
	}

	private void initResiter() {
		mReceiver = new MyBrocastreceiver();
		IntentFilter filter = new IntentFilter();
		// ���ӹ㲥
		filter.addAction("com.WRISTBAND_CONNECT");
		// �Ͽ��㲥
		filter.addAction("com.WRISTBAND_DISCONNECT");
		registerReceiver(mReceiver, filter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finishThisActivity();
			break;
		case R.id.progress_wheel:
			getWristBand().stopLeScan();
			getWristBand().startLeScan();
			progressWheel.spin();
			txt_search_state.setText("��������...");
			break;
		default:
			break;
		}
	}

	private WristBandDevice getWristBand() {
		WristBandDevice.getInstance(this, null, this);
		return WristBandDevice.getInstance(this);
	}

	@Override
	public void onCharacteristicWriteData() {

	}

	@Override
	public void onWristBandFindNewAgreement(final BluetoothDevice dev) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (devs.contains(dev)) {
				} else {
					devs.add(dev);
					mAdapter.notifyDataSetChanged();
				}
			}
		});
	}

	/**
	 * �ն������ݻص� �ն������ݸ�ʽ��װ��DailySport sport.getSteps() �ղ��� sport.getDistance() �վ���
	 * ��λ���� sport.getCalorie() �տ�·�� ��λ���� sport.getYear() ��+2000
	 * sport.getMonth() ��+1 sport.getDay() ��+1 ���н���������� ��ʽ��ffffff
	 * 
	 */
	@Override
	public void onWristBandReadDailyData(final DailySport sport) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(BangDingActivity.this, toJson(sport), 200)
						.show();
				// System.out.println("json::::::::::" + toJson(sport));
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
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(BangDingActivity.this, toJson(sport), 200)
						.show();
				// System.out.println("json::::::::::" + toJson(sport));
			}
		});
	}

	/**
	 * model to json ʹ��Gson����
	 * 
	 * @param obj
	 * @return
	 */
	public String toJson(Object obj) {
		Gson gson = new Gson();
		return gson.toJson(obj);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		/**
		 * ֹͣɨ�������ķ���
		 */
		WristBandDevice.getInstance(this).stopLeScan();
		unregisterReceiver(mReceiver);
	}

	private class MyBrocastreceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ("com.WRISTBAND_CONNECT".equals(intent.getAction())) {
				devs.clear();
				mAdapter.notifyDataSetChanged();
				txt_connect_device_name.setText(current_device_name);
				layout_connect_device_state.setVisibility(View.VISIBLE);
				sendBroadcast(new Intent(Constants.BIND_FINISH));
				SharedUtils.setConnectState(true);
			} else if ("com.WRISTBAND_DISCONNECT".equals(intent.getAction())) {
				SharedUtils.setConnectState(false);
			}
		}
	}

	@Override
	public void deviceInfoMessage(FMdeviceInfo arg0) {

	}
}
