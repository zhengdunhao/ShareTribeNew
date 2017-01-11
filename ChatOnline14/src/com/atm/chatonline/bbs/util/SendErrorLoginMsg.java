package com.atm.chatonline.bbs.util;
/**
 * @author Jackbing
 * �����û����������룬��������ȸ�����������÷��ؽ��
 */
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.atm.chatonline.bbs.commom.UriAPI;

public class SendErrorLoginMsg {
	private String respCode="����������Ӧ";
	private ChangeJson changeJson;
	private JSONObject jsonObject;
	/**
	 *������������������
	 * @param Email
	 * @return
	 */
	public String sendEmailMsg(String Email){
		try {
			JSONArray jsonarray=new ChangeJson().returnOneData("email", Email);
			ConToServer t=new ConToServer(jsonarray,UriAPI.FIND_USERNAME); 
			t.join();
			t.run();
			respCode=t.returnRespCode();
			
			return respCode;
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
		return respCode;
	}
	
	/**
	 * �����û�����������
	 * @param userName
	 * @return
	 */
	public JSONObject sendUserName(String userName){
		JSONArray jsonarray;
		try {
			jsonarray = new ChangeJson().returnOneData("idOrEmail", userName);
			ConToServer t=new ConToServer(jsonarray,UriAPI.FORGET_PASSWORD); 
			t.join();
			t.run();
			respCode=t.returnRespCode();
			changeJson=t.returnChangeJson();
			return changeJson.getJSONObject();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
		return changeJson.getJSONObject();
	}
	
	public String sendPassword(String userId,String password){
		JSONArray jsonarray;
		try {
			Log.i("updatePassword", "userId:"+userId+", password:"+password);
			jsonarray = new ChangeJson().returnTwoData("userId", userId,"password",password);
			ConToServer t=new ConToServer(jsonarray,UriAPI.UPDATE_PASSWORD); 
			t.join();
			t.run();
			respCode=t.returnRespCode();
			return respCode;
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
		return respCode;
	}
	public ChangeJson getChangeJson(){
		return changeJson;
	}
}
