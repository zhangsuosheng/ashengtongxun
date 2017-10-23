package asheng;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

//在线用户表
public class UserOnlineList {
	
	private UserOnlineList(){}
	
	private static UserOnlineList userOnlineList=new UserOnlineList();
	
	public static UserOnlineList getList(){
		return userOnlineList;
	}
	
	private HashMap<String,UserInfo> hashMap=new HashMap<String,UserInfo>();
	
	//用户上线时，检查是否有抢线，并对在线用户表进行修改
	public void regOnline (String uid,Socket socket,String userName){
		
		UserInfo userInfo=hashMap.get(uid);
		if(userInfo!=null){
			try{
				try{
					userInfo.getSocket().getOutputStream().write(4);}catch(Exception e){}
				userInfo.getSocket().close();
			}catch(Exception e){}
		}
		userInfo=new UserInfo();
		userInfo.setUid(uid);
		userInfo.setUsrName(userName);
		userInfo.setSocket(socket);
		hashMap.put(uid, userInfo);
	}
	
	//用于聊天
	public void updateOnlineUDP(String uid,String ip,int port){
		UserInfo userInfo=hashMap.get(uid);
		userInfo.setUdpIp(ip);
		userInfo.setUdpPort(port);
	}
	
	public boolean isOnline(String uid){
		//System.out.println(hashMap.keySet());//测试用：打印在线用户列表
		return hashMap.containsKey(uid);
	}
	
	public void logout(String uid){
		hashMap.remove(uid);
	}
	
	public Set<String> getUserOnline(){
		return hashMap.keySet();
	}
	
	public UserInfo getOnlineUserInfo(String uid){
		return hashMap.get(uid);
	}
}