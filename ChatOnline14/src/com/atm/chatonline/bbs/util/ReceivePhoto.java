package com.atm.chatonline.bbs.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.atm.chatonline.bbs.commom.UriAPI;

public class ReceivePhoto {
	private String tag ="ReceivePhoto";
	
	private String address;
	private Drawable d;
	private URL url;
	private String subURL = UriAPI.SUB_URL;
	private HttpURLConnection con;
	private InputStream is;

	@SuppressWarnings("deprecation")
	public ReceivePhoto(String address){
		this.address = subURL + address;	
		LogUtil.d("ͼƬ���棺�ж��Ƿ��л��棿");
		if(ImageFileCacheUtils.getImage(address)==null) {
			LogUtil.d("ͼƬ���棺û��ͼƬ��ȥ����ͼƬ");
			sendRequestWithHttpURLConnection();
		}else {
			LogUtil.d("ͼƬ���棺��ͼƬ��ȥ������ͼƬ");
			d = new BitmapDrawable(ImageFileCacheUtils.getImage(address));
		}
	}
//	public ReceivePhoto(String address){
//		this.address = subURL + address;		
//		sendRequestWithHttpURLConnection();
//	}
	
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
					Bitmap bm = BitmapFactory.decodeStream(is, null, options);
					d = new BitmapDrawable(bm);
//				    d = Drawable.createFromStream(is,"btn_msg.png");
//				    d = zoomDrawable(Drawable.createFromStream(is,"btn_msg.png"),800,480);//��һ��
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
	
	//����ӣ���ΪͼƬ����,�������ԣ���������
//	private Bitmap drawableToBitmap(Drawable drawable) // drawable ת���� bitmap   
//    {  
//              int width = drawable.getIntrinsicWidth();   // ȡ drawable �ĳ���   
//              int height = drawable.getIntrinsicHeight();  
//              Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565;    // ȡ drawable ����ɫ��ʽ   
//              Bitmap bitmap = Bitmap.createBitmap(width, height, config);     // ������Ӧ bitmap   
//              Canvas canvas = new Canvas(bitmap);         // ������Ӧ bitmap �Ļ���   
//              drawable.setBounds(0, 0, width, height);  
//              drawable.draw(canvas);      // �� drawable ���ݻ���������   
//              return bitmap;  
//    } 
//
//	private Drawable zoomDrawable(Drawable drawable, int w, int h)  
//    {  
//              int width = drawable.getIntrinsicWidth();  
//              int height= drawable.getIntrinsicHeight();  
//              Bitmap oldbmp = drawableToBitmap(drawable); // drawable ת���� bitmap   
//              Matrix matrix = new Matrix();   // ��������ͼƬ�õ� Matrix ����   
//              float scaleWidth = ((float)w / width);   // �������ű���   
//              float scaleHeight = ((float)h / height);  
//              matrix.postScale(scaleWidth, scaleHeight);         // �������ű���   
//              Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);       // �����µ� bitmap ���������Ƕ�ԭ bitmap �����ź��ͼ  
//              
//              return new BitmapDrawable(newbmp);       // �� bitmap ת���� drawable ������   
//    }  

	
	public Drawable getPhoto(){		
		Log.d("Receive�����������","��+4");
		return d;
	}
}
