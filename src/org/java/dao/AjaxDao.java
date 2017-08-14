package org.java.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AjaxDao extends BaseDao {
	
	

	
	public void update(Integer id,String name,String gender,String clazz,Integer score,String bir){
		try {
			String sql="update info set name=?,gender=?,class=?,score=?,bir=? where id=?";
			pst = getConn().prepareStatement(sql);
			pst.setObject(1,name);
			pst.setObject(2,gender);
			pst.setObject(3,clazz);
			pst.setObject(4,score);
			pst.setObject(5,bir);
			pst.setObject(6,id);
			pst.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			close();
		}
	}
	
	
	public void del(Integer id){
		try {
			String sql = "delete from info where id="+id;
			pst = getConn().prepareStatement(sql);
			pst.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			close();
		}
	}
	
	
	public void add(String name,String gender,String clazz,Integer score,String bir){
		try {
			String sql = "insert into info values(null,?,?,?,?,?)";
			pst = getConn().prepareStatement(sql);
			pst.setString(1,name);
			pst.setString(2,gender);
			pst.setString(3,clazz);
			pst.setInt(4,score);
			pst.setObject(5,bir);
			
			pst.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			close();
		}
	}
	
	
	public int getCount(String name,String clazz,String gender,String result){
		try {
			String sql = "select count(*) from info where 1=1";
			
			//ƴ�Ӳ�ѯ����
			if(name!=null&&!name.equals("")){
				sql+=" and name like '%"+name+"%'";
			}
			if(clazz!=null&&!clazz.equals("")){
				sql+=" and class like '%"+clazz+"%'";
			}
			if(gender!=null&&!gender.equals("-1")){
				sql+=" and gender='"+gender+"'";
			}
			if(result!=null&&!result.equals("-1")){
				if(result.equals("1")){
					sql+=" and score>=90";
				}else if(result.equals("2")){
					sql+=" and score>=80";
				}else if(result.equals("3")){
					sql+=" and score>=60";
				}else{
					sql+=" and score<60";
				}
				
			}
			
			
			pst = getConn().prepareStatement(sql);
			rs = pst.executeQuery();
			if(rs.next()){
				return rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			close();
		}
		return 0;
	}
	/**
	 * 
	 * @param page ��ǰҳ
	 * @param size ÿҳ��ʾ��������
	 * @param sort ����һ���ֶ����� 
	 * @param order �����ǽ���
	 * 
	 ********�����ǲ�ѯ����*****************************
	 * 
	 * @param name  ����
	 * @param clazz  �༶
	 * @param gender �Ա� 
	 * @param result  ���˽��
	 * @return
	 */
	public List<Map<String, Object>> load(Integer page,Integer size,String sort,String order,String name,String clazz,String gender,String result) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			//���㿪ʼ�±�
			Integer start = (page-1)*size;
			
			//String sql = "select * from info limit ?,?";//��ʼ�±꣬��ʾ��������
			String sql = "select * from info where 1=1";
			
			//ƴ�Ӳ�ѯ����
			if(name!=null&&!name.equals("")){
				sql+=" and name like '%"+name+"%'";
			}
			if(clazz!=null&&!clazz.equals("")){
				sql+=" and class like '%"+clazz+"%'";
			}
			if(gender!=null&&!gender.equals("-1")){
				sql+=" and gender='"+gender+"'";
			}
			if(result!=null&&!result.equals("-1")){
				if(result.equals("1")){
					sql+=" and score>=90";
				}else if(result.equals("2")){
					sql+=" and score>=80";
				}else if(result.equals("3")){
					sql+=" and score>=60";
				}else{
					sql+=" and score<60";
				}
				
			}
			
			if(sort!=null){//�����Ϊ�գ���ʾҪ���� 
				if(sort.equals("clazz")){
					sort="class";
				}
				sql+=" order by "+sort+" "+order;
			}
			
			sql+=" limit ?,?";
			
			pst = getConn().prepareStatement(sql);
			
			//������ռλ����ֵ
			pst.setInt(1,start);//����һ���±꿪ʼ��ʾ
			pst.setInt(2,size);//��ʾ����
			
			
			rs = pst.executeQuery();
			while(rs.next()){
				Map<String,Object> m = new HashMap<String, Object>();
				m.put("id", rs.getObject(1));
				m.put("name", rs.getObject(2));
				m.put("gender", rs.getObject(3));
				m.put("clazz", rs.getObject(4));
				m.put("score", rs.getObject(5));
				m.put("bir", rs.getString(6));
				list.add(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return list;
	}
}
