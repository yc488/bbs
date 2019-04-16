package biz;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import bean.PageBean;
import bean.Topic;
import bean.User;
import bean.UserInfo;
import dao.StopDao;
import dao.TopicDao;
import dao.UserDao;
import utils.Myutil;

public class TopicBiz {
	TopicDao td=new TopicDao();
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
	 * 分页
	 * @param topic
	 * @return
	 * @throws BizException 
	 */
	public PageBean<Topic> findPageBean(Topic topic) throws BizException {

		PageBean<Topic> findPageBean = td.findPageBean(topic);
		return findPageBean;
	}


	/**
	 * 发帖
	 * @param topic
	 * @param email
	 * @return
	 * @throws BizException
	 */
	public void post(Topic topic, String email) throws BizException {
		UserInfo userinfo=ud.selectAll(topic.getUid());
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
		System.out.println("执行post方法"+userinfo);

		List<Map<String,Object>> list=sd.query();
		//判断过滤前后的内容是否一致,如不,则增加用户的次数
		String beforeTitle=topic.getTitle();
		String beforeContent=topic.getContent();
		String afterTitle=beforeTitle;
		String afterContent=beforeContent;
		for(int i=0;i<list.size();i++) {
			afterTitle=afterTitle.replace((String)list.get(i).get("sname"), "**");
			afterContent=afterContent.replace((String)list.get(i).get("sname"), "**");
		}
		if(!beforeTitle.equals(afterTitle)||!beforeContent.equals(afterContent)) {
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
		topic.setTitle(afterTitle);
		topic.setContent(afterContent);
		
		int post = td.post(topic, email);
		
		if(post<0) {
			throw new BizException("服务器繁忙");			
		}
	}
	
	/**
	 *查询单个帖子的详情
	 * @param topic
	 * @return
	 * @throws BizException 
	 */
	public Topic topicdetail(Topic topic) throws BizException {
		Topic topicdetail = td.topicdetail(topic);
		return topicdetail;
	}
	
	/**
	 * 删除帖子
	 * @param topic
	 * @return
	 * @throws BizException 
	 */
	public void delTopic(Topic topic) throws BizException {
		if(topic.getTopicid()==null) {
			throw new BizException("帖子不存在，删除失败");
		}
		td.delTopic(topic);
		
	}
	
	/**
	 * 查询个人的帖子
	 * @param topic
	 * @return
	 */
	public List<Topic> myTopic(Topic topic) throws BizException {
		List<Topic> myTopic = td.personTopTopic(topic);
		return myTopic;
	}
	/**
	 * 每个板块前10的热帖
	 * @param topic
	 * @return
	 * @throws BizException 
	 */
	public List<Topic> findHostTopic(Topic topic) throws BizException {
		List<Topic> findHostTopic = td.findHostTopic(topic);
		
		return findHostTopic;
	}
	/**
	 * 论坛热帖
	 * @return
	 * @throws BizException 
	 */
	public List<Topic> findAllHostTopic() throws BizException {
		List<Topic> findAllHostTopic = td.findAllHostTopic();
		return findAllHostTopic;
	}
	
	/**
	 * 风云人物
	 * @return
	 * @throws BizException 
	 */
	public List<User> personTop() throws BizException {
		List<User> personTop = td.personTop();
		return personTop;
	}
	
	
	/**
	 * 风云人物的所有帖子
	 * @return
	 * @throws BizException 
	 */
	public List<Topic> personTopTopic(Topic topic) throws BizException {
		List<Topic> personTopTopic = td.personTopTopic(topic);
		
		return personTopTopic;
	}
	
	/**
	 * 查找帖子
	 * @param topicname
	 * @return 
	 * @throws BizException 
	 */
	public List<Topic> searchTopic(String topicname) throws BizException {
		if(topicname==null || topicname.isEmpty()) {
			throw new BizException("帖名不能为空");
		}
		
		return  td.serachTopic(topicname);
		
	}
	
	
	
	
}
