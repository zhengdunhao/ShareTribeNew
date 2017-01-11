package com.atm.chatonline.bbs.activity.login;
/**
 * @author Jackbing
 * ��������
 * 
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
import android.widget.Toast;

import com.atm.chatonline.bbs.commom.IsNetworkAvailable;
import com.atm.chatonline.bbs.commom.MyToast;
import com.atm.chatonline.bbs.util.SendErrorLoginMsg;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;

public class UpDatePassword extends Activity implements OnClickListener{
	private String confirmNumber,userId,tag="UpDatePassword",newPassword,nextPassword,conNumberInput,respCode;
	private IsNetworkAvailable conNetwork=null;//�ж��Ƿ�����������
	private EditText etNewPassword,etNextPassword,etConNumberInput;
	private MyToast toast;
	private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_error_update_password);
		initData();
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
		Button btnSubmit=(Button)findViewById(R.id.btn_update_password);
		Button btnBack=(Button)findViewById(R.id.btn_back);
		etNewPassword=(EditText)findViewById(R.id.edit_update_password_newpassword);
		etNextPassword=(EditText)findViewById(R.id.edit_update_password_nextpassword);
		etConNumberInput=(EditText)findViewById(R.id.edit_update_password_conNumber);
		btnBack.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);
	}
	private void initData() {
		Intent intent=getIntent();
		confirmNumber=intent.getStringExtra("confirmNumber");
		userId=intent.getStringExtra("userId");
		if(!confirmNumber.equals("")){
			Log.i(tag, "confirmNumber is"+confirmNumber);
		}else{
			Log.i(tag, "confirmNumber is null");
		}
	}
	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch(id){
		case R.id.btn_back:
			onBackPressed();
			break;
		case R.id.btn_update_password:
			if(isNetworkAvailable()){
				Log.i(tag, "ȷ����ť�����");
				upDatePassword();
			}else{
				showToast("��ǰû�п������磡");
			}
			break;
		}
		
	}
	private void upDatePassword() {
		newPassword=etNewPassword.getText().toString().trim();
		nextPassword=etNextPassword.getText().toString().trim();
		conNumberInput=etConNumberInput.getText().toString().trim();
		if(newPassword.equals(nextPassword)){
			if(confirmNumber.equals(conNumberInput)){
				new Thread(runnable).start();
			}else{
				showToast("��֤�벻��ȷ��");
			}
		}else{
			showToast("�����������벻һ�£�");
		}
	}
	
	Runnable runnable=new Runnable(){

		@Override
		public void run() {
			SendErrorLoginMsg sendErrorLoginMsg=new SendErrorLoginMsg();
			Log.i(tag, "userId="+userId+" pwd="+newPassword);
			String tips=sendErrorLoginMsg.sendPassword(userId,newPassword);
			//���ؽ��
			if(tips.equals("success")){
				respCode="�޸�����ɹ���";handler.sendEmptyMessage(1);
			}else if(tips.equals("failed")){
				respCode="���벻�淶��";handler.sendEmptyMessage(2);
			}
			
		}
		
	};
	
	/**
	 * �ɹ��޸����룬��ת����¼����
	 */
	public void redirectToLoginView(){
		Intent intent=new Intent(this,LoginView.class);
		Bundle bundle=new Bundle();
		bundle.putInt("login", Config.FIRSTLOGIN);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	/**
	 * �ж��Ƿ�����������
	 * @return
	 */
	private boolean isNetworkAvailable() {
		if(conNetwork==null){
			conNetwork=new IsNetworkAvailable();
		}
		return conNetwork.isNetworkAvailable(UpDatePassword.this);
	}
	
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
