package db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * 用于用户登录后的数据库服务
 * 1、查询好友列表
 * 2、查询个人信息
 * 3、每隔一段时间查询在线好友
 * @author acer
 *
 */
public class UserService{
	
	
	/**
	 * 好友列表查询
	 * @param uid
	 * @return
	 * @throws SQLException
	 */
	public Vector<UserInfo2> getHaoyouliebiao(String uid)throws SQLException{
		// 创建一个数据库连接对象
				Connection conn = null;
				
				//整个获取、使用数据库连接的过程可能存在连接错误，故用try块包裹并捕获SQLException异常
				try {
					
					// 从包装有数据库连接池的类中获取一个Connection对象
					conn = DBManager.getConnection();
					
					
					
					/**
					 * 用Connection.prepareStatement("数据库语句")来获取一个PreparedStatement对象
					 * PreparedStatement对象的功能就是存储一句数据库语句，通过在set方法将java中得到的参数传递给这句数据库语句并执行该语句
					 * 将数据库语句中的需要外部输入的内容设置为"？"，利用PreparedStatement.setString(问号的编号,参数)将参数传递给数据库语句
					 */
					//使用数据库连接和数据库语句生成PrepareStatement对象
					PreparedStatement pst = conn.prepareStatement("SELECT u.`uid`,u.`img`,u.`name`,u.`sign` FROM friends h INNER JOIN users u ON u.`uid`=h.`hyuid` AND h.`uid`=?");
					//将java中的字符串传递到数据库语句中					
					pst.setString(1, uid);
					//执行数据库语句
					ResultSet rs = pst.executeQuery();
					
					//因为好友列表里不一定有多少个好友，每一个好友的信息又需要一个UserInfo2对象来存储，故使用一个UserInfo2的Vector容器！！
					//创建一个userinfo2容器， 遍历结果给每个userinfo赋值，再将userinfo添加到容器中
					Vector<UserInfo2> vector=new Vector();
					while(rs.next()){
						UserInfo2 userInfo=new UserInfo2();
						userInfo.setUid(rs.getString(1));
						userInfo.setImg(rs.getString(2));
						userInfo.setNetname(rs.getString(3));
						userInfo.setSign(rs.getString(4));
						vector.add(userInfo);
					}
					//返回这个含有好友列表信息的vector容器
					return vector;
					
					
				} catch (SQLException e) {
					throw e;
				} 
				
				//在数据库连接使用完后不要忘记关闭数据库连接。
				finally {
					conn.close();
				}
	}
	
	/**
	 * 个人信息查询
	 * @param uid
	 * @return
	 * @throws SQLException
	 */
	public UserInfo3 getUserinfo(String uid)throws SQLException{
		
		Connection conn = null;
		
		//整个获取、使用数据库连接的过程可能存在连接错误，故用try块包裹并捕获SQLException异常
		try {
			
			// 从包装有数据库连接池的类中获取一个Connection对象
			conn = DBManager.getConnection();
			
			
			
			/**
			 * 用Connection.prepareStatement("数据库语句")来获取一个PreparedStatement对象
			 * PreparedStatement对象的功能就是存储一句数据库语句，通过在set方法将java中得到的参数传递给这句数据库语句并执行该语句
			 * 将数据库语句中的需要外部输入的内容设置为"？"，利用PreparedStatement.setString(问号的编号,参数)将参数传递给数据库语句
			 */
			//使用数据库连接和数据库语句生成PrepareStatement对象
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE UID=?");
			//将java中的字符串传递到数据库语句中
			pst.setString(1, uid);
			//执行数据库语句
			ResultSet rs = pst.executeQuery();
			
			
			//因为个人信息只需要一个UserInfo3对象即可存储，故不需要申请一个vector
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
			//返回这个含有好友列表信息的vector容器
			return userInfo3;
			
			
		} catch (SQLException e) {
			throw e;
		} 
		
		//在数据库连接使用完后不要忘记关闭数据库连接。
		finally {
			conn.close();
		}
		
	}
	
	public void regUser(String usrname,String usrpassword)throws UserNameUsedException,IOException, SQLException{
Connection conn = null;
		
		//整个获取、使用数据库连接的过程可能存在连接错误，故用try块包裹并捕获SQLException异常
		try {
			
			// 从包装有数据库连接池的类中获取一个Connection对象
			conn = DBManager.getConnection();
			
			
			
			/**
			 * 用Connection.prepareStatement("数据库语句")来获取一个PreparedStatement对象
			 * PreparedStatement对象的功能就是存储一句数据库语句，通过在set方法将java中得到的参数传递给这句数据库语句并执行该语句
			 * 将数据库语句中的需要外部输入的内容设置为"？"，利用PreparedStatement.setString(问号的编号,参数)将参数传递给数据库语句
			 */
			//使用数据库连接和数据库语句生成PrepareStatement对象
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE usrname=?");
			//将java中的字符串传递到数据库语句中
			pst.setString(1,usrname);
			//执行数据库语句
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
		
		//在数据库连接使用完后不要忘记关闭数据库连接。
		finally {
			conn.close();
		}
	}
	
}
