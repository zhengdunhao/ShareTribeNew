package com.atm.chatonline.bbs.commom;

import java.io.Serializable;

public class NewMessage implements Serializable{
	
	private int sum;//����Ϣ������
	private int applySum;//������Ϣ����
	private int replySum;//@�ҵ���Ϣ����
	private int systemSum;//ϵͳ��Ϣ����
	
	public NewMessage(){
		
	}
	
	
	public NewMessage(int sum, int applySum, int replySum, int systemSum) {
		super();
		this.sum = sum;
		this.applySum = applySum;
		this.replySum = replySum;
		this.systemSum = systemSum;
	}


	public void setData(String key,int sum){
		switch (key) {
		case "0":
			setApplySum(sum);
			break;
		case "1":
			setReplySum(sum);
			break;
		case "2":
			setSystemSum(sum);
			break;
		}
	}
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}
	public int getApplySum() {
		return applySum;
	}
	public void setApplySum(int applySum) {
		this.applySum = applySum;
	}
	public int getReplySum() {
		return replySum;
	}
	public void setReplySum(int replySum) {
		this.replySum = replySum;
	}
	public int getSystemSum() {
		return systemSum;
	}
	public void setSystemSum(int systemSum) {
		this.systemSum = systemSum;
	}


}
