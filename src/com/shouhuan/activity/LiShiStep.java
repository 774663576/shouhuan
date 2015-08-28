package com.shouhuan.activity;

import android.content.Context;
import android.view.View;

public abstract class LiShiStep {
	private View contentRootView;
	protected LiShiFragment mActivity;
	protected onNextListener mOnNextListener;
	protected Context mContext;

	public LiShiStep(LiShiFragment activity, View contentRootView) {
		this.contentRootView = contentRootView;
		this.mActivity = activity;
		initView();
		setListener();
	}

	public abstract void initView();

	public abstract void setListener();

	public View findViewById(int id) {
		return contentRootView.findViewById(id);

	}

	public void setmOnNextListener(onNextListener mOnNextListener) {
		this.mOnNextListener = mOnNextListener;
	}

	public interface onNextListener {
		void next();
	}

}
