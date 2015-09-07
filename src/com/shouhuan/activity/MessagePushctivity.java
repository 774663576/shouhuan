package com.shouhuan.activity;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ble.ble_fastcode.bluetooth.InewAgreementListener;
import com.ble.ble_fastcode.bluetooth.WristBandDevice;
import com.ble.ble_fastcode.model.DailySport;
import com.ble.ble_fastcode.model.FMdeviceInfo;
import com.ble.ble_fastcode.model.LocalSport;
import com.ble.ble_fastcode.task.NewAgreementBackgroundThreadManager;
import com.ble.ble_fastcode.task.WriteOneDataTask;
import com.shouhuan.R;
import com.shouhuan.Util.SharedUtils;

public class MessagePushctivity extends BaseActivity implements
		InewAgreementListener {
	private TextView txt_title;
	private ImageView img_phone_state;
	private ImageView img_sms_state;
	private ImageView img_shuimian_state;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_pushctivity);
		initView();
	}

	private void initView() {
		img_phone_state = (ImageView) findViewById(R.id.img_phone_state);
		img_sms_state = (ImageView) findViewById(R.id.img_sms_state);
		img_shuimian_state = (ImageView) findViewById(R.id.img_shuimian_state);
		img_phone_state.setSelected(SharedUtils.getPhoneState());
		img_sms_state.setSelected(SharedUtils.getSMSState());
		img_shuimian_state.setSelected(SharedUtils.getShuiMianState());
		txt_title = (TextView) findViewById(R.id.title_txt);
		txt_title.setText("消息推送");
		setListener();

	}

	private void setListener() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.layout_phone).setOnClickListener(this);
		findViewById(R.id.layout_sms).setOnClickListener(this);
		findViewById(R.id.layout_shuimian).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finishThisActivity();
			break;
		case R.id.layout_phone:
			if (img_phone_state.isSelected()) {
				img_phone_state.setSelected(false);
				SharedUtils.setPhoneState(false);
			} else {
				img_phone_state.setSelected(true);
				SharedUtils.setPhoneState(true);

			}
			break;
		case R.id.layout_sms:
			if (img_sms_state.isSelected()) {
				img_sms_state.setSelected(false);
				SharedUtils.setSMSState(false);
			} else {
				img_sms_state.setSelected(true);
				SharedUtils.setSMSState(true);

			}
			break;
		case R.id.layout_shuimian:
			if (img_shuimian_state.isSelected()) {
				img_shuimian_state.setSelected(false);
				SharedUtils.setShuiMianState(false);
			} else {
				img_shuimian_state.setSelected(true);
				SharedUtils.setShuiMianState(true);

			}
			byte[] data = getWristBand().setWristBandGestureAndLight(false,
					false, false, false, SharedUtils.getShuiMianState());
			/**
			 * 将数据放到队列中
			 */
			WriteOneDataTask task = new WriteOneDataTask(this, data);
			NewAgreementBackgroundThreadManager.getInstance().addTask(task);
			break;
		default:
			break;
		}
	}

	/**
	 * 获取WristBandDevice 对象
	 * 
	 * @return
	 */
	private WristBandDevice getWristBand() {
		WristBandDevice.getInstance(this, null, this);
		return WristBandDevice.getInstance(this);
	}

	@Override
	public void deviceInfoMessage(FMdeviceInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCharacteristicWriteData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWristBandFindNewAgreement(BluetoothDevice arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWristBandReadDailyData(DailySport arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWristBandReading(LocalSport arg0) {
		// TODO Auto-generated method stub

	}
}
