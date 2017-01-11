package com.atm.chatonline.bbs.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ������������û��������Ϣ�Ƿ�Ϸ�
 * @author Jackbing
 *
 */
public class CkeckRegisterMessage {
	
	private boolean flag=false;
	private String userName,pwd,comfirmPwd,
	userEmail;
	private int errorCode;
	public CkeckRegisterMessage(){}
	
	public CkeckRegisterMessage(String userName, String pwd, String comfirmPwd
			) {
		super();
		this.userName = userName;
		this.pwd = pwd;
		this.comfirmPwd = comfirmPwd;
	}
	//����������ʽ��֤�����Ƿ�Ϸ�
	public boolean checkEmail(String userEmail)
	{
		this.userEmail=userEmail;
		String str="^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(userEmail);
		return m.matches();
	}
	//�ж��û�������ȷ�����볤�ȣ��û��������Ƿ�Ϸ��������ȷ�������Ƿ���ͬ
	public int returnNumber()
	{
		//�ж������Ƿ�Ϊ��
		if(userName.equals("")&&!pwd.equals("")&&!comfirmPwd.equals("")&&!userEmail.equals(""))
		{
			errorCode=101;
		}else if(true)
		{
			
		}
		return errorCode;
	}
	
}
