package biz;

import java.util.List;
import java.util.Map;

import bean.Board;
import dao.BoardDao;

public class BoardBiz {
	
	
	BoardDao bd=new BoardDao();
	/**
	 * ��̨��ʾ����������Ϣ
	 * @return
	 */
	public List<Board> bigBoardList(){
		List<Board> bigBoardList = bd.bigBoardList();
		return bigBoardList;
	}
	
	/**
	 * �޸��������Ϣ
	 * @param board
	 * @return
	 * @throws BizException 
	 */
	public void updateBigBoard(Board board) throws BizException {
		if(board.getBoardid()<0){
			throw new BizException("��鲻����");
		}else if(board.getBoardname()==null) {
			throw new BizException("���������Ϊ��");
		}
		bd.updateBigBoard(board);
		
	}

	/**
	 * ���������
	 * @param board
	 * @return
	 * @throws BizException 
	 */
	public void addBigBoard(Board board) throws BizException {
		if(board.getBoardname()==null) {
			throw new BizException("���������Ϊ��");
		}
		
		bd.addBigBoard(board);
		
	}

	/**
	 * ɾ�������
	 * @param board
	 * @return
	 * @throws BizException 
	 */
	public void delBigBoard(Board board) throws BizException {
		if(board.getBoardid()<0){
			throw new BizException("��鲻����");
		}
		bd.delBigBoard(board);
		
	}

	/**
	 * ȡ�����а����Ϣ
	 * @return
	 * @throws BizException 
	 */
	public Map<Integer, List<Board>> findAllBoard() throws BizException {
		
		
		Map<Integer, List<Board>> findAllBoard = bd.findAllBoard();
		
		return findAllBoard;
	}
}
