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
 * 客户端的配置文件，存储所要连接到的服务器的相关信息
 * @author acer
 *服务器IP地址
 *服务器端口号
 *用户登录的用户名、密码（用于断线自动重连）
 */

public class Config {
	//服务器地址
	public static final String IP="";
	
	//登陆服务使用的端口号（在LoginService中使用）
	public static final int LOGIN_PORT=4001;
	
	//注册服务使用的端口号（在RegServer中使用）
	public static final int REG_PORT=4002;
	
	//聊天服务器使用的UDP端口号
	public static final int UDP_PORT=4003;

	
	//自己的用户名和密码
	public static String username;
	public static String password;
	
	//好友列表信息
	public static String haoyou_json_data="";

	
	//存储解析后的好友id
	public static String haoyou_id_data="";
	
	
	/**
	 * 解析好友列表信息
	 * 取出好友列表信息haoyou_json_data字符串中的各种具体值
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
	
	
	//存储在线的好友的id
	public static String haoyou_online_id_data="";
	
	//个人资料
	public static String geren_json_data="";
	
	//
	public static FriendsListPanel friendsListPanel;
	
	//本机的datagramsocket
	public static DatagramSocket datagramSocket=null;
	
	//存放正在聊天的聊天框
	public static Hashtable<String,ChatWindow> chatWindowTable=new Hashtable<String,ChatWindow>();
	
	//双击时打开对应好友的聊天框，若已打开，则置顶
	public static ChatWindow openChatWindow(String uid,String name,String sign,String img,boolean online,Vector msgs){
		if(chatWindowTable.get(uid)==null){
			ChatWindow chatWindow=new ChatWindow(uid,name,sign,img,msgs);
			chatWindow.setOnline(online);//根据用户是否在线设置一下打开的聊天窗中对方头像的颜色
			chatWindow.setVisible(true);
			chatWindowTable.put(uid, chatWindow);
			return chatWindow;
		}else{
			ChatWindow chatWindow=chatWindowTable.get(uid);
			chatWindow.setAlwaysOnTop(true);
			return chatWindow;
		}
	}
	//关闭聊天窗口时从chatWindowTable中去掉对应的ChatWindow
	public static void closeChatWindow(String uid){
		chatWindowTable.remove(uid);
	}
	
	
	
	/**
	 * Create the panel.
	 */
	// 存储好友列表缓存信息
	public static Hashtable<String, FaceJPanel> hashtable = new Hashtable();
}
