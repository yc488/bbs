package biz;

import dao.WordDao;

public class WordBiz {

	WordDao wd=new WordDao();
	/**
	 * ������д�
	 * @param word
	 * @return
	 * @throws BizException 
	 */
	public void add(String word) throws BizException {
		if(word==null|| word.isEmpty()) {
			throw new BizException("���дʲ���Ϊ��");
		}
		int add = wd.add(word);
		if(add<0) {
			throw new BizException("���ʧ��");
		}
		
	}
	
	/**
	 * �޸����д�
	 * @param sname
	 * @param sid 
	 * @throws BizException 
	 */
	public void updateWord(String sname, int sid) throws BizException {
		if(sname==null || sname.isEmpty()) {
			throw new BizException("���дʲ���Ϊ��");
		}
		wd.updateWord(sname,sid);
	}
	
}
