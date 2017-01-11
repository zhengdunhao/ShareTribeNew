package com.atm.chatonline.bbs.activity.bbs;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.atm.chatonline.bbs.bean.Data;
import com.atm.chatonline.bbs.bean.Department;
import com.atm.chatonline.bbs.adapter.DepartAdapter;
import com.atm.chatonline.bbs.util.BBSConnectNet;
import com.atm.chatonline.bbs.util.ExtendsIntent;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.bbs.util.SendLoginInfo;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;
import com.google.gson.Gson;

public class BBSDepartmentView extends BaseActivity implements android.widget.AdapterView.OnItemClickListener {
	private String response;
	private List<Department> deparList = new ArrayList<Department>();
	private String tag="BBSDepartmentView";
	DepartAdapter adapter;
	private Handler handler;
	private ListView listView;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_depar_list_view);
		initData();
		initView();
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.bbs_depar_list);
//		Message msg1 = new Message();
//		msg1.what = Config.REFRESH_UI;
		Log.i(tag, "�������ϵ�б�����");
//		if(msg1==null){
//			Log.i(tag, "msg1Ϊ��");
//		}else{
//			Log.i(tag, "msg1��Ϊ��");
//		}
//		handler.sendMessage(msg1);
		Log.i(tag, "departlist:"+deparList.size());
		adapter = new DepartAdapter( this, R.layout.bbs_depar_list_item , deparList);
		
		listView.setAdapter(adapter);
		Log.i(tag, "departlist���½�����³ɹ�");
		listView.setOnItemClickListener(this);
		
	}

	private void initData(){
		Thread thread = new Thread(new Runnable(){
			public void run(){
				SharedPreferences pref = getSharedPreferences("data",Context.MODE_PRIVATE);
				BBSConnectNet bBSConnectNet = new BBSConnectNet("", "", 0, "atm_deptList.action", pref.getString("cookie", ""));
				response = bBSConnectNet.getResponse();
				Log.i(tag,"ConnectNetwork");
			}
		});
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Data resultBean = new Gson().fromJson(response,Data.class);
//		Log.i(tag, "departList----222222");
		if(resultBean==null){
			Log.i(tag,"resultBeanΪnull");
		}else{
			Log.i(tag,"resultBean��Ϊnull");
		}
		Log.i(tag, "resultBean.getDepartment():"+resultBean.getDepartment().size());
		for(Department department : resultBean.getDepartment()){
//			Log.i(tag, "departList----44444");
				if(!deparList.contains(department)){
					deparList.add(department);
//					Log.i(tag, "departList----1111");
				}
			}
//		Log.i(tag, "departList----33333");
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Department depart = deparList.get(position);
		String dNo = depart.getDno();
		ExtendsIntent intent = new ExtendsIntent(this,BBSListView.class,
				dNo, "essay_deptEssay.action", "depart" , 1);
		startActivity(intent);
	}

	@Override
	public void processMessage(Message msg) {
		// TODO �Զ����ɵķ������
		if (msg.what == Config.SEND_NOTIFICATION) {
			LogUtil.p(tag, "����Ϣ֪ͨ");
			sendNotifycation();
		}else if (msg.what == Config.LOGIN_SUCCESS) {
			Log.i(tag, "�ڶ��ε�¼�ɹ�");
			Log.i("BBSMainView",
					"LoginActivity----�õ�LOGIN_SUCCESS��������תChatMainActivity");
			// Intent intent=new Intent(this,ChatMainActivity.class);
			// setPreference(username,pwd);
			Thread thread = new Thread(httpLogin);
			thread.start();
			// startActivity(intent);
		}
	}
	Runnable httpLogin = new Runnable() {

		@Override
		public void run() {
			LogUtil.p(tag, "httpLogin-userID:"+BaseActivity.getSelf().getUserID());
			SendLoginInfo sendLoginInfo = new SendLoginInfo(BaseActivity.getSelf().getUserID(),
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
	
}
