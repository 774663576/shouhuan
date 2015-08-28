package com.shouhuan.adapter;

import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shouhuan.R;

public class DeviceSearchAdapter extends AbsListAdapter<BluetoothDevice> {

	public DeviceSearchAdapter(Context context, List<BluetoothDevice> mList) {
		super(context, mList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.device_item, null);
			viewHolder = new ViewHolder();
			viewHolder.deviceName = (TextView) convertView
					.findViewById(R.id.txt_device_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.deviceName.setText(getItem(position).getName());
		return convertView;
	}

	class ViewHolder {
		private TextView deviceName;
		private ImageView rssi;
	}
}
