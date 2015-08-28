package com.shouhuan.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shouhuan.R;
import com.shouhuan.view.LeftMenu;
import com.shouhuan.view.ResideLayout;
import com.shouhuan.view.ResideMenuListener;

public class MainActivity extends FragmentActivity implements OnClickListener,
		ResideMenuListener {
	private ResideLayout resideLayout;
	private LinearLayout layotu_jiankang;
	private LinearLayout layout_message;
	private ImageView img_jiankang;
	private ImageView img_message;
	private FragmentTransaction fraTra = null;
	private FragmentManager manager;
	private JianKangFragment jkFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		LinearLayout leftMe = (LinearLayout) findViewById(R.id.left_menu);
		new LeftMenu(this, leftMe);
		resideLayout = (ResideLayout) findViewById(R.id.reside_layout);
		initView();
		initFragment();
	}

	private void initView() {
		layotu_jiankang = (LinearLayout) findViewById(R.id.layout_jiankang);
		layout_message = (LinearLayout) findViewById(R.id.layout_message);
		img_jiankang = (ImageView) findViewById(R.id.img_jiankang);
		img_message = (ImageView) findViewById(R.id.img_message);
		img_jiankang.setSelected(true);
		setListener();
	}

	private void setListener() {
		layotu_jiankang.setOnClickListener(this);
		layout_message.setOnClickListener(this);
	}

	private void initFragment() {
		manager = getSupportFragmentManager();
		fraTra = manager.beginTransaction();
		jkFragment = new JianKangFragment();
		fraTra.add(R.id.main_layout, jkFragment);
		fraTra.commit();
	}

	public static class SimpleFragment extends Fragment {

		private int mIndex;
		private TextView mView;

		public static SimpleFragment newInstance(int index) {
			SimpleFragment fragment = new SimpleFragment();
			Bundle args = new Bundle();
			args.putInt("index", index);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mIndex = getArguments().getInt("index");
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			mView = new TextView(container.getContext());
			mView.setBackgroundColor(Color.WHITE);
			return mView;
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
			mView.setText("Fragment: " + mIndex);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_jiankang:
			img_message.setSelected(false);
			img_jiankang.setSelected(true);
			break;
		case R.id.layout_message:
			img_message.setSelected(true);
			img_jiankang.setSelected(false);
			break;
		default:
			break;
		}
	}

	@Override
	public void openMenu() {
		resideLayout.openPane();
	}

	public void closeMenu() {
		resideLayout.closePane();
	}
}
