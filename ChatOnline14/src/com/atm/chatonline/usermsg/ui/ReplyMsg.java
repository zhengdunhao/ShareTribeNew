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
import com.atm.chatonline.usermsg.adapter.ReplyAdapter.ViewHolder;
import com.atm.chatonline.usermsg.adapter.ReplyAdapter;
import com.atm.chatonline.usermsg.bean.ApplyMessage;
import com.atm.chatonline.usermsg.bean.ReplyMessage;
import com.atm.chatonline.usermsg.bean.ReplyMessageData;
import com.atm.chatonline.usermsg.util.CacheUtils;
import com.atm.chatonline.usermsg.util.MyMsgReceivePhoto;
import com.example.studentsystem01.R;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
/**
 *  �ҵ���Ϣ�����@�ҵĽ���
 */

public class ReplyMsg extends BaseActivity implements OnClickListener{

	//private View mView;
	private ProgressBar pro;
	private String userId;
	private boolean hasCache=false;
	private String tag="Replymsg";
	private PullToRefreshListView plv; 
	private ReplyAdapter adapter;
	private TextView replymsg_hint;
	private ReplyMessageData data;
	private ACache mCache;
	private List<ReplyMessage> list;
	private boolean isShowCheckBox=false;
	private Map<Integer,Integer> map=new HashMap<Integer,Integer>();
	private TextView delete;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usermsg_replymsg_view);
		Log.e("www", "reply");
		initUI();
		initData();
	
	}
	private void initData() {
		mCache=ACache.get(getApplicationContext());
		userId=BaseActivity.getSelf().getUserID();
		initCache();
		//��ȡ������Ϣ
		new Thread(myMsgRunnable).start();
		
		
	}
	private void initUI() {
		Button btn=(Button) findViewById(R.id.btn_back);
		pro=(ProgressBar) findViewById(R.id.replymsg_probar);
		replymsg_hint=(TextView) findViewById(R.id.replymsg_hint);
		delete=(TextView)findViewById(R.id.delete_or_not);
		btn.setOnClickListener(this);
		delete.setOnClickListener(this);
		
	}
	@SuppressWarnings({ "deprecation", "static-access" })
	private void initCache() {
//		cacheManager=CacheManager.getInstance();
//		cacheManager.init(getApplicationContext());
		data=getCacheData();
		//list=getCacheData();
	
		if(data!=null){
			//���list�Ĵ�С����0������ʾ����
			list=data.getReplyMessage();
			LogUtil.i("list size="+list.size());
			for(ReplyMessage replyMeesage:list){
				replyMeesage.getContent().setHeadImage(mCache.getAsBitmap(replyMeesage.getContent().getUserId()));
			}
			hasCache=true;
			initAdapter();
		}
		
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ReplyMessageData getCacheData() {
		return (ReplyMessageData) mCache.getAsObject(CacheUtils.REPLY_MSG);

	}
	
	private void initAdapter() {
				plv=(PullToRefreshListView)findViewById(R.id.reply_msg_list_view);
				adapter=new ReplyAdapter(getApplicationContext(), R.layout.reply_msg_listview_item, list,false); 
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
							ExtendsIntent intent = new ExtendsIntent(ReplyMsg.this,
									BBSPostDetailView.class,String.valueOf(list.get(position - 1)
											.getContent().getEssayId()), null, null, 1);
							startActivity(intent);
						}else{
							ViewHolder viewHolder=(ViewHolder) view.getTag();
							viewHolder.check_box.toggle();
							if(viewHolder.check_box.isChecked()){
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

//	private void initData() {
//		list=new ArrayList<ReplyMessage>();
//		list.add(new ReplyMessage("İ���","��ҵȥ�Ķ�","2016-09-17","����@�ҵ����ݣ�������������"));
//		list.add(new ReplyMessage("������","��ҵȥ�Ķ�","2016-09-17","����@�ҵ����ݣ�������������"));
//	
//	}
	
	@Override
	public void processMessage(Message msg) {
		Bundle bundle =msg.getData();
		String json=bundle.getString("MyMessage");
		LogUtil.i(json);
		JSONObject jsonObject=null;
		try {
			jsonObject = new JSONObject(json);
			JSONArray jsonArr=(JSONArray) jsonObject.get("message");
			if(jsonArr.length()>0){//������Ϣ
				
				if(data==null){
					data=new Gson().fromJson(json, ReplyMessageData.class);
					List<ReplyMessage> temp=(List<ReplyMessage>)data.getReplyMessage();
					if(list==null){
						list=new ArrayList<ReplyMessage>();
					}
					for(int i=temp.size()-1;i>=0;i--){
						list.add(temp.get(i));
					}
					data.setReplyMessage(list);
				}else{
					ReplyMessageData temp=new Gson().fromJson(json, ReplyMessageData.class);
					
					if(list!=null){
						for(int i=0;i<temp.getReplyMessage().size();i++){
							if(!list.contains(temp.getReplyMessage().get(i))){
								list.add(0, temp.getReplyMessage().get(i));
							}
							
						}
					}
					data.setReplyMessage(list);
				}
				new GetPhotoTask().execute();
				replymsg_hint.setVisibility(View.GONE);
			}else{
				if(!hasCache){
					replymsg_hint.setVisibility(View.VISIBLE);
				}else{
					replymsg_hint.setVisibility(View.GONE);
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

	//����ͼƬ
	public void loadPhoto(){
		MyMsgReceivePhoto recPho=new MyMsgReceivePhoto();
		for(ReplyMessage replyMeesage:list){
			replyMeesage.getContent().setHeadImage(recPho.getPhoto(mCache, replyMeesage.getContent().getUserId(), replyMeesage.getContent().getAvatar()));
		}
		//����Ϊnull,���ⳤʱ��ռ���ڴ棬����oom�Ĳ�������
		recPho=null;
		LogUtil.i("111111111111^^^^^^^^^^^^^");
	}
	
	/**
	 * ���滺��
	 * @param data
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private void addCacheData(ReplyMessageData data){
		mCache.put(CacheUtils.REPLY_MSG, data);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.delete_or_not:
			LogUtil.i("indexOfItem �ĳ���="+map.size());
			Stack<ReplyMessage> temp=new Stack<ReplyMessage>();
			for (Map.Entry<Integer, Integer> m : map.entrySet()) {
				LogUtil.i("indexOfItem ��position="+m.getValue());
				temp.push(list.get(m.getValue()-1));
			}
			
			while(!temp.isEmpty()){
				ReplyMessage obj=temp.pop();
				list.remove(obj);
				data.getReplyMessage().remove(obj);
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
	
	//���ͻ�ȡ�ҵ�������Ϣ���������
	Runnable myMsgRunnable=new Runnable() {
			
			@Override
			public void run() {
				Log.i(tag, "��ȡ������Ϣ");
				ReplyMsg.con.reqMyMsg(userId,0);	
			}
	};
	
}
