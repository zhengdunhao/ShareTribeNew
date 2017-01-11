package com.atm.chatonline.chat.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.atm.chatonline.chat.util.FileUtil;
import com.example.studentsystem01.R;



public class CreateGroupActivity extends BaseActivity implements OnClickListener{
	
	private EditText nameEditor,descEditor,labelEditor;
	private String userId,groupName,groupDesc,groupLabel;
	private int groupProperty;
	private String groupHeadImg="create_group_headimg.png",path="sdcard/IMG_20150801_212211.jpg";//Ŀǰͷ��Ĭ��Ϊһ��
	private Bitmap bm;
	private RadioGroup generRadio;
	private RadioButton publicRadio,hideRadio;
	private Button btnSubmit,btnOnBack;
	protected Handler handler;
	private final String IMAGE_TYPE="image/*";
	private final int IMAGE_CODE=0;
	private ImageView img;
	final static String TAG="CreateGroupActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_group_view);
		initData();
		initUI();
		generRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId==publicRadio.getId()){
					groupProperty=1;
				}else if(checkedId==hideRadio.getId()){
					groupProperty=0;
				}	
			}
		});	
	}
	/**
	 * @��ʼ��UI
	 */
	private void initUI() {
		nameEditor=(EditText) findViewById(R.id.group_name);
		descEditor=(EditText) findViewById(R.id.group_desc);
		labelEditor=(EditText) findViewById(R.id.group_label);
		generRadio=(RadioGroup) findViewById(R.id.group_property_radiogroup);
		publicRadio=(RadioButton) findViewById(R.id.public_group);
		hideRadio=(RadioButton) findViewById(R.id.hide_group);
		btnSubmit=(Button) findViewById(R.id.btn_create_group);
		btnOnBack=(Button) findViewById(R.id.create_group_back);
		img=(ImageView) findViewById(R.id.create_group_headimg);
		img.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);
		btnOnBack.setOnClickListener(this);
	}
	/**
	 * @��ʼ�� 
	 */
	private void initData() {
		SharedPreferences pref = getSharedPreferences("User",MODE_PRIVATE);
		userId=pref.getString("userID", "");
	}



	Runnable runnable=new Runnable(){
		@Override
		public void run() {
			try {
				sendCreateMsg(userId,groupName,groupDesc,groupLabel,groupProperty,groupHeadImg,path);
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
	};
	
	public void sendCreateMsg(String userId,String groupName,String groupDesc,String groupLabel,int groupProperty,String groupHeadImg,String path) throws IOException{//�����½�Ⱥ����Ϣ�������
		con.sendCreateGroupMsg(userId, groupName, groupDesc, groupLabel, groupProperty, groupHeadImg, path);
	}
	
	@Override
	public void onClick(View v) {
		int id=v.getId();
		if(id==R.id.btn_create_group){//ȷ���½�Ⱥ��ť
			groupName=nameEditor.getText().toString().trim();
			groupDesc=descEditor.getText().toString().trim();
			groupLabel=labelEditor.getText().toString().trim();
			new Thread(runnable).start();
		}else if(id==R.id.create_group_back){//���ذ�ť
			CreateGroupActivity.this.onBackPressed();
		}else if(id==R.id.create_group_headimg){
			getAblum();
		}
	}
	
	@Override
	public void processMessage(Message msg) {
		
		Bundle bundle=msg.getData();
		boolean flag=bundle.getBoolean("flag");
		if(flag){
		int groupId=bundle.getInt("groupId");
		try {
			bm=FileUtil.ByteToBitmap(FileUtil.makeFileToByte(path));
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		//�Ƚ�ͼƬ���뱾��sd��
		File file=FileUtil.createFile(userId, groupId);
		FileUtil.saveBitmap(file, bm);
		recyle(bm);
		//����Ⱥ��Ϣ�������ݿ�
		saveToDB(userId,groupId,groupName, groupDesc, groupLabel, groupProperty);
		onBackPressed();
		}else{
			Toast.makeText(getApplicationContext(), "����Ⱥ��ʧ��", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	//����bitmap
	public void recyle(Bitmap bm){
		if(bm!=null&&!bm.isRecycled()){
			bm.recycle();
			System.gc();
		}
	}
	
	public void saveToDB(String userId,int groupId,String groupName,String groupDesc,String groupLabel,int groupProperty ){
		ContentValues values=new ContentValues();
		values.put("user_Id", userId);
		values.put("group_Id", groupId);
		values.put("groupName", groupName);
		values.put("groupDesc", groupDesc);
		values.put("groupLabel", groupLabel);
		values.put("groupProperty", groupProperty);
		dbUtil.insertGroupInfo(values);
	}
	
	public void getAblum(){
		Intent getAlblum=new Intent(Intent.ACTION_GET_CONTENT);
		getAlblum.setType(IMAGE_TYPE);
		Log.i(TAG, "2222222");
		startActivityForResult(getAlblum,IMAGE_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO �Զ����ɵķ������
		//super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode!=RESULT_OK){
			Log.i("CreateGroup", "ActivityResult resultCode error");
			return;
		}
		Log.i(TAG, "333333333");
		Bitmap bm=null;
		Cursor cursor=null;
		ContentResolver resolver=getContentResolver();
		if(requestCode==IMAGE_CODE){
			Uri uri=data.getData();
			try {
				bm=MediaStore.Images.Media.getBitmap(resolver, uri);
				String[] proj=new String[]{MediaStore.Images.Media.DISPLAY_NAME,MediaStore.Images.Media.DATA};
				//cursor=this.managedQuery(uri, proj, null, null, null);
				cursor=resolver.query(uri, proj, null, null, null);
				int name_index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
				cursor.moveToFirst();
				groupHeadImg=cursor.getString(name_index);
				Log.i("CreateGroup", "�ļ����ǣ�"+groupHeadImg);
				int path_index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				path=cursor.getString(path_index);
				Log.i("CreateGroup", "�ļ�·���ǣ�"+path);
				Log.i(TAG, "444444444");
				 changeHeadImg();
			} catch (FileNotFoundException e) {
				Log.i("creategroup", "�ļ��Ҳ���");
				e.printStackTrace();
			} catch (IOException e) {
				Log.i("creategroup", "io error");
				e.printStackTrace();
			}finally{
				if(cursor!=null&&!cursor.isClosed()){
					cursor.close();
				}
			}	
		}
	}
	
	public void changeHeadImg(){
		Drawable d = null;
		try {
			Log.i(TAG, "666666666");
			d = new BitmapDrawable(FileUtil.ByteToBitmap(FileUtil.makeFileToByte(path)));
			Log.i(TAG, "7777777");
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		Log.i(TAG, "88888888");
		img.setImageDrawable(d);
		Log.i(TAG, "99999999");
		img.invalidate();
		Log.i(TAG, "$$$$$$$$$$");
	}
	
	
	
}
