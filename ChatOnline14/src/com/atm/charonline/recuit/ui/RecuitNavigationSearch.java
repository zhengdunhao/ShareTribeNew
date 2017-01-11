/**
 * 
 */
package com.atm.charonline.recuit.ui;

import java.util.ArrayList;

import com.atm.chatonline.bbs.activity.bbs.BBSListView;
import com.atm.chatonline.bbs.activity.bbs.SearchUserView;
import com.atm.chatonline.bbs.adapter.ScrollPageViewAdapter;
import com.atm.chatonline.bbs.util.ExtendsIntent;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView.OnEditorActionListener;

/**
 * @�� com.atm.chatonline.activity.bbs ---NavigationSearch
 * @����	BBS����������������
 * @���� ��С��
 * @ʱ�� 2015-8-18
 * 
 */
@SuppressLint("ResourceAsColor")
public class RecuitNavigationSearch extends BaseActivity implements
OnClickListener,OnTouchListener{
	private RadioGroup radioGroup;
	private String title[] = { "��Ƹ", "��ְ" };
	private LinearLayout linearLayout;
	
	private ArrayList<TextView> textViews;
	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private HorizontalScrollView horizontalScrollView;
	private TextView textView;
	private EditText searchText;
	private Button btnBack;
	private String [] relativePath = {"recuit_getRecuit.action","apply_getApply.action"};
	private int width=0;
	

	private InputMethodManager mInputMethodManager;
	private ExtendsIntent intent1,intent2;
	private String searchInfo;
	private View view01;
	private View view02;
	private ScrollPageViewAdapter viewAdapter;
	private int nums=0;

	private String tag="RecuitSearchActivity";
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bbs_search_view);
		width = getWindowManager().getDefaultDisplay().getWidth();//��ȡ��Ļ�ĳ���
		mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		
		linearLayout = (LinearLayout) findViewById(R.id.tab_view);// ��ȡbbs_main_view���Ӳ��֣�������������Ĳ���
		viewPager = (ViewPager) findViewById(R.id.pager);
		horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		
		horizontalScrollView.setPadding(width/3, 0, 0, 0);//�û�����У�width/3�Ǹ�����Ļ�ĳ����Լ���ť�ĳ������
		InItView();
		Log.i(tag,"ChatMainActivity--------�Ѿ�������GroupListActivity��FriendListActivity");
		InItTitle();
		setSelector(0);
		
		btnBack = (Button)findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);

		viewAdapter = new ScrollPageViewAdapter(pageViews);
		viewPager.setAdapter(viewAdapter);// ���֮�������Ļ���һ���
		viewPager.clearAnimation();
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {// ����ǵ����ͬ��Ƭ����ת��ͬ��ҳ��
//
//					@Override
					public void onPageSelected(int arg0) {
//						choose=arg0;
						setSelector(arg0);
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
						
						
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
					}
				});
		
		viewPager.setOnClickListener(this);
		//�����л�ѡ�ʱ��ϵͳ�Դ�������̻�����
		viewPager.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				closeInput(RecuitNavigationSearch.this.getCurrentFocus());//ϵͳ�Դ�������̻�����
				return false;
			}
			
		});
		
	
		searchText = (EditText)findViewById(R.id.bbs_search_edit);
		
		searchText.setOnTouchListener(this);
		Log.i(tag, "999999999");
		
		//����������
		searchText.setOnEditorActionListener(new OnEditorActionListener(){
			public boolean onEditorAction(TextView v,int actionId,KeyEvent event){
				if(actionId == EditorInfo.IME_ACTION_SEARCH){ //�ж�������Ƿ����������ť�������������runnable�������ݷ�����̨����ͬʱ������̸�����
					closeInput(v);
					searchInfo = searchText.getText().toString();
					if(nums!=0) {

						 Message msg4 = new Message();
						 msg4.what = Config.SEARCHESSAY;
						 RecuitListView.setTip(searchInfo);
						 RecuitListView.getHandler().sendMessage(msg4);
						 Message msg5 = new Message();
						 msg5.what = Config.SEARCHESSAY;
						 ApplyListView.setTip(searchInfo);
						 ApplyListView.getHandler().sendMessage(msg5);
					}else {
					getExtendsIntent();
					view01 = getLocalActivityManager().startActivity(
							"SearchGroupListActivity", new Intent(intent1)).getDecorView();
				
					view02 = getLocalActivityManager().startActivity(
							"SearchFrinedListActivity",new Intent(intent2)).getDecorView();
					Log.i(tag,
							"ChatMainActivity--------��ʼ����SearchGroupListActivity��SearchFriendListActivity");
					pageViews.add(view01);
					pageViews.add(view02);
					viewAdapter.notifyDataSetChanged();
					nums++;
					}
					return true;
				}
				return false;
			}
		});
		
	}
	public void getExtendsIntent() {
		Log.d("˳��","getExtendsIntentk");
		intent1 = new ExtendsIntent(this,RecuitListView.class,
				searchInfo, relativePath[0], searchInfo, 1);
		intent2 = new ExtendsIntent(this,ApplyListView.class,
				searchInfo, relativePath[1], searchInfo , 1);
	}
	//���������������̻�����
	public boolean onTouchEvent(MotionEvent event){
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			if(RecuitNavigationSearch.this.getCurrentFocus()!=null){
				if(RecuitNavigationSearch.this.getCurrentFocus().getWindowToken()!=null){
					closeInput(RecuitNavigationSearch.this.getCurrentFocus());
					Log.i(tag, "״̬�������");
				}
			}
		}
		return super.onTouchEvent(event);
	}

	void InItView() {
//		String searchInfo = searchText.getText().toString();
//		ExtendsIntent intent1 = new ExtendsIntent(this,NavigationSearch.class,
//				searchInfo, relativePath[0], "" , 1);
//		ExtendsIntent intent2 = new ExtendsIntent(this,NavigationSearch.class,
//				searchInfo, relativePath[1], "" , 1);
		pageViews = new ArrayList<View>();
//		view01 = getLocalActivityManager().startActivity(
//				"SearchGroupListActivity", new Intent(this,NavigationSearchPost.class)).getDecorView();
//		view02 = getLocalActivityManager().startActivity(
//				"SearchFrinedListActivity",new Intent(this,NavigationSearchUser.class)).getDecorView();
//		Log.i(tag,
//				"ChatMainActivity--------��ʼ����SearchGroupListActivity��SearchFriendListActivity");
//		pageViews.add(view01);
//		pageViews.add(view02);
		
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

	@SuppressLint("ResourceAsColor")
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
	public void processMessage(Message msg){
	}
	
	@SuppressLint("ResourceAsColor")
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
			RecuitNavigationSearch.this.onBackPressed();
			break;
//		case R.id.pager:
//			Log.i(tag, "������");
//			break;

		}
	}
	
	//���ò���ò���
	public void switchInput(View v){
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}
	//��ʾ�����
	public void openInput(View v){
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(findViewById(R.id.search_edit), InputMethodManager.SHOW_FORCED);
	}
	//���������
	public boolean closeInput(View v){
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
	
}
