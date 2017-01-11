package com.atm.chatonline.chat.util;

/**
 * �����˽�ĵ����ݿ⣬���ݿ��DB_NAME="atm_chat_db"
 * ��һ�ű���messages��(auto_Id integer primary key autoincrement, self_Id varchar(10) , friend_Id varchar(10), direction int, type int, content varchar(240), time varchar(12),showTime int);"
 * 
 * */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.info.ChatMessage;
import com.atm.chatonline.chat.info.Friend;
import com.atm.chatonline.chat.info.Group;
import com.atm.chatonline.chat.info.GroupChatMessage;
import com.example.studentsystem01.R;



public class DatabaseUtil extends SQLiteOpenHelper{

	private static String tag="DatabaseUtil";
	private final String TABLE_NAME="messages";
	private final static String DB_NAME="atm_chat_db";
	private final String TABLE_NAME_GROUP="groupmessage";
	private final String TABLE_GROUP="mygroup";
	private final String TABLE_FRIEND_LIST="friend_list";
	private final String TABLE_STATUS="status_list";
	static Context context;
//	private static DatabaseUtil instance;
	
//	public static DatabaseUtil newInstance(){
//		Log.i(tag, "DatabaseUtil������������");
//		if(instance==null){
//			instance = new DatabaseUtil(context);
//			Log.i(tag, "DatabaseUtil��������");
//		}
//		return instance;
//	}
	
	
	
	public DatabaseUtil(Context context){
		this(context.getApplicationContext(),DB_NAME,null,1);
	}
	
	public DatabaseUtil(Context context,String name,CursorFactory factory, int version){
		super(context,name,factory,version);
		
	}
	
	public void onCreate(SQLiteDatabase db){
		//message�����ѸĹ���2015.8.25.11:34----jackbing
		String sql="create table "+TABLE_NAME+"(auto_Id integer primary key autoincrement, self_Id varchar(10) , friend_Id varchar(10), friend_nickName varchar(32),direction int, type int, content varchar(320), time varchar(12),showTime int);";
		String sql_1="create table '"+TABLE_NAME_GROUP+"'(_Id integer primary key autoincrement,self_Id varchar(15),group_Id varchar(15),direction int,type int," +
				"content varchar(225),time varchar(30),showTime integer)";
		String sqlgroup="create table '"+TABLE_GROUP+"'(g_id integer primary key autoincrement,user_Id varchar(10), group_Id int, groupDesc varchar(25), groupName varchar(15), groupLabel varchar(10)," +
				"groupProperty int);";
		
		//sqlFriendList:����һ�Ź�ע�ͷ�˿������ֶ�:relationship�������û���˵��2002��ʾ��ע��2003��ʾ��˿
		String sqlFriendList="create table "+TABLE_FRIEND_LIST+"(_Id integer primary key autoincrement,self_Id varchar(10),friend_Id varchar(10),friend_nickName varchar(32),department varchar(32),relationship int)";
		String sqlStatus="create table "+TABLE_STATUS+"(_Id integer primary key autoincrement,flag_id varchar(10) default 'status', status_id integer default 0);";
		db.execSQL(sql);
		db.execSQL(sql_1);
		db.execSQL(sqlgroup);
		db.execSQL(sqlFriendList);
		db.execSQL(sqlStatus);
	}
	public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
		
	}
	
	//��ѯ״̬
	public  int queryStatus(){
		SQLiteDatabase db=getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from "+TABLE_STATUS+" where flag_id=?",new String[]{"status"});
		if(cursor==null||cursor.getCount()==0){
			LogUtil.p(tag, "��ѯ״̬�޼�¼");
			insertStatus();
			cursor.close();
			return 0;
		}else{
			cursor.moveToFirst();
			LogUtil.p(tag, "�м�¼");
			int status_id = cursor.getInt(cursor.getColumnIndex("status_id"));
			cursor.close();
			return status_id;			
		}
	}
	
	//��ѯ�������û��������¼������ȡ���µ�һ����Ϣ������ȡ�Է���ͷ���ǳ�
	public List<Friend> queryPersonalChatList(String userId){

		//List<String> friendIdList=new ArrayList<String>();//�ռ�����id
		List<Friend> friends=new ArrayList<Friend>();
		SQLiteDatabase db=getReadableDatabase();
		//String selection="self_Id=?";
		String []selectionArgs=new String[]{userId};
		Log.i("��ѯ", "������ѯ��¼");
		//Cursor cursor=db.rawQuery("select * from "+TABLE_NAME, null);
		Cursor cursor1=db.rawQuery("select distinct friend_Id,friend_nickName from "+TABLE_NAME+" where self_Id=?", selectionArgs);
		//Cursor cursor=db.rawQuery("select distinct m.friend_Id , f.friend_nickName from "+TABLE_NAME+" as m,"+TABLE_FRIEND_LIST+" as f where m.self_Id=? and m.friend_Id=f.friend_Id", selectionArgs);
		/*Cursor cursor=db.query(TABLE_NAME, null, selection, selectionArgs, null, null, "time");
		*/if(cursor1==null||cursor1.getCount()==0){
			Log.i(tag,"��ѯ������ʷ��¼");
			return friends;
		}else
		{
			Log.i("cursor ���������", "cursror ��СΪ"+cursor1.getCount());
		}
//		android�����ݿ⴦��ʹ��cursorʱ���α겻�Ƿ���Ϊ0���±꣬���Ƿ���Ϊ-1���±괦��ʼ�ġ�
//		Ҳ����˵���ظ�cursor��ѯ���ʱ�����ܹ����ϴ�cursor����ȡֵ��  ***************************** ---2015-8-24-21:32
			cursor1.moveToFirst();
			
			
			while(!cursor1.isAfterLast()){
				Friend friend=new Friend();
				
				Log.i(tag, "friend_id>>>>>"+cursor1.getString(cursor1.getColumnIndex("friend_Id")));
				Log.i(tag, "nickName>>>>>"+cursor1.getString(cursor1.getColumnIndex("friend_nickName")));
				String friendId=cursor1.getString(cursor1.getColumnIndex("friend_Id"));
				String nickName=cursor1.getString(cursor1.getColumnIndex("friend_nickName"));
				friend.setFriendID(friendId);
				friend.setNickName(nickName);
				friends.add(friend);
				cursor1.moveToNext();
			}
			Log.i(tag,"friends size>>>"+friends.size());
			cursor1.close();
			
			
			/*for(int i=0;i<friends.size();i++){
				String[] selectionArgs2=new String[]{friends.get(i).getFriendID()};
				Cursor cursor2=db.rawQuery("select friend_nickName from "+TABLE_FRIEND_LIST+" where friend_Id=?", selectionArgs2);
				if(cursor2.getCount()==0||cursor2==null){
					Log.i(tag, "cursor2>>>c��ѯ������¼ ");
				}else{
					Log.i(tag, "cursor2>>>"+cursor2.getCount());
				}
				cursor2.moveToFirst();
				while(!cursor2.isAfterLast()){
					String nickName=cursor2.getString(cursor2.getColumnIndex("friend_nickName"));
					friends.get(i).setNickName(nickName);
					Log.i(tag, "nickName>>>"+nickName);
					cursor2.moveToNext();
				}
				cursor2.close();
			}*/

		db.close();
		return friends;
	}
	//��ѯ���ѵ������¼
	public ArrayList<ChatMessage> queryMessages(String selfID,String friendID){
		
		
		Log.i(tag, "�������ݿ⼴������������"+friendID+"���ѵ���ʷ�����¼");
		ArrayList<ChatMessage> list = new ArrayList<ChatMessage>();
		Log.i(tag, "DatabaseUtil-----1111");
		try{
		SQLiteDatabase db =getReadableDatabase();
		
		Log.i(tag, "DatabaseUtil-----2222");
		String selection="self_Id=? and friend_Id=?";
		String []selectionArgs = new String[]{selfID,friendID};
		Log.i(tag, "DatabaseUtil-----3333");
		Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, "time");
		Log.i(tag, "�������");
		if(cursor==null||cursor.getCount()==0){//��ѯ������Ϊ��
			Log.i(tag, "�������ݿ���Ҳ������"+friendID+"���ѵ���ʷ�����¼,��¼Ϊ��");
			if(list!=null){
			Log.i(tag, "list��Ϊ��");
			}
			return list;
		}
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.setSelfID(cursor.getString(cursor.getColumnIndex("self_Id")));
			chatMessage.setFriendID(cursor.getString(cursor.getColumnIndex("friend_Id")));
			chatMessage.setDirection(cursor.getInt(cursor.getColumnIndex("direction")));
			chatMessage.setType(cursor.getInt(cursor.getColumnIndex("type")));
			chatMessage.setContent(cursor.getString(cursor.getColumnIndex("content")));
			Log.i(tag, "�鿴��Ϣ�洢˳��---content:"+cursor.getString(cursor.getColumnIndex("content")));
			chatMessage.setTime(cursor.getString(cursor.getColumnIndex("time")));
			Log.i(tag, "�鿴��Ϣ�洢˳��---time:"+cursor.getString(cursor.getColumnIndex("time")));
			chatMessage.setShowTime(cursor.getInt(cursor.getColumnIndex("showTime")));
			
			list.add(chatMessage);
			cursor.moveToNext();
			
			Log.i(tag, "��ѯ������ʷ���������ҵ�ID�ǣ�"+chatMessage.getSelfID());
			Log.i(tag, "��ѯ������ʷ�����������ѵ�ID�ǣ�"+chatMessage.getFriendID());
		}
		Log.i(tag, "�������ݿ���ҵ����"+friendID+"���ѵ���ʷ�����¼,�ܹ���"+cursor.getCount()+"����¼");
		cursor.close();
		db.close();
		
		}catch(Exception e){e.printStackTrace();}
		return list;
		
		
	}
	
	//��ѯȺ��Ϣ��¼
		public List<GroupChatMessage> queryGroupChatMessage(String userId,String groupId){
			
			List<GroupChatMessage> list=new ArrayList<GroupChatMessage>();
			SQLiteDatabase db=getReadableDatabase();
			//�������
			String selection="self_Id =? and group_Id=?";
			String[] selectionArgs=new String[]{userId,groupId};
			Cursor cursor=db.query(TABLE_NAME_GROUP, null, selection, selectionArgs, null, null, "time");
			if(cursor==null||cursor.getCount()==0){
				Log.i(tag, "cursor is null");
				return list;
			}
			Log.i(tag, "cursor count="+cursor.getCount());
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				GroupChatMessage msg=new GroupChatMessage();
				msg.setDirection(cursor.getInt(cursor.getColumnIndex("direction")));
				Log.i(tag, msg.getDirection()+" ********");
				if(msg.getDirection()==GroupChatMessage.MESSAGE_FROM){//������������Ѿ�ͨ����ѯ����ͷ����������ͷ������ȡsd��ָ��·���ϵ�ͷ��
					msg.setHeadImg(R.drawable.xiaohei);
				}else{
					msg.setHeadImg(R.drawable.me);
				}
				msg.setContent(cursor.getString(cursor.getColumnIndex("content")));
				msg.setTime(cursor.getString(cursor.getColumnIndex("time")));
				msg.setShowTime(cursor.getInt(cursor.getColumnIndex("showTime")));
				list.add(msg);
				cursor.moveToNext();
			}
			cursor.close();
			db.close();
			return list;
		}
	
	//��ѯȺ�б�
	public Map<String,Object> queryGroupList(String userId){
		List<Group> list=new ArrayList<Group>();
		boolean flag=true;
		Map<String,Object> map=new HashMap<String,Object>();
		SQLiteDatabase db=getReadableDatabase();
		String selection="user_Id =?";
		Log.i(tag, userId);
		String[] selectionArgs=new String[]{userId};
		Cursor cursor=db.query(TABLE_GROUP, null, selection, selectionArgs, null, null, null);
		if(cursor==null||cursor.getCount()==0){
			Log.i(tag, "û�и�Ⱥ��¼");
			map.put("groupList", list);
			map.put("flag", flag);
			return map;
		}else{
			flag=false;
		}
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Group group=new Group();
			group.setGroupId(cursor.getString(cursor.getColumnIndex("group_Id")));
			group.setGroupName(cursor.getString(cursor.getColumnIndex("groupName")));
			group.setGroupDesc(cursor.getString(cursor.getColumnIndex("groupDesc")));
			group.setGroupLabel(cursor.getString(cursor.getColumnIndex("groupLabel")));
			group.setGroupPorperty(cursor.getInt(cursor.getColumnIndex("groupProperty")));
			list.add(group);
			cursor.moveToNext();
		}
		cursor.close();
		db.close();
		map.put("groupList", list);
		map.put("flag", flag);
		return map;
	}
	
	//�ж�ĳ�����Ƿ��Ѿ�����ע��
		public boolean queryIsAttetion(String userID,String friendID,int relationship){
			SQLiteDatabase db = getReadableDatabase();
			String selection="self_Id =? and friend_Id=? and relationship="+relationship;
			Log.i(tag, "queryIsAttetion()--self_Id:"+userID+"��friend_Id:"+friendID+"��relationship:"+relationship);
			String[] selectionArgs=new String[]{userID,friendID};
			Cursor cursor = db.query(TABLE_FRIEND_LIST, null, selection, selectionArgs, null, null, null);
			if(cursor!=null&&cursor.getCount()==0){
				Log.i(tag, "�㻹û��ע����ˣ�����IDΪ:"+friendID);
				return false;
			}
			else if(cursor.getCount()==1){
				return true;
			}
			return false;
			
		}
		
	//�ж�TABLE_FRIEND_LIST�����Ƿ��м�¼
		public boolean queryIsFriendList(String userID){
			SQLiteDatabase db = getReadableDatabase();
			String selection = "self_Id=?";
			Log.i(tag, "queryIsFriendList---userID"+userID);
			String[] selectionArgs=new String[]{userID};
			Cursor cursor = db.query(TABLE_FRIEND_LIST, null, selection, selectionArgs, null, null, null);
			if(cursor!=null&&cursor.getCount()==0){
				Log.i(tag, "TABLE_FRIEND_LIST��û�м�¼");
				return false;
			}
			else if(cursor.getCount()>=1){
				return true;
			}
			return false;
		}
		
	/**--������Ϣ--**/
		//������ģʽ��״̬���ȥ
	public void insertStatus(){
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("status_id", 0);
		db.insert(TABLE_STATUS, null, cv);
	}	
	
	
	//����������Ϣ
	public void insertMessages(ContentValues values){
		SQLiteDatabase db = getWritableDatabase();
		db.insert(TABLE_NAME, null, values);
		String userID = values.getAsString("self_Id");
		String friendID = values.getAsString("friend_Id");
		Log.i(tag, "@@@@@@@@@userID:"+userID);
		Log.i(tag, "@@@@@@@@@friendID:"+friendID);
		Log.i(tag, "insertMessages()�����Ѿ����뱾�����ݿ�");
		db.close();
	}
	//����Ⱥ������Ϣ
	public  void insertGroupChatMessage(ContentValues values){
		SQLiteDatabase db=getWritableDatabase();
		db.insert(TABLE_NAME_GROUP, null, values);
		String selfId=values.getAsString("userId");
		String groupId=values.getAsString("groupId");
		String content=values.getAsString("content");
		String time=values.getAsString("time");
		int direction=values.getAsInteger("direction");
		Log.i(tag, "selfId = "+selfId+" ,groupId= "+groupId +" ,content ="+content+",time= "+time+",direction= "+direction);
		db.close();
	}
	
	//����Ⱥ��Ϣ
	public void insertGroupInfo(ContentValues values){
		SQLiteDatabase db=getWritableDatabase();
		db.beginTransaction();
		db.insert(TABLE_GROUP, null, values);
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}
	
	//�����ע���˿�б�
		public void insertFriendList(ContentValues values){
			SQLiteDatabase db = getWritableDatabase();
			db.insert(TABLE_FRIEND_LIST, null, values);
			db.close();
			Log.i(tag, "������Ϣ�ѱ�����");
		}
		
		/**--������Ϣ--**/
		
		public void updateStatus(int status){
			LogUtil.p(tag, "status:"+status);
			SQLiteDatabase db = getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put("status_id", status);
			String[] args={"status"};
			db.update(TABLE_STATUS, cv, "flag_id=?", args);
			LogUtil.p(tag, "update��鿴:"+queryStatus());
		}
		
		
		/**--ɾ����Ϣ--**/
		
		public void deleteFriendList(String userID,String friendID,int relationship){
			SQLiteDatabase db = getWritableDatabase();
			Log.i(tag, "deleteFriendList()---self_Id:"+userID+"��friend_Id:"+friendID+"��relationship:"+relationship);
			String selection="self_Id =? and friend_Id=? and relationship="+relationship;
			String[] selectionArgs=new String[]{userID,friendID};
			db.delete(TABLE_FRIEND_LIST, selection, selectionArgs);
			
		}
}
