package view;

import javax.swing.JPanel;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import view.util.Config;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * ��Panel �Զ�����ࣨ����һ����ͨ��Ȼ��д�����ģ� ���ں����б����е�JScrollPane�ؼ���
 * ���з������ɸ�СPanel���Զ����FaceJPanel�ࣩ
 * 
 * @author acer
 *
 */
public class FriendsListPanel extends JPanel {

	public FriendsListPanel() {
		setLayout(null);
		
		//����ʼ��¼ʱ�����ĺ����б����ʵ�������ô���Config�����ļ��У���NetService�����ļ��и��º����б����ʱ�������
		Config.friendsListPanel=this;

		/*
		 * for (int i = 0; i < 10; i++) { JPanel panel = new JPanel();
		 * panel.setBounds(5, i * 50, 430, 60); add(panel);
		 * panel.setLayout(null);
		 * 
		 * JLabel label = new JLabel(new ImageIcon("face_color/" + i + ".png"));
		 * label.setBounds(5, 5, 48, 48); panel.add(label);
		 * 
		 * JLabel lblNewLabel = new JLabel("New label");
		 * lblNewLabel.setBounds(58, 10, 337, 15); panel.add(lblNewLabel);
		 * 
		 * JLabel lblNewLabel_1 = new JLabel("New label");
		 * lblNewLabel_1.setBounds(58, 35, 337, 15); panel.add(lblNewLabel_1); }
		 */
		//this.setPreferredSize(new Dimension(0, 600));

		gengxin();
	}

	/**
	 * �����б�ʵʱ����
	 */
	public void gengxin() {
		String haoyou_data = Config.haoyou_json_data;
		System.out.println(haoyou_data);
		if(haoyou_data.equals("")){
			System.out.println("��û�к��ѣ�");
			return;
		}
		JSONArray jsonArray = JSONArray.fromObject(haoyou_data);

		//����
		System.out.println("SB:"+Config.hashtable);

		// ��½���һ�μ��غ����б����洢�����б�����Ϣ��hashtable����Ϊ0
		if (Config.hashtable.size() == 0) {

			// ѭ������ÿһ������
			for (int i = 0; i < jsonArray.size(); i++) {
				// ��Ϊ�ǵ�һ�μ��أ�����ֱ���½�һ��FaceJPanel��put������hashtable
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				Config.hashtable.put(jsonObject.getString("uid"), new FaceJPanel(jsonObject.getString("img"),
						jsonObject.getString("uid"), jsonObject.getString("netname"), jsonObject.getString("sign")));
			}
		}
		// ��½����º����б����洢�����б�����Ϣ��hashtable���Ȳ�Ϊ0
		else {
			// ѭ������ÿһ������
			for (int i = 0; i < jsonArray.size(); i++) {
				// ȡ��ǰ������Ѷ�Ӧ��jsonObject
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				// ȡ��ǰ������ѵ�id
				String uid = jsonObject.getString("uid");

				// ����ú����Ѵ��ڣ��򲻴���ֻ����
				if (Config.hashtable.containsKey(uid)) {
					// ȡ��hashtable�����е�������ѵ�FaceJPanel
					FaceJPanel faceJPanel = (FaceJPanel) Config.hashtable.get(uid);

					faceJPanel.setImg(jsonObject.getString("img"));
					faceJPanel.setName(jsonObject.getString("netname"));
					faceJPanel.setSign(jsonObject.getString("sign"));
				}
				// ����ú��Ѳ����ڣ����½�һ��FaceJPanel��put������hashtable
				else {
					Config.hashtable.put(jsonObject.getString("uid"),
							new FaceJPanel(jsonObject.getString("img"), jsonObject.getString("uid"),
									jsonObject.getString("netname"), jsonObject.getString("sign")));
				}
			}
		}
		//���ú���ͷ����ɫʵʱ����
		onlinegengxin();
	}

	/**
	 * �������ߣ�ͷ����ɫ��ʵʱ����
	 */
	public void onlinegengxin() {
		String zaixianliebiao = Config.haoyou_online_id_data;
		System.out.println(zaixianliebiao);

		String[] onlinefriendsid = zaixianliebiao.split(",");
		Set<String> allfriendsid = Config.hashtable.keySet();
		for (String uid : allfriendsid) {
			Config.hashtable.get(uid).setOnline(false);
		}
		if (!zaixianliebiao.equals("notFound")) {
			//System.out.println(Arrays.toString(onlinefriendsid));
			for (String uid : onlinefriendsid) {
				if(!uid.trim().equals("")){
					FaceJPanel tmp=(FaceJPanel)Config.hashtable.get(uid);
					tmp.setOnline(true);
				}
			}
		}

		
		/**
		 * ��hashtable�ĸ��³��ֵ�UI������
		 */
		
		//��hashtable.values()����һ���洢���к��ѵ�JPanel��Collection����
		Collection<FaceJPanel> faceJPanels = Config.hashtable.values();
		//�ٴ���һ��ArrayList�͵�List���齫collection����������ת�Ƶ�list�����У���Ϊֻ��list������collections.sort()��������hashtable.values()��ֻ����һ��collection��
		List<FaceJPanel> list2 = new ArrayList(faceJPanels);
		//��list����list2�е�Ԫ���������������list2�洢����������FaceJPanel���е�compareTo()����ȷ����
		Collections.sort(list2);

		//��������еĿؼ��������ڸ����û�����Ĳ����е�һ����
		this.removeAll();
		int i=0;
		for (FaceJPanel faceJPanel : list2) {
			//ע�⣬�κ�һ���ؼ���Ҫ��ִ��setBounds()���趨һ�����ʵ�λ�ò���ʹ�ã���һ���ؼ�δִ��setBounds()��Ĭ�ϲ���ʾ�ÿؼ�
			faceJPanel.setBounds(0,i++*55,546,59);
			this.add(faceJPanel);
		}

		this.setPreferredSize(new Dimension(0,40*Config.hashtable.size()));
		this.updateUI();
	}
}
