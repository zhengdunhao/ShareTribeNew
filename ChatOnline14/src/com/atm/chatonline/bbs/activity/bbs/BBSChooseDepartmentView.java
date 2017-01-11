package com.atm.chatonline.bbs.activity.bbs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.bbs.util.BBSHttpClient;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.bbs.util.PhotoItem;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;

/**
 * @�� com.atm.chatonline.activity.bbs ---BBSChooseDepartmentView
 * @���� ����������ʾѡ��ϵ�����
 * @���� ��YD
 * @ʱ�� 2015-8-24
 * 
 */

public class BBSChooseDepartmentView extends BaseActivity implements
		OnClickListener {
	private RadioGroup radioGroup;
	private TextView next;
	private ImageView iv_return;
	private String subPath = "atm_deptList.action";
	private BBSHttpClient connect;
	private String contentResponse;
	private String selectedDept, selectedDno;
	private Handler handler;
	private static final int UPDATE_RADIOGROUP = 1;
	private Map<String, String> departInfo = new HashMap<String, String>();
	private static String cookie;
	private Context context;
	private String tag = "BBSChooseDepartmentView";
	private Intent intent;
	private List<PhotoItem> selectedPic;
	private List<String> aiteID;
	private String str_content, str_title, str_type;
	private ProgressBar bar;
	private RelativeLayout rl_proBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getCookie();
		requestContent();// �����߳����������ȡ����
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_department);
		intent = getIntent();
		getDataFromPreviousActivity();
		initialViews();
		setListenerForViews();
		//getCookie();
		context = getApplicationContext();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				String department;
				switch (msg.what) {
				case UPDATE_RADIOGROUP:
					int size = departInfo.size();
					int i = 1;
					for (String key : departInfo.keySet()) {
						RadioButton button = new RadioButton(context);
						department = departInfo.get(key);
						Log.d("BBSChooseDepartmentView", department);
						button.setText(department);
						button.setTextColor(Color.BLACK);
						radioGroup.addView(button);
					}
					rl_proBar.setVisibility(View.GONE);
					break;
				}
			}
		};
	}

	private void getDataFromPreviousActivity() {
		// TODO Auto-generated method stub
		str_content = intent.getStringExtra("str_content");
		str_title = intent.getStringExtra("str_title");
		str_type = intent.getStringExtra("str_type");
		if(intent.getStringArrayListExtra("aiteID") != null)
			aiteID = intent.getStringArrayListExtra("aiteID");
		if(intent.getSerializableExtra("selectedPic") != null)
			selectedPic = (ArrayList<PhotoItem>) intent.getSerializableExtra("selectedPic");
	}

	// ��ȡcookie
	private void getCookie() {
		// TODO Auto-generated method stub
		SharedPreferences pref = getSharedPreferences("data",
				Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");
		LogUtil.i("cookie="+cookie);
	}

	private void requestContent() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					connect = new BBSHttpClient(cookie, subPath);
					contentResponse = connect.getResponse();
					processData(contentResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	// �����ȡ��������
	private void processData(String response) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(response);
			JSONArray jsonArray = jsonObject.getJSONArray("department");
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				jsonObject = jsonArray.getJSONObject(i);
				String dno = jsonObject.getString("dno");
				String department = jsonObject.getString("dname");
				departInfo.put(dno, department);
			}
			Message message = new Message();
			message.what = UPDATE_RADIOGROUP;
			handler.sendMessage(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setListenerForViews() {
		// TODO Auto-generated method stub
		radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						// TODO Auto-generated method stub
						int radioButtonId = arg0.getCheckedRadioButtonId();
						// ����ID��ȡRadioButton��ʵ��
						RadioButton rb = (RadioButton) findViewById(radioButtonId);
						selectedDept = (String) rb.getText();
						for (String getKey : departInfo.keySet()) {
							if (departInfo.get(getKey).equals(
									selectedDept)) {
								selectedDno = getKey;
							}
						}
						Log.d(tag, "ѡ�е�ϵ��" + selectedDept);
						Log.d(tag, "ϵ���" + selectedDno);
					}
				});

		next.setOnClickListener(this);
		iv_return.setOnClickListener(this);
	}

	private void initialViews() {
		// TODO Auto-generated method stub
		iv_return = (ImageView) findViewById(R.id.iv_return);
		next = (TextView) findViewById(R.id.next);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		rl_proBar = (RelativeLayout) findViewById(R.id.rl_proBar);
		bar = (ProgressBar) findViewById(R.id.bar);
	}

	@Override
	public void processMessage(Message msg) {
		// TODO �Զ����ɵķ������
		if (msg.what == Config.SEND_NOTIFICATION) {
			sendNotifycation();
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.iv_return:
			AlertDialog.Builder back = new AlertDialog.Builder(this);
			back.setTitle("��ʾ��")
					.setMessage("ȷ��������һ�����棿")
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									BBSChooseDepartmentView.this.finish();
								}

							})
					.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
								}
							});

			back.create().show();
			break;
		case R.id.next:
			if (selectedDno == null) {
				AlertDialog.Builder dept = new AlertDialog.Builder(this);
				dept.setTitle("��ʾ��")
						.setMessage("��ѡ��ϵ��")
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
									}
								});
				dept.create().show();
			} else {
				Intent intent = new Intent(BBSChooseDepartmentView.this,
						BBSChooseLabelView.class);
				intent.putExtra("id", selectedDno);
				intent.putExtra("selectedDept", selectedDept);
				intent.putExtra("str_content", str_content);
				intent.putExtra("str_title", str_title);
				intent.putExtra("str_type", str_type);
				if(selectedPic != null)
					intent.putExtra("selectedPic", (Serializable)selectedPic);
				if(aiteID != null)
					intent.putStringArrayListExtra("aiteID", (ArrayList<String>) aiteID);
				startActivity(intent);
			}
			break;
		}
	}
}
