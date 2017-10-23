package asheng;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.json.JSONObject;

public class UDPMessageServer implements Runnable {
	// DatagramPacket�࣬�����ݰ����൱��TCP���socket�࣬�����װ��inputstream��outputstream
	private DatagramPacket packet = null;

	public UDPMessageServer(DatagramPacket datagramPacket) {
		this.packet = datagramPacket;
	}

	// ҵ����
	public void run() {
		try{

			// �ؼ�����udp���ݰ��е���Ϣ����ΪJSONObject
			String jsonstr = new String(packet.getData(), 0, packet.getLength());
			JSONObject jsonObject = JSONObject.fromObject(jsonstr);
			

			//����������
			if (jsonObject.getString("type").equals("reg")) {
				UserOnlineList.getList().updateOnlineUDP(jsonObject.getString("myUID"), packet.getAddress().getHostAddress(),
						packet.getPort());
				System.out.println("��ע����Ϣ����");
				System.out.println(jsonstr);
				System.out.println(packet.getAddress().getHostAddress()+" "+packet.getPort());
			} 
			//���ղ�ת��������Ϣ
			else if (jsonObject.getString("type").equals("msg") || jsonObject.getString("type").equals("qr")) {
				UserOnlineList.getList().updateOnlineUDP(jsonObject.getString("myUID"), packet.getAddress().getHostAddress(),
						packet.getPort());
				System.out.println("����ת��Ϣ����");
				System.out.println(jsonObject.getString("msg"));
				UserInfo userinfo = UserOnlineList.getList().getOnlineUserInfo(jsonObject.getString("toUID"));
				//UDP��Ϣ��ת����������Ҫ�Ѿ�����������ݴӰ���ȡ������ֻ��Ҫ������DatagramPacketת����ȥ�Ϳ���
				DatagramPacket datagramPacket=new DatagramPacket(packet.getData(),packet.getLength(),InetAddress.getByName(userinfo.getUdpIp()),userinfo.getUdpPort());
				//�������ݰ�
				datagramSocket.send(datagramPacket);
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("UDP��Ϣ��ת��������");
		}
	}

	public static DatagramSocket datagramSocket=null;
	
	// ����������
	public static void openServer() throws Exception {
		// DatagramSocket�࣬����ʱ����˿ں�
		datagramSocket = new DatagramSocket(4003);
		// �̳߳�
		ExecutorService execute = Executors.newFixedThreadPool(1000);
		// ����
		while (true) {
			// ����һ��UDP���ݰ����ݴ���յ������ݰ���UDP���ݰ���󳤶�Ϊ64K��������b��󳤶�Ϊ1024*64��
			byte[] b = new byte[1024 * 10];
			DatagramPacket datagramPacket = new DatagramPacket(b, b.length);
			// �������ݰ���receive()��������ֵΪvoid��������һ��DatagramPacket�������������յ��������������DatagramPacket��
			datagramSocket.receive(datagramPacket);
			// �����߳�
			execute.execute(new UDPMessageServer(datagramPacket));
		}

	}
}
