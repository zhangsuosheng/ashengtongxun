package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 用于登陆前的数据库服务：检查用户名和密码
 * public String loginForPhone(String usrname, String usrpassword)
 * @author acer
 *
 */
public class LoginCheck {

	
	/**
	 * 使用手机号码来登录
	 * 
	 * @param phone
	 * @param password
	 * @return
	 * @throws UsernameNotFoundException
	 *             用户不存在
	 * @throws PasswordException
	 *             密码错误
	 * @throws StateException
	 *             账户被锁定
	 */
	
	//这里使用 另一个方法调用login方法，为的是方便在有不同登录方式时对不同登陆方式作区分。
	public String loginForPhone(String usrname, String usrpassword)
			throws UsernameNotFoundException, PasswordException, StateException, SQLException {
		return login(usrname, usrpassword, "SELECT * FROM users where usrname=?");
	}

	
	private String login(String key, String usrpassword, String sql)
			/**
			 * 自定义异常来实现对 客户端用户名不存在UsernameNotFoundException、密码错误PasswordException、账户被封禁StateException的情况的处理
			 * 由于login函数中使用了从数据库连接池中获取的连接，该连接可能存在连接失败的情况，故该函数还需要抛出SQLException
			 */
			throws UsernameNotFoundException, PasswordException, StateException, SQLException {
		
		
		
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
			PreparedStatement pst = conn.prepareStatement(sql);
			//将java中的字符串传递到数据库语句中
			pst.setString(1, key);
			//执行数据库语句
			ResultSet rs = pst.executeQuery();
			
			
			//！！！！！依次验证用户名是否正确，是否被封号，密码是否正确，注意这段中用到的嵌套的3个if else和这段代码的逻辑！！！！！//
			//查询结果集中是否存在用户ResultSet.next()，若结果集为空，则表示用户名错误，则抛出UsernameNotFoundException
			if (rs.next()) {
				
				//第一次执行若不为空，则查询该用户当前的状态是否为封号ResultSet.getInt("状态列名")，若被封号则抛出StateException
				if (rs.getInt("state") == 0) {
					
					//若第一次执行不为空且状态值为未封号状态，则查询password并与用户传来的password比较，若密码错误则抛出PasswordException
					if (rs.getString("usrpassword").equals(usrpassword)) {
						
						//查询成功，返回第一个列中的值（用户id）
						//Result.getString(int) 获取结果集中当前游标指向的行的第int列的值（string类型，若该值为其他类型则应用对应类型的get方法）
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
		
		//在数据库连接使用完后不要忘记关闭数据库连接。
		finally {
			conn.close();
		}

	}

	
	/**
	 * 测试类，用于测试是否能成功连接数据库并验证信息
	 * 注意，测试类中就根据抛出的异常做对应的处理，成功则提示成功。
	 * @param args
	 
	public static void main(String[] args) {
		try {
			new LoginCheck().loginForPhone("zhangsuosheng", "123456");
			System.out.println("成功");
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
