package view.service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import net.sf.json.JSONObject;
import view.util.Config;

/**
 * 每隔10秒向UDP服务器发送一个心跳包
 * 作用:
 * 1、防止客户端的UDP端口号自动改变
 * @author 张所晟
 *
 */
public class MessageRegService extends Thread {

	DatagramSocket datagramSocket = null;

	public MessageRegService(DatagramSocket datagramSocket) {
		this.datagramSocket = datagramSocket;
		this.start();
	}

	public void run() {
		byte[] bytes = new byte[1024 * 10];
		String myUID;

		while (true) {
			try {
				myUID = JSONObject.fromObject(Config.geren_json_data).getString("uid");
				bytes = ("{\"type\":\"reg\",\"myUID\":\"" + myUID + "\"}").getBytes();
				DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length,InetAddress.getByName(Config.IP),Config.UDP_PORT);

				datagramSocket.send(datagramPacket);
				// 每隔10秒循环一次
				Thread.sleep(10000);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
