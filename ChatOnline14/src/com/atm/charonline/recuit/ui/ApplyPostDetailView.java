package com.atm.charonline.recuit.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.charonline.recuit.ui.RecuitPostDetailView.TelRecuitJSInterface;
import com.atm.chatonline.bbs.activity.bbs.BBSClickGoodListView;
import com.atm.chatonline.bbs.activity.bbs.BBSCommentView;
import com.atm.chatonline.bbs.adapter.MyWebChromeClient;
import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.bbs.util.BBSConnectNet;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.example.studentsystem01.R;

/**
 * @�� com.atm.chatonline.activity.bbs ---BBSPostDetailView
 * @���� ����������ʾ��������
 * @���� ��YD
 * @ʱ�� 2015-8-24
 * 
 * */

public class ApplyPostDetailView extends BaseActivity implements OnClickListener {
	private WebView webView;
	private WebSettings webSettings;
	private ImageView iv_return,iv_collect;
	private boolean flag = true;
	private String essayId = "", response;
	private String relativePath = "essay_content.action";
	private BBSConnectNet httpClientGet;
	private String url = UriAPI.SUB_URL + "apply/";
	public static final int IS_CLICK = 1, IS_NOT_CLICK = 2;
	private Handler handler;
	private int replyNum;
	private static String cookie;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//ȡ��������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recuit_post_detail);
		
		//����BBSMainView�������һƪ����ʱ���ᴫ����һ��essatId
		Bundle bundle = this.getIntent().getExtras();
		essayId = bundle.getString("id");

		initView();
		initEvent();
				
		//��ȡcookie
		SharedPreferences pref = getSharedPreferences("data",Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");

		jsonDemo();//�����߳����������ȡ����
		
		
		webViewLoadUrl();//WebView������ҳ

	}
	
	//WebView������ҳ
	private void webViewLoadUrl() {
		// TODO Auto-generated method stub
		
		webSettings = webView.getSettings();
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setJavaScriptEnabled(true);
		//������ҳ�е����������ʱ�������ø�webView������
		webView.setWebChromeClient(new MyWebChromeClient());
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				// �������Ҫ�����Ե�������¼��Ĵ�����true�����򷵻�false
				return true;
			}
		});
		webView.addJavascriptInterface(new DemoJavaScriptInterface(), "demo1");
		webView.addJavascriptInterface(new TelRecuitJSInterface(), "recuitView");
		synCookies(this, url + essayId + ".html");//ͬ��cookie
		webView.loadUrl(url + essayId + ".html");
	}
	
	/** 
	 * ��Ϊ�ͻ��˺���ҳ��cookie�ǲ�һ���ģ���������Ҫͬ��һ��cookie 
	 */  
	public static void synCookies(Context context, String url) {  
	    CookieSyncManager.createInstance(context);  
	    CookieManager cookieManager = CookieManager.getInstance();  
	    cookieManager.setAcceptCookie(true);  
	    cookieManager.removeSessionCookie();//�Ƴ�  
	    cookieManager.setCookie(url, cookie);
	    CookieSyncManager.getInstance().sync();  
	}  

	//�����߳����������ȡ����
	private void jsonDemo() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
				httpClientGet = new BBSConnectNet(essayId, relativePath, cookie);
				response = httpClientGet.getResponse();				
				parseJSONWithJSONObject(response);//�����ȡ��������
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();

	}

	//�����ȡ��������
	private void parseJSONWithJSONObject(String response) {
		// TODO Auto-generated method stub
		JSONObject json;
		Message message;
		try {
			json = new JSONObject(response);
			message = new Message();
			message.what = 0;
			boolean clickGood = json.getBoolean("clickGood");
			boolean collect = json.getBoolean("collect");
			replyNum = json.getInt("replyNum");
			if (clickGood == true) {
				message.what = IS_CLICK;
			} else {
				message.what = IS_NOT_CLICK;
			}
			handler.sendMessage(message);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// ��ʼ���ؼ�
	private void initView() {
		// TODO Auto-generated method stub
		webView = (WebView) findViewById(R.id.webView);
		iv_return = (ImageView) findViewById(R.id.iv_return);
		iv_collect = (ImageView) findViewById(R.id.iv_collect);
	}

	// ���ü�����
	private void initEvent() {
		// TODO Auto-generated method stub
		iv_return.setOnClickListener(this);
		iv_collect.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.iv_return:
			ApplyPostDetailView.this.finish();
			break;
		case R.id.iv_collect:
			Toast.makeText(ApplyPostDetailView.this, "�ղسɹ�", Toast.LENGTH_SHORT).show();
			break;
		}
		
	}
	
	//Javascript���ð�׿�������ڲ���
	class DemoJavaScriptInterface{
	// ��Ϊ��ȫ���⣬��Android4.2��(���Ӧ�õ�android:targetSdkVersion��ֵΪ17+)
				// JSֻ�ܷ��ʴ��� @JavascriptInterfaceע���Java������
				// ��������Ŀ����汾�Ƚϸߣ���Ҫ�ڱ����õĺ���ǰ����@JavascriptInterfaceע�⡣
				@JavascriptInterface
				public void goClickGoodView(String url) {

					Intent intent = new Intent(ApplyPostDetailView.this, BBSClickGoodListView.class);
					Bundle bundle = new Bundle();
					bundle.putString("url", url);
					intent.putExtras(bundle);
					ApplyPostDetailView.this.startActivity(intent);
				}
				@JavascriptInterface
				public void finishPostDetailView(){
					ApplyPostDetailView.this.finish();
				}
	}
	
	/**
	 * js����native �ľ����recuitView  ����ʱtel
	 * @author Jackbing
	 *
	 */
	class TelRecuitJSInterface{
		//type���������� һ����sms��һ����tel
		public void tel(String type,String number){
			
			if(type.equals("sms")){
				//������
				sendSmsOnlyNumber(number);
			}else{
				//��绰
				dialPhoneNumber(number);
			}
		}
		
	}
	
	/**
	 * ����ϵͳ���Ͷ��ŵĽ���,ָ��Ŀ�귢����
	 * @param content
	 */

	public void sendSmsOnlyNumber(String number){
		Uri smsUri=Uri.parse("smsto:"+number);
		Intent intent=new Intent(Intent.ACTION_SENDTO,smsUri);
		startActivity(intent);
	}
	
	/**
	 * ����ϵͳ��绰�Ľ��棬ָ��Ŀ��绰����
	 * @param number
	 */
	public void dialPhoneNumber(String number){
		Uri telUri=Uri.parse("tel:"+number);
		Intent intent=new Intent(Intent.ACTION_VIEW,telUri);
		startActivity(intent);
	}

	@Override
	public void processMessage(Message msg) {
		// TODO �Զ����ɵķ������
		
	}

}
