package com.atm.chatonline.chat.ui;
/**
 * �鿴Ⱥ���ϣ�����Ⱥ��Ա��
 * author--��
 */
import java.util.ArrayList;
import java.util.List;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.atm.chatonline.chat.adapter.GroupMemberAdapter;
import com.atm.chatonline.chat.info.Friend;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;



/**
 * @author Jackbing
 *
 */
public class GroupChatLookDataActivity extends BaseActivity implements OnClickListener,OnItemClickListener{
	private final static String tag="GroupChatLookDataActivity";
	private String groupIds;//Ⱥid
	private String[] manager=new String[]{"Ⱥ����","����Ⱥ"};
	private int[] images=new int[]{R.drawable.group_look_image_item4};//�����ͼƬ
	private String[] name=new String[]{" "};//�����ͼƬ�·���ʾ�ո����ʾ�ַ���
	private Handler handler;
	private GridView grid;
	private Button btnOnBack;
	private LinearLayout settingLayout,managerLayout;
	private List<Friend> listImgItems;//Ⱥ��Ա�б�
	private GroupMemberAdapter memberAdapter;//GridView��Adapter
	private Friend friend;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_look_data_view);
		initComponent();
		groupIds=getIntent().getStringExtra("groupId");
		initData(groupIds);
		grid=(GridView) findViewById(R.id.group_member_gridview);
		listImgItems=new ArrayList<Friend>();
		friend=new Friend(name[0],BitmapFactory.decodeResource(getResources(), R.drawable.group_look_image_item4));
		listImgItems.add(friend);
		initAdapter();
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				if(msg.what==0){
					initAdapter();
				}
			}
		};
	}
	
	/**
	 * 
	 */
	private void initComponent() {
		btnOnBack=(Button) findViewById(R.id.look_group_data_back);
		settingLayout=(LinearLayout) findViewById(R.id.group_setting_layout);
		managerLayout=(LinearLayout) findViewById(R.id.group_manager_layout);
		grid=(GridView) findViewById(R.id.group_member_gridview);
		btnOnBack.setOnClickListener(this);
		settingLayout.setOnClickListener(this);
		managerLayout.setOnClickListener(this);
		grid.setOnItemClickListener(this);
		
	}
	private void initData(String groupIds) {//���ʷ���������
		//�Ƚ�groupIdתΪ���Σ����������յ�Ⱥid����������
		new Thread(runnable).start();
	}
	
	
	public void initAdapter(){
		memberAdapter=new GroupMemberAdapter(GroupChatLookDataActivity.this,listImgItems);
		grid.setAdapter(memberAdapter);
		memberAdapter.notifyDataSetChanged();
	}
	Runnable runnable=new Runnable(){

		@Override
		public void run() {
			int groupId=Integer.parseInt(groupIds);
			con.sendLookGroupData(groupId);
		}
		
	};
	@Override
	public void processMessage(Message msg) {
		Bundle bundle=msg.getData();
		int result=bundle.getInt("result");
		if(result==Config.SUCCESS){
			ArrayList arrayList=bundle.getParcelableArrayList("arrayList");
			listImgItems=(ArrayList<Friend>)arrayList.get(0);
			listImgItems.add(friend);
			handler.sendEmptyMessage(0);
			
		}else if(result==Config.FAILED)
		{
			Log.i("GroupChatLookDataActivity", "�鿴Ⱥ����ʧ��");
		}else{
			Log.i("GroupChatLookDataActivity", "���Ҳ�����Ⱥ����Ϣ");
		}
		
	}
	@Override
	public void onClick(View v) {
		int id=v.getId();
		if(id==R.id.look_group_data_back){
			this.onBackPressed();
		}else if(id==R.id.group_setting_layout){
			Log.i(tag, "Ⱥ���ñ����");
		}else if(id==R.id.group_manager_layout){
			Log.i(tag, "����Ⱥ�����");
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
			Log.i(tag, "Ⱥ��Ա�б����");
	}
	
}
