package com.atm.chatonline.bbs.activity.bbs;

/**
 * ����������ʾ��̳��������
 * 2015-7-30-֣
 * */

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.view.View.OnClickListener;
import android.view.Window;
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
import com.atm.chatonline.bbs.activity.login.LoginView;
import com.atm.chatonline.bbs.adapter.ScrollPageViewAdapter;
import com.atm.chatonline.bbs.commom.NewMessage;
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
import com.atm.chatonline.recuit.bean.Recuit;
import com.example.studentsystem01.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

@SuppressLint("ResourceAsColor")
public class BBSMainView extends BaseActivity implements OnClickListener {
	private String tag = "BBSMainView";

	private RadioGroup radioGroup;
	private String title[] = { "����", "Ժϵ", "��ע" };
	private LinearLayout linearLayout;
	private ArrayList<TextView> textViews;
	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private HorizontalScrollView horizontalScrollView;
	private ImageView imgViewMore, imgViewEdit, imgViewSearch;
	private TextView textView;
	private View view01;
	private static View view02;
	private long mExitTime;
	private View view03;
	private User user;
	private String userID;

	private Activity acitivity;
	private String[] relativePath = { "essay_mainEssay.action",
			"essay_attendEssay.action" };

	private static Handler handler;
	public static Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	private ExtendsIntent intent1;
	private ExtendsIntent intent2;
	private ProgressDialog progressDialog;
//	private InitView initview;

	
	//recuit
	private String id="";
	private String tip = "";
	private String relativePath01="recuit_getRecuit.action";
	
	private BBSConnectNet bBSConnectNet;
	private List<Recuit> recuitList = new ArrayList<Recuit>();
	private RecuitAdapter recuitAdapter;
	private PullToRefreshListView plv;
	private int recuitNums = 0;
	private String response;
	private String cookie;
	private TextView recuit_wait;
	private ImageView reImgViewMore,reImgViewEdit,reImgViewSearch;
	private FrameLayout recuitLayout;
	private ScrollPageViewAdapter pageAdapter;
	private Spinner sp;
	private TextView bbsWait;
	public static SlidingTitleLayout slidingTitleLayout=null;	
	//�������������������༭�����������һ��,bbs��recuit�ı���������һ��
	private LinearLayout bbsTitle,recuitTitle;
	
	public static View getView02() {
		return view02;
	}

	public void onRestart() {
		super.onRestart();
	}
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.p(tag, "BBSMainView-----------");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bbs_main_view);
		sp = (Spinner) findViewById(R.id.sp_workType);
		slidingTitleLayout=(SlidingTitleLayout)findViewById(R.id.sliding_title_layout);
		sp.setVisibility(View.GONE);
		linearLayout = (LinearLayout) findViewById(R.id.tab_view);// ��ȡbbs_main_view���Ӳ��֣�������������Ĳ���
		viewPager = (ViewPager) findViewById(R.id.pager);
		horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		pageViews = new ArrayList<View>();
		pageAdapter=new ScrollPageViewAdapter(pageViews);
        //����ȴ����ؽ��档��
        bbsWait = (TextView) findViewById(R.id.bbs_wait);
		new GetDataTask1().execute();

		
//		InItView();

		InItTitle();
		initUserHead();
		setSelector(0);

		initData();
		showBadgeView();
		if (con == null) {
			Log.i(tag, "conΪnull");

		} else {
			Log.i(tag, "con��Ϊnull");
		}

		viewPager.setAdapter(pageAdapter);// ���֮�������Ļ���һ���
		viewPager.clearAnimation();
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {// ����ǵ����ͬ��Ƭ����ת��ͬ��ҳ��

					@Override
					public void onPageSelected(int arg0) {
						Log.i(tag, "viewPager-1");
						setSelector(arg0);
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						Log.i(tag, "viewPager-2");
					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
						Log.i(tag, "viewPager-3");
					}
				});
		handler = new Handler(){
			public void handleMessage(Message msg){
				if(msg.what==Config.MAINVIEW_UPDATEUI){
					pageViews.add(view01);
					pageViews.add(view02);
					pageViews.add(view03);
					pageAdapter.notifyDataSetChanged();
					BBSListView.getBbsWait().setVisibility(View.GONE);
				}
			}
		};
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
//			Log.i(tag, "------------third");
		}
		
		protected void onPostExecute(Boolean result){
			Log.i(tag, "-----------four");
			if(result==true){
			pageViews.add(view01);
			pageViews.add(view02);
			pageViews.add(view03);
			
			viewPager.setAdapter(pageAdapter);// ���֮�������Ļ���һ���
			Log.i(tag, "-----------six");
//			viewPager.clearAnimation();
//			viewPager.setOnPageChangeListener(new OnPageChangeListener() {// ����ǵ����ͬ��Ƭ����ת��ͬ��ҳ��
//
//						@Override
//						public void onPageSelected(int arg0) {
//							setSelector(arg0);
//						}
//
//						@Override
//						public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//						}
//
//						@Override
//						public void onPageScrollStateChanged(int arg0) {
//
//						}
//					});
			}
			progressDialog.dismiss();
			Log.i(tag, "-----------five");
		}
		
	}
*/
	private void initData() {
		user = getPreference();
		userID = BaseActivity.getSelf().getUserID();
		LogUtil.p(tag, "userID:"+userID);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			int login = bundle.getInt("login");
			Log.i("BBSMainView", "isAutoLogin is " + login);
			if (login == Config.AUTOLOGIN) {
				Log.i(tag, "login==Config.AUTOLOGIN");
				initAutoLogin();
			}
		}
		// boolean result = dbUtil.queryIsFriendList(userID);
		// if(!result){
		// Log.i(tag, "��������б�û��¼");
		// // new Thread(myAttention).start();
		// }

	}

	private void initAutoLogin() {// ������¼����
		Log.i(tag, "������¼����");
		new Thread(runnable).start();

	}
	public void initUserHead() {
		LogUtil.p(tip, "initUserHead--userID:"+BaseActivity.getSelf().getUserID());
		if (FileUtil.isFile(BaseActivity.getSelf().getUserID())) {
			Log.i(tag, "initUserHead()�ֻ��ڴ濨��ͼ");
		} else {
			Log.i(tag, "initUserHead()�ֻ��ڴ�û��ͼ�����������");
			new Thread(reqUserHeadRunnable).start();
		}
	}

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// con=Communication.newInstance();//���ﲻ��Ҫprivate Communication
			// con����Ϊ�����con����WoliaoBaseActivity
			if (con == null) {
				Log.i(tag, "new con");
				BaseActivity.con = Communication.newInstance();// ���ﲻ��Ҫprivate
																		// Communication
																		// con����Ϊ�����con����WoliaoBaseActivity
			} else if (!con.newNetWorker01.socketChannel.isRegistered()) {
				Log.i(tag, "opensocket channel");
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

//	/**
//	 * ��ʼ�������˵�
//	 * 
//	 * */
//	//InItSlidingMenu()�Ѿ�û������2016.7.19
//	void InItSlidingMenu() {
//		slidingMenu = new SlidingMenu(this);
//		slidingMenu.setMode(SlidingMenu.LEFT);
//		slidingMenu.setBehindOffset(4 / 5);
//		slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
//		slidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);// ���ô�����Χ
//																	// TOUCHMODE_FULLSCREEN��ȫ�֣�
//		slidingMenu.attachToActivity(this, SlidingMenu.RIGHT);// SLIDING_CONTENT
//		Log.i(tag, "InItSlidingMenu()1111111");
//		slidingMenu.setMenu(R.layout.sliding_menu_view);
//		Log.i(tag, "InItSlidingMenu()������");
//	}

	/***
	 * ��ʼ��С��ͼ�������֣�Ժϵ����ע����ͼ ���ص�pageViews
	 */
	@SuppressWarnings("deprecation")
	void InItView() {
		Log.i(tag, "initview()---------11111");
		intent1 = new ExtendsIntent(this, BBSListView.class, null,
				relativePath[0], "first", 1);
		
		Log.i(tag, "initview()---------222222");
		intent2 = new ExtendsIntent(this, BBSListView.class, null,

				relativePath[1], "", 1);
		

	//			relativePath[1], "concern", 1);

		if (intent1 == null) {
			Log.i(tag, "intent1Ϊnull");
		} else {
			Log.i(tag, "intent1��Ϊnull");
		}
		Log.i(tag, "initViewRunnable()����");
		// Looper.prepare();
		view01 = getLocalActivityManager().startActivity("activity01",
				new Intent(intent1)).getDecorView();
		Log.i(tag, "!!!!!!!!!! view01�Ƿ�Ϊnull="+(view01==null));
		view02 = getLocalActivityManager().startActivity("activity02",
				new Intent(BBSMainView.this, BBSDepartmentView.class))
				.getDecorView();
		Log.i(tag, "@@@@@@@@@2");
		view03 = getLocalActivityManager().startActivity("activity03",
				new Intent(intent2)).getDecorView();
		Log.i(tag, "***********");
//		pageViews.add(view01);
//		pageViews.add(view02);
//		pageViews.add(view03);

//		initview = new InitView();
//		initview.execute();
		Log.i(tag, "``````````");
//		progressDialog.dismiss();
//		 new Thread(initViewRunnable).start();
		Log.i(tag, "#######");

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
		int width = getWindowManager().getDefaultDisplay().getWidth() / 3;
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

/*	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.imgView_more:
			slidingMenu.toggle(true);
			break;
		case R.id.imgView_edit:

			// startActivity(new
			// Intent(BBSMainView.this,NavigationPublishPost.class));
			startActivity(new Intent(BBSMainView.this, BBSPublishPostView.class));
			break;
		case R.id.imgView_search:
			startActivity(new Intent(BBSMainView.this, NavigationSearch.class));
			break;
		case R.id.recuit_imgView_more:
			slidingMenu.toggle(true);
			break;
		case R.id.recuit_imgView_edit:
			startActivity(new Intent(BBSMainView.this,RecuitNavigationPublishPost.class));
			break;
		case R.id.recuit_imgView_search:
			startActivity(new Intent(BBSMainView.this,RecuitNavigationSearch.class));
			break;
		case R.id.bbs:
			bbsTitle.setVisibility(View.VISIBLE);
			recuitTitle.setVisibility(View.GONE);
			horizontalScrollView.setVisibility(View.VISIBLE);
			viewPager.setVisibility(View.VISIBLE);
			recuitLayout.setVisibility(View.GONE);
			slidingMenu.toggle(false);
			break;
		case R.id.chat:
			Log.d("kkkkkkkk", "chat");
			Intent intent = new Intent(BBSMainView.this, ChatMainActivity.class);
			startActivity(intent);
			break;
		case R.id.recuit:
			Log.i(tag, "��Ƹ����--------��������");
			
			recuit_wait.setVisibility(View.GONE);
//			try {
//				loadData();
//			} catch (JSONException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			bbsTitle.setVisibility(View.GONE);
			recuitTitle.setVisibility(View.VISIBLE);
			horizontalScrollView.setVisibility(View.GONE);
			viewPager.setVisibility(View.GONE);
			recuitLayout.setVisibility(View.VISIBLE);
			slidingMenu.toggle(false);
			break;
		case R.id.user_center:
			Log.i(tag, "�������ı����");
			Intent intent1=new Intent(BBSMainView.this,UserCenter.class);
			startActivity(intent1);
			break;

		}
	}*/
	
	private void showBadgeView(){
		LogUtil.i("showbadge1");
		new Thread(IsHasNewMessage).start();
		//newMessage=new NewMessage();
		//newMessage.setSum(11);
		//slidingTitleLayout.showBadgeView(newMessage);
	}

	@Override
	public void processMessage(Message msg) {// ��¼���
		LogUtil.i("processMessage");
		if (msg.what == Config.LOGIN_SUCCESS) {
			Log.i("BBSMainView",
					"LoginActivity----�õ�LOGIN_SUCCESS��������תChatMainActivity");
			Log.i(tag, "�ڶ��ε�¼�ɹ�");
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
			LogUtil.p(tag, "����Ϣ֪ͨ");
			sendNotifycation();
		}else if(msg.what==Config.ISHAS_NEW_MYMESSAGE){
			Bundle bundle=msg.getData();
			NewMessage newMessage=(NewMessage)bundle.getSerializable("NewMessage");
			if(newMessage.getSum()>0){
				slidingTitleLayout.showBadgeView(newMessage);
			}	
		}

	}
	
	Runnable IsHasNewMessage=new Runnable() {
		
		@Override
		public void run() {
			LogUtil.i("sendHasNewMessage1");
			con.sendIsHasNewMsg(userID);
		}
	};
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

	@SuppressWarnings("deprecation")
	@Override
	protected void onDestroy() {
		// TODO �Զ����ɵķ������
		super.onDestroy();
	}

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

//			InItView();
			return null;
		}
	
		protected void onPostExecute(String result){
			InItView();
			Message msg = new Message();
			msg.what = Config.MAINVIEW_UPDATEUI;
			handler.sendMessage(msg);
			bbsWait.setVisibility(View.GONE);
			
//			InItTitle();
//			InItTitle();
			pageAdapter.notifyDataSetChanged();
//			initUserHead();
//			setSelector(0);
		}
	}

	
	
	
	private void redirectTo() {
		Intent intent = new Intent(this, LoginView.class);
		intent.putExtra("login", Config.BE_OFF_LOGIN);
		Log.i(tag, "main view queue size="+queue.size());
		startActivity(intent);
		
	}

	//�����β��˳�����
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
         if (keyCode == KeyEvent.KEYCODE_BACK) {
        	 Log.i(tag,"����ϵͳ�Դ��ķ��ؼ�");
                 if ((System.currentTimeMillis() - mExitTime) > 2000) {
                         Object mHelperUtils;
                         Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
                         mExitTime = System.currentTimeMillis();

                 } else {
                         finish();
                         con.shutDownSocketChannel();
                         LogUtil.p(tag, "shutDownSocketChannel");
                 }
                 return true;
         }
         return super.onKeyDown(keyCode, event);
 }

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
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
