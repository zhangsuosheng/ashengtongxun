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
 * �ͻ��˵�ͨѶ���񣬸�������������ֳ�����״̬ ������ǿͻ�������Ҫ��һ��ͨѶ�࣬�ʿ�������Ϊ������
 * 
 * @author acer 1�����º�������״̬��5min��һ������ 2����½��֤ 3���˳��˻�
 */

public class NetService extends Thread {

	// ������(����ʽ����)
	private NetService() {
	}

	private static NetService netService = new NetService();

	public static NetService getNetService() {
		return netService;
	}

	// ����׼������������г�ʱ��ͨѶ
	public void run() {
		

		try {
			
///////////////////////////////////////////////////////////////////////////
			/**
			 * ���ߺ��ѵ�ʵʱ����
			 */
			
			//���������б�json�ַ�����ȡ���к���id
			Config.jiexi_haoyou_json_data(Config.haoyou_json_data);
			
			byte[] bytes=new byte[1024*10];
			int len=0;
			
			//ÿ��5s�����������ߺ�����Ϣ�������Լ����еĺ��ѵ�id���������ߵĺ��ѵ�id��������д��洢���ߺ���id���ļ�
			while (run) {
				
				output.write("U0002".getBytes());
				output.flush();
				input.read();
				output.write(Config.haoyou_id_data.getBytes());
				//System.out.println(Config.haoyou_id_data);
				output.flush();
				len=input.read(bytes);
				String online=new String(bytes,0,len);
				
				//System.out.println("�����˻���"+online);//�������
				
				try {
					//������ߺ�����Ϣ�����˱仯�������
					if (!Config.haoyou_online_id_data.equals(online)) {
						Config.haoyou_online_id_data = online;
						Config.friendsListPanel.gengxin();
					}
				} catch (Exception e) {
				}
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO �Զ����ɵ� catch ��
				}
			}
		} catch (Exception e) {
			// �����½�����쳣����run�û�false��
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
	 * �ͻ����ļ���������½����ķ������ڵ�½��ť����¼��е���
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public JSONObject login() throws UnknownHostException, IOException {
		// �ͻ���socket��������˷�����������ʱ��IP��ַ�����Ҳ�����Ҳ������IO�쳣����Ӧ�׳�
		// UnknownHostException,IOException
		
		
		// �÷�������IP�Ͷ˿ںų�ʼ������һ��socket����ȡ����InputStream��OutputStream������һ���ַ����洢Ҫ���͵��û���������
		socket = new Socket(Config.IP, Config.LOGIN_PORT);
		input = socket.getInputStream();
		output = socket.getOutputStream();
		String json_str = "{\"usrname\":\"" + Config.username + "\",\"usrpassword\":\"" + Config.password + "\"}";

		// �������������û���������
		output.write(json_str.getBytes());
		output.flush();

		// ���շ�������ִ����Ϣ
		byte[] bytes = new byte[1024];
		int len = input.read(bytes);
		json_str = new String(bytes, 0, len);

		JSONObject json = JSONObject.fromObject(json_str);
		if (json.getInt("state") == 0) {

			/**
			 * �����������ǵ�������ǿ���������������񣨿��̣߳�
			 */
			// 1������Ҫ���¿����߳�
			if (thread != null) {
				// �ж��߳��Ƿ񻹻���
				if (thread.getState() == Thread.State.RUNNABLE) {
					run = false;
					try {
						thread.stop();
					} catch (Exception e) {
					}
				}
			}
//start::ע�⣡��������������ȡ�����б���Ϣд�������ļ� �� ��ȡ�������� ����������Ҫ��UI������س���֮ǰ��ɣ��ʽ���������������login()�����У���Ϊ�ڵ�½����login.java�У�����д�����ȵ���login()����Ȼ����new���������б���壨JFrame����
			/**
			 * ��ȡ�����б���Ϣд�������ļ�
			 */
			//���ͻ�ȡ�����б�����
			output.write("U0001".getBytes());
			output.flush();
			
			//���պ����б���Ϣ
			bytes=new byte[1024*10];
			len=input.read(bytes);
			System.out.println(len);
			String jsonstr=new String(bytes,0,len);
			
			//�����յ��ĺ����б���Ϣ��json��ʽ�ַ���д�������ļ����ȴ�ͼ�λ��û��������ʱ��ͼ�λ��������������ļ��л�ȡ�����б���Ϣ����ʾ
			Config.haoyou_json_data=jsonstr;
			
			//�������
			System.out.println("�����б�"+jsonstr);
			
			/**
			 * ��ȡ��������
			 */
			//���ͻ�ȡ������������
			output.write("U0003".getBytes());
			output.flush();
			
			//���ո�������
			bytes=new byte[1024*10];
			len=input.read(bytes);
			jsonstr=new String(bytes,0,len);
			
			//�����յ��ĸ������ϵ�json��ʽ�ַ���д�������ļ����ȴ�ͼ�λ��û��������ʱ��ͼ�λ��������������ļ��л�ȡ����ʾ
			Config.geren_json_data=jsonstr;

			//�������
			System.out.println("�������ϣ�"+jsonstr);
			
			/**
			 * ����UDP������
			 * ע�⣺ֻ�з������ķ�������Ҫ�̶���ĳЩ�˿ڵģ����ͻ���ֻ��Ҫ��ס��Щ�˿ھ��У��ͻ����Լ�ʹ�õ�����Щ�˿�һ�㲻�ù̶�
			 */
			
			DatagramSocket datagramSocket=new DatagramSocket();
			//ע�⣬����udp��������Ҫ�ѱ���ʹ�õ�UDPsocket�������ļ��д�һ��
			Config.datagramSocket=datagramSocket;
			//����������
			new MessageRegService(datagramSocket);
			//�����շ���Ϣ
			new MessageService(datagramSocket);
			System.out.println("���������");
//end::!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			run = true;
			thread = new Thread(this);
			thread.start();

		}
		return json;
	}

}
