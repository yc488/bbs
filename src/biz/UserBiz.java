package biz;

import java.util.List;
import java.util.Map;

import bean.User;
import bean.UserInfo;
import dao.UserDao;

public class UserBiz {
	
	UserDao ud=new UserDao();
	/**
	 * 用户登录
	 * @param user
	 * @param code
	 * @param valCode
	 * @return
	 * @throws BizException
	 */
	public User userLogin(User user,String code,String valCode) throws BizException {
		
		
		if(!code.equalsIgnoreCase(valCode)) {
			throw new BizException("验证码错误");
		}else if(user.getUname()==null || user.getUname().isEmpty() ) {
			throw new BizException("用户名或密码错误");
		}else if(user.getUpass()==null || user.getUpass().isEmpty() ){
			throw new BizException("用户名或密码错误");
		}
		
		List<User> userLogin = ud.userLogin(user);
		if(userLogin.size()==0){
			throw new BizException("用户名或密码错误");
		}else {
			return userLogin.get(0);
		}
		
	}
	
	/**
	 * 补全用户信息
	 * @param uid
	 * @return
	 * @throws BizException
	 */
	public UserInfo selectAll(int uid) throws BizException {

		UserInfo selectAll = ud.selectAll(uid);
		return selectAll;
	}
	
	/**
	 * 用户注册
	 * @param user
	 * @param code
	 * @param regcode
	 * @throws BizException
	 */
	public void reg(User user,String code,String regcode) throws BizException {
		
		if(!code.equalsIgnoreCase(regcode)) {
			throw new BizException("验证码错误");
		}else if(user.getUname()==null || user.getUname().isEmpty() ) {
			throw new BizException("用户名或密码错误");
		}else if(user.getUpass()==null || user.getUpass().isEmpty() ){
			throw new BizException("用户名或密码错误");
		}
		
		
		int regUser = ud.regUser(user);
		if(regUser<0) {
			throw new BizException("注册失败");
		}
		List<Map<String,Object>> list=ud.getBasicInfo(user.getUname(), user.getUpass());
		Integer uid=(Integer) list.get(0).get("uid");
		
		user.setUid(uid);
		ud.addExpendInfo(user);
		
	}

	/**
	 * 判断用户名是否存在
	 * @param user
	 * @return
	 * @throws BizException
	 */
	public int isUserName(User user) throws BizException {
		if(user.getUname()==null) {
			throw new BizException("用户名不能为空");
		}

		List<Map<String,Object>> userName = ud.isUserName(user);
		
		if(userName.size()>0) {
			return 1;
		}else {
			return 0;
		}
	}

	/**
	 * 查询所有用户信息
	 * @return
	 */
	public List<Map<String, Object>> findUSer() {
		
		return ud.findUSer();
	}

	/**
	 * 查询所有用户的扩展信息
	 * @return
	 */
	public List<Map<String, Object>> findUserInfo() {
		
		return ud.findUserInfo();
	}

	/**
	 * 用户修改密码
	 * @param user.getUid()
	 * @param newpass
	 * @param upass 
	 * @return
	 * @throws BizException 
	 */
	public void pwdchange(User user, String newpass, String upass) throws BizException {
		if(upass==null) {
			throw new BizException("新密码不能为空");
		}else if(newpass==null) {
			throw new BizException("原密码不能为空");
		}
		else if(!upass.equals(user.getUpass())) {
			throw new BizException("原密码错误，请重新输入");
		}
		
		Integer result = ud.pwdchange(user.getUid(),newpass);
		if( result < 0  ){//修改失败
			throw new BizException("修改失败");
		}
	}
	
	/**
	 * 重置密码
	 * @param upass
	 * @param uname
	 * @param code22 
	 * @return
	 * @throws BizException 
	 */
	public void resetpwd(String upass, String upass1,String email, String code1, String code2 ) throws BizException {
		if( !code1.equals(code2) ) {//两次输入的验证码相同，可以进行修改密码
			throw new BizException("验证码错误");
		}else if(upass==null) {
			throw new BizException("新密码不能为空");
		}else if(!upass1.equals(upass1)) {
			throw new BizException("两次密码不一致");
		}
		int resetpwd = ud.resetpwd(upass, email);
		if(resetpwd<0) {
			throw new BizException("密码重置失败。");
		}

	}

	/**
	 * 邮箱是否存在
	 * @param email
	 * @throws BizException 
	 */
	public int isEmail(String email) throws BizException {
		if(email==null) {
			throw new BizException("邮箱不能为空");
		}
		
		int email2 = ud.isEmail(email);
		
		return email2;
	}

	
	
}
