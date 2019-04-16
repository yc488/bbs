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
	 * 邮箱是否存在
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
	 * 重置密码的方法
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void resetpwd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取验证码
		HttpSession session = request.getSession();
		String code1 = (String)session.getAttribute("code");//随机生成的验证码
		String code2 = request.getParameter("mycode");//用户输入的验证码
		String upass = request.getParameter("upass");
		String upass1 = request.getParameter("upass1");
		String email = request.getParameter("email");
		
		try {
			ub.resetpwd(upass,upass1, email,code1,code2);
			String msg = "密码重置成功,请重新登录";
			request.setAttribute("success", msg);
			request.getRequestDispatcher("pages/resetpwd.jsp").forward(request, response);
		} catch (BizException e) {			
			e.printStackTrace();
			request.setAttribute("msg", e.getMessage());
			request.getRequestDispatcher("pages/resetpwd.jsp").forward(request, response);
		}
		
	}

	/**
	 * 获取验证码的方法
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void sendcode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//使用工具类发送验证码
		String email = request.getParameter("email");
		System.out.println("邮箱地址："+ email);
		//发送邮件，返回验证码，并带回到前端界面
		String code = Myutil.sendemail(email);
		request.getSession().setAttribute("code", code);
		response.getWriter().append(code);
	}


	//修改密码
	private void pwdchange(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取参数
		User user = new User();
		
		HttpSession session = request.getSession();
		
		user = (User) session.getAttribute("user");

		String upass = request.getParameter("upass");
		String newpass = request.getParameter("newpass");
		
		
		try {
			ub.pwdchange(user,newpass,upass);
			String msg = "修改成功,请重新登录";
			request.setAttribute("msg", msg);
			request.getRequestDispatcher("pages/pwdChange.jsp").forward(request, response);
		} catch (BizException e) {
			e.printStackTrace();
			request.setAttribute("msg",e.getMessage() );
			request.getRequestDispatcher("pages/pwdChange.jsp").forward(request, response);
		}

	}
	
	//查询所有用户扩展信息
	private void findUserInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> userInfo= ub.findUserInfo();
		String jsonString=JSON.toJSONString(userInfo);
		response.getWriter().append(jsonString);
	}


	//查询所有用户
	private void findUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		List<Map<String, Object>> findUSer = ub.findUSer();
		
		String jsonString = JSON.toJSONString(findUSer);
		
		response.getWriter().append(jsonString);
	}


	//退出
	private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		session.setAttribute("user", null);
		session.setAttribute("callbackPath", null);
		request.getRequestDispatcher("index.jsp").forward(request, response);
		
	}
	//修改密码后退出
	private void logAgain(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		session.setAttribute("user", null);
		request.getRequestDispatcher("pages/login.jsp").forward(request, response);	
	}

	//判断用户名是否存在
	private void isUserName(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String uname = request.getParameter("uName");
		User user=new User();
		user.setUname(uname);
		
		if(uname==null||"".equals(uname)){   //用户名为空
			response.getWriter().write("2");
			return ;
		}

		try {
			int isname= ub.isUserName(user);
			if(isname==1) {  //用户名已存在
				response.getWriter().write("1");
			}else {           //用户名不存在
				response.getWriter().write("0");
			}
		} catch (BizException e) {
			e.printStackTrace();
		}	
		
	}


	//用户注册
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
			request.setAttribute("msg", "注册成功");
			request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
		} catch (BizException e) {
			e.printStackTrace();
			request.setAttribute("msg",e.getMessage() );
			request.getRequestDispatcher("pages/reg.jsp").forward(request, response);
		}
		
	}


	//用户登录
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
			
			//判断是否有回调路径
			if(request.getSession().getAttribute("callbackPath")!=null) {
				String path = (String) request.getSession().getAttribute("callbackPath");
				
				@SuppressWarnings("unchecked")
				Map<String, String[]> newmap = (Map<String, String[]>) request.getSession().getAttribute("callbackMap");
				//拼接地址
				path+="?";
				for (Map.Entry<String, String[]> entry : newmap.entrySet()) {
					String name=entry.getKey();
					String value=entry.getValue()[0];
					path+=name+"="+value+"&";
					
				}
				//重定向回调页面
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
