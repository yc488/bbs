package servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;

import bean.Board;
import bean.PageBean;
import bean.Reply;
import bean.Topic;
import bean.User;
import bean.UserInfo;
import biz.BizException;
import biz.BoardBiz;
import biz.ReplyBiz;
import biz.TopicBiz;

import utils.Myutil;


@WebServlet("/topic")
public class topicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	Topic topic =new Topic();

	TopicBiz tb=new TopicBiz();
	ReplyBiz rb=new ReplyBiz();
	BoardBiz bb=new BoardBiz();
    public topicServlet() {
        super();
    }
    
    

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=UTF-8");
		String flag = request.getParameter("flag");
		switch (flag) {
		case "topicList":
			topicList(request,response);
			break;
		case "post":
			post(request,response);
			break;
		case "topicDetail":
			topicDetail(request,response);
			break;
		case "answer":
			answer(request,response);
			break;
		case "del":
			del(request,response);
			break;
		case "firstPage":
			firstPage(request, response);
			break;
		case "nextPage":
			nextPage(request, response);
			break;
		case "lastPage":
			lastPage(request, response);
			break;
		case "finalPage":
			finalPage(request, response);
			break;
		case "topicHostList":
			topicHostList(request, response);
			break;
		case "personTopTopic":
			personTopTopic(request, response);
			break;
		case "showBigBoardList":
			showBigBoardList(request, response);
			break;
		case "updateBigBoard":
			updateBigBoard(request, response);
			break;
		case "addBigBoard":
			addBigBoard(request, response);
			break;
		case "delBigBoard":
			delBigBoard(request, response);
			break;
		case "listDel":
			listDel(request, response);
			break;
		case "hostDel":
			hostDel(request, response);
			break;
		case "search":
			search(request, response);
			break;
		case "myTopic":
			myTopic(request, response);
			break;
		default:
			break;
		}
	}

	

	/**
	 * 查找帖子
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String topicname = request.getParameter("topicname");
		
		try {
			List<Topic> searchTopic = tb.searchTopic(topicname);
			HttpSession session=request.getSession();
			session.setAttribute("searchTopic", searchTopic);
			request.getRequestDispatcher("/pages/searchTopic.jsp").forward(request, response);
		} catch (BizException e) {
			
			e.printStackTrace();
			request.setAttribute("msg", e.getMessage());
			request.getRequestDispatcher("/pages/searchTopic.jsp").forward(request, response);
		}
		
	}



	//每个板块热帖的删除
	private void hostDel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int topicid = Integer.parseInt(request.getParameter("topicid")) ;
		topic.setTopicid(topicid);
		
		try {
			tb.delTopic(topic);
			request.setAttribute("msg", "删除成功");
			topicHostList(request, response);
		} catch (BizException e) {
			e.printStackTrace();
			request.setAttribute("msg", e.getMessage());
			request.getRequestDispatcher("/pages/hostList.jsp").forward(request, response);
		}
		
	}



	//每个板块list的删除
	private void listDel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int topicid = Integer.parseInt(request.getParameter("topicid")) ;
		topic.setTopicid(topicid);
		
		try {
			tb.delTopic(topic);
			request.setAttribute("msg", "删除成功");
			topicList(request,response);
		} catch (BizException e) {
			
			e.printStackTrace();
			request.setAttribute("msg", e.getMessage());
			request.getRequestDispatcher("/pages/list.jsp").forward(request, response);
		}
		
	}



	//删除主板块
	private void delBigBoard(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Integer boardid = Integer.parseInt(request.getParameter("boardid"));
		
		Board board=new Board();
		board.setBoardid(boardid);
		try {
			bb.delBigBoard(board);
			response.getWriter().write("1");
		} catch (BizException e) {
			e.printStackTrace();
			response.getWriter().write("0");
		}
		
		
	}

	//增加主板块

	private void addBigBoard(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String boardname = request.getParameter("boardname");

		Board board=new Board();
		board.setBoardname(boardname);
		
		try {
			bb.addBigBoard(board);
			response.getWriter().write("1");
		} catch (BizException e) {
			e.printStackTrace();
			response.getWriter().write("0");
		}

		
	}

	//修改主板块信息
	private void updateBigBoard(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String boardname = request.getParameter("boardname");
		
		Integer boardid = Integer.parseInt(request.getParameter("boardid"));
		
		Board board=new Board();
		board.setBoardid(boardid);
		board.setBoardname(boardname);
		
		try {
			bb.updateBigBoard(board);
			response.getWriter().write("1");
		} catch (BizException e) {
			e.printStackTrace();
			response.getWriter().write("0");
		}
		
		
	}

	//后台主板块管理
	@SuppressWarnings("unchecked")
	private void showBigBoardList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<Board> bigBoardList = bb.bigBoardList();

		@SuppressWarnings("rawtypes")
		Map map=new HashMap<>();
		map.put("rows", bigBoardList);
		String jsonString = JSON.toJSONString(map);
		response.getWriter().write(jsonString);
	}

	//风云人物的所有帖子
	private void personTopTopic(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer uid = Integer.parseInt(request.getParameter("uid"));
		topic .setUid(uid);
		
		List<Topic> personTopTopic;
		try {
			personTopTopic = tb.personTopTopic(topic);
			HttpSession session=request.getSession();
			session.setAttribute("personTopTopic", personTopTopic);
			
			request.getRequestDispatcher("/pages/personTopTopic.jsp").forward(request, response);
		} catch (BizException e) {
			
			e.printStackTrace();
			request.setAttribute("msg", e.getMessage());
			request.getRequestDispatcher("/pages/personTopTopic.jsp").forward(request, response);
		}
		
	}


	//每个板块前10的热帖
	private void topicHostList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Board board=new Board();
		Integer boardid = Integer.parseInt(request.getParameter("boardid"));
		
		board.setBoardid(boardid);
		
		HttpSession session=request.getSession();
		@SuppressWarnings("unchecked")
		Map<Integer, List<Board>> map=(Map<Integer, List<Board>>) session.getAttribute("boardMap");
		for( Map.Entry<Integer, List<Board>> entry: map.entrySet()  ) {
			List<Board> listBoard=entry.getValue();
			for(  Board b:listBoard) {
				if( b.getBoardid()==board.getBoardid()) {
					board=b;
					break;
				}
			}
		}
		session.setAttribute("board", board);
		
		topic.setBoardid(board.getBoardid());
		List<Topic> pagebean;
		try {
			pagebean = tb.findHostTopic(topic);
			session.setAttribute("pagebean", pagebean);
			
			request.getRequestDispatcher("/pages/hostList.jsp").forward(request, response);
		} catch (BizException e) {
			
			e.printStackTrace();
			request.setAttribute("msg", e.getMessage());
			request.getRequestDispatcher("/pages/hostList.jsp").forward(request, response);
		}
		
		
		
	}
	//用户的帖子
	private void myTopic(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer uid = Integer.parseInt(request.getParameter("uid"));
		topic .setUid(uid);
		
		List<Topic> personTopic;
		try {
			personTopic = tb.myTopic(topic);
			HttpSession session=request.getSession();
			session.setAttribute("personTopic", personTopic);
			request.getRequestDispatcher("/pages/myTopic.jsp").forward(request, response);
		} catch (BizException e) {
			e.printStackTrace();
			request.setAttribute("msg", e.getMessage());
			request.getRequestDispatcher("/pages/myTopic.jsp").forward(request, response);
		}
	}

	//首页
	public void firstPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer pages = Integer.parseInt(request.getParameter("pages")) ;
		Topic topic=new Topic();
		Integer boardid = Integer.parseInt(request.getParameter("boardid"));
		topic.setBoardid(boardid);
		
		topic.setPages(pages);
		PageBean<Topic> pagebean = null;
		try {
			pagebean = tb.findPageBean(topic);
			HttpSession session = request.getSession();
			pagebean.setPages(pages);
			session.setAttribute("pagebean",pagebean);
			request.getRequestDispatcher("pages/list.jsp").forward(request, response);
		} catch (BizException e) {
			
			e.printStackTrace();
			request.setAttribute("msg", e.getMessage());
			request.getRequestDispatcher("pages/list.jsp").forward(request, response);
		}
		
	}
	
	//下一页
	public void nextPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		@SuppressWarnings("unchecked")
		PageBean<Topic> pageBean = (PageBean<Topic>) session.getAttribute("pagebean");

		Integer pages = Integer.parseInt(request.getParameter("pages")) ;

		if(pageBean.getTotalPage()<=pages) {
			pages=pageBean.getTotalPage().intValue();
		}

		Topic topic=new Topic();

		Integer boardid = Integer.parseInt(request.getParameter("boardid"));
		topic.setBoardid(boardid);
		
		topic.setPages(pages);
		PageBean<Topic> pagebean;
		try {
			pagebean = tb.findPageBean(topic);
			pagebean.setPages(pages);
			session.setAttribute("pagebean",pagebean);

			request.getRequestDispatcher("pages/list.jsp").forward(request, response);
		} catch (BizException e) {
			e.printStackTrace();
			request.setAttribute("msg", e.getMessage());
			request.getRequestDispatcher("pages/list.jsp").forward(request, response);
		}
		
	}
	
	//上一页
	public void lastPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Integer pages = Integer.parseInt(request.getParameter("pages")) ;
		if(pages>1) {
			pages--;
		}
		
		Topic topic=new Topic();

		Integer boardid = Integer.parseInt(request.getParameter("boardid"));
		topic.setBoardid(boardid);
		
		HttpSession session = request.getSession();
		topic.setPages(pages);
		PageBean<Topic> pagebean;
		try {
			pagebean = tb.findPageBean(topic);
			pagebean.setPages(pages);
			session.setAttribute("pagebean",pagebean);
			request.getRequestDispatcher("pages/list.jsp").forward(request, response);
		} catch (BizException e) {
			e.printStackTrace();
			request.setAttribute("msg", e.getMessage());
			request.getRequestDispatcher("pages/list.jsp").forward(request, response);
		}
		
	}
	
	//末页
	public void finalPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Integer pages = Integer.parseInt(request.getParameter("pages")) ;	
		Topic topic=new Topic();
		Integer boardid = Integer.parseInt(request.getParameter("boardid"));
		topic.setBoardid(boardid);
		
		HttpSession session = request.getSession();
		topic.setPages(pages);
		PageBean<Topic> pagebean;
		try {
			pagebean = tb.findPageBean(topic);
			pagebean.setPages(pages);
			session.setAttribute("pagebean",pagebean);
			request.getRequestDispatcher("pages/list.jsp").forward(request, response);
		} catch (BizException e) {
			e.printStackTrace();
			request.setAttribute("msg", e.getMessage());
			request.getRequestDispatcher("pages/list.jsp").forward(request, response);
		}
		
	}

	//删除帖子
	private void del(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int topicid = Integer.parseInt(request.getParameter("topicid")) ;
		topic.setTopicid(topicid);
		
		try {
			tb.delTopic(topic);
			request.setAttribute("msg", "删除成功");
			personTopTopic(request,response);
		} catch (BizException e) {
			e.printStackTrace();
			request.setAttribute("msg", e.getMessage());
			request.getRequestDispatcher("/pages/personTopTopic.jsp").forward(request, response);
		}
	}

	//回复帖子
	private void answer(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		int uid = Integer.parseInt(request.getParameter("uid")) ;
		int topicid = Integer.parseInt(request.getParameter("topicid")) ;
		
		
		Integer pages =0;
		if(request.getParameter("pages")==null || "".equals(request.getParameter("pages"))) {
			pages=1;
		}else {
			pages = Integer.parseInt(request.getParameter("pages"));
			
		}
		UserInfo userinfo=(UserInfo) request.getSession().getAttribute("userinfo");		
		System.out.println("登录时的userinfo"+userinfo);
		String content = request.getParameter("content");
		topic.setUid(uid);
		topic.setTopicid(topicid);
		topic.setContent(content);
		User user=(User) request.getSession().getAttribute("user");
		try {
			rb.answer(topic,user.getEmail());
			System.out.println("回帖后的"+rb.getUserinfo());
			if(rb.getFlag()==false) {
				request.getSession().setAttribute("userinfo", rb.getUserinfo());
				response.getWriter().write("<script language='javascript'>"
						+ "alert('请注意用词!!!');"
						+ "window.location='topic?flag=topicDetail&topicid="+topic.getTopicid()+"&pages="+pages+"'"
						+ "</script>");			
			}else {
				response.sendRedirect("topic?flag=topicDetail&topicid="+topic.getTopicid()+"&pages="+pages);				
			}
		} catch (BizException e) {
			request.setAttribute("msg", e.getMessage());
			System.out.println(e.getMessage());
			request.getRequestDispatcher("pages/answer.jsp").forward(request, response);
		}

	}

	//每个帖子的详情
	private void topicDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer topicid = Integer.parseInt(request.getParameter("topicid"));
		topic.setTopicid(topicid);
		HttpSession session=request.getSession();
		
		//查询该帖子的详情
		Topic topicdetail=null;
		try {
			topicdetail = tb.topicdetail(topic);
		} catch (BizException e) {
			e.printStackTrace();
			request.setAttribute("msg", e.getMessage());
			request.getRequestDispatcher("pages/detail.jsp").forward(request, response);
		}
		
		if(request.getParameter("boardid")!=null) {
			Board board=new Board();
			Integer boardid = Integer.parseInt(request.getParameter("boardid"));
			board.setBoardid(boardid);
						
			@SuppressWarnings("unchecked")
			Map<Integer, List<Board>> map=(Map<Integer, List<Board>>) session.getAttribute("boardMap");
			for( Map.Entry<Integer, List<Board>> entry: map.entrySet()  ) {
				List<Board> listBoard=entry.getValue();
				for(  Board b:listBoard) {
					if( b.getBoardid()==board.getBoardid()) {
						board=b;
						break;
					}
				}
			}
			session.setAttribute("board", board);
		}
		session.setAttribute("topicdetail", topicdetail);

		Integer pages=0;
		if(request.getParameter("replyPages")==null || "".equals(request.getParameter("replyPages"))) {
			pages=1;
		}else {
			pages = Integer.parseInt(request.getParameter("replyPages")) ;
		}
		
		//查询回复该贴的列表
		topic.setPages(pages);
		PageBean<Reply> pagebean = rb.findPageBean(topic);
		//所有回复该帖的列表	
		session.setAttribute("pagebeanReply", pagebean);
		request.getRequestDispatcher("pages/detail.jsp").forward(request, response);
	}

	//发帖
	private void post(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		Map<String, String[]> parameterMap = request.getParameterMap();
		topic = Myutil.MapToJavaBean(parameterMap, Topic.class);
		
		Integer uid = Integer.parseInt(request.getParameter("uid")) ;
		Integer boardid = Integer.parseInt(request.getParameter("boardid"));
		
		topic.setUid(uid);
		topic.setBoardid(boardid);
		User user=null;
		HttpSession session = request.getSession();
		if(session.getAttribute("user")!=null) {
			user = (User) session.getAttribute("user");

			topic.setUid(user.getUid() );
		}
		UserInfo userinfo=(UserInfo) session.getAttribute("userinfo");		
		System.out.println("登录时的"+userinfo);
		
		try {	
			tb.post(topic,user.getEmail());
			System.out.println("发帖后的"+tb.getUserinfo());
			if(tb.getFlag()==false) {								//tb.getUserinfo().getTime()>userinfo.getTime()
				request.getSession().setAttribute("userinfo", tb.getUserinfo());
				response.getWriter().write("<script language='javascript'>"
						+ "alert('请注意用词!!!');"
						+ "window.location='topic?flag=topicList&boardid="+topic.getBoardid()+"'"
						+ "</script>");
			}else {
				request.getSession().setAttribute("userinfo", tb.getUserinfo());
				response.sendRedirect("topic?flag=topicList&boardid="+topic.getBoardid());
			}	
			
		} catch (BizException e) {
			e.printStackTrace();
			request.setAttribute("msg", e.getMessage());
			request.getRequestDispatcher("pages/post.jsp").forward(request, response);
		}
		
	}

	
	
	//帖子列表
	private void topicList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Board board=new Board();
		Integer boardid = Integer.parseInt(request.getParameter("boardid"));
		Integer pages =0;
		if(request.getParameter("pages")==null || "".equals(request.getParameter("pages"))) {
			pages=1;
		}else {
			pages = Integer.parseInt(request.getParameter("pages"));			
		}
		
		
		board.setBoardid(boardid);
		
		HttpSession session=request.getSession();
		@SuppressWarnings("unchecked")
		Map<Integer, List<Board>> map=(Map<Integer, List<Board>>) session.getAttribute("boardMap");
		for( Map.Entry<Integer, List<Board>> entry: map.entrySet()  ) {
			List<Board> listBoard=entry.getValue();
			for(  Board b:listBoard) {
				if( b.getBoardid()==board.getBoardid()) {
					board=b;
					break;
				}
			}
		}
		session.setAttribute("board", board);
		
		topic.setBoardid(board.getBoardid());
		topic.setPages(pages);
		
		//每页查询到的帖子
		PageBean<Topic> pagebean;
		try {
			pagebean = tb.findPageBean(topic);
			pagebean.setPages(pages);
			session.setAttribute("pagebean", pagebean);
			
			request.getRequestDispatcher("/pages/list.jsp").forward(request, response);
		} catch (BizException e) {
			e.printStackTrace();
			request.setAttribute("msg", e.getMessage());
			request.getRequestDispatcher("pages/list.jsp").forward(request, response);
		}
		
	}
	
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
