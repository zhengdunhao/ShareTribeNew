package com.atm.chatonline.bbs.activity.bbs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.chatonline.bbs.adapter.BBSAdapter;
import com.atm.chatonline.bbs.bean.BBS;
import com.atm.chatonline.bbs.bean.Data;
import com.atm.chatonline.bbs.commom.NewMessage;
import com.atm.chatonline.bbs.util.BBSConnectNet;
import com.atm.chatonline.bbs.util.ConfigUtil;
import com.atm.chatonline.bbs.util.ExtendsIntent;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.bbs.util.ReceivePhoto;
import com.atm.chatonline.bbs.util.SendLoginInfo;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * @�� com.atm.chatonline.activity.bbs ---BBSListView
 * @���� �����������������б����ʾ
 * @���� ��С��
 * @ʱ�� 2015-8-16
 * 
 */
public class BBSListView extends BaseActivity implements OnClickListener {

	// ���ݸ�������������Ĳ���
	private static String id;
	private String tip = "";
	private String relativePath;

	private BBSConnectNet bBSConnectNet;
	private List<BBS> bbsList = new ArrayList<BBS>();
	private BBSAdapter bbsAdapter;
	private PullToRefreshListView plv;
	private int bbsNums = 0;
	private String response;
	private String cookie;
	private LinearLayout layout;
	private String tag = "BBSListView";
	private static TextView bbsWait;
	private static Handler handler;
	private boolean isNeedCache = true;

	private NewMessage newMessage;
	private boolean loadnewMessage=false;

	private TextView more;


	public static Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public static TextView getBbsWait() {
		return bbsWait;
	}

	public void setBbsWait(TextView bbsWait) {
		this.bbsWait = bbsWait;
	}

	private TextView bbsSearchGone;
	private Button btnBack;

	private long mExitTime;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bbs_list_view);
		initParams();
		initView();
		bbsWait = (TextView) findViewById(R.id.bbs_wait);
		bbsWait.setVisibility(View.GONE);
		// new GetDataTask().execute();
		try {
			initData();
			loadPhoto();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Config.SEARCHESSAY:
					bbsNums = 0;
					new GetDataTask().execute();
					new GetPhotoTask().execute();
					break;
				case 0101:
					bbsSearchGone.setVisibility(View.VISIBLE);
				}
			}
		};

	}

	public static String getId() {
		return id;
	}

	public static void setId(String id) {
		BBSListView.id = id;
	}

	/**
	 * ��ʼ�������� ��ȡ��Bundle����Ĳ����� �ò��������ڴ��ݸ�������������Ĳ����� �ĸ�������id;tip;relativePath
	 */
	private void initParams() {

		Log.i(tag, "initParams");

		Bundle bundle = this.getIntent().getExtras();
		this.relativePath = bundle.getString("relativePath");
		this.id = bundle.getString("id");
		this.tip = bundle.getString("tip");
		

		Log.i(tag, "id:" + id + ",tip:" + tip + ",relativePath:" + relativePath);

		SharedPreferences pref = getSharedPreferences("data",
				Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");
		ConfigUtil.COOKIE = cookie;
		Log.i(tag, "cookie:" + cookie);
	}

	/**
	 * ��ʼ������
	 */
	private void initView() {

		Log.i(tag, "����initView");

		// ����ȴ����ؽ��档��
		// bbsWait = (TextView) findViewById(R.id.bbs_wait);
		// ����������ʱ��ʾ��
		bbsSearchGone = (TextView) findViewById(R.id.bbs_search_gone);
		// �ж��Ƿ��б�����
		if (tip.equals("depart")) {
			Log.i(tag, "tip.equals(depart");
			layout = (LinearLayout) findViewById(R.id.title);
			layout.setVisibility(View.VISIBLE);
		}

		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
		// ʵ����PullToRefreshListView��������������
		plv = (PullToRefreshListView) findViewById(R.id.lv_home);
		bbsAdapter = new BBSAdapter(this, R.layout.bbs_list_item, bbsList);
		plv.setAdapter(bbsAdapter);
		// Ϊ���б�����������������
		plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				bbsNums = 0;
				new GetDataTask().execute();
				new GetPhotoTask().execute();
			}

			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				new GetDataTask().execute();
				new GetPhotoTask().execute();
			}
		});

		// ����PullRefreshListView�������ʱ�ļ�����ʾ
		plv.getLoadingLayoutProxy(false, true).setPullLabel(
				"������������������O(��_��)O����~");
		plv.getLoadingLayoutProxy(false, true).setRefreshingLabel(
				"�׼�����Ŭ������ing���I(^��^)�J");
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
				ExtendsIntent intent = new ExtendsIntent(BBSListView.this,
						BBSPostDetailView.class, bbsList.get(position - 1)
								.getEssayId(), null, null, 1);
				Log.i(tag, "���ӱ����111");
				Log.i(tag, bbsList.get(position - 1).getEssayId());
				startActivity(intent);
				Log.i(tag, "queue.size:" + BaseActivity.queue.size());
				Log.i(tag, "���ӱ����2");
			}

		});
		Log.i(tag, "����initView");

	}
	
	/**
	 * @param response2 
	 * 
	 */
	private void saveData(String response2) {
		FileOutputStream out = null;
		BufferedWriter writer = null;
		try {
			out = openFileOutput("data", Context.MODE_PRIVATE);
			writer = new BufferedWriter(new OutputStreamWriter(out));
			writer.write(response2);
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(writer != null) {
					writer.close();
				}
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ��ʼ������
	 * 
	 * @throws JSONException
	 */
	private void initData() throws JSONException {

		Log.i(tag, "initData");
		loadData();
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
				bBSConnectNet = new BBSConnectNet(tip, id, bbsNums,
						relativePath, cookie);
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
	 * �첽��������
	 */
	private class GetDataTask extends AsyncTask<Void, Void, String> {

		public GetDataTask() {
		}

		protected String doInBackground(Void... params) {
			try {
				isNeedCache = false;
				// ��������
				loadData();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String result) {
			// ˢ���б�
			bbsWait.setVisibility(View.GONE);
			bbsAdapter.notifyDataSetChanged();
			plv.onRefreshComplete();
			Message msg = new Message();
			msg.what = Config.MAINVIEW_UPDATEUI;
			// if(tip=="first"&&first==1) {
			// BBSMainView.getHandler().sendMessage(msg);
			// first++;
			// LogUtil.d(first+"");
			// }
			// Log.i(tag, "bbsAdapter.notifyDataSetChanged();_______3");
		}
	}

	/**
	 * �첽��������
	 */
	private class GetPhotoTask extends AsyncTask<Void, Void, String> {

		public GetPhotoTask() {
		}

		protected String doInBackground(Void... params) {
			loadPhoto();
			return null;
		}

		protected void onPostExecute(String result) {
			// ˢ���б�
			bbsAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * �첽���µļ�������
	 * 
	 * @param page2
	 * @throws JSONException
	 */
	
	public void loadData() throws JSONException {
		
		Log.d(tag,"loadData");
		
		if(bbsNums == 0){
			//�Ȱ��б����
			Log.d(tag,"bbsList.clear()");
			bbsList.clear();
		}
		//������������������ݣ����ж��Ƿ�������
		if(tip.equals("first")&&isNeedCache) {
			
			LogUtil.d("BBSMainView������BBSListView�����ж��Ƿ�������");
			String cacheDate = judgeDataCache();
			if(!TextUtils.isEmpty(cacheDate)) {
				LogUtil.d("BBSMainView������BBSListView��������");
				response = cacheDate;
			}else {
				response = getResponseFromNet();
				Log.d(tag,"loadData+1");
				//������������������ݣ�����������ݱ�������
				saveData(response);
			}
		}else {
			LogUtil.d("����BBSMainView������BBSListView");
			response = getResponseFromNet();
			Log.d(tag,"loadData+1");
		}
		
		Data data = new Gson().fromJson(response,Data.class);
		LogUtil.p(tag, "gsonû����");
		//���жϣ��ٽ��м���
		switch(data.getTip()) {
		case "û�н��":
			Message msg = new Message();
			msg.what=0101;
			handler.sendMessage(msg);
			Log.d(tag,"data.getTip():"+data.getTip());
//			MyToast toast=MyToast.makeText(getApplicationContext(), data.getTip(), Toast.LENGTH_SHORT);
//			toast.show();
			break;
		default:
			bbsSearchGone.setVisibility(View.GONE);
			addData(data);
		}
		Log.d(tag,"loadData+3");
		
		/*//��ȡ��Ƭ��
		for(BBS bbs: bbsList){
			bbs.setHeadImage(new ReceivePhoto(bbs.getHeadImagePath()).getPhoto());
			//��ȡ��ǩ���ͱ�ǩ��ɫ
			bbs.setLabName0(bbs.getLabName().split("\\*#"));
			String[] colors = bbs.getLabColor().split("\\*");
			int[] color = new int[colors.length];
			for(int i = 0 ; i < colors.length; i++){
				color[i] = Color.parseColor(colors[i]);
			}
			bbs.setLabColor0(color);
		}*/
		Log.d(tag,"loadData����");
	}

	/**
	 * @return 
	 * 
	 */
	private String judgeDataCache() {
		FileInputStream in = null;
		BufferedReader reader = null;
		StringBuilder content = new StringBuilder();
		try {
			in = openFileInput("data");
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while((line = reader.readLine())!= null) {
				content.append(line);
			}
			if(content.toString()!=null) {
				LogUtil.d("�˴��������ݣ�����Ϊ��");
				LogUtil.d(content.toString());
			}
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			if(reader != null) {
				try {
					reader.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		return content.toString();
	}
	
	/**
	 * ��ȡ��Ƭ��
	 */
	public void loadPhoto() {
		int count=1;
		for(BBS bbs: bbsList){
			Log.i(tag, "��"+count+++"����¼");
			bbs.setHeadImage(new ReceivePhoto(bbs.getHeadImagePath()).getPhoto());
			LogUtil.p("bbs.getHeadImagePath()",bbs.getHeadImagePath());
			//��ȡ��ǩ���ͱ�ǩ��ɫ
//			Log.i(tag, "��ȡ��ǩ��---"+bbs.getLabName());
			bbs.setLabName0(bbs.getLabName().split("\\*#"));
//			Log.i(tag, "��ȡ��ǩ��ɫ---"+bbs.getLabColor());
			String[] colors = bbs.getLabColor().split("\\*");
			int[] color = new int[colors.length];
			for(int i = 0 ; i < colors.length; i++){
				color[i] = Color.parseColor(colors[i]);
			}
			bbs.setLabColor0(color);
//			Log.i(tag, "��ȡ��ǩ���ͱ�ǩ��ɫ---333");
		}
	}
	
	/**
	 * �������� ��BBSFirst�е�BBS�б���ӵ�BBS bean���У�����ȡͼƬ
	 * 
	 * @param dataFromJson
	 *            : BBSFirst��ʵ����
	 */
	private void addData(Data data) {

		Log.d(tag,"addData");
		
		//��BBSFirst�е�BBS�б���ӵ�BBS bean����
		for(BBS bbs :data.getBbs()){
			if(!bbsList.contains(bbs)){
				bbsList.add(bbs);
				bbsNums++;
			}
		}

		Log.i(tag, "bbsList.size()"+bbsList.size());
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			BBSListView.this.onBackPressed();
			break;
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

	/*
	 * @Override protected void onDestroy() { // TODO Auto-generated method stub
	 * if(bbsList.size()>0){ for(int i=0;i<bbsList.size();i++) {
	 * bbsList.get(i).getHeadImage().setCallback(null);
	 * 
	 * } }
	 * 
	 * }
	 */
	// Runnable httpLogin = new Runnable() {
	//
	// @Override
	// public void run() {
	//
	// setCookie();
	// }
	//
	// };
	//
	// public void setCookie() {
	// Log.i(tag, "setCookie--username:" + user.getUserID());
	// sendLoginInfo = new SendLoginInfo(user.getUserID(), null);
	// try {
	// respCode = sendLoginInfo.checkLoginInfo();
	// Log.i(tag, "��̳����respCode:" + respCode);
	// if (respCode.equals("success"))// ������û�����Ϊ��,���ҵ�¼�ɹ�
	// {
	// // handler.sendEmptyMessage(2);// ��ת����̳������
	// Log.d("setCookie()", sendLoginInfo.getCookie());
	// SharedPreferences.Editor editor1 = getSharedPreferences("data",
	// Context.MODE_PRIVATE).edit();
	// editor1.putString("cookie", sendLoginInfo.getCookie());
	// editor1.commit();
	//
	// } else {
	// // �û����������������
	// handler.sendEmptyMessage(1);
	// }
	// } catch (InterruptedException e) {
	// handler.sendEmptyMessage(4);// ����������Ӧ
	// }
	// }

	/*// �����β��˳�����
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.i(tag, "����ϵͳ�Դ��ķ��ؼ�");
			LogUtil.d("tip:"+tip);
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Object mHelperUtils;
				Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();

			} else {

				finish();
				con.shutDownSocketChannel();
				Log.i(tag, "�ر���socketchannel");
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	*/
	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub
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
}
