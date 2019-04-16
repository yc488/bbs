package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.PageBean;
import bean.Topic;
import bean.User;
import utils.JDBCHelp;
import utils.Myutil;

public class TopicDao {
	
	private JDBCHelp db=new JDBCHelp();
	/**
	 * 查询用户的帖子
	 * @param topic
	 * @return
	 */
	public List<Topic> findMyTopic(Topic topic) {
		String sql = "select * from tbl_topic where uid = ?";
		List<Map<String,Object>> executeQuery  = db.executeQuery(sql, topic.getUid());
		return Myutil.ListMapToJavaBean(executeQuery, Topic.class);
	}
	
	
	/**
	 * 每一页的topic信息
	 * @param topic
	 * @return
	 */
	public PageBean<Topic> findPageBean(  Topic topic  ) {
		PageBean<Topic> pb=new PageBean<>();
		
		//该板块所有帖子的信息
		List<Topic> list = findBoardTopic( topic);
		
		//帖子的总记录数
		int total=findTopicCount( topic);
		
		pb.setList(list);
		
		pb.setTotal((long)total);
		
		//总页数
		int totalpages=total%topic.getPagesize()==0?total/topic.getPagesize():(total/topic.getPagesize()+1);
		
		if(totalpages==0) {
			totalpages=1;
		}
		pb.setTotalPage((long)totalpages);
		return pb;
		
	}
	
	/**
	 * 查询当前板块的所有topic
	 * @param topic
	 * @return
	 */
	public List<Topic> findBoardTopic(Topic topic) {
		StringBuffer sql=new StringBuffer();
		
		sql.append(" select * from (select a.topicid,title,content,publishtime,modifytime,uid,uname,boardid, total as replycount ");
		sql.append(" from		      (		     select topicid,title,content,date_format(publishtime,'%Y-%m-%d %H:%i:%s') as publishtime,date_format(modifytime,'%Y-%m-%d %H:%i:%s') as  modifytime,  tbl_user.uid,  uname,boardid ");
		sql.append(" from tbl_topic  inner join tbl_user on tbl_topic.uid=tbl_user.uid where boardid=?  order by modifytime desc limit ?,?) a ");
		sql.append(" left join  (select topicid, count(*) as total from tbl_reply group by topicid) b on a.topicid=b.topicid   order by publishtime desc )  d");
		List<Object> params=new ArrayList<>();
		params.add(topic.getBoardid());
		int start=(topic.getPages()-1)*topic.getPagesize();
		
		params.add(start);
		params.add(topic.getPagesize());
		
		List<Map<String,Object>> executeQuery = db.executeQuery(sql.toString(), params);
		return Myutil.ListMapToJavaBean(executeQuery, Topic.class);
				
	}
	
	/**
	 * 查询当前板块的所有topic
	 * @param topic
	 * @return
	 */
	public int findTopicCount( Topic topic ) {
		String sql="select count(*) as total from tbl_topic where boardid=?";
		List<Map<String, Object>> map = db.executeQuery(sql, topic.getBoardid());
		return  Integer.parseInt( map.get(0).get("total").toString() );
	}
	
	/**
	 * 发帖
	 * @param topic
	 * @param email
	 * @return
	 */
	public int post(Topic topic,String email){
		
		String sql="insert into tbl_topic values(null,?,?,now(),now(),?,?)";
		return db.executeUpdate(sql, topic.getTitle()
							, topic.getContent()
							, topic.getUid()
							, topic.getBoardid()
				);
	}
	
	/**
	 *查询单个帖子的详情
	 * @param topic
	 * @return
	 */
	public Topic topicdetail(Topic topic) {
		String sql="select * from ("+
				"select topicid,title,content,\r\n" + 
				"       date_format(publishtime,'%Y-%m-%d %H:%i:%s') as publishtime,\r\n" + 
				"       date_format(modifytime,'%Y-%m-%d %H:%i:%s') as  modifytime, \r\n" + 
				"       tbl_topic.uid, \r\n" + 
				"       uname,\r\n" + 
				"       head,\r\n" + 
				"       date_format(regtime,'%Y-%m-%d %H:%i:%s') as  regtime, \r\n" + 
				"       boardid   \r\n" + 
				" from tbl_topic \r\n" + 
				" inner join tbl_user\r\n" + 
				" on tbl_topic.uid=tbl_user.uid\r\n" + 
				 " where topicid=?) a";
		List<Map<String,Object>> executeQuery = db.executeQuery(sql, topic.getTopicid());
		
		return Myutil.ListMapToJavaBean(executeQuery, Topic.class).get(0);
	}
	
	
	/**
	 * 删除帖子
	 * @param topic
	 * @return
	 */
	public int delTopic(Topic topic) {
		String sql1="delete from tbl_reply where topicid=?";
		String sql="delete from tbl_topic where topicid=?";
		db.executeUpdate(sql1, topic.getTopicid());
		return db.executeUpdate(sql, topic.getTopicid());
	}
	
	/**
	 * 每个板块前13的热帖
	 * @param topic
	 * @return
	 */
	public List<Topic> findHostTopic(Topic topic) {
		StringBuffer sql=new StringBuffer();
		
		sql.append(" select * from ( select * from (select a.topicid,title,content,publishtime,modifytime,uid,uname,boardid, total as replycount ");
		sql.append(" from		      (		     select topicid,title,content,date_format(publishtime,'%Y-%m-%d %H:%i:%s') as publishtime,date_format(modifytime,'%Y-%m-%d %H:%i:%s') as  modifytime,  tbl_user.uid,  uname,boardid ");
		sql.append(" from tbl_topic  inner join tbl_user on tbl_topic.uid=tbl_user.uid where boardid=?  order by modifytime desc ) a ");
		sql.append(" left join  (select topicid, count(*) as total from tbl_reply group by topicid) b on a.topicid=b.topicid   order by total desc )  d ) e limit 0,10");
		List<Object> params=new ArrayList<>();
		params.add(topic.getBoardid());
		List<Map<String,Object>> executeQuery = db.executeQuery(sql.toString(), params);
		
		return Myutil.ListMapToJavaBean(executeQuery, Topic.class);
				
	}
	
	
	/**
	 * 论坛热帖
	 * @return
	 */
	public List<Topic> findAllHostTopic() {
		StringBuffer sql=new StringBuffer();
		
		sql.append(" select * from ( select * from (select a.topicid,title,content,publishtime,modifytime,uid,uname,a.boardid,c.boardname, total as replycount ");
		sql.append(" from ( select topicid,title,content,date_format(publishtime,'%Y-%m-%d %H:%i:%s') as publishtime,date_format(modifytime,'%Y-%m-%d %H:%i:%s') as  modifytime,  tbl_user.uid,  uname,boardid ");
		sql.append(" from tbl_topic  inner join tbl_user on tbl_topic.uid=tbl_user.uid order by modifytime desc ) a ");
		sql.append(" left join  (select topicid, count(*) as total from tbl_reply group by topicid) b on a.topicid=b.topicid LEFT JOIN tbl_board c  ON a.boardid=c.boardid  order by total desc )  d ) e limit 0,13");

		
		List<Map<String,Object>> executeQuery = db.executeQuery(sql.toString());
		
		return Myutil.ListMapToJavaBean(executeQuery, Topic.class);
				
	}
	
	
	/**
	 * 风云人物
	 * @return
	 */
	public List<User> personTop() {
		String sql="select * from ( SELECT\n" + 
				"  a.*,\n" + 
				"  b.total\n" + 
				"FROM\n" + 
				"  tbl_user a\n" + 
				"  LEFT JOIN (\n" + 
				"      SELECT uid,\n" + 
				"      COUNT(uid) AS total FROM tbl_topic GROUP BY uid\n" + 
				"    ) b\n" + 
				"    ON a.uid = b.uid ) c order by total desc limit 0,13\n";
		
		
		List<Map<String,Object>> executeQuery = db.executeQuery(sql);
		
		return Myutil.ListMapToJavaBean(executeQuery, User.class);
				
	}
	
	/**
	 * 风云人物的所有帖子
	 * @return
	 */
	public List<Topic> personTopTopic(Topic topic) {
		String sql="SELECT\n" + 
				"    *\n" + 
				"  FROM\n" + 
				"    (SELECT\n" + 
				"      a.topicid,\n" + 
				"      title,\n" + 
				"      content,\n" + 
				"      publishtime,\n" + 
				"      modifytime,\n" + 
				"      uid,\n" + 
				"      uname,\n" + 
				"      a.boardid,\n" + 
				"      c.boardname,\n" + 
				"      total AS replycount\n" + 
				"    FROM\n" + 
				"      (SELECT\n" + 
				"        topicid,\n" + 
				"        title,\n" + 
				"        content,\n" + 
				"        DATE_FORMAT(\n" + 
				"          publishtime,\n" + 
				"          '%Y-%m-%d %H:%i:%s'\n" + 
				"        ) AS publishtime,\n" + 
				"        DATE_FORMAT(modifytime, '%Y-%m-%d %H:%i:%s') AS modifytime,\n" + 
				"        tbl_user.uid,\n" + 
				"        uname,\n" + 
				"        boardid\n" + 
				"      FROM\n" + 
				"        tbl_topic\n" + 
				"        INNER JOIN tbl_user\n" + 
				"          ON tbl_topic.uid = tbl_user.uid\n" + 
				"          WHERE tbl_user.uid=?\n" + 
				"      ORDER BY modifytime DESC) a\n" + 
				"      LEFT JOIN\n" + 
				"        (SELECT\n" + 
				"          topicid,\n" + 
				"          COUNT(*) AS total\n" + 
				"        FROM\n" + 
				"          tbl_reply\n" + 
				"        GROUP BY topicid) b\n" + 
				"        ON a.topicid = b.topicid\n" + 
				"        \n" + 
				"    LEFT JOIN tbl_board c \n" + 
				"    ON a.boardid=c.boardid    \n" + 
				"    \n" + 
				"    \n" + 
				"    ORDER BY total DESC) d";
		
		
		List<Map<String,Object>> executeQuery = db.executeQuery(sql,topic.getUid());
		
		return Myutil.ListMapToJavaBean(executeQuery, Topic.class);
				
	}

	/**
	 * 查找帖子
	 * @param topicname
	 * @return 
	 */
	public List<Topic> serachTopic(String topicname) {
		String sql="SELECT\n" + 
				"  *\n" + 
				"FROM\n" + 
				"  (SELECT\n" + 
				"    a.topicid,\n" + 
				"    title,\n" + 
				"    content,\n" + 
				"    publishtime,\n" + 
				"    modifytime,\n" + 
				"    uid,\n" + 
				"    uname,\n" + 
				"    a.boardid,\n" + 
				"    c.boardname,\n" + 
				"    total AS replycount\n" + 
				"  FROM\n" + 
				"    (SELECT\n" + 
				"      topicid,\n" + 
				"      title,\n" + 
				"      content,\n" + 
				"      DATE_FORMAT(\n" + 
				"        publishtime,\n" + 
				"        '%Y-%m-%d %H:%i:%s'\n" + 
				"      ) AS publishtime,\n" + 
				"      DATE_FORMAT(modifytime, '%Y-%m-%d %H:%i:%s') AS modifytime,\n" + 
				"      tbl_user.uid,\n" + 
				"      uname,\n" + 
				"      boardid\n" + 
				"    FROM\n" + 
				"      tbl_topic\n" + 
				"      INNER JOIN tbl_user\n" + 
				"        ON tbl_topic.uid = tbl_user.uid\n" + 
				"    WHERE title LIKE \'%"+topicname+"%\' \n" + 
				"    ORDER BY modifytime DESC) a\n" + 
				"    LEFT JOIN\n" + 
				"      (SELECT\n" + 
				"        topicid,\n" + 
				"        COUNT(*) AS total\n" + 
				"      FROM\n" + 
				"        tbl_reply\n" + 
				"      GROUP BY topicid) b\n" + 
				"      ON a.topicid = b.topicid\n" + 
				"    LEFT JOIN tbl_board c\n" + 
				"      ON a.boardid = c.boardid\n" + 
				"  ORDER BY total DESC) d";
		List<Map<String,Object>> executeQuery = db.executeQuery(sql);
		return Myutil.ListMapToJavaBean(executeQuery, Topic.class);
	}
	
	
}
