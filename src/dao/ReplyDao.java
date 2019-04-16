package dao;

import java.util.List;
import java.util.Map;

import bean.PageBean;
import bean.Reply;
import bean.Topic;
import biz.BizException;
import utils.JDBCHelp;
import utils.Myutil;

public class ReplyDao {
	private JDBCHelp db=new JDBCHelp();

	/**
	 * �ظ�����
	 * @throws BizException 
	 */
	public int answer(Topic topic,String email) throws BizException {
		String sql="insert into tbl_reply values(null,null,?,now(),now(),?,?,0)";
		return db.executeUpdate(sql, topic.getContent(),topic.getUid(),topic.getTopicid());
	}
	
	/**
	 * ��ѯ���������лظ�
	 */
	public List<Reply> findReply(Topic topic) {
		StringBuffer sql=new StringBuffer();
		sql.append("select replyid,content,date_format(publishtime,'%Y-%m-%d %H:%i:%s') as publishtime,\r\n" + 
				"    date_format(modifytime,'%Y-%m-%d %H:%i:%s') as modifytime, tbl_reply.uid, topicid,\r\n" + 
				"    uname,\r\n" + 
				"    head,\r\n" + 
				"    agreecount,\r\n"+
				"    date_format(regtime,'%Y-%m-%d %H:%i:%i') as  regtime,agreecount\r\n" + 
				"from tbl_reply\r\n" + 
				"inner join tbl_user\r\n" + 
				"on tbl_reply.uid=tbl_user.uid\r\n" + 
				"where topicid=?\r\n" + 
				"order by modifytime desc limit ?,?\r\n");
		int start=(topic.getPages()-1)*topic.getPagesize();
		List<Map<String,Object>> executeQuery = db.executeQuery(sql.toString(), topic.getTopicid(),start,topic.getPagesize());
		return Myutil.ListMapToJavaBean(executeQuery, Reply.class);
	}
	
	/**
	 * �鵱ǰ�����µĻظ�����
	 */
	public int findReplyCount(  Topic topic ) {
		String sql="select count(*) as total from tbl_reply where topicid=?";
		List<Map<String,Object>> executeQuery = db.executeQuery(sql, topic.getTopicid());
		return  Integer.parseInt( executeQuery.get(0).get("total").toString() );
	}
	
	/**
	 * ɾ������
	 */
	public int delReply(Reply reply) {
		String sql="delete from tbl_reply where replyid=?";
		return db.executeUpdate(sql, reply.getReplyid());
	}
	
	/**
	 * �鵱ǰ�������������
	 */
	public int findTopicCount( Topic topic ) {
		String sql="select count(*) as total from tbl_reply where topicid=?";
		List<Map<String, Object>> map = db.executeQuery(sql, topic.getTopicid());
		return  Integer.parseInt( map.get(0).get("total").toString() );
	}
	
	
	public PageBean<Reply> findPageBean(  Topic topic  ) {
		PageBean<Reply> pb=new PageBean<>();
		
		//�ð���������ӵ���Ϣ
		List<Reply> list = findReply( topic);
		
		//���ӵ��ܼ�¼��
		int total=findTopicCount( topic);
		
		
		pb.setList(list);
		
		pb.setTotal((long)total);
		
		//��ҳ��
		int totalpages=total%topic.getPagesize()==0?total/topic.getPagesize():(total/topic.getPagesize()+1);
		if(totalpages==0) {
			totalpages=1;
		}
		pb.setTotalPage((long)totalpages);


		return pb;
		
	}
	
	
	/**
	 * ��������һ
	 * @param uid
	 * @param replyId
	 * @return
	 */
	public int agree(String topicid,String replyId) {
		String sql="update tbl_reply set agreecount=agreecount+1 where topicid=? and replyid=?";
		return db.executeUpdate(sql, topicid,replyId);
	}
	
	/**
	 * ��ѯ�����ĵ�����
	 * @param replyId
	 * @return
	 */
	public List<Map<String,Object>> selectAgreeCount(String replyId) {
		String sql="select agreecount from tbl_reply where replyid=?";
		return db.executeQuery(sql, replyId);
	}
	
	/**
	  *   ȡ������,��Ŀ��һ
	 * @param topicId
	 * @param replyId
	 * @return
	 */
	public Integer disagree(String topicId, String replyId) {
		String sql="update tbl_reply set agreecount=agreecount-1 where topicid=? and replyid=?";
		return db.executeUpdate(sql, topicId,replyId);
	}
}
