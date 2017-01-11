package com.atm.chatonline.bbs.util;

/**
 * ���������޸�ConToServer t=new ConToServer(sendJsonArray,UriAPI.USER_LOGIN);
 * -֣
 * */
import org.json.JSONArray;

import com.atm.chatonline.bbs.commom.UriAPI;

public class SendLoginInfo{
	private String username,pwd;
	private String respCode;
	private ConToServer t;
	private String cookie;
	//�Զ��幹�캯��
	public SendLoginInfo(String username,String pwd)
	{
		this.username=username;
		this.pwd=pwd;
	}

	//��¼��֤����½�ɹ�,flagΪtrue������Ϊfalse
	public String checkLoginInfo() throws InterruptedException{
		
		JSONArray sendJsonArray=new ChangeJson(username,null).returnLoginJsonarray();
		t=new ConToServer(sendJsonArray,UriAPI.USER_LOGIN);
		t.join();
		t.run();
		cookie = t.getCookie();
		respCode=t.returnRespCode();
		return respCode;
	}
	public String getCookie() {
		return cookie;
	}
}
