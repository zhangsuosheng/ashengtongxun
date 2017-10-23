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
 * СPanel�� �ṩ����ͷ���������������ø���ǩ������������״̬������ͷ����ɫ���ӿ� �Զ���ģ�����һ����ͨ��Ȼ���ֶ�д�ģ�
 * �ڴ�panel����ÿһ������Ķ����Ӧһ�����ѣ���ͬ��ɺ����б�
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
	ChatWindow chatWindow=null;//ͬ�ú��ѵ����촰

	// ��¼��panel������ϲ�����������
	int xx = 0;
	int yy = 0;

	public FaceJPanel(String img, String uid, String name, String sign) {
		this.img = img;
		this.uid = uid;
		this.name = name;
		this.sign = sign;

		this.setLayout(null);

		// ����ʹ�õ�����¼���Ҫ���ڹ��캯�����ʼ��һ�£������޷�ʹ��
		this.addMouseListener(this);

		// ��Сpanel����ӵĿؼ�����������Сpanel�е����꣬����ֱ���þ�����ֵȷ������
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
	 * ʵ�֣����û�δ����ú��ѵ����촰��ʱ��δ����Ϣ�浽Vector�У����û������촰��ʱ��δ����Ϣ��ʾ
	 */
	Vector<Message> msgs=new Vector<Message>();//�洢δ����Ϣ
	Thread thread=null;//ʵ���û�ͷ���������߳�
	boolean run;//�����û�ͷ���Ƿ�����
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
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			this.setLocation(x+2,y+2);
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
		this.setLocation(x,y);
	}
	
	/**
	 * �����촰�ĺ�������ʱӦ�ùر���Ϣ����ͬʱ��δ����Ϣͨ����������ʽ���ݸ��½��Ĵ���
	 * @param ������
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// ������¼���ʵ�ֵ������촰
		if (arg0.getClickCount() == 1) {

		} else if (arg0.getClickCount() == 2) {
			run=false;
			chatWindow=Config.openChatWindow(uid, name, sign, img,online,msgs);
			msgs.clear();//ע�⣬���û��Ķ���δ����Ϣ֮��Ҫ��δ����Ϣ��vector���
		}
	}
///////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////
	
	
	
	
	public void setImg(String img) {
		// ��������û������Լ�ͷ������Ҫ���õĺ����������Ǹ����û���������ͷ����ɫʱ�����õĺ������û���������ͷ����ɫ�ĸ���Ӧ���������û��Ƿ����ߵĺ���setOnline()��ʵ��
		// ע�⣬������ʾ�û�ͷ��ʱ��Ҫ���һ�������Ƿ�ΪĬ��
		// ע�⣬���������û�ͼ�����ɫ�������û��Ƿ����ߵģ����Լ��ػ����ͷ����Ӧ�����жϵ�ǰ�û�������״̬��if���� ���ز�ɫͷ�� else
		// ���ػ�ɫͷ��
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
		// ע�⣬�����ú����Ƿ����ߵĺ������������online���boolean��������Ҫ����ͷ����ɫ��
		if (online) {
			label_img.setIcon(new ImageIcon("face_color/" + img + ".png"));
		} else {
			label_img.setIcon(new ImageIcon("face_black/" + img + ".png"));
		}
		if(chatWindow!=null){//�������ͬ�ú��ѵ����촰����Ҫ�ı����촰�к���ͷ�����ɫ
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
		// getX(),getY()����������ȡ�ؼ�ԭ�����ϲ������ڵ����꣬������mouseExitedʱ��ԭ
		xx = this.getX();
		yy = this.getY();

		this.setLocation(xx - 3, yy - 3);
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO �Զ����ɵķ������
		this.setLocation(xx, yy);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO �Զ����ɵķ������

	}



}
