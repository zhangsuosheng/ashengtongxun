package db;

import java.sql.Connection;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 用于连接数据库配置
 * 
 * @author 张所晟
 *
 */
public class DBManager {

	// 定义要链接的那个数据库的个参数，等会用set方法传给数据库连接池或数据库对应的对象
	public static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
	public static final String USERNAME = "";
	public static final String PASSWORD = "";
	public static final String URL = "";
	public static DataSource dataSource=null;
	static {
		try {
			// 创建一个数据库连接池
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
			System.out.println("数据库连接池加载失败！");
		}
	}
	
	public static Connection getConnection()throws SQLException{
		return dataSource.getConnection();
	}
}
