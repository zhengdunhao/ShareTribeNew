package com.atm.chatonline.chat.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.atm.chatonline.bbs.bean.BBS;
import com.atm.chatonline.bbs.bean.Data;
import com.atm.chatonline.bbs.activity.bbs.BBSPostDetailView;
import com.atm.chatonline.bbs.util.BBSConnectNet;
import com.atm.chatonline.bbs.util.ExtendsIntent;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.bbs.util.ReceivePhoto;
import com.atm.chatonline.usercenter.adapter.NoteAdapter;
import com.example.studentsystem01.R;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class PeopleCollectedNote extends BaseActivity implements OnClickListener{

	private int bbsNums = 0;
	private int count = 0;
	private String response;
	private TextView title;
	private Button btnBack;
	private String userID,friendID,friendNickName;
	private PullToRefreshListView plv;
	private ProgressBar pro;
	private String cookie;
	private List<BBS> bbsList = new ArrayList<BBS>();
	private NoteAdapter noteAdapter;
	private BBSConnectNet bBSConnectNet;
	private Handler handler;
	private String tag ="PeopleCollectedNote";
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.people_published_note);
		initUI();
		initIntent();
		initParams();
		new GetDataTask().execute();
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO �Զ����ɵķ������

				switch (msg.what) {
				case 1:
					initView();
					break;
				case 2:
					pro.setVisibility(View.GONE);
					Toast.makeText(PeopleCollectedNote.this.getApplicationContext(),
							"û�������ˣ�����ˢ��Ŷ��", Toast.LENGTH_SHORT).show();
					break;
				case 3:
					pro.setVisibility(View.GONE);
					Toast.makeText(PeopleCollectedNote.this.getApplicationContext(),
							"�㻹û�з������ӣ��Ͻ�ȥ�����Ӱɣ���", Toast.LENGTH_SHORT).show();
					break;
				}

			}

		};
	}
	
	void initUI(){
		btnBack = (Button)findViewById(R.id.btn_back);
		title = (TextView)findViewById(R.id.title);
		btnBack.setOnClickListener(this);
		pro = (ProgressBar)findViewById(R.id.myprogressbar_note);
		plv = (PullToRefreshListView)findViewById(R.id.lv_mybbs);
	}
	void initIntent(){
		try{
			Intent intent = getIntent();
			userID = intent.getStringExtra("userID");
			friendID = intent.getStringExtra("friendID");
			friendNickName = intent.getStringExtra("nickName");
			title.setText(friendNickName+"�ղص�����");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	void initParams() {
		SharedPreferences pref = getSharedPreferences("data",Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");
		LogUtil.p(tag, "cookie:"+cookie);
	}
	void initView(){
		noteAdapter = new NoteAdapter(PeopleCollectedNote.this,
				R.layout.my_bbs_note_list_item, bbsList);
		plv.setAdapter(noteAdapter);

		// Ϊ���б�����������������
		plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				bbsNums = 0;
				new GetDataTask().execute();
			}

			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				new GetDataTask().execute();
			}
		});

		// ����PullRefreshListView�������ʱ�ļ�����ʾ
		plv.getLoadingLayoutProxy(false, true).setPullLabel("���ڼ���...");
		plv.getLoadingLayoutProxy(false, true).setRefreshingLabel("���ڼ���ing");
		plv.getLoadingLayoutProxy(false, true).setReleaseLabel(
				"�ɿ������Ҿͼ��أ�(*^__^*) ��������");
		// ����PullRefreshListView��������ʱ�ļ�����ʾ
		plv.getLoadingLayoutProxy(true, false).setPullLabel(
				"������������������O(��_��)O����~");
		plv.getLoadingLayoutProxy(true, false).setRefreshingLabel(
				"�׼�����Ŭ��ˢ��ing���I(^��^)�J");
		plv.getLoadingLayoutProxy(true, false).setReleaseLabel(
				"�ɿ������Ҿ�ˢ�£�(*^__^*) ��������");

		plv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ����essayid�����ӡ�
				Log.i(tag, "���ӱ����");
				ExtendsIntent intent = new ExtendsIntent(PeopleCollectedNote.this,
						BBSPostDetailView.class, bbsList.get(position - 1)
								.getEssayId(), null, null, 1);
				Log.i(tag, "���ӱ����111");
				Log.i(tag, bbsList.get(position - 1).getEssayId());
				startActivity(intent);
				// Log.i(tag, "queue.size:"+WoliaoBaseActivity.queue.size());
				Log.i(tag, "���ӱ����2");
			}

		});
		Log.i(tag, "����initView");
		// ����ǵ�һ�μ������滻view��������ǣ������view
		/*
		 * if(count==0){ count++; setContentView(v); }
		 */

		// textView.setVisibility(View.GONE);
		pro.setVisibility(View.GONE);
		plv.setVisibility(View.VISIBLE);
		
	}

	/**
	 * �첽��������
	 */
	private class GetDataTask extends AsyncTask<Void, Void, String> {

		public GetDataTask() {
			Log.i(tag, "+++++++first");
		}

		protected String doInBackground(Void... params) {
			// ����������ͣס���ȴ�2��
			Log.i(tag, "+++++++second");
			// 2015.10.9����Ҹ���һ�£�doInBackground�Ǵ����ʱ���⣬���԰�loadData��������������ANR����---֣
			// Thread.sleep(2000);
			try {
				Log.i(tag, "+++++++third");
				loadData();
				// loadPhoto();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String result) {
			Log.i(tag, "update bbslistview !");
			noteAdapter.notifyDataSetChanged();
			plv.onRefreshComplete();
		}

		/**
		 * �ӷ������˻�ȡ����
		 * 
		 * @return response��json������
		 */
		private String getResponseFromNet() {
			Log.i(tag, "getResponseFromNet");
			Thread thread = new Thread(new Runnable() {
				public void run() {
					bBSConnectNet = new BBSConnectNet("", friendID, bbsNums,
							"essay_collectedEssay.action", cookie);

					Log.i(tag, "getResponseFromNet+BBSConnectNet����");
					response = bBSConnectNet.getResponse();
					Log.i(tag, "Gson:" + response);
					Log.i(tag, "bBSConnectNet.getResponse()����");
				}
			});
			thread.start();
			try {
				thread.join();
			} catch (InterruptedException e) {
				Log.d(tag, "getResponseFromNet-�̱߳����");
				e.printStackTrace();
			}
			Log.i(tag, "555");
			return response;
		}

		/**
		 * �첽���µļ�������
		 * 
		 * @param page2
		 * @throws JSONException
		 */
		public void loadData() throws JSONException {

			Log.d(tag, "loadData");

			if (bbsNums == 0) {
				// �Ȱ��б����
				Log.d(tag, "bbsList.clear()");
				bbsList.clear();
			}
			response = getResponseFromNet();
			Log.d(tag, "loadData+1");

			Data data = new Gson().fromJson(response, Data.class);

			Log.d(tag, "loadData+3");

			addData(data);
			// ��BBSFirst�е�BBS�б���ӵ�BBS bean����
			for (BBS bbs : bbsList) {
				Log.i(tag, "img path" + bbs.getHeadImagePath().toLowerCase());
			}
			Log.d(tag, "loadData����");
			if (count == 0) {
				handler.sendEmptyMessage(1);
				count++;
			}
		}
	}
	

	/**
	 * �������� ��BBSFirst�е�BBS�б���ӵ�BBS bean���У�����ȡͼƬ
	 * 
	 * @param dataFromJson
	 *            : BBSFirst��ʵ����
	 */
	private void addData(Data data) {

		Log.d(tag, "addData");

		// ��BBSFirst�е�BBS�б���ӵ�BBS bean����
		for (BBS bbs : data.getBbs()) {
			if (!bbsList.contains(bbs)) {
				bbsList.add(bbs);
				bbsNums++;
			}
		}

		Log.i(tag, "bbsList.size()" + bbsList.size());
		int count = 1;
		// ��ȡ��Ƭ��
		for (BBS bbs : bbsList) {
			Log.i(tag, "��" + count++ + "����¼");
			bbs.setHeadImage(new ReceivePhoto(bbs.getHeadImagePath())
					.getPhoto());
			// ��ȡ��ǩ���ͱ�ǩ��ɫ
			Log.i(tag, "��ȡ��ǩ���ͱ�ǩ��ɫ---111");
			bbs.setLabName0(bbs.getLabName().split("\\*#"));
			Log.i(tag, "��ȡ��ǩ���ͱ�ǩ��ɫ---222");
			String[] colors = bbs.getLabColor().split("\\*");
			int[] color = new int[colors.length];
			for (int i = 0; i < colors.length; i++) {
				color[i] = Color.parseColor(colors[i]);
			}
			bbs.setLabColor0(color);
			Log.i(tag, "��ȡ��ǩ���ͱ�ǩ��ɫ---333");
		}
	}

	@Override
	public void processMessage(Message msg) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void onClick(View v) {
		// TODO �Զ����ɵķ������
		switch(v.getId()){
		case R.id.btn_back:
			this.onBackPressed();
			break;
		}
	}

}
