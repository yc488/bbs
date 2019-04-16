package filters;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class userFilter
 */
@WebFilter(urlPatterns= {"/pages/personal.jsp","/pages/pwdChange.jsp"})
public class UserFilter implements Filter {

    /**
     * Default constructor. 
     */
    public UserFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest) request;
		HttpSession session=req.getSession();
		//判断user对象是否加入会话
		if(session.getAttribute("user")==null) {
			HttpServletResponse res=(HttpServletResponse) response;
			res.setContentType("text/html; charset=utf-8"); 
			PrintWriter out = res.getWriter();
			//提示用户登录，并返回登录界面
			out.println("<script>alert('��δ��½');location.href='login.jsp';</script>");
			out.flush();
		}
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
