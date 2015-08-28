package com.shouhuan;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shouhuan.Util.SharedUtils;
import com.shouhuan.activity.BaseActivity;

public class MessagePushctivity extends BaseActivity {
	private TextView txt_title;
	private ImageView img_phone_state;
	private ImageView img_sms_state;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_pushctivity);
		initView();
	}

	private void initView() {
		img_phone_state = (ImageView) findViewById(R.id.img_phone_state);
		img_sms_state = (ImageView) findViewById(R.id.img_sms_state);
		img_phone_state.setSelected(SharedUtils.getPhoneState());
		img_sms_state.setSelected(SharedUtils.getSMSState());
		txt_title = (TextView) findViewById(R.id.title_txt);
		txt_title.setText("ÏûÏ¢ÍÆËÍ");
		setListener();
	}

	private void setListener() {
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.layout_phone).setOnClickListener(this);
		findViewById(R.id.layout_sms).setOnClickListener(this);
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
		default:
			break;
		}
	}
}
