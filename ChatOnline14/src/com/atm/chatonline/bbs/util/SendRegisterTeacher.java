package com.atm.chatonline.bbs.util;
/**
 * �ù��������ڽ��ս�ʦע����洫������Ϣ�������˴�����Ϣ
 * 2015.7.24��atm--��
 * 
 * ������һЩ�޸ģ�ConToServer t=new ConToServer(sendJsonarray,UriAPI.USER_REGISTER);
 * -֣
 */

import org.json.JSONArray;
import org.json.JSONException;

import com.atm.chatonline.bbs.commom.UriAPI;




public class SendRegisterTeacher {
	private String respCode;
	private String userName,pwd,comfirmPwd,
	userSchool,userDept,userEmail,flag,schoolNum,schoolPwd;
	private int comfirmNumber;
	ChangeJson changeJson;
	//��װ��ʦ�������֤��Ϣ
	public SendRegisterTeacher(String userEmail) {
		super();
		this.userEmail = userEmail;
	}
	//��װ��ʦע�����Ϣ
	public SendRegisterTeacher(String flag,String userName, String pwd, 
			String userSchool, String userDept,
			String userEmail) {
		super();
		this.flag = flag;
		this.userName = userName;
		this.pwd = pwd;
		this.userSchool = userSchool;
		this.userDept = userDept;
		this.userEmail = userEmail;
	}
	//��װ��ʦ��֤����Ϣ
	public SendRegisterTeacher(String flag,String userName, String schoolNum,String schoolPwd){
		super();
		this.flag = flag;
		this.userName = userName;
		this.schoolNum = schoolNum;
		this.schoolPwd = schoolPwd;
	}
	//trueΪע��ɹ���falseΪע��ʧ��
	public String checkRegister() throws InterruptedException
	{
		JSONArray sendJsonarray=new ChangeJson(flag,userName,pwd,userSchool,userDept,userEmail).returnTeacherJsonarray();
		ConToServer t=new ConToServer(sendJsonarray,UriAPI.USER_REGISTER);
		t.run();
		respCode=t.returnRespCode();
		return respCode;
	}
	//���������Ƿ���Ч���ַ�����ͨ�������߳��������ͨ�ţ���
	public String checkEmail()
	{
		try {
			
			JSONArray sendJsonArray=new ChangeJson(userEmail).returnEmailarray();
			ConToServer t=new ConToServer(sendJsonArray,UriAPI.CHECK_EMAIL);
			t.join();
			t.run();
			respCode=t.returnRespCode();
			changeJson=t.returnChangeJson();
			return respCode;
		} catch (JSONException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
		
		return respCode;
	}
	
	//���ؽ�ʦ����֤��Ϣ
	public String checkTeacher() throws InterruptedException{
			JSONArray sendJsonArray = new ChangeJson(flag,userName,schoolNum,schoolPwd).returnTeacher();
			ConToServer t = new ConToServer(sendJsonArray,UriAPI.CONFIRM_IDENTITY);
			t.join();
			t.run();
			respCode = t.returnRespCode();
			return respCode;
	}
	
	//�����֪���������û���ע��
	public void reqExitConfig()throws InterruptedException{
		JSONArray sendJsonArray = new ChangeJson(flag,userName,schoolNum,schoolPwd).returnExitConfig();
		ConToServer t = new ConToServer(sendJsonArray,UriAPI.REQUEST_CONFIG);
		t.join();
		t.run();
	}
	public ChangeJson getChangeJson(){
		return changeJson;
	}
}
