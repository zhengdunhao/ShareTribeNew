package com.atm.chatonline.chat.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.atm.chatonline.chat.adapter.ScrollPageViewAdapter;
import com.atm.chatonline.chat.info.Friend;
import com.atm.chatonline.chat.info.Group;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;



@SuppressLint("ResourceAsColor")
public class SearchActivity extends BaseActivity implements
		OnClickListener,OnTouchListener{

//	private SlidingMenu slidingMenu;
	private RadioGroup radioGroup;
	private String title[] = { "Ⱥ", "�û�" };
	private LinearLayout linearLayout;
	
	private LinearLayout searchGroup;
	// private final int height = 70;
	private ArrayList<TextView> textViews;
	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private HorizontalScrollView horizontalScrollView;
	private ImageView imgViewMore;
	private TextView textView;
	private EditText searchText;
	private Button btnBack;
	
	private int width=0;

	private boolean isFaceShow = false;
	private InputMethodManager mInputMethodManager;
	private String condition;
	private int choose=0;//0��ʾ��Ҫ��Ⱥ  1��ʾ��Ҫ����
	private int click = 0;
	private boolean flag =false;//
	private String userId;
	private String tag="SearchActivity";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_group_view);
		SharedPreferences pref = getSharedPreferences("User",MODE_PRIVATE);
		userId=pref.getString("userID", "");
		width = getWindowManager().getDefaultDisplay().getWidth();//��ȡ��Ļ�ĳ���
		mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		
		linearLayout = (LinearLayout) findViewById(R.id.tab_view);// ��ȡbbs_main_view���Ӳ��֣�������������Ĳ���
		viewPager = (ViewPager) findViewById(R.id.pager);
		horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		
		horizontalScrollView.setPadding(width/3, 0, 0, 0);//�û�����У�width/3�Ǹ�����Ļ�ĳ����Լ���ť�ĳ������
		InItView();
		Log.i(tag,"ChatMainActivity--------�Ѿ�������GroupListActivity��FriendListActivity");
//		InItTitle();
//		setSelector(0);
		
		btnBack = (Button)findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);

		//����������ע������ΪȺ�Ĳ�Ҫ�ˣ�ֻ����һ�������û���2016-3-18��
//		viewPager.setAdapter(new ScrollPageViewAdapter(pageViews));// ���֮�������Ļ���һ���
//		viewPager.clearAnimation();
//		viewPager.setOnPageChangeListener(new OnPageChangeListener() {// ����ǵ����ͬ��Ƭ����ת��ͬ��ҳ��
//
//					@Override
//					public void onPageSelected(int arg0) {
//						choose=arg0;
//						setSelector(arg0);
//						Log.i(tag, "��onPageSelected�������");
//						
//						if(flag==true){
//							if(click==0){
//								Log.i(tag, "onPageSelected----choose:"+choose);
//								new Thread(runnable).start();
//								
//								
//							}else if(click==1){
//								Log.i(tag, "onPageSelected----choose:"+choose);
//								new Thread(runnable).start();
//								
//							}
//						}
//						
//						
//					}
//
//					@Override
//					public void onPageScrolled(int arg0, float arg1, int arg2) {
//					}
//
//					@Override
//					public void onPageScrollStateChanged(int arg0) {
//					}
//				});
//		
//		viewPager.setOnClickListener(this);
//		
//		viewPager.setOnTouchListener(new OnTouchListener(){
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				closeInput(SearchActivity.this.getCurrentFocus());
//				return false;
//			}
//			
//		});
		
	
		searchText = (EditText)findViewById(R.id.search_edit);
		
		searchText.setOnTouchListener(this);
		Log.i(tag, "999999999");
		
		searchText.setOnEditorActionListener(new OnEditorActionListener(){
			public boolean onEditorAction(TextView v,int actionId,KeyEvent event){
				if(actionId == EditorInfo.IME_ACTION_SEARCH){
					closeInput(v);
					new Thread(runnable).start();
					Log.i(tag, "���̵�������ť�����");
					flag=true;
					
					return true;
				}
				return false;
			}
		});
		
	}
	public boolean onTouchEvent(MotionEvent event){
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			if(SearchActivity.this.getCurrentFocus()!=null){
				if(SearchActivity.this.getCurrentFocus().getWindowToken()!=null){
					closeInput(SearchActivity.this.getCurrentFocus());
					Log.i(tag, "״̬�������");
				}
			}
		}
		return super.onTouchEvent(event);
	}

	void InItView() {

		pageViews = new ArrayList<View>();
		
//		Intent intent1=new Intent(this, SearchGroupListActivity.class);
//		intent1.putExtra("userId", userId);
//		View view01 = getLocalActivityManager().startActivity(
//				"SearchGroupListActivity", intent1)
//				.getDecorView();
		Intent intent2=new Intent(this, SearchFriendListActivity.class);
		intent2.putExtra("userId", userId);
		View view02 = getLocalActivityManager().startActivity(
				"SearchFriendListActivity",
				intent2).getDecorView();
		Log.i(tag,
				"ChatMainActivity--------��ʼ����SearchGroupListActivity��SearchFriendListActivity");
//		pageViews.add(view01);
		pageViews.add(view02);
//		new Thread(groupHobbyRunnable).start();
		
		
		
	}

	@SuppressLint("ResourceAsColor")
	void InItRadioButton() {
		int width = getWindowManager().getDefaultDisplay().getWidth() / 6;// ��ƽ������
		for (int i = 0; i < title.length; i++) {
			RadioButton radioButton = new RadioButton(this, null,
					R.style.radioButton);
			radioButton.setTextSize(17);
			radioButton
					.setTextColor(R.color.black);
			radioButton.setWidth(width);
			radioButton.setGravity(Gravity.CENTER);
			radioGroup.addView(radioButton);
		}
	}

	void InItTitle() {
		textViews = new ArrayList<TextView>();
		int width = getWindowManager().getDefaultDisplay().getWidth() / 6;
		int height = 70;
		for (int i = 0; i < title.length; i++) {
			textView = new TextView(this);
			textView.setText(title[i]);
			textView.setTextSize(17);
			textView.setTextColor(R.color.black);
			textView.setWidth(width);
			textView.setGravity(Gravity.CENTER);
			textView.setId(i);
			textView.setOnClickListener(new TextView.OnClickListener() {// ��������
				public void onClick(View v) {
					setSelector(v.getId());
				}

			}); // ÿ�ε��textview���д�����Ӧ���������setSelector(int
				// id),idΪ���٣��������ĸ�textview
			textViews.add(textView);
			// �ָ���

			// �Ҹ�����Ϊ�������ģ�����ͼ����ͼ˵����Ҫ�ڸ���ͼ����ռ�������ͼ��textview������ͼ���Ǹ�����LinearLayout
			// textview����פLinearLayout��Ҫͨ��LayoutParam�����ݴ�С��

			View view = new View(this);
			LinearLayout.LayoutParams layoutParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.width = 1;
			layoutParams.height = height - 40;
			layoutParams.gravity = Gravity.CENTER;
			view.setLayoutParams(layoutParams);
			
			view.setBackgroundColor(R.color.gray);
			linearLayout.addView(textView);
			

		}
	}
	public void setSelector(int id) {
		for (int i = 0; i < title.length; i++) {
			if (id == i) {
//				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
//						R.drawable.grouplist_item_bg_normal1);
//				textViews.get(id).setBackgroundDrawable(
//						new BitmapDrawable(bitmap));
				textViews.get(id).setBackgroundResource(R.drawable.radiobutton);
//				textViews.get(id).setBackgroundColor(Color.GREEN);
				
				textViews.get(id).setTextColor(Color.WHITE);//ѡ������ʾ��ɫ
				
				
				viewPager.setCurrentItem(i);
				
				
			}

			else {
				textViews.get(i).setBackgroundDrawable(new BitmapDrawable());
				textViews.get(i).setTextColor(R.color.black);
			}
		}
	}
	
	Runnable runnable = new Runnable(){	//ֻ�������뷨��������ť��Ч����ֱ�Ӱ���Ƭ�л���Ч
		public void run(){
			String searchTxt = searchText.getText().toString();
			Log.i(tag, "������������:"+searchTxt);
			if(!searchTxt.equals("")){
//			if(choose==0){		//��Ⱥ
//				con.findCrowd(searchTxt,userId);
//				click=1;
//			}else if(choose==1){	//����
//				con.findUser(searchTxt,userId);
//				click=0;
//			}
			con.findUser(searchTxt, userId);
			Log.i(tag, "searchTxt:"+searchTxt);
			}
			
		}
	};
	Runnable groupHobbyRunnable = new Runnable(){
		public void run(){
			Log.i(tag, "����SearchActivity�������͸���Ȥ��Ⱥ");
			con.reqHobbyGroup(userId);
			Log.i(tag, "����SearchActivity�������͸���Ȥ���û�");
			con.reqHobbyUser(userId);
		}
	};
	
	public void processMessage(Message msg){
		Log.i(tag, "process���{��");
		if(msg.what==Config.CROWD_FIND_SUCCESS){
			Bundle bundle = msg.getData();
			ArrayList  groupList = bundle.getParcelableArrayList("groupList");
			ArrayList<Group> listGroup = (ArrayList<Group>)groupList.get(0);
			if(listGroup.size()!=0){
				Log.i(tag, "Bundle����ArrayList�ǳɹ���");
			}
			
			if(getLocalActivityManager().getActivity("SearchGroupListActivity")!=null){
				Log.i(tag, "SearchGroupListActivity��Ϊ��");
				SearchGroupListActivity group = (SearchGroupListActivity)getLocalActivityManager().getActivity("SearchGroupListActivity");
				group.showGroupList(listGroup);
			}
		}else if(msg.what==Config.USER_FIND_SUCCESS){
			Bundle bundle = msg.getData();
			ArrayList friendList = bundle.getParcelableArrayList("friendList");
			if(friendList.size()!=0){
				Log.i(tag, "friendList.size()��Ϊ��");
			}
			ArrayList<Friend> listFriend = (ArrayList<Friend>)friendList.get(0);
			if(listFriend.size()!=0){
				Log.i(tag, "Bundle����ArrayList�ǳɹ���");
			}
			if(getLocalActivityManager().getActivity("SearchFriendListActivity")!=null){
				Log.i(tag, "SearchFriendListActivity��Ϊ��");
				SearchFriendListActivity friend = (SearchFriendListActivity)getLocalActivityManager().getActivity("SearchFriendListActivity");
				friend.showFriendList(listFriend);
			}
			
		}
		else if(msg.what==Config.NOT_FOUND){
			Log.i(tag, "û���ҵ���ص���Ϣ");
			Toast.makeText(SearchActivity.this, "û���ҵ���ص���Ϣ", Toast.LENGTH_SHORT).show();
		}else if(msg.what==Config.NOT_FOUND_HOBBY_GOURP){
			Log.i(tag, "û���ҵ������Ȥ��Ⱥ");
			Toast.makeText(SearchActivity.this, "û���ҵ������Ȥ��Ⱥ", Toast.LENGTH_SHORT).show();
		}else if(msg.what==Config.NOT_FOUND_HOBBY_USER){
			Log.i(tag, "û���ҵ������Ȥ���û�");
			Toast.makeText(SearchActivity.this, "û���ҵ������Ȥ���û�", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		
		case R.id.search_edit:    //�������ʵ��
			mInputMethodManager.showSoftInput(searchText, 0);
			Log.i(tag, "333333333333333");
			break;
		}
		return false;
	}
	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_back:
			SearchActivity.this.onBackPressed();
			break;
//		case R.id.pager:
//			Log.i(tag, "������");
//			break;

		}
	}
	
	public void switchInput(View v){
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	public void openInput(View v){
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(findViewById(R.id.search_edit), InputMethodManager.SHOW_FORCED);
	}
	
	public boolean closeInput(View v){
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
	
}
