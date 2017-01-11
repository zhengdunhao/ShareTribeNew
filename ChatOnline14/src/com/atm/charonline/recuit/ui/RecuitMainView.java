
package com.atm.charonline.recuit.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.charonline.recuit.util.BBSConnectNet;
import com.atm.chatonline.bbs.adapter.ScrollPageViewAdapter;
import com.atm.chatonline.bbs.util.ExtendsIntent;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.bbs.util.SendLoginInfo;
import com.atm.chatonline.bbs.util.SharedPreferenceUtils;
import com.atm.chatonline.bbs.util.SlidingTitleLayout;
import com.atm.chatonline.chat.info.User;
import com.atm.chatonline.chat.net.Communication;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.Config;
import com.atm.chatonline.chat.util.FileUtil;
import com.atm.chatonline.recuit.adapter.RecuitAdapter;
import com.example.studentsystem01.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * @�� com.atm.charonline.recuit.ui ---RecuitMainView
 * @����
 * @���� ��С��
 * @ʱ�� 2015-10-26
 * 
 */
@SuppressLint("ResourceAsColor")
public class RecuitMainView extends BaseActivity {
	private SlidingMenu slidingMenu;
	private RadioGroup radioGroup;
	private String title[] = { "��Ƹ", "��ְ"};
	private LinearLayout linearLayout;
	private ArrayList<TextView> textViews;
	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private ImageView imgViewMore, imgViewEdit, imgViewSearch;
	private HorizontalScrollView horizontalScrollView;
	private TextView textView;
	private View view01;
	private static View view02;
	private User user;
	private String userID;
	private Activity acitivity;
	private String[] relativePath = { "recuit_getRecuit.action",
	"apply_getApply.action" };
	private SlidingTitleLayout slidingTitleLayout;
	private Spinner sp;
	private int nums = 0;

	private static Handler handler;
	public static Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	private ExtendsIntent[] intent;
	private ProgressDialog progressDialog;
//	private InitView initview;

	
	//recuit
	private String id="";
	private String tip = "";
	
	private BBSConnectNet bBSConnectNet;
	private RecuitAdapter recuitAdapter;
	private PullToRefreshListView plv;
	private int recuitNums = 0;
	private String response;
	private String cookie;
	private TextView recuit_wait;
	private FrameLayout recuitLayout;
	private ScrollPageViewAdapter pageAdapter;
	private TextView recuitWait;

	public static View getView02() {
		return view02;
	}

	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.bbs_main_view);
	sp = (Spinner) findViewById(R.id.sp_workType);
	recuitWait = (TextView)findViewById(R.id.bbs_wait);
	linearLayout = (LinearLayout) findViewById(R.id.tab_view);// ��ȡbbs_main_view���Ӳ��֣�������������Ĳ���
	viewPager = (ViewPager) findViewById(R.id.pager);
	slidingTitleLayout=(SlidingTitleLayout)findViewById(R.id.sliding_title_layout);
	slidingTitleLayout.showBadgeView(SharedPreferenceUtils.getInstance().getNewMessage(getApplicationContext()));
	horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
	pageViews = new ArrayList<View>();
	pageAdapter=new ScrollPageViewAdapter(pageViews);
	intent = new ExtendsIntent[30];
	new GetDataTask1().execute();
//	InItView();
	InItTitle();
//	initUserHead();
	setSelector(0);
//	// ������ʾ��
//	progressDialog = new ProgressDialog(BBSMainView.this);
//	progressDialog.setTitle("�ٵȵȣ��������");
//	progressDialog.setMessage("Loading...");
//	progressDialog.setCancelable(true);
//	progressDialog.show();

	initData();
	if (con == null) {
		LogUtil.d( "conΪnull");

	} else {
		LogUtil.d( "con��Ϊnull");
	}
//	InItView();
	
	

	viewPager.setAdapter(pageAdapter);// ���֮�������Ļ���һ���
	viewPager.clearAnimation();
	viewPager.setOnPageChangeListener(new OnPageChangeListener() {// ����ǵ����ͬ��Ƭ����ת��ͬ��ҳ��

				@Override
				public void onPageSelected(int arg0) {
					LogUtil.d( "viewPager-1");
					setSelector(arg0);
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					LogUtil.d( "viewPager-2");
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
					LogUtil.d( "viewPager-3");
				}
			});
	handler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what==Config.MAINVIEW_UPDATEUI){
				pageViews.add(view01);
				pageViews.add(view02);
				pageAdapter.notifyDataSetChanged();
				nums++;
			}
		}
	};
    sp.setOnItemSelectedListener(
    		new OnItemSelectedListener() {
    			public void onItemSelected(AdapterView<?> parent, 
    				View view, int position, long id) {
    				LogUtil.d("�����spinner");
//    				String a = "Spinner1: position="  + position  + " id="  + id;
    				LogUtil.d("�����spinner");
//    				MyToast toast=MyToast.makeText(getApplicationContext(), a, Toast.LENGTH_SHORT);
//    				toast.show();
    				LogUtil.d("�����spinner");
//    				recuitList.clear();
    				switch(position) {
    				case 0:
        				LogUtil.d("�����ȫ��");
    					tip="ȫ��";
//    					 Message msg1 = new Message();
//    					 msg1.what = Config.ALLKIND; 
//    					 if(RecuitListView.getHandler()!=null)
//    					 RecuitListView.getHandler().sendMessage(msg1);
//    					 if( ApplyListView.getHandler()!=null)
//    					 ApplyListView.getHandler().sendMessage(msg1);
    					 changeWorkType(Config.ALLKIND);
    					break;
    				case 1:
        				LogUtil.d("�����ʵϰ");
//    					tip="ʵϰ";
//   					 Message msg5 = new Message();
//   					 msg5.what = Config.INTERNSHIP;
//					 if(RecuitListView.getHandler()!=null)
//   					 RecuitListView.getHandler().sendMessage(msg5);
//					 if( ApplyListView.getHandler()!=null)
//					 ApplyListView.getHandler().sendMessage(msg5);
					 changeWorkType(Config.INTERNSHIP);
    					break;
    				case 2:
        				LogUtil.d("����˼�ְ");
    					tip="��ְ";
//   					 Message msg2 = new Message();
//   					 msg2.what = Config.PARTTIME;
//					 if(RecuitListView.getHandler()!=null)
//   					 RecuitListView.getHandler().sendMessage(msg2);
//					 if( ApplyListView.getHandler()!=null)
//					 ApplyListView.getHandler().sendMessage(msg2);
					 changeWorkType(Config.PARTTIME);
    					break;
    				case 3:
        				LogUtil.d("�����ȫְ");
    					tip="ȫְ";
//   					 Message msg3 = new Message();
//   					 msg3.what = Config.FULLTIME;
//					 if(RecuitListView.getHandler()!=null)
//   					 RecuitListView.getHandler().sendMessage(msg3);
//					 if( ApplyListView.getHandler()!=null)
//					 ApplyListView.getHandler().sendMessage(msg3);
					 changeWorkType(Config.FULLTIME);
    					break;
    				default:
        				LogUtil.d("�����ȫ��r");
    					tip="ȫ��";
//   					 Message msg4 = new Message();
//   					 msg4.what = Config.ALLKIND;
//					 if(RecuitListView.getHandler()!=null)
//   					 RecuitListView.getHandler().sendMessage(msg4);
//					 if( ApplyListView.getHandler()!=null)
//					 ApplyListView.getHandler().sendMessage(msg4);
					 changeWorkType(Config.ALLKIND);
    				}
    				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					 changeWorkType(Config.ALLKIND);
				}
    			});
}
/**
	 * @param allkind
	 */
	protected void changeWorkType(int allkind) {
			 Message msg4 = new Message();
			 msg4.what = allkind;
		 if(RecuitListView.getHandler()!=null)
			 RecuitListView.getHandler().sendMessage(msg4);
		 Message msg5 = new Message();
		 msg5.what = allkind;
		 if( ApplyListView.getHandler()!=null)
		 ApplyListView.getHandler().sendMessage(msg5);
		
	}

/*	class InitView extends AsyncTask<Void,Integer,Boolean>{
	@Override
	protected void onPreExecute(){
		Log.i(tag, "--------------first");
		progressDialog.show();
		Log.i(tag,"---------progressDialog.show()");
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		Log.i(tag, "------------------second");
		try{
		Looper.prepare();
		view01 = getLocalActivityManager().startActivity("activity01",
				new Intent(intent1)).getDecorView();
		Log.i(tag, "-------!!!!!!!!!!");
		view02 = getLocalActivityManager().startActivity("activity02",
				new Intent(BBSMainView.this, BBSDepartmentView.class))
				.getDecorView();
		Log.i(tag, "---------@@@@@@@@@2");
		view03 = getLocalActivityManager().startActivity("activity03",
				new Intent(intent2)).getDecorView();
		Log.i(tag, "---------@@@@@@@@@333");
		
		viewPager.setAdapter(new ScrollPageViewAdapter(pageViews));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	protected void onProgressUpdate(){
//		Log.i(tag, "------------third");
	}
	
	protected void onPostExecute(Boolean result){
		Log.i(tag, "-----------four");
		if(result==true){
		pageViews.add(view01);
		pageViews.add(view02);
		pageViews.add(view03);
		
		viewPager.setAdapter(pageAdapter);// ���֮�������Ļ���һ���
		Log.i(tag, "-----------six");
//		viewPager.clearAnimation();
//		viewPager.setOnPageChangeListener(new OnPageChangeListener() {// ����ǵ����ͬ��Ƭ����ת��ͬ��ҳ��
//
//					@Override
//					public void onPageSelected(int arg0) {
//						setSelector(arg0);
//					}
//
//					@Override
//					public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//					}
//
//					@Override
//					public void onPageScrollStateChanged(int arg0) {
//
//					}
//				});
		}
		progressDialog.dismiss();
		Log.i(tag, "-----------five");
	}
	
}
*/
private void initData() {
	user = getPreference();
	userID = user.getUserID();
	Bundle bundle = getIntent().getExtras();
//	if (bundle != null) {
//		int login = bundle.getInt("login");
//		Log.i("BBSMainView", "isAutoLogin is " + login);
//		if (login == Config.AUTOLOGIN) {
//			LogUtil.d("login==Config.AUTOLOGIN");
//			initAutoLogin();
//		}
//	}
	// boolean result = dbUtil.queryIsFriendList(userID);
	// if(!result){
	// Log.i(tag, "��������б�û��¼");
	// // new Thread(myAttention).start();
	// }

}

private void initAutoLogin() {// ������¼����
	LogUtil.d("������¼����");
	new Thread(runnable).start();

}

public void initUserHead() {
	if (FileUtil.isFile(userID)) {
		LogUtil.d( "initUserHead()�ֻ��ڴ濨��ͼ");
	} else {
		LogUtil.d("initUserHead()�ֻ��ڴ�û��ͼ�����������");
		new Thread(reqUserHeadRunnable).start();
	}
}

Runnable runnable = new Runnable() {

	@Override
	public void run() {
		// con=Communication.newInstance();//���ﲻ��Ҫprivate Communication
		// con����Ϊ�����con����WoliaoBaseActivity
		if (con == null) {
			LogUtil.d( "new con");
			BaseActivity.con = Communication.newInstance();// ���ﲻ��Ҫprivate
																	// Communication
																	// con����Ϊ�����con����WoliaoBaseActivity
		} else if (!con.newNetWorker01.socketChannel.isRegistered()) {
			LogUtil.d( "opensocket channel");
			BaseActivity.con.openSocketChannel();
		}
		// con.reqLogin(user.getUserID(),user.getPwd());
		// Log.i(tag, "�ѷ���");
	}

};

Runnable reqUserHeadRunnable = new Runnable() {
	public void run() {
		BaseActivity.con.reqUserHead(userID);
	}
};

// Runnable myAttention = new Runnable(){
// public void run(){
// con.reqMyAttentionList();
// }
// };

/**
 * ��ʼ�������˵�
 * 
 * */

void InItSlidingMenu() {
	slidingMenu = new SlidingMenu(this);
	slidingMenu.setMode(SlidingMenu.LEFT);
	slidingMenu.setBehindOffset(4 / 5);
	slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
	slidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);// ���ô�����Χ
																// TOUCHMODE_FULLSCREEN��ȫ�֣�
	slidingMenu.attachToActivity(this, SlidingMenu.RIGHT);// SLIDING_CONTENT
	LogUtil.d("InItSlidingMenu()1111111");
	slidingMenu.setMenu(R.layout.sliding_menu_view);
	LogUtil.d( "InItSlidingMenu()������");
}

/***
 * ��ʼ��С��ͼ�������֣�Ժϵ����ע����ͼ ���ص�pageViews
 */
@SuppressWarnings("deprecation")
void InItView() {
	LogUtil.d("initview()---------11111");
	intent[nums] = new ExtendsIntent(this, RecuitListView.class, null,
			relativePath[0], tip,1);
	LogUtil.d("initview()---------222222");
	intent[nums+1] = new ExtendsIntent(this, ApplyListView.class, null,
			relativePath[1], tip, 1);
	LogUtil.d("initViewRunnable()����");
	// Looper.prepare();
	view01 = getLocalActivityManager().startActivity("1activity01"+nums,
			new Intent(intent[nums])).getDecorView();
	LogUtil.d("!!!!!!!!!!");
	LogUtil.d( "@@@@@@@@@2");
	view02 = getLocalActivityManager().startActivity("1activity03"+nums,
			new Intent(intent[nums+1])).getDecorView();
	LogUtil.d( "***********");
	pageViews.add(view01);
	pageViews.add(view02);
//	pageViews.add(view03);
	nums+=2;
	LogUtil.d( "***********");

//	Message msg = new Message();
//	msg.what = Config.MAINVIEW_UPDATEUI;
//	handler.sendMessage(msg);
	
//	InItTitle();
//	InItTitle();
	pageAdapter.notifyDataSetChanged();
//	initview = new InitView();
//	initview.execute();
//	progressDialog.dismiss();
//	 new Thread(initViewRunnable).start();

}

/***
 * ��ʼ��radiobutton������ÿ��radiobutton�ĳ��ȣ����տ�ʼ��ʼ��ʱ����һ���Ǳ꼴���ֱ���ʼ��Ϊ��ɫ������Ϊ��ɫ
 * ������radiobutton����radioGroup��
 */
@SuppressLint("ResourceAsColor")
void InItRadioButton() {
	@SuppressWarnings("deprecation")
	int width = getWindowManager().getDefaultDisplay().getWidth() / 3;// ��ƽ������
	for (int i = 0; i < title.length; i++) {
		RadioButton radioButton = new RadioButton(this, null,
				R.style.radioButton);
		// radioButton.setText(title[i]);������ʡ����
		radioButton.setTextSize(17);
		radioButton.setTextColor(com.example.studentsystem01.R.color.black);
		radioButton.setWidth(width);
		// radioButton.setHeight(height);
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
	@SuppressWarnings("deprecation")
	int width = getWindowManager().getDefaultDisplay().getWidth() / 2;
	int height = 70;
	for (int i = 0; i < title.length; i++) {
		textView = new TextView(this);
		textView.setText(title[i]);
		textView.setTextSize(17);
		textView.setTextColor(com.example.studentsystem01.R.color.black);
		textView.setWidth(width);
		// textView.setHeight(height - 30);
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
		view.setBackgroundColor(com.example.studentsystem01.R.color.gray);
		linearLayout.addView(textView);
		// if (i != title.length - 1) {
		// linearLayout.addView(view);
		// }

	}
}

/***
 * ѡ��Ч�� ���˾��ã���������ʼ�����ã�����һ���ȸ��Ǳ�Ϊ0��textview��Ȼ��Ǳ�Ϊ0��textview�ȱ�ɺ�ɫ�����������Ū�ɻ�ɫ
 * ȫ��textview����Ū
 */
@SuppressWarnings("deprecation")
public void setSelector(int id) {
	for (int i = 0; i < title.length; i++) {
		if (id == i) {
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.grouplist_item_bg_normal);
			textViews.get(id).setBackgroundDrawable(
					new BitmapDrawable(bitmap));
			textViews.get(id).setTextColor(Color.GREEN);// ѡ������ʾ��ɫ
			viewPager.setCurrentItem(i);
		}

		else {
			textViews.get(i).setBackgroundDrawable(new BitmapDrawable());
			textViews.get(i).setTextColor(
					com.example.studentsystem01.R.color.black);
		}
	}
}

public void onClick(View v) {

	switch (v.getId()) {
	
	}
}

@SuppressLint("ResourceAsColor")
@Override
public void processMessage(Message msg) {// ��¼���
	if (msg.what == Config.SUCCESS) {
		Log.i("BBSMainView",
				"LoginActivity----�õ�LOGIN_SUCCESS��������תChatMainActivity");
		// Intent intent=new Intent(this,ChatMainActivity.class);
		// setPreference(username,pwd);
		Thread thread = new Thread(httpLogin);
		thread.start();
		// startActivity(intent);
	} else if (msg.what == Config.FAILED) {
		// msg.what=1;handler.sendMessage(msg);
		// Toast.makeText(getApplicationContext(), "�û���¼ʧ��",
		// Toast.LENGTH_SHORT).show();
	} else if (msg.what == Config.USER_LOGIN_ALREADY) {
		// showToast("�û��ѵ�¼");
		// Toast.makeText(getApplicationContext(), "�û��ѵ�¼",
		// Toast.LENGTH_SHORT).show();
	} else if (msg.what == Config.SEND_NOTIFICATION) {
		sendNotifycation();
	}

}

Runnable httpLogin = new Runnable() {

	@Override
	public void run() {
		SendLoginInfo sendLoginInfo = new SendLoginInfo(user.getUserID(),
				null);
		try {
			String respCode = sendLoginInfo.checkLoginInfo();
			if (respCode.equals("success"))// ������û�����Ϊ��,���ҵ�¼�ɹ�
			{
				// handler.sendEmptyMessage(2);//��ת����̳������
				SharedPreferences pref = getSharedPreferences("data",
						Context.MODE_PRIVATE);
				String cookie = pref.getString("cookie", "");
				if (cookie.equals("")) {
					Log.d("cookie", sendLoginInfo.getCookie());
					SharedPreferences.Editor editor1 = getSharedPreferences(
							"data", Context.MODE_PRIVATE).edit();
					editor1.putString("cookie", sendLoginInfo.getCookie());
					editor1.commit();
				} else {
					Log.i("cookie", "cookie =" + cookie);
				}

			} else {
				Log.i("/////", "1111");
				// �û����������������
				// handler.sendEmptyMessage(1);
			}
		} catch (InterruptedException e) {
			Log.i("0000", "2222");
			// handler.sendEmptyMessage(4);//����������Ӧ
		}
	}

};
/*
// �������̼߳�������
 Runnable initViewRunnable = new Runnable(){
 @SuppressWarnings("deprecation")
 public void run(){
	 Log.i(tag, "initViewRunnable()����");
		 Looper.prepare();
		view01 = BBSMainView.this.getLocalActivityManager().startActivity("activity01",
				new Intent(intent1)).getDecorView();
		Log.i(tag, "!!!!!!!!!!");
		view02 = BBSMainView.this.getLocalActivityManager().startActivity("activity02",
				new Intent(BBSMainView.this, BBSDepartmentView.class))
				.getDecorView();
		Log.i(tag, "@@@@@@@@@2");
		view03 = BBSMainView.this.getLocalActivityManager().startActivity("activity03",
				new Intent(intent2)).getDecorView();
		Log.i(tag, "***********");
 Log.i(tag, "+++++++++++");
 Message msg1 = new Message();
 msg1.what = Config.REFRESH_UI;
 handler.sendMessage(msg1);
 }
 };
*/
@SuppressWarnings("deprecation")
@Override
protected void onDestroy() {
	// TODO �Զ����ɵķ������
	super.onDestroy();
	Log.i(">>>>>>>>>", "ondestory");
	new Thread(exitRunnable).start();
}

Runnable exitRunnable = new Runnable() {

	@Override
	public void run() {
		Log.i("---->>>>>>", "userId = " + user.getUserID());
		con.exit(user.getUserID());
		con.shutDownSocketChannel();
		// redirectTo();
	}

};
/*
 * private void redirectTo(){ Intent intent=new
 * Intent(this,LoginView.class); intent.putExtra("login", Config.AUTOLOGIN);
 * startActivity(intent); finish(); }
 */

/**
 * �첽��������
 */
private class GetDataTask1 extends AsyncTask<Void , Void , String>{
	
	
	public GetDataTask1(){
	}
	
	protected String doInBackground(Void... params) {
	try{
		Thread.sleep(500);
		}catch(InterruptedException e){
		}
		

//		InItView();
		return null;
	}

	protected void onPostExecute(String result){
		InItView();
//		pageViews.add(view01);
//		pageViews.add(view02);
////		pageViews.add(view03);
//		LogUtil.d( "***********");

//		Message msg = new Message();
//		msg.what = Config.MAINVIEW_UPDATEUI;
//		handler.sendMessage(msg);
		
//		InItTitle();
//		InItTitle();
		recuitWait.setVisibility(View.GONE);
//		pageAdapter.notifyDataSetChanged();
		
//		InItView();
//		initUserHead();
//		setSelector(0);
	}}
	//�����β��˳�����
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			LogUtil.d("����ϵͳ�Դ��ķ��ؼ�");
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
                   Object mHelperUtils;
                   Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
                   mExitTime = System.currentTimeMillis();

			} else {
                   finish();
                   con.shutDownSocketChannel();
                   LogUtil.d("shutDownSocketChannel");
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onResume() {
		// TODO �Զ����ɵķ������
		super.onResume();
		if(SharedPreferenceUtils.getInstance().getNewMessage(getApplicationContext())==null){
			slidingTitleLayout.hideBadgeView();
		}else{
			slidingTitleLayout.showBadgeView(SharedPreferenceUtils.getInstance().getNewMessage(getApplicationContext()));
		}
	}
}
