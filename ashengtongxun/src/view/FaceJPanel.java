package view;

import java.awt.Event;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Message;
import view.util.Config;

/**
 * 小Panel类 提供设置头像、设置姓名、设置个性签名、设置在线状态（更改头像颜色）接口 自定义的（创建一个普通类然后手动写的）
 * 在大panel类中每一个该类的对象对应一个好友，共同组成好友列表
 * 
 * @author acer
 *
 */
public class FaceJPanel extends JPanel implements Comparable<FaceJPanel>, MouseListener, Runnable {
	String img;
	String uid;
	String name;
	String sign;

	JLabel label_img;
	JLabel label_name;
	JLabel label_sign;

	boolean online = false;
	ChatWindow chatWindow=null;//同该好友的聊天窗

	// 记录该panel相对于上层容器的坐标
	int xx = 0;
	int yy = 0;

	public FaceJPanel(String img, String uid, String name, String sign) {
		this.img = img;
		this.uid = uid;
		this.name = name;
		this.sign = sign;

		this.setLayout(null);

		// 凡是使用的鼠标事件都要先在构造函数里初始化一下，否则无法使用
		this.addMouseListener(this);

		// 向小panel中添加的控件的坐标是在小panel中的坐标，所以直接用具体数值确定就行
		label_img = new JLabel();
		label_img.setBounds(5, 5, 48, 48);
		add(label_img);
		setImg(img);

		label_name = new JLabel();
		label_name.setBounds(58, 10, 337, 15);
		add(label_name);
		label_name.setText(name);

		label_sign = new JLabel();
		label_sign.setBounds(58, 35, 337, 15);
		add(label_sign);
		label_sign.setText(sign);
	}

	
	
///////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////
	/**
	 * 实现：在用户未打开与该好友的聊天窗口时将未读消息存到Vector中，在用户打开聊天窗口时将未读消息显示
	 */
	Vector<Message> msgs=new Vector<Message>();//存储未读消息
	Thread thread=null;//实现用户头像闪动的线程
	boolean run;//控制用户头像是否闪动
	public void addMessage(Message msg){
		msgs.add(msg);
		if(thread==null||thread.getState()==Thread.State.TERMINATED){
			thread=new Thread(this);
			thread.start();
		}
	}
	@Override
	public void run() {
		run=true;
		int x=this.getX();
		int y=this.getY();
		while(run){
			this.setLocation(x-2,y-2);
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			this.setLocation(x+2,y+2);
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		this.setLocation(x,y);
	}
	
	/**
	 * 打开聊天窗的函数，打开时应该关闭消息闪动同时将未读消息通过构造器形式传递给新建的窗口
	 * @param 张所晟
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// 鼠标点击事件，实现弹出聊天窗
		if (arg0.getClickCount() == 1) {

		} else if (arg0.getClickCount() == 2) {
			run=false;
			chatWindow=Config.openChatWindow(uid, name, sign, img,online,msgs);
			msgs.clear();//注意，在用户阅读了未读消息之后要把未读消息的vector清空
		}
	}
///////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////
	
	
	
	
	public void setImg(String img) {
		// 这个是在用户设置自己头像是所要调用的函数，而不是更改用户上线下线头像颜色时所调用的函数，用户上线下线头像颜色的更改应该在设置用户是否在线的函数setOnline()中实现
		// 注意，接收显示用户头像时，要检察一下输入是否为默认
		// 注意，我们是用用户图像的颜色来表现用户是否在线的，所以加载或更新头像是应当先判断当前用户的在线状态，if在线 加载彩色头像 else
		// 加载灰色头像
		if (img.equals("def")) {
			img = "0";
		}
		if (online) {
			label_img.setIcon(new ImageIcon("face_color/" + img + ".png"));
		} else {
			label_img.setIcon(new ImageIcon("face_black/" + img + ".png"));
		}
		this.img = img;
	}

	public void setName(String name) {
		label_name.setText(name);
		this.name = name;
	}

	public void setSign(String sign) {
		label_sign.setText(sign);
		this.sign = sign;
	}

	public void setOnline(boolean online) {
		this.online = online;
		// 注意，在设置好友是否在线的函数里除了设置online这个boolean变量，还要更改头像颜色！
		if (online) {
			label_img.setIcon(new ImageIcon("face_color/" + img + ".png"));
		} else {
			label_img.setIcon(new ImageIcon("face_black/" + img + ".png"));
		}
		if(chatWindow!=null){//如果打开了同该好友的聊天窗，则还要改变聊天窗中好友头像的颜色
			chatWindow.setOnline(online);
		}
	}

	public int compareTo(FaceJPanel o) {
		if (o.online)
			return 1;
		else if (this.online)
			return -1;
		else
			return 0;
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// getX(),getY()方法用来获取控件原来在上层容器内的坐标，方便在mouseExited时还原
		xx = this.getX();
		yy = this.getY();

		this.setLocation(xx - 3, yy - 3);
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO 自动生成的方法存根
		this.setLocation(xx, yy);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO 自动生成的方法存根

	}



}
