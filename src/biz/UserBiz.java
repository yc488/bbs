package biz;

import java.util.List;
import java.util.Map;

import bean.User;
import bean.UserInfo;
import dao.UserDao;

public class UserBiz {
	
	UserDao ud=new UserDao();
	/**
	 * �û���¼
	 * @param user
	 * @param code
	 * @param valCode
	 * @return
	 * @throws BizException
	 */
	public User userLogin(User user,String code,String valCode) throws BizException {
		
		
		if(!code.equalsIgnoreCase(valCode)) {
			throw new BizException("��֤�����");
		}else if(user.getUname()==null || user.getUname().isEmpty() ) {
			throw new BizException("�û������������");
		}else if(user.getUpass()==null || user.getUpass().isEmpty() ){
			throw new BizException("�û������������");
		}
		
		List<User> userLogin = ud.userLogin(user);
		if(userLogin.size()==0){
			throw new BizException("�û������������");
		}else {
			return userLogin.get(0);
		}
		
	}
	
	/**
	 * ��ȫ�û���Ϣ
	 * @param uid
	 * @return
	 * @throws BizException
	 */
	public UserInfo selectAll(int uid) throws BizException {

		UserInfo selectAll = ud.selectAll(uid);
		return selectAll;
	}
	
	/**
	 * �û�ע��
	 * @param user
	 * @param code
	 * @param regcode
	 * @throws BizException
	 */
	public void reg(User user,String code,String regcode) throws BizException {
		
		if(!code.equalsIgnoreCase(regcode)) {
			throw new BizException("��֤�����");
		}else if(user.getUname()==null || user.getUname().isEmpty() ) {
			throw new BizException("�û������������");
		}else if(user.getUpass()==null || user.getUpass().isEmpty() ){
			throw new BizException("�û������������");
		}
		
		
		int regUser = ud.regUser(user);
		if(regUser<0) {
			throw new BizException("ע��ʧ��");
		}
		List<Map<String,Object>> list=ud.getBasicInfo(user.getUname(), user.getUpass());
		Integer uid=(Integer) list.get(0).get("uid");
		
		user.setUid(uid);
		ud.addExpendInfo(user);
		
	}

	/**
	 * �ж��û����Ƿ����
	 * @param user
	 * @return
	 * @throws BizException
	 */
	public int isUserName(User user) throws BizException {
		if(user.getUname()==null) {
			throw new BizException("�û�������Ϊ��");
		}

		List<Map<String,Object>> userName = ud.isUserName(user);
		
		if(userName.size()>0) {
			return 1;
		}else {
			return 0;
		}
	}

	/**
	 * ��ѯ�����û���Ϣ
	 * @return
	 */
	public List<Map<String, Object>> findUSer() {
		
		return ud.findUSer();
	}

	/**
	 * ��ѯ�����û�����չ��Ϣ
	 * @return
	 */
	public List<Map<String, Object>> findUserInfo() {
		
		return ud.findUserInfo();
	}

	/**
	 * �û��޸�����
	 * @param user.getUid()
	 * @param newpass
	 * @param upass 
	 * @return
	 * @throws BizException 
	 */
	public void pwdchange(User user, String newpass, String upass) throws BizException {
		if(upass==null) {
			throw new BizException("�����벻��Ϊ��");
		}else if(newpass==null) {
			throw new BizException("ԭ���벻��Ϊ��");
		}
		else if(!upass.equals(user.getUpass())) {
			throw new BizException("ԭ�����������������");
		}
		
		Integer result = ud.pwdchange(user.getUid(),newpass);
		if( result < 0  ){//�޸�ʧ��
			throw new BizException("�޸�ʧ��");
		}
	}
	
	/**
	 * ��������
	 * @param upass
	 * @param uname
	 * @param code22 
	 * @return
	 * @throws BizException 
	 */
	public void resetpwd(String upass, String upass1,String email, String code1, String code2 ) throws BizException {
		if( !code1.equals(code2) ) {//�����������֤����ͬ�����Խ����޸�����
			throw new BizException("��֤�����");
		}else if(upass==null) {
			throw new BizException("�����벻��Ϊ��");
		}else if(!upass1.equals(upass1)) {
			throw new BizException("�������벻һ��");
		}
		int resetpwd = ud.resetpwd(upass, email);
		if(resetpwd<0) {
			throw new BizException("��������ʧ�ܡ�");
		}

	}

	/**
	 * �����Ƿ����
	 * @param email
	 * @throws BizException 
	 */
	public int isEmail(String email) throws BizException {
		if(email==null) {
			throw new BizException("���䲻��Ϊ��");
		}
		
		int email2 = ud.isEmail(email);
		
		return email2;
	}

	
	
}
