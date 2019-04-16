package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.Board;
import utils.JDBCHelp;
import utils.Myutil;

public class BoardDao {
	
	private JDBCHelp db=new JDBCHelp();
	

	
	/**
	 * 后台显示的主板块的信息
	 * @return
	 */
	public List<Board> bigBoardList() {
		String sql="SELECT\n" + 
				"  a.boardid,\n" + 
				"  boardname,\n" + 
				"  b.sonTotal\n" + 
				"FROM\n" + 
				"  tbl_board a\n" + 
				"  LEFT JOIN\n" + 
				"    (SELECT\n" + 
				"      parentid,\n" + 
				"      COUNT(boardname) AS sonTotal\n" + 
				"    FROM\n" + 
				"      tbl_board\n" + 
				"    GROUP BY parentid\n" + 
				"    HAVING parentid > 0) b\n" + 
				"    ON a.boardid = b.parentid\n" + 
				"WHERE a.parentid = 0";
		
		List<Map<String,Object>> executeQuery = db.executeQuery(sql);
		return (List<Board>) Myutil.ListMapToJavaBean(executeQuery, Board.class);
	}
	
	/**
	 * 修改主板块信息
	 */
	public int updateBigBoard(Board board) {
		String sql="update tbl_board set boardname=? where boardid=?";
		
		return db.executeUpdate(sql,board.getBoardname(),board.getBoardid());
		
	}
	
	/**
	 * 增加主板块
	 */
	public int addBigBoard(Board board) {
		String sql="insert into tbl_board values(null,?,0)";
		
		return db.executeUpdate(sql,board.getBoardname());
		
	}
	
	/**
	 * 删除主板块
	 * @param board
	 * @return
	 */
	public int delBigBoard(Board board) {
		String sql="delete from tbl_board where boardid=?";
		return db.executeUpdate(sql, board.getBoardid());
		
	}
	
	/**
	 * 取出所有板块信息
	 */
	
	public Map<Integer ,List<Board>> findAllBoard() {
		StringBuffer sql = new StringBuffer();
	
		sql.append("select * from (select a.boardid,boardname,parentid, total AS topicsum, topicid AS recenttopicid,title AS recenttopictitle,date_format(modifytime,'%Y-%m-%d %H:%I:%S') as recenttopicmodifytime,uname AS recenttopicusername");
		sql.append(" from (select tbl_board.boardid,boardname,parentid , count( topicid ) as total from tbl_board left join tbl_topic on tbl_board.boardid=tbl_topic.boardid group by tbl_board.boardid,boardname,parentid ) a ");
		sql.append(" left join (select topicid,title,a.modifytime,uname,a.boardid from (	select topicid, title, modifytime, uname, boardid from tbl_topic left join tbl_user on tbl_topic.uid=tbl_user.uid ) a,");
		sql.append(" ( select boardid,max(modifytime) as modifytime from tbl_topic group by boardid ) b 	where  a.boardid=b.boardid and a.modifytime=b.modifytime ) b");
		sql.append(" on a.boardid=b.boardid) c"); 
		
		List<Map<String,Object>> executeQuery = db.executeQuery(sql.toString());
		List<Board> list = Myutil.ListMapToJavaBean(executeQuery, Board.class);
		
		Map<Integer ,List<Board>> map=new HashMap<Integer ,List<Board>>();
		
		//循环所有的list 
		//取出每个baord中的parentid,
		//根据parentid到map中查是否有这个父版块id 的子版块列表,
		//如果有,则将这个board存到这个子版块列表用
		//如果没有,则创建一个List<Board>,将这个board存到List<Board>,再讲这个List<Board>存到map中
		
		for (Board board : list) {
			List<Board> sonList=null;
			if(map.containsKey(board.getParentid())) {
				sonList=map.get(board.getParentid());
			}else {
				sonList=new ArrayList<Board>();
			}
			sonList.add(board);
			map.put(board.getParentid(), sonList);		
		}
		return map;
	}
}
