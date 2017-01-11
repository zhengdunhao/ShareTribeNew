package com.atm.chatonline.chat.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atm.chatonline.bbs.commom.CircleImageView;
import com.atm.chatonline.chat.info.Friend;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;


public class AttentionAdapter extends ArrayAdapter<Friend>{
	private int resourceId;
	
	private String tag="AttentionAdapter";
	
	private String userID,friendID;
	private int relationship;
	
	
	public AttentionAdapter(Context context, int textViewResourceId,
			List<Friend> objects) {
		super(context,textViewResourceId, objects);
		resourceId=textViewResourceId;
	}
	
	public View getView(int position,View convertView,ViewGroup partent){
		final Friend friend = getItem(position);
		View view;
		final ViewHolder viewHolder;
		if(convertView == null){
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.imageID = (CircleImageView)view.findViewById(R.id.friend_img);
			viewHolder.friendNickName = (TextView)view.findViewById(R.id.friend_name);
			viewHolder.department = (TextView)view.findViewById(R.id.department);
			viewHolder.imageRelationship = (ImageView)view.findViewById(R.id.attention_follower_img);
			view.setTag(viewHolder);
		}else{
			view = convertView;
			viewHolder = (ViewHolder)view.getTag();
		}
		if(friend.getBm()==null){
			Log.i(tag, "friend.getBm()Ϊ��");
		}
		
		viewHolder.imageID.setImageBitmap(friend.getBm());
		
		Log.i(tag, "friend.getFriendID"+friend.getOtherID());
		
		viewHolder.friendNickName.setText(friend.getNickName());
		Log.i(tag, "friend.getNickName"+friend.getNickName());
		
		viewHolder.department.setText(friend.getDepartment());
		Log.i(tag, "friend.getDepartment"+friend.getDepartment());
		
		if(friend.getRelationship()==Config.RELATIONSHIP_ATTENTION){
			Log.i(tag, "�ҹ�ע��NickName:"+friend.getNickName()+"");
			viewHolder.imageRelationship.setImageResource(R.drawable.had_attention);
		}else if(friend.getRelationship()==Config.RELATIONSHIP_FOLLOER){
			Log.i(tag, "NickName��:"+friend.getNickName()+"�ҵķ�˿");
			viewHolder.imageRelationship.setImageResource(R.drawable.no_attention);
		}else if(friend.getRelationship()==Config.RELATIONSHIP_NO_MATTER){
			Log.i(tag, "�Һ�NickName:"+friend.getNickName()+"û���κι�ϵ");
			viewHolder.imageRelationship.setImageResource(R.drawable.no_attention);
		}
		
		viewHolder.imageRelationship.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userID = BaseActivity.getSelf().getUserID();
				friendID = friend.getOtherID();
				relationship = friend.getRelationship();
				Log.i(tag, "��ѯuserID:"+userID+"��OtherID:"+friend.getOtherID()+"��ϵ��:"+relationship);
				if(relationship == Config.RELATIONSHIP_ATTENTION){
					Log.i(tag, "�Ѿ���ע�˴��ˣ�����Ҫȡ����ע����");
//					WoliaoBaseActivity.getDbUtil().deleteFriendList(userID, friend.getOtherID(), Config.RELATIONSHIP_ATTENTION);
					friend.setRelationship(Config.RELATIONSHIP_NO_MATTER);
					viewHolder.imageRelationship.setImageResource(R.drawable.no_attention);
					new Thread(reqCanncelRunnable).start();
				}
				else if(relationship == Config.RELATIONSHIP_FOLLOER||relationship == Config.RELATIONSHIP_NO_MATTER){
					Log.i(tag, "���˻�û��ע������׼����ע");
//					saveFriendToDB(userID,friend.getOtherID(),friend.getNickName(),friend.getDepartment(),Config.RELATIONSHIP_ATTENTION);
					friend.setRelationship(Config.RELATIONSHIP_ATTENTION);
					viewHolder.imageRelationship.setImageResource(R.drawable.had_attention);
					new Thread(reqAttentionRunnable).start();
					
				}
			}
		});
	
		
		return view;
	}
	
	
	Runnable reqCanncelRunnable = new Runnable(){
		public void run(){
			BaseActivity.getCon().reqCanncel(userID, friendID);
			Log.i(tag, "�����߳�con.reqCanncelRunnable(userID, friendID)��userID:"+userID+"��friendID:"+friendID);
		}
	};
	
	Runnable reqAttentionRunnable = new Runnable(){
		public void run(){
			BaseActivity.getCon().reqAttention(userID, friendID);
			Log.i(tag, "�����߳�con.reqAttention(userID, friendID)��userID:"+userID+"��friendID:"+friendID);
		}
	};
	
	class ViewHolder{
		CircleImageView imageID;
		TextView friendNickName;
		TextView department;
		ImageView imageRelationship;
	}
}

