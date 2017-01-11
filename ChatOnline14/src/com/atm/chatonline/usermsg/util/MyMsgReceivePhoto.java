package com.atm.chatonline.usermsg.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.afinal.simplecache.ACache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.chat.util.FileUtil;



public class MyMsgReceivePhoto {

	private String tag ="MyMsgReceivePhoto";
	
	private String address;
	private URL url;
	private String subURL = UriAPI.SUB_URL;
	private HttpURLConnection con;
	private InputStream is;
	private Bitmap bm; 
	private ACache mCahce;
	private String key;
	
	public MyMsgReceivePhoto(){}
	
	/**
	 * 
	 * @param cacheManager	���������
	 * @param key		����ͷ���key,���ǰѷ����ߵ�id��Ϊkey
	 * @param address	ͷ����Ե�ַ
	 * @return	����ͷ��Drawable
	 */
	@SuppressWarnings("deprecation")
	public Bitmap getPhoto(ACache cacheManager,String key,String address){		
		this.address = subURL + address;
		this.mCahce=cacheManager;
		this.key=key;
		sendRequestWithHttpURLConnection();
		mCahce.put(key,bm);
//		if(mCahce.getAsBitmap(key)==null){
//			mCahce.put(key, bm);
////			cacheManager.addCache(new CacheData<byte[]>(key, FileUtil.BitmapToBytes(bm, false)));
////			d = new BitmapDrawable(bm);
//		}else{
//			mCahce.put(key, bm);
//			
////			d=new BitmapDrawable(FileUtil.ByteToBitmap((byte[])cacheManager.getCache(key).getData()));
////			//����ͷ��
////			cacheManager.addCache(new CacheData<byte[]>(key, FileUtil.BitmapToBytes(bm, false)));
//		}
		return bm;
	}
	
	
	
	private void sendRequestWithHttpURLConnection() {
		Thread thread1 =new Thread(new Runnable(){
			public void run(){
				try{

					url = new URL(address);
					con = (HttpURLConnection) url.openConnection();
					con.setRequestMethod("GET");
					is = con.getInputStream();
					BitmapFactory.Options options = new Options();
					options.inDither = false; //������ͼƬ��������
					options.inPreferredConfig = null;//������ѽ���������
					options.inSampleSize = 2;
					bm = BitmapFactory.decodeStream(is, null, options);
					
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		thread1.start();
		try {
			thread1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}
	}
	

}
