package dao;

import utils.JDBCHelp;

public class WordDao {
	
	
	private JDBCHelp db=new JDBCHelp();
	/**
	 * 添加敏感词
	 * @param word
	 * @return
	 */
	public int add(String word) {
		String sql="insert into tbl_stop values(null,?)";
		return db.executeUpdate(sql, word);
	}
	
	/**
	 * 修改敏感词
	 * @param sname
	 * @param sid 
	 */
	public void updateWord(String sname, int sid) {
		String sql="update  tbl_stop set sname=? where sid=?";
		db.executeUpdate(sql, sname,sid);
	}
}
