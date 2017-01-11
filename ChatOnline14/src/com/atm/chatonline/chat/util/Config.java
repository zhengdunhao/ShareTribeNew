package com.atm.chatonline.chat.util;

/**
 * �滮����Config.����ϵͳ��ö��ÿһ�������¼� {������Ϣ����(1001-1110)} {�������Ͳ���(100-999)}
 * {Ⱥ����(1200-1399)} {�û�����(1400-1599)}{�ɹ�:1,ʧ��:0} ÿ��50֮��ΪԤ���˺� 2000-3000�պ�װ��
 * 3001-4000��ϼװ��
 * 
 * @version 2.0
 * @author ye
 * @2015.8.18
 */
public interface Config {

	// * ��*������ʾ�����иĶ�
	/**
	 * ������Ϣ����
	 */
	// ˽��(
	public static final int MESSAGE_TO = 1001;// ����˽����Ϣ
	public static final int MESSAGE_FROM = 1002;// ����˽����Ϣ
	public static final int MESSAGE_TEXT = 10003;// *˽���ı���Ϣ
	public static final int MESSAGE_IMG = 1004;// *˽��ͼƬ��Ϣ
	public static final int MESSAGE_SOUND = 1005;// * Ⱥ��������Ϣ(δ�����������ڴ�)
	public static final int MESSAGE_AUDIO = 1006;// *Ⱥ��������Ϣ(δ�����������ڴ�)
	public static final int MESSAGE_SUCCESS = 1007;// ��Ϣ���ͳɹ�
	public static final int MESSAGE_FAILED = 1008; // ��Ϣ����ʧ��
	public static final int MESSAGE_OFFLINE = 1009;// ��ȡ������Ϣ

	// Ⱥ����
	public static final int CROWD_MESSAGE_TO = 1101; // ����Ⱥ����Ϣ
	public static final int CROWD_MESSAGE_FROM = 1102;// ����Ⱥ����Ϣ
	public static final int CROWD_MESSAGE_TEXT = 1103; // Ⱥ���ı���Ϣ
	public static final int CROWD_MESSAGE_IMG = 1104;// Ⱥ��ͼƬ��Ϣ
	public static final int CROWD_MESSAGE_SOUND = 1105;// Ⱥ��������Ϣ(δ�����������ڴ�)
	public static final int CROWD_MESSAGE_AUDIO = 1106;// Ⱥ��������Ϣ(δ�����������ڴ�)
	public static final int CROWD_MESSAGE_SUCCESS = 1107;// Ⱥ����Ϣ���ͳɹ�
	public static final int CROWD_MESSAGE_FAILED = 1108; // Ⱥ����Ϣ����ʧ��

	/** �������������� **/
	public static final int REQUEST_LOGIN = 100;// ��¼
	public static final int REQUEST_REGIST = 101;// ע��
	public static final int REQUEST_EXIT = 102;
	public static final int REQUEST_BE_OFF = 103;
	public static final int REFRESH_UI = 99; // ����UI��ͼ *�غ���

	public static final int RESULT_LOGIN = 200;
	public static final int RESULT_REGIST = 201;
	public static final int RESULT_UPDATE_HEAD = 202;

	public static final int IMG_NEED_UPDATE = 600;
	public static final int IMG_NO_UPDATE = 601;

	/**
	 * Ⱥ�Ĳ���
	 */
	public static final int CROWD_CREATE = 1201;// ����Ⱥ��
	public static final int CROWD_MY = 1202;// �����ҵ�Ⱥ
	public static final int CROWD_FIND = 1203;// ����Ⱥ
	public static final int CROWD_INTIVE = 1204;// �����Ⱥ
	public static final int CROWD_APPLY = 1205;// �����Ⱥ
	public static final int CROWD_GET_INFO = 1206;// Ⱥ��Ϣ
	public static final int CROWD_GET_HEAD = 1207;// ��ȡȺͷ��
	public static final int CROWD_SET_INFO = 1208;// ȺԱ�Ѿ���Ⱥ��
	public static final int CROWD_SET_HEAD = 1209;// ����Ⱥͷ��
	public static final int CROWD_FOUND = 1210;// foundȺ ���ܸ���Ȥ��Ⱥ��������Ⱥ

	/**
	 * Ⱥ�����Ļظ�
	 */
	public static final int CROWD_RESULT_CREATE = 1301; // ����Ⱥ�Ľ��
	public static final int CROWD_RESULT_MY = 1302;// �����ҵ�Ⱥ�Ľ��
	public static final int CROWD_RESULT_FIND = 1303;// ����Ⱥ�Ľ��
	public static final int CROWD_RESULT_INTIVE = 1304; // ������
	public static final int CROWD_RESULT_APPLY = 1305;// �����Ⱥ���
	public static final int CROWD_RESULT_GETINFO = 1306;// Ⱥ��Ϣ
	public static final int CROWD_RESULT_GETHEAD = 1307;// �����Ⱥ���
	public static final int CROWD_RESULT_SETINFO = 1308;// �����Ⱥ���
	public static final int CROWD_RESULT_SETHEAD = 1309;// �����Ⱥ���
	public static final int CROWD_RESULT_FOUND = 1310;// ���ҿ��ܸ���ȤȺ�Ľ��

	public static final int CROWD_LIST = 1321; // Ⱥ�б�
	public static final int CROWD_USER_ALREADY = 1322; // ȺԱ�Ѿ���Ⱥ��
	public static final int CROWD_FULL = 1323; // Ⱥ����
	public static final int CROWD_NO_EXIST = 1324;// Ⱥ������

	/**
	 * �ҵ���Ϣ
	 */
	public static final int MY_MESSAGE = 1154; 
	public static final int ISHAS_NEW_MYMESSAGE = 1153; // �Ƿ����µ��ҵ���Ϣ
	
	public static final int CROWD_FIND_SUCCESS = 1350;// ����Ⱥ�ɹ����ж��Ƿ���ø�������Ⱥ����---֣
	/**
	 * �û�����
	 */
	public static final int USER_LIST = 1401;// �û��б�
	public static final int USER_GET_ATTENT = 1402;// �ҹ�ע��
	public static final int USER_GET_ATTENTED = 1403;// ��ע�ҵ�
	public static final int USER_GET_INFO = 1404;// �û���Ϣ
	public static final int USER_GET_HEAD = 1405;// �û�ͷ��
	public static final int USER_ADD_ATTENT = 1406;// ��ӹ�ע
	public static final int USER_CANCEL_ATTENT = 1407;// ȡ����ע
	public static final int USER_SET_INFO = 1408;// �޸�����
	public static final int USER_SET_HEAD = 1409;// �޸�ͷ��
	public static final int USER_FIND = 1410;// �����û�

	public static final int USER_FIND_SUCCESS = 1450; // �����û��ɹ����ж��Ƿ���ø��������û�����---֣
	public static final int USER_FOUND = 1411;// ���ҿ��ܸ���Ȥ���û�
	public static final int USER_OTHER_ATTENT = 1412;// ���˹�ע��
	public static final int USER_OTHER_FANS = 1413;// ���˵ķ�˿

	// �û��������
	public static final int USER_RESULT_LIST = 1501;// �û��б�
	public static final int USER_RESULT_GETATTENT = 1502;// �ҹ�ע��
	public static final int USER_RESULT_GETATTENTED = 1503;// ��ע�ҵ�
	public static final int USER_RESULT_GETINFO = 1504;// �û���Ϣ
	public static final int USER_RESULT_GETHEAD = 1505;// �û�ͷ��
	public static final int USER_RESULT_ADDATTENT = 1506;// ��ӹ�ע
	public static final int USER_RESULT_CANCELATTENT = 1507;// ȡ����ע
	public static final int USER_RESULT_SETINFO = 1508;// �޸�����
	public static final int USER_RESULT_SETHEAD = 1509;// �޸�ͷ��
	public static final int USER_RESULT_FIND = 1510;// �����û�
	public static final int USER_RESULT_FOUND = 1511;// ���Ҹ���Ȥ���û��Ľ��
	public static final int USER_RESULT_OATTENT = 1512;// ��ȡ���˵Ĺ�ע�Ľ��
	public static final int USER_RESULT_OFANS = 1513;// ��ȡ���˵ķ�˿�Ľ��

	// ����
	public static final int USER_NO_FOUND = 1520;// �û�û�ҵ�
	public static final int USER_LOGIN_ALREADY = 1521;// �û��ѵ�¼
	public static final int USER_NO_ONLINE = 1522; // �û�������

	// �պ�
	public static final int NOT_FOUND_HOBBY_GOURP = 2000;// û���ҵ�����Ȥ��Ⱥ
	public static final int NOT_FOUND_HOBBY_USER = 2001;// û���ҵ�����Ȥ���û�
	public static final int RELATIONSHIP_ATTENTION = 2002;// ��ע��־
	public static final int RELATIONSHIP_FOLLOER = 2003;// ��˿��־
	public static final int RELATIONSHIP_NO_MATTER = 2004;// û�й�ϵ�ı�־
	public static final int USER_ATTENTION_LIST_SUCCESS = 2005; // �ҵ����ҵĹ�ע�б�
	public static final int USER_HEAD_SCUUESS = 2006;// �ɹ��õ��û���ͷ��
	public static final int SEND_NOTIFICATION = 2007;// ������Ϣ֪ͨ
	public static final int REFRESH_TOP_NEWS = 2008;// ����ͷ������
	public static final int LOGIN_SUCCESS = 2009;// ����ɹ�
	public static final int PERSONALMESSAGEACTIVITY = 2010;// ��������PersonalMessageActivity
	public static final int BBSPOSTDETAILVIEW = 2011;// ��������BBSPostDetailView
	public static final int USER_CENTER = 2012;// ��������User_Center
	public static final int CHANGE_PWD = 2013;//��������
	public static final int CHANGE_EMAIL = 2014;//��������
	public static final int FEEDBACK_MESSAGE = 2015;//������Ϣ
	

	// other
	public static final int USER_MOVE = 9998;// �ƶ��û�
	public static final int MAP_INFO = 9999;
	/**
	 * ����������ͬ��ظ�
	 */
	public static final int NOT_FOUND = 2;// �������Ĳ����Ļظ�--û���ҵ�
	public static final int SUCCESS = 1;
	public static final int FAILED = 0;
	public static final int AUTOLOGIN = 3;// �Զ���¼
	public static final int FIRSTLOGIN = 4;// ��һ�ε�¼
	public static final int BE_OFF_LOGIN = 5;// ����֮����ת����¼ҳ��
	public static final int USED = 6;//��ʾ�ѱ�ʹ��

	// �µ�ϼ
	public static final int MAINVIEW_UPDATEUI = 3001;// ���������
	public static final int FULLTIME = 3002;// ȫְ�ĸ���
	public static final int ALLKIND = 3003;// ȫ��ְҵ�ĸ���
	public static final int PARTTIME = 3004;// ��ְ�ĸ���
	public static final int INTERNSHIP = 3005;// ʵϰ�ĸ���
	public static final int SEARCHESSAY = 3006;// �������ӵĸ���
	public static final int SEARCHUSER = 3007;// �����û��ĸ���
	public static final int LOGIN_AFTER_REGISTER=6;//ע��֮���ٵ�¼
}
