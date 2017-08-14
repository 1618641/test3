package org.java.dao;
import java.sql.*;
public class BaseDao {

	protected Connection con;
	protected PreparedStatement pst;
	protected ResultSet rs;
	
	/**
	 * �������ӵķ���
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
	 * �ر���Դ�ķ���
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
