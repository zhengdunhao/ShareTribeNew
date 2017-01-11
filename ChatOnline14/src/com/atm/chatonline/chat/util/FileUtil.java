package com.atm.chatonline.chat.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class FileUtil {
	private static String tag = "chat-FileUtil";
	public final static String GROUP_HEAD_PATH=Environment.getExternalStorageDirectory()+"/ATM/chat/group/grouphead";
	public final static String APP_PATH=Environment.getExternalStorageDirectory()+"/ATM/chat";
	public final static String USER_HEAD=Environment.getExternalStorageDirectory()+"/ATM/userhead";
	public final static String PIC_PATH=Environment.getExternalStorageDirectory()+"/ATM/image";
	public static final int IMAGE_SIZE=32768;
	/**
	 * ��userid,groupId���½�Ŀ¼���ļ�
	 * @param userId
	 * @param groupId
	 * @return
	 */
	public static File createFile(String userId,int groupId){
		String filepath=APP_PATH+"/group"+"/"+userId;
		File fileparent=new File(filepath);
		if(fileparent.exists()==false){
			fileparent.mkdirs();
		}
		File file=new File(filepath+"/"+groupId+".jpg");
		try {
			file.createNewFile();
		} catch (IOException e) {
			
			e.printStackTrace();
			return null;
		}
		return file;
	}
	
	public static File createFriendFile(String userID,String friendID){
		String filepath=APP_PATH+"/friend"+"/"+userID;
		File fileparent = new File(filepath);
		if(fileparent.exists()==false){
			fileparent.mkdirs();
		}
		File file = new File(filepath+"/"+friendID+".jpg");
		try{
			file.createNewFile();
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
		return file;
	}
	//�û����췢��ͼƬ�����·��
	public static File createImgFile(String userID){
		String nowTime = TimeUtil.getAbsoluteTime2();//��ȡ��ǰʱ��
		String filepath=PIC_PATH+"/"+userID;
		File fileparent = new File(filepath);
		if(fileparent.exists()==false){
			fileparent.mkdirs();
		}
		File file = new File(filepath+"/"+nowTime+".jpg");
		try{
			file.createNewFile();
		}catch(IOException e){
			e.printStackTrace();
		}
		return file;
	}
	
	//�û������յ�ͼƬ�����·��
	public static File createFromImg(String friendID,String name){
		String filepath=PIC_PATH+"/"+friendID;
		File fileparent = new File(filepath);
		if(fileparent.exists()==false){
			fileparent.mkdirs();//�жϸ��ļ��д��ڲ��������ڴ����µ�
		}
		File file = new File(filepath+"/"+name);
		try{
			file.createNewFile();
		}catch(IOException e){
			e.printStackTrace();
		}
		return file;
	}
	
	//���ļ�д��SD����
	public static boolean writeFile(ContentResolver cr,File file,Uri uri){
		boolean result=true;
		try{
			FileOutputStream fout = new FileOutputStream(file);
			Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
			bitmap.compress(Bitmap.CompressFormat.JPEG,100,fout);
			try{
				fout.flush();
				fout.close();
			}catch(IOException e){
				e.printStackTrace();
				result = false;
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
			result = false;
		}catch(Exception e){
			
		}
		return result;
	}
	
	/**
	 * ȥ��sd���е��ļ�
	 * @param filepath��ָ�ļ�·�����ļ���
	 */
	public static Bitmap getBitmap(String filepath){
		try{
			Bitmap bm=BitmapFactory.decodeFile(filepath);
			return bm;
		}catch(OutOfMemoryError err){
			return null;
		}
		
		
		
	}
	
	/**
	 * ����Դ��ȡbitmap
	 * @param res
	 * @param id
	 * @return
	 */
	public static Bitmap getBitmapFromRes(Resources res,int id){
		Bitmap bm=BitmapFactory.decodeResource(res, id);
		return bm;
	}
	
	/**
	 * �ж��ļ��Ƿ���ڣ��Ӷ��Ƿ�Ҫ��������������û���ͷ��
	 * */
	public static boolean isFile(String userID){
		String filepath=USER_HEAD+"/"+userID+".jpg";
		File fileparent = new File(filepath);
		if(fileparent.exists()==false){
			Log.i(tag, "�û�ͷ�񲻴���");
			return false;
		}
		Log.i(tag, "�û�ͷ�����");
		return true;
	}
	
	/**
	 * �����û�ͷ��
	 * */
	public static File createUserHead(String userID){
		String filepath=USER_HEAD;
		File fileparent = new File(filepath);
		if(fileparent.exists()==false){
			fileparent.mkdirs();
		}
		File file = new File(filepath+"/"+userID+".jpg");
		try{
			file.createNewFile();
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
		return file;
	}
	
	
	/**
	 * bitmapתbyte[]
	 * @param bitmap
	 * @return
	 */
	public static byte[] BitmapToBytes(Bitmap bitmap,boolean flag){
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		if(flag){
			bitmap.recycle();
		}
		byte[] result=baos.toByteArray();
		try {
			baos.close();
		} catch (Exception e) {
			return null;
		}
		return result;
	}
	
	public static void saveBitmap(File file,Bitmap bm){
		FileOutputStream fout=null;
		try {
			fout=new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.PNG, 100, fout);
		} catch (FileNotFoundException e) {
			Log.i("fileutil", "����ʧ��");
			e.printStackTrace();
		}
	
		try {
			fout.flush();
			fout.close();
		} catch (IOException e) {
			Log.i("fileutil", "�ر�ʧ��");
			e.printStackTrace();
		}
	}
	
	public static Bitmap convertToBitmap(String path,int w,int h){
		 BitmapFactory.Options opts = new BitmapFactory.Options(); 
	     // ����Ϊtureֻ��ȡͼƬ��С 
	      opts.inJustDecodeBounds = true; 
	        opts.inPreferredConfig = Bitmap.Config.ARGB_8888; 
	        // ����Ϊ�� 
	       BitmapFactory.decodeFile(path, opts); 
	      int width = opts.outWidth; 
	       int height = opts.outHeight; 
	       float scaleWidth = 0.f, scaleHeight = 0.f; 
	       if (width > w || height > h) { 
	          // ���� f
	          scaleWidth = ((float) width) / w; 
	          scaleHeight = ((float) height) / h; 
	      } 
	     opts.inJustDecodeBounds = false; 
	       float scale = Math.max(scaleWidth, scaleHeight); 
	        opts.inSampleSize = (int)scale; 
	        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts)); 
	       return Bitmap.createScaledBitmap(weak.get(), w, h, true); 
	}
	
	
	
	public static byte[] makeFileToByte(String fileFath) throws IOException {
		File file = new File(fileFath);
		if(file.exists()){
			Log.i("fileutil", "1---�ļ�����");
		}
		Log.i("fileutil", "2---�ҵ���ͼƬ��·��");
		FileInputStream fis = new FileInputStream(file);
		Log.i("fileutil", "3---�ɹ�������ͼƬ");
		int length = (int) file.length();
		byte[] bytes = new byte[length];
		int temp = 0;
		int index = 0;
		while (true) {
			index = fis.read(bytes, temp, length - temp);
			if (index <= 0)
				break;
			temp += index;
		}
		fis.close();
		return bytes;
	}
	
	public static Bitmap ByteToBitmap(byte[] bytes){ 
       if(bytes.length!=0){
    	   return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
       }else{
    	   return null;
       }
   
    } 
	
	
	public static byte[] compressBitmapToByte(Bitmap bitmap,boolean flag,Context context){
		
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		int options=100;
		bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
		
		while(baos.toByteArray().length>IMAGE_SIZE&&options!=10){
			baos.reset();
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
			options-=10;
		}
		
		if(flag){
			bitmap.recycle();
		}
		byte[] result=baos.toByteArray();
		try {
			baos.close();
		} catch (Exception e) {
			return null;
		}
		return result;
	}
	
	
}
