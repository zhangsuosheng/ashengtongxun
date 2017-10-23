package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.Message;
import net.sf.json.JSONObject;
import view.util.Config;

import javax.swing.JLabel;
import javax.swing.JSplitPane;
import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChatWindow extends JFrame implements WindowListener{

	private JPanel contentPane;
	
	/*������
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
					ChatWindow frame = new ChatWindow("uid","netname","sign","0");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	*/
	String uid,netnamestr,signstr,imgstr;
	JLabel img;
	JTextArea msgstr;
	
	//��ʾ˫��������Ϣ�����
	JTextArea textArea;
	
	public ChatWindow(String uid,String netnamestr,String signstr,String imgstr,Vector msgs) {
		this.uid=uid;
		this.netnamestr=netnamestr;
		this.signstr=signstr;
		this.imgstr=imgstr;
		
		//���ô���ı���
		setTitle(netnamestr);
		//���ô�������ϵ�Сͼ��
		this.setIconImage(new ImageIcon("face_color/"+imgstr+".png").getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		img = new JLabel(new ImageIcon("face_color/"+imgstr+".png"));
		panel.add(img, BorderLayout.WEST);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JLabel netname = new JLabel(netnamestr);
		panel_1.add(netname, BorderLayout.CENTER);
		
		JLabel sign = new JLabel(signstr);
		panel_1.add(sign, BorderLayout.SOUTH);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		JPanel panel_2 = new JPanel();
		splitPane.setRightComponent(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		JButton close_button = new JButton("\u5173\u95ED");
		panel_3.add(close_button);
		
		
		
		JPanel panel_4 = new JPanel();
		panel_2.add(panel_4, BorderLayout.NORTH);
		panel_4.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JButton font_button = new JButton("\u5B57\u4F53");
		panel_4.add(font_button);
		
		JButton shake_button = new JButton("\u53D1\u9001\u7A97\u53E3\u6296\u52A8");
		panel_4.add(shake_button);
		
		JScrollPane scrollPane = new JScrollPane();
		panel_2.add(scrollPane, BorderLayout.CENTER);
		
		msgstr = new JTextArea();
		msgstr.setWrapStyleWord(true);
		msgstr.setLineWrap(true);
		scrollPane.setViewportView(msgstr);
		splitPane.setDividerLocation(250);
		
		//////////////////////////
		//�����ť����UDP������Ϣ��������////
		//////////////////////////
		JButton send_button = new JButton("  \u53D1 \u9001  ");
		send_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					// ��Ҫ����####################################################################################
					//1������һ��Message������װҪ�������ݣ�����˵��Ҫ�������ݷ�װ��һ��message���󣨷�ֹ�����ַ����µĴ�����ң�ͬʱ�򻯴��룩
					Message message=new Message();
					message.setType("msg");
					//��{"key":value}��ʽ��String�л�ȡvalue��  JSONObject.fromObject(json��ʽ���ַ���).getString/getInt/...("key")
					message.setMyUID(JSONObject.fromObject(Config.geren_json_data).getString("uid"));
					//JFrame�еİ�ť�ĵ���¼���Ҫ���ø�JFrame���ж���ĳ�Ա������      ��JFrame����.this.��Ա������
					message.setToUID(ChatWindow.this.uid);
					//java��ȡ�ı����е����ݣ�     �ı�����.getText()
					message.setMsg(msgstr.getText());
					//����һ��""��System.currentTimeMillis()�Ľ��ת��Ϊ�ַ���
					message.setCode(System.currentTimeMillis()+"");
					
					//2����message����ת��ΪJSON�ַ���
					String chatting_json_data=JSONObject.fromObject(message).toString();
					//3����JSON�ַ���ת��Ϊ����������
					byte[] bytes=chatting_json_data.getBytes();
					//4���������ݰ�
					DatagramPacket datagramPacket=new DatagramPacket(bytes,bytes.length,InetAddress.getByName(Config.IP),Config.UDP_PORT);
					//5���������ݰ����õ㿪���촰��ʱ������DatagramSocket��
					Config.datagramSocket.send(datagramPacket);
					// ############################################################################################
					//��շ��Ϳ�
					msgstr.setText("");
					//���Լ����͵�������ʾ����Ϣ��¼
					addMyMessage(message);
				}catch(Exception e){
					e.printStackTrace();
					System.out.println("�ͻ��˷�����Ϣ��������");
				}
			}
		});
		panel_3.add(send_button);
		
		textArea = new JTextArea();
		splitPane.setLeftComponent(textArea);
		
		//���������implements WindowListener�ӿڣ���дwindowClosing���������ùرմ���ʱ����������ر�
		this.addWindowListener(this);
		//����this.disposeִ�еķ�ʽ���õ�����ں��ʱֻ�رոô��ڶ������˳���������
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		for(int i=0;i<msgs.size();i++){
			this.addMessage((Message)msgs.get(i));
		}
	}
	
	// ���Լ����͵���Ϣ��ӵ���Ϣ��¼
	public void addMyMessage(Message msg){//����ֱ��ʹ���Լ������Message������  ��Ϣ���ݣ�����ʱ�䣬�û�id����Ϣ���򻯴���
		String str="\n"+JSONObject.fromObject(Config.geren_json_data).getString("netname")+"\t"+new Date().toLocaleString()+"\n"+msg.getMsg()+"\n";
		textArea.setText(textArea.getText()+str);
	}
	//�����ѷ�������Ϣ��ӵ���Ϣ��¼
	public void addMessage(Message msg){
		String str="\n"+netnamestr+"\t"+new Date().toLocaleString()+"\n"+msg.getMsg()+"\n";
		textArea.setText(textArea.getText()+str);
	}
	
	
// ���öԷ��Ƿ��������ߣ�ͷ����ɫ��
	public void setOnline(boolean online){
		if (online) {
			img.setIcon(new ImageIcon("face_color/" + imgstr + ".png"));
			this.setIconImage(new ImageIcon("face_color/"+imgstr+".png").getImage());
		} else {
			img.setIcon(new ImageIcon("face_black/" + imgstr + ".png"));
			this.setIconImage(new ImageIcon("face_black/"+imgstr+".png").getImage());
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		//���ڹر�ʱ����Ӧ�ĺ��Ѵ����촰���򿪵ĺ��ѵ�hashtable��ȥ��
		Config.closeChatWindow(uid);
		this.dispose();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO �Զ����ɵķ������
		
	}

}
