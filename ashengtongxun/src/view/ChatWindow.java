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
	
	/*测试用
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
	
	//显示双方聊天消息的面板
	JTextArea textArea;
	
	public ChatWindow(String uid,String netnamestr,String signstr,String imgstr,Vector msgs) {
		this.uid=uid;
		this.netnamestr=netnamestr;
		this.signstr=signstr;
		this.imgstr=imgstr;
		
		//设置窗体的标题
		setTitle(netnamestr);
		//设置窗体标题上的小图标
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
		//点击按钮发送UDP聊天消息给服务器////
		//////////////////////////
		JButton send_button = new JButton("  \u53D1 \u9001  ");
		send_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					// 主要步骤####################################################################################
					//1、创建一个Message类来封装要发的内容，或者说将要发的内容封装成一个message对象（防止特殊字符导致的传输错乱，同时简化代码）
					Message message=new Message();
					message.setType("msg");
					//从{"key":value}格式的String中获取value：  JSONObject.fromObject(json格式的字符串).getString/getInt/...("key")
					message.setMyUID(JSONObject.fromObject(Config.geren_json_data).getString("uid"));
					//JFrame中的按钮的点击事件中要调用该JFrame类中定义的成员变量：      该JFrame的名.this.成员变量名
					message.setToUID(ChatWindow.this.uid);
					//java获取文本框中的内容：     文本框名.getText()
					message.setMsg(msgstr.getText());
					//加上一个""把System.currentTimeMillis()的结果转化为字符串
					message.setCode(System.currentTimeMillis()+"");
					
					//2、将message对象转化为JSON字符串
					String chatting_json_data=JSONObject.fromObject(message).toString();
					//3、将JSON字符串转化为二进制数组
					byte[] bytes=chatting_json_data.getBytes();
					//4、创建数据包
					DatagramPacket datagramPacket=new DatagramPacket(bytes,bytes.length,InetAddress.getByName(Config.IP),Config.UDP_PORT);
					//5、发送数据包（用点开聊天窗口时创建的DatagramSocket）
					Config.datagramSocket.send(datagramPacket);
					// ############################################################################################
					//清空发送框
					msgstr.setText("");
					//将自己发送的内容显示到消息记录
					addMyMessage(message);
				}catch(Exception e){
					e.printStackTrace();
					System.out.println("客户端发送消息出现问题");
				}
			}
		});
		panel_3.add(send_button);
		
		textArea = new JTextArea();
		splitPane.setLeftComponent(textArea);
		
		//让这个窗体implements WindowListener接口，重写windowClosing方法，放置关闭窗体时让整个程序关闭
		this.addWindowListener(this);
		//设置this.dispose执行的方式，让点击窗口红叉时只关闭该窗口而不是退出整个程序
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		for(int i=0;i<msgs.size();i++){
			this.addMessage((Message)msgs.get(i));
		}
	}
	
	// 将自己发送的消息添加到消息记录
	public void addMyMessage(Message msg){//这里直接使用自己定义的Message来传输  消息内容，发送时间，用户id等信息，简化代码
		String str="\n"+JSONObject.fromObject(Config.geren_json_data).getString("netname")+"\t"+new Date().toLocaleString()+"\n"+msg.getMsg()+"\n";
		textArea.setText(textArea.getText()+str);
	}
	//将好友发来的消息添加到消息记录
	public void addMessage(Message msg){
		String str="\n"+netnamestr+"\t"+new Date().toLocaleString()+"\n"+msg.getMsg()+"\n";
		textArea.setText(textArea.getText()+str);
	}
	
	
// 设置对方是否是上下线（头像颜色）
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
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		//窗口关闭时将对应的好友从聊天窗正打开的好友的hashtable中去掉
		Config.closeChatWindow(uid);
		this.dispose();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO 自动生成的方法存根
		
	}

}
