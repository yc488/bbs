package biz;

import java.util.List;
import java.util.Map;

import bean.Board;
import dao.BoardDao;

public class BoardBiz {
	
	
	BoardDao bd=new BoardDao();
	/**
	 * 后台显示的主板块的信息
	 * @return
	 */
	public List<Board> bigBoardList(){
		List<Board> bigBoardList = bd.bigBoardList();
		return bigBoardList;
	}
	
	/**
	 * 修改主板块信息
	 * @param board
	 * @return
	 * @throws BizException 
	 */
	public void updateBigBoard(Board board) throws BizException {
		if(board.getBoardid()<0){
			throw new BizException("板块不存在");
		}else if(board.getBoardname()==null) {
			throw new BizException("板块名不能为空");
		}
		bd.updateBigBoard(board);
		
	}

	/**
	 * 增加主板块
	 * @param board
	 * @return
	 * @throws BizException 
	 */
	public void addBigBoard(Board board) throws BizException {
		if(board.getBoardname()==null) {
			throw new BizException("板块名不能为空");
		}
		
		bd.addBigBoard(board);
		
	}

	/**
	 * 删除主板块
	 * @param board
	 * @return
	 * @throws BizException 
	 */
	public void delBigBoard(Board board) throws BizException {
		if(board.getBoardid()<0){
			throw new BizException("板块不存在");
		}
		bd.delBigBoard(board);
		
	}

	/**
	 * 取出所有板块信息
	 * @return
	 * @throws BizException 
	 */
	public Map<Integer, List<Board>> findAllBoard() throws BizException {
		
		
		Map<Integer, List<Board>> findAllBoard = bd.findAllBoard();
		
		return findAllBoard;
	}
}
