/**
 * 
 */
package com.atm.chatonline.bbs.util;

import android.app.Activity;
import android.util.Log;

/**
 * @�� com.atm.charonline.bbs.util ---LogUtil
 * @���� �Զ�����־���ߣ�����ʱ��ӡ���ǲ���ʱ�����ε���
 * @���� ��С��
 * @ʱ�� 2015-10-26
 * 
 */
public class LogUtil {
	public static final int VERBOSE = 1;
	public static final int DEBUG = 2;
	public static final int INFO = 3;
	public static final int WARN = 4;
	public static final int ERROR = 5; 
	public static final int NOTHING = 6;
	public static final int LEVEL = VERBOSE;
	/*
	 *     ��Ϊ��������if�жϣ�ֻ�е�LEVEL������ֵС�ڻ���ڶ�Ӧ��־�����ʱ��
	 * �ŻὫ��־��ӡ������
	 * 		���ԣ������ڿ����׶��趨LEVELֵΪVERBOSE,����Ŀ��ʽ���ߵ�ʱ��
	 * LEVELָ��ΪNOTHING�Ϳ����ˡ�
	 */
	
	//��ȡtag,��ʽ������.�������������кš�  ����д��ȡ���ǵ�ǰ�����
//	public static String getTag() { 
//		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
//		if(elements.length<4) {
//			return "Stack to shallow";
//		}
//		else {
//			String fullClassName = elements[3].getClassName();
//			String className = fullClassName.substring(fullClassName.lastIndexOf(".")+1);
//			String methodName = elements[3].getMethodName();
//			int lineNumber = elements[3].getLineNumber();
//			String tag1 = className+"."+methodName+"():"+lineNumber+"��";
//			return tag1;
//		}
//	}

	/**
	 * ����ΪVERBOSE��
	 * @param msg
	 * Ҫ��ӡ����Ϣ
	 */
	public static void v(String msg) {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if(elements.length<4) {
			Log.d("LogUtil����", "Stack to shallow");
		}
		else {
			String fullClassName = elements[3].getClassName();
			String className = fullClassName.substring(fullClassName.lastIndexOf(".")+1);
			String methodName = elements[3].getMethodName();
			int lineNumber = elements[3].getLineNumber();
			String tag = className+"."+methodName+"():"+lineNumber+"��";
			if(LEVEL <= VERBOSE)
				Log.v(tag,msg);
		}
	}
	/**
	 * ����ΪDEBUG��
	 * @param msg
	 * Ҫ��ӡ����Ϣ
	 */
	public static void d(String msg) {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if(elements.length<4) {
			Log.d("LogUtil����", "Stack to shallow");
		}
		else {
			String fullClassName = elements[3].getClassName();
			String className = fullClassName.substring(fullClassName.lastIndexOf(".")+1);
			String methodName = elements[3].getMethodName();
			int lineNumber = elements[3].getLineNumber();
			String tag = className+"."+methodName+"():"+lineNumber+"��";
			if(LEVEL <= DEBUG)
				Log.d(tag,msg);
		}
	}
	/**
	 * ����ΪINFO��
	 * @param msg
	 * Ҫ��ӡ����Ϣ
	 */
	public static void i(String msg) {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if(elements.length<4) {
			Log.d("LogUtil����", "Stack to shallow");
		}
		else {
			String fullClassName = elements[3].getClassName();
			String className = fullClassName.substring(fullClassName.lastIndexOf(".")+1);
			String methodName = elements[3].getMethodName();
			int lineNumber = elements[3].getLineNumber();
			String tag = className+"."+methodName+"():"+lineNumber+"��";
			if(LEVEL <= INFO)
				Log.i(tag,msg);
		}
	}
	/**
	 * ����ΪWARN��
	 * @param msg
	 * Ҫ��ӡ����Ϣ
	 */
	public static void w(String msg) {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if(elements.length<4) {
			Log.d("LogUtil����", "Stack to shallow");
		}
		else {
			String fullClassName = elements[3].getClassName();
			String className = fullClassName.substring(fullClassName.lastIndexOf(".")+1);
			String methodName = elements[3].getMethodName();
			int lineNumber = elements[3].getLineNumber();
			String tag = className+"."+methodName+"():"+lineNumber+"��";
			if(LEVEL <= WARN)
				Log.w(tag,msg);
		}
	}
	/**
	 * ����ΪERROR��
	 * @param msg
	 * Ҫ��ӡ����Ϣ
	 */
	public static void e(String msg) {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if(elements.length<4) {
			Log.d("LogUtil����", "Stack to shallow");
		}
		else {
			String fullClassName = elements[3].getClassName();
			String className = fullClassName.substring(fullClassName.lastIndexOf(".")+1);
			String methodName = elements[3].getMethodName();
			int lineNumber = elements[3].getLineNumber();
			String tag = className+"."+methodName+"():"+lineNumber+"��";
			if(LEVEL <= ERROR)
				Log.e(tag,msg);
		}
	}
	/**
	 * ��ֵ�Եķ�ʽ��ӡ��Ϣ��p�����ӡ,����ΪDEBUG��
	 * @param key
	 * ��
	 * @param value
	 * ֵ
	 */
	public static void p(String key, String value) {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if(elements.length<4) {
			Log.d("LogUtil����", "Stack to shallow");
		}
		else {
			String fullClassName = elements[3].getClassName();
			String className = fullClassName.substring(fullClassName.lastIndexOf(".")+1);
			String methodName = elements[3].getMethodName();
			int lineNumber = elements[3].getLineNumber();
			String tag = className+"."+methodName+"():"+lineNumber+"��";
			Log.d(tag, key+":"+value);
		}
	}
}
