package com.atm.chatonline.chat.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.atm.chatonline.chat.adapter.ScrollPageViewAdapter;
import com.example.studentsystem01.R;




@SuppressLint("ResourceAsColor")
public class BBSMainActivity extends ActivityGroup implements OnClickListener{
//	private SlidingMenu slidingMenu;
	private RadioGroup radioGroup;
	private String title[] = { "����", "Ժϵ","��ע" };
	private LinearLayout linearLayout;
//	private final int height = 70;
	private ArrayList<TextView> textViews;
	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private HorizontalScrollView horizontalScrollView;
	private ImageView imgViewMore;
	private TextView textView;
	private String tag="";
	
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(tag, "ChatMainActivity--------��������");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_main_view);
		linearLayout = (LinearLayout) findViewById(R.id.tab_view);//��ȡbbs_main_view���Ӳ��֣�������������Ĳ���
		viewPager = (ViewPager) findViewById(R.id.pager);
		horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		InItView();
		Log.i(tag, "ChatMainActivity--------�Ѿ�������GroupListActivity��FriendListActivity");
		InItTitle();
		setSelector(0);
//		InItSlidingMenu();
		
		imgViewMore=(ImageView)findViewById(R.id.imgView_more);
		imgViewMore.setOnClickListener(this);
		
		viewPager.setAdapter(new ScrollPageViewAdapter(pageViews));//���֮�������Ļ���һ���
		viewPager.clearAnimation();
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {//����ǵ����ͬ��Ƭ����ת��ͬ��ҳ��

			@Override
			public void onPageSelected(int arg0) {
				setSelector(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}
	/**
	 *��ʼ�������˵�
	 * 
	 * */
//	
//	void InItSlidingMenu(){
//		slidingMenu = new SlidingMenu(this);
//        slidingMenu.setMode(SlidingMenu.LEFT);
//        slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
//        slidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);//���ô�����Χ TOUCHMODE_FULLSCREEN��ȫ�֣� 
//        slidingMenu.attachToActivity(this, SlidingMenu.RIGHT);//SLIDING_CONTENT
//        slidingMenu.setMenu(R.layout.left_sliding_menu_view);
//	}
    
	/***
	 * ��ʼ��С��ͼ�������֣�Ժϵ����ע����ͼ
	 * ���ص�pageViews
	 */
	void InItView() {	//��ϼ���������Լ�Ҫ�����������࣬�ֱ��Ƿ��֣�Ժϵ���Լ���ע������������Ⱥ�ĺ�˽��������
		
		pageViews = new ArrayList<View>();
		View view01 = getLocalActivityManager().startActivity("GroupListActivity",
				new Intent(this, GroupListActivity.class)).getDecorView();
		
		View view02 = getLocalActivityManager().startActivity("FrinedListActivity",
				new Intent(this, FriendListActivity.class)).getDecorView();
		Log.i(tag, "ChatMainActivity--------��ʼ����GroupListActivity��FriendListActivity");
		
		View view03 = getLocalActivityManager().startActivity("FrinedListActivity",
				new Intent(this, FriendListActivity.class)).getDecorView();
		Log.i(tag, "ChatMainActivity--------��ʼ����GroupListActivity��FriendListActivity");
		
		
		pageViews.add(view01);
		pageViews.add(view02);
		pageViews.add(view03);
		
	}
    /***
     * ��ʼ��radiobutton������ÿ��radiobutton�ĳ��ȣ����տ�ʼ��ʼ��ʱ����һ���Ǳ꼴���ֱ���ʼ��Ϊ��ɫ������Ϊ��ɫ
     * ������radiobutton����radioGroup��
     */
	@SuppressLint("ResourceAsColor")
	void InItRadioButton() {
		int width = getWindowManager().getDefaultDisplay().getWidth() / 3;//��ƽ������
		for (int i = 0; i < title.length; i++) {
			RadioButton radioButton = new RadioButton(this, null,
					R.style.radioButton);
																	//radioButton.setText(title[i]);������ʡ����
			radioButton.setTextSize(17);
			radioButton.setTextColor(R.color.black);
			radioButton.setWidth(width);
//			radioButton.setHeight(height);
			radioButton.setGravity(Gravity.CENTER);
			radioGroup.addView(radioButton);
		}
	}
	 /***
     * init title
     * 
     */
	@SuppressLint("ResourceAsColor")
	void InItTitle() {
		textViews = new ArrayList<TextView>();
		int width = getWindowManager().getDefaultDisplay().getWidth() / 3;
		int height = 70;
		for (int i = 0; i < title.length; i++) {
			textView = new TextView(this);
			textView.setText(title[i]);
			textView.setTextSize(17);
			textView.setTextColor(R.color.black);
			textView.setWidth(width);
//			textView.setHeight(height - 10);
			textView.setGravity(Gravity.CENTER);
			textView.setId(i);
			textView.setOnClickListener(new TextView.OnClickListener(){//��������    
	            public void onClick(View v) {    
	            	setSelector(v.getId());
	            }    
	  
	        });  //ÿ�ε��textview���д�����Ӧ���������setSelector(int id),idΪ���٣��������ĸ�textview
			textViews.add(textView);
			// �ָ���
			
			//�Ҹ�����Ϊ�������ģ�����ͼ����ͼ˵����Ҫ�ڸ���ͼ����ռ�������ͼ��textview������ͼ���Ǹ�����LinearLayout
			//textview����פLinearLayout��Ҫͨ��LayoutParam�����ݴ�С��
			
			View view = new View(this);
			LinearLayout.LayoutParams layoutParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.width = 1;
			layoutParams.height = height - 40;
			layoutParams.gravity = Gravity.CENTER;
			view.setLayoutParams(layoutParams);
			view.setBackgroundColor(R.color.gray);
			linearLayout.addView(textView);
//			if (i != title.length - 1) {
//				linearLayout.addView(view);
//			}

		}
	}

	/***
	 * ѡ��Ч��
	 * ���˾��ã���������ʼ�����ã�����һ���ȸ��Ǳ�Ϊ0��textview��Ȼ��Ǳ�Ϊ0��textview�ȱ�ɺ�ɫ�����������Ū�ɻ�ɫ
	 * ȫ��textview����Ū
	 */
	public void setSelector(int id) {
		for (int i = 0; i < title.length; i++) {
			if (id == i) {
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.grouplist_item_bg_normal);
				textViews.get(id).setBackgroundDrawable(
						new BitmapDrawable(bitmap));
				textViews.get(id).setTextColor(Color.GREEN);//ѡ������ʾ��ɫ
				viewPager.setCurrentItem(i);
			}

			else {
				textViews.get(i).setBackgroundDrawable(new BitmapDrawable());
				textViews.get(i).setTextColor(R.color.black);
			}
		}
	}

	
	

	
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.imgView_more) {
			//slidingMenu.toggle(true);
		}
	}
	
	
	

}
