package asheng;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.json.JSONObject;

public class UDPMessageServer implements Runnable {
	// DatagramPacket类，即数据包，相当于TCP里的socket类，里面封装了inputstream和outputstream
	private DatagramPacket packet = null;

	public UDPMessageServer(DatagramPacket datagramPacket) {
		this.packet = datagramPacket;
	}

	// 业务处理
	public void run() {
		try{

			// 关键：将udp数据包中的信息解析为JSONObject
			String jsonstr = new String(packet.getData(), 0, packet.getLength());
			JSONObject jsonObject = JSONObject.fromObject(jsonstr);
			

			//接收心跳包
			if (jsonObject.getString("type").equals("reg")) {
				UserOnlineList.getList().updateOnlineUDP(jsonObject.getString("myUID"), packet.getAddress().getHostAddress(),
						packet.getPort());
				System.out.println("有注册信息发来");
				System.out.println(jsonstr);
				System.out.println(packet.getAddress().getHostAddress()+" "+packet.getPort());
			} 
			//接收并转发聊天信息
			else if (jsonObject.getString("type").equals("msg") || jsonObject.getString("type").equals("qr")) {
				UserOnlineList.getList().updateOnlineUDP(jsonObject.getString("myUID"), packet.getAddress().getHostAddress(),
						packet.getPort());
				System.out.println("有中转信息发来");
				System.out.println(jsonObject.getString("msg"));
				UserInfo userinfo = UserOnlineList.getList().getOnlineUserInfo(jsonObject.getString("toUID"));
				//UDP消息中转服务器并不要把具体的聊天内容从包里取出来，只需要将整个DatagramPacket转发过去就可以
				DatagramPacket datagramPacket=new DatagramPacket(packet.getData(),packet.getLength(),InetAddress.getByName(userinfo.getUdpIp()),userinfo.getUdpPort());
				//发送数据包
				datagramSocket.send(datagramPacket);
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("UDP消息中转出现问题");
		}
	}

	public static DatagramSocket datagramSocket=null;
	
	// 启动服务器
	public static void openServer() throws Exception {
		// DatagramSocket类，定义时传入端口号
		datagramSocket = new DatagramSocket(4003);
		// 线程池
		ExecutorService execute = Executors.newFixedThreadPool(1000);
		// 监听
		while (true) {
			// 定义一个UDP数据包来暂存接收到的数据包（UDP数据包最大长度为64K，即数组b最大长度为1024*64）
			byte[] b = new byte[1024 * 10];
			DatagramPacket datagramPacket = new DatagramPacket(b, b.length);
			// 接收数据包，receive()函数返回值为void，并接受一个DatagramPacket参数，它将接收到的数据填入这个DatagramPacket中
			datagramSocket.receive(datagramPacket);
			// 开启线程
			execute.execute(new UDPMessageServer(datagramPacket));
		}

	}
}
