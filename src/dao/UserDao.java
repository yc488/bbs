package dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.User;
import bean.UserInfo;
import utils.JDBCHelp;
import utils.Myutil;

public class UserDao {
	private JDBCHelp db=new JDBCHelp();
	
	/**
	 * �ж������Ƿ񱻽���
	 * @param uid
	 * @return
	 */
	public boolean isPost(Integer uid) {
		String sql="select * from tbl_userinfo where uid=?";
		List<Map<String,Object>> list=db.executeQuery(sql, uid);
		if(list.get(0).get("canpost").equals("false")) {
			return false;
		}
		return true;
	}
	
	/**
	 * ���ӷ��໰�Ĵ���
	 * @param user
	 */
	public void addTime(Integer uid) {
		String sql="update tbl_userinfo set time=time+1 where uid=?";
		db.executeUpdate(sql, uid);
	}
	
	/**
	 * ��ʼ����
	 * @param user
	 */
	public void stopPost(Integer uid) {
		String sql="update tbl_userinfo set starttime=?,endtime=? where uid=?";
		db.executeUpdate(sql,new Timestamp(System.currentTimeMillis()),
				new Timestamp(System.currentTimeMillis()+24*60*60*1000),uid);
	}
	
	/**
	 * �������
	 * @param user
	 */
	public int releasePost(Integer uid) {
		String sql="update tbl_userinfo set time=0,starttime=null,endtime=null where uid=?";
		return db.executeUpdate(sql, uid);
	}
	
	/**
	 * ��ѯ��ǰ�û������з�����Ϣ
	 * @param uid
	 * @return
	 */
	public UserInfo selectAll(Integer uid) {
		String sql="select * from tbl_userinfo where uid=?";
		List<Map<String,Object>> list=db.executeQuery(sql, uid);
		return Myutil.ListMapToJavaBean(list, UserInfo.class).get(0) ;
	}
	
	public void releaseAll() {
		String sql="UPDATE tbl_userinfo SET starttime=NULL,endtime=NULL WHERE endtime<NOW()";
		db.executeUpdate(sql);
	}
	
	
	/**
	 * ��ѯ�û����¼
	 * @param user
	 * @return
	 */
	public List<User> userLogin(User user) {
		String sql="select uid,uname,upass,head,gender,date_format(regtime,'%Y-%m-%d %H:%i:%s') as regtime,email from tbl_user where uname=? and upass=?";
		List<Object> params =new ArrayList<>();
		params.add(user.getUname());
		params.add(user.getUpass());
		List<Map<String,Object>> executeQuery = db.executeQuery(sql, params);
		return (List<User>) Myutil.ListMapToJavaBean(executeQuery, User.class);
	}
	
	/**
	 * ע���û�
	 * @param user
	 * @return
	 */
	public int regUser(User user) {
		String sql="insert into tbl_user values(null,?,?,?,sysdate(),?,?)";
		List<Object> params =new ArrayList<>();
		params.add(user.getUname());
		params.add(user.getUpass());
		params.add(user.getHead());
		params.add(user.getGender());
		params.add(user.getEmail());
		int executeUpdate = db.executeUpdate(sql, params);
		return executeUpdate;
	}
	
	/**
	 * ��ȡ��ǰע���û���uid
	 * @param uname
	 * @param upass
	 * @return
	 */
	public List<Map<String,Object>> getBasicInfo(String uname,String upass){
		String sql="select uid from tbl_user where uname=? and upass=?";
		return db.executeQuery(sql, uname,upass);
	}
	
	/**
	 * ��ȫע���û�����Ϣ
	 * @param user
	 */
	public void addExpendInfo(User user) {
		String sql="insert into tbl_userinfo values(?,0,null,null)";
		db.executeUpdate(sql, user.getUid());
	}
	
	/**
	 * ����ע����û����Ƿ����
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> isUserName(User user) {
		String sql="select * from tbl_user where uname=?";
		List<Map<String,Object>> executeQuery = db.executeQuery(sql, user.getUname());
		return executeQuery;
	}
	
	/**
	 * ��ѯ�����û�
	 * @return
	 */
	public List<Map<String, Object>> findUSer() {
		String sql="select uid,uname,head,date_format(regtime,'%Y-%m-%d %H:%i:%s') as regtime,gender,email from tbl_user";
		return db.executeQuery(sql);
	}
	
	/**
	 * ��ѯ�����û�����չ��Ϣ
	 * @return
	 */
	public List<Map<String, Object>> findUserInfo() {
		String sql="select a.*,b.uname from tbl_userinfo a ,tbl_user b where a.uid=b.uid";
		return db.executeQuery(sql);
	}
	
	/**
	 * �û��޸�����
	 * @param uid
	 * @param upass
	 * @return
	 */
	public Integer pwdchange(Integer uid ,String upass) {
		String sql = "UPDATE `bbs`.`tbl_user` SET `upass` = ? WHERE `uid` = ?; ";
		return db.executeUpdate(sql, upass,uid);		
	}

	/**
	 * ��������
	 * @param upass
	 * @param uname
	 * @return
	 */
	public int resetpwd(String upass,String email) {
		String sql = "UPDATE tbl_user SET upass = ? WHERE email = ?";
		return db.executeUpdate(sql, upass,email);
	}
	

	/**
	 * ��ѯ�����Ƿ����
	 * @return
	 */
	public int isEmail(String email) {
		String sql="select uid from tbl_user where email=?";
		List<Map<String,Object>> executeQuery = db.executeQuery(sql,email);
		if(executeQuery.size()>0) {
			return 1;
		}else {
			return 0;
		}
	}
}

