package com.atm.chatonline.chat.ui;

/**
 * 
 * ˽�ĵ����촰��
 * */
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.chatonline.bbs.activity.bbs.BBSMainView;
import com.atm.chatonline.bbs.commom.NewMessage;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.bbs.util.SharedPreferenceUtils;
import com.atm.chatonline.bbs.util.SlidingTitleLayout;
import com.atm.chatonline.chat.adapter.ScrollPageViewAdapter;
import com.atm.chatonline.chat.info.ChatMessage;
import com.atm.chatonline.chat.info.Friend;
import com.atm.chatonline.chat.info.Group;
import com.atm.chatonline.chat.net.Communication;
import com.atm.chatonline.chat.util.Config;
import com.atm.chatonline.chat.util.FileUtil;
import com.atm.chatonline.chat.util.MtitlePopupWindow;
import com.atm.chatonline.chat.util.MtitlePopupWindow.OnPopupWindowClickListener;
import com.atm.chatonline.schoolnews.ui.SchoolNewsActivity;
import com.atm.chatonline.setting.ui.SettingView;
import com.atm.chatonline.usercenter.activity.usercenter.UserCenter;
import com.example.studentsystem01.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

@SuppressLint("ResourceAsColor")
public class ChatMainActivity extends BaseActivity implements OnClickListener,
		ReceiveInfoListener {
	private SlidingMenu slidingMenu;
	private RadioGroup radioGroup;
	private String title[] = { "����", "��ע" };
	private LinearLayout linearLayout;
	// private final int height = 70;
	private ArrayList<TextView> textViews;
	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private HorizontalScrollView horizontalScrollView;
	private ImageView imgViewMore, imgViewCenter;
	private TextView textView;
	private String tag = "ChatMainActivity";
	private String userId;
	private List<Group> GroupList;
	private List<Friend> FriendList;
	private MtitlePopupWindow popupWindow;
	private boolean flag = true;
	public static SlidingTitleLayout slidingTitleLayout;
	private static String STATE = "M";

	protected void onCreate(Bundle savedInstanceState) {
		Log.i(tag, "ChatMainActivity--------��������");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chat_main_view);
		try {
			if (con == null) {
				Log.i(tag, "conΪnull");
				new Thread(loginAgainRunnable).start();
			} else {
				Log.i(tag, "con��Ϊnull");
				con.addReceiveInfoListener(STATE, ChatMainActivity.this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.i(tag, "con.addReceiveInfoListener����");
		SharedPreferences pref = getSharedPreferences("User", MODE_PRIVATE);
		// userId=pref.getString("userID", "");
		userId = BaseActivity.getSelf().getUserID();
		LogUtil.p(tag, "����Ҫ:userId:" + userId);
		gueryDataBase();// ��ѯ�������ݿ�
		Log.i("@@@@@@", "queue size =" + queue.size());
		linearLayout = (LinearLayout) findViewById(R.id.tab_view);// ��ȡbbs_main_view���Ӳ��֣�������������Ĳ���
		viewPager = (ViewPager) findViewById(R.id.pager);
		horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		slidingTitleLayout=(SlidingTitleLayout)findViewById(R.id.slidingtitlelayout);
		slidingTitleLayout.showBadgeView(SharedPreferenceUtils.getInstance().getNewMessage(getApplicationContext()));
		InItView();

		Log.i(tag,
				"ChatMainActivity--------�Ѿ�������GroupListActivity��FriendListActivity");
		InItTitle();
		setSelector(0);
		initPager();

		popupWindow = new MtitlePopupWindow(this);
		popupWindow
				.setOnPopupWindowClickListener(new OnPopupWindowClickListener() {

					@Override
					public void onPopupWindowItemClick(int position) {
						// ��Ҫ������
						Log.i(tag, "********");
						if (position == 0) {
							Log.i(tag, "position��ѡ����0");
							Intent intent = new Intent(ChatMainActivity.this,
									CreateGroupActivity.class);
							startActivity(intent);
						} else if (position == 1) {
							Log.i(tag, "position��ѡ����1");
							Intent intent = new Intent(ChatMainActivity.this,
									SearchActivity.class);
							startActivity(intent);
						}
					}
				});
		// InItSlidingMenu();
		// initWidget();

		if (flag) {
			new Thread(runnable).start();
		}
		Log.i(tag, flag + "");

	}

	// ���л��ؼ����ü���
	private void initWidget() {
		LinearLayout l1 = (LinearLayout) findViewById(R.id.bbs);
		// LinearLayout l2 = (LinearLayout) findViewById(R.id.chat);
		LinearLayout l3 = (LinearLayout) findViewById(R.id.recuit);
		LinearLayout l4 = (LinearLayout) findViewById(R.id.user_center);
		LinearLayout l5 = (LinearLayout) findViewById(R.id.news);
		l1.setOnClickListener(this);
		// l2.setOnClickListener(this);
		l3.setOnClickListener(this);
		l4.setOnClickListener(this);
		l5.setOnClickListener(this);
	}

	/**
	 * �����û�id�ڱ������ݿ��ѯ�û���ص�Ⱥ,Ŀǰ��ѯȺ�б�˽���б�δд
	 */
	@SuppressWarnings("unchecked")
	private void gueryDataBase() {
		try {
			userId = BaseActivity.getSelf().getUserID();
			LogUtil.p(tag, "gueryDataBase--����Ҫ:userId:" + userId);
			Map<String, Object> map = dbUtil.queryGroupList(userId);
			GroupList = (List<Group>) map.get("groupList");
			flag = (Boolean) map.get("flag");
			Log.i(tag, flag + "2");
			Log.i(tag, "Ⱥ���� =" + GroupList.size());
			if (GroupList != null && GroupList.size() > 0) {
				for (int i = 0; i < GroupList.size(); i++) {
					Log.i(tag, "gueryDataBase()---��Ⱥ");
					Group group = GroupList.get(i);
					Bitmap bm = FileUtil.getBitmap(FileUtil.APP_PATH + "/group"
							+ "/" + userId + "/" + group.getGroupId() + ".jpg");
					group.setBm(bm);
				}
			}
			Log.i(tag, "---gueryDataBase()---userId:" + userId);
			FriendList = dbUtil.queryPersonalChatList(userId);
			if (FriendList == null) {
				Log.i("friendlist", "friend is null");
			} else {
				Log.i("friendlist", "friend is not null");
				LogUtil.p(tag, "friendlist.size:" + FriendList.size());
			}
			if (FriendList != null && FriendList.size() > 0) {
				Log.i(tag, "gueryDataBase()---���˽���");
				for (Friend friend : FriendList) {
					Bitmap bm = FileUtil.getBitmap(FileUtil.APP_PATH
							+ "/friend" + "/" + userId + "/"
							+ friend.getFriendID() + ".jpg");
					friend.setBm(bm);
				}
			}
			Log.i(tag, "gueryDataBase()������");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * ��ʼ��Ⱥ�б�˽���б� ���ص�pageViews
	 * 
	 * @throws InterruptedException
	 */
	void InItView() {
		try {
			Log.i("*************", "*********************");
			pageViews = new ArrayList<View>();
			View view01, view02;// view01:���죬view02:��ע

			if (getLocalActivityManager().getActivity("ChatAttentionActivity") != null) {
				Log.i(tag, "ChatAttentionActivity��Ϊnull");
				ChatAttentionActivity chatAtt = (ChatAttentionActivity) getLocalActivityManager()
						.getActivity("ChatAttentionActivity");
				// group.upDate(GroupList);
				view02 = chatAtt.getWindow().getDecorView();
			} else {
				Intent intent = new Intent(this, ChatAttentionActivity.class);
				// ArrayList list=new ArrayList();
				// list.add(GroupList);
				// intent.putExtra("userId", userId);
				// intent.putParcelableArrayListExtra("list", list);
				view02 = getLocalActivityManager().startActivity(
						"GroupListActivity", intent).getDecorView();
			}

			if (getLocalActivityManager().getActivity("FriendListActivity") != null) {
				Log.i(tag, "FriendListActivity��Ϊnull");
				Log.i(tag, "friendlist size=" + FriendList.size());
				FriendListActivity friend = (FriendListActivity) getLocalActivityManager()
						.getActivity("FriendListActivity");
				friend.upDate(FriendList);
				if (FriendList.size() > 0) {
					Log.i(tag, "FriendList.friendID:######"
							+ FriendList.get(0).getFriendID());
				} else {
					Log.i(tag, "����0");
				}
				view01 = friend.getWindow().getDecorView();
			} else {
				Intent intent = new Intent(this, FriendListActivity.class);
				ArrayList list = new ArrayList();
				Log.i(tag, "friendlist size=" + FriendList.size());
				list.add(FriendList);
				intent.putExtra("userId", userId);
				intent.putParcelableArrayListExtra("list", list);
				view01 = getLocalActivityManager().startActivity(
						"FriendListActivity", intent).getDecorView();
			}

			Log.i(tag,
					"ChatMainActivity--------��ʼ����GroupListActivity��FriendListActivity");
			pageViews.add(view01);
			pageViews.add(view02);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initPager() {
		// imgViewMore=(ImageView)findViewById(R.id.imgView_more);
		// imgViewMore.setOnClickListener(this);
		//
		// imgViewCenter = (ImageView)findViewById(R.id.imgView_person_center);
		// imgViewCenter.setOnClickListener(this);
		try {
			viewPager.setAdapter(new ScrollPageViewAdapter(pageViews));// ���֮�������Ļ���һ���
			viewPager.clearAnimation();
			viewPager.setOnPageChangeListener(new OnPageChangeListener() {// ����ǵ����ͬ��Ƭ����ת��ͬ��ҳ��

						@Override
						public void onPageSelected(int arg0) {
							setSelector(arg0);
						}

						@Override
						public void onPageScrolled(int arg0, float arg1,
								int arg2) {

						}

						@Override
						public void onPageScrollStateChanged(int arg0) {

						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * ��ʼ��radiobutton������ÿ��radiobutton�ĳ��ȣ����տ�ʼ��ʼ��ʱ����һ���Ǳ꼴���ֱ���ʼ��Ϊ��ɫ������Ϊ��ɫ
	 * ������radiobutton����radioGroup��
	 */
	@SuppressLint("ResourceAsColor")
	void InItRadioButton() {
		int width = getWindowManager().getDefaultDisplay().getWidth() / 2;// ��ƽ������
		for (int i = 0; i < title.length; i++) {
			RadioButton radioButton = new RadioButton(this, null,
					R.style.radioButton);
			// radioButton.setText(title[i]);������ʡ����
			radioButton.setTextSize(17);
			radioButton.setTextColor(R.color.black);
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
		int width = getWindowManager().getDefaultDisplay().getWidth() / 2;
		int height = 70;
		for (int i = 0; i < title.length; i++) {
			textView = new TextView(this);
			textView.setText(title[i]);
			textView.setTextSize(17);
			textView.setTextColor(R.color.black);
			textView.setWidth(width);
			// textView.setHeight(height - 10);
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
			// if (i != title.length - 1) {
			// linearLayout.addView(view);
			// }

		}
	}

	/***
	 * ѡ��Ч�� ���˾��ã���������ʼ�����ã�����һ���ȸ��Ǳ�Ϊ0��textview��Ȼ��Ǳ�Ϊ0��textview�ȱ�ɺ�ɫ�����������Ū�ɻ�ɫ
	 * ȫ��textview����Ū
	 */
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
				textViews.get(i).setTextColor(R.color.black);
			}
		}
	}

	public void onClick(View v) {

	}

	/**
	 * ִ�������������������߳�
	 */
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			LogUtil.p(tag, "��Ⱥ------userID:"
					+ BaseActivity.getSelf().getUserID());
			con.sendFindMyGroup(BaseActivity.getSelf().getUserID());
			// con.getOfflineMessage(userId);
		}
	};

	Runnable loginAgainRunnable = new Runnable() {
		public void run() {
			BaseActivity.con = Communication.newInstance();
			con.addReceiveInfoListener(STATE, ChatMainActivity.this);
		}
	};

	/**
	 * ����̨���õļ̳���WoliaoActivity��ķ����������������н���
	 */
	@Override
	public void processMessage(Message msg) {
		
		gueryDataBase();
		Log.i(tag, "chatmainActivity ������");
		Bundle bundle = msg.getData();

		int result = bundle.getInt("result");
		if (msg.what == 0) {// 0��ʾ��̨���ص���Ⱥ�б�Ĳ�ѯ�����1��ʾ���ص��ǲ�ѯ˽���б��صĽ��
			if (result == Config.SUCCESS) {
				ArrayList list = bundle.getParcelableArrayList("groupList");
				GroupList = (List<Group>) list.get(0);
				insertGroupList(GroupList);
			} else if (result == Config.FAILED) {
				showToast("����������");
			} else {
				showToast("����û��ȺŶ");
			}
		} else if (msg.what == Config.SEND_NOTIFICATION) {
			Log.i(tag, "����Ϣ֪ͨ");
			sendNotifycation();
		} else if (msg.what == 1) {
			ChatMessage chatMessage = (ChatMessage) bundle
					.getSerializable("chatMessage");
			boolean isAdd = true;
			if (FriendList == null) {
				Log.i("friendlist", "friend is null");
			} else {
				Log.i("friendlist", "friend is not null");
			}
			for (Friend friend : FriendList) {// �ж�������Ƿ��Ѿ����б���

				// �ж������Ƿ�Ϊ��*****************************//
				if (friend.getFriendID() == null) {
					Log.i(tag, "friend.getFriendID()Ϊ��");
					isAdd = false;
					continue;
				} else if (friend.getFriendID().equals(
						chatMessage.getFriendID())) {
					Log.i(tag, "friend.getFriendID()Ϊ����");
					isAdd = false;
					continue;
				}
			}
			if (isAdd) {
				Log.i(tag, chatMessage.getNickName());
				FriendList.add(new Friend(chatMessage.getFriendID(),
						chatMessage.getNickName(), FileUtil
								.getBitmap(FileUtil.APP_PATH + "/friend" + "/"
										+ userId + "/"
										+ chatMessage.getFriendID() + ".jpg")));
			}
			BaseActivity.saveMessagesToDB(chatMessage.getSelfID(),
					chatMessage.getFriendID(), chatMessage.getNickName(),
					Config.MESSAGE_FROM, chatMessage.getType(),
					chatMessage.getTime(), chatMessage.getContent());
			if (FriendList.size() > 0) {
				Log.i(tag, "FriendList.friendID:%%%%%%%%%"
						+ FriendList.get(0).getFriendID());
			} else {
				Log.i(tag, "����0");
			}
		}


		InItView();// �����б�
		Log.i(tag, "InItView()����");
	}

	public void showToast(String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
	}

	/**
	 * ����ChatMainActivityʱ���ã��������»��Ѹ�Activity
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		gueryDataBase();
		Log.i(tag, "onRestart ������");
		InItView();
	}

	/**
	 * ���ӷ��������ص�Ⱥ�б�������ݿ⣬���ҽ�����һ��ע���¼��ɾ���������ݿ���¼����
	 * 
	 * @param groupList
	 */
	public void insertGroupList(List<Group> groupList) {
		for (Group group : groupList) {
			// �Ƚ�ͼƬ���뱾��sd��
			File file = FileUtil.createFile(userId,
					Integer.parseInt(group.getGroupId()));
			FileUtil.saveBitmap(file, group.getBm());
			// ����Ⱥ��Ϣ�������ݿ�
			saveToDB(userId, Integer.parseInt(group.getGroupId()),
					group.getGroupName());
		}
	}

	/**
	 * �������ݵ����ݿ�
	 * 
	 * @param userId
	 * @param groupId
	 * @param groupName
	 */
	public void saveToDB(String userId, int groupId, String groupName) {
		ContentValues values = new ContentValues();
		values.put("user_Id", userId);
		values.put("group_Id", groupId);
		values.put("groupName", groupName);
		dbUtil.insertGroupInfo(values);
	}

	//�����β��˳�����
//		 public boolean onKeyDown(int keyCode, KeyEvent event) {
//	         if (keyCode == KeyEvent.KEYCODE_BACK) {
//	        	 onBackPressed();
//	                 return true;
//	         }
//	         return super.onKeyDown(keyCode, event);
//	 }


	/**
	 * ��˽�ĵ�������Ϣ�������ݿ�
	 * 
	 * @param userID
	 * @param friendID
	 * @param direction
	 * @param type
	 * @param time
	 * @param content
	 */
	// public void saveMessagesToDB(String userID,String friendID,String
	// nickName,int direction,int type,String time,String content){
	// Log.i(tag, "������ӵ�messages�ɹ����Ѷ��Ŵ��뱾�ص����ݿ�ķ���������");
	// // DatabaseUtil db = new DatabaseUtil(this);
	// ContentValues values = new ContentValues();
	// values.put("self_Id",userID);
	// values.put("friend_Id",friendID);
	// values.put("friend_nickName", nickName);
	// values.put("direction",direction);
	// values.put("type",type);
	// values.put("time",time);
	// values.put("content",content);
	// values.put("showTime",0);
	// dbUtil.insertMessages(values);
	// Log.i(tag, "�հѶ��Ų��뱾�����ݿ⣬�������һ��");
	// }
	@Override
	public boolean isChatting(Object info) {
		ChatMessage chatMessage = (ChatMessage) info;// ��objectתΪ��Ϣ
		Log.i(tag, "PersonChatActivity----chatMessage.getContent��"
				+ chatMessage.getContent());
		Log.i(tag, "PersonChatActivity----chatMessage.getFriendID()��"
				+ chatMessage.getFriendID());
		Log.i(tag, "PersonChatActivity----chatMessage.getFriendID()"
				+ chatMessage.getFriendID());
		return true;
	}	
	
//	@Override
//    public void onBackPressed() {
////      super.onBackPressed();
//        finish();
//    }
	

	
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
