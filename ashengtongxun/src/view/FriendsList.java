package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.sf.json.JSONObject;
import view.util.Config;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * 好友列表窗体
 * @author acer
 *
 */
public class FriendsList extends JFrame {

	private JPanel contentPane;
	
	final JLabel mynetname=new JLabel();//昵称定义
	final JLabel myimg=new JLabel(new ImageIcon("face_color/11.png"));//头像定义
	final JLabel mysign=new JLabel();//个性签名定义
	
	public void gengxin(){
		JSONObject jsonObject=JSONObject.fromObject(Config.geren_json_data);
		//读取Config.geren_json_data中信息设置自己的昵称
		mynetname.setText(jsonObject.getString("netname"));
		//读取Config.geren_json_data中信息设置自己的个性签名
		mysign.setText(jsonObject.getString("sign"));
		//读取Config.geren_json_data中信息设置自己的头像（注意要判断是否为默认头像）
		String img=jsonObject.getString("img");
		if(img.trim().equals("def")){
			img="0";
		}
		myimg.setIcon(new ImageIcon("face_color/"+img+".png"));
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
			FriendsList frame = new FriendsList();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Create the frame.
	 */
	public FriendsList() {
		setTitle("\u963F\u665F\u901A\u8BAF");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(10, 10));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(10, 10));

		//头像加载
		myimg.setPreferredSize(new Dimension(80,80));
		panel.add(myimg, BorderLayout.WEST);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(10, 10));
		
		//昵称加载
		mynetname.setFont(new Font("宋体", Font.BOLD, 12));
		panel_1.add(mynetname, BorderLayout.CENTER);
		
		//个性签名加载
		panel_1.add(mysign, BorderLayout.SOUTH);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnNewButton = new JButton("\u8BBE\u7F6E");
		panel_2.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("\u67E5\u627E");
		panel_2.add(btnNewButton_1);
		
		JButton button = new JButton("\u767B\u51FA");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		panel_2.add(button);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		tabbedPane.addTab("New tab", null, scrollPane, null);
		scrollPane.setViewportView(new FriendsListPanel());
		
		//登陆后初始化时加载自己的头像、个性签名和昵称
		gengxin();
	}
	
}
