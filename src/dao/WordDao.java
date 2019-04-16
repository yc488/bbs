package dao;

import utils.JDBCHelp;

public class WordDao {
	
	
	private JDBCHelp db=new JDBCHelp();
	/**
	 * ������д�
	 * @param word
	 * @return
	 */
	public int add(String word) {
		String sql="insert into tbl_stop values(null,?)";
		return db.executeUpdate(sql, word);
	}
	
	/**
	 * �޸����д�
	 * @param sname
	 * @param sid 
	 */
	public void updateWord(String sname, int sid) {
		String sql="update  tbl_stop set sname=? where sid=?";
		db.executeUpdate(sql, sname,sid);
	}
}
