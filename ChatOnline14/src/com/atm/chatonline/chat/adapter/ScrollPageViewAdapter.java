package com.atm.chatonline.chat.adapter;
/*
 * ������ʵ��ViewPager������Ļƽ���л���PagerAdapter��������
 * 2015-7-30-֣
 * */
import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ScrollPageViewAdapter extends PagerAdapter {

	private ArrayList<View> pageViews= new ArrayList<View>();
	
	public ScrollPageViewAdapter(ArrayList<View> pageViews){
		this.pageViews=pageViews;
	}

	// ��ʾ��Ŀ
	@Override
	public int getCount() {
		return pageViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return super.getItemPosition(object);
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		// TODO Auto-generated method stub
		((ViewPager) arg0).removeView(pageViews.get(arg1));
	}

	/***
	 * ��ȡÿһ��item�� ����listview�е�getview
	 */
	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(pageViews.get(arg1));
		return pageViews.get(arg1);
	}

}
