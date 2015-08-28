package com.shouhuan.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * ListView适配器抽象类
 * 
 * @author hzy
 * 
 * @param <T>
 */

public abstract class AbsListAdapter<T> extends BaseAdapter {

	protected List<T> mList; // Arraylist数据�?
	protected Context mContext; // 上下文对�?
	protected LayoutInflater mInflater;

	/**
	 * 构�?方法
	 * 
	 * @param context
	 *            上下文对�?
	 * @param mList
	 *            数据�?
	 */
	public AbsListAdapter(Context context, List<T> mList) {
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		setList(mList);
	}

	/**
	 * 
	 * @param context
	 * @param mList
	 */
	public AbsListAdapter(Context context, T[] mArray) {
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		setList(mArray);
	}

	@Override
	public int getCount() {
		return mList != null ? mList.size() : 0;
	}

	@Override
	public T getItem(int position) {
		return mList != null ? mList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 设置数据�?
	 * 
	 * @param mList2
	 */
	private void setList(List<T> mList2) {
		this.mList = mList2 != null ? mList2 : new ArrayList<T>();
	}

	/**
	 * 获取数据集合
	 * 
	 * @return
	 */
	public List<T> getList() {
		return mList;
	}

	/**
	 * 
	 * @param mArray
	 */
	private void setList(T[] mArray) {
		List<T> list = new ArrayList<T>();
		if (mArray != null) {
			for (T t : mArray) {
				list.add(t);
			}
		}
		setList(list);
	}

	/**
	 * 更新数据�?
	 * 
	 * @param mList
	 *            ArrayList<T>
	 */
	public void changeData(List<T> mList) {
		setList(mList);
		this.notifyDataSetChanged();
	}

	/**
	 * 更新数据�?
	 * 
	 * @param mArray
	 */
	public void changeData(T[] mArray) {
		setList(mArray);
		this.notifyDataSetChanged();
	}

	/**
	 * 增加�?��数据
	 * 
	 * @param t
	 * @return
	 */
	public boolean add(T t) {
		boolean result = this.mList.add(t);
		this.notifyDataSetChanged();
		return result;
	}

	/**
	 * 增加�?��集合的数�?
	 * 
	 * @param mList
	 * @return
	 */
	public boolean addAll(List<T> mList) {
		if (mList == null) {
			return false;
		}
		// HashSet<T> set = new HashSet<T>(mList);
		// set.addAll(this.mList);
		// this.mList.clear();
		boolean result = this.mList.addAll(mList);
		notifyDataSetChanged();
		return result;
	}

	/**
	 * 
	 * @param mList
	 * @return
	 */
	public boolean set(List<T> mList) {
		this.mList.clear();
		boolean result = this.mList.addAll(mList);
		notifyDataSetChanged();
		return result;
	}

	/**
	 * 插入�?��数据
	 * 
	 * @param position
	 *            插入的位�?
	 * @param t
	 *            数据对象
	 */
	public void insert(int position, T t) {
		this.mList.add(position, t);
		this.notifyDataSetChanged();
	}

	/**
	 * 删除�?��数据
	 * 
	 * @param t
	 *            数据对象
	 * @return 删除是否成功
	 */
	public boolean remove(T t) {
		boolean removed = this.mList.remove(t);
		this.notifyDataSetChanged();
		return removed;
	}

	/**
	 * 删除�?��集合的数�?
	 * 
	 * @param list
	 * @return
	 */
	public boolean removeAll(Collection<T> list) {
		boolean removed = mList.removeAll(list);
		this.notifyDataSetChanged();
		return removed;
	}

	/**
	 * 删除指定位置的数�?
	 * 
	 * @param position
	 *            要删除的位置
	 * @return 被删除的对象
	 */
	public T remove(int position) {
		T t = this.mList.remove(position);
		this.notifyDataSetChanged();
		return t;
	}

	/**
	 * 清除�?��
	 */
	public void clear() {
		this.mList.clear();
		this.notifyDataSetChanged();
	}

	/**
	 * 更新指定位置的对�?
	 * 
	 * @param position
	 *            指定位置
	 * @param t
	 *            数据对象
	 */
	public void set(int position, T t) {
		this.mList.set(position, t);
		this.notifyDataSetChanged();
	}

	/**
	 * 根据指定的比较器进行排序
	 * 
	 * @param comparator
	 *            比较�?
	 */
	public void sort(Comparator<T> comparator) {
		Collections.sort(mList, comparator);
		this.notifyDataSetChanged();
	}

	@Override
	public abstract View getView(int position, View convertView,
			ViewGroup parent);

}
