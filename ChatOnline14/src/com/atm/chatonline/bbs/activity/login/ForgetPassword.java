package com.atm.chatonline.bbs.activity.login;
/**
 * @author Jackbing
 * �һ�����
 */
import org.json.JSONException;
import org.json.JSONObject;

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
import com.atm.chatonline.bbs.util.SendErrorLoginMsg;
import com.example.studentsystem01.R;

public class ForgetPassword extends Activity implements OnClickListener{
	private EditText forgetusername;
	private Button forgetsubmit;
	private TextView textviewappeal,title;
	private String sourceStr,tag="ForgetPassword",respCode="",confirmNumber,userId;
	private IsNetworkAvailable conNetwork=null;//�ж��Ƿ�����������
	private MyToast toast;
	private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_password);
		Log.i(tag, ">>>>");
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
					toUpatePassword();
					break;
				case 2:
					showToast(respCode);
					break;
				}
			}
			
		};
	}
	private void initView() {
		forgetusername=(EditText)findViewById(R.id.edit_forget_pwd);
		forgetsubmit=(Button)findViewById(R.id.btn_forget_submit);
		textviewappeal=(TextView)findViewById(R.id.text_forget_appeal);
		title = (TextView)findViewById(R.id.title);//2017.7.22
		title.setText("�һ�����");
		title.setTextSize(18);
		Button btnBack=(Button)findViewById(R.id.btn_back);
		forgetsubmit.setOnClickListener(this);
		textviewappeal.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch(id){
		case R.id.text_forget_appeal:
			if(isNetworkAvailable()){
				Log.i(tag, "�������ӱ����");
				forgetAppeal();
			}else{
			showToast("��ǰû�п������磡");
			}
			break;
		case R.id.btn_forget_submit:
			if(isNetworkAvailable()){
			Log.i(tag, "ȷ�ϰ�ť�����");
			forgetPassword();
			}else{
				showToast("��ǰû�п������磡");
			}
			break;
		case R.id.btn_back:
			onBackPressed();
			break;
		}
		
	}
	
	
	private void forgetPassword() {
		sourceStr=forgetusername.getText().toString().trim();
		Log.i(tag, "username ="+sourceStr);
		if(sourceStr.equals("")){
			showToast("�������û�����󶨵�����");
		}else{
			 new Thread(runnable).start();
		}
	}
	
	Runnable runnable=new Runnable(){

		@Override
		public void run() {
			SendErrorLoginMsg sendErrorLogin=new SendErrorLoginMsg();
			JSONObject obj=sendErrorLogin.sendUserName(sourceStr);
			try {
				if(obj.getString("tip").equals("success")){//������ڣ��ѷ�����֤�뵽��������
					respCode="�뵽���������ȡ��֤�룡";
					confirmNumber=obj.getString("captchas");
					userId=obj.getString("userId");
					Log.i(tag, "confirmNumber ="+confirmNumber);
					Log.i(tag, "confirmNumber ="+userId);
					handler.sendEmptyMessage(1);//���ⴴ��û��Ҫ��message����ֱ����sendemptymsg
					return;							//1�����û����ڲ���ת������������棬2��ʾ����������ʾ
				}else if(obj.getString("tip").equals("unRegister")){//�û���������δ��ע��
					respCode="�û���������δ��ע��";
				}else if(obj.getString("tip").equals("failed")){
					respCode="�û�����������д����";
				}
			} catch (JSONException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			handler.sendEmptyMessage(2);
		}
		
	};
	
	/**
	 * �ж��Ƿ�����������
	 * @return
	 */
	private boolean isNetworkAvailable() {
		if(conNetwork==null){
			conNetwork=new IsNetworkAvailable();
		}
		return conNetwork.isNetworkAvailable(ForgetPassword.this);
	}
	/**
	 * ��ת�������������
	 */
	public void toUpatePassword(){
		Intent intent=new Intent(this,UpDatePassword.class);
		intent.putExtra("confirmNumber", confirmNumber);
		intent.putExtra("userId", userId);
		startActivity(intent);
		
	}
	/**
	 * ��ת�����߽���
	 */
	private void forgetAppeal() {
		
		startActivity(new Intent(this,UserAppeal.class));
		finish();
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
