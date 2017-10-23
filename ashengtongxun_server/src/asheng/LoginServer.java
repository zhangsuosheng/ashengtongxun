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

//��½�࣬ÿ��һ���û�����socket���ӣ�ϵͳ��Ҫ����һ��������Ϊ�����
public class LoginServer implements Runnable {

	// ����һ�����ڷ����û���socket��
	private Socket socket = null;

	// ���캯����socket���ʼ��
	public LoginServer(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		String uid = null;
		InputStream in = null;
		OutputStream out = null;
		try {
			// ��Scoket.getInputStream()��Socket.getOutputStream()��ȡ�û�������socket�е�inputstream��outputstream
			in = socket.getInputStream();
			out = socket.getOutputStream();

			// ����һ�����1024�ֽڵ�byte����������socket�е�inputsteam�е���Ϣ
			byte[] bytes = new byte[1024];
			// ��InputStream.read(byte[])���� ��ȡһ���ַ�������byte[]������
			int len = in.read(bytes);
			// ͨ��ʹ��ƽ̨��Ĭ���ַ�������ָ���� byte[]���飬����һ���µ� String
			String json_str = new String(bytes, 0, len);

			JSONObject json = JSONObject.fromObject(json_str);
			String usrname = json.getString("usrname");
			String usrpassword = json.getString("usrpassword");

			
			// ��֤�����Ƿ�Ϊ��ȷ���ֻ�����
			if (usrname.trim().length() == 11) {
				try {
					Long.parseLong(usrname);
				} catch (NumberFormatException e) {
					out.write("{\"state\":4,\"msg\":\"�û�����ʽ����\"}".getBytes());
					// ��ջ�������һЩ�������ʵ���Ǵ�����ģ���������д����һ�������������Ҫ�Ȼ���������flush()
					out.flush();
					//ע�⣬���߳��׳���ĳ���쳣��Ҫ��catch��finally������return��close()�����رո��߳�
					return;
				}
			}
			
			

			/**
			 * �ж����û����Ƿ���ϸ�ʽ�󼴽��û��������봫��LoginCheck�������֤
			 * ��֤������ͨ���쳣������ͻ��˷�����Ϣ���ر��߳�
			 * ��֤�ɹ����û���Ϣд�������û��б�
			 * 
			 */
			
			try {
				uid = new LoginCheck().loginForPhone(usrname, usrpassword);

			} catch (UsernameNotFoundException e) {
				out.write("{\"state\":2,\"msg\":\"�û�������\"}".getBytes());
				out.flush();
				e.printStackTrace();
				return;
			} catch (PasswordException e) {
				out.write("{\"state\":1,\"msg\":\"�������\"}".getBytes());
				out.flush();
				e.printStackTrace();
				return;
			} catch (StateException e) {
				out.write("{\"state\":3,\"msg\":\"��Ǹ�����ѱ����\"}".getBytes());
				out.flush();
				e.printStackTrace();
				return;
			} catch (SQLException e) {
				out.write("{\"state\":5,\"msg\":\"��Ǹ��δ֪�Ĵ�������\"}".getBytes());
				out.flush();
				e.printStackTrace();
				return;
			}
			
			
			//��½�ɹ�����һ����Ϣ���ͻ���
			out.write("{\"state\":0,\"msg\":\"��½�ɹ���\"}".getBytes());
			out.flush();
			//��½�ɹ����Ǽǵ�½��Ϣ
			UserOnlineList.getList().regOnline(uid, socket, usrname);

			
			
			while(true){
				
				//��Ϊ��½�ɹ���bytes�����Ҫ����������ߺ��ѵ��������ϴ����Ϣ��������������ֽ�
				bytes=new byte[2048];
				
				
				len=in.read(bytes);
				String command=new String(bytes,0,len);
				
				if(command.equals("U0001")){
					//���º�����Ϣ
					
					//��ȡUserService���getHaoyouliebiao������������vector����
					Vector<UserInfo2> userinfos =new UserService().getHaoyouliebiao(uid);
					//��JSONArray.fromObject()��̬������Vector����ת��ΪJSON���飬����toStringת��ΪString�ַ�������ת��Ϊbyte�����͸��ͻ���
					out.write(JSONArray.fromObject(userinfos).toString().getBytes());
					out.flush();
				
				}else if(command.equals("U0002")){
					/**
					 * �������ߺ��ѣ��Ƚ��ܿͻ��˷������������к��ѵ�id��Ȼ���ѯ��Щ�����Ƿ�����
					 */
					//���ѿͻ��˷�������׼���ã��뷢�������к��ѵ�id�������Ұ����ѯ��Щ����
					out.write(1);
					out.flush();
					//��øÿͻ������к��ѵ�id
					len=in.read(bytes);//�������ݵĸ�ʽ��1��2��3��4��5
					String str=new String(bytes,0,len);
					String[] ids=str.split(",");
					
					//����һ�����淵�ز�ѯ������ַ�������
					StringBuffer stringBuffer=new StringBuffer();
					
					for(String string:ids){
						if(UserOnlineList.getList().isOnline(string)){
							//����ѯ���ĺ��ѵ�id���У���StringBuffer.append()���뵽�ַ���������
							stringBuffer.append(string);
							stringBuffer.append(",");
						}
					}
					
					//�жϲ�ѯ����Ƿ�Ϊ�գ�Ϊ���򷵻�һ�仰
					if(stringBuffer.length()==0){
						out.write("notFound".getBytes());
						out.flush();
					}
					//�жϲ�ѯ����Ƿ�Ϊ�գ���Ϊ�����ַ�������תΪ�ַ�����תΪbyte�����ظ��ͻ���
					else{
						out.write(stringBuffer.toString().getBytes());
						out.flush();
					}
					
				}else if (command.equals("U0003")){
					//���¸�������
					
					UserInfo3 userinfo3=new UserService().getUserinfo(uid);
					//System.out.println(userinfo3);
					//System.out.println(JSONObject.fromObject(userinfo3).toString());
					out.write(JSONObject.fromObject(userinfo3).toString().getBytes());
					out.flush();
					
				}else if (command.equals("U0004")){
					//�޸ĸ�������
				}else if (command.equals("EXIT")){
					//�˳���¼
					UserOnlineList.getList().logout(uid);
					//����ѭ�������ٽ��տͻ�����Ϣ��
					return;
				}
				
			}
			
		} 
		
		
		
		//������run�����е��쳣�Ĳ�����
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		//������run�������̣߳��Ľ�������:���߳�ʹ�ý������socket��inputstream��outputstream�ر�
		finally {
			// ����������ӹر�
			try {
				// �������ͻȻ�رջ����ǹرյĻ� ������Ҫ���б���ȥ�����˻�
				UserOnlineList.getList().logout(uid);
				in.close();
				out.close();
				socket.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	
	
	
	
	// �����������ĵ�½����ķ�����ע�����Ǿ�̬��
	public static void openServer() throws Exception {
		// ������һ�����1000���̵߳��̳߳�
		ExecutorService execute = Executors.newFixedThreadPool(1000);

		// ���ŷ�������4001�˿�
		ServerSocket server = new ServerSocket(4001);

		// ��½���񷽷���Ҫ����ѭ�����м���
		while (true) {
			// ��server.accept()���ܿͻ��˷�����socket������һ�����ô洢��
			Socket socket = server.accept();
			// setSotimeout(10000)�Ǳ�ʾ����Է�����״̬10��û���յ����ݵĻ�ǿ�ƶϿ��ͻ��ˡ������Ҫ�����ӵĻ�������ʹ����������֪ͨ��������Ҳ������û�з��������ݣ������Ҹ������һ����š�
			socket.setSoTimeout(10000);
			// ��execute.execute(��Ķ��󣩴���һ���µ��̲߳�ִ��
			execute.execute(new LoginServer(socket));
		}
	}
	
	/**
	 * �ø÷���������������������˷���������
	 * @param args
	 */
	public static void main(String[] args){
		try {
			openServer();
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
	
}
