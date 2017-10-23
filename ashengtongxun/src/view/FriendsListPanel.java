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
 * 大Panel 自定义的类（创建一个普通类然后写出来的） 放在好友列表窗体中的JScrollPane控件中
 * 其中放着若干个小Panel（自定义的FaceJPanel类）
 * 
 * @author acer
 *
 */
public class FriendsListPanel extends JPanel {

	public FriendsListPanel() {
		setLayout(null);
		
		//将初始登录时创建的好友列表面板实例的引用存入Config配置文件中，在NetService服务文件中更新好友列表面板时方便调用
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
	 * 好友列表实时更新
	 */
	public void gengxin() {
		String haoyou_data = Config.haoyou_json_data;
		System.out.println(haoyou_data);
		if(haoyou_data.equals("")){
			System.out.println("您没有好友！");
			return;
		}
		JSONArray jsonArray = JSONArray.fromObject(haoyou_data);

		//测试
		System.out.println("SB:"+Config.hashtable);

		// 登陆后第一次加载好友列表——存储好友列表缓存信息的hashtable长度为0
		if (Config.hashtable.size() == 0) {

			// 循环处理每一个好友
			for (int i = 0; i < jsonArray.size(); i++) {
				// 因为是第一次加载，所以直接新建一个FaceJPanel并put进缓存hashtable
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				Config.hashtable.put(jsonObject.getString("uid"), new FaceJPanel(jsonObject.getString("img"),
						jsonObject.getString("uid"), jsonObject.getString("netname"), jsonObject.getString("sign")));
			}
		}
		// 登陆后更新好友列表——存储好友列表缓存信息的hashtable长度不为0
		else {
			// 循环处理每一个好友
			for (int i = 0; i < jsonArray.size(); i++) {
				// 取当前这个好友对应的jsonObject
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				// 取当前这个好友的id
				String uid = jsonObject.getString("uid");

				// 如果该好友已存在，则不创建只更新
				if (Config.hashtable.containsKey(uid)) {
					// 取出hashtable中已有的这个好友的FaceJPanel
					FaceJPanel faceJPanel = (FaceJPanel) Config.hashtable.get(uid);

					faceJPanel.setImg(jsonObject.getString("img"));
					faceJPanel.setName(jsonObject.getString("netname"));
					faceJPanel.setSign(jsonObject.getString("sign"));
				}
				// 如果该好友不存在，则新建一个FaceJPanel并put进缓存hashtable
				else {
					Config.hashtable.put(jsonObject.getString("uid"),
							new FaceJPanel(jsonObject.getString("img"), jsonObject.getString("uid"),
									jsonObject.getString("netname"), jsonObject.getString("sign")));
				}
			}
		}
		//调用好友头像颜色实时更新
		onlinegengxin();
	}

	/**
	 * 好友在线（头像颜色）实时更新
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
		 * 将hashtable的更新呈现到UI界面中
		 */
		
		//用hashtable.values()创建一个存储所有好友的JPanel的Collection容器
		Collection<FaceJPanel> faceJPanels = Config.hashtable.values();
		//再创建一个ArrayList型的List数组将collection容器的内容转移到list容器中（因为只有list才能用collections.sort()方法，而hashtable.values()又只返回一个collection）
		List<FaceJPanel> list2 = new ArrayList(faceJPanels);
		//将list容器list2中的元素排序（排序规则由list2存储的数据类型FaceJPanel类中的compareTo()方法确定）
		Collections.sort(list2);

		//清空容器中的控件（常用于更新用户界面的操作中的一步）
		this.removeAll();
		int i=0;
		for (FaceJPanel faceJPanel : list2) {
			//注意，任何一个控件都要先执行setBounds()来设定一个合适的位置才能使用，若一个控件未执行setBounds()则默认不显示该控件
			faceJPanel.setBounds(0,i++*55,546,59);
			this.add(faceJPanel);
		}

		this.setPreferredSize(new Dimension(0,40*Config.hashtable.size()));
		this.updateUI();
	}
}
