package com.atm.chatonline.chat.ui;



import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.atm.chatonline.chat.net.Communication;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;





public class LoginActivity extends BaseActivity implements OnClickListener{

	private String tag=null;
	private EditText editAccount,editPwd;
	private Button btnLogin;
	private String userID,pwd;
	
	protected  void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_view);
		initUI();
		btnLogin.setOnClickListener(this);
		
	}
	
	void initUI(){
		editAccount=(EditText)findViewById(R.id.edit_account);
		editPwd=(EditText)findViewById(R.id.edit_password);
		btnLogin=(Button)findViewById(R.id.btnLogin);
	}
	
	public void onClick(View v){
		userID=editAccount.getText().toString();
		pwd=editPwd.getText().toString();
		int id = v.getId();
		if (id == R.id.btnLogin) {
			new Thread(runnable).start();
			Log.i(tag, "LoginActivity-----userID:"+userID+"pwd:"+pwd);
		}
	}
	
	public void processMessage(Message msg){
		if(msg.what==Config.SUCCESS){
			Log.i(tag, "LoginActivity----�õ�LOGIN_SUCCESS��������תChatMainActivity");
			Intent intent=new Intent(this,ChatMainActivity.class);
			setPreference(userID,pwd);
			startActivity(intent);
		}else if(msg.what==Config.FAILED){
			Toast.makeText(getApplicationContext(), "�û���¼ʧ��", Toast.LENGTH_SHORT).show();
		}else if(msg.what==Config.USER_LOGIN_ALREADY){
			Toast.makeText(getApplicationContext(), "�û��ѵ�¼", Toast.LENGTH_SHORT).show();
		}
	}
	
	Runnable runnable = new Runnable(){
		public void run(){
			con=Communication.newInstance();//���ﲻ��Ҫprivate Communication con����Ϊ�����con����WoliaoBaseActivity
			if(con==null){
				Log.i(tag, "LoginActivity-----conΪ��");
			}
			con.reqLogin(userID,pwd);
		}
	};
	
	
}
