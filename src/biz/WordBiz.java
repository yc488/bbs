package biz;

import dao.WordDao;

public class WordBiz {

	WordDao wd=new WordDao();
	/**
	 * 添加敏感词
	 * @param word
	 * @return
	 * @throws BizException 
	 */
	public void add(String word) throws BizException {
		if(word==null|| word.isEmpty()) {
			throw new BizException("敏感词不能为空");
		}
		int add = wd.add(word);
		if(add<0) {
			throw new BizException("添加失败");
		}
		
	}
	
	/**
	 * 修改敏感词
	 * @param sname
	 * @param sid 
	 * @throws BizException 
	 */
	public void updateWord(String sname, int sid) throws BizException {
		if(sname==null || sname.isEmpty()) {
			throw new BizException("敏感词不能为空");
		}
		wd.updateWord(sname,sid);
	}
	
}
