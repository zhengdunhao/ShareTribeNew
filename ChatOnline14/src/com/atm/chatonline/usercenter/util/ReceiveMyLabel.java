package com.atm.chatonline.usercenter.util;

import com.atm.chatonline.bbs.util.BBSConnectNet;

public class ReceiveMyLabel extends BBSConnectNet{
	
	public ReceiveMyLabel(){}
	public ReceiveMyLabel(String relativePath) {
		super(relativePath);
	}

	@Override
	public void setCookie(String cookie) {
		// TODO �Զ����ɵķ������
		super.setCookie(cookie);
	}

	@Override
	public void Connect(String URL, int METHOD) {
		// TODO �Զ����ɵķ������
		super.Connect(URL, METHOD);
	}

	@Override
	public String getResponse() {
		// TODO �Զ����ɵķ������
		return super.getResponse();
	}
	
	
	
}
