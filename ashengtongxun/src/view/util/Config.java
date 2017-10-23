package view.util;

import java.net.DatagramSocket;
import java.util.Hashtable;
import java.util.Vector;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import view.ChatWindow;
import view.FaceJPanel;
import view.FriendsListPanel;

/**
 * �ͻ��˵������ļ����洢��Ҫ���ӵ��ķ������������Ϣ
 * @author acer
 *������IP��ַ
 *�������˿ں�
 *�û���¼���û��������루���ڶ����Զ�������
 */

public class Config {
	//��������ַ
	public static final String IP="";
	
	//��½����ʹ�õĶ˿ںţ���LoginService��ʹ�ã�
	public static final int LOGIN_PORT=4001;
	
	//ע�����ʹ�õĶ˿ںţ���RegServer��ʹ�ã�
	public static final int REG_PORT=4002;
	
	//���������ʹ�õ�UDP�˿ں�
	public static final int UDP_PORT=4003;

	
	//�Լ����û���������
	public static String username;
	public static String password;
	
	//�����б���Ϣ
	public static String haoyou_json_data="";

	
	//�洢������ĺ���id
	public static String haoyou_id_data="";
	
	
	/**
	 * ���������б���Ϣ
	 * ȡ�������б���Ϣhaoyou_json_data�ַ����еĸ��־���ֵ
	 * @param haoyou_json_data
	 */
	public static void jiexi_haoyou_json_data(String haoyou_json_data){
		JSONArray json=JSONArray.fromObject(haoyou_json_data);
		StringBuffer stringBuffer=new StringBuffer();
		for(int i=0;i<json.size();i++){
			JSONObject jsonobj=(JSONObject)json.get(i);
			stringBuffer.append(jsonobj.getString("uid"));
			stringBuffer.append(",");
		}
		haoyou_id_data=stringBuffer.toString();
	}
	
	
	//�洢���ߵĺ��ѵ�id
	public static String haoyou_online_id_data="";
	
	//��������
	public static String geren_json_data="";
	
	//
	public static FriendsListPanel friendsListPanel;
	
	//������datagramsocket
	public static DatagramSocket datagramSocket=null;
	
	//�����������������
	public static Hashtable<String,ChatWindow> chatWindowTable=new Hashtable<String,ChatWindow>();
	
	//˫��ʱ�򿪶�Ӧ���ѵ���������Ѵ򿪣����ö�
	public static ChatWindow openChatWindow(String uid,String name,String sign,String img,boolean online,Vector msgs){
		if(chatWindowTable.get(uid)==null){
			ChatWindow chatWindow=new ChatWindow(uid,name,sign,img,msgs);
			chatWindow.setOnline(online);//�����û��Ƿ���������һ�´򿪵����촰�жԷ�ͷ�����ɫ
			chatWindow.setVisible(true);
			chatWindowTable.put(uid, chatWindow);
			return chatWindow;
		}else{
			ChatWindow chatWindow=chatWindowTable.get(uid);
			chatWindow.setAlwaysOnTop(true);
			return chatWindow;
		}
	}
	//�ر����촰��ʱ��chatWindowTable��ȥ����Ӧ��ChatWindow
	public static void closeChatWindow(String uid){
		chatWindowTable.remove(uid);
	}
	
	
	
	/**
	 * Create the panel.
	 */
	// �洢�����б�����Ϣ
	public static Hashtable<String, FaceJPanel> hashtable = new Hashtable();
}
