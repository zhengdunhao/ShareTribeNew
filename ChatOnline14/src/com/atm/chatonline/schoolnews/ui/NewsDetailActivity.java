package com.atm.chatonline.schoolnews.ui;

import java.io.File;
import java.io.FileOutputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.sharesdk.framework.AbstractWeibo;
import cn.sharesdk.onekeyshare.ShareAllGird;

import com.atm.charonline.recuit.ui.RecuitPostDetailView;
import com.atm.chatonline.bbs.adapter.MyWebChromeClient;
import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.bbs.util.BBSConnectNet;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.share.SharePopupWindow;
import com.atm.chatonline.share.WebPageShare;
import com.example.studentsystem01.R;

public class NewsDetailActivity extends BaseActivity implements
		OnClickListener {

	private String tag = "NewsDetailActivity";
	private String newsId = "", response;
	private String relativePath = "essay_content.action";
	private String cookie;
	private String url = UriAPI.SUB_URL + "schoolActive/getNews/";
	private ProgressDialog progressDialog;// ������
	private WebView webView;
	private ImageView imgShare;
	private ImageView imgReturn;

	private BBSConnectNet httpClientGet;
	private WebSettings webSettings;
	// ����ͼƬ��ŵĵ�ַ
	public static String TEST_IMAGE;
	
	
	private SharePopupWindow pop;
	private OnClickListener listener;
	private LinearLayout parentLayout;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȡ��������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_detail);

		// ��ʼ��ShareSDK
		AbstractWeibo.initSDK(this);
		// ���ձ����������ID
		Bundle bundle = this.getIntent().getExtras();
		Log.i(tag, "newsId:" + bundle.getString("id"));
		newsId = bundle.getString("id");
		// ��ȡcookie
		SharedPreferences pref = getSharedPreferences("data",
				Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");
		initLintener();
		initView();
		initProgressDialog();
		new Thread(runnable).start();
	}

	void initView() {
		webView = (WebView) findViewById(R.id.news_webView);
		imgShare = (ImageView) findViewById(R.id.iv_share);
		imgReturn = (ImageView) findViewById(R.id.iv_return);
		parentLayout = (LinearLayout) findViewById(R.id.parent_layout);
		imgReturn.setOnClickListener(this);
		imgShare.setOnClickListener(this);
	}

	private void initProgressDialog() {
		// ������ʾ��
		progressDialog = new ProgressDialog(NewsDetailActivity.this);
		progressDialog.setTitle("�ٵȵȣ��������");
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(true);
		progressDialog.show();
	}
	
	//���������ж��û����������������|����Ȧ�� 2016.4.13 ��
		private void initLintener() {
			listener=new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Bitmap thumb=BitmapFactory.decodeResource(NewsDetailActivity.this.getResources(), R.drawable.pic_software_small);
					WebPageShare webpage=new WebPageShare(NewsDetailActivity.this, url + newsId + ".do", "У�Ѷ�̬", "�����������������������������������������������������������������������������", thumb);
					if(!webpage.isInstalled()){
						Toast.makeText(NewsDetailActivity.this.getApplicationContext(), "����û�а�װ΢�ſͻ��ˣ�", Toast.LENGTH_SHORT).show();
						return;
					}
					switch(v.getId()){
					//������������ͼ��
					case R.id.ly_share_friend:
						webpage.shareToWx(0);
						break;
						//����˷�������Ȧͼ��
					case R.id.ly_share_timeline:
						webpage.shareToWx(1);
						break;
					}
					//ÿ�η�����Ϲرյ�������
					pop.dismiss();
				}
			};
		}

	// WebView������ҳ
	private void webViewLoadUrl() {
		// TODO Auto-generated method stub
		Log.i(tag, "webViewLoadUrl()");
		webSettings = webView.getSettings();
		Log.i(tag, "webViewLoadUrl()2222");
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setJavaScriptEnabled(true);
		Log.i(tag, "webViewLoadUrl()1111");
		// ������ҳ�е����������ʱ�������ø�webView������
		webView.setWebChromeClient(new MyWebChromeClient());
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				Log.i(tag, "shouldOverrideUrlLoading-url" + url);
				view.loadUrl(url);
				// �������Ҫ�����Ե�������¼��Ĵ�����true�����򷵻�false
				return true;
			}
		});
		// webView.addJavascriptInterface(new DemoJavaScriptInterface(),
		// "demo1");
		// synCookies(this, url + essayId + ".html");//ͬ��cookie
		Log.i(tag, "loadUrl:" + url + newsId + ".do");
		webView.loadUrl(url + newsId + ".do");
		progressDialog.dismiss();
	}

	Runnable runnable = new Runnable() {
		public void run() {
			webViewLoadUrl();

		}
	};

	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_return:
			NewsDetailActivity.this.finish();
			break;

		case R.id.iv_share:
			Log.i(tag, "��������");
			if(pop==null){
				pop=new SharePopupWindow(this, 0.5f, listener);
				}
				pop.showAtLocation(parentLayout, Gravity.BOTTOM, 0, 0);
				
//			showGrid(false);
			break;
		}
	}

	private void showGrid(boolean silent) {
		Log.i(tag, "showGrid");
		Intent i = new Intent(this, ShareAllGird.class);
		// ����ʱNotification��ͼ��
		i.putExtra("notif_icon", R.drawable.ic_launcher);
		// ����ʱNotification�ı���
		i.putExtra("notif_title", this.getString(R.string.app_name));

		// title���⣬��ӡ��ʼǡ����䡢��Ϣ��΢�ţ��������Ѻ�����Ȧ������������QQ�ռ�ʹ�ã�������Բ��ṩ
		i.putExtra("title", this.getString(R.string.share));
		// titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ�ã�������Բ��ṩ
		i.putExtra("titleUrl", "http://sharesdk.cn");
		// text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		i.putExtra("text", this.getString(R.string.share_content));
		// imagePath�Ǳ��ص�ͼƬ·��������ƽ̨��֧������ֶΣ����ṩ�����ʾ������ͼƬ
		i.putExtra("imagePath", NewsDetailActivity.TEST_IMAGE);
		// url����΢�ţ��������Ѻ�����Ȧ����ʹ�ã�������Բ��ṩ
		i.putExtra("url", "http://sharesdk.cn");
		// thumbPath������ͼ�ı���·��������΢�ţ��������Ѻ�����Ȧ����ʹ�ã�������Բ��ṩ
		i.putExtra("thumbPath", NewsDetailActivity.TEST_IMAGE);
		// appPath�Ǵ�����Ӧ�ó���ı���·��������΢�ţ��������Ѻ�����Ȧ����ʹ�ã�������Բ��ṩ
		i.putExtra("appPath", NewsDetailActivity.TEST_IMAGE);
		// comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ�ã�������Բ��ṩ
		i.putExtra("comment", this.getString(R.string.share));
		// site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ�ã�������Բ��ṩ
		i.putExtra("site", this.getString(R.string.app_name));
		// siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ�ã�������Բ��ṩ
		i.putExtra("siteUrl", "http://sharesdk.cn");

		// �Ƿ�ֱ�ӷ���
		i.putExtra("silent", silent);
		Log.i(tag, "showGrid1111");
		this.startActivity(i);
	}

	private void initImagePath() {
		try {// �ж�SD�����Ƿ���ڴ��ļ���
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())
					&& Environment.getExternalStorageDirectory().exists()) {
				TEST_IMAGE = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/pic.png";
			} else {
				TEST_IMAGE = getApplication().getFilesDir().getAbsolutePath()
						+ "/pic.png";
			}
			File file = new File(TEST_IMAGE);
			// �ж�ͼƬ�Ƿ����ļ�����
			if (!file.exists()) {
				file.createNewFile();
				Bitmap pic = BitmapFactory.decodeResource(getResources(),
						R.drawable.pic);
				FileOutputStream fos = new FileOutputStream(file);
				pic.compress(CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			TEST_IMAGE = null;
		}
	}

	public static String actionToString(int action) {
		switch (action) {
		case AbstractWeibo.ACTION_AUTHORIZING:
			return "ACTION_AUTHORIZING";
		case AbstractWeibo.ACTION_GETTING_FRIEND_LIST:
			return "ACTION_GETTING_FRIEND_LIST";
		case AbstractWeibo.ACTION_FOLLOWING_USER:
			return "ACTION_FOLLOWING_USER";
		case AbstractWeibo.ACTION_SENDING_DIRECT_MESSAGE:
			return "ACTION_SENDING_DIRECT_MESSAGE";
		case AbstractWeibo.ACTION_TIMELINE:
			return "ACTION_TIMELINE";
		case AbstractWeibo.ACTION_USER_INFOR:
			return "ACTION_USER_INFOR";
		case AbstractWeibo.ACTION_SHARE:
			return "ACTION_SHARE";
		default: {
			return "UNKNOWN";
		}
		}
	}

}
