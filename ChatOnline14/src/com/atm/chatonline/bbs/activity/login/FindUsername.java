package com.atm.chatonline.bbs.activity.login;
/**
 * @author Jackbing
 * �һ��û���
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.chatonline.bbs.commom.IsNetworkAvailable;
import com.atm.chatonline.bbs.commom.MyToast;
import com.atm.chatonline.bbs.util.CkeckRegisterMessage;
import com.atm.chatonline.bbs.util.SendErrorLoginMsg;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;

public class FindUsername extends Activity implements OnClickListener{
	private EditText edit_find_username;
	private Button btn_find_submit;
	private TextView text_find_appeal;
	private String respCode,eMail="",tag="findusername";
	private MyToast toast;
	private IsNetworkAvailable conNetwork=null;//�ж��Ƿ�����������
	private Handler handler;
	int i=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_username);
		initView();
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO �Զ����ɵķ������
				super.handleMessage(msg);
				switch(msg.what){
				case 1:
					showToast(respCode);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
					redirectToLoginView();
					break;
				case 2:
					showToast(respCode);
					break;
				}
			}
			
		};
		
	}
	private void initView() {
		edit_find_username=(EditText) findViewById(R.id.edit_find_username);
		btn_find_submit=(Button)findViewById(R.id.btn_find_submit);
		text_find_appeal=(TextView)findViewById(R.id.text_find_appeal);
		Button btnBack=(Button)findViewById(R.id.btn_back);
		btn_find_submit.setOnClickListener(this);
		text_find_appeal.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch(id){
		case R.id.btn_find_submit:
			if(isNetworkAvailable()){
				Log.i(tag, "�������һ��˻���������֤��ȷ����ť");
				findUserName();
			
			}else{
				showToast("��ǰû�п������磡");
			}
			break;
		case R.id.text_find_appeal:
			if(isNetworkAvailable()){
				Log.i(tag, "����������������");
				findAppeal();
				
			}else{
				showToast("��ǰû�п������磡");
			}
			break;
		case R.id.btn_back:
			Log.i(tag, "���ذ�ť�����");
			onBackPressed();
			break;
		}
	}
	
	/**
	 * �ж��Ƿ�����������
	 * @return
	 */
	private boolean isNetworkAvailable() {
		if(conNetwork==null){
			conNetwork=new IsNetworkAvailable();
		}
		return conNetwork.isNetworkAvailable(FindUsername.this);
	}
	/**
	 * ��ת�����߽���
	 */
	private void findAppeal() {
		
		startActivity(new Intent(this,UserAppeal.class));
		finish();
	}
	
	/**
	 * �ɹ��һ��˺ţ���ת����¼����
	 */
	public void redirectToLoginView(){
		Intent intent=new Intent(this,LoginView.class);
		Bundle bundle=new Bundle();
		bundle.putInt("login", Config.FIRSTLOGIN);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	private void findUserName() {
		eMail=edit_find_username.getText().toString().trim();
		Log.i(tag,"email ="+eMail);
		//��֤�����Ƿ�淶
		if(new CkeckRegisterMessage().checkEmail(eMail)){
			
			Thread thread=new Thread(runnable);
			thread.start();
		}else{
			showToast("����дһ���淶������");
		}
	}
	
	Runnable runnable=new Runnable(){

		@Override
		public void run() {
			SendErrorLoginMsg sendErrorLogin=new SendErrorLoginMsg();
			String tips=sendErrorLogin.sendEmailMsg(eMail);
			//�ж�respcdoe���
			if(tips.equals("success")){//���ҳɹ�
				respCode="���¼���������ȡ�����˻���";
				handler.sendEmptyMessage(1);//Ϊ�˱��ⴴ����һ��message����ֱ�ӷ���emptymsg�����棬1����ɹ�����ת
				return;						//2����ֻ��ʾrespCode������ת
			}else if(tips.equals("error")){//���淶
				respCode="����������Ӧ";
			}else if(tips.equals("failed")){//δ��ע��
				respCode="������δ��ע��";
			}
			handler.sendEmptyMessage(2);
		}
		
	};
	public void showToast(String s)
		{
			if(toast==null)
			{
				toast=MyToast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
			}else{
				toast.setText(s);toast.setDuration(Toast.LENGTH_SHORT);
			}
			toast.show();
		}
		//cancel��toast
		public void cancelToast()
		{
			if(toast!=null){
				toast.cancel();
			}
		}
		
		@Override
		public void onBackPressed() {
			super.onBackPressed();
			cancelToast();
 			Log.i(">>>>>>", "�˳�");
 			finish();
 		}
	
}
