package com.atm.chatonline.chat.ui;

/**
 * Ⱥ�ĵ��б�*/
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.atm.chatonline.chat.adapter.GroupAdapter;
import com.atm.chatonline.chat.info.Group;
import com.example.studentsystem01.R;



public class GroupListActivity extends Activity{
	private List<Group> GroupList;
	private  ListView listView;
	private String userID;
	private String tag="GroupListActivity";
	private static Handler handler;
	private GroupAdapter groupAdapter;
	protected void onCreate(Bundle savedInstanceState){
		Log.i(tag, "GroupListActivity--------��������");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.group_list_view);
		initData();
		Log.i(tag, "GroupListActivity--------��ɳ�ʼ�������б�");
		initAdapter();
		listView.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view,int position,long id){
				Log.i(tag, "FriendListActivity ------222");
				Group group = GroupList.get(position);
				String groupID=group.getGroupId();
				Log.i(tag, "FriendListActivity --333--userID:"+userID);
				Log.i(tag, "FriendListActivity --333--friendID:"+groupID);
				Intent intent=new Intent(GroupListActivity.this, GroupChatActivity.class);
				Log.i(tag, "FriendListActivity ------444");
				intent.putExtra("userID", userID);
				intent.putExtra("groupID", groupID);
				startActivity(intent);
				Log.i(tag, "FriendListActivity ------555");
			}
		});
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				if(msg.what==1){
					initAdapter();
					groupAdapter.notifyDataSetChanged();
				}
			}
		};
	}
	/**
	 * ��ʼ��Ҫ�õ������ݣ�userId,��ȡ��ChatMainActivtiy���ݹ�����Ⱥ�б�
	 */
	private void initData() {
		userID=getIntent().getStringExtra("userId");
		Log.i(tag, "GroupListActivity-------�Ҵ�User���л��userID��:"+userID);
		ArrayList list=getIntent().getParcelableArrayListExtra("list");
		GroupList=(List<Group>)list.get(0);	
	}
	/**
	 * ��ʼ��GroupAdapter
	 */
	private void initAdapter(){
		groupAdapter= new GroupAdapter(GroupListActivity.this,R.layout.group_friend_item,GroupList);
		listView = (ListView)findViewById(R.id.list_group);
		listView.setAdapter(groupAdapter);
	}
	
	/**
	 * �½�Ⱥʱ��������������ã���������Ⱥ�б���
	 * @throws InterruptedException 
	 */
	protected void upDate(List<Group> groupList){
		GroupList=groupList;
		Log.i(tag, "upDate������");
		handler.sendEmptyMessage(1);
	}	
}
