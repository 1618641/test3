package org.java.dao;

import java.util.HashMap;
import java.util.Map;

public class AjaxDao2 extends BaseDao {


	public Map<String,Object> getBar(){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String sql = "SELECT result,COUNT(*) val FROM (SELECT NAME,class, CASE WHEN score>=90 THEN '90分及以上' WHEN score>=80 THEN '80分及以上' WHEN score>=60 THEN '60分及以上' ELSE '60分以下' END result FROM info) t GROUP BY result";
			pst = getConn().prepareStatement(sql);
			rs = pst.executeQuery();
			while(rs.next()){
				map.put(rs.getString("result"),rs.getObject("val"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			close();
		}
		return map;
	}
}
