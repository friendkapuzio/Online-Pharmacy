package by.bsu.machulski.controller.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//@WebFilter(dispatcherTypes = { DispatcherType.FORWARD }, urlPatterns = { "/jsp/admin/*" } )
public class AdminForwardFilter implements Filter {
    public void init(FilterConfig fConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        User user = (User) httpRequest.getSession().getAttribute(PageManager.USER);
//        if(user == null || !user.getRole().equals(RoleEnum.ADMIN.getRole()))
        {

            RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
        }
        chain.doFilter(request, response);
    }

    public void destroy()
    {

    }
}
