package org.java.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AjaxDao3 extends BaseDao {

	/**
	 * 返回要显示的数据，二维数组
	 * @return
	 */
	public List<Map<String,Object>> getList(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try {
			String sql = "SELECT result,COUNT(*) val FROM (SELECT NAME,score,CASE WHEN score>=90 THEN '90分及以上' WHEN score>=80 THEN '80-90分' WHEN score>=60 THEN '60-80分' ELSE '60分以下' END result FROM info) t GROUP BY result";
			pst = getConn().prepareStatement(sql);
			rs = pst.executeQuery();
			while(rs.next()){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name",rs.getString("result"));
				map.put("value",rs.getInt("val"));
				list.add(map);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			close();
		}
		return list;
	}
	
}
