package view;

import java.awt.BorderLayout;
import java.awt.*;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import ashengtongxun.WindowXY;
import net.sf.json.JSONObject;
import view.service.NetService;
import view.util.Config;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField username;
	private JTextField reg_usrname;
	private JTextField reg_password;
	private JTextField reg_configpassword;
	private JTextField reg_code;
	private JPasswordField password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		try {
			//javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
			Login frame = new Login();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setAlwaysOnTop(true);
		setTitle("\u963F\u665F\u901A\u8BAF");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 296);
		
		//这里插入一句调用WindowXY.getXY()方法的语句，让Login窗体初始化时就定位在屏幕中央
		setLocation(WindowXY.getXY(this.getSize()));
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnNewButton = new JButton("\u6CE8\u518C");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Login.this.getHeight()==535)
					Login.this.setSize(300,296);
				else
					Login.this.setSize(300, 535);
				
				//这里插入一句调用WindowXY.getXY()的方法，让点击注册按钮，窗体大小改变后重新将窗体位置定位在屏幕中央
				setLocation(WindowXY.getXY(Login.this.getSize()));
				
			}
		});
		btnNewButton.setBounds(29, 215, 106, 38);
		contentPane.add(btnNewButton);

		JButton loginbutton = new JButton("\u767B\u5F55");
		loginbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//从TextField收集用户填写的信息
				String username_str=username.getText().trim();
				String password_str=password.getText().trim();
				
				//注意，要验证用户名和密码不能为空
				if(username_str.trim().equals("")||password_str.trim().equals("")){
					//弹出警示框
					javax.swing.JOptionPane.showMessageDialog(Login.this, "用户名和密码不能为空！");
					//跳出点击事件
					return;
				}
				
				
				//用户信息本地验证通过，则开始与服务器连接登陆，注意，在连接登陆前先把获得的用户名和密码再Config配置类里寄存一份，用于掉线自动重连
				Config.username=username_str;
				Config.password=password_str;
				try {
					JSONObject json=NetService.getNetService().login();
					
					//登陆成功，则在客户输出登陆成功结果
					if(json.getInt("state")==0){
						javax.swing.JOptionPane.showMessageDialog(Login.this, "登陆成功！");
						new FriendsList().setVisible(true);
						Login.this.setVisible(false);
						Login.this.dispose();
					}
					else{
						javax.swing.JOptionPane.showMessageDialog(Login.this, json.getString("msg"));
					}
					
					
					
				} catch (UnknownHostException e) {
					e.printStackTrace();
					javax.swing.JOptionPane.showMessageDialog(Login.this, "网络连接失败！");
				} catch (IOException e) {
					//打印堆栈信息语句在程序未发布前最好保留做测试用
					e.printStackTrace();
					javax.swing.JOptionPane.showMessageDialog(Login.this, "网络连接失败！");
				}
			}
		});
		loginbutton.setBounds(166, 215, 106, 38);
		contentPane.add(loginbutton);

		JLabel lblNewLabel = new JLabel("\u7528 \u6237 \u540D \uFF1A");
		lblNewLabel.setBounds(40, 122, 73, 15);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("   \u5BC6 \u7801 \uFF1A");
		lblNewLabel_1.setBounds(40, 169, 73, 15);
		contentPane.add(lblNewLabel_1);

		username = new JTextField();
		username.setBounds(123, 111, 149, 37);
		contentPane.add(username);
		username.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "\u6CE8\u518C\u7528\u6237", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setToolTipText("");
		panel.setBounds(10, 277, 274, 219);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblEmail = new JLabel("  Email \uFF1A");
		lblEmail.setBounds(26, 25, 69, 15);
		panel.add(lblEmail);
		
		JLabel label = new JLabel(" \u5BC6  \u7801 \uFF1A");
		label.setBounds(26, 128, 69, 15);
		panel.add(label);
		
		JButton send_code = new JButton("\u53D1\u9001\u9A8C\u8BC1");
/******************************************************************************************
 * 发送验证码功能_客户端发送发验证码的请求
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(reg_usrname.getText().trim().equals("")){
						javax.swing.JOptionPane.showMessageDialog(Login.this, "用户名不能为空");
						return;
					}
					Socket socket= new Socket(Config.IP,Config.REG_PORT);
					InputStream in=socket.getInputStream();
					OutputStream out=socket.getOutputStream();
					
					out.write(("{\"type\":\"code\",\"usrname\":\""+reg_usrname.getText()+"\"}").getBytes());
					out.flush();
					
					in.close();
					out.close();
					socket.close();
	
				} catch (UnknownHostException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		});
***********************************************************************************/
		send_code.setBounds(155, 53, 93, 23);
		panel.add(send_code);
		
		reg_usrname = new JTextField();
		reg_usrname.setBounds(92, 22, 156, 21);
		panel.add(reg_usrname);
		reg_usrname.setColumns(10);
		
		reg_password = new JTextField();
		reg_password.setBounds(92, 125, 156, 21);
		panel.add(reg_password);
		reg_password.setColumns(10);
		
		JLabel label_1 = new JLabel("\u786E\u8BA4\u5BC6\u7801\uFF1A");
		label_1.setBounds(26, 156, 69, 15);
		panel.add(label_1);
		
		reg_configpassword = new JTextField();
		reg_configpassword.setBounds(92, 153, 156, 21);
		panel.add(reg_configpassword);
		reg_configpassword.setColumns(10);
		
		JLabel label_2 = new JLabel("\u9A8C \u8BC1 \u7801\uFF1A");
		label_2.setBounds(26, 93, 69, 15);
		panel.add(label_2);
		
		reg_code = new JTextField();
		reg_code.setBounds(92, 90, 156, 21);
		panel.add(reg_code);
		reg_code.setColumns(10);
		
		JButton quit = new JButton("\u53D6\u6D88");
		quit.setBounds(26, 186, 93, 23);
		panel.add(quit);
		
		JButton send_reg = new JButton("\u786E\u8BA4\u6CE8\u518C");
		send_reg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(reg_usrname.getText().trim().equals("")){
						javax.swing.JOptionPane.showMessageDialog(Login.this, "用户名不能为空");
						return;
					}
					if(reg_password.getText().trim().equals("")){
						javax.swing.JOptionPane.showMessageDialog(Login.this, "密码不能为空");
						return;
					}
					if(reg_configpassword.getText().trim().equals("")){
						javax.swing.JOptionPane.showMessageDialog(Login.this, "确认密码不能为空");
						return;
					}
					if(reg_code.getText().trim().equals("")){
						javax.swing.JOptionPane.showMessageDialog(Login.this, "验证码不能为空");
						return;
					}
					if(!reg_password.getText().trim().equals(reg_configpassword.getText())){
						javax.swing.JOptionPane.showMessageDialog(Login.this, "两次输入的密码不同！");
						return;
					}
					
					Socket socket= new Socket(Config.IP,Config.REG_PORT);
					InputStream in=socket.getInputStream();
					OutputStream out=socket.getOutputStream();
					
					out.write((("{\"type\":\"reg\",\"usrname\":\""+reg_usrname.getText()+"\",\"usrpassword\":\""+reg_password.getText()+"\"}")).getBytes());
					out.flush();
					
					byte[] bytes=new byte[1024];
					int len=in.read(bytes);
					String json_str=new String(bytes,0,len);
					
					JSONObject json=JSONObject.fromObject(json_str);
					if(json.getString("state").equals("1")){
						javax.swing.JOptionPane.showMessageDialog(Login.this, "用户名已存在！");
					}
					if(json.getString("state").equals("2")){
						javax.swing.JOptionPane.showMessageDialog(Login.this, "未知的错误发生了！");
					}
					if(json.getString("state").equals("3")){
						javax.swing.JOptionPane.showMessageDialog(Login.this, "恭喜您，注册成功！");
						reg_usrname.setText("");
						reg_password.setText("");
						reg_code.setText("");
						reg_configpassword.setText("");
					}
					
					
					in.close();
					out.close();
					socket.close();
	
				} catch (UnknownHostException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		});
		send_reg.setBounds(155, 186, 93, 23);
		panel.add(send_reg);
		
		password = new JPasswordField();
		password.setBounds(123, 156, 149, 37);
		contentPane.add(password);
	}
}
