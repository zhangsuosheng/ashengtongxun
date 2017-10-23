package view.service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import model.MessagePool;
import net.sf.json.JSONObject;
import view.ChatWindow;
import view.util.Config;

/**
 * ���շ�����ת������UDP��Ϣ
 * @author ������
 *
 */
public class MessageService extends Thread {

	DatagramSocket datagramSocket = null;

	public MessageService(DatagramSocket datagramSocket) {
		this.datagramSocket = datagramSocket;
		this.start();
	}

	public void run() {
		try {
			byte[] bytes = new byte[1024 * 32];
			DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);

			while (true) {
				datagramSocket.receive(datagramPacket);
				
				//������
				String jsonstr = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
				JSONObject jsonObject = JSONObject.fromObject(jsonstr);
				System.out.println("�����ǲ�����Ϣ");
				System.out.println(jsonObject.getString("msg"));
				MessagePool.getMessagePool().addMessage(jsonstr);
				System.out.println(jsonstr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
