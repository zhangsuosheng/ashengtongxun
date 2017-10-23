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
		
		//�������һ�����WindowXY.getXY()��������䣬��Login�����ʼ��ʱ�Ͷ�λ����Ļ����
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
				
				//�������һ�����WindowXY.getXY()�ķ������õ��ע�ᰴť�������С�ı�����½�����λ�ö�λ����Ļ����
				setLocation(WindowXY.getXY(Login.this.getSize()));
				
			}
		});
		btnNewButton.setBounds(29, 215, 106, 38);
		contentPane.add(btnNewButton);

		JButton loginbutton = new JButton("\u767B\u5F55");
		loginbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//��TextField�ռ��û���д����Ϣ
				String username_str=username.getText().trim();
				String password_str=password.getText().trim();
				
				//ע�⣬Ҫ��֤�û��������벻��Ϊ��
				if(username_str.trim().equals("")||password_str.trim().equals("")){
					//������ʾ��
					javax.swing.JOptionPane.showMessageDialog(Login.this, "�û��������벻��Ϊ�գ�");
					//��������¼�
					return;
				}
				
				
				//�û���Ϣ������֤ͨ������ʼ����������ӵ�½��ע�⣬�����ӵ�½ǰ�Ȱѻ�õ��û�����������Config��������Ĵ�һ�ݣ����ڵ����Զ�����
				Config.username=username_str;
				Config.password=password_str;
				try {
					JSONObject json=NetService.getNetService().login();
					
					//��½�ɹ������ڿͻ������½�ɹ����
					if(json.getInt("state")==0){
						javax.swing.JOptionPane.showMessageDialog(Login.this, "��½�ɹ���");
						new FriendsList().setVisible(true);
						Login.this.setVisible(false);
						Login.this.dispose();
					}
					else{
						javax.swing.JOptionPane.showMessageDialog(Login.this, json.getString("msg"));
					}
					
					
					
				} catch (UnknownHostException e) {
					e.printStackTrace();
					javax.swing.JOptionPane.showMessageDialog(Login.this, "��������ʧ�ܣ�");
				} catch (IOException e) {
					//��ӡ��ջ��Ϣ����ڳ���δ����ǰ��ñ�����������
					e.printStackTrace();
					javax.swing.JOptionPane.showMessageDialog(Login.this, "��������ʧ�ܣ�");
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
 * ������֤�빦��_�ͻ��˷��ͷ���֤�������
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(reg_usrname.getText().trim().equals("")){
						javax.swing.JOptionPane.showMessageDialog(Login.this, "�û�������Ϊ��");
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
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
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
						javax.swing.JOptionPane.showMessageDialog(Login.this, "�û�������Ϊ��");
						return;
					}
					if(reg_password.getText().trim().equals("")){
						javax.swing.JOptionPane.showMessageDialog(Login.this, "���벻��Ϊ��");
						return;
					}
					if(reg_configpassword.getText().trim().equals("")){
						javax.swing.JOptionPane.showMessageDialog(Login.this, "ȷ�����벻��Ϊ��");
						return;
					}
					if(reg_code.getText().trim().equals("")){
						javax.swing.JOptionPane.showMessageDialog(Login.this, "��֤�벻��Ϊ��");
						return;
					}
					if(!reg_password.getText().trim().equals(reg_configpassword.getText())){
						javax.swing.JOptionPane.showMessageDialog(Login.this, "������������벻ͬ��");
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
						javax.swing.JOptionPane.showMessageDialog(Login.this, "�û����Ѵ��ڣ�");
					}
					if(json.getString("state").equals("2")){
						javax.swing.JOptionPane.showMessageDialog(Login.this, "δ֪�Ĵ������ˣ�");
					}
					if(json.getString("state").equals("3")){
						javax.swing.JOptionPane.showMessageDialog(Login.this, "��ϲ����ע��ɹ���");
						reg_usrname.setText("");
						reg_password.setText("");
						reg_code.setText("");
						reg_configpassword.setText("");
					}
					
					
					in.close();
					out.close();
					socket.close();
	
				} catch (UnknownHostException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
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
