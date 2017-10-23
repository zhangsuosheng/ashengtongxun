package view.service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import model.MessagePool;
import net.sf.json.JSONObject;
import view.ChatWindow;
import view.util.Config;

/**
 * 接收服务器转发来的UDP消息
 * @author 张所晟
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
				
				//测试用
				String jsonstr = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
				JSONObject jsonObject = JSONObject.fromObject(jsonstr);
				System.out.println("以下是测试消息");
				System.out.println(jsonObject.getString("msg"));
				MessagePool.getMessagePool().addMessage(jsonstr);
				System.out.println(jsonstr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
