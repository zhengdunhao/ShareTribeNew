package com.atm.chatonline.bbs.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * @TODO�����ļ��ϴ����ԣ����Ļ�����
 * @fileName : com.atm.test.image.manyFile.java date | author | version |
 *           2015��10��5�� | Jiong | 1.0 |
 */

/*
 * �޸���ʷ��
 */
public class SendDataToServer {
	String multipart_form_data = "multipart/form-data";
	String twoHyphens = "--";
	String boundary = "****************fD4fH3gL0hK7aI6"; // ���ݷָ���
	String lineEnd = "\r\n";
	String cookie = null;														 

	/*
	 * �ϴ�ͼƬ���ݣ���ʽ��ο�HTTP Э���ʽ��
	 * ������Photos.upload�еġ�������á�http://wiki.dev.renren.com/
	 * wiki/Photos.upload#.E7.A8.8B.E5.BA.8F.E8.B0.83.E7.94.A8 �����ʽ���͵ķǳ�������
	 * ��ʽ������ʾ�� --****************fD4fH3hK7aI6 Content-Disposition: form-data;
	 * name="upload_file"; filename="apple.jpg" Content-Type: image/jpeg
	 * 
	 * ������ļ������ݣ�������������ʽ
	 */
	private void addFileContent(Bitmap[] files, DataOutputStream output) {
		if (files == null) { // û���ļ�ֱ���˳�����
			return;
		}
		// ѭ�����ļ���ʽд�뵽ͷ�ļ���
		for (Bitmap file : files) {
			StringBuilder split = new StringBuilder();
			split.append(twoHyphens + boundary + lineEnd);
			// �����files��Ҫ�ͷ��������յ�����һ����������Ϊfiles
			split.append("Content-Disposition: form-data; name=\"files\"; filename=\""
					+ "picture1.jpg" + "\"" + lineEnd);
			split.append("Content-Type: application/octet-stream; charset=utf-8"
					+ lineEnd);
			split.append(lineEnd);
			try {

				output.writeBytes(split.toString()); // д��ǰ��ĸ�ʽ
				// ���ļ������ֽ�����д��

				byte[] bytes = Bitmap2Bytes(file);

				output.write(bytes, 0, bytes.length);

				output.writeBytes(lineEnd); // д�����ĸ�ʽ
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/*
	 * �������ֶ����ݣ���ʽ��ο�HTTP Э���ʽ����FireBug����ץȡ��������ݣ���(�Ա��ϴ������Ӧ�Ĳ���ֵ) ��ʽ������ʾ��
	 * --****************fD4fH3hK7aI6 Content-Disposition: form-data;
	 * name="action" // һ���У������� upload
	 */
	private void addFormField(Map<Object, Object> params,
			DataOutputStream output) {
		StringBuilder sb = new StringBuilder();
		// ѭ��������ʽд�뵽ͷ�ļ���
		// entrySet()---------------------
		for (Map.Entry<Object, Object> param : params.entrySet()) {
			sb.append(twoHyphens + boundary + lineEnd);
			sb.append("Content-Disposition: form-data; name=\""
					+ param.getKey() + "\"" + lineEnd);
			sb.append(lineEnd);
			sb.append(param.getValue() + lineEnd);
		}
		try {
			output.write(sb.toString().getBytes("UTF-8"));// д����ֶ�����
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * ֱ��ͨ�� HTTP Э���ύ���ݵ���������ʵ�ֱ��ύ���ܡ�
	 * 
	 * @param actionUrl
	 *            �ϴ�·��
	 * @param params
	 *            �������keyΪ��������valueΪ����ֵ
	 * @param files
	 *            �ϴ��ļ���Ϣ
	 * @return ����������
	 */
	//List<String> cookies = null;// �������ʱ�����õ�cookie������ֱ���ô�java�����ԣ�

	public String post(String actionUrl, Map<Object, Object> params,
			Bitmap[] files,String cookie) {
		Log.i("SendDataToServer", lineEnd);
		HttpURLConnection conn = null;
		DataOutputStream output = null;
		BufferedReader input = null;
		try {
			URL url = new URL(actionUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(120000);
			conn.setRequestProperty("Charset", "utf-8"); // ���ñ���
			conn.setDoInput(true); // ��������
			conn.setDoOutput(true); // �������
			conn.setUseCaches(false); // ��ʹ��Cache
			conn.setRequestMethod("POST");
			if (cookie != null) {
				//System.out.println("��ȡ��cookie");
				
					conn.addRequestProperty("Cookie", cookie);
				
			}
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Content-Type", multipart_form_data
					+ "; boundary=" + boundary);

			conn.connect();
			output = new DataOutputStream(conn.getOutputStream());

			addFileContent(files, output); // ���ͼƬ����
			addFormField(params, output); // ������

			output.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);// ���ݽ�����־
			output.flush();
			int code = conn.getResponseCode(); // ��ȡ����������Ӧ����
			cookie = conn.getHeaderField("Set-Cookie");//.get("Set-Cookie");// ��ȡ��Ӧͷ�ֶε�ֵ
			
		
			if (code != 200) {
				Log.i("code", "" + code);
				throw new RuntimeException("����" + actionUrl + "��ʧ�ܣ�");
			}

			input = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			StringBuilder response = new StringBuilder();
			String oneLine;
			while ((oneLine = input.readLine()) != null) {
				response.append(oneLine + lineEnd);
			}

			return response.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			// ͳһ�ͷ���Դ
			try {
				if (output != null) {
					output.close();
				}
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			if (conn != null) {
				conn.disconnect();
			}
			Log.i("SendDataToServer", "��������");
		}

	} /*
	 * public static void main(String[] args) {
	 * 
	 * SendDataToServer go = new SendDataToServer();
	 * 
	 * String response = ""; //������ͼƬ�ķ�������·���ĳ���������������� String actionUrl =
	 * "http://139.129.131.179/ATM/essay/publish.do";
	 * 
	 * //�ȵ�¼ String loginUrl = "http://139.129.131.179/ATM/user_login.action";
	 * Map<Object,Object> loginParams = new HashMap<Object,Object>();
	 * loginParams.put("userId", "1"); loginParams.put("password", "1");
	 * response = go.post(loginUrl, loginParams, null);
	 * System.out.println("���ؽ����" + response);
	 * 
	 * //����������Щ���Ⱑʲô���߰�������� Map<Object,Object> params = new
	 * HashMap<Object,Object>(); params.put("type", "����"); params.put("label",
	 * "�ϴ�*#���ļ�"); params.put("title", "����ʱʲô"); params.put("department",
	 * "����ϵ"); params.put("content", "���ļ��ϴ�����");
	 * 
	 * //�ļ���ͼƬ�� ArrayList<File> list = new ArrayList<File>(); list.add(new
	 * File("E:\\test-0.jpg")); list.add(new File("E:\\test-1.jpg")); File[]
	 * files = (File[])list.toArray(new File[list.size()]);
	 * 
	 * //�������� response = go.post(actionUrl, params, files);
	 * System.out.println("���ؽ����" + response);
	 * 
	 * }
	 */
}
