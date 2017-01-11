package com.atm.chatonline.usermsg.ui;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.afinal.simplecache.ACache;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.usermsg.adapter.SystemMsgAdapter;
import com.atm.chatonline.usermsg.adapter.SystemMsgAdapter.ViewHolder;
import com.atm.chatonline.usermsg.bean.ApplyMessage;
import com.atm.chatonline.usermsg.bean.Notification;
import com.atm.chatonline.usermsg.bean.NotificationContent;
import com.atm.chatonline.usermsg.bean.SystemMessageData;
import com.atm.chatonline.usermsg.util.CacheUtils;
import com.example.studentsystem01.R;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
/**
 * �ҵ���Ϣ����ϵͳ֪ͨ����
 */

public class SystemMsg extends BaseActivity implements OnClickListener{

	private PullToRefreshListView plv;
	private List<Notification> list;
	private String userId;
	private SystemMsgAdapter adapter;
	private ProgressBar pro;
	private boolean hasCache=false;
	private String tag="Applymsg";
	private TextView applymsg_hint;
	private ACache mCache;
	private SystemMessageData systemMessageData;
	private TextView delete;//ɾ����һ��ʼ������ʾ
	private boolean isShowCheckBox=false;
	private Map<Integer,Integer> map=new HashMap<Integer,Integer>();
	//private SparseIntArray arr=new SparseIntArray();//��SparseIntArray�滻Map
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usermsg_systemmsg_view);
		initView();
		//testAddData();
		initData();
	}
	
	/**
	 * ��ʼ�����棬��ȡuserid���Ա���Ϊ������ʵĲ���
	 */
	private void initData() {
		mCache=ACache.get(getApplicationContext());
		userId=BaseActivity.getSelf().getUserID();
		initCache();
		//��ȡϵͳ��Ϣ
		new Thread(myMsgRunnable).start();
	}

	/**
	 * ��ʼ�����
	 */
	private void initView(){
		Button btn=(Button) findViewById(R.id.btn_back);
		pro=(ProgressBar) findViewById(R.id.systemmsg_probar);
		applymsg_hint=(TextView) findViewById(R.id.systemmsg_hint);
		delete=(TextView) findViewById(R.id.delete_or_not);
		plv=(PullToRefreshListView)findViewById(R.id.systemmsg_list_view);
		
		btn.setOnClickListener(this);
		delete.setOnClickListener(this);
	}
	@SuppressWarnings({ "deprecation", "static-access" })
	private void initCache() {
		systemMessageData=getCacheData();
		if(systemMessageData!=null){
			//���list�Ĵ�С����0������ʾ����
			list=systemMessageData.getMessage();
			LogUtil.i("list size="+list.size());
			hasCache=true;
			initAdapter();
		}
	}
	
//	//���Է�������ʱ����ɾ��
//	public void testAddData(){
//		if(list==null){
//			list=new ArrayList<Notification>();
//			list.add(new Notification(1,1,"131544228",new NotificationContent("title", "bjbhjkbhjc", "78-90")));
//			
//		}
//		initAdapter();
//	}
	private void initAdapter() {
		
		adapter=new SystemMsgAdapter(getApplicationContext(), R.layout.usermsg_systemmsg_listview_item, list,isShowCheckBox);
		plv.setAdapter(adapter);
		
		//ˢ�£������Ƿ�������Ϣ������ Config.ishaveNewMessage
		plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

				@Override
				public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {	
					//��ȡ������Ϣ
					new Thread(myMsgRunnable).start();
				}

				@Override
				public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
					//��ȡ������Ϣ
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
				plv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
							LogUtil.i("�û������item");
							if(!isShowCheckBox){
								LogUtil.i("tiaozhuan");
								Intent intent=new Intent(SystemMsg.this,SystemMessageDetail.class);
								Bundle bundle=new Bundle();
								bundle.putSerializable("SystemMessage", list.get(position).getContent());
								intent.putExtras(bundle);
								startActivity(intent);
								
							}else{
								LogUtil.i("�ж�checkbox �Ƿ�ѡ��");
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

	private void isShowCheckBox(boolean isShow){
		adapter.setShowCheckBox(isShow);
		adapter.notifyDataSetChanged();
	} 
	
	/**
	 * ��ȡ����
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private SystemMessageData getCacheData(){
		return (SystemMessageData) mCache.getAsObject(CacheUtils.SYSTEM_MSG);
	}
	
	/**
	 * ���滺��
	 * @param data
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private void addCacheData(SystemMessageData data){
		mCache.put(CacheUtils.SYSTEM_MSG, data);
	}
	
	@Override
	public void processMessage(Message msg) {
		
		Bundle bundle =msg.getData();
		String json=bundle.getString("MyMessage");
		
		LogUtil.i(json);
		JSONObject jsonObject=null;
		try {
			jsonObject = new JSONObject(json);
			JSONArray jsonArr=(JSONArray) jsonObject.get("message");
			plv.onRefreshComplete();//������û������Ϣ����Ҫ����ˢ��
			if(jsonArr.length()>0){//������Ϣ
				
				if (systemMessageData==null) {
					systemMessageData=new Gson().fromJson(json, SystemMessageData.class);
					List<Notification> temp=systemMessageData.getMessage();
					if(list==null){
						list=new ArrayList<Notification>();
					}
					for(int i=temp.size()-1;i>=0;i--){
						list.add(temp.get(i));
					}
					systemMessageData.setMessage(list);
				}else{
					SystemMessageData temp=new Gson().fromJson(json, SystemMessageData.class);
					if(list!=null){
						for(int i=0;i<temp.getMessage().size();i++){
							if(!list.contains(temp.getMessage().get(i))){
								list.add(0, temp.getMessage().get(i));
							}
							
						}
					}
					
					systemMessageData.setMessage(list);
				}
				//���滺��
				addCacheData(systemMessageData);
				
				//����л�����������ݣ����򴴽�����������
				if(hasCache){
					adapter.notifyDataSetChanged();
				}else{
					initAdapter();
					hasCache=true;
				}
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
	
	Runnable myMsgRunnable = new Runnable(){

		@Override
		public void run() {
			
			LogUtil.i("��ȡϵͳ��Ϣ");
			ApplyMsg.con.reqMyMsg(userId,2);	
		}
		
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.delete_or_not:
			LogUtil.i("indexOfItem �ĳ���="+map.size());
			Stack<Notification> temp=new Stack<Notification>();
			for (Map.Entry<Integer, Integer> m : map.entrySet()) {
				LogUtil.i("indexOfItem ��position="+m.getValue());
				temp.push(list.get(m.getValue()-1));
			}
			
			while(!temp.isEmpty()){
				Notification obj=temp.pop();
				list.remove(obj);
				systemMessageData.getMessage().remove(obj);
			}
			isShowCheckBox=false;
			map.clear();
			addCacheData(systemMessageData);
			adapter.setShowCheckBox(isShowCheckBox);//ȡ����ʾcheckbox
			adapter.notifyDataSetChanged();//��������
			delete.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode==KeyEvent.KEYCODE_BACK){
			
			if(isShowCheckBox){
				LogUtil.i("isshow ="+isShowCheckBox);
				isShowCheckBox=false;
				adapter.setShowCheckBox(false);
				adapter.notifyDataSetChanged();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
