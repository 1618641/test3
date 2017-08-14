package org.java.dao;
import java.sql.*;
public class BaseDao {

	protected Connection con;
	protected PreparedStatement pst;
	protected ResultSet rs;
	
	/**
	 * 产生连接的方法
	 * @return
	 */
	protected Connection getConn(){
		try {
			String url ="jdbc:mysql://localhost:3306/mydb";
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url,"root","root");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	
	/**
	 * 关闭资源的方法
	 */
	protected void close(){
		try {
			if(rs!=null)rs.close();
			if(pst!=null)pst.close();
			if(con!=null)con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
