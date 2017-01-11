/**
 * 
 */
package com.atm.charonline.recuit.ui;

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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.charonline.recuit.util.BBSConnectNet;
import com.atm.charonline.recuit.util.ExtendsIntent;
import com.atm.chatonline.bbs.commom.MyToast;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.util.Config;
import com.atm.chatonline.recuit.adapter.ApplyAdapter;
import com.atm.chatonline.recuit.bean.Apply;
import com.atm.chatonline.recuit.bean.Data;
import com.example.studentsystem01.R;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * @�� com.atm.charonline.recuit.ui ---RecuitListView
 * @���� ��������������Ƹ�б����ʾ
 * @���� ��С��
 * @ʱ�� 2015-9-23
 * 
 */

public class ApplyListView extends Activity {

	//���ݸ�������������Ĳ���
	private static String id;
	private static String tip = "";
	public String getTip() {
		return tip;
	}

	public static void setTip(String tip) {
		ApplyListView.tip = tip;
	}

	private String relativePath;
	
	private BBSConnectNet bBSConnectNet;
	private List<Apply> applyList = new ArrayList<Apply>();
	private ApplyAdapter applyAdapter;
	private PullToRefreshListView plv;
	private int applyNums = 0;
	private String response;
	private String cookie;
	private TextView recuit_wait;
	private static Handler handler;
	
	private boolean isNeedCache = true;
	
	public static Handler getHandler() {
		return handler;
	}

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recuit_list_view);
		LogUtil.p("class","recuitListView");
		initParams();
		initView();
		recuit_wait.setVisibility(View.GONE);
//		new GetDataTask().execute();
		try {
			initData();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		handler = new Handler(){
			public void handleMessage(Message msg){
				switch(msg.what){
					case Config.FULLTIME:
						applyNums = 0;
						tip="ȫְ";
						new GetDataTask().execute();
//						try {
//							initData();
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						applyAdapter.notifyDataSetChanged();
						break;
					case Config.ALLKIND:
						applyNums = 0;
						tip="ȫ��";
						new GetDataTask().execute();
//						try {
//							initData();
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						applyAdapter.notifyDataSetChanged();
						break;
					case Config.PARTTIME:
						applyNums = 0;
						tip="��ְ";
						new GetDataTask().execute();
//						try {
//							initData();
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						applyAdapter.notifyDataSetChanged();
						break;
					case Config.INTERNSHIP:
						applyNums = 0;
						tip="ʵϰ";
						new GetDataTask().execute();
//						try {
//							initData();
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						applyAdapter.notifyDataSetChanged();
						break;
					case Config.SEARCHESSAY:
						applyNums = 0;
						new GetDataTask().execute();
						break;
				}
			}
		};
		
	}
	
	/**
	 * ��ʼ��������
	 * ��ȡ��Bundle����Ĳ�����
	 * �ò��������ڴ��ݸ�������������Ĳ�����
	 * �ĸ�������id;tip;relativePath
	 */
	private void initParams() {
		
		LogUtil.d("initParams");
		
		Bundle bundle = this.getIntent().getExtras();
		this.relativePath = bundle.getString("relativePath");
		ApplyListView.id = bundle.getString("id");
		ApplyListView.tip = bundle.getString("tip");

		SharedPreferences pref = getSharedPreferences("data",Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");
		Log.d("initParams()cookie",cookie);
	}

	/**
	 * ��ʼ������
	 */
	private void initView() {
		
		LogUtil.d("initView");

        recuit_wait = (TextView) findViewById(R.id.recuit_wait);
        Spinner sp = (Spinner) findViewById(R.id.sp_workType);

        // ʵ����PullToRefreshListView��������������
		plv = (PullToRefreshListView) findViewById(R.id.recuit_lv_home);
		applyAdapter = new ApplyAdapter(this,R.layout.recuit_apply_list_item,applyList);
		plv.setAdapter(applyAdapter);
//		ListViewUtil.setListViewHeightBasedOnChildren(plv , applyAdapter);

		//Ϊ���б�����������������
		plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				applyNums = 0;
				new GetDataTask().execute();
			}
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				new GetDataTask().execute();
			}
		});
		
		//����PullRefreshListView�������ʱ�ļ�����ʾ
        plv.getLoadingLayoutProxy(false, true).setPullLabel("������������������O(��_��)O����~");
        plv.getLoadingLayoutProxy(false, true).setRefreshingLabel("�׼�����Ŭ������ing���I(^��^)�J");
        plv.getLoadingLayoutProxy(false, true).setReleaseLabel("�ɿ������Ҿͼ��أ�(*^__^*) ��������");
        // ����PullRefreshListView��������ʱ�ļ�����ʾ
        plv.getLoadingLayoutProxy(true, false).setPullLabel("������������������O(��_��)O����~");
        plv.getLoadingLayoutProxy(true, false).setRefreshingLabel("�׼�����Ŭ��ˢ��ing���I(^��^)�J");
        plv.getLoadingLayoutProxy(true, false).setReleaseLabel("�ɿ������Ҿ�ˢ�£�(*^__^*) ��������");
	
        plv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//����essayid�����ӡ�
				ExtendsIntent intent = new ExtendsIntent( ApplyListView.this , ApplyPostDetailView.class ,
						applyList.get(position - 1).getApInfoId(), null , null , 1);
				Log.d("essayId", applyList.get(position - 1).getApInfoId());
				startActivity(intent);
				
			}
        	
		});
        
	}

	/**
	 * ��ʼ������
	 * @throws JSONException 
	 */
	private void initData() throws JSONException {
		
		LogUtil.d("initData");
		loadData();
	}

	/**
	 * �ӷ������˻�ȡ����
	 * @return response��json������
	 */
	private String getResponseFromNet() {
		LogUtil.d("getResponseFromNet");
		Thread thread = new Thread(new Runnable(){
			public void run(){
				bBSConnectNet = new BBSConnectNet(tip , id ,applyNums,relativePath,cookie);
				Log.d("˳��","getResponseFromNet+RecuitConnectNet����");
				response = bBSConnectNet.getResponse();
				LogUtil.d(response);
				LogUtil.d("bBSConnectNet.getResponse()����");
			}
		});
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			LogUtil.p("getResponseFromNet","�̱߳����");
			e.printStackTrace();
		}
		LogUtil.p("444444","555");
		return response;
	}

	/**
	 * �첽��������
	 */
	private class GetDataTask extends AsyncTask<Void , Void , String>{
		
		
		public GetDataTask(){
		}
		
		protected String doInBackground(Void... params) {

			//��������
			try {
				isNeedCache = false;
				loadData();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	
		protected void onPostExecute(String result){
//			recuit_wait.setVisibility(View.GONE);
			//ˢ���б�
			applyAdapter.notifyDataSetChanged();
			plv.onRefreshComplete();
		}
	}

	/**
	 * �첽���µļ�������
	 * @param page2
	 * @throws JSONException 
	 */
	public void loadData() throws JSONException {
		
		LogUtil.p("˳��","loadData");
		
		if(applyNums == 0){
			//�Ȱ��б����
			applyList.clear();
		}
		if(tip!="ȫ��"&&tip!="ȫְ"&&tip!="��ְ"&&tip!="ʵϰ") {
			LogUtil.d("tip������ȫ��ȫְ��ְʵϰ");
			isNeedCache = false;
		}
		//��������
		if(isNeedCache) {
			String cacheDate = judgeDataCache();
			if(!TextUtils.isEmpty(cacheDate)) {
				LogUtil.d("������ApplyListView��������");
				response = cacheDate;
			}else {
				response = getResponseFromNet();
				//������������������ݣ�����������ݱ�������
				saveData(response);
			}
		}else {
			response = getResponseFromNet();
		}
		LogUtil.p("˳��","loadData+1");
		
		Data data = new Gson().fromJson(response,Data.class);
		if(data.getApply()==null) {

			LogUtil.p("data","data.getApply()==null");
			MyToast toast=MyToast.makeText(getApplicationContext(), data.getTip(), Toast.LENGTH_SHORT);
			toast.show();
		}else {
		switch(data.getTip()) {
		case "û�н��":
			LogUtil.p("data.getTip():",data.getTip());
			MyToast toast=MyToast.makeText(getApplicationContext(), data.getTip(), Toast.LENGTH_SHORT);
			toast.show();
			break;
		default:
			addData(data);
		}
		}
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
			in = openFileInput("applyData");
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
	 * @param response2 
	 * 
	 */
	private void saveData(String response2) {
		FileOutputStream out = null;
		BufferedWriter writer = null;
		try {
			out = openFileOutput("applyData", Context.MODE_PRIVATE);
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
	 * ��������
	 * ��RecuitFirst�е�Recuit�б���ӵ�Recuit bean���У�����ȡͼƬ
	 * @param dataFromJson : RecuitFirst��ʵ����
	 */
	private void addData(Data data) {

		LogUtil.p("˳��","addData");
		
		//��RecuitFirst�е�Recuit�б���ӵ�Recuit bean����
		for(Apply apply :data.getApply()){
			if(!applyList.contains(apply)){
				LogUtil.p("˳��","applyList.add(apply)");
				applyList.add(apply);
				applyNums++;
				LogUtil.p("˳��",applyNums+"");
			}
		}
	}

}
