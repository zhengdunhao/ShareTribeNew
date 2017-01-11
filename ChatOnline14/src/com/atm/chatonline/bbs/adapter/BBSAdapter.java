package com.atm.chatonline.bbs.adapter;
/**
 * @�� com.atm.chatonline.adapter ---BBSAdapter
 * @���� BBS��������
 * @���� ��С��
 * @ʱ�� 2015-8-16
 * 
 */
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atm.chatonline.bbs.activity.bbs.BBSDepartmentView;
import com.atm.chatonline.bbs.activity.bbs.BBSListView;
import com.atm.chatonline.bbs.activity.bbs.BBSMainView;
import com.atm.chatonline.bbs.bean.BBS;
import com.atm.chatonline.bbs.commom.CircleImageView;
import com.atm.chatonline.bbs.util.ExtendsIntent;
import com.atm.chatonline.bbs.util.LogUtil;
import com.example.studentsystem01.R;

public class BBSAdapter extends ArrayAdapter<BBS>{
	private String tag ="BBSAdapter";
	private int resID;
	public BBSAdapter(Context context, int resID, List<BBS> objects){
		super(context, resID , objects);
		this.resID = resID;
	}
	
	class ViewHolder{
		TextView clickGoodNum, publishTime, publishID,replyNum,title,someContent,type,more;
		RelativeLayout labType;
		CircleImageView headImage;
		TextView [] lab = new TextView[3];
		
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		BBS bbs = getItem(position);
		View view ;
		ViewHolder viewHolder ;
		if(convertView == null){
			view = LayoutInflater.from(getContext()).inflate(resID, null);
			viewHolder = new ViewHolder();
			viewHolder.clickGoodNum = (TextView) view.findViewById(R.id.clickGoodNum);
			viewHolder.publishTime = (TextView) view.findViewById(R.id.publishTime);
			viewHolder.publishID = (TextView) view.findViewById(R.id.publishID);
			viewHolder.replyNum = (TextView) view.findViewById(R.id.replynum);
			viewHolder.title = (TextView) view.findViewById(R.id.title);
			viewHolder.someContent = (TextView) view.findViewById(R.id.someContent);
			viewHolder.headImage = (CircleImageView) view.findViewById(R.id.headImage);
			viewHolder.type=(TextView) view.findViewById(R.id.tv_type);
			viewHolder.labType=(RelativeLayout)view.findViewById(R.id.lab_type);//�����ȡ���ǩ
			viewHolder.more=(TextView) view.findViewById(R.id.tv_more);//�����ȡ���ǩβ���ġ����ࡱ
			int [] labs = {R.id.labName1,R.id.labName2,R.id.labName3};
			for(int i = 0 ; i < viewHolder.lab.length ; i++){
				viewHolder.lab[i] = (TextView) view.findViewById(labs[i]);
			}
			view.setTag(viewHolder);//��ViewHolder�洢��view��
		}else{
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();//���»�ȡViewHolder
		}
		viewHolder.clickGoodNum.setText(bbs.getClickGoodNum());
		viewHolder.publishTime.setText(bbs.getPublishTime());
		viewHolder.publishID.setText(bbs.getNickname());
		viewHolder.replyNum.setText(bbs.getReplyNum());
		viewHolder.title.setText("��"+bbs.getEssayType()+"��"+bbs.getTitle());
		viewHolder.someContent.setText(bbs.getSomeContent());
		viewHolder.headImage.setImageDrawable(bbs.getHeadImage());
		//�ж��Ƿ�ӱ�ǩ������/���ȡ�
		if(bbs.getFlag()!=null) {
			if(bbs.getFlag().equals("hot")) {
				viewHolder.type.setText("   ������");
				viewHolder.more.setVisibility(View.VISIBLE);
				viewHolder.labType.setVisibility(View.VISIBLE);
				viewHolder.type.setVisibility(View.VISIBLE);
				Log.d("flag",bbs.getFlag());
			}else if(bbs.getFlag().equals("new")) {
				viewHolder.type.setText("   ������");
				viewHolder.more.setVisibility(View.GONE);
				viewHolder.labType.setVisibility(View.VISIBLE);
				viewHolder.type.setVisibility(View.VISIBLE);
			}
			else {
				viewHolder.more.setVisibility(View.GONE);
				viewHolder.type.setVisibility(View.GONE);
				viewHolder.labType.setVisibility(View.GONE);
			}
		}
		//���ر�ǩ
		for(int i=0;i<bbs.getLabName0().length;i++){
			viewHolder.lab[i].setText(bbs.getLabName0()[i]);
//			Log.i(tag, "bbs.getLabColor0()[i]"+bbs.getLabColor0()[i]);
			viewHolder.lab[i].setBackgroundColor(bbs.getLabColor0()[i]);
			viewHolder.lab[i].setVisibility(View.VISIBLE);
		}
		for(int i = 2 ; i >= bbs.getLabName0().length ; i--){
			viewHolder.lab[i].setVisibility(View.INVISIBLE);
		}
		viewHolder.more.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				LogUtil.d("����ˡ����ࡱ");
				ExtendsIntent intent = new ExtendsIntent(getContext(),BBSListView.class, null,
						"essay_hotEssay.action", "depart", 1);
				getContext().startActivity(intent);
			}
			
		});
		return view;
	}
	
}
