package com.atm.chatonline.schoolnews.bean;

import android.graphics.drawable.Drawable;

public class TopNews {
	// ���ŵ�ID
	private String newsId;
	// ����������
	private String mainTitle;
	// ����ͼƬ·��
	private String newsImagePath;

	transient private Drawable NewsImage;

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
}
