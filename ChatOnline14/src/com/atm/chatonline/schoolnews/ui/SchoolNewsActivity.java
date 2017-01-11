package com.atm.chatonline.schoolnews.ui;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.KeyEvent;
import android.view.ViewParent;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atm.chatonline.bbs.commom.NewMessage;
import com.atm.chatonline.bbs.util.BBSConnectNet;
import com.atm.chatonline.bbs.util.ExtendsIntent;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.bbs.util.ReceivePhoto;
import com.atm.chatonline.bbs.util.SharedPreferenceUtils;
import com.atm.chatonline.bbs.util.SlidingTitleLayout;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.Config;
import com.atm.chatonline.schoolnews.adapter.NewsAdapter;
import com.atm.chatonline.schoolnews.bean.News;
import com.atm.chatonline.schoolnews.bean.TopNews;
import com.atm.chatonline.schoolnews.util.ImageHandler;
import com.atm.chatonline.schoolnews.util.NewsXmlParser;
import com.example.studentsystem01.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class SchoolNewsActivity extends BaseActivity {

	private String tag = "SchoolNewsActivity";
	private SlidingMenu slidingMenu;
	//����ͼƬ�ļ���
	private ArrayList<View> imagePageViews = null;
	public ViewPager viewPager = null;
	//��ǰViewPager����
	private int pageIndex = 0;
	//�ؼ��л�
	private ImageView imgViewMore;

	//����Բ��ͼƬ
	private ViewGroup imageCircleView = null;
	private ImageView[] imageCircleViews = null;

	//����������
	private TextView tvSlideTitle = null;

	private SlideImageLayout slideLayout = null;
	private NewsXmlParser parser;
	private ProgressDialog progressDialog;// ������
	
	//��������������
	private String relativePath;
	private int page;
	private String cookie;
	
	//����ͷ������
	private String topNewsRelativePath;
	private List<TopNews> topNewsList = new ArrayList<TopNews>();
	private String topResponse;
	
	//���������б����Ӧ
	private String response;
	
	//ΪListView��׼��
	private PullToRefreshListView plv;
	private List<News> newsList = new ArrayList<News>();
	
	
	public Handler handler;
	public ImageHandler imageHandler = new ImageHandler(new WeakReference<SchoolNewsActivity>(this));
	
	private BBSConnectNet bBSConnectNet;
	private NewsAdapter newsAdapter;
	
	private SlidingTitleLayout slidingTitleLayout;
	
	private AlphaAnimation start_anima;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.school_news_main);
		initProgressDialog();
//		InItSlidingMenu();
		SharedPreferences pref = getSharedPreferences("data",Context.MODE_PRIVATE);
		cookie = pref.getString("cookie", "");
		initView();
//		loadTopNews();
		loadData();
//		initWidget();
		
		//��ʼ��������
		imageHandler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
		
		handler = new Handler(){
			public void handleMessage(Message msg){
				LogUtil.p(tag, "msg.what:"+msg.what);//Config.REFRESH_UI=99  Config.REFRESH_TOP_NEWS=2008
				if(msg.what==Config.REFRESH_UI){
					try{
					if(newsList.size()!=0){
						LogUtil.p(tag, "newsList��Ϊ��,����Ϊ:"+newsList.size());
					}
					
					newsAdapter = new NewsAdapter(SchoolNewsActivity.this,R.layout.news_list_item,newsList);
					plv.setAdapter(newsAdapter);
					}catch(Exception e){
						return ;
					}
					newsAdapter.notifyDataSetChanged();
					plv.onRefreshComplete();
					progressDialog.dismiss();
					LogUtil.p(tag, "newsList���½�����³ɹ�");
				}else if(msg.what==Config.REFRESH_TOP_NEWS){
					try{
					if(topNewsList.size()==0){
						LogUtil.p(tag, "topNewsListΪ��,����Ϊ:"+topNewsList.size());
					}
					//��ʾͷ��ͼƬ��ԲȦ
					int length = topNewsList.size();
					LogUtil.p(tag, "length:" + length);
					imageCircleViews = new ImageView[length];
					slideLayout = new SlideImageLayout(SchoolNewsActivity.this,topNewsList);
					slideLayout.setCircleImageLayout(length);
					for (int i = 0; i < length; i++) {
						imagePageViews.add(slideLayout.getSlideImageLayout(topNewsList.get(i).getNewsImage()));//���ͷ������ͼƬ
						
						imageCircleViews[i] = slideLayout.getCircleImageLayout(i);
						imageCircleView.addView(slideLayout.getLinearLayout(
								imageCircleViews[i], 10, 10));
					}


					
					LogUtil.p(tag, "tvSlideTitle��ʼ:"+topNewsList.get(0).getMainTitle());
					tvSlideTitle.setText(topNewsList.get(0).getMainTitle());//��ӱ���
					}catch(Exception e){
						return ;
					}
					viewPager.setAdapter(new SlideImageAdapter());
					progressDialog.dismiss();
					//����һ�¿��ܲ��ܻ���
//					start_anima = new AlphaAnimation();
				}
				
			}
		};
	}
	
	void InItSlidingMenu(){
		slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setBehindOffset(4 / 5);
        slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        slidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);//���ô�����Χ TOUCHMODE_FULLSCREEN��ȫ�֣� 
        slidingMenu.attachToActivity(this, SlidingMenu.RIGHT);//SLIDING_CONTENT
        slidingMenu.setMenu(R.layout.sliding_menu_view);
	
	}
	
	private void initProgressDialog() {
		// ������ʾ��
		progressDialog = new ProgressDialog(SchoolNewsActivity.this);
		progressDialog.setTitle("�ٵȵȣ��������");
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(true);
		progressDialog.show();
	}

	private void initView() {
		
		slidingTitleLayout=(SlidingTitleLayout)findViewById(R.id.slidingtitlelayout);
		slidingTitleLayout.showBadgeView(SharedPreferenceUtils.getInstance().getNewMessage(getApplicationContext()));
		imagePageViews = new ArrayList<View>();
		viewPager = (ViewPager) findViewById(R.id.image_slide_page);
		parser = new NewsXmlParser();
		imageCircleView = (ViewGroup) findViewById(R.id.layout_circle_images);

		tvSlideTitle = (TextView) findViewById(R.id.tvSlideTitle);
//		tvSlideTitle.setText(parser.getSlideTitles()[0]);//��ӱ���
		
		plv = (PullToRefreshListView)findViewById(R.id.lv_news);

//		viewPager.setAdapter(new SlideImageAdapter());
//		viewPager.setOnPageChangeListener(new ImagePageChangeListener());
		viewPager.setOnPageChangeListener(new ImagePageChangeListener());
		
		//Ϊ���б�����������������
		plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
//				bbsNums = 0;
//				new GetDataTask().execute();
				new Thread(loadRunnable).start();
			}
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
//				new GetDataTask().execute();
				new Thread(loadRunnable).start();
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
				Log.i(tag, "���ӱ����");
				Log.i(tag, "position:"+position);
				ExtendsIntent intent = new ExtendsIntent( SchoolNewsActivity.this , NewsDetailActivity.class ,
						newsList.get(position-1).getNewsId(), null , null , 1);
				Log.i(tag, "���ӱ����111");
				Log.i(tag, newsList.get(position - 1).getNewsId());
				startActivity(intent);
				Log.i(tag, "queue.size:"+BaseActivity.queue.size());
				Log.i(tag, "���ӱ����2");
			}
        	
		});
	}
	
	private void loadData(){
		page = 0;
		relativePath = "schoolActive/newsList.do";
		
		Log.i(tag,"cookie:"+cookie);
		new Thread(loadRunnable).start();
	}
	
	private void loadTopNews(){
		
		topNewsRelativePath ="schoolActive/getHeadNews.do";
		new Thread(loadTopNewsRunnable).start();
		
		
	}
	
	Runnable loadRunnable = new Runnable(){
		public void run(){
			try{
				LogUtil.p(tag, "���̨������");
			bBSConnectNet = new BBSConnectNet(page,relativePath,cookie);
			response = bBSConnectNet.getResponse();
//			News news = new Gson().fromJson(response,News.class);
			newsList = new Gson().fromJson(response, new TypeToken<ArrayList<News>>(){}.getType());
			page = newsList.size();
			setImage(newsList);
			
			Message msg1 = new Message();
			msg1.what = Config.REFRESH_UI;
			handler.sendMessage(msg1);
			LogUtil.p(tag, "���߳�loadRunnable����loadTopNews();");
			loadTopNews();
			
		}catch(Exception e){
			e.printStackTrace();
			return ;
		}
		}
	};
	
	Runnable loadTopNewsRunnable= new Runnable(){
		public void run(){
			try{
			bBSConnectNet = new BBSConnectNet(topNewsRelativePath,cookie);
			topResponse = bBSConnectNet.getResponse();
			LogUtil.p(tag, "topResponse��"+topResponse);
			topNewsList = new Gson().fromJson(topResponse, new TypeToken<ArrayList<TopNews>>(){}.getType());
			setTopImage(topNewsList);
			Message msg2 = new Message();
			LogUtil.p(tag, "loadTopNewsRunnable2222");
			msg2.what = Config.REFRESH_TOP_NEWS;
			handler.sendMessage(msg2);
			LogUtil.p(tag, "loadTopNewsRunnable3333");
		}catch(Exception e){
			e.printStackTrace();
			return ;
		}
		}
		
	};
	
	//ReceivePhoto�������ӷ�������ȡ��ͼƬ��ַ���ٴ�ȥ��ͼƬ
	//����topNewsListͷ������ͼƬ  
	private void setTopImage(List<TopNews> topNewsList){
		try{
		for(int i = 0;i<topNewsList.size();i++){
			LogUtil.p(tag, "setTopImage:"+topNewsList.get(i).getNewsImagePath());
			topNewsList.get(i).setNewsImage(new ReceivePhoto(topNewsList.get(i).getNewsImagePath()).getPhoto());
		}
		}catch(NullPointerException e){
			return ;
		}
	}
	
	
	private void setImage(List<News> newsList){
		for(int i = 0;i<newsList.size();i++){
			newsList.get(i).setNewsImage(new ReceivePhoto(newsList.get(i).getNewsImagePath()).getPhoto());
		}
	}

	// ����ͼƬ����������
	private class SlideImageAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return imagePageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).removeView(imagePageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View container, int position) {//public Object instantiateItem(View arg0, int arg1) {
			// TODO Auto-generated method stub
//			((ViewPager) arg0).addView(imagePageViews.get(arg1));
//
//			return imagePageViews.get(arg1);
		    //��ViewPagerҳ����ģȡ��View�б���Ҫ��ʾ����
            position %= imagePageViews.size();
            if (position<0){
                position = imagePageViews.size()+position;
            }
            View view = imagePageViews.get(position);
            //���View�Ѿ���֮ǰ��ӵ���һ����������������remove��������׳�IllegalStateException��
            ViewParent vp =view.getParent();
            if (vp!=null){
                ViewGroup parent = (ViewGroup)vp;
                parent.removeView(view);
            }
            ((ViewGroup) container).addView(view);  
            //add listeners here if necessary
            return view; 
			
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}
	}

	// ����ҳ������¼�������
	private class ImagePageChangeListener implements OnPageChangeListener {
		@Override
		   public void onPageScrollStateChanged(int arg0) {
            switch (arg0) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
                break;
            case ViewPager.SCROLL_STATE_IDLE:
                handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
                break;
            default:
                break;
            }
        }

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int index) {
			pageIndex = index;
			slideLayout.setPageIndex(index);
//			tvSlideTitle.setText(parser.getSlideTitles()[index]);
			tvSlideTitle.setText(topNewsList.get(index).getMainTitle());
			
			for (int i = 0; i < imageCircleViews.length; i++) {
				LogUtil.p(tag, "onPageSelected-index:"+index);
				imageCircleViews[index]
						.setBackgroundResource(R.drawable.dot_selected);

				if (index != i) {
					imageCircleViews[i]
							.setBackgroundResource(R.drawable.dot_none);
				}
			}
		}
	}
	

	
	

	@Override
	public void processMessage(Message msg) {
		if(msg.what==Config.ISHAS_NEW_MYMESSAGE){
			Bundle bundle=msg.getData();
			NewMessage newMessage=(NewMessage)bundle.getSerializable("NewMessage");
			if(newMessage.getSum()>0){
				slidingTitleLayout.showBadgeView(newMessage);
			}	
		}


	}

	protected void onResume(){
		super.onResume();
		getAllChildViews(SchoolNewsActivity.this);
			
		if(SharedPreferenceUtils.getInstance().getNewMessage(getApplicationContext())==null){
			slidingTitleLayout.hideBadgeView();
		}else{
			slidingTitleLayout.showBadgeView(SharedPreferenceUtils.getInstance().getNewMessage(getApplicationContext()));
		}
		
	}
	
	
	 class SlideImageLayout {
		// ����ͼƬ��ArrayList
		private ArrayList<ImageView> imageList = null;
		private Activity activity = null;
		// Բ��ͼƬ����
		private ImageView[] imageViews = null; 
		private ImageView imageView = null;
		private NewsXmlParser parser = null;
		// ��ʾ��ǰ����ͼƬ������
		private int pageIndex = 0;
		
		//����һ��List<TopNews>
		private List<TopNews> topNewsList = new ArrayList<TopNews>();
		
		
		
		public SlideImageLayout(Activity activity,List<TopNews> topNewsList) {
			// TODO Auto-generated constructor stub
			this.activity = activity;
			imageList = new ArrayList<ImageView>();
			parser = new NewsXmlParser();
			this.topNewsList = topNewsList;
		}
		
		/**
		 * ���ɻ���ͼƬ���򲼾�
		 * @param index
		 * @return
		 */
		@SuppressLint("NewApi")
		public View getSlideImageLayout(Drawable background){
			// ����TextView��LinearLayout
			LinearLayout imageLinerLayout = new LinearLayout(activity);
			LinearLayout.LayoutParams imageLinerLayoutParames = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, 
					LinearLayout.LayoutParams.MATCH_PARENT,
					1);
			
			ImageView iv = new ImageView(activity);
//			iv.setBackgroundResource(index);
			iv.setBackground(background);
			iv.setOnClickListener(new ImageOnClickListener());
			imageLinerLayout.addView(iv,imageLinerLayoutParames);
			imageList.add(iv);
			
			return imageLinerLayout;
		}
		
		/**
		 * ��ȡLinearLayout
		 * @param view
		 * @param width
		 * @param height
		 * @return
		 */
		public View getLinearLayout(View view,int width,int height){
			LinearLayout linerLayout = new LinearLayout(activity);
			LinearLayout.LayoutParams linerLayoutParames = new LinearLayout.LayoutParams(
					width, 
					height,
					1);
			// �������Ҳ�Զ������ã�����Ȥ���Լ����á�
			linerLayout.setPadding(10, 0, 10, 0);
			linerLayout.addView(view, linerLayoutParames);
			
			return linerLayout;
		}
		
		/**
		 * ����Բ�����
		 * @param size
		 */
		public void setCircleImageLayout(int size){
			imageViews = new ImageView[size];
		}
		
		/**
		 * ����Բ��ͼƬ���򲼾ֶ���
		 * @param index
		 * @return
		 */
		public ImageView getCircleImageLayout(int index){
			imageView = new ImageView(activity);  
			imageView.setLayoutParams(new LayoutParams(10,10));
	        imageView.setScaleType(ScaleType.FIT_XY);
	        
	        imageViews[index] = imageView;
	         
	        if (index == 0) {  
	            //Ĭ��ѡ�е�һ��ͼƬ
	            imageViews[index].setBackgroundResource(R.drawable.dot_selected);  
	        } else {  
	            imageViews[index].setBackgroundResource(R.drawable.dot_none);  
	        }  
	         
	        return imageViews[index];
		}
		
		/**
		 * ���õ�ǰ����ͼƬ������
		 * @param index
		 */
		public void setPageIndex(int index){
			pageIndex = index;
		}
		
		// ����ҳ�����¼�������
	    private class ImageOnClickListener implements OnClickListener{
	    	@Override
	    	public void onClick(View v) {
	    		Log.i(tag, "wo hao xiang bei dian le ");
	    		ExtendsIntent intent1 = new ExtendsIntent(activity , NewsDetailActivity.class ,
						topNewsList.get(pageIndex).getNewsId(), null , null , 1);
	    		startActivity(intent1);
	    	}

		
	    }

	}	
	 //�����β��˳�����
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				LogUtil.d("����ϵͳ�Դ��ķ��ؼ�");
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
	                   Object mHelperUtils;
	                   Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
	                   mExitTime = System.currentTimeMillis();

				} else {
	                   finish();
	                   con.shutDownSocketChannel();
	                   LogUtil.d("shutDownSocketChannel");
				}
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}
}
