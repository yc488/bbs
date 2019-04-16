package dao;

import java.util.List;
import java.util.Map;

import bean.User;
import bean.collect;
import utils.JDBCHelp;
import utils.Myutil;

public class CollectDao {
	JDBCHelp db=new JDBCHelp();
	
	/**
	 * �����ղ�
	 * @param col
	 * @return
	 */
	public int addCollect(collect col) {
		String sql="insert into tbl_collect values(null,?,?,?,sysdate())";
		return db.executeUpdate(sql, col.getTopicid(),col.getUid(),col.getBoardid());
		
	}
	
	/**
	 * �鿴�ҵ��ղ�
	 * @param col
	 * @return
	 */
	public List<collect> findMyCollect(collect col) {
		String sql="SELECT\r\n" + 
				"  *\r\n" + 
				"FROM\r\n" + 
				"  (SELECT\r\n" + 
				"    e.*,\r\n" + 
				"    f.`uname` AS sendname\r\n" + 
				"  FROM\r\n" + 
				"    (SELECT\r\n" + 
				"      a.cid,\r\n" + 
				"      a.topicid,\r\n" + 
				"      a.uid,\r\n" + 
				"      a.boardid,\r\n" + 
				"      DATE_FORMAT(\r\n" + 
				"        a.collectime,\r\n" + 
				"        '%Y-%m-%d %H:%i:%s'\r\n" + 
				"      ) AS collectime,\r\n" + 
				"      c.`title`,\r\n" + 
				"      c.uid AS sendid,\r\n" + 
				"      d.`boardname`\r\n" + 
				"    FROM\r\n" + 
				"      tbl_collect a\r\n" + 
				"      LEFT JOIN tbl_topic c\r\n" + 
				"        ON c.`topicid` = a.`topicid`\r\n" + 
				"      LEFT JOIN tbl_board d\r\n" + 
				"        ON d.`boardid` = a.`boardid`\r\n" + 
				"    WHERE a.uid = ?) e\r\n" + 
				"    LEFT JOIN tbl_user f\r\n" + 
				"      ON e.sendid = f.`uid`) g";
		List<Map<String,Object>> list = db.executeQuery(sql,col.getUid());
		return Myutil.ListMapToJavaBean(list, collect.class);
	}
	
	/**
	 * ȡ���ղ�
	 * @param col
	 * @return
	 */
	public int cancleCollect(collect col) {
		String sql="delete from tbl_collect where cid=?";
		
		return db.executeUpdate(sql, col.getCid());
	}

	/**
	 * �����ǰ�û��ղ����ӵ�����
	 * @param user
	 */
	public int findAllCollect(User user) {
		String sql="select count(cid) as total from tbl_collect where uid=?";
		List<Map<String,Object>> executeQuery = db.executeQuery(sql, user.getUid());
		int total=Integer.parseInt( executeQuery.get(0).get("total").toString()) ;
		return total;
	}
}
