package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.collect;
import bean.User;
import biz.BizException;
import biz.CollectBiz;


@WebServlet("/collect")
public class CollectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	CollectBiz cb=new CollectBiz();
	
    public CollectServlet() {
        super();

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String flag = request.getParameter("flag");
		
		switch (flag) {
		case "addCollect":
			addCollect(request,response);
			break;
		case "myCollect":
			myCollect(request,response);
			break;
		case "cancleCollect":
			cancleCollect(request,response);
			break;
		default:
			break;
		}
	}
	

	//取消收藏
	private void cancleCollect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int cid = Integer.parseInt(request.getParameter("cid"));
		collect col=new collect();
		col.setCid(cid);
		
		try {
			cb.cancleCollect(col);
			request.setAttribute("msg", "取消收藏成功");
			myCollect(request,response);
		} catch (BizException e) {

			e.printStackTrace();
			request.setAttribute("msg", e.getMessage());
			request.getRequestDispatcher("/pages/myCollect.jsp").forward(request, response);
		}
	}

	//查看我的收藏
	private void myCollect(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		int uid = Integer.parseInt(request.getParameter("uid"));
		collect col=new collect();
		col.setUid(uid);
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		List<collect> list;
		try {
			list = cb.findMyCollect(col);
			int collectTotal=cb.findAllCollect(user);
			session.setAttribute("collectTotal", collectTotal);
			session.setAttribute("myCollect", list);

			request.getRequestDispatcher("/pages/myCollect.jsp").forward(request, response);
		} catch (BizException e) {
			
			e.printStackTrace();
			request.setAttribute("msg", e.getMessage());
			request.getRequestDispatcher("/pages/myCollect.jsp").forward(request, response);
		}
		
	}

	//加入收藏1
	private void addCollect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int topicid = Integer.parseInt(request.getParameter("topicid"));
		Integer boardid = Integer.parseInt(request.getParameter("boardid"));
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		collect col=new collect();
		col.setTopicid(topicid);
		col.setBoardid(boardid);
		col.setUid(user.getUid());
		try {
			
			cb.addCollect(col);
			request.setAttribute("msg", "收藏成功");
			int collectTotal=cb.findAllCollect(user);
			session.setAttribute("collectTotal", collectTotal);
			request.getRequestDispatcher("/pages/detail.jsp").forward(request, response);
		} catch (BizException e) {
			e.printStackTrace();
			request.setAttribute("msg", e.getMessage());
			request.getRequestDispatcher("/pages/detail.jsp").forward(request, response);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
