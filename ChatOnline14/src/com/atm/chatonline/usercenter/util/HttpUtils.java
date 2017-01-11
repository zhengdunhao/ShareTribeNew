package com.atm.chatonline.usercenter.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * ͨ��URL��ȡ�û�����Ϣ������ͨ��URL�����û��ڱ����޸ĵĸ�����Ϣ��Ҳ����ͨ��url�����û���ͷ��
 * 
 * @author Jackbing
 * @date 2016-10-2 �޸�
 */
public class HttpUtils {

	/**
	 * ֱ�Ӵ������л�ȡbitmapͼƬ
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getBitmapFromUrl(String url) {

		try {
			URL uri = new URL(url);
			HttpURLConnection con = (HttpURLConnection) uri.openConnection();
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				return BitmapFactory.decodeStream(con.getInputStream());
			}
		} catch (MalformedURLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * �򵥵����󷽷�
	 * 
	 * @param url
	 * @return jsonObject
	 */
	public static JSONObject get(String url, List<BasicNameValuePair> params) {
		/*
		 * try { URL uri=new URL(url); HttpURLConnection con=(HttpURLConnection)
		 * uri.openConnection(); StringBuilder sb=new StringBuilder();
		 * StringBuffer sbf=new StringBuffer();
		 * 
		 * 
		 * 
		 * Log.i("response", "content="+con.getResponseMessage());
		 * con.disconnect();
		 * 
		 * } catch (MalformedURLException e) { // TODO �Զ����ɵ� catch ��
		 * e.printStackTrace(); return null; } catch (IOException e) { // TODO
		 * �Զ����ɵ� catch �� e.printStackTrace(); return null; }
		 */

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		UrlEncodedFormEntity entity;
		try {
			entity = new UrlEncodedFormEntity(params, "UTF-8");
			post.setEntity(entity);

			HttpResponse response = client.execute(post);
			String out = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
			Log.i("get", "out=" + out);
			return new JSONObject(out);

		} catch (ClientProtocolException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��

			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ���͸��Ĺ������ݸ���̨
	 * 
	 * @param url
	 * @param params
	 * @param file
	 * @return jsonobject
	 */
	@SuppressLint("NewApi")
	public static JSONObject post(String url, Map<String, String> params,
			Map<String, File> file) {
		// file��key���ļ���
		JSONObject object = new JSONObject();
		try {
			object.put("tip", "failed");
		} catch (JSONException e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
			return object;
		}
		String BOUNDARY = java.util.UUID.randomUUID().toString();// ������ɵ��ַ���
		String PREFIX = "--";
		String LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";

		HttpURLConnection con = null;
		DataOutputStream out = null;
		BufferedReader reader = null;
		InputStream is = null;
		try {
			URL uri = new URL(url);
			con = (HttpURLConnection) uri.openConnection();
			// con.setConnectTimeout(30000);
			con.setReadTimeout(30000);// ����timeout
			con.setDoInput(true);// ��������
			con.setDoOutput(true);// �������
			con.setUseCaches(false);// ��������ʹ�û���
			con.setRequestMethod("POST");
			con.setRequestProperty("connection", "keep-alive");
			con.setRequestProperty("Charset", CHARSET);
			con.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
					+ ";boundary=" + BOUNDARY);
			Log.i("......", "�����������");
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINEND);
				sb.append("Content-Disposition: form-data; name=\""
						+ entry.getKey() + "\"" + LINEND);
				sb.append("Content-Type:text/plain; charset=" + CHARSET
						+ LINEND);
				sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
				sb.append(LINEND);
				sb.append(entry.getValue());
				sb.append(LINEND);
			}
			/*
			 * sb.append(PREFIX); sb.append(BOUNDARY); sb.append(LINEND);
			 * sb.append
			 * ("Content-Disposition: form-data;name=\""+"json"+"\""+LINEND);
			 * sb.append("Content-Type:text/plain; charset="+CHARSET+LINEND);
			 * sb.append("Content-Transfer-Encoding: 8bit"+LINEND);
			 * sb.append(LINEND); sb.append(jsonArray.toString());
			 * sb.append(LINEND);
			 */
			Log.i(">>>>>>>>", "con.getOutputStream");
			out = new DataOutputStream(con.getOutputStream());
			Log.i(">>>>>>>>", "write");
			out.write(sb.toString().getBytes());
			Log.i("......", "�ı��������");

			if (file != null) {
				for (Map.Entry<String, File> currentfile : file.entrySet()) {
					StringBuilder sb1 = new StringBuilder();
					sb1.append(PREFIX);
					sb1.append(BOUNDARY);
					sb1.append(LINEND);
					// name��post�д��εļ� filename���ļ�������
					sb1.append("Content-Disposition:form-data; name=\"file\"; filename=\""
							+ currentfile.getKey() + "\"" + LINEND);
					sb1.append("Content-Type:application/octet-stream; charset="
							+ CHARSET + LINEND);
					sb1.append(LINEND);
					out.write(sb1.toString().getBytes());

					is = new FileInputStream(currentfile.getValue());
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						out.write(buffer, 0, len);
					}

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
			Log.i(">>>>", (con == null) + "");
			int res = con.getResponseCode();

			Log.i(">>>>>>>>>", "respCode =" + res);
			if (res == 200) {
				reader = new BufferedReader(new InputStreamReader(
						con.getInputStream(), "UTF-8"));
				int ch;
				String line;
				StringBuilder sb2 = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					sb2.append(line);
				}
				Log.i("resp", sb2.toString());
				object = new JSONObject(sb2.toString());

				Log.i("jsononject", "object=" + object.toString());
				return object;
			} else {
				int ch;
				StringBuilder ss = new StringBuilder();
				while ((ch = con.getInputStream().read()) != -1) {
					ss.append((char) ch);
				}
				Log.i("resp error", ss.toString());
			}

		} catch (java.net.ConnectException e) {
			return object;
		} catch (MalformedURLException e) {

			e.printStackTrace();
			return object;
		} catch (IOException e) {

			e.printStackTrace();
			return object;
		} catch (JSONException e) {
			Log.i("1>>>>>>>", "error");
			e.printStackTrace();
			return object;
		} catch (Exception e) {
			Log.i("2>>>>>>>", "error");
			return object;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
			if (con != null) {
				con.disconnect();
			}
		}
		Log.i("3>>>>>>>", "error");
		return object;

	}
}
