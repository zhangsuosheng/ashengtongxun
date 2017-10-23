package asheng;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import db.UserNameUsedException;
import db.UserService;
import net.sf.json.JSONObject;

public class RegServer implements Runnable {

	private Socket socket = null;

	public RegServer(Socket socket) {
		this.socket = socket;
	}

	// 存储已发送的验证码
	HashMap<String, String> hashMap_code = new HashMap<String, String>();

	public void run() {
		InputStream in = null;
		OutputStream out = null;

		try {
			in = socket.getInputStream();
			out = socket.getOutputStream();
			byte[] bytes = new byte[2048];
			int len = in.read(bytes);
			String json_str = new String(bytes, 0, len);

			JSONObject json = JSONObject.fromObject(json_str);

			// 接收请求字符串type
			String type = json.getString("type");
			/****************************************************************************************
			 * 生成验证码、发送验证码、接收并验证用户填写的验证码是否正确并执行注册功能 // 若请求字符串内容为code，则发送验证码 if
			 * (type.equals("code")) { String usrname =
			 * json.getString("usrname");
			 * 
			 * //生成验证码 StringBuffer code = new StringBuffer(); Random random =
			 * new Random(); for (int i = 0; i < 6; i++) {
			 * code.append(random.nextInt(10)); } // 判断是否为手机号，给手机号发送验证码 if
			 * (usrname.trim().length() == 11) { try {//判断手机号格式是否正确
			 * Long.parseLong(usrname);//
			 * 将字符串解析为long值，若该字符串为不能转化为long类型（并非都由数字组成）则会抛出异常
			 * 
			 * hashMap_code.put(usrname, code.toString());
			 * SendCode.send(usrname, code.toString());
			 * out.write("验证码发送成功".getBytes()); out.flush(); } catch (Exception
			 * e) { out.write("手机号格式错误，验证码发送失败".getBytes()); out.flush(); } } //
			 * 判断是否为邮箱，给邮箱发验证码 else { if (usrname.indexOf("@") >= 0) {//
			 * 判断邮箱格式是否正确
			 * 
			 * hashMap_code.put(usrname, code.toString());
			 * SendCode.send(usrname, code.toString());
			 * out.write("验证码发送成功".getBytes()); out.flush(); }else{
			 * out.write("邮箱格式错误，验证码发送失败".getBytes()); out.flush(); } } }
			 * //若请求字符串为reg，则执行验证验证码和注册用户功能 else if (type.equals("reg")) {
			 * String usrname=json.getString("usrname"); String
			 * password=json.getString("usrpassword"); String
			 * code=json.getString("code"); String
			 * code1=hashMap_code.get(usrname);
			 * if(code1!=null){//无论验证码是否验证成功，都让验证码失效
			 * hashMap_code.remove(usrname); } if(code1.equals(code1)){
			 * out.write("{\"state\":\"1\",\"msg\":\"验证码错误，请重新获取\"}".getBytes());
			 * out.flush(); } …………………（执行用户注册的其他代码） }
			 *********************************************************************************************/
			if (type.equals("reg")) {
				String usrname = json.getString("usrname");
				String usrpassword = json.getString("usrpassword");
				try {
					new UserService().regUser(usrname, usrpassword);
				} catch (UserNameUsedException e) {
					out.write("{\"state\":\"1\",\"msg\":\"该用户名已存在！\"}".getBytes());
					out.flush();
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO 自动生成的 catch 块
					out.write("{\"state\":\"2\",\"msg\":\"未知的错误发生了！\"}".getBytes());
					out.flush();
					e.printStackTrace();
				}
				out.write("{\"state\":\"3\",\"msg\":\"注册成功，您可以登陆了\"}".getBytes());
				out.flush();

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void openServer() throws IOException {
		ExecutorService service = Executors.newFixedThreadPool(100);
		ServerSocket server = new ServerSocket(4002);
		while (true) {
			Socket socket = server.accept();
			service.execute(new RegServer(socket));
		}
	}
}
