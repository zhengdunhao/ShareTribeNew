/**
 * 
 */
package com.atm.chatonline.recuit.bean;

/**
 * @�� com.atm.charonline.recuit ---Recuit
 * @���� 
 * @���� ��С��
 * @ʱ�� 2015-9-19
 * 
 */
public class Recuit {
	
	private String reInfoId="";//����ID
	private String reTypeName=""; //��Ƹ����
	private String woTypeName="";//��������
	private String workAddress="";//�����ص�
	private String salary="";//н��
	private String publishTime="";//����ʱ��
	
	public String getReTypeName() {
		return reTypeName;
	}
	public void setReTypeName(String reTypeName) {
		this.reTypeName = reTypeName;
	}
	public String getWoTypeName() {
		return woTypeName;
	}
	public void setWoTypeName(String woTypeName) {
		this.woTypeName = woTypeName;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getReInfoId() {
		return reInfoId;
	}
	public void setReInfoId(String reInfoId) {
		this.reInfoId = reInfoId;
	}
	public String getWorkAddress() {
		return workAddress;
	}
	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
}
