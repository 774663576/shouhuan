package com.shouhuan.Util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.shouhuan.R;

public class RightBtnPopwindow implements OnClickListener {
	private PopupWindow popupWindow;
	private Context mContext;
	private View v;

	private View view;
	private SelectOnclick mSelectOnclick;
	private LinearLayout layout_parent;

	public void setmSelectOnclick(SelectOnclick mSelectOnclick) {
		this.mSelectOnclick = mSelectOnclick;
	}

	public RightBtnPopwindow(Context context, View v) {
		this.mContext = context;
		this.v = v;
		LayoutInflater inflater = LayoutInflater.from(mContext);
		view = inflater.inflate(R.layout.right_btn_popwindow_layout, null);
		initView();
		initPopwindow();

	}

	private void initView() {
		layout_parent = (LinearLayout) view.findViewById(R.id.parent);
		layout_parent.getBackground().setAlpha(150);
		layout_parent.setOnClickListener(this);
		view.findViewById(R.id.layout_yundong).setOnClickListener(this);
		view.findViewById(R.id.layout_shuimian).setOnClickListener(this);
	}

	/**
	 * ��ʼ��popwindow
	 */
	@SuppressWarnings("deprecation")
	private void initPopwindow() {
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		// �����Ϊ�˵��������Back��Ҳ��ʹ����ʧ�����Ҳ�����Ӱ����ı�����������ģ�
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// popupWindow.setAnimationStyle(R.style.AnimBottom);
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				mSelectOnclick.onDismiss();
			}
		});
	}

	/**
	 * popwindow����ʾ
	 */
	public void show() {
		popupWindow.showAsDropDown(v);
		// ʹ��ۼ�
		popupWindow.setFocusable(true);
		// ����������������ʧ
		popupWindow.setOutsideTouchable(true);
		// ˢ��״̬
		popupWindow.update();
	}

	// ����
	public void dismiss() {
		popupWindow.dismiss();
	}

	@Override
	public void onClick(View v) {
		dismiss();
		switch (v.getId()) {
		case R.id.parent:
			dismiss();
			break;
		case R.id.layout_yundong:
			mSelectOnclick.menu1_select();
			break;
		case R.id.layout_shuimian:
			mSelectOnclick.menu2_select();
			break;
		default:
			break;
		}
	}

	public interface SelectOnclick {
		void menu1_select();

		void menu2_select();

		void onDismiss();

	}
}
