package db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * �����û���¼������ݿ����
 * 1����ѯ�����б�
 * 2����ѯ������Ϣ
 * 3��ÿ��һ��ʱ���ѯ���ߺ���
 * @author acer
 *
 */
public class UserService{
	
	
	/**
	 * �����б��ѯ
	 * @param uid
	 * @return
	 * @throws SQLException
	 */
	public Vector<UserInfo2> getHaoyouliebiao(String uid)throws SQLException{
		// ����һ�����ݿ����Ӷ���
				Connection conn = null;
				
				//������ȡ��ʹ�����ݿ����ӵĹ��̿��ܴ������Ӵ��󣬹���try�����������SQLException�쳣
				try {
					
					// �Ӱ�װ�����ݿ����ӳص����л�ȡһ��Connection����
					conn = DBManager.getConnection();
					
					
					
					/**
					 * ��Connection.prepareStatement("���ݿ����")����ȡһ��PreparedStatement����
					 * PreparedStatement����Ĺ��ܾ��Ǵ洢һ�����ݿ���䣬ͨ����set������java�еõ��Ĳ������ݸ�������ݿ���䲢ִ�и����
					 * �����ݿ�����е���Ҫ�ⲿ�������������Ϊ"��"������PreparedStatement.setString(�ʺŵı��,����)���������ݸ����ݿ����
					 */
					//ʹ�����ݿ����Ӻ����ݿ��������PrepareStatement����
					PreparedStatement pst = conn.prepareStatement("SELECT u.`uid`,u.`img`,u.`name`,u.`sign` FROM friends h INNER JOIN users u ON u.`uid`=h.`hyuid` AND h.`uid`=?");
					//��java�е��ַ������ݵ����ݿ������					
					pst.setString(1, uid);
					//ִ�����ݿ����
					ResultSet rs = pst.executeQuery();
					
					//��Ϊ�����б��ﲻһ���ж��ٸ����ѣ�ÿһ�����ѵ���Ϣ����Ҫһ��UserInfo2�������洢����ʹ��һ��UserInfo2��Vector��������
					//����һ��userinfo2������ ���������ÿ��userinfo��ֵ���ٽ�userinfo��ӵ�������
					Vector<UserInfo2> vector=new Vector();
					while(rs.next()){
						UserInfo2 userInfo=new UserInfo2();
						userInfo.setUid(rs.getString(1));
						userInfo.setImg(rs.getString(2));
						userInfo.setNetname(rs.getString(3));
						userInfo.setSign(rs.getString(4));
						vector.add(userInfo);
					}
					//����������к����б���Ϣ��vector����
					return vector;
					
					
				} catch (SQLException e) {
					throw e;
				} 
				
				//�����ݿ�����ʹ�����Ҫ���ǹر����ݿ����ӡ�
				finally {
					conn.close();
				}
	}
	
	/**
	 * ������Ϣ��ѯ
	 * @param uid
	 * @return
	 * @throws SQLException
	 */
	public UserInfo3 getUserinfo(String uid)throws SQLException{
		
		Connection conn = null;
		
		//������ȡ��ʹ�����ݿ����ӵĹ��̿��ܴ������Ӵ��󣬹���try�����������SQLException�쳣
		try {
			
			// �Ӱ�װ�����ݿ����ӳص����л�ȡһ��Connection����
			conn = DBManager.getConnection();
			
			
			
			/**
			 * ��Connection.prepareStatement("���ݿ����")����ȡһ��PreparedStatement����
			 * PreparedStatement����Ĺ��ܾ��Ǵ洢һ�����ݿ���䣬ͨ����set������java�еõ��Ĳ������ݸ�������ݿ���䲢ִ�и����
			 * �����ݿ�����е���Ҫ�ⲿ�������������Ϊ"��"������PreparedStatement.setString(�ʺŵı��,����)���������ݸ����ݿ����
			 */
			//ʹ�����ݿ����Ӻ����ݿ��������PrepareStatement����
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE UID=?");
			//��java�е��ַ������ݵ����ݿ������
			pst.setString(1, uid);
			//ִ�����ݿ����
			ResultSet rs = pst.executeQuery();
			
			
			//��Ϊ������Ϣֻ��Ҫһ��UserInfo3���󼴿ɴ洢���ʲ���Ҫ����һ��vector
			UserInfo3 userInfo3=new UserInfo3();
			while(rs.next()){
				userInfo3.setUid(rs.getString("uid"));
				userInfo3.setUsrname(rs.getString("usrname"));
				userInfo3.setNetname(rs.getString("name"));
				userInfo3.setSign(rs.getString("sign"));
				userInfo3.setRealname(rs.getString("realname"));
				userInfo3.setSex(rs.getString("sex"));
				userInfo3.setAge(rs.getInt("age"));
				userInfo3.setTip(rs.getString("tip"));
				userInfo3.setImg(rs.getString("img"));
			}
			//����������к����б���Ϣ��vector����
			return userInfo3;
			
			
		} catch (SQLException e) {
			throw e;
		} 
		
		//�����ݿ�����ʹ�����Ҫ���ǹر����ݿ����ӡ�
		finally {
			conn.close();
		}
		
	}
	
	public void regUser(String usrname,String usrpassword)throws UserNameUsedException,IOException, SQLException{
Connection conn = null;
		
		//������ȡ��ʹ�����ݿ����ӵĹ��̿��ܴ������Ӵ��󣬹���try�����������SQLException�쳣
		try {
			
			// �Ӱ�װ�����ݿ����ӳص����л�ȡһ��Connection����
			conn = DBManager.getConnection();
			
			
			
			/**
			 * ��Connection.prepareStatement("���ݿ����")����ȡһ��PreparedStatement����
			 * PreparedStatement����Ĺ��ܾ��Ǵ洢һ�����ݿ���䣬ͨ����set������java�еõ��Ĳ������ݸ�������ݿ���䲢ִ�и����
			 * �����ݿ�����е���Ҫ�ⲿ�������������Ϊ"��"������PreparedStatement.setString(�ʺŵı��,����)���������ݸ����ݿ����
			 */
			//ʹ�����ݿ����Ӻ����ݿ��������PrepareStatement����
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE usrname=?");
			//��java�е��ַ������ݵ����ݿ������
			pst.setString(1,usrname);
			//ִ�����ݿ����
			ResultSet rs = pst.executeQuery();
			if(rs.next()){
				throw new UserNameUsedException();
			}
			
			pst=conn.prepareStatement("INSERT INTO users(uid,usrname,usrpassword,createtime) VALUES(?,?,?,SYSDATE())");
			pst.setString(1,System.currentTimeMillis()+"R"+(int)(Math.random()*10000));
			pst.setString(2,usrname);
			pst.setString(3,usrpassword);
			
			if(pst.executeUpdate()<=0){
				throw new SQLException();
			}
			
			
			
		} catch (SQLException e) {
			throw e;
		} 
		
		//�����ݿ�����ʹ�����Ҫ���ǹر����ݿ����ӡ�
		finally {
			conn.close();
		}
	}
	
}
