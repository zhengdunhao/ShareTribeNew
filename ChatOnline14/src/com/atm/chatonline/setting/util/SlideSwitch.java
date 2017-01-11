package com.atm.chatonline.setting.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.BitmapFactory.Options;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.example.studentsystem01.R;
  
/** 
 * SlideSwitch ��iphone��������������°ٶ�ħͼ����������� 
 * �����Ϊ����״̬���򿪡��رա����ڻ���<br/> 
 * ʹ�÷�����         
 * <pre>SlideSwitch slideSwitch = new SlideSwitch(this); 
 *slideSwitch.setOnSwitchChangedListener(onSwitchChangedListener); 
 *linearLayout.addView(slideSwitch); 
</pre> 
ע��Ҳ���Լ�����xml����ʹ�� 
 * @author scott 
 * 
 */  
public class SlideSwitch extends View  
{  
    public static final String TAG = "SlideSwitch";  
    public static final int SWITCH_OFF = 0;//�ر�״̬  
    public static final int SWITCH_ON = 1;//��״̬  
    public static final int SWITCH_SCROLING = 2;//����״̬  
    
    public static final float FONT_SIZE=20;//���������С
      
    private String tag="SlideSwitch";
    //������ʾ���ı�  
    private String mOnText = "��";  
    private String mOffText = "�ر�";  
  
    public static int mSwitchStatus = BaseActivity.isDisturb ;  
  
    private boolean mHasScrolled = false;//��ʾ�Ƿ���������  
  
    private int mSrcX = 0, mDstX = 0;  
      
    private int mBmpWidth = 0;  
    private int mBmpHeight = 0;  
    private int mThumbWidth = 0;  
  
    private     Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
      
    private OnSwitchChangedListener mOnSwitchChangedListener = null;  
  
    //����״̬ͼ  
    Bitmap mSwitch_off, mSwitch_on, mSwitch_thumb;  
  
    public SlideSwitch(Context context)   
    {  
        this(context, null);  
    }  
  
    public SlideSwitch(Context context, AttributeSet attrs)   
    {  
        super(context, attrs);
        if (isInEditMode()) {  }
        init();  
    }  
  
    public SlideSwitch(Context context, AttributeSet attrs, int defStyle)  
    {  
        super(context, attrs, defStyle);  
        init();  
    }  
  
    //��ʼ������ͼƬ  
    private void init()  
    {  
        Resources res = getResources();  
    	BitmapFactory.Options options = new Options();
		options.inDither = false; // ������ͼƬ��������
		options.inPreferredConfig = null;// ������ѽ���������
		
        mSwitch_off = BitmapFactory.decodeResource(res,R.drawable.switch_false,options);  
        mSwitch_on = BitmapFactory.decodeResource(res, R.drawable.switch_true,options);  
        mSwitch_thumb = BitmapFactory.decodeResource(res, R.drawable.switch_button);  
        mBmpWidth = mSwitch_on.getWidth();  
        mBmpHeight = mSwitch_on.getHeight();  
        mThumbWidth = mSwitch_thumb.getWidth(); 
        Log.d(tag, "��ʼ��mSwitchStatus:"+mSwitchStatus);
    }  
  
    @Override  
    public void setLayoutParams(LayoutParams params)   
    {  
        params.width = mBmpWidth;  
        params.height = mBmpHeight;  
        super.setLayoutParams(params);  
    }  
      
    /** 
     * Ϊ���ؿؼ�����״̬�ı�������� 
     * @param onSwitchChangedListener �μ� {@link OnSwitchChangedListener} 
     */  
    public void setOnSwitchChangedListener(OnSwitchChangedListener onSwitchChangedListener)  
    {  
        mOnSwitchChangedListener = onSwitchChangedListener;  
    }  
      
    /** 
     * ���ÿ���������ı� 
     * @param onText  �ؼ���ʱҪ��ʾ���ı� 
     * @param offText  �ؼ��ر�ʱҪ��ʾ���ı� 
     */  
    public void setText(final String onText, final String offText)  
    {  
        mOnText = onText;  
        mOffText =offText;  
        invalidate();  
    }  
      
    /** 
     * ���ÿ��ص�״̬ 
     * @param on �Ƿ�򿪿��� ��Ϊtrue �ر�Ϊfalse 
     */  
    public void setStatus(boolean on)  
    {  
        mSwitchStatus = ( on ? SWITCH_ON : SWITCH_OFF);  
    }  
      
    @Override  
    public boolean onTouchEvent(MotionEvent event)  
    {  
        int action = event.getAction();  
        Log.d(TAG, "onTouchEvent  x="  + event.getX());  
        switch (action) {  
        case MotionEvent.ACTION_DOWN:  
            mSrcX = (int) event.getX();  
            break;  
        case MotionEvent.ACTION_MOVE:  
            mDstX = Math.max( (int) event.getX(), 10);  
            mDstX = Math.min( mDstX, 62);  
            if(mSrcX == mDstX)  
                return true;  
            mHasScrolled = true;  
            AnimationTransRunnable aTransRunnable = new AnimationTransRunnable(mSrcX, mDstX, 0);  
            new Thread(aTransRunnable).start();  
            mSrcX = mDstX;  
            break;  
        case MotionEvent.ACTION_UP:  
            if(mHasScrolled == false)//���û�з���������������ζ������һ�ε�������  
            {  
                mSwitchStatus = Math.abs(mSwitchStatus-1);  
                int xFrom = 10, xTo = 62;  
                if(mSwitchStatus == SWITCH_OFF)  
                {  
                    xFrom = 62;  
                    xTo = 10;  
                }  
                AnimationTransRunnable runnable = new AnimationTransRunnable(xFrom, xTo, 1);  
                new Thread(runnable).start();  
            }  
            else  
            {  
                invalidate();  
                mHasScrolled = false;  
            }  
            //״̬�ı��ʱ�� �ص��¼�����  
            if(mOnSwitchChangedListener != null)  
            {  
                mOnSwitchChangedListener.onSwitchChanged(this, mSwitchStatus);  
            }  
            break;  
  
        default:  
            break;  
        }
        Log.d(tag, "mSwitchStatus:"+mSwitchStatus);
        return true;  
    }  
  
    @Override  
    protected void onSizeChanged(int w, int h, int oldw, int oldh)  
    {  
        super.onSizeChanged(w, h, oldw, oldh);  
    }  
  
    @Override  
    protected void onDraw(Canvas canvas)  
    {  
        super.onDraw(canvas);  
        //��ͼ��ʱ�� �ڲ��õ���һЩ��ֵ��Ӳ���룬��ʵ��̫�ã�  
        //��Ҫ�ǿ��ǵ�ͼƬ��ԭ��ͼƬ��Χ��͸���߽磬����Ҫ��һ����ƫ��  
        //Ӳ�������ֵֻҪ�����˴��룬��ʵ��������京�壬��������Ӧ�Ľ���  
        mPaint.setTextSize(FONT_SIZE);  
        mPaint.setTextAlign(Paint.Align.CENTER);//�����־���
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);  
          
        if(mSwitchStatus == SWITCH_OFF)  
        {  
        	Log.d(tag, "SWITCH_OFF");
            drawBitmap(canvas, null, null, mSwitch_off);  
            drawBitmap(canvas, null, null, mSwitch_thumb);  
            mPaint.setColor(Color.rgb(105, 105, 105));  
            canvas.translate(mSwitch_thumb.getWidth(), 0);  
            canvas.drawText(mOffText, 0, 20, mPaint);  
//            BaseActivity.isDisturb = true;//��ʾ����  ���Խ�����Ϣģʽ
            BaseActivity.setDisStatus(SWITCH_OFF);
			LogUtil.p(tag, "������Ϣģʽ");
        }  
        else if(mSwitchStatus == SWITCH_ON)  
        {  
        	Log.d(tag, "SWITCH_ON");
            drawBitmap(canvas, null, null, mSwitch_on);  
            int count = canvas.save();  
            canvas.translate(mSwitch_on.getWidth() - mSwitch_thumb.getWidth(), 0);  
            drawBitmap(canvas, null, null, mSwitch_thumb);  
            mPaint.setColor(Color.WHITE);  
            canvas.restoreToCount(count);  
            canvas.drawText(mOnText, 17, 20, mPaint);  
//            BaseActivity.isDisturb = false;//��ʾ���� ����ģʽ
            BaseActivity.setDisStatus(SWITCH_ON);
			LogUtil.p(tag, "����ģʽ");
        }  
        else //SWITCH_SCROLING  
        {  
            mSwitchStatus = mDstX > 35 ? SWITCH_ON : SWITCH_OFF;  
            drawBitmap(canvas, new Rect(0, 0, mDstX, mBmpHeight), new Rect(0, 0, (int)mDstX, mBmpHeight), mSwitch_on);  
            mPaint.setColor(Color.WHITE);  
            canvas.drawText(mOnText, 17, 20, mPaint);  
  
            int count = canvas.save();  
            canvas.translate(mDstX, 0);  
            drawBitmap(canvas, new Rect(mDstX, 0, mBmpWidth, mBmpHeight),  
                          new Rect(0, 0, mBmpWidth - mDstX, mBmpHeight), mSwitch_off);  
            canvas.restoreToCount(count);  
  
            count = canvas.save();  
            canvas.clipRect(mDstX, 0, mBmpWidth, mBmpHeight);  
            canvas.translate(mThumbWidth, 0);  
            mPaint.setColor(Color.rgb(105, 105, 105));
            canvas.drawText(mOffText, 0, 20, mPaint);  
            canvas.restoreToCount(count);  
  
            count = canvas.save();  
            canvas.translate(mDstX - mThumbWidth / 2, 0);  
            drawBitmap(canvas, null, null, mSwitch_thumb);  
            canvas.restoreToCount(count);  
        }  
  
    }  
  
    public void drawBitmap(Canvas canvas, Rect src, Rect dst, Bitmap bitmap)  
    {  
        dst = (dst == null ? new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()) : dst);  
        Paint paint = new Paint();  
        canvas.drawBitmap(bitmap, src, dst, paint);  
    }  
  
    /** 
     * AnimationTransRunnable ������������ʹ�õ��߳� 
     */  
    private class AnimationTransRunnable implements Runnable  
    {  
        private int srcX, dstX;  
        private int duration;  
  
        /** 
         * �������� 
         * @param srcX ������ʼ�� 
         * @param dstX ������ֹ�� 
         * @param duration �Ƿ���ö�����1���ã�0������ 
         */  
        public AnimationTransRunnable(float srcX, float dstX, final int duration)  
        {  
            this.srcX = (int)srcX;  
            this.dstX = (int)dstX;  
            this.duration = duration;  
        }  
  
        @Override  
        public void run()   
        {  
            final int patch = (dstX > srcX ? 5 : -5);  
            if(duration == 0)  
            {  
                SlideSwitch.this.mSwitchStatus = SWITCH_SCROLING;  
                SlideSwitch.this.postInvalidate();  
            }  
            else  
            {  
                Log.d(TAG, "start Animation: [ " + srcX + " , " + dstX + " ]");  
                int x = srcX + patch;  
                while (Math.abs(x-dstX) > 5)   
                {  
                    mDstX = x;  
                    SlideSwitch.this.mSwitchStatus = SWITCH_SCROLING;  
                    SlideSwitch.this.postInvalidate();  
                    x += patch;  
                    try   
                    {  
                        Thread.sleep(10);  
                    }   
                    catch (InterruptedException e)  
                    {  
                        e.printStackTrace();  
                    }  
                }  
                mDstX = dstX;  
                SlideSwitch.this.mSwitchStatus = mDstX > 35 ? SWITCH_ON : SWITCH_OFF;  
                SlideSwitch.this.postInvalidate();  
            }  
        }  
  
    }  
  
    public static interface OnSwitchChangedListener  
    {  
        /** 
         * ״̬�ı� �ص����� 
         * @param status  SWITCH_ON��ʾ�� SWITCH_OFF��ʾ�ر� 
         */  
        public abstract void onSwitchChanged(SlideSwitch obj, int status);  
    }  
  
}  
