package com.atm.chatonline.schoolnews.bean;

import android.graphics.drawable.Drawable;

/**
 * ����ʵ����
 * @Description: ����ʵ����

 * @File: News.java

 * @Package com.atm.chatonline.schoolnews.bean

 * @Author zhengdunhao

 * @Date 2015-10-17 

 * @Version V1.0
 */
public class News {
	// ���ŵ�ID
	private String newsId;
	// ����������
	private String mainTitle;
	// ���Ÿ�����
	private String viceTitle;
	// �鿴����
	private String viewCount;
	//����ͼƬ·��
	private String newsImagePath;
  
	
	transient private Drawable NewsImage;
//	// ���ӵ�ַ
//	private String newsUrl;
//	// ��������
//	private String newsContent;
	
	
	public String getNewsId() {
		return newsId;
	}
	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}

	
	public String getMainTitle() {
		return mainTitle;
	}
	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}
	public String getViceTitle() {
		return viceTitle;
	}
	public void setViceTitle(String viceTitle) {
		this.viceTitle = viceTitle;
	}

	public String getViewCount() {
		return viewCount;
	}
	public void setViewCount(String viewCount) {
		this.viewCount = viewCount;
	}
	public String getNewsImagePath() {
		return newsImagePath;
	}
	public void setNewsImagePath(String newsImagePath) {
		this.newsImagePath = newsImagePath;
	}
	
	public Drawable getNewsImage() {
		return NewsImage;
	}

	public void setNewsImage(Drawable newsImage) {
		NewsImage = newsImage;
	}

//	// ���۴���
//	private int commentCount;
//	// �Ƿ񱻶���
//	private boolean isReaded;
	
	
}
