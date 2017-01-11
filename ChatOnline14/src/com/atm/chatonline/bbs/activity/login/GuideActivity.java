package com.atm.chatonline.bbs.activity.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Message;


import java.util.ArrayList;
import java.util.List;

import com.atm.chatonline.bbs.adapter.ViewPagerAdapter;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.Config;
import com.example.studentsystem01.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * @�� com.atm.chatonline.bbs.activity.bbs ---GuideActivity
 * @���� ���δ�������ֵ���������
 * @���� ��С��
 * @ʱ�� 2017-1-2
 * 
 */
public class GuideActivity extends BaseActivity implements OnPageChangeListener {

    private ViewPager vp;
    private GuideAdapter vpAdapter;
    private List<View> views;
    private Button btnSure;

    // �ײ�С��ͼƬ
    private ImageView[] dots;

    // ��¼��ǰѡ��λ��
    private int currentIndex;
    Boolean isFirst;

	private int[] mResIds=new int[]{
			R.drawable.bg1,
			R.drawable.bg2,
			R.drawable.bg3,
			}; 	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.guide_layout);

        // ��ʼ��ҳ��
        initViews();

        // ��ʼ���ײ�С��
        initDots();
    }

	private void initViews() {
		
		//�ж��Ƿ���δ����
        SharedPreferences pref = getSharedPreferences("first",Activity.MODE_PRIVATE);
        isFirst = pref.getBoolean("status",true);
        Editor editor = pref.edit(); 
        
        if(!isFirst){
    		Intent intent=new Intent(getApplicationContext(), LoginView.class);
    		startActivity(intent);
            finish();
        }
        else {
        editor.putBoolean("status", false); 
        editor.commit(); 
        }
//        LayoutInflater inflater = LayoutInflater.from(this);
//        RelativeLayout guideFour = (RelativeLayout) inflater.inflate(R.layout.guide_four, null);
//        guideFour.findViewById(R.id.toMain).setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//        		Intent intent=new Intent(getApplicationContext(), LoginView.class);
//        		Bundle bundle=new Bundle();
//        		bundle.putInt("login", Config.FIRSTLOGIN);
//        		intent.putExtras(bundle);
//        		startActivity(intent);
//                finish();
//            }
//        });

		btnSure = (Button) findViewById(R.id.btn_sure);
        views = new ArrayList<View>();
        LogUtil.d("1");
//        // ��ʼ������ͼƬ�б�
//        views.add(inflater.inflate(R.layout.guide_one, null));
//        views.add(inflater.inflate(R.layout.guide_two, null));
//        views.add(guideFour);
		btnSure = (Button) findViewById(R.id.btn_sure);
        // ��ʼ��Adapter
        vpAdapter = new GuideAdapter(views);

        vp = (ViewPager) findViewById(R.id.viewpager);
//        vp.setAdapter(vpAdapter);
        // �󶨻ص�
//        vp.setOnPageChangeListener(this);
        initDatas();
        btnSure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
        		Intent intent=new Intent(getApplicationContext(), LoginView.class);
        		Bundle bundle=new Bundle();
        		bundle.putInt("login", Config.FIRSTLOGIN);
        		intent.putExtras(bundle);
        		startActivity(intent);
                finish();
			}
		});
    }

    /**
	 * 
	 */
	private void initDatas() {
		for (int i = 0; i < mResIds.length; i++) {
			ImageView img=new ImageView(this);
			//�ӳ�����ͼƬ����PagerAdapter�����ã����OOM����					

			/*ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
	  	                ViewGroup.LayoutParams.MATCH_PARENT,  	               
			 ViewGroup.LayoutParams.MATCH_PARENT);
			img.setBackgroundResource(mResIds[i]);
			img.setLayoutParams(params);*/
			views.add(img);
		}		
		vp.setAdapter(vpAdapter);
		vp.setOnPageChangeListener(this);
		vp.setCurrentItem(0);
		vp.setOffscreenPageLimit(1);
	}

	private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        dots = new ImageView[views.size()];

        // ѭ��ȡ��С��ͼƬ
        for (int i = 0; i < views.size(); i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);// ����Ϊ��ɫ
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);// ����Ϊ��ɫ����ѡ��״̬
    }

    private void setCurrentDot(int position) {
        if (position < 0 || position > views.size() - 1
                || currentIndex == position) {
            return;
        }

        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = position;
    }
//
//    // ������״̬�ı�ʱ����
//    @Override
//    public void onPageScrollStateChanged(int arg0) {
//    }
//
//    // ����ǰҳ�汻����ʱ����
//    @Override
//    public void onPageScrolled(int arg0, float arg1, int arg2) {
//    }
//
//    // ���µ�ҳ�汻ѡ��ʱ����
//    @Override
//    public void onPageSelected(int arg0) {
//        // ���õײ�С��ѡ��״̬
//        setCurrentDot(arg0);
//    }

	/* (non-Javadoc)
	 * @see com.atm.chatonline.chat.ui.BaseActivity#processMessage(android.os.Message)
	 */
	public void processMessage(Message msg) {
		
	}

	/**
	 *
 	 * @Create_date 2014-12-19 ����11:09:48
	 * @TODO ������
	 */	

	class GuideAdapter extends PagerAdapter{
		private List<View> views;
		
		public GuideAdapter(List<View> views) {
			this.views = views;
		}		

		@Override
		public int getCount() {
			return views.size();
		}		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(views.get(position));
		}
		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			//�ڴ����ñ���ͼƬ����߼����ٶȣ����OOM����
			View view=views.get(position);
			int count=getCount();
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams( 	
		 	                ViewGroup.LayoutParams.MATCH_PARENT, 
		 	                ViewGroup.LayoutParams.MATCH_PARENT);
			view.setBackgroundResource(mResIds[position%count]);
			view.setLayoutParams(params);
			container.addView(view,0);
			return views.get(position);
		}
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
			}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
	@Override
	public void onPageSelected(int arg0) {

		// ���õײ�С��ѡ��״̬
		setCurrentDot(arg0);
		if(arg0<vpAdapter.getCount()-1){
		btnSure.setVisibility(View.GONE);
		}else{
			btnSure.setVisibility(View.VISIBLE);
		}
	}
}