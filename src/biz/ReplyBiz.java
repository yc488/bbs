package biz;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import bean.PageBean;
import bean.Reply;
import bean.Topic;
import bean.UserInfo;
import dao.ReplyDao;
import dao.StopDao;
import dao.UserDao;
import utils.Myutil;


public class ReplyBiz {

	ReplyDao rd=new ReplyDao();
	UserDao ud=new UserDao();
	StopDao sd=new StopDao();
	private  UserInfo info;
	private Boolean flag = true;
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public UserInfo getUserinfo() {
		return info;
	}

	public void setUserinfo(UserInfo info) {
		this.info = info;
	}
	
	public Boolean getFlag() {
		return flag;
	}
	public void setFlag(Boolean flag) {
		this.flag = flag;
	}
	/**
	 * 回帖分页
	 * @param topic
	 * @return
	 */
	public PageBean<Reply> findPageBean(Topic topic) {
		return rd.findPageBean(topic);
		
	}
	
	/**
	 * 回帖
	 * @param topic
	 * @param email
	 * @return
	 * @throws BizException 
	 */
	public void answer(Topic topic, String email) throws BizException {
		UserInfo userinfo=ud.selectAll(topic.getUid());
		System.out.println("调用answer方法时的userinfo"+userinfo);
		if(userinfo.getEndtime()!=null&&userinfo.getStarttime()!=null) {
			if(userinfo.getEndtime().before(new Timestamp(System.currentTimeMillis()))) {
				ud.releasePost(topic.getUid());
				userinfo.setStarttime(null);
				userinfo.setEndtime(null);
				userinfo.setTime(0);
			}
			if(userinfo.getEndtime()!=null&&userinfo.getEndtime().after(new Timestamp(System.currentTimeMillis()))) {
				System.out.println("您已被禁言");
				throw new BizException("您已被禁言,禁言结束时间为"+sdf.format(new Date(userinfo.getEndtime().getTime())));		
			}
		}
		this.setUserinfo(userinfo);
		System.out.println("传递后的"+this.getUserinfo());
		//被禁言的时候不能发帖

		List<Map<String,Object>> list=sd.query();
		//判断过滤前后的内容是否一致,如不,则增加用户的次数
		String beforeContent=topic.getContent();
		String afterContent=beforeContent;
		for(int i=0;i<list.size();i++) {
			afterContent=afterContent.replace((String)list.get(i).get("sname"), "**");
		}
		if(!beforeContent.equals(afterContent)) {
			ud.addTime(topic.getUid());
			info.setTime(info.getTime()+1);
			flag=false;
		}else {
			flag=true;
		}
		//每发三次脏话禁言一天
		if(info.getTime()==3) {
			ud.stopPost(userinfo.getUid());
			new Thread() {
				public void run() {
					Myutil.sendemail(email, new Timestamp(System.currentTimeMillis()+24*60*60*1000));
				}; {};
			}.start();
		}
		
		//把内容设置成过滤之后的内容
		topic.setContent(afterContent);
		int answer = rd.answer(topic, email);
		if(answer<0) {
			throw new BizException("服务器繁忙");			
		}
	}

	/**
	 * 删除回帖
	 * @param reply
	 * @return
	 * @throws BizException 
	 */
	public void delReply(Reply reply) throws BizException {
		if(reply.getReplyid()==null) {
			throw new BizException("回帖不存在,删除失败");	
		}
		rd.delReply(reply);
		
	}
	
	/**
	 * 点赞数加一
	 * @param uid
	 * @param replyId
	 * @return
	 */
	public int agree(String topicId, String replyId) {
		return rd.agree(topicId, replyId);
	}
	
	/**
	 * 根据回帖Id查询点赞的数目
	 * @param replyId
	 * @return
	 */
	public int selectAgreeCount(String replyId) {
		List<Map<String,Object>> list=rd.selectAgreeCount(replyId);
		return Integer.valueOf(list.get(0).get("agreecount").toString());
	}
	
	/**
	 * 赞的数目减一
	 * @param topicId
	 * @param replyId
	 * @return
	 */
	public Integer disagree(String topicId, String replyId) {
		return rd.disagree(topicId, replyId);
	}
}
