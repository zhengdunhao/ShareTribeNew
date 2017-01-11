package com.atm.chatonline.bbs.util;
/**
 * �������ڵ�¼��ע��ģ��Ĺ����࣬���������˴���json����
 */
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

public class ConToServer extends Thread  {
	private String tag ="ConToServer";
	private static String respCode;
	private JSONArray sendJsonArray;
	private static String uriAPI;
	private SharedPreferences preferences;
	private String cookie;
	protected ChangeJson changeJson;
	public ConToServer(JSONArray sendJsonArray,String uriAPI) {
		super();
		this.sendJsonArray = sendJsonArray;
		this.uriAPI=uriAPI;
	}

	@Override
	public void run() {
		//���̨����
		HttpPost httpRequest=new HttpPost(uriAPI);
		DefaultHttpClient httpClient=new DefaultHttpClient();
		try {
			StringEntity sendEntity=new StringEntity(sendJsonArray.toString(),HTTP.UTF_8);
			httpRequest.setEntity(sendEntity);
			HttpResponse httpResponse=httpClient.execute(httpRequest);
			Log.i(tag, "��~~~ "+httpResponse.getStatusLine().getStatusCode());
			if(httpResponse.getStatusLine().getStatusCode()==200)
			{
				Log.i(tag, "����200");
				//ͨ��changejson�������ص���Ϣ��Ϊ�ַ���
				changeJson=new ChangeJson();
				String respStatus=changeJson.returnResult(httpResponse.getEntity());//*tip:registerע��ɹ�
				Log.i(tag, "respCode:"+respCode);
				getCookie(httpClient);
				//ͨ��ResponseCode���Ҷ�Ӧ��������ı��봮
				ConToServer.respCode=respStatus;
			}else
			{
				Log.i(tag, "û����200");
				//����������Ӧ
				ConToServer.respCode="����������Ӧ";
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) 
		{
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			} 

		}
	public String returnRespCode()
	{
		return respCode;
	}
	public ChangeJson returnChangeJson(){
		return changeJson;
	}
	//��ȡcookie�ļ�
    private void getCookie(DefaultHttpClient httpClient) {
        List<Cookie> cookies = httpClient.getCookieStore().getCookies();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < cookies.size(); i++) {
            Cookie cookie = cookies.get(i);
            String cookieName = cookie.getName();
            String cookieValue = cookie.getValue();
            if (!TextUtils.isEmpty(cookieName)
                     && !TextUtils.isEmpty(cookieValue)) {
                sb.append(cookieName + "=" );
                sb.append(cookieValue + ";" );
           }
       }
       Log.d( "cookie~~~~", sb.toString());
       cookie = sb.toString();
    }
    
    public String getCookie() {
    	return cookie;
    }

}
