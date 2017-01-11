package com.atm.chatonline.setting.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.atm.chatonline.bbs.util.BBSConnectNet;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.ClearEditText;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;

/**
 * Author:ZDH Content:�޸�����Ľ��棬�޸ĵ������У������룬������
 * */
public class ChangePassword extends BaseActivity implements OnClickListener {

	private String tag = "ChangePassword";
	private ClearEditText edit_original_pwd, edit_new_pwd, edit_confirm_pwd;
	private Button btnBack, btnSure;
	
	private TextView title;
	private SpannableString sps = null;
	private String titleName ="��������";

	private String oldPwd, newPwd1, newPwd2;
	private String pwd;// ��ϵͳ�����ȡ���������
	private String userID;
	
	//���Ͳ�����������
	private String cookie;
	private String changePwdRelativePath;
	
	//��������Ӧ
	private String response;
	
	private BBSConnectNet bBSConnectNet;
	
	private Handler handler;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_pwd_view);
		initUI();
		
		
		handler = new Handler(){
			public void handleMessage(Message msg){
				if(msg.what==Config.SUCCESS){
					TipAlertDialog("��ʾ","�޸ĳɹ�",true);
					//��ϵͳ���������Ĺ���
					setPreference(userID,newPwd1);
					LogUtil.p(tag, "��飺pwd:"+BaseActivity.getSelf().getPwd());
				}else if(msg.what==Config.FAILED){
					TipAlertDialog("��ʾ","��������쳣",false);
				}
			}
		};
		
	}

	void initUI() {
		edit_original_pwd = (ClearEditText) findViewById(R.id.edit_original_pwd);
		edit_new_pwd = (ClearEditText) findViewById(R.id.edit_new_pwd);
		edit_confirm_pwd = (ClearEditText) findViewById(R.id.edit_confirm_pwd);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnSure = (Button) findViewById(R.id.btn_sure);
		title = (TextView)findViewById(R.id.title);
		sps = new SpannableString(titleName);
		sps.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, titleName.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //���� 0-4����ĳ���
		title.setText(sps);
		title.setTextSize(18);
		btnBack.setOnClickListener(this);
		btnSure.setOnClickListener(this);
		//cookie �͸������������
		SharedPreferences pref = getSharedPreferences("data",Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");
		changePwdRelativePath =  "user/changePassword.do";
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			this.onBackPressed();
			break;
		case R.id.btn_sure:
			oldPwd = edit_original_pwd.getText().toString();
			newPwd1 = edit_new_pwd.getText().toString();
			newPwd2 = edit_confirm_pwd.getText().toString();
			pwd = BaseActivity.getSelf().getPwd();
			userID = BaseActivity.getSelf().getUserID();
			LogUtil.p(tag, "oldPwd:" + oldPwd + ",newPwd1:" + newPwd1
					+ ",newPwd2:" + newPwd2 + ",pwd:" + pwd);
			if (oldPwd.equals(pwd)) {
				LogUtil.p(tag, "ԭʼ������ȷ");// ����֤ԭʼ����
				if (newPwd1.equals(newPwd2)) {// ����֤������
					LogUtil.p(tag, "������һ��");
					//���������뵽������
					sendPwd();
				} else {
					LogUtil.p(tag, "�����벻һ��");
					TipAlertDialog("��ʾ","�����벻һ��",false);
				}
			} else {
				LogUtil.p(tag, "ԭʼ���벻��ȷ");
				TipAlertDialog("��ʾ","ԭʼ��������",false);
			}

			break;
		}
	}

	void TipAlertDialog(String title, String message, final Boolean flag) {
		AlertDialog.Builder sure = new AlertDialog.Builder(ChangePassword.this);
		LogUtil.p(tag, "handler123");
		sure.setTitle(title)
		.setMessage(message)
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (flag) {
							LogUtil.p(tag, "ChangePassword.this.finish()");
							ChangePassword.this.finish();
						} else {
							
						}

					}

				});
		sure.create().show();// �����������û�취��ʾ��ʾ��
	}
	
	void sendPwd(){
		new Thread(sendPwdRunnable).start();
	}
	
	Runnable sendPwdRunnable = new Runnable(){
		public void run(){
			try{
				bBSConnectNet = new BBSConnectNet(changePwdRelativePath,cookie,userID,newPwd1,Config.CHANGE_PWD);
				response = bBSConnectNet.getResponse();
				LogUtil.p(tag, "response:"+response);
				Message msg = new Message();
				if(response.equals(Config.SUCCESS+"")){
					msg.what = Config.SUCCESS;
					handler.sendMessage(msg);
				}else{
					msg.what = Config.FAILED;
					handler.sendMessage(msg);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	};

	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub

	}

}
