package com.atm.chatonline.bbs.util;

import org.apache.http.NameValuePair;

public class ObjectNameValuePair implements NameValuePair {
	String name;
	Object o;
	public ObjectNameValuePair(String name,Object o){
		this.o=o;
		this.name=name;
	}
	@Override
	public String getName() {
		// TODO �Զ����ɵķ������
		return name;
	}

	@Override
	public String getValue() {
		// TODO �Զ����ɵķ������
		return null;
	}
	
	public Object getObject(){
		return o;
	}

}
