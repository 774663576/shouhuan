package com.shouhuan.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shouhuan.R;
import com.shouhuan.Util.Utils;
import com.shouhuan.activity.BangDingActivity;
import com.shouhuan.activity.MainActivity;
import com.shouhuan.activity.MessagePushctivity;
import com.shouhuan.data.LeftMenuData;

public class LeftMenu {
	private MainActivity mActivity;
	private View contentRootView;
	private ListView mListView;
	private List<LeftMenuData> mLists = new ArrayList<LeftMenuData>();

	public LeftMenu(MainActivity mActivity, View contentRootView) {
		this.mActivity = mActivity;
		this.contentRootView = contentRootView;
		initData();
		initView();
	}

	private void initView() {
		mListView = (ListView) contentRootView.findViewById(R.id.listView1);
		mListView.setAdapter(new MyAdapter());
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				mActivity.closeMenu();
				switch (position) {
				case 0:
					mActivity.startActivity(new Intent(mActivity,
							BangDingActivity.class));
					break;
				case 2:
					mActivity.startActivity(new Intent(mActivity,
							MessagePushctivity.class));
					break;
				default:
					break;
				}
				Utils.leftOutRightIn(mActivity);
			}
		});
	}

	private void initData() {
		mLists.add(new LeftMenuData(R.drawable.banding, "设备绑定"));
		mLists.add(new LeftMenuData(R.drawable.setting, "设备设置"));
		mLists.add(new LeftMenuData(R.drawable.setting, "消息推送"));

	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mLists.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mActivity).inflate(
						R.layout.menu_item, null);
				holder = new ViewHolder();
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.txt = (TextView) convertView.findViewById(R.id.menu_txt);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.img.setImageResource(mLists.get(position).getDrawable_id());
			holder.txt.setText(mLists.get(position).getMenu_name());
			return convertView;
		}
	}

	class ViewHolder {
		ImageView img;
		TextView txt;
	}
}
