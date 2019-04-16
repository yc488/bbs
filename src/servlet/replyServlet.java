package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;

import bean.JsonModel;
import bean.PageBean;
import bean.Reply;
import bean.Topic;
import bean.User;
import biz.BizException;
import biz.ReplyBiz;
import biz.replyRedisImpl;


@WebServlet("/reply")
public class replyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	Reply reply=new Reply();

	replyRedisImpl rri=new replyRedisImpl();	
	ReplyBiz rb=new ReplyBiz();
    public replyServlet() {
        super();

    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String flag = request.getParameter("flag");
		switch (flag) {
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
		case "glktimes":
			glktimes(request, response);
			break;
		case "glk":
			glk(request, response);
			break;
		case "agree":
			agree(request,response);
			break;
		case "disagree":
			disagree(request,response);
			break;
		default:
			break;
		}
	}


	private void disagree(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String topicId=request.getParameter("topicid");
		String replyId=request.getParameter("replyid");
		Integer num=rb.disagree(topicId,replyId);
		Integer count=rb.selectAgreeCount(replyId);
		System.out.println(count);
		if(num>0) {			
			System.out.println(num);
			response.getWriter().write(count.toString());
		}
	}


	/**
	 * ��������
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void agree(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String topicId=request.getParameter("topicid");
		String replyId=request.getParameter("replyid");		
		Integer num=rb.agree(topicId,replyId);
		Integer count=rb.selectAgreeCount(replyId);
		System.out.println(count);
		if(num>0) {			
			System.out.println(num);
			response.getWriter().write(count.toString());
		}
	}
	
	
	/**
	 * topic����
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void glk(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonModel jm = new JsonModel();
		
		Reply reply = new Reply();
		Integer topicid = Integer.parseInt(request.getParameter("topicid")) ;
		reply.setTopicid(topicid);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		reply.setUid(user.getUid());
		long a = rri.glkreply(reply);
		
		jm.setCode(1);
		jm.setObj(a);
		
		String jsonString = JSON.toJSONString(jm);
		response.getWriter().append(jsonString);
		
	}

	/**
	 * topic���޴���
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void glktimes(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonModel jm = new JsonModel();
		
		Reply reply = new Reply();
		Integer topicid = Integer.parseInt(request.getParameter("topicid")) ;
		reply.setTopicid(topicid);
		Long a = rri.gettimes(reply);
		
		jm.setCode(1);
		jm.setObj(a + "");
		
		String jsonString = JSON.toJSONString(jm);
		response.getWriter().append(jsonString);
	}


	//��ҳ
	public void firstPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer topicid = Integer.parseInt(request.getParameter("topicid")) ;
		Topic topic=new Topic();

		
		topic.setTopicid(topicid);
		PageBean<Reply> pagebean = rb.findPageBean(topic);
		HttpSession session = request.getSession();
		pagebean.setPages(1);
		session.setAttribute("pagebeanReply",pagebean);
		request.getRequestDispatcher("pages/detail.jsp").forward(request, response);
	}
	
	//��һҳ
	public void nextPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		@SuppressWarnings("unchecked")
		PageBean<Topic> pagebeanReply = (PageBean<Topic>) session.getAttribute("pagebeanReply");

		Integer pages = Integer.parseInt(request.getParameter("replyPages")) ;

		if(pagebeanReply.getTotalPage()<=pages) {
			pages=pagebeanReply.getTotalPage().intValue();
		}

		Integer topicid = Integer.parseInt(request.getParameter("topicid")) ;
		Topic topic=new Topic();

		topic.setTopicid(topicid);

		topic.setPages(pages);
		PageBean<Reply> pagebean = rb.findPageBean(topic);
		pagebean.setPages(pages);

		session.setAttribute("pagebeanReply",pagebean);

		request.getRequestDispatcher("pages/detail.jsp").forward(request, response);
	}
	
	//��һҳ
	public void lastPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Integer pages = Integer.parseInt(request.getParameter("replyPages")) ;
		if(pages>1) {
			pages--;
		}
		
		Integer topicid = Integer.parseInt(request.getParameter("topicid")) ;
		Topic topic=new Topic();

		topic.setTopicid(topicid);
		
		HttpSession session = request.getSession();
		topic.setPages(pages);
		PageBean<Reply> pagebean = rb.findPageBean(topic);
		pagebean.setPages(pages);
		session.setAttribute("pagebeanReply",pagebean);
		request.getRequestDispatcher("pages/detail.jsp").forward(request, response);
	}
	
	//ĩҳ
	public void finalPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Integer pages = Integer.parseInt(request.getParameter("replyPages")) ;	
		Integer topicid = Integer.parseInt(request.getParameter("topicid")) ;
		Topic topic=new Topic();

		topic.setTopicid(topicid);
		
		HttpSession session = request.getSession();
		topic.setPages(pages);
		PageBean<Reply> pagebean = rb.findPageBean(topic);
		pagebean.setPages(pages);
		session.setAttribute("pagebeanReply",pagebean);
		request.getRequestDispatcher("pages/detail.jsp").forward(request, response);
	}

	//ɾ���ظ�
	private void del(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		Integer replyid = Integer.parseInt(request.getParameter("replyid"));
		Integer topicid = Integer.parseInt(request.getParameter("topicid"));
		reply.setReplyid(replyid);

		try {
			rb.delReply(reply);
			response.sendRedirect("topic?flag=topicDetail&topicid="+topicid);
		} catch (BizException e) {
			e.printStackTrace();
			request.setAttribute("msg", e.getMessage());
			request.getRequestDispatcher("pages/detail.jsp").forward(request, response);
		}
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
