package com.atm.chatonline.usercenter.activity.usercenter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.atm.chatonline.bbs.bean.BBS;
import com.atm.chatonline.bbs.activity.bbs.BBSPostDetailView;
import com.atm.chatonline.bbs.adapter.BBSAdapter;
import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.bbs.util.ExtendsIntent;
import com.atm.chatonline.bbs.util.ReceivePhoto;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.usercenter.bean.MyLabelOfBBS;
import com.atm.chatonline.usercenter.util.HandleLabel;
import com.example.studentsystem01.R;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class LabelOfBBS extends BaseActivity implements OnClickListener{
	private String userTag,cookie,tag="LabelOfBBS",response,relativePath="essay_tagEssay.action";
	//private ProgressBar probar;
	private BBSAdapter bbsAdapter;
	private PullToRefreshListView plv;
	private Button btnBack;
	private int bbsNums=0;
	private TextView failedTextView,textview_wait;
	private List<BBS> bbsList= new ArrayList<BBS>();
	private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_center_labelofbbs_main_view);
		initParams();
		initView();
		initData();
		
	}

	public void initParams(){
		Intent intent=getIntent();
		cookie=intent.getStringExtra("cookie");
		userTag=intent.getStringExtra("userTag");
		Log.i(tag, "userTag:"+userTag+",  cookie:"+cookie);
	}
	private void initData() {
		Log.i(tag, "initData");
		try {
			loadData();
		} catch (JSONException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
	
	public void initView(){
		//probar=(ProgressBar)findViewById(R.id.label_of_bbs_pro);
		TextView title=(TextView)findViewById(R.id.labelofbbs_title);
		textview_wait=(TextView)findViewById(R.id.textview_wait);
		btnBack=(Button)findViewById(R.id.labelofbbs_btn_back);
		failedTextView=(TextView)findViewById(R.id.load_failed_textview);
		plv=(PullToRefreshListView)findViewById(R.id.user_center_labelofbbs_lv);
		
		bbsAdapter = new BBSAdapter(this, R.layout.bbs_list_item, bbsList);
		plv.setAdapter(bbsAdapter);
		plv.setVisibility(View.VISIBLE);
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
				ExtendsIntent intent = new ExtendsIntent(LabelOfBBS.this,
						BBSPostDetailView.class, bbsList.get(position - 1)
								.getEssayId(), null, null, 1);
				Log.i(tag, "���ӱ����111");
				Log.i(tag, bbsList.get(position - 1).getEssayId());
				startActivity(intent);
				Log.i(tag, "queue.size:" + BaseActivity.queue.size());
				Log.i(tag, "���ӱ����2");
			}

		});
		btnBack.setOnClickListener(this);
		title.setText(userTag);
	}

	
	/**
	 * �첽��������
	 */
	private class GetDataTask extends AsyncTask<Void, Void, String> {

		public GetDataTask() {
			Log.i(tag, "+++++++first");
		}

		protected String doInBackground(Void... params) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Thread thread = new Thread(new Runnable() {
				public void run() {
					try {
						// ��������
						loadData();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
			thread.start();
			try {
				thread.join();
			} catch (InterruptedException e) {
				Log.d(tag, "GetDataTask-�̱߳����");
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String result) {
			// ˢ���б�
			//probar.setVisibility(View.GONE);
			plv.onRefreshComplete();
			bbsAdapter.notifyDataSetChanged();
			Log.i(tag, "bbsAdapter.notifyDataSetChanged();_______3");
		}
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
				HandleLabel handleLabel=new HandleLabel();
				Log.i(tag, "label:"+userTag);
				handleLabel.startLoadBBS(UriAPI.SUB_URL+relativePath, cookie, userTag, bbsNums);
				response=handleLabel.getResponse();
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
		Log.i(tag, "loadData-response:" + response);
		MyLabelOfBBS labelofbbs=new Gson().fromJson(response,MyLabelOfBBS.class);
		String tip=labelofbbs.getTip();
		if(tip.equals("success")){
			/*if(probar.getVisibility()==View.VISIBLE){
				probar.setVisibility(View.GONE);
			}*/
			if(textview_wait.getVisibility()==View.VISIBLE){
				textview_wait.setVisibility(View.GONE);
			}
		}else{
			/*if(probar.getVisibility()==View.VISIBLE){
				probar.setVisibility(View.GONE);
			}*/
			if(textview_wait.getVisibility()==View.VISIBLE){
				textview_wait.setVisibility(View.GONE);
			}
			if(failedTextView.getVisibility()==View.GONE){
				failedTextView.setVisibility(View.VISIBLE);
			}
		}
		addData(labelofbbs);
		Log.i(tag,"�Ѿ���ʼ��");
		//handler.sendEmptyMessage(1);
	}

	/**
	 * �������� ��BBSFirst�е�BBS�б���ӵ�BBS bean���У�����ȡͼƬ
	 * 
	 * @param dataFromJson
	 *            : BBSFirst��ʵ����
	 */
	private void addData(MyLabelOfBBS data ) {

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
			// Log.i(tag, "��ȡ��ǩ��---"+bbs.getLabName());
			bbs.setLabName0(bbs.getLabName().split("\\*#"));
			// Log.i(tag, "��ȡ��ǩ��ɫ---"+bbs.getLabColor());
			String[] colors = bbs.getLabColor().split("\\*");
			int[] color = new int[colors.length];
			for (int i = 0; i < colors.length; i++) {
				color[i] = Color.parseColor(colors[i]);
			}
			bbs.setLabColor0(color);
			// Log.i(tag, "��ȡ��ǩ���ͱ�ǩ��ɫ---333");
		}
	}
	
	
	@Override
	public void processMessage(Message msg) {
	

	}

	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch(id){
		case R.id.labelofbbs_btn_back:
			this.onBackPressed();
			break;
		}
		
	}

}
