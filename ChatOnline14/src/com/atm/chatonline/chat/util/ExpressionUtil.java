package com.atm.chatonline.chat.util;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

import com.example.studentsystem01.R;


public class ExpressionUtil {
	/**
	 * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
	 * @param context
	 * @param spannableString
	 * @param patten
	 * @param start
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws NumberFormatException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	
	private static String tag="ExpressionUtil";
    public static void dealExpression(Context context,SpannableString spannableString, Pattern patten, int start) throws SecurityException, NoSuchFieldException, NumberFormatException, IllegalArgumentException, IllegalAccessException {
    	Matcher matcher = patten.matcher(spannableString);
    	Log.i(tag, "dealExpression");
        while (matcher.find()) {
        	Log.i(tag, "dealExpression33");
            String key = matcher.group();
            if (matcher.start() < start) {
                continue;
            }
            key=key.substring(1);//ȥ�����ţ��õ����ַ���ƥ��ͼƬ����
            Log.i(tag, "key:"+key);
            Field field = R.drawable.class.getDeclaredField(key);
			int resId = Integer.parseInt(field.get(null).toString());		//ͨ������ƥ��õ����ַ���������ͼƬ��Դid
			Log.i(tag,"resId:"+resId);
            if (resId != 0) {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);	
                ImageSpan imageSpan = new ImageSpan(bitmap);				//ͨ��ͼƬ��Դid���õ�bitmap����һ��ImageSpan����װ
                int end = matcher.start() + key.length()+1;					//�����ͼƬ���ֵĳ��ȣ�Ҳ����Ҫ�滻���ַ����ĳ���
                spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);	//����ͼƬ�滻�ַ����й涨��λ����
                if (end < spannableString.length()) {						//��������ַ�����δ��֤�꣬���������
                    dealExpression(context,spannableString,  patten, end);
                }
                break;
            }
        }
        Log.i(tag, "dealExpression2");
    }
    
    /**
     * 得到�?��SpanableString对象，�?过传入的字符�?并进行正则判�?     * @param context
     * @param str
     * @return
     */
    public static SpannableString getExpressionString(Context context,String str,String zhengze){
    	Log.i(tag, "str:"+str+",zhengze:"+zhengze);
    	SpannableString spannableString = new SpannableString(str);
    	Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);		//ͨ�������������ʽ������һ��pattern
        try {
            dealExpression(context,spannableString, sinaPatten, 0);
        } catch (Exception e) {
            Log.e("dealExpression", e.getMessage());
        }
        return spannableString;
    }
	

}