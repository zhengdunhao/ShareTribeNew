/**
 * 
 */
package com.atm.chatonline.recuit.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.recuit.bean.Recuit;
import com.example.studentsystem01.R;

/**
 * @�� com.atm.charonline.recuit.adapter ---RecuitAdapter
 * @���� ��Ƹ�б��������
 * @���� ��С��
 * @ʱ�� 2015-9-20
 * 
 */
public class RecuitAdapter extends ArrayAdapter<Recuit> {
	private int resource;

	/**
	 * @param context
	 * @param resource
	 * @param objects
	 */
	public RecuitAdapter(Context context, int resource, List<Recuit> objects) {
		super(context, resource, objects);
		this.resource=resource;
	}
	
	class ViewHolder{
		ImageView recuitType,enough;
		TextView workContent,publishTime,salary,location;
		
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		Recuit recuit = getItem(position);
		View view;
		ViewHolder viewHolder;
		if(convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resource,null);
			viewHolder = new ViewHolder();
			viewHolder.workContent = (TextView) view.findViewById(R.id.recuit_content);
			viewHolder.publishTime = (TextView) view.findViewById(R.id.recuit_publishTime);
			viewHolder.salary = (TextView) view.findViewById(R.id.recuit_salary);
			viewHolder.recuitType = (ImageView) view.findViewById(R.id.recuit_recuitType);
			viewHolder.location = (TextView) view.findViewById(R.id.recuit_location);
			viewHolder.enough = (ImageView) view.findViewById(R.id.recuit_enough);
			view.setTag(viewHolder);
		}else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		Log.d("recuitAdapter:recuit.getPublishTime()",recuit.getPublishTime());
		Log.d("recuitAdapter:recuit.getSalary()",recuit.getSalary());
		Log.d("recuitAdapter:recuit.getWorkAddress()",recuit.getWorkAddress());
		Log.d("recuitAdapter:recuit.getWoTypeName()",recuit.getWoTypeName());
		Log.d("recuitAdapter:recuit.getReTypeName()",recuit.getReTypeName());
		viewHolder.publishTime.setText(recuit.getPublishTime());
		viewHolder.salary.setText("н��"+recuit.getSalary());
		viewHolder.workContent.setText("��λ"+recuit.getWoTypeName());
		viewHolder.location.setText(recuit.getWorkAddress());

			switch(recuit.getReTypeName()) {
			case "ʵϰ":
				LogUtil.d("ͼƬ�䶯��ʵϰ");
				viewHolder.recuitType.setBackgroundResource(R.drawable.image_internship);
				break;
			case "ȫְ":
				LogUtil.d("ͼƬ�䶯��ȫְ");
				viewHolder.recuitType.setBackgroundResource(R.drawable.image_fulltime);
				break;
			case "��ְ":
				LogUtil.d("ͼƬ�䶯����ְ");
				viewHolder.recuitType.setBackgroundResource(R.drawable.image_parttime);
				break;
			default:
				viewHolder.recuitType.setBackgroundResource(R.drawable.image_fulltime);
			}
		return view;
	}

}
