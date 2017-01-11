package com.atm.chatonline.chat.ui;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.atm.chatonline.bbs.adapter.ExpressionPagerAdapter;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.adapter.ChatAdapter;
import com.atm.chatonline.chat.info.ChatMessage;
import com.atm.chatonline.chat.util.Config;
import com.atm.chatonline.chat.util.FileUtil;
import com.atm.chatonline.chat.util.TimeUtil;
import com.example.studentsystem01.R;




public class PersonChatActivity extends BaseActivity implements OnClickListener,ReceiveInfoListener,OnTouchListener{

	private String tag="PersonChatActivity";
	
	private ChatAdapter historyAdapter;
	private List<ChatMessage> messages=new ArrayList<ChatMessage>();
	private ListView chatHistoryLV;
	private Button btnSend;
	private Button btnBack;
	private ImageButton personal,expression;
	private ImageView btnPic;
	
	private EditText editMessage;
	private TextView nickNameTV;
	private String userID,nickName,sendStr;
	private static String friendID;
	private Handler pca_handler;
	private final static String STATE="P";
	private InputMethodManager mInputMethodManager;
	private Bitmap bm;
	private File fileImg;
	private Resources res;
	private LinearLayout ll_exp;
	private boolean isFaceShow = false;
	private ExpressionPagerAdapter pagerAdapter;
	private ViewPager exp_pager;
	private List<View> view;
	private GridView gird1,gird2;
	private SimpleAdapter adapter1,adapter2;
	private View viewPager1, viewPager2;
	private List<Map<String,Object>> listItems1,listItems2;
	private String[] description1, description2;
	private int[] imageIds1 = { R.drawable.exp00, R.drawable.exp01,
			R.drawable.exp02, R.drawable.exp03, R.drawable.exp04, R.drawable.exp05,
			R.drawable.exp06, R.drawable.exp07, R.drawable.exp08,R.drawable.exp09,
			R.drawable.exp10, R.drawable.exp11, R.drawable.exp12,
			R.drawable.exp13, R.drawable.exp14, R.drawable.exp15,
			R.drawable.exp16, R.drawable.exp17, R.drawable.exp18
			 };
	private int[] imageIds2 = { R.drawable.exp22, R.drawable.exp23,
			R.drawable.exp24, R.drawable.exp25, R.drawable.exp26,
			R.drawable.exp27, R.drawable.exp28, R.drawable.exp29,
			R.drawable.exp30, R.drawable.exp31, R.drawable.exp32,
			R.drawable.exp33};
	
	protected void onCreate(Bundle savedInstanceState){
		Log.i(tag, "PersonChatActivity-----�Ѿ�����PersonChatActivity");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.single_chat_view);
		Log.i(tag, "������userID�ǣ�"+userID+"��friendID:"+friendID+"��nickName:"+nickName);
		mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		Log.i(tag, "PersonChatActivity-----1111");
		if(con==null){
			Log.i(tag, "PersonChatActivity-----conΪ��");
		}
		con.addReceiveInfoListener(STATE,PersonChatActivity.this);
		pca_handler = new Handler(){
			public void handleMessage(Message msg){
				if(msg.what==Config.REFRESH_UI){
					historyAdapter.notifyDataSetChanged();
					LogUtil.p(tag, "PersonChatActivity--historyAdapter.notifyDataSetChanged()������");
					LogUtil.p(tag, "PersonChatActivity---����ChatAdapter��ʲô�仯");
				}
			}
		};
		getArray();
		initData();
		Log.i(tag, "PersonChatActivity---��FriendListActivity������userID:"+userID+"��friendID:"+friendID);
		initUI();
		try{
		accomplishExpBoard();
		}catch(Exception e){
			e.printStackTrace();
		}
		setAdapterForList();
		
	}
	
	//�Ѿ��Ķ�2015.8.25
	private void initData() {
		Intent intent = getIntent();
		
		
		userID = intent.getStringExtra("userID");
		Log.i(tag, "userID:"+intent.getStringExtra("userID"));
		friendID = intent.getStringExtra("friendId");
		Log.i(tag, "friendId:"+intent.getStringExtra("friendId"));
		nickName=intent.getStringExtra("nickName");
		Log.i(tag, "nickName:"+intent.getStringExtra("nickName"));
		bm=FileUtil.ByteToBitmap(intent.getByteArrayExtra("bm"));
		
	}

	void initUI(){
		chatHistoryLV=(ListView) findViewById(R.id.chat_view);
		editMessage=(EditText) findViewById(R.id.chat_editor);
		
		btnSend=(Button) findViewById(R.id.btn_submit);
		btnBack=(Button) findViewById(R.id.single_chat_back);
		personal=(ImageButton) findViewById(R.id.personal_message);
		expression=(ImageButton)findViewById(R.id.btn_emjio);
		btnPic=(ImageView)findViewById(R.id.btn_pic);
		/*LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.single_chat_title, null);*/
		nickNameTV = (TextView)findViewById(R.id.user_name);
		nickNameTV.setText(nickName);
		Log.i(tag, "PersonChatActivity-----��nickName����Ϊ:"+nickName);
		exp_pager = (ViewPager) findViewById(R.id.exp_pager);
		ll_exp = (LinearLayout)findViewById(R.id.ll_expression);
		btnSend.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnPic.setOnClickListener(this);
		personal.setOnClickListener(this);
		chatHistoryLV.setOnTouchListener(this);
		editMessage.setOnTouchListener(this);
		expression.setOnClickListener(this);
		Log.i(tag, "PersonChatActivity----initUI()�����ʼ�����");
	}
	
	void initMessages(){
		Log.i(tag, "PersonChatActivity-----5555");
		if(messages!=null){
			Log.i(tag, "initMessages()---messages��Ϊ�գ�������PersonChatActivity����һ��ʼ������");
		}
		messages=dbUtil.queryMessages(userID,friendID);
		
		Log.i(tag, "PersonChatActivity-----66666");
	}
	

	// ����Դ�ļ����»�ȡ�Ѷ���õ�����
	private void getArray() {
		// TODO Auto-generated method stub
		res = PersonChatActivity.this.getResources();
//		type = res.getStringArray(R.array.type);
		description1 = res.getStringArray(R.array.description1);
		description2 = res.getStringArray(R.array.description2);
	}
	
	//ʵ�ֱ������
	void accomplishExpBoard(){
		listItems1 = new ArrayList<Map<String,Object>>();
		listItems2 = new ArrayList<Map<String,Object>>();
		for(int i=0;i<imageIds1.length;i++){
			Map<String,Object> listItem1 = new HashMap<String,Object>();
			listItem1.put("image1",imageIds1[i]);
			listItems1.add(listItem1);
		}
		for(int i=0;i<imageIds2.length;i++){
			Map<String,Object> listItem2 = new HashMap<String,Object>();
			listItem2.put("image2",imageIds2[i]);
			listItems2.add(listItem2);
		}
		adapter1 = new SimpleAdapter(this,listItems1,R.layout.simple_item,
				new String[]{"image1"},new int[]{R.id.image});
		adapter2 = new SimpleAdapter(this,listItems2,R.layout.simple_item,
				new String[]{"image2"},new int[]{R.id.image});
		viewPager1 = View.inflate(this, R.layout.viewpager1, null);
		viewPager2 = View.inflate(this, R.layout.viewpager2, null);
		gird1 =(GridView)viewPager1.findViewById(R.id.grid1);
		gird2 =(GridView)viewPager2.findViewById(R.id.grid2);
		gird1.setAdapter(adapter1);
		gird2.setAdapter(adapter2);
		view = new ArrayList<View>();
		view.add(viewPager1);
		view.add(viewPager2);
		pagerAdapter = new ExpressionPagerAdapter(view);
		exp_pager.setAdapter(pagerAdapter);
		
		//��������еı���
		gird1.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						LogUtil.p(tag,"��grid1");
					
						Bitmap bitmap = null;
						bitmap = BitmapFactory.decodeResource(getResources(), imageIds1[arg2]);
						ImageSpan imageSpan = new ImageSpan(PersonChatActivity.this, bitmap);
						Log.i(tag, "#exp"+arg2);
						String str="";
						if(arg2<10){
							str="#exp0"+arg2;
						}else{
							str="#exp"+arg2;
						}
						SpannableString spannableString = new SpannableString(str);
						spannableString.setSpan(imageSpan, 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						editMessage.append(spannableString);
						// TODO Auto-generated method stub
						int index1 = Math.max(editMessage.getSelectionStart(), 0);
						LogUtil.p(tag,"index1��"+index1);
						String oriContent1 = editMessage.getText().toString();
						LogUtil.p(tag,"oriContent1��"+oriContent1);
						StringBuilder sBuilder1 = new StringBuilder(oriContent1);//����StringBuffer,���ٶȸ���
						LogUtil.p(tag, "arg1:"+arg1+".arg2:"+arg2);//arg2������λ��
						if (arg2 == 20) {//ɾ��ͼ��
							if (editMessage.getSelectionStart() > 0) {
								int selection = editMessage.getSelectionStart();
								String text2 = oriContent1.substring(selection - 1);
								if (")".equals(text2)) {//��ɾ���Ǳ����ʱ������ɾ��
									int start = oriContent1.lastIndexOf("#");
									int end = selection;
									editMessage.getText().delete(start, end);
								}
								// input.getText().delete(selection - 1, selection);
							}
						} else {
							sBuilder1.insert(index1, description1[arg2]);
//							editMessage.setText(sBuilder1.toString());
//							editMessage.setSelection(index1 + description1[arg2].length());
						}
					
					
					}
				});

				gird2.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						 LogUtil.p(tag,"��grid2");
						// TODO Auto-generated method stub
						 	Bitmap bitmap = null;
							bitmap = BitmapFactory.decodeResource(getResources(), imageIds2[arg2]);
							ImageSpan imageSpan = new ImageSpan(PersonChatActivity.this, bitmap);
							Log.i(tag, "#exp"+arg2);
							String str="";
							String oriContent2="";
							StringBuilder sBuilder2;
							int index2;
							if(arg2<12){
							int result=arg2+22;
							str="#exp"+result;
							
							SpannableString spannableString = new SpannableString(str);
							spannableString.setSpan(imageSpan, 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							editMessage.append(spannableString);
							// TODO Auto-generated method stub
							index2 = Math.max(editMessage.getSelectionStart(), 0);
							oriContent2 = editMessage.getText().toString();
							sBuilder2 = new StringBuilder(oriContent2);//����StringBuffer,���ٶȸ���
							LogUtil.p(tag, "arg1:"+arg1+".arg2:"+arg2);//arg2������λ��
							sBuilder2.insert(index2, description2[arg2]);
							}
							

					}
				});
}
	
	
	public void setAdapterForList(){
		initMessages();
		Log.i(tag, "PersonChatActivity-----77777");
		if(messages==null){
			messages = new ArrayList<ChatMessage>();
		}
		if(messages!=null){
			Log.i(tag, "setAdapterForList()����ʷ��¼������ˢ��");
			
		}
		
		historyAdapter=new ChatAdapter(this,messages);
		chatHistoryLV.setAdapter(historyAdapter);
		refreshUI();
		chatHistoryLV.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				chatHistoryLV.setTag(position);
				
				ChatMessage message=messages.get(position);
				int type=message.getType();
				String content = message.getContent();
				LogUtil.p(tag, "type:"+type+",content:"+content);
				switch(type){
			
				case Config.MESSAGE_IMG:{
					Intent intent = new Intent(PersonChatActivity.this,ImageShower.class);
					intent.putExtra("content", content);
					startActivity(intent);
				    }
					break;
				}
			}
		});
		Log.i(tag, "PersonChatActivity----chatHistoryLV�������������");
	}
	
	public void onClick(View v){
		int id = v.getId();
		if (id == R.id.btn_submit) {
			Log.i(tag, "PersonChatActivity----�ͻ����Ͷ���");
			String str = editMessage.getText().toString().trim();
			if (str != null
					&& (sendStr = str.replaceAll("\r", "").replaceAll("\t", "").replaceAll("\n", "")//���У���ҳ��tab�����������
							.replaceAll("\f", "")) != "") {
				Log.i(tag, "PersonChatActivity----����������:"+sendStr);
				new Thread(sendTextRunnable).start();
			}
			editMessage.setText("");
			
			//�Ѿ��Ķ���2015.8.25
			//ǿ�������б��õ�ͼƬ�������ݿ�
			//���浽���ݿ�
//			File file=FileUtil.createFriendFile(userID, Integer.parseInt(friendID));
			File file=FileUtil.createFriendFile(userID, friendID);
			Log.i(tag, "ͷ�񱣴�·��������������"+file.getAbsolutePath());
			FileUtil.saveBitmap(file, bm);
		} else if (id == R.id.single_chat_back) {
			con.removeReceiveInfoListener();
			PersonChatActivity.this.onBackPressed();
			
		}else if(id==R.id.personal_message){
			Intent intent=new Intent(PersonChatActivity.this,PersonalMessageActivity.class);
			intent.putExtra("friendID", friendID);
			startActivity(intent);
		}else if(id==R.id.btn_emjio){
			if(!isFaceShow){
				mInputMethodManager.hideSoftInputFromWindow(editMessage.getWindowToken(), 0);
				try{
					Thread.sleep(80);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				ll_exp.setVisibility(View.VISIBLE);
				isFaceShow = true;
			}else{
				ll_exp.setVisibility(View.GONE);
				isFaceShow = false;
			}
		}else if(id==R.id.btn_pic){
			//��ת��ͼƬ�������Ӧ�ã�ѡȡҪ���͵�ͼƬ
			Intent i = new Intent();
			i.setType("image/*");
			i.putExtra("return-data", true);
			i.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(i, Activity.DEFAULT_KEYS_SHORTCUT);
		}
	}
	
	public boolean onTouch(View v,MotionEvent event){
		switch (v.getId()) {
		case R.id.chat_view:   //ListView����ʵ��
			mInputMethodManager.hideSoftInputFromWindow(editMessage.getWindowToken(), 0);
//			faceLinearLayout.setVisibility(View.GONE);
			ll_exp.setVisibility(View.GONE);
			isFaceShow = false;
			Log.i(tag, "222222222222");
			
			break;
		case R.id.chat_editor:    //�������ʵ��
			mInputMethodManager.showSoftInput(editMessage, 0);
			ll_exp.setVisibility(View.GONE);
			isFaceShow = false;
			Log.i(tag, "333333333333333");
			
			break;

		default:
			break;
		}
		return false;
	}
	
	
	
	
	public void sendChatMessage(int type,String content){
//		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String time = TimeUtil.getAbsoluteTime();
		boolean result = true;
		if(type==Config.MESSAGE_TEXT){
			Log.i(tag, "PersonChatActivity----���εĶ���������type:Config.MESSAGE_TYPE_TXT"+",������:"+content);
			result = con.sendText(userID,friendID, time, content);
		}else if(type==Config.MESSAGE_IMG){
			result = con.sendImg(userID, friendID, time, content);
		}
		if(result==true){
			Log.i(tag, "���ŷ��ͳɹ����Ѷ�����ӵ�messages");
			messages.add(new ChatMessage(userID,friendID,nickName,Config.MESSAGE_TO,type,time,content,0));
			BaseActivity.saveMessagesToDB(userID,friendID,nickName,Config.MESSAGE_TO,type,time,content);
			Log.i(tag, "�Ѷ��Ŵ��뱾�ص����ݿ�ɹ�");
			refreshUI();
		}
	}
	//�ķ����ѸĹ���2015.8.25.11:37������ˡ��ǳơ��ֶ�--jackbing
//	public void saveMessagesToDB(String userID,String friendID,String nickName,int direction,int type,String time,String content){
//		Log.i(tag, "������ӵ�messages�ɹ����Ѷ��Ŵ��뱾�ص����ݿ�ķ���������");
////		DatabaseUtil db = new DatabaseUtil(this);
//		ContentValues values = new ContentValues();
//		values.put("self_Id",userID);
//		values.put("friend_Id",friendID);
//		values.put("friend_nickName", nickName);
//		values.put("direction",direction);
//		values.put("type",type);
//		values.put("time",time);
//		values.put("content",content);
//		values.put("showTime",0);
//		dbUtil.insertMessages(values);
//		Log.i(tag, "�հѶ��Ų��뱾�����ݿ⣬�������һ��");
//		dbUtil.queryMessages(userID, friendID);
//	}
	
	/*public boolean receive(ChatMessage chatMessage){
		Log.i(tag, "PersonChatActivity----chatMessage.getContent��"+chatMessage.getContent());
		Log.i(tag, "PersonChatActivity----chatMessage.getFriendID()��"+chatMessage.getFriendID());
		Log.i(tag, "PersonChatActivity----chatMessage.getFriendID()"+chatMessage.getFriendID()+"  ��friendID:"+friendID);
		if(chatMessage.getFriendID().equals(friendID)){
			return true;
		}
		return false;
	}*/
	//�ķ����ѸĹ���2015.8.25.11:37������ˡ��ǳơ�--jackbing
	public void processMessage(Message msg){
		LogUtil.p(tag, "PersonChatActivity----��������н��յ��µ���Ϣ");
		if(msg.what==Config.MESSAGE_FROM){
			LogUtil.p(tag, "PersonChatActivity----�յ�msg.what==Config.MESSAGE_FROM");
			Bundle bundle = msg.getData();
			ChatMessage newMessages = (ChatMessage)bundle.getSerializable("chatMessage");
			messages.add(newMessages);
//			Log.i(tag, "�����ҽ��յ��Ķ��ţ�����׼�����뱾�����ݿ⣬������"+newMessages.getContent());
//			String userID = newMessages.getSelfID();
//			String friendID = newMessages.getFriendID();
//			String nickName=newMessages.getNickName();
//			int direction = newMessages.getDirection();
//			int type = newMessages.getType();
//			String time = newMessages.getTime();
//			String content = newMessages.getContent();
//			
//			int showTime = newMessages.getShowTime();
//			Log.i(tag, "userID:"+userID+"��friendID:"+friendID+"direction"+direction);
//			Log.i(tag, "type:"+type+"��time:"+time+"showTime"+showTime);
//			saveMessagesToDB(userID,friendID,nickName,direction,type,time,content);
			BaseActivity.saveToDB(newMessages);
			refreshUI();
			Log.i(tag, "PersonChatActivity----���������³ɹ�");
		}else if(msg.what==Config.SEND_NOTIFICATION){
			LogUtil.p(tag, "PersonChatActivity----�յ�msg.what==Config.SEND_NOTIFICATION");
			Bundle bundle = msg.getData();
			ChatMessage otherMessage =(ChatMessage)bundle.getSerializable("chatMessage");
			BaseActivity.saveToDB(otherMessage);
			LogUtil.p(tag, "�Ѳ���ͬһ��������������Ϣ���뵽�����ݿ�");
			sendNotifycation();
//			messages.add(otherMessage);
		}
	}
	
//	void saveToDB(ChatMessage chatMessage){
//		Log.i(tag, "�����ҽ��յ��Ķ��ţ�����׼�����뱾�����ݿ⣬������"+chatMessage.getContent());
//		String userID = chatMessage.getSelfID();
//		String friendID = chatMessage.getFriendID();
//		String nickName=chatMessage.getNickName();
//		int direction = chatMessage.getDirection();
//		int type = chatMessage.getType();
//		String time = chatMessage.getTime();
//		String content = chatMessage.getContent();
//		int showTime = chatMessage.getShowTime();
//		Log.i(tag, "userID:"+userID+"��friendID:"+friendID+"direction"+direction);
//		Log.i(tag, "type:"+type+"��time:"+time+"showTime"+showTime);
//		saveMessagesToDB(userID,friendID,nickName,direction,type,time,content);
//	}
	
	Runnable sendTextRunnable = new Runnable(){
		public void run(){
			sendChatMessage(Config.MESSAGE_TEXT,sendStr);
			Log.i(tag, "PersonChatActivity----sendTextRunnable�̱߳�����");
		}
		
	};
	
	Runnable sendImgRunnable = new Runnable(){
		public void run(){
			sendChatMessage(Config.MESSAGE_IMG,fileImg.getAbsolutePath());
			Log.i(tag, "PersonChatActivity----sendImgRunnable�̱߳�����");
		}
		
	};
	
	public void refreshUI(){
		try{
			Message msg = new Message();
			msg.what = Config.REFRESH_UI;
			pca_handler.sendMessage(msg);
			Log.i(tag, "PersonChatActivity----refreshUI()������");
			}catch(Exception e){
				e.printStackTrace();
			}	
	}

	@Override
	public boolean isChatting(Object info) {
		ChatMessage chatMessage=(ChatMessage)info;//��objectתΪ��Ϣ
		Log.i(tag, "PersonChatActivity----chatMessage.getContent��"+chatMessage.getContent());
		Log.i(tag, "PersonChatActivity----chatMessage.getFriendID()��"+chatMessage.getFriendID());
		Log.i(tag, "PersonChatActivity----chatMessage.getFriendID()"+chatMessage.getFriendID()+"  ��friendID:"+friendID);
		Log.i(tag, "PersonChatActivity----chatMessage.nickName()��"+chatMessage.getNickName());
		if(chatMessage.getFriendID().equals(friendID)){
			return true;
		}
		return false;
	}
	
	protected void onActivityResult(int requestCode,int resultCode,Intent data){
		Log.i(tag, "requestCode:"+requestCode+",resultCode:"+resultCode);
		if(resultCode!=RESULT_OK)
			return;
		if(requestCode==Activity.DEFAULT_KEYS_SHORTCUT){
			Log.i(tag, "data:"+data);
			Uri uri = data.getData();
			Log.i(tag, "uri:"+uri.toString());
			fileImg = FileUtil.createImgFile(userID);
			boolean result =FileUtil.writeFile(getContentResolver(), fileImg, uri);
			LogUtil.p(tag, "ͼƬ������ͼƬд�뱾��SD����"+result);
//			sendChatMessage(Config.MESSAGE_IMG,fileImg.getAbsolutePath());
			new Thread(sendImgRunnable).start();
		}
	}
	
}
