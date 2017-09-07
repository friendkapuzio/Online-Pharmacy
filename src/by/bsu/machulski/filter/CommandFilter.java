package by.bsu.machulski.filter;

import by.bsu.machulski.command.CommandType;
import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.resource.ConfigurationManager;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CommandFilter implements Filter {
    private static final String COMMAND_PARAMETER = "command";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String command = httpRequest.getParameter(COMMAND_PARAMETER);
        if (command == null || !CommandType.contains(command)) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + ConfigurationManager.getProperty(PageConfigConstant.WRONG_REQUEST));
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
