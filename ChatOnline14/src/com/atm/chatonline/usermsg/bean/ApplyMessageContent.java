package com.atm.chatonline.usermsg.bean;
/**
 * ʵ������@�ҵ���Ϣʵ��
 */
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

@SuppressWarnings("serial")
public class ApplyMessageContent implements Serializable{

	private int floorId;//����¥��id
	private String replyTime;//����ʱ��
	private String replyId;//�����˵�id
	private String nickname;//�������ǳ�
	private String avatar;//�����˵�ͷ��·��
	private String essayId;//����id
	private String essayTitle;//���ӱ���
	private String userId;//������id
	public int getFloorId() {
		return floorId;
	}
	public void setFloorId(int floorId) {
		this.floorId = floorId;
	}
	public String getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}
	public String getReplyId() {
		return replyId;
	}
	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getEssayId() {
		return essayId;
	}
	public void setEssayId(String essayId) {
		this.essayId = essayId;
	}
	public String getEssayTitle() {
		return essayTitle;
	}
	public void setEssayTitle(String essayTitle) {
		this.essayTitle = essayTitle;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	transient private Bitmap HeadImage;

	public Bitmap getHeadImage() {
		return HeadImage;
	}
	public void setHeadImage(Bitmap headImage) {
		HeadImage = headImage;
	}
	
	
}
