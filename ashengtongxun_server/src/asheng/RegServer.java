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

	// �洢�ѷ��͵���֤��
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

			// ���������ַ���type
			String type = json.getString("type");
			/****************************************************************************************
			 * ������֤�롢������֤�롢���ղ���֤�û���д����֤���Ƿ���ȷ��ִ��ע�Ṧ�� // �������ַ�������Ϊcode��������֤�� if
			 * (type.equals("code")) { String usrname =
			 * json.getString("usrname");
			 * 
			 * //������֤�� StringBuffer code = new StringBuffer(); Random random =
			 * new Random(); for (int i = 0; i < 6; i++) {
			 * code.append(random.nextInt(10)); } // �ж��Ƿ�Ϊ�ֻ��ţ����ֻ��ŷ�����֤�� if
			 * (usrname.trim().length() == 11) { try {//�ж��ֻ��Ÿ�ʽ�Ƿ���ȷ
			 * Long.parseLong(usrname);//
			 * ���ַ�������Ϊlongֵ�������ַ���Ϊ����ת��Ϊlong���ͣ����Ƕ���������ɣ�����׳��쳣
			 * 
			 * hashMap_code.put(usrname, code.toString());
			 * SendCode.send(usrname, code.toString());
			 * out.write("��֤�뷢�ͳɹ�".getBytes()); out.flush(); } catch (Exception
			 * e) { out.write("�ֻ��Ÿ�ʽ������֤�뷢��ʧ��".getBytes()); out.flush(); } } //
			 * �ж��Ƿ�Ϊ���䣬�����䷢��֤�� else { if (usrname.indexOf("@") >= 0) {//
			 * �ж������ʽ�Ƿ���ȷ
			 * 
			 * hashMap_code.put(usrname, code.toString());
			 * SendCode.send(usrname, code.toString());
			 * out.write("��֤�뷢�ͳɹ�".getBytes()); out.flush(); }else{
			 * out.write("�����ʽ������֤�뷢��ʧ��".getBytes()); out.flush(); } } }
			 * //�������ַ���Ϊreg����ִ����֤��֤���ע���û����� else if (type.equals("reg")) {
			 * String usrname=json.getString("usrname"); String
			 * password=json.getString("usrpassword"); String
			 * code=json.getString("code"); String
			 * code1=hashMap_code.get(usrname);
			 * if(code1!=null){//������֤���Ƿ���֤�ɹ���������֤��ʧЧ
			 * hashMap_code.remove(usrname); } if(code1.equals(code1)){
			 * out.write("{\"state\":\"1\",\"msg\":\"��֤����������»�ȡ\"}".getBytes());
			 * out.flush(); } ����������������ִ���û�ע����������룩 }
			 *********************************************************************************************/
			if (type.equals("reg")) {
				String usrname = json.getString("usrname");
				String usrpassword = json.getString("usrpassword");
				try {
					new UserService().regUser(usrname, usrpassword);
				} catch (UserNameUsedException e) {
					out.write("{\"state\":\"1\",\"msg\":\"���û����Ѵ��ڣ�\"}".getBytes());
					out.flush();
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO �Զ����ɵ� catch ��
					out.write("{\"state\":\"2\",\"msg\":\"δ֪�Ĵ������ˣ�\"}".getBytes());
					out.flush();
					e.printStackTrace();
				}
				out.write("{\"state\":\"3\",\"msg\":\"ע��ɹ��������Ե�½��\"}".getBytes());
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
