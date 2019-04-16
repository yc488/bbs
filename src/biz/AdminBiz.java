package biz;

import java.util.List;
import java.util.Map;

import bean.TblAdmin;
import dao.AdminDao;
import dao.StopDao;
import dao.UserDao;

public class AdminBiz {
	
	private UserDao ud=new UserDao();
	private StopDao sd=new StopDao();
	private AdminDao ad=new AdminDao();
	/**
	 * �����û���uid���������
	 * @param uid
	 * @return
	 * @throws BizException 
	 */
	public void releaseById(Integer uid) throws BizException {
		if(uid<0) {
			throw new BizException("�û�������");
		}
		ud.releasePost(uid);
		
	}

	/**
	 * ����Ա��¼
	 * @param admin
	 * @return
	 * @throws BizException 
	 */
	public TblAdmin login(TblAdmin admin) throws BizException {
		
		if(admin.getRaname()==null || admin.getRaname().isEmpty() ) {
			throw new BizException("�û������������");
		}else if(admin.getRapwd()==null || admin.getRapwd().isEmpty() ){
			throw new BizException("�û������������");
		}
		
		
		TblAdmin login = ad.login(admin);
		if(login==null) {
			throw new BizException("�û������������");
		}
		return login;
	}
	
	/**
	 * ��ѯ���е����д�
	 * @return
	 */
	public List<Map<String, Object>> findAllWords() {		
		return sd.query();
	}
	
	/**
	 * ɾ�����д�
	 * @param sid
	 * @return
	 * @throws BizException 
	 */
	public void delWordById(String sid) throws BizException {
		if(sid==null) {
			throw new BizException("���дʲ�����");
		}
		
		sd.delWordById(sid);
	}
}
