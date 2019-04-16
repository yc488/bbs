package listener;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import bean.User;

/**
 * Application Lifecycle Listener implementation class UserListener
 *
 */
@WebListener
public class UserListener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener, HttpSessionBindingListener {

   
    public UserListener() {
        
    }	
  
    public void sessionDestroyed(HttpSessionEvent arg0)  { 
         HttpSession session=arg0.getSession();
         session.invalidate();
    }

    public void attributeAdded(HttpSessionBindingEvent event)  { 
    	@SuppressWarnings("unchecked")
		HashMap<String, HttpSession> map=(HashMap<String, HttpSession>) event.getSession().getServletContext().getAttribute("userMap");
    	String name = event.getName();

		if(name.equals("user")){
			User user= (User) event.getValue();
			System.out.println(map);
			if(map.get(user.getUname())!=null){
				HttpSession session=map.get(user.getUname());
				session.removeAttribute("user");

			}
			map.put(user.getUname(),event.getSession());
		}
		System.out.println(map);
    }

	
    public void attributeRemoved(HttpSessionBindingEvent event)  { 
    	@SuppressWarnings("unchecked")
		HashMap<String, HttpSession> map=(HashMap<String, HttpSession>) event.getSession().getServletContext().getAttribute("userMap");
    	String name = event.getName();
		System.out.println(name);
		if(name.equals("user")){
			User user= (User) event.getValue();
			System.out.println(map);
			if(map.get(user.getUname())!=null){
				HttpSession session=map.get(user.getUname());
				session.removeAttribute("user");
				map.remove(user.getUname());
			}
		}
		
    }
	
    public void contextInitialized(ServletContextEvent arg0)  { 
         ServletContext context = arg0.getServletContext();
         context.setAttribute("userMap", new HashMap<String,HttpSession>());
    }

	@Override
	public void valueBound(HttpSessionBindingEvent event) {}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {}

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {}
	
	
}
