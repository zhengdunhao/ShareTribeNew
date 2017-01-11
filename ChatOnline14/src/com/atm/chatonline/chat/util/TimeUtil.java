package com.atm.chatonline.chat.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class TimeUtil {
	/**
	 * ��ȡ����ʱ��(ϵͳ��ǰʱ��),ʱ���ʽΪ"yyMMddHHmmss"
	 * 
	 * @return
	 */
	private static String tag = "TimeUtil";

	public static String getAbsoluteTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	
	public static String getAbsoluteTime2(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		return sdf.format(new Date());
	}
	
	/**
	 * date������Ϣ��ʱ�䣬lastTimeΪ��һ������Ϣ��showTime��ChatMessage���һ���������ԡ�
	 * ����showTime=0��ʾÿһ�θ�����Ϣʱ��������ϢҪ����һ����Ϣ���бȽ�
	 * showTime=-1��ʾ������Ϣ����һ����Ϣʱ���������3���ӣ����������Ժ����UI���棬�Ӵ˲�����ʾʱ�䣬
	 * showTime=1��ʾ������Ϣ����һ����Ϣʱ���������3���ӣ��ڽ�����ֱ����ʾ������Ϣ�ľ���ʱ�䣬�Ӵ�Ҳ����ʾ�Լ�ChatMessage������ʱ��
	 * 
	 * */

	public static String compareTime(String date,String lastTime,int showTime){
		String time="";
		try {
			SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dt1=sdf.parse(date);
			Date dt3=sdf.parse(lastTime);
			
			Calendar cl=Calendar.getInstance();
			cl.setTime(dt1);
			int year1=cl.get(Calendar.YEAR);
			int month1=cl.get(Calendar.MONTH);
			int day1=cl.get(Calendar.DAY_OF_MONTH);
			int hour1=cl.get(Calendar.HOUR_OF_DAY);
			int minute1=cl.get(Calendar.MINUTE);
			int second1=cl.get(Calendar.SECOND);
			
			cl.setTime(dt3);
			int year3=cl.get(Calendar.YEAR);
			int month3=cl.get(Calendar.MONTH);
			int day3=cl.get(Calendar.DAY_OF_MONTH);
			int hour3=cl.get(Calendar.HOUR_OF_DAY);
			int minute3=cl.get(Calendar.MINUTE);
			int second3=cl.get(Calendar.SECOND);
			
			Log.i(tag, "����������(������)��ʱ���ǣ�"+year1+"��"+month1+"��"+day1+"��"+hour1+"Сʱ"+minute1+"��"+second1+"��");
			Log.i(tag, "��һ�������ŵ�ʱ���ǣ�"+year3+"��"+month3+"��"+day3+"��"+hour3+"Сʱ"+minute3+"��"+second3+"��");
			if(showTime==0){
				if(year1==year3){
					if(month1==month3){
						if(day1==day3){
							if(hour1==hour3){
								if((minute1 - minute3) < 3){
									return time="";
								}else{
									return time=date;
								}
							}
						}
					}
				}
			}else if(showTime==1){
				return time=date;
			}else if(showTime==-1){
				return time="";
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Log.i(tag, "���ã�ʱ�䱨���ˣ�getRelativeTime�������߽����ˣ���ʱ���ʽ���ܲ��԰�");
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * ��ȡ���ʱ��(�������ʱ��任�������ϵͳ��ǰʱ��Ĳ�ֵ)����ʽΪ��XX����ǰ��
	 * 
	 * @return
	 */
	

	private static String formatTime(int hour, int minute) {
		String time = "";
		if (hour < 10) {
			time += "0" + hour + ":";
		} else {
			time += hour + ":";
		}

		if (minute < 10) {
			time += "0" + minute;
		} else {
			time += minute;
		}
		System.out.println("format(hour, minute)=" + time);
		return time;
	}
}
