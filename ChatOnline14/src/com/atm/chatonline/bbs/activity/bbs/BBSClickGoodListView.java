package com.atm.chatonline.bbs.activity.bbs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.ui.PersonalMessageActivity;
import com.example.studentsystem01.R;

/**
 * @�� com.atm.chatonline.activity.bbs ---BBSClickGoodListView
 * @���� ����������ʾһƪ���ӵĵ����б�
 * @���� ��YD
 * @ʱ�� 2015-8-24
 * 
 */
public class BBSClickGoodListView extends BaseActivity {
	private WebView click_good;
	private WebSettings webSettings;
	private ImageView iv_return;
	private static String cookie;
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.click_good_view);
		
		
		//��ȡcookie
		SharedPreferences pref = getSharedPreferences("data",Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");
		
		click_good = (WebView) findViewById(R.id.click_good);
		iv_return = (ImageView) findViewById(R.id.iv_return);
		iv_return.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				BBSClickGoodListView.this.finish();
			}
		});
		
		webSettings = click_good.getSettings();
		webSettings.setUseWideViewPort(true); 
		webSettings.setLoadWithOverviewMode(true); 
		webSettings.setJavaScriptEnabled(true);
		click_good.addJavascriptInterface(new PersonalMsgInterface(), "demo1");
		//���մ�BBSPostDetailView������������
		Bundle bundle = this.getIntent().getExtras();
		String url = bundle.getString("url");
		synCookies(this, url);
		click_good.loadUrl(url);
	}
	@Override
	public void processMessage(Message msg) {
		// TODO �Զ����ɵķ������
		
	}
	
	
	/** 
	 * ��Ϊ�ͻ��˺���ҳ��cookie�ǲ�һ���ģ���������Ҫͬ��һ��cookie 
	 */  
	public static void synCookies(Context context, String url) {  
	    CookieSyncManager.createInstance(context);  
	    CookieManager cookieManager = CookieManager.getInstance();  
	    cookieManager.setAcceptCookie(true);  
	    //cookieManager.removeSessionCookie();//�Ƴ�  
	    cookieManager.setCookie(url, cookie);
	    CookieSyncManager.getInstance().sync();  
	}  
	
	class PersonalMsgInterface{
		@JavascriptInterface
		public void toPersonalMsg(String friendID){
			Intent intent=new Intent(BBSClickGoodListView.this,PersonalMessageActivity.class);
			intent.putExtra("friendID", friendID);
			startActivity(intent);
		}
	}
}
