package com.atm.chatonline.bbs.activity.bbs;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
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

import com.atm.charonline.recuit.ui.RecuitPostDetailView;
import com.atm.chatonline.bbs.adapter.MyWebChromeClient;
import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.bbs.util.BBSConnectNet;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.ui.PersonalMessageActivity;
import com.atm.chatonline.share.SharePopupWindow;
import com.atm.chatonline.share.WebPageShare;
import com.atm.chatonline.usercenter.activity.usercenter.LabelOfBBS;
import com.example.studentsystem01.R;

/**
 * @�� com.atm.chatonline.activity.bbs ---BBSPostDetailView
 * @���� ����������ʾ��������
 * @���� ��YD
 * @ʱ�� 2015-8-24
 * 
 * */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("NewApi")
public class BBSPostDetailView extends BaseActivity implements OnClickListener {
	private WebView webView;
	private WebSettings webSettings;
	private LinearLayout ll_clickGood, ll_comment, ll_share, ll_report;
	private ImageView iv_clickGood, iv_comment, iv_share, iv_report, iv_return,
			iv_collect;
	private TextView tv_clickGood, tv_comment, tv_share, tv_report;
	private String essayId = "";
	private String contentResponse, collectResponse, clickResponse;
	private String contentPath = "essay_content.action";// ��������
	private String collectPath = "essay_collectEssay.action";// �ղ�����·��
	private String clickGoodPath = "essay_clickGood.action";// ����·��
	private BBSConnectNet httpClientGet;
	private String url = UriAPI.SUB_URL + "essay/";
	public static final int IS_CLICK_COLLECT = 1, IS_CLICK_NOT_COLLECT = 2,
			NOT_CLICK_IS_COLLECT = 3, NOT_CLICK_NOT_COLLECT = 4;
	public boolean isClicked, isCollected;
	private Handler handler;
	private int replyNum;
	private static String cookie;
	private ProgressDialog dialog;

	private String tag = "BBSPostDetailView";

	private SharePopupWindow pop;// ���һ��Popwindow������һ����������,���Բ��ֱ��� author ��
									// 2016.4.14
	private OnClickListener listener;
	private LinearLayout parentLayout;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ȡ��������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bbs_post_detail);

		// ����BBSMainView�������һƪ����ʱ���ᴫ����һ��essatId
		Bundle bundle = this.getIntent().getExtras();
		essayId = bundle.getString("id");

		// ��ʼ���������󣬼����û��Ĳ���
		initLintener();
		initView();
		initEvent();

		// ��ȡcookie
		SharedPreferences pref = getSharedPreferences("data",
				Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");

		requestContent();// �����߳����������ȡ��������

		// �첽��Ϣ�������
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case IS_CLICK_COLLECT:
					iv_clickGood.setImageResource(R.drawable.clickgood_green);
					tv_clickGood.setTextColor(0xff33cc66);
					tv_clickGood.setText("ȡ����");
					Log.d(tag, "IS_CLICK_COLLECT:collected==" + isCollected);
					iv_collect.setBackground(getResources().getDrawable(R.drawable.collected));
					break;
				case IS_CLICK_NOT_COLLECT:
					iv_clickGood.setImageResource(R.drawable.clickgood_green);
					tv_clickGood.setTextColor(0xff33cc66);
					tv_clickGood.setText("ȡ����");
					Log.d(tag, "IS_CLICK_NOT_COLLECT:collected==" + isCollected);
					iv_collect.setBackground(getResources().getDrawable(R.drawable.collect));
					break;
				case NOT_CLICK_IS_COLLECT:
					iv_clickGood.setImageResource(R.drawable.clickgood);
					tv_clickGood.setTextColor(0xff666666);
					tv_clickGood.setText("����");
					Log.d(tag, "NOT_CLICK_IS_COLLECT:collected==" + isCollected);
					iv_collect.setBackground(getResources().getDrawable(R.drawable.collected));
				case NOT_CLICK_NOT_COLLECT:
					iv_clickGood.setImageResource(R.drawable.clickgood);
					tv_clickGood.setTextColor(0xff666666);
					tv_clickGood.setText("����");
					Log.d(tag, "NOT_CLICK_NOT_COLLECT:collected==" + isCollected);
					iv_collect.setBackground(getResources().getDrawable(R.drawable.collect));
					break;
				}
				tv_comment.setText("����(" + replyNum + ")");
			}
		};
		webViewLoadUrl();// WebView������ҳ

	}

	// WebView������ҳ
	private void webViewLoadUrl() {
		// TODO Auto-generated method stub

		webSettings = webView.getSettings();
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setJavaScriptEnabled(true);
		// ������ҳ�е����������ʱ�������ø�webView������
		webView.setWebChromeClient(new MyWebChromeClient());
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				// �������Ҫ�����Ե�������¼��Ĵ�����true�����򷵻�false
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				super.onPageFinished(view, url);
			}
		});
		webView.addJavascriptInterface(new DemoJavaScriptInterface(), "demo1");
		synCookies(this, url + essayId + ".html");// ͬ��cookie
		webView.loadUrl(url + essayId + ".html");
		dialog = ProgressDialog.show(this, null, "ҳ������У����Ժ�..");

	}

	/**
	 * ��Ϊ�ͻ��˺���ҳ��cookie�ǲ�һ���ģ���������Ҫͬ��һ��cookie
	 */
	public static void synCookies(Context context, String url) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.removeSessionCookie();// �Ƴ�
		cookieManager.setCookie(url, cookie);
		CookieSyncManager.getInstance().sync();
	}

	// �����߳����������ȡ��������
	private void requestContent() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					httpClientGet = new BBSConnectNet(essayId, contentPath,
							cookie);
					contentResponse = httpClientGet.getResponse();
					parseJSONWithJSONObject(contentResponse);// �����ȡ��������
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	// �����߳���������������ݣ��Ƿ��ղ�
	private void collect() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					BBSConnectNet get = new BBSConnectNet(essayId, collectPath,
							cookie, isCollected);
					collectResponse = get.getResponse();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	// �����߳���������������ݣ��Ƿ����
	private void clickGood() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					BBSConnectNet get = new BBSConnectNet(essayId,
							clickGoodPath, cookie, isClicked);
					clickResponse = get.getResponse();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	// �����ȡ��������
	private void parseJSONWithJSONObject(String response) {
		// TODO Auto-generated method stub
		JSONObject json;
		Message message;
		try {
			json = new JSONObject(response);
			message = new Message();
			message.what = 0;
			isClicked = json.getBoolean("clickGood");
			System.out.println("clickGood is " + isClicked);
			isCollected = json.getBoolean("collect");
			replyNum = json.getInt("replyNum");
			if (isClicked) {
				if (isCollected) {
					message.what = IS_CLICK_COLLECT;
				} else
					message.what = IS_CLICK_NOT_COLLECT;
			} else {
				if (isCollected) {
					message.what = NOT_CLICK_IS_COLLECT;
				} else
					message.what = NOT_CLICK_NOT_COLLECT;
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
		ll_clickGood = (LinearLayout) findViewById(R.id.ll_clickGood);
		ll_comment = (LinearLayout) findViewById(R.id.ll_comment);
		ll_share = (LinearLayout) findViewById(R.id.ll_share);
		ll_report = (LinearLayout) findViewById(R.id.ll_report);
		iv_clickGood = (ImageView) findViewById(R.id.iv_clickGood);
		iv_comment = (ImageView) findViewById(R.id.iv_comment);
		iv_share = (ImageView) findViewById(R.id.iv_share);
		iv_report = (ImageView) findViewById(R.id.iv_report);
		iv_return = (ImageView) findViewById(R.id.iv_return);
		iv_collect = (ImageView) findViewById(R.id.iv_collect);
		tv_clickGood = (TextView) findViewById(R.id.tv_clickGood);
		tv_comment = (TextView) findViewById(R.id.tv_comment);
		tv_share = (TextView) findViewById(R.id.tv_share);
		tv_report = (TextView) findViewById(R.id.tv_report);
		parentLayout = (LinearLayout) findViewById(R.id.parent_layout);
	}

	// ���ü�����
	private void initEvent() {
		// TODO Auto-generated method stub
		ll_clickGood.setOnClickListener(this);
		ll_comment.setOnClickListener(this);
		ll_share.setOnClickListener(this);
		ll_report.setOnClickListener(this);
		iv_return.setOnClickListener(this);
		iv_collect.setOnClickListener(this);
	}

	// ���������ж��û����������������|����Ȧ�� 2016.4.13 ��
	private void initLintener() {
		listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bitmap thumb = BitmapFactory.decodeResource(
						BBSPostDetailView.this.getResources(),
						R.drawable.pic_software_small_jpg);
				WebPageShare webpage = new WebPageShare(BBSPostDetailView.this,
						url + essayId + ".html", "��̳��Ϣ",
						"����������Ժ�ͬУ��ѧ�������������ͱ�ҵ��ʦ��ʦ��/��û��ҵ��ʦ��ʦ�ý��н������໥����", thumb);
				if (!webpage.isInstalled()) {
					Toast.makeText(
							BBSPostDetailView.this.getApplicationContext(),
							"����û�а�װ΢�ſͻ��ˣ�", Toast.LENGTH_SHORT).show();
					return;
				}
				switch (v.getId()) {
				// ������������ͼ��
				case R.id.ly_share_friend:
					
					webpage.shareToWx(0);
					break;
				// ����˷�������Ȧͼ��
				case R.id.ly_share_timeline:
					webpage.shareToWx(1);
					break;
				}
				// ÿ�η�����Ϲرյ�������
				pop.dismiss();
			}
		};
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.ll_clickGood:
			String response = "";
			clickGood();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				JSONObject click = new JSONObject(clickResponse);
				response = click.getString("tip");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (isClicked == false) {
				if (response.equals("success")) {
					iv_clickGood.setImageResource(R.drawable.clickgood_green);
					tv_clickGood.setTextColor(0xff33cc66);
					tv_clickGood.setText("ȡ����");
					isClicked = true;
				} else {
					Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
				}
			} else {
				if (response.equals("success")) {
					iv_clickGood.setImageResource(R.drawable.clickgood);
					tv_clickGood.setTextColor(0xff666666);
					tv_clickGood.setText("����");
					isClicked = false;
				} else {
					Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.ll_comment:
			Intent intent_comment = new Intent(this, BBSCommentView.class);
			Bundle bundle = new Bundle();
			bundle.putString("essayId", essayId);
			intent_comment.putExtras(bundle);
			startActivity(intent_comment);
			break;
		case R.id.ll_share:
			// ���һ���������ڣ�author �� 2016.4.14
			if (pop == null) {
				pop = new SharePopupWindow(this, 0.5f, listener);
			}
			pop.showAtLocation(parentLayout, Gravity.BOTTOM, 0, 0);
			break;
		case R.id.ll_report:
			Intent intent_report = new Intent(this, BBSReportView.class);
			intent_report.putExtra("essayId", essayId);
			startActivity(intent_report);
			break;
		case R.id.iv_return:
			BBSPostDetailView.this.finish();
			break;
		case R.id.iv_collect:
			String collResponse = "";
			collect();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				JSONObject collect = new JSONObject(collectResponse);
				collResponse = collect.getString("tip");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (isCollected == false) {
				iv_collect.setBackground(getResources().getDrawable(
						R.drawable.collected));
				Toast.makeText(BBSPostDetailView.this, "�ղسɹ�",
						Toast.LENGTH_SHORT).show();
				isCollected = true;
			} else {
				iv_collect.setBackground(getResources().getDrawable(
						R.drawable.collect));
				Toast.makeText(BBSPostDetailView.this, "��ȡ���ղ�",
						Toast.LENGTH_SHORT).show();
				isCollected = false;
			}
			break;
		}

	}

	// Javascript���ð�׿�������ڲ���
	class DemoJavaScriptInterface {
		// ��Ϊ��ȫ���⣬��Android4.2��(���Ӧ�õ�android:targetSdkVersion��ֵΪ17+)
		// JSֻ�ܷ��ʴ��� @JavascriptInterfaceע���Java������
		// ��������Ŀ����汾�Ƚϸߣ���Ҫ�ڱ����õĺ���ǰ����@JavascriptInterfaceע�⡣
		@JavascriptInterface
		public void goClickGoodView(String url) {

			Intent intent = new Intent(BBSPostDetailView.this,
					BBSClickGoodListView.class);
			Bundle bundle = new Bundle();
			bundle.putString("url", url);
			intent.putExtras(bundle);
			BBSPostDetailView.this.startActivity(intent);
		}

		@JavascriptInterface
		public void finishPostDetailView() {
			BBSPostDetailView.this.finish();
		}

		// ��ת��������Ϣ�б�2015.11.16��
		@JavascriptInterface
		public void toPersonalMsg(String friendID) {
			Intent intent = new Intent(BBSPostDetailView.this,
					PersonalMessageActivity.class);
			intent.putExtra("friendID", friendID);
			startActivity(intent);
		}

		// ��ת����ǩ�б�2015.11.16��
		@JavascriptInterface
		public void goLabelofBBS(String label) {
			Intent intent = new Intent(BBSPostDetailView.this, LabelOfBBS.class);
			intent.putExtra("cookie", cookie);
			intent.putExtra("userTag", label);
			startActivity(intent);
		}
	}

	@Override
	public void processMessage(Message msg) {
		// TODO �Զ����ɵķ������

	}

}
