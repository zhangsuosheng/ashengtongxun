package db;

import java.sql.Connection;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * �����������ݿ�����
 * 
 * @author ������
 *
 */
public class DBManager {

	// ����Ҫ���ӵ��Ǹ����ݿ�ĸ��������Ȼ���set�����������ݿ����ӳػ����ݿ��Ӧ�Ķ���
	public static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
	public static final String USERNAME = "";
	public static final String PASSWORD = "";
	public static final String URL = "";
	public static DataSource dataSource=null;
	static {
		try {
			// ����һ�����ݿ����ӳ�
			ComboPooledDataSource pool = new ComboPooledDataSource();
			pool.setDriverClass(DRIVER_NAME);
			pool.setUser(USERNAME);
			pool.setPassword(PASSWORD);
			pool.setJdbcUrl(URL);
			pool.setMaxPoolSize(30);
			pool.setMinPoolSize(5);
			dataSource=pool;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("���ݿ����ӳؼ���ʧ�ܣ�");
		}
	}
	
	public static Connection getConnection()throws SQLException{
		return dataSource.getConnection();
	}
}
