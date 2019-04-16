package biz;

import java.util.List;

import bean.User;
import bean.collect;
import dao.CollectDao;

public class CollectBiz {

	CollectDao cd=new CollectDao();
	
	/**
	 * 加入收藏
	 * @param col
	 * @return
	 * @throws BizException 
	 */
	public void addCollect(collect col) throws BizException {
		if(col.getUid()<0) {
			throw new BizException("用户不存在");
		}else if(col.getBoardid()<0) {
			throw new BizException("板块不存在");
		}else if(col.getTopicid()<0) {
			throw new BizException("帖子不存在");
		}
		
		int addCollect = cd.addCollect(col);
		System.out.println(addCollect);
		if(addCollect<0) {
			throw new BizException("收藏失败");
		}else if(addCollect==0) {
			throw new BizException("你已收藏该帖，请查看我的收藏");
		}
		
	}

	/**
	 * 查看我的收藏
	 * @param col
	 * @return
	 * @throws BizException 
	 */
	public List<collect> findMyCollect(collect col) throws BizException {
		List<collect> findMyCollect = cd.findMyCollect(col);
		
		return findMyCollect;
	}

	/**
	 * 取消收藏
	 * @param col
	 * @return
	 * @throws BizException 
	 */
	public void cancleCollect(collect col) throws BizException {
		if(col.getCid()<0) {
			throw new BizException("帖子未被收藏");
		}
		
		cd.cancleCollect(col);
		
		
	}

	/**
	 * 查出当前用户收藏帖子的总数
	 * @param user 
	 * @return
	 * @throws BizException 
	 */
	public int findAllCollect(User user) throws BizException {
		if(user.getUid()<0) {
			throw new BizException("用户不存在");
		}
		
		int findAllCollect = cd.findAllCollect(user);
		
		
		return findAllCollect;
	}

}
