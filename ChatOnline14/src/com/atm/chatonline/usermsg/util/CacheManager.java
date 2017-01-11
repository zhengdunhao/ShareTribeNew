package com.atm.chatonline.usermsg.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

public class CacheManager {

	//��volatile���εı�����ֵ�������ᱻ�����̻߳��棬
	//���жԸñ����Ķ�д����ֱ�Ӳ��������ڴ�,�Ӷ�ȷ������߳�����ȷ�Ĵ���ñ�����
	
	public volatile static CacheManager instance;
	public static Context mContext;
	
	public static void init(Context context){
		mContext=context;
	}
	public CacheManager(){}
	/**
     *˫����
	 * @return
	 */
	public static CacheManager getInstance(){
		if(instance==null) {
			synchronized (CacheManager.class) {
				if(instance==null){
					instance=new CacheManager();
				}
			}
		}
		return instance;
	}
	
	
	public void addCache(CacheData cacheData){
		if (cacheData == null) return;
		
		 ObjectOutputStream oos=null;
       try {
            File file = new File(mContext.getCacheDir(), cacheData.getKey());
            if (!file.exists()) {
                file.createNewFile();
            }
            oos= new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(cacheData);
           
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
        	if(oos!=null){
        		try {
					oos.close();
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
        	}
        }
	}
	
	@SuppressWarnings("unused")
	public CacheData getCache(String key){
		ObjectInputStream ois=null;
		try {
			System.out.println(mContext.getCacheDir().toString());
			File file=new File(mContext.getCacheDir(),key);
			
			if(file==null) {
				
				return null;
			}
		
			ois=new ObjectInputStream(new FileInputStream(file));
			
			CacheData cacheData=(CacheData) ois.readObject();
			
			
			System.out.println("�������ݻ�ȡ�ɹ�");
			return cacheData;
		} catch (Exception e) {
			System.out.println("��ȡ��������ʧ��");
			e.printStackTrace();
		}finally{
			if(ois!=null){
				try {
					ois.close();
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
}
