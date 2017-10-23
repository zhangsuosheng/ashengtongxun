package asheng;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import db.UserService;
import db.LoginCheck;
import db.PasswordException;
import db.StateException;
import db.UserInfo2;
import db.UserInfo3;
import db.UsernameNotFoundException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//登陆类，每有一个用户发起socket连接，系统就要创建一个该类来为其服务
public class LoginServer implements Runnable {

	// 定义一个用于服务用户的socket类
	private Socket socket = null;

	// 构造函数将socket类初始化
	public LoginServer(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		String uid = null;
		InputStream in = null;
		OutputStream out = null;
		try {
			// 用Scoket.getInputStream()和Socket.getOutputStream()获取用户发来的socket中的inputstream和outputstream
			in = socket.getInputStream();
			out = socket.getOutputStream();

			// 创建一个最大1024字节的byte数组来接收socket中的inputsteam中的信息
			byte[] bytes = new byte[1024];
			// 用InputStream.read(byte[])方法 读取一串字符流存入byte[]数组中
			int len = in.read(bytes);
			// 通过使用平台的默认字符集解码指定的 byte[]数组，构造一个新的 String
			String json_str = new String(bytes, 0, len);

			JSONObject json = JSONObject.fromObject(json_str);
			String usrname = json.getString("usrname");
			String usrpassword = json.getString("usrpassword");

			
			// 验证输入是否为正确的手机号码
			if (usrname.trim().length() == 11) {
				try {
					Long.parseLong(usrname);
				} catch (NumberFormatException e) {
					out.write("{\"state\":4,\"msg\":\"用户名格式错误\"}".getBytes());
					// 清空缓冲区，一些输出流的实现是带缓冲的，你往里面写，不一定立刻输出而是要等缓冲区满或flush()
					out.flush();
					//注意，在线程抛出了某种异常后，要在catch或finally块中用return或close()等语句关闭该线程
					return;
				}
			}
			
			

			/**
			 * 判断完用户名是否符合格式后即将用户名和密码传入LoginCheck类进行验证
			 * 验证出错则通过异常处理向客户端发送信息并关闭线程
			 * 验证成功则将用户信息写入在线用户列表
			 * 
			 */
			
			try {
				uid = new LoginCheck().loginForPhone(usrname, usrpassword);

			} catch (UsernameNotFoundException e) {
				out.write("{\"state\":2,\"msg\":\"用户名错误\"}".getBytes());
				out.flush();
				e.printStackTrace();
				return;
			} catch (PasswordException e) {
				out.write("{\"state\":1,\"msg\":\"密码错误\"}".getBytes());
				out.flush();
				e.printStackTrace();
				return;
			} catch (StateException e) {
				out.write("{\"state\":3,\"msg\":\"抱歉，您已被封号\"}".getBytes());
				out.flush();
				e.printStackTrace();
				return;
			} catch (SQLException e) {
				out.write("{\"state\":5,\"msg\":\"抱歉，未知的错误发生了\"}".getBytes());
				out.flush();
				e.printStackTrace();
				return;
			}
			
			
			//登陆成功返回一个消息给客户端
			out.write("{\"state\":0,\"msg\":\"登陆成功！\"}".getBytes());
			out.flush();
			//登陆成功，登记登陆信息
			UserOnlineList.getList().regOnline(uid, socket, usrname);

			
			
			while(true){
				
				//因为登陆成功后bytes数组就要负责接收在线好友等数据量较大的信息，则给它多分配点字节
				bytes=new byte[2048];
				
				
				len=in.read(bytes);
				String command=new String(bytes,0,len);
				
				if(command.equals("U0001")){
					//更新好友信息
					
					//获取UserService类的getHaoyouliebiao方法传过来的vector数组
					Vector<UserInfo2> userinfos =new UserService().getHaoyouliebiao(uid);
					//用JSONArray.fromObject()静态方法把Vector数组转化为JSON数组，再用toString转化为String字符串，再转化为byte流发送给客户端
					out.write(JSONArray.fromObject(userinfos).toString().getBytes());
					out.flush();
				
				}else if(command.equals("U0002")){
					/**
					 * 更新在线好友：先接受客户端发来的他的所有好友的id，然后查询这些好友是否在线
					 */
					//提醒客户端服务器已准备好，请发送你所有好友的id过来，我帮你查询哪些在线
					out.write(1);
					out.flush();
					//获得该客户的所有好友的id
					len=in.read(bytes);//传来数据的格式：1，2，3，4，5
					String str=new String(bytes,0,len);
					String[] ids=str.split(",");
					
					//定义一个储存返回查询结果的字符串变量
					StringBuffer stringBuffer=new StringBuffer();
					
					for(String string:ids){
						if(UserOnlineList.getList().isOnline(string)){
							//将查询到的好友的id序列，用StringBuffer.append()插入到字符串变量中
							stringBuffer.append(string);
							stringBuffer.append(",");
						}
					}
					
					//判断查询结果是否为空，为空则返回一句话
					if(stringBuffer.length()==0){
						out.write("notFound".getBytes());
						out.flush();
					}
					//判断查询结果是否为空，不为空则将字符串变量转为字符串在转为byte流返回给客户端
					else{
						out.write(stringBuffer.toString().getBytes());
						out.flush();
					}
					
				}else if (command.equals("U0003")){
					//更新个人资料
					
					UserInfo3 userinfo3=new UserService().getUserinfo(uid);
					//System.out.println(userinfo3);
					//System.out.println(JSONObject.fromObject(userinfo3).toString());
					out.write(JSONObject.fromObject(userinfo3).toString().getBytes());
					out.flush();
					
				}else if (command.equals("U0004")){
					//修改个人资料
				}else if (command.equals("EXIT")){
					//退出登录
					UserOnlineList.getList().logout(uid);
					//跳出循环（不再接收客户端信息）
					return;
				}
				
			}
			
		} 
		
		
		
		//对整个run方法中的异常的捕获处理
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		//对整个run方法（线程）的结束处理:该线程使用结束后把socket、inputstream、outputstream关闭
		finally {
			// 结束后把连接关闭
			try {
				// 如果遇到突然关闭或者是关闭的话 我们需要在列表里去除此账户
				UserOnlineList.getList().logout(uid);
				in.close();
				out.close();
				socket.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	
	
	
	
	// 启动服务器的登陆服务的方法，注意它是静态的
	public static void openServer() throws Exception {
		// 定义了一个最多1000个线程的线程池
		ExecutorService execute = Executors.newFixedThreadPool(1000);

		// 开放服务器的4001端口
		ServerSocket server = new ServerSocket(4001);

		// 登陆服务方法中要用死循环进行监听
		while (true) {
			// 用server.accept()接受客户端发来的socket，定义一个引用存储它
			Socket socket = server.accept();
			// setSotimeout(10000)是表示如果对方连接状态10秒没有收到数据的话强制断开客户端。如果想要长连接的话，可以使用心跳包来通知服务器，也就是我没有发给你数据，但是我告诉你我还活着。
			socket.setSoTimeout(10000);
			// 用execute.execute(类的对象）创建一个新的线程并执行
			execute.execute(new LoginServer(socket));
		}
	}
	
	/**
	 * 让该服务类可以启动，服务器端服务程序入口
	 * @param args
	 */
	public static void main(String[] args){
		try {
			openServer();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
}
