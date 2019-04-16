package servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;

import bean.User;
import bean.UserInfo;
import biz.BizException;
import biz.UserBiz;
import utils.Myutil;


@WebServlet("/bbsUser")
public class userServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	UserBiz ub=new UserBiz();
	
	
    public userServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String flag = request.getParameter("flag");
		
		switch (flag) {
		case "userLogin":
			userLogin(request,response);
			break;
		case "userReg":
			userReg(request,response);
			break;
		case "isUserName":
			isUserName(request,response);
			break;
		case "logout":
			logout(request,response);
			break;
		case "logAgain":
			logAgain(request,response);
			break;
		case "findUser":
			findUser(request,response);
			break;
		case "findUserInfo":
			findUserInfo(request,response);
			break;
		case "pwdchange":
			pwdchange(request,response);
			break;
		case "sendcode":
			sendcode(request,response);
			break;
		case "resetpwd":
			resetpwd(request,response);
			break;
		case "isEmail":
			isEmail(request,response);
			break;
		default:
			break;
		}
	}
	
	/**
	 * �����Ƿ����
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void isEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String email = request.getParameter("email");
		
		try {
			int email2 = ub.isEmail(email);
			if(email2==1) {
				response.getWriter().append("1");
			}else if(email2==0) {
				response.getWriter().append("0");
			}
		} catch (BizException e) {
			e.printStackTrace();
			response.getWriter().append("2");
		}
	}


	/**
	 * ��������ķ���
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void resetpwd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//��ȡ��֤��
		HttpSession session = request.getSession();
		String code1 = (String)session.getAttribute("code");//������ɵ���֤��
		String code2 = request.getParameter("mycode");//�û��������֤��
		String upass = request.getParameter("upass");
		String upass1 = request.getParameter("upass1");
		String email = request.getParameter("email");
		
		try {
			ub.resetpwd(upass,upass1, email,code1,code2);
			String msg = "�������óɹ�,�����µ�¼";
			request.setAttribute("success", msg);
			request.getRequestDispatcher("pages/resetpwd.jsp").forward(request, response);
		} catch (BizException e) {			
			e.printStackTrace();
			request.setAttribute("msg", e.getMessage());
			request.getRequestDispatcher("pages/resetpwd.jsp").forward(request, response);
		}
		
	}

	/**
	 * ��ȡ��֤��ķ���
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void sendcode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//ʹ�ù����෢����֤��
		String email = request.getParameter("email");
		System.out.println("�����ַ��"+ email);
		//�����ʼ���������֤�룬�����ص�ǰ�˽���
		String code = Myutil.sendemail(email);
		request.getSession().setAttribute("code", code);
		response.getWriter().append(code);
	}


	//�޸�����
	private void pwdchange(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//��ȡ����
		User user = new User();
		
		HttpSession session = request.getSession();
		
		user = (User) session.getAttribute("user");

		String upass = request.getParameter("upass");
		String newpass = request.getParameter("newpass");
		
		
		try {
			ub.pwdchange(user,newpass,upass);
			String msg = "�޸ĳɹ�,�����µ�¼";
			request.setAttribute("msg", msg);
			request.getRequestDispatcher("pages/pwdChange.jsp").forward(request, response);
		} catch (BizException e) {
			e.printStackTrace();
			request.setAttribute("msg",e.getMessage() );
			request.getRequestDispatcher("pages/pwdChange.jsp").forward(request, response);
		}

	}
	
	//��ѯ�����û���չ��Ϣ
	private void findUserInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> userInfo= ub.findUserInfo();
		String jsonString=JSON.toJSONString(userInfo);
		response.getWriter().append(jsonString);
	}


	//��ѯ�����û�
	private void findUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		List<Map<String, Object>> findUSer = ub.findUSer();
		
		String jsonString = JSON.toJSONString(findUSer);
		
		response.getWriter().append(jsonString);
	}


	//�˳�
	private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		session.setAttribute("user", null);
		session.setAttribute("callbackPath", null);
		request.getRequestDispatcher("index.jsp").forward(request, response);
		
	}
	//�޸�������˳�
	private void logAgain(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		session.setAttribute("user", null);
		request.getRequestDispatcher("pages/login.jsp").forward(request, response);	
	}

	//�ж��û����Ƿ����
	private void isUserName(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String uname = request.getParameter("uName");
		User user=new User();
		user.setUname(uname);
		
		if(uname==null||"".equals(uname)){   //�û���Ϊ��
			response.getWriter().write("2");
			return ;
		}

		try {
			int isname= ub.isUserName(user);
			if(isname==1) {  //�û����Ѵ���
				response.getWriter().write("1");
			}else {           //�û���������
				response.getWriter().write("0");
			}
		} catch (BizException e) {
			e.printStackTrace();
		}	
		
	}


	//�û�ע��
	private void userReg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uname = request.getParameter("uName");
		String upass = request.getParameter("uPass");
		Integer gender = Integer.parseInt(request.getParameter("gender")) ;
		String head = request.getParameter("head");
		String email=request.getParameter("email");
		String regcode = request.getParameter("regcode");
		String code = (String)request.getSession().getAttribute("code");
		
		User user=new User();
		user.setGender(gender);
		user.setHead(head);
		user.setUname(uname);
		user.setUpass(upass);
		user.setEmail(email);
		
		try {
			ub.reg(user, code, regcode);
			request.setAttribute("msg", "ע��ɹ�");
			request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
		} catch (BizException e) {
			e.printStackTrace();
			request.setAttribute("msg",e.getMessage() );
			request.getRequestDispatcher("pages/reg.jsp").forward(request, response);
		}
		
	}


	//�û���¼
	private void userLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uname = request.getParameter("uName");
		String upass = request.getParameter("uPass");
		String code = request.getParameter("code");
		
		
		User user=new User();
		user.setUname(uname);
		user.setUpass(upass);
		HttpSession session = request.getSession();
		String valCode = (String) session.getAttribute("code");
		
		try {
			user = ub.userLogin(user, code, valCode);
			session.setAttribute("user", user);

			UserInfo userinfo=ub.selectAll(user.getUid());
			session.setAttribute("userinfo", userinfo);
			
			//�ж��Ƿ��лص�·��
			if(request.getSession().getAttribute("callbackPath")!=null) {
				String path = (String) request.getSession().getAttribute("callbackPath");
				
				@SuppressWarnings("unchecked")
				Map<String, String[]> newmap = (Map<String, String[]>) request.getSession().getAttribute("callbackMap");
				//ƴ�ӵ�ַ
				path+="?";
				for (Map.Entry<String, String[]> entry : newmap.entrySet()) {
					String name=entry.getKey();
					String value=entry.getValue()[0];
					path+=name+"="+value+"&";
					
				}
				//�ض���ص�ҳ��
				String cxtPath=request.getContextPath();
				response.sendRedirect(cxtPath+path);
			}else {
				response.sendRedirect("index.jsp");
			}
		} catch (BizException e) {
			e.printStackTrace();
			request.setAttribute("msg",e.getMessage() );
			request.getRequestDispatcher("pages/login.jsp").forward(request, response);
		}

		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
