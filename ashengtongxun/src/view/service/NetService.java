package view.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import net.sf.json.JSONObject;
import view.util.Config;

/**
 * 客户端的通讯服务，负责与服务器保持长连接状态 这个类是客户端最重要的一个通讯类，故可以设置为单例类
 * 
 * @author acer 1、更新好友在线状态，5min发一次请求 2、登陆验证 3、退出账户
 */

public class NetService extends Thread {

	// 单例类(饿汉式单例)
	private NetService() {
	}

	private static NetService netService = new NetService();

	public static NetService getNetService() {
		return netService;
	}

	// 这里准备与服务器进行长时间通讯
	public void run() {
		

		try {
			
///////////////////////////////////////////////////////////////////////////
			/**
			 * 在线好友的实时更新
			 */
			
			//解析好友列表json字符串获取所有好友id
			Config.jiexi_haoyou_json_data(Config.haoyou_json_data);
			
			byte[] bytes=new byte[1024*10];
			int len=0;
			
			//每隔5s发送请求在线好友信息，发送自己所有的好友的id，接收在线的好友的id，并将其写入存储在线好友id的文件
			while (run) {
				
				output.write("U0002".getBytes());
				output.flush();
				input.read();
				output.write(Config.haoyou_id_data.getBytes());
				//System.out.println(Config.haoyou_id_data);
				output.flush();
				len=input.read(bytes);
				String online=new String(bytes,0,len);
				
				//System.out.println("在线账户："+online);//测试语句
				
				try {
					//如果在线好友信息发生了变化，则更新
					if (!Config.haoyou_online_id_data.equals(online)) {
						Config.haoyou_online_id_data = online;
						Config.friendsListPanel.gengxin();
					}
				} catch (Exception e) {
				}
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
				}
			}
		} catch (Exception e) {
			// 如果登陆发生异常，则将run置回false！
			run = false;
		}
/////////////////////////////////////////////////////////////////////
	}

	private Socket socket = null;
	private InputStream input = null;
	private OutputStream output = null;
	private Thread thread = null;
	private boolean run = false;

	
	/**
	 * 客户端文件的启动登陆服务的方法，在登陆按钮点击事件中调用
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public JSONObject login() throws UnknownHostException, IOException {
		// 客户用socket向服务器端发出连接请求时，IP地址可能找不到，也可能有IO异常，故应抛出
		// UnknownHostException,IOException
		
		
		// 用服务器的IP和端口号初始化创建一个socket，获取他的InputStream和OutputStream，创建一个字符串存储要发送的用户名和密码
		socket = new Socket(Config.IP, Config.LOGIN_PORT);
		input = socket.getInputStream();
		output = socket.getOutputStream();
		String json_str = "{\"usrname\":\"" + Config.username + "\",\"usrpassword\":\"" + Config.password + "\"}";

		// 给服务器发送用户名和密码
		output.write(json_str.getBytes());
		output.flush();

		// 接收服务器回执的消息
		byte[] bytes = new byte[1024];
		int len = input.read(bytes);
		json_str = new String(bytes, 0, len);

		JSONObject json = JSONObject.fromObject(json_str);
		if (json.getInt("state") == 0) {

			/**
			 * 接下来，我们的任务就是开启持续的网络服务（开线程）
			 */
			// 1、首先要重新开启线程
			if (thread != null) {
				// 判断线程是否还活着
				if (thread.getState() == Thread.State.RUNNABLE) {
					run = false;
					try {
						thread.stop();
					} catch (Exception e) {
					}
				}
			}
//start::注意！！！！！！！获取好友列表信息写入配置文件 和 获取个人资料 两个操作需要在UI界面加载出来之前完成，故将这两个操作放在login()函数中，因为在登陆窗口login.java中，我们写的是先调用login()函数然后再new出来好友列表面板（JFrame）。
			/**
			 * 获取好友列表信息写入配置文件
			 */
			//发送获取好友列表请求
			output.write("U0001".getBytes());
			output.flush();
			
			//接收好友列表信息
			bytes=new byte[1024*10];
			len=input.read(bytes);
			System.out.println(len);
			String jsonstr=new String(bytes,0,len);
			
			//将接收到的好友列表信息的json格式字符串写入配置文件，等待图形化用户界面加载时由图形化界面程序从配置文件中获取好友列表信息并显示
			Config.haoyou_json_data=jsonstr;
			
			//测试语句
			System.out.println("好友列表："+jsonstr);
			
			/**
			 * 获取个人资料
			 */
			//发送获取个人资料请求
			output.write("U0003".getBytes());
			output.flush();
			
			//接收个人资料
			bytes=new byte[1024*10];
			len=input.read(bytes);
			jsonstr=new String(bytes,0,len);
			
			//将接收到的个人资料的json格式字符串写入配置文件，等待图形化用户界面加载时由图形化界面程序从配置文件中获取并显示
			Config.geren_json_data=jsonstr;

			//测试语句
			System.out.println("个人资料："+jsonstr);
			
			/**
			 * 启动UDP服务器
			 * 注意：只有服务器的服务是需要固定在某些端口的，而客户端只需要记住这些端口就行，客户端自己使用的是那些端口一般不用固定
			 */
			
			DatagramSocket datagramSocket=new DatagramSocket();
			//注意，启动udp服务器后要把本机使用的UDPsocket在配置文件中存一下
			Config.datagramSocket=datagramSocket;
			//启动心跳包
			new MessageRegService(datagramSocket);
			//启动收发消息
			new MessageService(datagramSocket);
			System.out.println("服务程序开启");
//end::!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			run = true;
			thread = new Thread(this);
			thread.start();

		}
		return json;
	}

}
