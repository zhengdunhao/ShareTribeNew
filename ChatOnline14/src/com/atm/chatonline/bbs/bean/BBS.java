package com.atm.chatonline.bbs.bean;

import java.util.Date;
import java.sql.Timestamp;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

/**
 * @������ʾ ���������б��У��������ӵĴ�����Ϣ����ʾ������ģ�
 * @author Jiong
 * @�޸�ʱ�� 2015��07��29�� 19:48:59
 */
public class BBS implements java.io.Serializable {
	
	private String clickGoodNum; //������
	private String publishTime; //����ʱ��
	private String headImagePath; //ͷ��·��
	private String publisherId; //�������˺�
	private String essayId;   //���ӱ�ţ���Ϊ���ȥ��ȡȫ�����ݵı�ʶ��
	private String nickname;   //�������ǳƣ�15��
	private String replyNum;  //�ظ���
	private String labName;    //��ǩ����100��  ʾ������ǩ1*#��ǩ2 ���ָ�����*#��
	private String someContent;//�����������ݣ�50��
	private String title;      //���ӱ��� ��100��
	private String essayType;  //�������� ��20��
	private String labColor;	//��ǩ��ɫ��100��ʾ������ɫ1*��ɫ2���ָ�����*��
	transient private String department; //ϵ�����ƣ�20���������ⲻ��ʾ�������͸���,��ɾ����
	private String flag = "a";
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	transient private Drawable HeadImage;

	private String [] labName0 = {" "," "," "};
	private int [] labColor0 = {Color.parseColor("#EED5D2"),Color.parseColor("#EED5D2"),Color.parseColor("#EED5D2")};
	
	
	public String[] getLabName0() {
		return labName0;
	}

	public void setLabName0(String[] labName0) {
		this.labName0 = labName0;
	}

	public int[] getLabColor0() {
		return labColor0;
	}

	public void setLabColor0(int[] color) {
		this.labColor0 = color;
	}


	public Drawable getHeadImage() {
		return HeadImage;
	}

	public void setHeadImage(Drawable headImage) {
		HeadImage = headImage;
	}

	/**
	 * 
	 */
	public BBS() {
	}
	
	/**
	 * @param essayId
	 * @param essayType
	 * @param title
	 * @param labName
	 * @param someContent
	 * @param nickname
	 * @param publishTime
	 * @param clickGoodNum
	 * @param replyNum
	 */

	public String getEssayId() {
		return essayId;
	}
	public void setEssayId(String essayId) {
		this.essayId = essayId;
	}
	public String getEssayType() {
		return essayType;
	}
	public void setEssayType(String essayType) {
		this.essayType = essayType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLabName() {
		return labName;
	}
	public void setLabName(String labName) {
		this.labName = labName;
	}
	public String getSomeContent() {
		return someContent;
	}
	public void setSomeContent(String someContent) {
		this.someContent = someContent;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickName) {
		this.nickname = nickName;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getClickGoodNum() {
		return clickGoodNum;
	}
	public void setClickGoodNum(String clickGoodNum) {
		this.clickGoodNum = clickGoodNum;
	}
	public String getReplyNum() {
		return replyNum;
	}
	public void setReplyNum(String collectNum) {
		this.replyNum = collectNum;
	}
	public String getPublisherId() {
		return publisherId;
	}
	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}

	public String getHeadImagePath() {
		return headImagePath;
	}

	public void setHeadImagePath(String headImagePath) {
		this.headImagePath = headImagePath;
	}

	public String getLabColor() {
		return labColor;
	}

	public void setLabColor(String labColor) {
		this.labColor = labColor;
	}
	
	
	
	
}
