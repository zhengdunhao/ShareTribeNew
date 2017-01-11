/**
 * 
 */
package com.atm.chatonline.bbs.bean;

import com.atm.chatonline.bbs.util.LogUtil;

import android.graphics.drawable.Drawable;

/**
 * @�� com.atm.charonline.bbs.bean ---user
 * @���� ���������û�
 * @���� ��С��
 * @ʱ�� 2015-11-2
 * 
 */
public class User {
	private String userId; //�û�ID
	private String nickname;   //�������ǳƣ�15��
	private String sex;   //�û��Ա�15��
    private String headImagePath; //ͷ��·��
	private String dName; //ϵ��
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getdName() {
		return dName;
	}
	public void setdName(String dName) {
		this.dName = dName;
	}
	private Drawable HeadImage;
	public Drawable getHeadImage() {
		return HeadImage;
	}
	public void setHeadImage(Drawable headImage) {
		HeadImage = headImage;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
		LogUtil.d(userId);
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getHeadImagePath() {
		return headImagePath;
	}
	public void setHeadImagePath(String headImagePath) {
		this.headImagePath = headImagePath;
	}
}
