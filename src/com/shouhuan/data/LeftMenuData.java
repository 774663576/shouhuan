package com.shouhuan.data;

public class LeftMenuData {
	private int drawable_id;
	private String menu_name = "";

	public LeftMenuData(int drawable_id, String menu_name) {
		this.drawable_id = drawable_id;
		this.menu_name = menu_name;
	}

	public int getDrawable_id() {
		return drawable_id;
	}

	public void setDrawable_id(int drawable_id) {
		this.drawable_id = drawable_id;
	}

	public String getMenu_name() {
		return menu_name;
	}

	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}
}
