package com.atm.chatonline.bbs.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

public class SendPicture {
	
	@SuppressLint("NewApi")
	public static JSONObject post(String url,JSONArray jsonArray,Map<String,File> file){
																	//file��key���ļ���			
		JSONObject object=new JSONObject();
		try {
			object.put("tip", "failed");
		} catch (JSONException e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
		}
		String BOUNDARY=java.util.UUID.randomUUID().toString();//������ɵ��ַ���
		String PREFIX="--";
		String LINEND="\r\n";
		String MULTIPART_FROM_DATA="multipart/form-data";
		String CHARSET="UTF-8";
		
		
		try {
			URL uri=new URL(url);
			HttpURLConnection con=(HttpURLConnection)uri.openConnection();
			//con.setConnectTimeout(30000);
			con.setReadTimeout(30000);//����timeout
			con.setDoInput(true);//��������
			con.setDoOutput(true);//�������
			con.setUseCaches(false);//��������ʹ�û���
			con.setRequestMethod("POST");
			con.setRequestProperty("connection", "keep-alive");
			con.setRequestProperty("Charset", CHARSET);
			con.setRequestProperty("Content-Type", MULTIPART_FROM_DATA+";boundary="+BOUNDARY);
			Log.i("......", "�����������");
			StringBuilder sb=new StringBuilder();
			/*for(Map.Entry<String, String> entry:params.entrySet()){
				sb.append(PREFIX);
	            sb.append(BOUNDARY);
	            sb.append(LINEND);
	            sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
	            sb.append("Content-Type:text/plain; charset=" + CHARSET + LINEND);
	            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
	            sb.append(LINEND);
	            sb.append(entry.getValue());
	            sb.append(LINEND);
			}*/
			sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data;name=\""+"json"+"\""+LINEND);
            sb.append("Content-Type:text/plain; charset="+CHARSET+LINEND);
            sb.append("Content-Transfer-Encoding: 8bit"+LINEND);
            sb.append(LINEND);
            sb.append(jsonArray.toString());
            sb.append(LINEND);
            Log.i(">>>>>>>>", "con.getOutputStream");
			DataOutputStream out=new DataOutputStream(con.getOutputStream());
			Log.i(">>>>>>>>", "write");
			out.write(sb.toString().getBytes());
			Log.i("......", "�ı��������");
			InputStream in=null;
			
			if(file!=null){
				for(Map.Entry<String, File> currentfile:file.entrySet()){
					StringBuilder sb1 = new StringBuilder();
	                sb1.append(PREFIX);
	                sb1.append(BOUNDARY);
	                sb1.append(LINEND);
	                // name��post�д��εļ� filename���ļ�������
	                sb1.append("Content-Disposition:form-data; name=\"file\"; filename=\"" + currentfile.getKey() + "\"" + LINEND);
	                sb1.append("Content-Type:application/octet-stream; charset=" + CHARSET + LINEND);
	                sb1.append(LINEND);
	                out.write(sb1.toString().getBytes());

	                InputStream is = new FileInputStream(currentfile.getValue());
	                byte[] buffer = new byte[1024];
	                int len = 0;
	                while ((len = is.read(buffer)) != -1)
	                {
	                    out.write(buffer, 0, len);
	                }

	                is.close();
	                out.write(LINEND.getBytes());
	                Log.i("......", "�ļ��������");
				}
			}
			
			 Log.i("----", "write data flush");
			
			 // ���������־
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            out.write(end_data);
            out.flush();
            // �õ���Ӧ��
            Log.i("_____", "getResponse");
            Log.i(">>>>",(con==null)+"");
            int res = con.getResponseCode();
            
            Log.i(">>>>>>>>>", "respCode ="+res);
            if (res == 200)
            {
                in = con.getInputStream();
                int ch;
                StringBuilder sb2 = new StringBuilder();
                while ((ch = in.read()) != -1)
                {
                    sb2.append((char) ch);
                }
                Log.i("resp", sb2.toString());
               JSONArray arr=new JSONArray(sb2.toString());
             
                object=arr.getJSONObject(0);
                Log.i("jsononject", "object="+object.toString());
                return object;
            }
            
            
            out.close();
            con.disconnect();
        
       
		} catch(java.net.ConnectException e){
			return object;
		}catch (MalformedURLException e) {
			
			e.printStackTrace();
			return object;
		} catch (IOException e) {
			
			e.printStackTrace();
			return object;
		} catch (JSONException e) {
			
			return object;
		}catch(Exception e){
			
			return object;
		}
		return object;
		
	}
	
	
	
	/*public static void clientPost(String url,JSONArray jsonArray,Map<String,File> file){
		String BOUNDARY=java.util.UUID.randomUUID().toString();//������ɵ��ַ���
		String PREFIX="--";
		String LINEND="\r\n";
		String MULTIPART_FROM_DATA="multipart/from-data";
		String CHARSET="UTF-8";
		HttpClient client=new DefaultHttpClient();
		HttpPost post=new HttpPost(url);
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,5000);
		
		
	}*/
}
