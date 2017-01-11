package com.atm.chatonline.usermsg.ui;
/**
 *
 * �ҵ���Ϣ�������ۣ������ظ����ҵĽ���
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.afinal.simplecache.ACache;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atm.chatonline.bbs.activity.bbs.BBSPostDetailView;
import com.atm.chatonline.bbs.util.ExtendsIntent;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.usermsg.adapter.ApplyAdapter;
import com.atm.chatonline.usermsg.adapter.ApplyAdapter.ViewHolder;
import com.atm.chatonline.usermsg.bean.ApplyMessage;
import com.atm.chatonline.usermsg.bean.ApplyMessageData;
import com.atm.chatonline.usermsg.util.CacheUtils;
import com.atm.chatonline.usermsg.util.MyMsgReceivePhoto;
import com.example.studentsystem01.R;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class ApplyMsg extends BaseActivity implements OnClickListener{

	private List<ApplyMessage> list;
	private String userId;
	private ApplyAdapter adapter;
	private PullToRefreshListView plv;
	private ProgressBar pro;
	private ACache mCache;
	private boolean hasCache=false;
	private String tag="Applymsg";
	private TextView applymsg_hint;
	private ApplyMessageData data;//������������Ϣ���б�
	private TextView delete;//ɾ����һ��ʼ������ʾ
	private boolean isShowCheckBox=false;
	private Map<Integer,Integer> map=new HashMap<Integer,Integer>();
	//private SparseIntArray arr=new SparseIntArray();//��SparseIntArray�滻Map
	//private Integer type=0;//0--���ۣ�1--@�ң�2--ϵͳ��Ϣ
	
	//���ͻ�ȡ�ҵ�������Ϣ���������,Ϊ��֮�������ΪActivity���ٺ����߳�û�б����٣����԰�����߳�д��ǰ��
	Runnable myMsgRunnable=new Runnable() {
			
			@Override
			public void run() {
				Log.i(tag, "��ȡ������Ϣ");
				ApplyMsg.con.reqMyMsg(userId,1);	
			}
	};
	

	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usermsg_applymsg_view);
		Log.e("www", "apply");
		initUI();
		initData();
	}
	
	private void initData() {
		
		mCache=ACache.get(getApplicationContext());
		//���ﻹû���ж�con�Ƿ���ڣ������Ѿ�����
		//��ȡuserId
		userId=BaseActivity.getSelf().getUserID();
		//�������󵽷����,������Ӧ���Ȳ�ѯ���ݻ��棬��������ȼ��ػ��棬��ʾ��Ȼ���ٻ�ȡ�µ���Ϣ��ˢ�£����û�л�������ʾ����ˢϴ��������Ȼ���ټ������ص�����
		initCache();
		//��ȡ������Ϣ
		new Thread(myMsgRunnable).start();	
	}

	/**
	 * ��ȡ����������ü����ȳ�ʼ������
	 */
	private void initUI() {
		Button btn=(Button) findViewById(R.id.btn_back);
		pro=(ProgressBar) findViewById(R.id.applymsg_probar);
		applymsg_hint=(TextView) findViewById(R.id.applymsg_hint);
		delete=(TextView) findViewById(R.id.delete_or_not);
		btn.setOnClickListener(this);
		delete.setOnClickListener(this);

	}

	/**
	 * ��ȡ����
	 */
	@SuppressWarnings({ "deprecation", "static-access" })
	private void initCache() {
		data=getCacheData();
		
		if(data!=null){
			//���list�Ĵ�С����0������ʾ����
			list=data.getApplyMessage();
			LogUtil.i("list size="+list.size());
			for(ApplyMessage applyMeesage:list){
				applyMeesage.getContent().setHeadImage(mCache.getAsBitmap(applyMeesage.getContent().getUserId())
						);
			}
			hasCache=true;
			initAdapter();
		}else{
			Log.i("applyMsg", "[message cache  ]:null");
		}
		
		
	}

	private void initAdapter() {
		
		plv=(PullToRefreshListView)findViewById(R.id.apply_msg_list_view);
		adapter=new ApplyAdapter(getApplicationContext(), R.layout.apply_msg_listview_item, list,isShowCheckBox); 
		plv.setAdapter(adapter);
		
		//ˢ�£������Ƿ�������Ϣ������ Config.ishaveNewMessage
		plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				new Thread(myMsgRunnable).start();	
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				new Thread(myMsgRunnable).start();		
			}
		});
		
		// ����PullRefreshListView�������ʱ�ļ�����ʾ
		plv.getLoadingLayoutProxy(false, true).setPullLabel(
						"��������...");
		plv.getLoadingLayoutProxy(false, true).setRefreshingLabel(
						"���ڼ���...");
		plv.getLoadingLayoutProxy(false, true).setReleaseLabel(
						"�ͷż���...");
				// ����PullRefreshListView��������ʱ�ļ�����ʾ
		plv.getLoadingLayoutProxy(true, false).setPullLabel(
						"��������...");
		plv.getLoadingLayoutProxy(true, false).setRefreshingLabel(
				        "���ڼ���...");
		plv.getLoadingLayoutProxy(true, false).setReleaseLabel(
						"�ͷż���...");
		
		//Ϊitem��Ӽ���
		plv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(!isShowCheckBox){
					ExtendsIntent intent = new ExtendsIntent(ApplyMsg.this,
							BBSPostDetailView.class, list.get(position - 1)
									.getContent().getEssayId(), null, null, 1);
					startActivity(intent);
				}else{
					ViewHolder viewHolder=(ViewHolder) view.getTag();
					viewHolder.checkBox.toggle();
					if(viewHolder.checkBox.isChecked()){
						if(!map.containsKey(position)){
							map.put(position, position);
						}
					}else{
						if(map.containsKey(position)){
							map.remove(position);
						}
					}
				}
			}
		});
		plv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0,
					View arg1, int arg2, long arg3) {
				// ��ʾcheckbox����ˢ��UI����ʾɾ����Textview
				if(!isShowCheckBox){
					isShowCheckBox=true;
					isShowCheckBox(isShowCheckBox);
				}
				delete.setVisibility(View.VISIBLE);
				return true;
			}
			
		});
		
		pro.setVisibility(View.GONE);
		
	}
	
	protected void isShowCheckBox(boolean isShow) {
		adapter.setShowCheckBox(isShow);
		adapter.notifyDataSetChanged();
		
	}

	/**
	 * ��ȡ����
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ApplyMessageData getCacheData(){
		return (ApplyMessageData) mCache.getAsObject(CacheUtils.APPLY_MSG);
//		CacheData  data =cacheManager.getCache(CacheUtils.APPLY_MSG);
//		if(data==null){
//			return null;
//		}
//		return (List<ApplyMessage>) data.getData();
	}
	
	/**
	 * ���滺��
	 * @param data
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private void addCacheData(ApplyMessageData data){
		mCache.put(CacheUtils.APPLY_MSG, data);
//		CacheData cacheData=new CacheData(CacheUtils.APPLY_MSG, data);
//		cacheManager.addCache(cacheData);
	}
	
	/**
	 * �ȴ���̨������Ϣ��������������Ϣ��List���������������Ϣ��ʱ��
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void processMessage(Message msg) {
		Bundle bundle =msg.getData();
		String json=bundle.getString("MyMessage");
		JSONObject jsonObject=null;
		try {
			jsonObject = new JSONObject(json);
			JSONArray jsonArr=(JSONArray) jsonObject.get("message");
			
			if(jsonArr.length()>0){//������Ϣ
				
				//����Ѿ��л��棬�������Ϣ��ӵ����棬������ֱ��ֱ��ת��Ϊdata
				
				if(data==null){
					data=new Gson().fromJson(json, ApplyMessageData.class);
					
					//��Ϣ��ʱ����������ȷ������ģ�����Ҫ��������
					List<ApplyMessage> temp=(List<ApplyMessage>)data.getApplyMessage();
					if(list==null){
						list=new ArrayList<ApplyMessage>();
					}
					for(int i=temp.size()-1;i>=0;i--){
						list.add(temp.get(i));
					}
					data.setApplyMessage(list);
				}else{//����Ϣ��ӵ�����
					ApplyMessageData temp=new Gson().fromJson(json, ApplyMessageData.class);
					
					if(list!=null){
						for(int i=0;i<temp.getApplyMessage().size();i++){
							if(!list.contains(temp.getApplyMessage().get(i))){
								list.add(0, temp.getApplyMessage().get(i));
							}
							
						}
					}
					
					data.setApplyMessage(list);
				}
				new GetPhotoTask().execute();
				applymsg_hint.setVisibility(View.GONE);
			}else{
				if(!hasCache){
					applymsg_hint.setVisibility(View.VISIBLE);
				}else{
					applymsg_hint.setVisibility(View.GONE);
				}
				pro.setVisibility(View.GONE);
			}
			
		} catch (JSONException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
			
	}
	
	
	/**
	 * �첽����ͷ��
	 */
	@SuppressWarnings("unused")
	private class GetPhotoTask extends AsyncTask<Void, Void, String> {

		public GetPhotoTask() {
		}

		protected String doInBackground(Void... params) {
			loadPhoto();
			return null;
		}

		

		protected void onPostExecute(String result) {
			//���滺��
			addCacheData(data);
			LogUtil.i("111111111111");
			//����л�����������ݣ����򴴽�����������
			if(hasCache){
				adapter.notifyDataSetChanged();
			}else{
				initAdapter();
				hasCache=true;
			}
			plv.onRefreshComplete();
		}
	}

	//����ͷ��ͼƬ��˳�����ͷ��
	public void loadPhoto(){
		MyMsgReceivePhoto recPho=new MyMsgReceivePhoto();
		for(ApplyMessage applyMeesage:list){
			applyMeesage.getContent().setHeadImage(recPho.getPhoto(mCache, applyMeesage.getContent().getUserId(), applyMeesage.getContent().getAvatar()));
		}
		//����Ϊnull,���ⳤʱ��ռ���ڴ棬����oom�Ĳ�������
		recPho=null;
		LogUtil.i("111111111111^^^^^^^^^^^^^");
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			LogUtil.i("click back button!");
			finish();
			break;
		case R.id.delete_or_not:
			LogUtil.i("indexOfItem �ĳ���="+map.size());
			Stack<ApplyMessage> temp=new Stack<ApplyMessage>();
			for (Map.Entry<Integer, Integer> m : map.entrySet()) {
				LogUtil.i("indexOfItem ��position="+m.getValue());
				temp.push(list.get(m.getValue()-1));
			}
			
			while(!temp.isEmpty()){
				ApplyMessage obj=temp.pop();
				list.remove(obj);
				data.getApplyMessage().remove(obj);
			}
			addCacheData(data);
			map.clear();
			isShowCheckBox=false;
			adapter.setShowCheckBox(isShowCheckBox);//ȡ����ʾcheckbox
			LogUtil.i("list size="+list.size());
			adapter.notifyDataSetChanged();//��������
			delete.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}
		
}
