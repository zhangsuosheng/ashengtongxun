package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ���ڵ�½ǰ�����ݿ���񣺼���û���������
 * public String loginForPhone(String usrname, String usrpassword)
 * @author acer
 *
 */
public class LoginCheck {

	
	/**
	 * ʹ���ֻ���������¼
	 * 
	 * @param phone
	 * @param password
	 * @return
	 * @throws UsernameNotFoundException
	 *             �û�������
	 * @throws PasswordException
	 *             �������
	 * @throws StateException
	 *             �˻�������
	 */
	
	//����ʹ�� ��һ����������login������Ϊ���Ƿ������в�ͬ��¼��ʽʱ�Բ�ͬ��½��ʽ�����֡�
	public String loginForPhone(String usrname, String usrpassword)
			throws UsernameNotFoundException, PasswordException, StateException, SQLException {
		return login(usrname, usrpassword, "SELECT * FROM users where usrname=?");
	}

	
	private String login(String key, String usrpassword, String sql)
			/**
			 * �Զ����쳣��ʵ�ֶ� �ͻ����û���������UsernameNotFoundException���������PasswordException���˻������StateException������Ĵ���
			 * ����login������ʹ���˴����ݿ����ӳ��л�ȡ�����ӣ������ӿ��ܴ�������ʧ�ܵ�������ʸú�������Ҫ�׳�SQLException
			 */
			throws UsernameNotFoundException, PasswordException, StateException, SQLException {
		
		
		
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
			PreparedStatement pst = conn.prepareStatement(sql);
			//��java�е��ַ������ݵ����ݿ������
			pst.setString(1, key);
			//ִ�����ݿ����
			ResultSet rs = pst.executeQuery();
			
			
			//����������������֤�û����Ƿ���ȷ���Ƿ񱻷�ţ������Ƿ���ȷ��ע��������õ���Ƕ�׵�3��if else����δ�����߼�����������//
			//��ѯ��������Ƿ�����û�ResultSet.next()���������Ϊ�գ����ʾ�û����������׳�UsernameNotFoundException
			if (rs.next()) {
				
				//��һ��ִ������Ϊ�գ����ѯ���û���ǰ��״̬�Ƿ�Ϊ���ResultSet.getInt("״̬����")������������׳�StateException
				if (rs.getInt("state") == 0) {
					
					//����һ��ִ�в�Ϊ����״ֵ̬Ϊδ���״̬�����ѯpassword�����û�������password�Ƚϣ�������������׳�PasswordException
					if (rs.getString("usrpassword").equals(usrpassword)) {
						
						//��ѯ�ɹ������ص�һ�����е�ֵ���û�id��
						//Result.getString(int) ��ȡ������е�ǰ�α�ָ����еĵ�int�е�ֵ��string���ͣ�����ֵΪ����������Ӧ�ö�Ӧ���͵�get������
						return rs.getString(1);
					} else {
						throw new PasswordException();
					}
				} else {
					throw new StateException();
				}

			} else {
				throw new UsernameNotFoundException();
			}

			
			
			
		} catch (SQLException e) {
			throw e;
		} 
		
		//�����ݿ�����ʹ�����Ҫ���ǹر����ݿ����ӡ�
		finally {
			conn.close();
		}

	}

	
	/**
	 * �����࣬���ڲ����Ƿ��ܳɹ��������ݿⲢ��֤��Ϣ
	 * ע�⣬�������о͸����׳����쳣����Ӧ�Ĵ����ɹ�����ʾ�ɹ���
	 * @param args
	 
	public static void main(String[] args) {
		try {
			new LoginCheck().loginForPhone("zhangsuosheng", "123456");
			System.out.println("�ɹ�");
		} catch (UsernameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PasswordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
}
