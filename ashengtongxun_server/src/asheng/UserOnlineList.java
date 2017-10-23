package asheng;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

//�����û���
public class UserOnlineList {
	
	private UserOnlineList(){}
	
	private static UserOnlineList userOnlineList=new UserOnlineList();
	
	public static UserOnlineList getList(){
		return userOnlineList;
	}
	
	private HashMap<String,UserInfo> hashMap=new HashMap<String,UserInfo>();
	
	//�û�����ʱ������Ƿ������ߣ����������û�������޸�
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
	
	//��������
	public void updateOnlineUDP(String uid,String ip,int port){
		UserInfo userInfo=hashMap.get(uid);
		userInfo.setUdpIp(ip);
		userInfo.setUdpPort(port);
	}
	
	public boolean isOnline(String uid){
		//System.out.println(hashMap.keySet());//�����ã���ӡ�����û��б�
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