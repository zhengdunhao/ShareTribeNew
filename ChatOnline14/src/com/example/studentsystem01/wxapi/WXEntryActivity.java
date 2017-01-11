package com.example.studentsystem01.wxapi;
/**
 * ��������΢�ŷ������Ļص���������������Ŀ����+.wxapi����Ϊ����İ�
 * ���Ҹ��������WXEntryActivity�������̳�Acivity ʵ��IWXAPIEventHandler����ʵ�ֻص�
 * @author Jackbing
 * 2016.4.13
 */
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.atm.chatonline.bbs.commom.UriAPI;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	
	private final  String tag="******";
	private IWXAPI api;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this,  
                UriAPI.APP_ID, false); 
		api.registerApp(UriAPI.APP_ID);
        api.handleIntent(getIntent(), this); 
	}

	@Override
	public void onReq(BaseReq rep) {
		 
		

	}

	@Override
	public void onResp(BaseResp resp) {
		switch (resp.errCode) {  
        case BaseResp.ErrCode.ERR_OK:  
           Toast.makeText(this, "����ɹ�", Toast.LENGTH_LONG).show(); 
           Log.i(tag, "����ɹ�");
            break;  
        case BaseResp.ErrCode.ERR_USER_CANCEL:  
        	Toast.makeText(this, "����ȡ��", Toast.LENGTH_LONG).show();
        	Log.i(tag, "����ȡ��");
            break;  
        case BaseResp.ErrCode.ERR_AUTH_DENIED:  
        	Toast.makeText(this, "�����ܾ�", Toast.LENGTH_LONG).show();
        	Log.i(tag, "�����ܾ�");
            break;  
        default:  
        	Toast.makeText(this, resp.errStr+resp.errCode, Toast.LENGTH_LONG).show(); 
        	Log.i(tag, resp.errStr+resp.errCode);
            break;  
        }  

		
		this.finish();
	}

}
