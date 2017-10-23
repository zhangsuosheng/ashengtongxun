package asheng;

import java.net.Socket;
import java.io.InputStream;

//单条用户信息类
public class UserInfo {
	
	private String uid; // 用户id
	private String usrName; // 用户名
	private Socket socket; // 用户的socket引用

	// 注意，socket中只包含TCP信息，不包含UDP信息，故还需对用户UDP信息进行存储
	private String udpIp; // 用户UDP地址
	private int udpPort; // 用户开放的UDP端口号
	
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUsrName() {
		return usrName;
	}
	public void setUsrName(String usrName) {
		this.usrName = usrName;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public String getUdpIp() {
		return udpIp;
	}
	public void setUdpIp(String udpIp) {
		this.udpIp = udpIp;
	}
	public int getUdpPort() {
		return udpPort;
	}
	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}
	
	
	
	
}
