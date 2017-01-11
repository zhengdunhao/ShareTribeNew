package com.atm.chatonline.schoolnews.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;

import com.atm.chatonline.schoolnews.bean.News;
import com.example.studentsystem01.R;

/**
 * �������������б�
 * @Description: �������������б�����ֻ�Ǹ�ʾ��������ز���ʵ�֡�

 * @File: NewsXmlParser.java

 * @Package com.image.indicator.parser

 * @Author Hanyonglu

 * @Date 2012-6-18 ����02:31:26

 * @Version V1.0
 */
public class NewsXmlParser {
	// �����б�
	private List<HashMap<String, News>> newsList = null;
	private String tag ="NewsXmlParser";
	
	// ����ͼƬ�ļ��ϣ��������ó��˹̶����أ���ȻҲ�ɶ�̬���ء�
	private int[] slideImages = {
			R.drawable.image01,
			R.drawable.image02
			};
	
	// ��������ļ���
	private int[] slideTitles = {
			R.string.title1,
			R.string.title2
			
	};
	
	// �������ӵļ���
	private String[] slideUrls = {
			"http://www.baidu.com",
			"http://cloud.csdn.net/a/20120614/2806646.html"
		
	};
	
	public int[] getSlideImages(){
		return slideImages;
	}
	
	public int[] getSlideTitles(){
		return slideTitles;
	}
	
	public String[] getSlideUrls(){
		return slideUrls;
	}
	
	/**
	 * ��ȡXmlPullParser����
	 * @param result
	 * @return
	 */
	private XmlPullParser getXmlPullParser(String result){
		XmlPullParser parser = Xml.newPullParser();
		Log.i(tag, "getXmlPullParser-result:"+result);
		InputStream inputStream = FileAccess.String2InputStream(result);
		
		try {
			parser.setInput(inputStream, "UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return parser;
	}
	
	public int getNewsListCount(String result){
		int count = -1;
		
		try {
			Log.i(tag, "getNewsListCount-result:"+result);
			XmlPullParser parser = getXmlPullParser(result);
	        int event = parser.getEventType();//������һ���¼�
	        
	        while(event != XmlPullParser.END_DOCUMENT){
	        	switch(event){
	        	case XmlPullParser.START_DOCUMENT:
	        		break;
	        	case XmlPullParser.START_TAG://�жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؿ�ʼ�¼�
	        		if("count".equals(parser.getName())){//�жϿ�ʼ��ǩԪ���Ƿ���count
	        			count = Integer.parseInt(parser.nextText());
	                }
	        		
	        		break;
	        	case XmlPullParser.END_TAG://�жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؽ����¼�
//	        		if("count".equals(parser.getName())){//�жϿ�ʼ��ǩԪ���Ƿ���count
//	        			count = Integer.parseInt(parser.nextText());
//	                }
	        		
	        		break;
	        	}
            
	        	event = parser.next();//������һ��Ԫ�ز�������Ӧ�¼�
	        }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		// �޷���ֵ���򷵻�-1
		return count;
	}
}
