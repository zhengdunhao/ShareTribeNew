package com.atm.chatonline.schoolnews.util;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.atm.chatonline.schoolnews.ui.SchoolNewsActivity;

public class ImageHandler extends Handler{
	/**
     * ���������ʾ��View��
     */
	public static final int MSG_UPDATE_IMAGE = 1;
	/**
     * ������ͣ�ֲ���
     */
	public static final int MSG_KEEP_SILENT = 2;
	 /**
     * ����ָ��ֲ���
     */
	public static final int MSG_BREAK_SILENT = 3;
	 /**
     * ��¼���µ�ҳ�ţ����û��ֶ�����ʱ��Ҫ��¼��ҳ�ţ������ʹ�ֲ���ҳ�����
     * ���統ǰ����ڵ�һҳ������׼�����ŵ��ǵڶ�ҳ������ʱ���û���������ĩҳ��
     * ��Ӧ�ò��ŵ��ǵ�һҳ�������������ԭ���ĵڶ�ҳ���ţ����߼��������⡣
     */
	public static final int MSG_PAGE_CHANGED = 4;

    //�ֲ����ʱ��
	public static final long MSG_DELAY = 5000;
	private WeakReference<SchoolNewsActivity> weakReference;
	private int currentItem = 0;
	public ImageHandler(WeakReference<SchoolNewsActivity> weakReference2){
		weakReference = weakReference2;
	}
	public void handleMessage(Message msg){
		super.handleMessage(msg);
		SchoolNewsActivity activity = (SchoolNewsActivity) weakReference.get();
		if (activity==null){
            //Activity�Ѿ����գ������ٴ���UI��
            return ;
        }
        //�����Ϣ���в��Ƴ�δ���͵���Ϣ������Ҫ�Ǳ����ڸ��ӻ�������Ϣ�����ظ������⡣
        if (activity.imageHandler.hasMessages(MSG_UPDATE_IMAGE)){
            activity.imageHandler.removeMessages(MSG_UPDATE_IMAGE);
        }
        switch (msg.what) {
        case MSG_UPDATE_IMAGE:
            currentItem++;
            activity.viewPager.setCurrentItem(currentItem);
            //׼���´β���
            activity.imageHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
            break;
        case MSG_KEEP_SILENT:
            //ֻҪ��������Ϣ����ͣ��
            break;
        case MSG_BREAK_SILENT:
            activity.imageHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
            break;
        case MSG_PAGE_CHANGED:
            //��¼��ǰ��ҳ�ţ����ⲥ�ŵ�ʱ��ҳ����ʾ����ȷ��
            currentItem = msg.arg1;
            break;
        default:
            break;
        } 
	}
}
