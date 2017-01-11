package com.atm.chatonline.usermsg.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class NotificationContent implements Serializable{

	private String title;//����
	private String content;//����
	private String time;//ʱ��
	
	public NotificationContent(String title, String content, String time) {
		super();
		this.title = title;
		this.content = content;
		this.time = time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
