package com.atm.chatonline.chat.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.atm.charonline.bbs.util.MD5Encrypt;
import com.atm.chatonline.bbs.util.LogUtil;
import com.atm.chatonline.chat.ui.BaseActivity;
import com.atm.chatonline.chat.util.Config;

public class NewNetWorkSend {

	private static String TAG = "ClientSendThread";
	static final int port = 23457;
	private Charset charset = Charset.forName("GBK");
	private SocketChannel socketChannel = null;
	private ByteBuffer buffer = null;

	public NewNetWorkSend(SocketChannel sc) {
		this.socketChannel = sc;
	}

	/**
	 * ��¼
	 * 
	 * @param userId
	 * @param userPwd
	 */
	public void login(String userId, String userPwd) {
		try {
			String userPwdMD5 = MD5Encrypt.stringMD5(userPwd);
			Log.i(TAG, "����������͵�¼����");
			buffer = ByteBuffer.allocate(12 + userId.getBytes().length
					+ userPwdMD5.getBytes().length);
			buffer.putInt(Config.REQUEST_LOGIN);
			buffer.putInt(userId.getBytes().length);
			buffer.put(userId.getBytes());
			buffer.putInt(userPwdMD5.getBytes().length);
			buffer.put(userPwdMD5.getBytes());
			writeBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * // �����ı�˽����Ϣ public void sendMessage(String userId, String friendId,
	 * String content) {
	 * 
	 * buffer = ByteBuffer.allocateDirect(16 + userId.getBytes().length +
	 * friendId.getBytes().length + content.getBytes().length); // д��buffer������
	 * buffer.putInt(Config.MESSAGE_TO); put(userId); put(friendId);
	 * put(content);
	 * 
	 * writeBuffer();
	 * 
	 * }
	 */

	/**
	 * ����˽���ı���Ϣ
	 * 
	 * @param userID
	 * @param friendID
	 * @param time
	 * @param content
	 * @return
	 */

	public boolean sendText(String userID, String friendID, String time,
			String content) {
		boolean result = true;
		try {

			Log.i(TAG, "NewNetWorker01---���߷������ͻ���Ҫ������Ϣ");
			buffer = ByteBuffer.allocateDirect(24 + userID.getBytes().length
					+ friendID.getBytes().length + time.getBytes().length
					+ content.getBytes().length);
			buffer.putInt(Config.MESSAGE_TO);
			buffer.putInt(Config.MESSAGE_TEXT);
			Log.i(TAG,
					"NewNetWorker01---����Config.MESSAGE_TO��Config.MESSAGE_TYPE_TXT����");
			buffer.putInt(userID.getBytes().length);
			buffer.put(userID.getBytes());
			buffer.putInt(friendID.getBytes().length);
			buffer.put(friendID.getBytes());
			buffer.putInt(time.getBytes().length);
			buffer.put(time.getBytes());
			buffer.putInt(content.getBytes().length);
			buffer.put(content.getBytes());
			writeBuffer();
			/*
			 * buffer.flip(); socketChannel.write(buffer); buffer.clear();
			 */
			Log.i(TAG, "NewNetWorker01---�ͻ��˷��Ͷ��ųɹ�");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ����˽��ͼƬ
	 * 
	 */
	public boolean sendImg(String userID, String friendID, String time,
			String filePath) {
		boolean result = true;
		try {
			byte[] imgByte = makeFileToByte(filePath);
			LogUtil.p(TAG, "NewNetWorker01---���߷������ͻ���Ҫ����ͼƬ");
			buffer = ByteBuffer.allocateDirect(28 + userID.getBytes().length
					+ friendID.getBytes().length + time.getBytes().length
					+ getFileName(filePath).getBytes().length + imgByte.length);
			buffer.putInt(Config.MESSAGE_TO);
			buffer.putInt(Config.MESSAGE_IMG);
			Log.i(TAG,
					"NewNetWorker01---����Config.MESSAGE_TO��Config.MESSAGE_IMG����");
			buffer.putInt(userID.getBytes().length);
			buffer.put(userID.getBytes());
			buffer.putInt(friendID.getBytes().length);
			buffer.put(friendID.getBytes());
			buffer.putInt(time.getBytes().length);
			buffer.put(time.getBytes());
			buffer.putInt(getFileName(filePath).getBytes().length);
			buffer.put(getFileName(filePath).getBytes());
			LogUtil.p(TAG, "filename:" + getFileName(filePath));
			buffer.putInt(imgByte.length);
			buffer.put(imgByte);
			writeBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/**
	 * �����ı�Ⱥ��Ϣ
	 * 
	 * @param userId
	 * @param crowdId
	 * @param content
	 */
	public boolean sendCrowdMessage(String userId, int groupId, String content) {
		boolean result = true;
		try {
			buffer = ByteBuffer.allocateDirect(20 + userId.getBytes().length
					+ content.getBytes().length);

			buffer.putInt(Config.CROWD_MESSAGE_TO);// Ⱥ����Ϣ
			buffer.putInt(Config.CROWD_MESSAGE_TEXT);// �ı���Ϣ
			put(userId);
			buffer.putInt(groupId);
			put(content);
			writeBuffer();
		} catch (Exception e) {
			result = false;
			return result;
		}
		return result;
	}

	// �½�Ⱥ//****
	public void sendCreateGroup(String userId, String groupName,
			String groupDesc, String groupLabel, int groupProperty,
			String groupHeadImg, String path) throws IOException {
		// String path="sdcard/create_group_headimg.png";
		byte[] bytes = makeFileToByte(path);

		try {
			buffer = ByteBuffer.allocate(36 + userId.getBytes().length
					+ groupName.getBytes().length + groupDesc.getBytes().length
					+ groupLabel.getBytes().length
					+ groupHeadImg.getBytes().length + bytes.length);
			buffer.putInt(Config.CROWD_CREATE);
			put(userId);
			put(groupName);
			put(groupDesc);
			put(groupLabel);
			buffer.putInt(groupProperty);
			put(groupHeadImg);
			buffer.putInt(bytes.length);
			Log.i("888888888888", "img size=" + bytes.length);
			buffer.put(bytes);
			writeBuffer();
		} catch (Exception e) {
			Log.i(TAG, "����Ⱥ���ɹ�");
		}
	}

	// �������Ϳ��ܸ���Ȥ��Ȥ
	public void reqHobbyGroup(String userID) {
		// String userID = WoliaoBaseActivity.getSelf().getUserID();
		Log.i(TAG, "reqHobbyGroup()-userID:" + userID);
		buffer = ByteBuffer.allocateDirect(8 + userID.getBytes().length);
		buffer.putInt(Config.CROWD_FOUND);
		put(userID);
		writeBuffer();
		Log.i(TAG, "reqHobbyGroup()�Է�������:Config.CROWD_FOUND");
	}

	public void reqHobbyUser(String userID) {
		try {
			// String userID = WoliaoBaseActivity.getSelf().getUserID();
			Log.i(TAG, "reqHobbyUser()-userID:" + userID);
			buffer = ByteBuffer.allocateDirect(8 + userID.getBytes().length);
			buffer.putInt(Config.USER_FOUND);
			put(userID);
			writeBuffer();
			Log.i(TAG, "reqHobbyUser()�Է�������:Config.USER_FOUND");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �鿴Ⱥ����
	public void sendLookGroupData(int groupId) {
		buffer = ByteBuffer.allocate(8);
		buffer.putInt(Config.CROWD_GET_INFO);
		buffer.putInt(groupId);
		writeBuffer();
	}

	// �ҵ�Ⱥ
	public void sendFindMyGroup(String userId) {
		Log.i("send ", "sendFindMyGroup ����");
		userId = BaseActivity.getSelf().getUserID();
		buffer = ByteBuffer.allocate(10 + userId.getBytes().length);
		buffer.putInt(Config.CROWD_MY);
		put(userId);
		writeBuffer();
	}

	// ����Ⱥ
	public void findCrowd(String condition, String userId) {
		// String userID = WoliaoBaseActivity.getSelf().getUserID();

		if (BaseActivity.getSelf() == null) {
			Log.i("2", "user is null");
		} else {
			Log.i("2", "user is not null");
		}
		Log.i(TAG, "findCrowd-userID:" + userId);
		buffer = ByteBuffer.allocate(12 + userId.getBytes().length
				+ condition.getBytes().length);
		buffer.putInt(Config.CROWD_FIND);
		put(userId);
		put(condition);
		writeBuffer();
	}

	// ��ѯ�����б�
	public void findFriendList(String condition, String userId) {
		try {
			Log.i("send ", "findFriendList ����");
			buffer = ByteBuffer.allocate(8 + userId.getBytes().length);
			buffer.putInt(Config.USER_GET_ATTENT);
			put(userId);
			writeBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �����û�
	public void findUser(String condition, String userID) {
		try {
			// String userID = WoliaoBaseActivity.getSelf().getUserID();
			Log.i(TAG, "findUser-userID:" + userID);
			Log.i(TAG, "condition:" + condition);
			buffer = ByteBuffer.allocate(12 + userID.getBytes().length
					+ condition.getBytes().length);
			buffer.putInt(Config.USER_FIND);
			put(userID);
			// put(condition);
			buffer.putInt(condition.getBytes().length);
			buffer.put(condition.getBytes());
			writeBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// �����Ⱥ
	public void intiveToCrowd() {
		String senderId = null;
		int crowdId = 0;
		String friendId = null;
		buffer = ByteBuffer.allocate(20 + senderId.getBytes().length
				+ friendId.getBytes().length);
		buffer.putInt(Config.CROWD_INTIVE);
		put(senderId);
		buffer.putInt(crowdId);
		put(friendId);
		writeBuffer();
	}

	// �����˳�
	public synchronized void exit(String userId) {
		try {
			Log.i("networksend", "�ѷ��������˳�����");
			buffer = ByteBuffer.allocateDirect(8 + userId.getBytes().length);
			buffer.putInt(Config.REQUEST_EXIT);
			put(userId);
			writeBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �Ƿ�����
	public void lookOnline() {

	}

	// ���ͻ�ȡ˽��������Ϣ������
	public void getOfflineMessage(String userId) {
		try {
			Log.i(TAG, "findOfflineMessage()---userId" + userId);
			buffer = ByteBuffer.allocateDirect(8 + userId.getBytes().length);
			buffer.putInt(Config.MESSAGE_OFFLINE);
			put(userId);
			writeBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ���������ȡ�ҵ���Ϣ
	public boolean getMyMessage(String userId, int type) {
		JSONObject json = new JSONObject();
		try {
			json.put("userId", userId);
			json.put("type", type);
		} catch (JSONException e) {
			System.out.println("���ͻ�ȡ�ҵ���Ϣ����ʱ��json �����쳣");
			return false;
		}
		buffer = ByteBuffer
				.allocateDirect(8 + json.toString().getBytes().length);
		buffer.putInt(Config.MY_MESSAGE);
		System.out.println(json.toString());
		put(json.toString());
		writeBuffer();
		return true;
	}
	
	public void sendIsHasNewMsg(String userId){
		JSONObject json = new JSONObject();
		try {
			json.put("userId", userId);
			LogUtil.i("sendHasNewMessage2");
		} catch (JSONException e) {
			System.out.println("���ͻ�ȡ�ҵ���Ϣ����ʱ��json �����쳣");
		}
		buffer = ByteBuffer
				.allocateDirect(8 + json.toString().getBytes().length);
		buffer.putInt(Config.ISHAS_NEW_MYMESSAGE);
		
		System.out.println("hasNewMessage3json:"+json.toString());
		put(json.toString());
		writeBuffer();
	}

	// ����鿴������Ϣ
	public void reqPersonINFO(String friendID) {
		try {
			String userID = BaseActivity.getSelf().getUserID();
			Log.i(TAG, "reqPersonMessage()---friendID" + friendID);
			buffer = ByteBuffer.allocateDirect(12 + friendID.getBytes().length
					+ userID.getBytes().length);
			buffer.putInt(Config.USER_GET_INFO);
			put(userID);
			put(friendID);
			writeBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �����ע
	public void reqAttention(String userID, String friendID) {
		try {
			Log.i(TAG, "�û�:" + userID + "Ҫ��עfriendIDΪ:" + friendID);
			buffer = ByteBuffer.allocateDirect(12 + userID.getBytes().length
					+ friendID.getBytes().length);
			buffer.putInt(Config.USER_ADD_ATTENT);
			put(userID);
			put(friendID);
			writeBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ����ȡ����ע
	public void reqCanncel(String userID, String friendID) {
		try {
			Log.i(TAG, "�û�:" + userID + "ȡ����עfriendIDΪ:" + friendID);
			buffer = ByteBuffer.allocateDirect(12 + userID.getBytes().length
					+ friendID.getBytes().length);
			buffer.putInt(Config.USER_CANCEL_ATTENT);
			put(userID);
			put(friendID);
			writeBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �����ȡ���˵Ĺ�ע�б�
	public void reqOtherAttentionList(String friendID) {
		try {
			String userID = BaseActivity.getSelf().getUserID();
			Log.i(TAG, "�û�:" + userID + "�鿴friendIDΪ:" + friendID + "�Ĺ�ע�б�");
			buffer = ByteBuffer.allocateDirect(12 + userID.getBytes().length
					+ friendID.getBytes().length);
			buffer.putInt(Config.USER_OTHER_ATTENT);
			put(userID);
			put(friendID);
			writeBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �����ȡ���˵ķ�˿�б�

	public void reqOtherFollowerList(String friendID) {
		try {
			String userID = BaseActivity.getSelf().getUserID();
			Log.i(TAG, "�û�:" + userID + "�鿴friendIDΪ:" + friendID + "�ķ�˿�б�");
			buffer = ByteBuffer.allocateDirect(12 + userID.getBytes().length
					+ friendID.getBytes().length);
			buffer.putInt(Config.USER_OTHER_FANS);
			put(userID);
			put(friendID);
			writeBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ��ȡ��ע�б�
	public void reqMyAttentionList() {
		try {
			String userID = BaseActivity.getSelf().getUserID();
			Log.i(TAG, "��ȡuserID:" + userID + "�Ĺ�ע�б�");
			buffer = ByteBuffer.allocateDirect(8 + userID.getBytes().length);
			buffer.putInt(Config.USER_GET_ATTENT);
			put(userID);
			writeBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ��ȡ�û���ͷ��
	public void reqUserHead(String userID) {
		try {
			userID = BaseActivity.getSelf().getUserID();
			Log.i(TAG, "��ȡuserID:" + userID + "���û�ͷ��");
			buffer = ByteBuffer.allocateDirect(8 + userID.getBytes().length);
			buffer.putInt(Config.USER_GET_HEAD);
			put(userID);
			writeBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��buffer�з��ַ������ֽ����鳤���Լ����ֽ�����
	 * 
	 * @param string
	 */
	public void put(String string) {
		// buffer.putInt(string.getBytes().length);
		// buffer.put(string.getBytes());
		try {
			buffer.putInt(string.getBytes("GBK").length);
			Log.i(TAG, "condition---�Ž�ȥ:" + string.getBytes("GBK"));
			buffer.put(string.getBytes("GBK"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * �Խ�bufferд��socketchannel.
	 */
	public void writeBuffer() {

		buffer.flip();// ��buffer����ת��
		while (buffer.hasRemaining()) {
			try {

				socketChannel.write(buffer);

				Log.i(TAG, "д��ɹ�");
			} catch (Exception e) {
				Log.i(TAG, "д��ʧ��");
				e.printStackTrace();
			}
		}
		buffer.clear();
	}

	private String getFileName(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			file.mkdirs();
		}
		return file.getName();
	}

	/**
	 * �ӱ��ػ�ȡͼƬ�ֽ�����
	 * 
	 * @param fileFath
	 * @return
	 * @throws IOException
	 */
	private byte[] makeFileToByte(String fileFath) throws IOException {
		File file = new File(fileFath);

		if (file.exists()) {
			Log.i(TAG, "NewNetWorker01---�ļ�����");
		}
		Log.i(TAG, "NewNetWorker01---�ҵ���ͼƬ��·��");
		FileInputStream fis = new FileInputStream(file);
		Log.i(TAG, "NewNetWorker01---�ɹ�������ͼƬ");
		int length = (int) file.length();
		byte[] bytes = new byte[length];
		int temp = 0;
		int index = 0;
		while (true) {
			index = fis.read(bytes, temp, length - temp);
			if (index <= 0)
				break;
			temp += index;
		}
		fis.close();
		return bytes;
	}

	// �ر�socketchannle
	public void close() {
		try {
			this.socketChannel.close();
		} catch (IOException e) {
			Log.i(TAG, "socketchannel �ر��쳣");
			e.printStackTrace();
		}
	}
}
