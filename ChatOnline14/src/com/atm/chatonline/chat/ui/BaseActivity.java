package com.atm.chatonline.chat.ui;
/**
 * ����������л�Ļ���
 * �ص��ǣ�protected static Communication con;
		  protected static DatabaseUtil dbUtil;
 * */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.chatonline.all.util.FontConfig;
import com.atm.chatonline.bbs.activity.login.LoginView;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.info.ChatMessage;
import com.atm.chatonline.chat.info.Friend;
import com.atm.chatonline.chat.info.Group;
import com.atm.chatonline.chat.info.User;
import com.atm.chatonline.chat.net.Communication;
import com.atm.chatonline.chat.util.Config;
import com.atm.chatonline.chat.util.DatabaseUtil;
import com.atm.chatonline.chat.util.FileUtil;
import com.atm.chatonline.setting.util.Status;
import com.example.studentsystem01.R;

public abstract class BaseActivity extends ActivityGroup {

	private static String tag = "BaseActivity";

	public static LinkedList<BaseActivity> queue = new LinkedList<BaseActivity>();
	public static Communication con;
	public static DatabaseUtil dbUtil;

	public static User self = new User();
	public static Friend friend = new Friend();
	public static Group group = new Group();
	public static Status status = new Status();

	private static List<View> listView = new ArrayList<View>();
	public static float fontSize = FontConfig.NOMAL_FONT;
	public static int isDisturb;// 0��ʾ����ģʽ�ǹر�
	public long mExitTime;

	int count = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			Log.i(tag, "WoliaoBaseActivity---�ж϶������Ƿ������Activity");
			if (!queue.contains(this)) {
				if (con == null) {
					Log.i(tag, "conΪnull");
					new Thread(loginAgainRunnable).start();
				} else {
					Log.i(tag, "con��Ϊnull");
				}
				Log.i(tag, "WoliaoBaseActivity---������û�������Activity��Ҫ���" + this.toString());
				queue.add(this);
				Log.i(tag, "queue.size:" + queue.size());
			}
			if (dbUtil == null) {
				dbUtil = new DatabaseUtil(this);

			}
			isDisturb = dbUtil.queryStatus();
			LogUtil.p(tag, "isDisturb" + isDisturb);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static User getSelf() {
		return self;
	}

	public static void setSelf(User self) {
		BaseActivity.self = self;
	}

	public static Friend getFriend() {
		return friend;
	}

	public static void setFriend(Friend friend) {
		BaseActivity.friend = friend;
	}

	public static Group getGroup() {// ***
		return group;
	}

	public static void setGroup(Group group) {// ***
		BaseActivity.group = group;
	}

	public static BaseActivity getNowActivity() {
		try {
			if (queue.size() == 1) {
				Log.i(tag, "����Ϊ1");
				return queue.getLast();
			} else {
				Log.i(tag, "���в�Ϊ1");
				Log.i(tag, "queue.size:" + queue.size());
			}
			return queue.getLast();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	public static Communication getCon() {
		return con;
	}

	public static DatabaseUtil getDbUtil() {
		return dbUtil;
	}

	public static void setDbUtil(DatabaseUtil dbUtil) {
		BaseActivity.dbUtil = dbUtil;
	}

	public static void sendMessage(Message msg) {
		Log.i(tag, "WoliaoBaseActivity---sendMessage()�����ã�������:" + msg.what);
		handler.sendMessage(msg);
		Log.i(tag, "WoliaoBaseActivity---sendMessage()�ɹ�������");
	}

	public abstract void processMessage(Message msg);

	private static Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			Log.i(tag, "WoliaoBaseActivity---handlerMessage()�����ã�������:" + msg.what);
			Log.i(tag, "handleMessage---queue.size:" + queue.size());
			Log.i(tag, "WoliaoBaseActivity---handlerMessage()�����ã�queue�Ƿ�Ϊ��:" + queue.isEmpty());
			switch (msg.what) {
			case 0:
				queue.getLast().processMessage(msg);
				break;
			case 22:
				showDialog();
				break;

			default:
				if (!queue.isEmpty()) {
					Log.i(tag, "WoliaoBaseActivity---��ǰ�Ļ��:" + queue.getLast().toString());
					queue.getLast().processMessage(msg);
					Log.i(tag, "��ǰ����activity�����ǣ�" + queue.size() + "");
				}
				break;
			}
		}
	};

	public static void setDisStatus(int status) {
		dbUtil.updateStatus(status);
	}

	protected void setPreference(String userID, String pwd) {
		try {
			SharedPreferences.Editor editor = getSharedPreferences("User", MODE_PRIVATE).edit();
			editor.putString("userID", userID);
			editor.putString("pwd", pwd);
			editor.commit();
			getPreference();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected User getPreference() {

		User user = new User();
		SharedPreferences pref = getSharedPreferences("User", MODE_PRIVATE);
		String userId = pref.getString("userID", "");
		String pwd = pref.getString("pwd", "");
		user.setUserID(userId);
		user.setPwd(pwd);
		return user;

	}

	// ��������Ĵ�С ������2016.7.19
	protected void setFontSize(float size) {
		SharedPreferences.Editor editor = getSharedPreferences("fontSize", MODE_PRIVATE).edit();
		editor.putFloat("size", size);
		editor.commit();

	}

	protected float getFontSize() {
		SharedPreferences pref = getSharedPreferences("fontSize", MODE_PRIVATE);
		float size = pref.getFloat("size", FontConfig.NOMAL_FONT);
		return size;
	}

	// �������пؼ����ҳ�TextView��2016.7.19
	public void getAllChildViews(BaseActivity activity) {
		View view = activity.getWindow().getDecorView();
		listView = getAllChildViews(view);
		fontSize = getFontSize();
		LogUtil.p(tag, "fontSize:" + fontSize);
		for (int i = 0; i < listView.size(); i++) {
			if (listView.get(i) instanceof TextView) {
				LogUtil.p(tag, "��TextView");
				TextView textView = (TextView) listView.get(i);
				textView.setTextSize(fontSize);
			}
		}
	}

	public List<View> getAllChildViews(View view) {
		List<View> allchildren = new ArrayList<View>();
		if (view instanceof ViewGroup) {
			ViewGroup vp = (ViewGroup) view;
			for (int i = 0; i < vp.getChildCount(); i++) {
				View viewchild = vp.getChildAt(i);
				allchildren.add(viewchild);
				allchildren.addAll(getAllChildViews(viewchild));
			}
		}
		return allchildren;
	}

	public static void showDialog() {
		try {
			AlertDialog.Builder builder = new Builder(getNowActivity());
			View view = LayoutInflater.from(getNowActivity().getApplicationContext())
					.inflate(com.example.studentsystem01.R.layout.be_off_dialog_view, null);
			builder.setView(view);
			builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					Intent intent = new Intent(BaseActivity.getNowActivity(), LoginView.class);
					Bundle bundle = new Bundle();
					bundle.putInt("login", Config.BE_OFF_LOGIN);
					Log.i(tag, "beoff");
					intent.putExtras(bundle);
					BaseActivity.getNowActivity().startActivity(intent);
				}
			});
			builder.create().show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ������Ϣ֪ͨ���ֻ����ϣ���ʵ�ֵ���¼�
	public void sendNotifycation() {
		isDisturb = dbUtil.queryStatus();
		if (isDisturb == 0) {// ��ť�ر�
			LogUtil.p(tag, "���Խ��ն���");
			NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			Notification notification = new Notification(R.drawable.pic_head, "New Message",
					System.currentTimeMillis());
			Intent intent = new Intent(this, PersonChatActivity.class);
			Log.i(tag, "sendNotifycation()");
			intent.putExtra("userID", self.getUserID());
			Log.i(tag, "userID:" + self.getUserID());
			intent.putExtra("friendId", friend.getFriendID());
			Log.i(tag, "friendId:" + friend.getFriendID());
			intent.putExtra("nickName", friend.getNickName());
			Log.i(tag, "nickName:" + friend.getNickName());
			intent.putExtra("bm", FileUtil.BitmapToBytes(friend.getBm(), false));
			PendingIntent pendingIntent = PendingIntent.getActivity(this, count, intent, 0);
			notification.setLatestEventInfo(this, "New Message", "Click here", pendingIntent);
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			// int friendid = Integer.parseInt(friend.getFriendID());
			Log.i(tag, "count:" + count++);
			manager.notify(0, notification);

			Log.i(tag, "��������Ϣ֪ͨ");
		} else {// ��ť��
			LogUtil.p(tag, "����ģʽ");
		}
	}

	public static void saveMessagesToDB(String userID, String friendID, String nickName, int direction, int type,
			String time, String content) {
		Log.i(tag, "������ӵ�messages�ɹ����Ѷ��Ŵ��뱾�ص����ݿ�ķ���������");
		
		ContentValues values = new ContentValues();
		values.put("self_Id", userID);
		values.put("friend_Id", friendID);
		values.put("friend_nickName", nickName);
		values.put("direction", direction);
		values.put("type", type);
		values.put("time", time);
		values.put("content", content);
		values.put("showTime", 0);
		dbUtil.insertMessages(values);
		Log.i(tag, "�հѶ��Ų��뱾�����ݿ⣬�������һ��");
		dbUtil.queryMessages(userID, friendID);
	}

	public void finish() {
		super.finish();
		if (!queue.isEmpty()) {
			Log.i(tag, "finish��");
			LogUtil.p(tag, "queue.name:" + queue.toString());
			Log.i(tag, "queue.last:" + queue.getLast().toString());
			queue.removeLast();
			
		}
		LogUtil.p(tag, "queue.size:" + queue.size());
	}

	public static void finishAll() {
		while (!queue.isEmpty()) {
			queue.removeLast();
		}
		LogUtil.p(tag, "queue.size:" + queue.size());
	}

	// �Ȱ��յ���chatMessage����ˣ��ٷŵ�saveMessageToDB,Ȼ��ŵ����ݿ�
	public static void saveToDB(ChatMessage chatMessage) {
		Log.i(tag, "�����ҽ��յ��Ķ��ţ�����׼�����뱾�����ݿ⣬������" + chatMessage.getContent());
		String userID = chatMessage.getSelfID();
		String friendID = chatMessage.getFriendID();
		String nickName = chatMessage.getNickName();
		int direction = chatMessage.getDirection();
		int type = chatMessage.getType();
		String time = chatMessage.getTime();
		String content = chatMessage.getContent();
		int showTime = chatMessage.getShowTime();
		Log.i(tag, "userID:" + userID + "��friendID:" + friendID + "direction" + direction);
		Log.i(tag, "type:" + type + "��time:" + time + "showTime" + showTime);
		saveMessagesToDB(userID, friendID, nickName, direction, type, time, content);
	}

	Runnable loginAgainRunnable = new Runnable() {
		public void run() {
			BaseActivity.con = Communication.newInstance();
			// con.addReceiveInfoListener(STATE, ChatMainActivity.this);
		}
	};

	// protected void onDestory(){
	// super.onDestroy();
	// WoliaoBaseActivity.finishAll();
	// }
	//
	// �����β��˳�����
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
