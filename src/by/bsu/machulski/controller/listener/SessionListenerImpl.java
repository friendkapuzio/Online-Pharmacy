package by.bsu.machulski.controller.listener;

import by.bsu.machulski.constant.SessionAttributeConstant;
import by.bsu.machulski.type.UserRole;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionListenerImpl implements HttpSessionListener {
    private static final String DEFAULT_ENCODING = "en-US";

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        session.setAttribute(SessionAttributeConstant.LOCALE, DEFAULT_ENCODING);
        session.setAttribute(SessionAttributeConstant.ROLE, UserRole.GUEST);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

    }
}
