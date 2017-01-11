package com.atm.chatonline.usercenter.activity.usercenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.usercenter.bean.LabelData;
import com.atm.chatonline.usercenter.util.HandleLabel;
import com.atm.chatonline.usercenter.util.ReceiveMyLabel;
import com.atm.chatonline.usercenter.view.FlowLayout;
import com.example.studentsystem01.R;
import com.google.gson.Gson;

public class MyLabel extends BaseActivity implements OnClickListener {

	/*
	 * private final String[]
	 * item={"����","������+","O2O","Apple","�������","�����ϳ�","����ʶ��","��ֱ����","��������"};
	 * 
	 * private boolean[]
	 * bool={false,false,false,false,false,false,false,false,false};
	 */
	private int[] tagId = { 12, 13, 14, 15, 16, 17, 18, 19, 20 };
	private List<String> list = new ArrayList<String>();// ��ų�ʼ���ı�ǩ
	private List<String> ischecked = new ArrayList<String>();// ��ѡ�еı�ǩ
	private List<String> hotLabel;
	private List<String> ischeckedHotLabel = new ArrayList<String>();
	private boolean isMutilchoice = false;// �ж��Ƿ��Ƕ�ѡ
	private ListView labelListView;
	private LabelAdapter adapter;
	private Context context;
	private String tag = "laebltest", cookie, subUri = "atm_hotLabel.action",
			response;
	private Button btnEdit, btnBack;
	private ProgressBar pro;
	private TextView hotLabelHint;
	private FlowLayout flowLayout;// ��ʾ���ű�ǩ
	private LinearLayout linearLayout;
	private String URI_ADDTAG = "atm_attTag.action",
			URI_CANCELTAG = "atm_cancelTag.action",
			ESSAY_TAG = "essay_tagEssay.action";
	private Handler handler;
	private HandleLabel handleLabel;
	private List<View> textViews = new ArrayList<View>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_label_listview);
		initView();
		initData();
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO �Զ����ɵķ������
				super.handleMessage(msg);
				pro.setVisibility(View.GONE);
				switch (msg.what) {
				case 1:
					// �û�û�й�ע��ǩ�����
					hotLabelHint.setVisibility(View.VISIBLE);
					initFlowLayout();
					// flowLayout.setVisibility(View.VISIBLE);
					break;
				case 2:
					// �û��й�ע��ǩ�����
					setAdapter(context);
					hotLabelHint.setVisibility(View.GONE);
					labelListView.setVisibility(View.VISIBLE);
					break;
				case 3:
					// �û���ȡ��ǩʧ�ܵ����
					hotLabelHint.setVisibility(View.GONE);
					labelListView.setVisibility(View.GONE);
					Toast.makeText(MyLabel.this, "��ȡ�û���ǩʧ��", Toast.LENGTH_SHORT)
							.show();
					break;
				case 4:
					// ����û���ע�ı�ǩ�ɹ�
					handleAddTag();
					break;
				case 5:
					// ɾ���û���ǩ�ɹ�
					handleDelete();
					break;
				}
			}
		};
	}

	/**
	 * ��ʼ��cookie�ͱ�ǩlist
	 */
	private void initData() {
		Intent intent = getIntent();
		cookie = intent.getStringExtra("cookie");
		Log.i(tag, "cookie:" + cookie);
		new Thread(runnable).start();

	}

	@SuppressLint("InflateParams")
	private void initView() {

		hotLabelHint = (TextView) findViewById(R.id.my_label_textview);
		pro = (ProgressBar) findViewById(R.id.my_label_progressbar);
		labelListView = (ListView) findViewById(R.id.my_label_listview);
		btnBack = (Button) findViewById(R.id.label_listview_btn_back);
		btnEdit = (Button) findViewById(R.id.label_listview_btn_finish);
		linearLayout = (LinearLayout) findViewById(R.id.linearlayout_of_mylabel);
		flowLayout = (FlowLayout) findViewById(R.id.flow_layout_view);
		btnEdit.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}

	// ��ʼ�����ű�ǩ
	public void initFlowLayout() {
		Log.i(tag, "�������ű�ǩ");
		LayoutInflater inflater = LayoutInflater.from(this);
		HotLabelOnClickListener m = new HotLabelOnClickListener();
		for (int i = 0; i < hotLabel.size(); i++) {
			TextView tv = (TextView) inflater.inflate(R.layout.textview_style,
					flowLayout, false);
			tv.setText(hotLabel.get(i));
			textViews.add(tv);
			tv.setOnClickListener(m);
			flowLayout.addView(tv);
		}

		flowLayout.setVisibility(View.VISIBLE);
	}

	// �������ű�ǩ��������¼�
	class HotLabelOnClickListener implements OnClickListener {
		private boolean flag = false;// �жϱ�ǩ�Ƿ����Ѿ�ѡ����б�����

		@SuppressLint("NewApi")
		@Override
		public void onClick(View v) {
			for (int i = 0; i < textViews.size(); i++) {
				if (textViews.get(i).equals(v)) {
					String label = hotLabel.get(i);
					for (int j = 0; j < ischeckedHotLabel.size(); j++) {
						if (ischeckedHotLabel.get(j).equals(label)) {
							flag = true;
							break;
						}
					}
					if (flag) {
						ischeckedHotLabel.remove(label);
						v.setBackground(getResources().getDrawable(
								R.drawable.bg_hottag));
					} else {
						ischeckedHotLabel.add(label);
						v.setBackground(getResources().getDrawable(
								R.drawable.bg_isselected_hottag));
					}

				}
			}
			// ���ñ������İ�ť�����
			if (ischeckedHotLabel.size() != 0) {
				setAddView();
			} else {
				setEditView();
			}
			flag = false;
		}
	}

	// ������ϽǱ������Ƿ����ִ��
	private void handleLabelList() {
		String text = btnEdit.getText().toString().trim();
		Log.i(tag, "text=" + text);
		if (text.equals("ɾ ��")) {
			new Thread(deleteRunnable).start();
			//handleDelete();
		} else if (text.equals("ȡ ��")) {
			cancel();
		} else if (text.equals("�� ��")) {
			Log.i(tag, "�༭�����");
			if (labelListView.getVisibility() == View.VISIBLE) {
				setView();
			}
		} else if (text.equals("�� ��")) {
			//handleAddTag();
			new Thread(addTagRunnable).start();
		}

	}

	// ��ȡ����ť�����ʱִ��
	public void cancel() {
		Log.i(tag, "ȡ�������");
		isMutilchoice = false;
		ischecked.clear();
		setAdapter(this);
		setEditView();
	}

	// ����Ӱ�ť�������ִ��
	public void handleAddTag() {
		list = ischeckedHotLabel;
		setAdapter(context);
		setEditView();
		labelListView.setVisibility(View.VISIBLE);
		hotLabelHint.setVisibility(View.GONE);
		flowLayout.setVisibility(View.GONE);
	}

	// ��ɾ����ť�������ִ��
	private void handleDelete() {
		Log.i(tag, "ɾ�������");
		isMutilchoice = false;
		Log.i(tag, "ischecked size:" + ischecked.size());
		Log.i(tag, "list size:" + list.size());
		for (int j = 0; j < ischecked.size(); j++) {
			for (int i = 0; i < list.size(); i++) {
				if (ischecked.get(j).equals(list.get(i))) {
					list.remove(i);
					break;
				}
			}
		}
		ischecked.clear();
		setAdapter(this);
		setEditView();
	}

	// ɾ����ǩ���߳�
	Runnable deleteRunnable = new Runnable() {

		@Override
		public void run() {
			if(handleLabel==null){
			handleLabel = new HandleLabel();
			}
			Log.i(tag, ischecked.toString());
			JSONArray arr=getJsonArray(ischecked);
			Log.i(tag, "json:"+arr.toString());
			handleLabel.doPost(UriAPI.SUB_URL+URI_CANCELTAG, cookie,arr);
			response=handleLabel.getResponse();
			Log.i(tag, "response:"+response);
			String tip=getJson(response);
			if(tip.equals("success")){
				handler.sendEmptyMessage(5);
			}else{
				handler.sendEmptyMessage(7);//ɾ��ʧ��
			}
			
		}
	};

	// ��ӱ�ǩ���߳�
	Runnable addTagRunnable = new Runnable() {

		@Override
		public void run() {
			if(handleLabel==null){
				handleLabel=new HandleLabel();
			}
			Log.i(tag, ischeckedHotLabel.toString());
			JSONArray arr=getJsonArray(ischeckedHotLabel);
			Log.i(tag, "json:"+arr.toString());
			handleLabel.doPost(UriAPI.SUB_URL+URI_ADDTAG, cookie,arr);
			response=handleLabel.getResponse();
			Log.i(tag, "response:"+response);
			
			String tip=getJson(response);
			if(tip.equals("success")){
				handler.sendEmptyMessage(4);//����û��ɹ�
			}else{
				handler.sendEmptyMessage(6);//����û���ǩʧ��
			}
			
			
		}

	};

	// ��ȡ��ǩ���߳�
	Runnable runnable = new Runnable() {

		@Override
		public void run() {

			ReceiveMyLabel receiveLable = new ReceiveMyLabel();
			receiveLable.setCookie(cookie);
			Log.i(tag, "url=" + UriAPI.SUB_URL + subUri);
			receiveLable.Connect(UriAPI.SUB_URL + subUri, 1);
			response = receiveLable.getResponse();
			Log.i(tag, "response:" + response);
			LabelData data = new Gson().fromJson(response, LabelData.class);
			String tip = data.getTip();
			list = data.getUserTag();
			hotLabel = data.getHotLabel();
			if (tip.equals("success")) {
				if (list == null) {
					if (hotLabel == null) {
						handler.sendEmptyMessage(3);// ������ʾ
					} else {
						handler.sendEmptyMessage(1);// û���Լ���ע�ı�ǩ��ʾ�����ű�ǩ���û�ѡ��
						Log.i(tag, "hotTag:" + hotLabel.toString());
					}
				} else {
					handler.sendEmptyMessage(2);// ���Լ���ע�ı�ǩ
				}
			} else {
				handler.sendEmptyMessage(3);// ������ʾ
			}
		}
	};

	public JSONArray getJsonArray(List<String> hasSelected){
		JSONArray arr=new JSONArray();
		for(int i=0;i<hasSelected.size();i++){
			arr.put(hasSelected.get(i));
		}
		return arr;
	}
	/**
	 * ��responseתΪjsonobject
	 * @return
	 */
	public String getJson(String resp){
		JSONObject o=null;
		try {
			o = new JSONObject(resp);
		} catch (JSONException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			return null;
		}
		String tip;
		try {
			tip = o.getString("tip");
		} catch (JSONException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			return null;
		}		
		return tip;
	}
	/**
	 * ����adapter
	 * 
	 * @param context
	 */
	public void setAdapter(Context context) {
		adapter = new LabelAdapter(this);
		labelListView.setAdapter(adapter);
	}

	/**
	 * ��ǩadapter
	 * 
	 * @author Jackbing
	 * 
	 */
	class LabelAdapter extends BaseAdapter {

		private Context context;
		private LayoutInflater inflater;
		private HashMap<Integer, View> mView;// �洢view
		private HashMap<Integer, Integer> visiblecheck;// ��¼�Ƿ���ʾ��ѡ��
		private HashMap<Integer, Boolean> selectState;
		private String tag = "adapter";

		@SuppressLint("UseSparseArrays")
		public LabelAdapter(Context context) {
			this.context = context;
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mView = new HashMap<Integer, View>();// ����ÿ��item��view
			visiblecheck = new HashMap<Integer, Integer>();
			selectState = new HashMap<Integer, Boolean>();
			if (isMutilchoice) {
				for (int i = 0; i < list.size(); i++) {
					selectState.put(i, false);
					visiblecheck.put(i, ImageView.VISIBLE);
				}
			} else {
				for (int i = 0; i < list.size(); i++) {
					selectState.put(i, false);
					visiblecheck.put(i, ImageView.INVISIBLE);
				}
			}
		}

		public int getCount() {
			return list.size();
		}

		public String getItem(int position) {
			return list.get(position);
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = mView.get(position);
			if (view == null) {
				view = inflater.inflate(R.layout.my_label_listview_item, null);
				TextView label = (TextView) view.findViewById(R.id.label_text);
				final ImageView box = (ImageView) view
						.findViewById(R.id.choicebox);
				label.setText(list.get(position));
				if (selectState.get(position)) {
					box.setBackgroundResource(R.drawable.selected);
				} else {
					box.setBackgroundResource(R.drawable.unselected);
				}
				box.setVisibility(visiblecheck.get(position));
				view.setOnLongClickListener(new OnLongClick());
				view.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						if (isMutilchoice) {
							Log.i(tag, "position:" + position);
							if (selectState.get(position)) {
								ischecked.remove(list.get(position));
								box.setBackgroundResource(R.drawable.unselected);
								selectState.put(position, false);
							} else {
								ischecked.add(list.get(position));
								box.setBackgroundResource(R.drawable.selected);
								selectState.put(position, true);
							}

							if (ischecked.size() > 0) {
								btnEdit.setBackgroundColor(Color.RED);
								btnEdit.setText("ɾ ��");
							}
							if (ischecked.size() == 0) {
								setCancelView();
							}

						} else {

							// ��������ĳ��������ص������б�
							Log.i(tag, "label:" + list.get(position));
							goLabelofBBS(list.get(position));
						}
					}
				});
			}
			mView.put(position, view);
			return view;
		}

		// ���������¼�
		class OnLongClick implements OnLongClickListener {

			public boolean onLongClick(View v) {
				// TODO �Զ����ɵķ������
				setView();
				return true;
			}
		}
	}

	/**
	 * ���û�ѡ�������ű�ǩ֮��
	 * 
	 */
	public void setAddView() {
		btnEdit.setBackgroundColor(Color.parseColor("#4033FF33"));
		btnEdit.setText("�� ��");
	}

	/**
	 * ���û����ȡ����ť��ɾ����ť�󣬸��İ�ť����Ϊ���༭������ɫҲ��Ӧ�ı�
	 */
	public void setEditView() {
		btnEdit.setBackgroundColor(Color.parseColor("#4000EE00"));
		btnEdit.setText("�� ��");
	}

	/**
	 * ���û�����༭��ť�󣬸��İ�ť����Ϊ��ȡ��������ɫҲ��Ӧ�ı�
	 */
	public void setCancelView() {
		btnEdit.setBackgroundColor(Color.parseColor("#4000EE00"));
		btnEdit.setText("ȡ ��");
	}

	/**
	 * �����б�Ϊ�ɱ༭ģʽ�����ɹ�ѡɾ��ĳ����ǩ
	 */
	public void setView() {
		isMutilchoice = true;
		setCancelView();
		for (int i = 0; i < list.size(); i++) {
			adapter.visiblecheck.put(i, ImageView.VISIBLE);
		}
		adapter = new LabelAdapter(this);
		labelListView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.label_listview_btn_back:
			Log.i(tag, "brnback");
			this.onBackPressed();
			break;
		case R.id.label_listview_btn_finish:
			handleLabelList();
			break;
		}
	}

	// ��ת�ķ�������ת��ĳ����ǩ�������б�
	public void goLabelofBBS(String label) {
		Intent intent = new Intent(MyLabel.this, LabelOfBBS.class);
		intent.putExtra("cookie", cookie);
		intent.putExtra("userTag", label);
		startActivity(intent);
	}

	@Override
	public void processMessage(Message msg) {

	}

}
