package com.atm.chatonline.chat.net;

import java.io.IOException;
import java.util.Map;

import android.util.Log;

import com.atm.chatonline.chat.ui.ReceiveInfoListener;



public class Communication {

	public NewNetWorker newNetWorker01;
	private static Communication instance;
	private String tag="Communication";
	public Communication() {
		newNetWorker01 = new NewNetWorker();
		newNetWorker01.start();
	}

	public static Communication newInstance() {
		if (instance == null)
			instance = new Communication();
			Log.i(">>>>>", "666");
		return instance;
	}
	//��Ӽ���
	public void addReceiveInfoListener(String state,
			ReceiveInfoListener listener) {
		newNetWorker01.addReceiveInfoListener(state, listener);
	}
	
	public void removeReceiveInfoListener(){
		newNetWorker01.removeReceiveInfoListener();
	}
	
//	//ע������
//	public void unRegisterReceiveInfoListener(String state){
//		if(newNetWorker01.listeners.containsKey(state)){
//			newNetWorker01.listeners.remove(state);
//		}
//	}
	//��¼����
	public void reqLogin(String userID, String pwd) {
		if(newNetWorker01.sendMsg==null){
			Log.i("communication", "senmsg is null");
		}else{
			Log.i("communication", "senmsg is not null");
		}
		Log.i(tag, "userID:"+userID+"��pwd��"+pwd);
		newNetWorker01.sendMsg.login(userID, pwd);
	}

	// �������͸���Ȥ��Ⱥ
	public void reqHobbyGroup(String userId) {
		newNetWorker01.sendMsg.reqHobbyGroup(userId);
	}

	// �������͸���Ȥ���û�
	public void reqHobbyUser(String userId) {
		newNetWorker01.sendMsg.reqHobbyUser(userId);
	}

	// ����鿴������Ϣ
	public void reqPersonINFO(String friendID) {
		newNetWorker01.sendMsg.reqPersonINFO(friendID);
	}

	// �����ע
	public void reqAttention(String userID, String friendID) {
		newNetWorker01.sendMsg.reqAttention(userID, friendID);
	}

	// ����ȡ����ע
	public void reqCanncel(String userID, String friendID) {
		newNetWorker01.sendMsg.reqCanncel(userID, friendID);
	}
	//����˽���ı�
	public boolean sendText(String userID, String friendID, String time,
			String content) {
		return newNetWorker01.sendMsg.sendText(userID, friendID, time, content);
	}
//����Ⱥ���ı�
	public boolean sendGroupText(String userId, String groupId, String content) {

		return newNetWorker01.sendMsg.sendCrowdMessage(userId,
				Integer.parseInt(groupId), content);
	}
//���ʹ���Ⱥ������
	public void sendCreateGroupMsg(String userId, String groupName,
			String groupDesc, String groupLabel, int groupProperty,
			String groupHeadImg, String path) throws IOException {// ***
		newNetWorker01.sendMsg.sendCreateGroup(userId, groupName, groupDesc,
				groupLabel, groupProperty, groupHeadImg, path);
	}
//���Ͳ鿴Ⱥ��������
	public void sendLookGroupData(int groupId) {
		newNetWorker01.sendMsg.sendLookGroupData(groupId);
	}
//����Ⱥ����
	public void findCrowd(String condition, String userId) {
		newNetWorker01.sendMsg.findCrowd(condition, userId);
	}
//���Ͳ����Լ�Ⱥ������
	public void sendFindMyGroup(String userId) {
		newNetWorker01.sendMsg.sendFindMyGroup(userId);
	}
	//���Һ����б�����
	public void findUser(String condition,String userId) {
		newNetWorker01.sendMsg.findUser(condition,userId);
	}
	
	//��ȡ˽�ĵ�������Ϣ
	public void getOfflineMessage(String userId){
		newNetWorker01.sendMsg.getOfflineMessage(userId);
	}
	
	//�����˳��˳�����������������
	public void exit(String userId){
		newNetWorker01.sendMsg.exit(userId);
		
	}
	//�ر�socketchannel
	
	public void shutDownSocketChannel(){
		newNetWorker01.sendMsg.close();
		
	}
	
	//��socketchannel
	public void openSocketChannel(){
//		newNetWorker01.connect();
//		newNetWorker01.interrupt();
//		newNetWorker01.start();
		newNetWorker01 = new NewNetWorker();
		newNetWorker01.start();
	}
	
	//�����ȡ���˵Ĺ�ע�б�
	public void reqOtherAttentionList(String friendID){
		newNetWorker01.sendMsg.reqOtherAttentionList(friendID);
	}
	//�����ȡ�ҵĹ�ע�б�
	public void reqMyAttentionList(){
		newNetWorker01.sendMsg.reqMyAttentionList();
	}
	//�����ȡ���˵ķ�˿�б�
	public void reqOtherFollowerList(String friendID){
		newNetWorker01.sendMsg.reqOtherFollowerList(friendID);
	}
	
	////��ȡ�û���ͷ��
	public void reqUserHead(String userID){
		newNetWorker01.sendMsg.reqUserHead(userID);
	}
	
	//˽�ķ���ͼƬ
	public boolean sendImg(String userID,String friendID,String time,String filePath){
		return newNetWorker01.sendMsg.sendImg(userID,friendID,time,filePath);
	}
	
	//���ͻ�ȡ�ҵ���Ϣ������
	public boolean reqMyMsg(String userId,int type){
		return newNetWorker01.sendMsg.getMyMessage(userId, type);
	}
	
	//
	public void sendIsHasNewMsg(String userId){
		newNetWorker01.sendMsg.sendIsHasNewMsg(userId);
	}
}
