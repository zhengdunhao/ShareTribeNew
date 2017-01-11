package com.atm.chatonline.share;
/**
 * �������ڰ�Ҫ�������ҳ����΢��
 * @author Jackbing
 * 2016.4.12
 */
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.sax.StartElementListener;
import android.util.Log;
import android.widget.Toast;

import com.atm.chatonline.bbs.commom.UriAPI;
import com.atm.chatonline.chat.util.FileUtil;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

public class WebPageShare {
	private static IWXAPI api;
	private String url,title,desc;
	private Bitmap thumb;
	private static Activity context;
	private final String tag="TAG";
	public WebPageShare(Activity context,String url,String title,String desc,Bitmap thumb){
		api = WXAPIFactory.createWXAPI(context, UriAPI.APP_ID);
		api.registerApp(UriAPI.APP_ID);
		//api.handleIntent(context.getIntent(), this);
		this.context=context;
		this.thumb=thumb;
		this.title=title;
		this.url=url;
		this.desc=desc;
	}

	
//	
//	public static Activity getActivitys(){
//		if(context==null){
//			return null;
//		}
//		return context;
//	}
//	
//	public static IWXAPI getWXAPI(){
//		if(api==null){
//			api = WXAPIFactory.createWXAPI(context, "wx49dfff6ceb8cbbba",false);
//			api.registerApp("wx49dfff6ceb8cbbba");
//			return api;
//		}
//		return api; 
//	}
	
	/**
	 * �ж��Ƿ�װ΢��
	 * @return
	 */
	public boolean isInstalled(){
		if(api.isWXAppInstalled()){
			//��δ��װ΢��
			return true;
		}
		return false;
	}
	
	/**
	 * ������ҳ��΢��
	 * @param flag 1��ʾ����΢������Ȧ 0��ʾ���������
	 */
	public void shareToWx(int flag){
		
		WXWebpageObject webpage = new WXWebpageObject();
		//��ҳ��ַ
	    webpage.webpageUrl = url; 
	    WXMediaMessage msg = new WXMediaMessage(webpage);  
	    //��ҳ���� 
        msg.title = title; 
        //��ҳ���ݣ�ǰ30���֣�
        msg.description = desc; 
        //����ͼ
        msg.thumbData=FileUtil.compressBitmapToByte(thumb, true,context);
        //���͵�΢��
        SendMessageToWX.Req req = new SendMessageToWX.Req();  
        req.transaction =buildTransaction("webpage");
        req.message = msg;  
        req.scene = flag;  
        api.sendReq(req); 
		
	}
	
	
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}



	

//	@Override
//	public void onReq(BaseReq arg0) {
//		// TODO �Զ����ɵķ������
//		
//	}
//
//	/**
//	 * ������ϵĻص�
//	 */
//	@Override
//	public void onResp(BaseResp resp) {
//		
//		 switch (resp.errCode) {  
//	        case BaseResp.ErrCode.ERR_OK:  
//	           Toast.makeText(context, "����ɹ�", Toast.LENGTH_LONG).show(); 
//	           Log.i(tag, "����ɹ�");
//	            break;  
//	        case BaseResp.ErrCode.ERR_USER_CANCEL:  
//	        	Toast.makeText(context, "����ȡ��", Toast.LENGTH_LONG).show();
//	        	Log.i(tag, "����ȡ��");
//	            break;  
//	        case BaseResp.ErrCode.ERR_AUTH_DENIED:  
//	        	Toast.makeText(context, "�����ܾ�", Toast.LENGTH_LONG).show();
//	        	Log.i(tag, "�����ܾ�");
//	            break;  
//	        default:  
//	        	Toast.makeText(context, "����δ֪����", Toast.LENGTH_LONG).show(); 
//	        	Log.i(tag, "����δ֪����");
//	            break;  
//	        }  
//		
//		
//	}
	
}
