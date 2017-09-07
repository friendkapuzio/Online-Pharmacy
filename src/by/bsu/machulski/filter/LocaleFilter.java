package by.bsu.machulski.filter;

import by.bsu.machulski.constant.SessionAttributeConstant;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LocaleFilter implements Filter {
    private String defaultEncoding;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        defaultEncoding = filterConfig.getInitParameter("defaultLocale");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) request).getSession();
        if (session.getAttribute(SessionAttributeConstant.LOCALE) == null) {
            session.setAttribute(SessionAttributeConstant.LOCALE, defaultEncoding);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
