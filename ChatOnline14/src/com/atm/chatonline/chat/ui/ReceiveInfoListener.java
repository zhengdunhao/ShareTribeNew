package com.atm.chatonline.chat.ui;




/**
 * ������Ϣ�Ľӿ�,������ʵ��receive(Message info)����
 * @author Administrator
 *
 */
public interface ReceiveInfoListener {

	/*public boolean receive(ChatMessage info);
	public boolean isGroupChatting(GroupChatMessage info);*/
	public boolean isChatting(Object info);
}
