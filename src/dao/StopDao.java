package dao;

import java.util.List;
import java.util.Map;

import utils.JDBCHelp;

public class StopDao {
	private JDBCHelp db=new JDBCHelp();
	
	/**
	 * ��ѯ���б����Ĵ���
	 * @return
	 */
	public List<Map<String,Object>> query(){
		String sql="select * from tbl_stop order by sid";
		return db.executeQuery(sql);
	}
	
	public int delWordById(String sid) {
		String sql="delete from tbl_stop where sid=?";
		return db.executeUpdate(sql, sid);
	}
}
