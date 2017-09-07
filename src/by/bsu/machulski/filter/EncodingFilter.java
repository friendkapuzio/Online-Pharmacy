package by.bsu.machulski.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    private String code;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        code = filterConfig.getInitParameter("encoding");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String requestCode = request.getCharacterEncoding();
        if (!code.equals(requestCode)) {
            request.setCharacterEncoding(code);
            response.setCharacterEncoding(code);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
