package asheng;

import java.net.Socket;
import java.io.InputStream;

//�����û���Ϣ��
public class UserInfo {
	
	private String uid; // �û�id
	private String usrName; // �û���
	private Socket socket; // �û���socket����

	// ע�⣬socket��ֻ����TCP��Ϣ��������UDP��Ϣ���ʻ�����û�UDP��Ϣ���д洢
	private String udpIp; // �û�UDP��ַ
	private int udpPort; // �û����ŵ�UDP�˿ں�
	
	
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
