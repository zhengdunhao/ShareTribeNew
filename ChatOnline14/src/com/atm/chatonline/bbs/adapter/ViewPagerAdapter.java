/**
 * 
 */
package com.atm.chatonline.bbs.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * @�� com.atm.chatonline.bbs.adapter ---ViewPagerAdapter
 * @����
 * @���� ��С��
 * @ʱ�� 2017-1-2
 * 
 */
public class ViewPagerAdapter extends PagerAdapter{

	    // �����б�
	    private List<View> views;


	    public ViewPagerAdapter(List<View> views) {
	        this.views = views;
	    }

	    public Object instantiateItem(ViewGroup container, int position) {
	        container.addView(views.get(position),0);
	        return views.get(position);
	    }

	    public void destroyItem(ViewGroup container, int position, Object object) {
	       // super.destroyItem(container, position, object);
	        container.removeView(views.get(position));
	    }

	    // ��õ�ǰ������
	    public int getCount() {
	        if (views != null) {
	            return views.size();
	        }
	        return 0;
	    }

	    public boolean isViewFromObject(View view, Object object) {
	        return view == object;
	    }


	}
