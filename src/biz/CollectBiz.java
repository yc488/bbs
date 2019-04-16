package biz;

import java.util.List;

import bean.User;
import bean.collect;
import dao.CollectDao;

public class CollectBiz {

	CollectDao cd=new CollectDao();
	
	/**
	 * �����ղ�
	 * @param col
	 * @return
	 * @throws BizException 
	 */
	public void addCollect(collect col) throws BizException {
		if(col.getUid()<0) {
			throw new BizException("�û�������");
		}else if(col.getBoardid()<0) {
			throw new BizException("��鲻����");
		}else if(col.getTopicid()<0) {
			throw new BizException("���Ӳ�����");
		}
		
		int addCollect = cd.addCollect(col);
		System.out.println(addCollect);
		if(addCollect<0) {
			throw new BizException("�ղ�ʧ��");
		}else if(addCollect==0) {
			throw new BizException("�����ղظ�������鿴�ҵ��ղ�");
		}
		
	}

	/**
	 * �鿴�ҵ��ղ�
	 * @param col
	 * @return
	 * @throws BizException 
	 */
	public List<collect> findMyCollect(collect col) throws BizException {
		List<collect> findMyCollect = cd.findMyCollect(col);
		
		return findMyCollect;
	}

	/**
	 * ȡ���ղ�
	 * @param col
	 * @return
	 * @throws BizException 
	 */
	public void cancleCollect(collect col) throws BizException {
		if(col.getCid()<0) {
			throw new BizException("����δ���ղ�");
		}
		
		cd.cancleCollect(col);
		
		
	}

	/**
	 * �����ǰ�û��ղ����ӵ�����
	 * @param user 
	 * @return
	 * @throws BizException 
	 */
	public int findAllCollect(User user) throws BizException {
		if(user.getUid()<0) {
			throw new BizException("�û�������");
		}
		
		int findAllCollect = cd.findAllCollect(user);
		
		
		return findAllCollect;
	}

}
