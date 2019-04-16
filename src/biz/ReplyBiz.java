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
	 * ������ҳ
	 * @param topic
	 * @return
	 */
	public PageBean<Reply> findPageBean(Topic topic) {
		return rd.findPageBean(topic);
		
	}
	
	/**
	 * ����
	 * @param topic
	 * @param email
	 * @return
	 * @throws BizException 
	 */
	public void answer(Topic topic, String email) throws BizException {
		UserInfo userinfo=ud.selectAll(topic.getUid());
		System.out.println("����answer����ʱ��userinfo"+userinfo);
		if(userinfo.getEndtime()!=null&&userinfo.getStarttime()!=null) {
			if(userinfo.getEndtime().before(new Timestamp(System.currentTimeMillis()))) {
				ud.releasePost(topic.getUid());
				userinfo.setStarttime(null);
				userinfo.setEndtime(null);
				userinfo.setTime(0);
			}
			if(userinfo.getEndtime()!=null&&userinfo.getEndtime().after(new Timestamp(System.currentTimeMillis()))) {
				System.out.println("���ѱ�����");
				throw new BizException("���ѱ�����,���Խ���ʱ��Ϊ"+sdf.format(new Date(userinfo.getEndtime().getTime())));		
			}
		}
		this.setUserinfo(userinfo);
		System.out.println("���ݺ��"+this.getUserinfo());
		//�����Ե�ʱ���ܷ���

		List<Map<String,Object>> list=sd.query();
		//�жϹ���ǰ��������Ƿ�һ��,�粻,�������û��Ĵ���
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
		//ÿ�������໰����һ��
		if(info.getTime()==3) {
			ud.stopPost(userinfo.getUid());
			new Thread() {
				public void run() {
					Myutil.sendemail(email, new Timestamp(System.currentTimeMillis()+24*60*60*1000));
				}; {};
			}.start();
		}
		
		//���������óɹ���֮�������
		topic.setContent(afterContent);
		int answer = rd.answer(topic, email);
		if(answer<0) {
			throw new BizException("��������æ");			
		}
	}

	/**
	 * ɾ������
	 * @param reply
	 * @return
	 * @throws BizException 
	 */
	public void delReply(Reply reply) throws BizException {
		if(reply.getReplyid()==null) {
			throw new BizException("����������,ɾ��ʧ��");	
		}
		rd.delReply(reply);
		
	}
	
	/**
	 * ��������һ
	 * @param uid
	 * @param replyId
	 * @return
	 */
	public int agree(String topicId, String replyId) {
		return rd.agree(topicId, replyId);
	}
	
	/**
	 * ���ݻ���Id��ѯ���޵���Ŀ
	 * @param replyId
	 * @return
	 */
	public int selectAgreeCount(String replyId) {
		List<Map<String,Object>> list=rd.selectAgreeCount(replyId);
		return Integer.valueOf(list.get(0).get("agreecount").toString());
	}
	
	/**
	 * �޵���Ŀ��һ
	 * @param topicId
	 * @param replyId
	 * @return
	 */
	public Integer disagree(String topicId, String replyId) {
		return rd.disagree(topicId, replyId);
	}
}
