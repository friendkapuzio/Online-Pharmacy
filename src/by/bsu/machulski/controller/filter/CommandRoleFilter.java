package by.bsu.machulski.controller.filter;

import by.bsu.machulski.command.CommandType;
import by.bsu.machulski.constant.PageConfigConstant;
import by.bsu.machulski.constant.SessionAttributeConstant;
import by.bsu.machulski.resource.ConfigurationManager;
import by.bsu.machulski.type.UserRole;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Optional;

@WebFilter
public class CommandRoleFilter implements Filter {
    private static final String COMMAND_PARAMETER = "command";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        Optional<String> command = Optional.ofNullable(httpRequest.getParameter(COMMAND_PARAMETER));
        HttpSession session = httpRequest.getSession();
        try {
            if (command.isPresent()) {
                EnumSet<UserRole> roles = CommandType.valueOf(command.get().toUpperCase()).getRoles();
                if (!roles.contains(session.getAttribute(SessionAttributeConstant.ROLE))) {
                    httpResponse.sendRedirect(httpRequest.getContextPath() + ConfigurationManager.getPath(PageConfigConstant.INDEX));
                    return;
                }
            } else {
                httpResponse.sendRedirect(httpRequest.getContextPath() + ConfigurationManager.getPath(PageConfigConstant.WRONG_REQUEST));
                return;
            }
            filterChain.doFilter(request, response);
        } catch (IllegalArgumentException e) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + ConfigurationManager.getPath(PageConfigConstant.WRONG_REQUEST));
        }
    }

    @Override
    public void destroy() {

    }
}
