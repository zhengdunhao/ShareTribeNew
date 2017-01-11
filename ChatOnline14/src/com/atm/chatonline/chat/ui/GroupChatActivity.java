package com.atm.chatonline.chat.ui;
/**
 * Ⱥ�ĵ��������
 * author--��
 */
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.chatonline.chat.adapter.GroupChatAdapter;
import com.atm.chatonline.chat.info.GroupChatMessage;
import com.atm.chatonline.chat.util.Config;
import com.atm.chatonline.chat.util.TimeUtil;
import com.example.studentsystem01.R;



public class GroupChatActivity extends BaseActivity implements OnClickListener,ReceiveInfoListener{
	final String TAG="MultipleChatActivity";
	private String userId,groupId,content;
	private GroupChatAdapter historyAdapter;
	private List<GroupChatMessage> message=new ArrayList<GroupChatMessage>();
	private ListView chatHistory;
	private int[] headImg={R.drawable.me,R.drawable.xiaohei};
	private Button btnsubmit,onBack;
	private EditText editor;
	private Handler handler;
	private ImageButton chatHeadImg;
	private final static String STATE="G";
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiple_chat_view);
		con.addReceiveInfoListener(STATE,GroupChatActivity.this);//��ӱO ��Ϣ�ıO ��,�жϵ�ǰ�û��Ƿ����������
		initData();
		setAdapterForList();
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO �Զ����ɵķ������
				super.handleMessage(msg);
				historyAdapter.notifyDataSetChanged();
			}
		};
	}
	private void initData() {
		Intent intent=getIntent();
		userId=intent.getStringExtra("userID");//��ȡuserId��groupId
		groupId=intent.getStringExtra("groupID");
		Log.i("mutl**", userId);
		Log.i("mutl**", groupId);
		chatHistory=(ListView) findViewById(R.id.chat_view);
		editor=(EditText) findViewById(R.id.chat_editor);
		onBack=(Button) findViewById(R.id.single_chat_back);
		btnsubmit=(Button) findViewById(R.id.btn_submit);
		chatHeadImg=(ImageButton) findViewById(R.id.personal_message);
		chatHeadImg.setOnClickListener(this);
		btnsubmit.setOnClickListener(this);
		onBack.setOnClickListener(this);
	}
	/**
	 * ��ʼ����������Adapter�����ý���ı��⣬���ڱ�������ʾȺid,ͨ����ѯ�������ݿ��ʼ����Ϣ
	 */
	public void setAdapterForList(){
		initTitleName();
		initMessage();
		historyAdapter=new GroupChatAdapter(this,message);
		chatHistory.setAdapter(historyAdapter);
	}
	/**
	 * ��������Ⱥid
	 */
	public void initTitleName(){//��ʼ��Ⱥ���Q
		LayoutInflater inflater=getLayoutInflater();
		View layout=inflater.inflate(R.layout.multiple_chat_title, null);
		TextView groupName=(TextView) layout.findViewById(R.id.group_name);
		groupName.setText(groupId);
	}
	/**
	 * ��ʼ����ʷ��Ϣ
	 */
	public void initMessage(){//��ʼ��Ⱥ��Ϣ
		Log.i(TAG, userId);
		Log.i(TAG, groupId);
		message=dbUtil.queryGroupChatMessage(userId, groupId);//��ѯ���ؼ�¼
	}
	/**
	 * ������Ͱ�ť���������ķ��ذ�ť���鿴Ⱥ���ϵİ�ť���¼���Ӧ
	 */
	@Override
	public void onClick(View v) {
		int id=v.getId();
		if(id==R.id.btn_submit){
			content=editor.getText().toString().trim();
			if(!content.equals("")){
				Log.i(TAG, "##### userId is"+userId);
				Log.i(TAG, "##### groupId is"+groupId);
				new Thread(runnable).start();
			}else{
				Toast.makeText(getApplicationContext(), "���ܷ��Ϳ���Ϣ", Toast.LENGTH_SHORT).show();
			}
			editor.setText("");
		}else if(id==R.id.personal_message){
			Intent intent=new Intent(this,GroupChatLookDataActivity.class);
			intent.putExtra("groupId", groupId);
			startActivity(intent);
			Log.i(TAG, "personal_message");
		}else if(id==R.id.single_chat_back){
			GroupChatActivity.this.onBackPressed();
		}
	}
	/**
	 * �����������,���һ���Լ����͵���Ϣ�������Ҵ������ݿ�
	 * @param type
	 * @param content
	 */
	public void sendMessage(int type,String content){
		String sendTime=TimeUtil.getAbsoluteTime();
		boolean result=false;
		if(type==Config.CROWD_MESSAGE_TEXT){
			Log.i(TAG, "PersonChatActivity----���εĶ���������type:Config.MESSAGE_TYPE_TXT"+",������:"+content);
			Log.i("****", "userId is "+userId );
			Log.i("***", "userId is "+groupId );
			result=con.sendGroupText(userId,groupId,content);//�l����Ϣ
		}
		if(result){
			message.add(new GroupChatMessage(GroupChatMessage.MESSAGE_TO,content,headImg[0],sendTime,0));
			handler.sendEmptyMessage(1);
			Log.i("&&&", "userId is "+userId);
			Log.i("&&&", "userId is "+groupId);
			saveToDB(userId,groupId,GroupChatMessage.MESSAGE_TO,1,content,sendTime,0);//���뱾�����ݿ�
		}
	}
	
	/**
	 * ���뱾�����ݿ�
	 * @param self_Id
	 * @param group_Id
	 * @param direction
	 * @param type
	 * @param content
	 * @param time
	 */
	public void saveToDB(String self_Id,String group_Id,int direction,int type,String content,String time,int showTime){
		ContentValues values=new ContentValues();
		values.put("self_Id", self_Id);
		values.put("group_Id", group_Id);
		values.put("direction", direction);
		values.put("type", 1);//������ָ��Ϣ���ͣ�����Ŀǰ��û�ж��壬Ŀǰֻ���ı�����
		values.put("content", content);
		values.put("time", time);
		values.put("showTime", showTime);
		dbUtil.insertGroupChatMessage(values);
	}
	/**
	 * ���߳�ִ����������صĲ���
	 */
	Runnable runnable=new Runnable(){

		@Override
		public void run() {
			sendMessage(Config.CROWD_MESSAGE_TEXT,content);
		}
	};
	/**
	 * ������յ�����Ϣ
	 */
	public void processMessage(Message msg){
		Log.i(TAG, "PersonChatActivity----��������н��յ��µ���Ϣ");
		if(msg.what==Config.CROWD_MESSAGE_FROM){
			Log.i(TAG, "PersonChatActivity----�յ�msg.what==Config.RECEIVE_MESSAGE");
			Bundle bundle = msg.getData();
			GroupChatMessage newMessages = (GroupChatMessage)bundle.getSerializable("groupChatMessage");
			message.add(newMessages);
			Log.i(TAG, "�����ҽ��յ��Ķ��ţ�����׼�����뱾�����ݿ⣬������"+newMessages.getContent());
			String sendrerID = newMessages.getSenderId();
			String groupID = newMessages.getGroupId();
			int direction = newMessages.getDirection();
			int type = newMessages.getType();
			String time = newMessages.getTime();
			String content = newMessages.getContent();
			int showTime = newMessages.getShowTime();
			Log.i(TAG, "senderId:"+sendrerID+"��groupID:"+groupID+"direction"+direction);
			Log.i(TAG, "type:"+type+"��time:"+time);
			saveToDB(sendrerID,groupID,direction,type,time,content,showTime);
			handler.sendEmptyMessage(1);
			Log.i(TAG, "PersonChatActivity----���������³ɹ�");
		}
	}
	/**
	 * �ж��Ƿ����������
	 */
	@Override
	public boolean isChatting(Object info) {
		GroupChatMessage groupChatMessage=(GroupChatMessage) info;
		Log.i(TAG, "direction="+groupChatMessage.getDirection());
		Log.i(TAG, "time="+groupChatMessage.getTime());
		Log.i(TAG, "headimg="+groupChatMessage.getHeadImg());
		Log.i(TAG, "content="+groupChatMessage.getContent());
		return true;
	}
	
	
}
