package by.bsu.machulski.controller;

import by.bsu.machulski.exception.NoSuchParameterException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SessionRequestContent {
    private HashMap<String, Object> requestAttributes = new HashMap<>();
    private HashMap<String, String[]> requestParameters = new HashMap<>();
    private HashMap<String, Object> sessionAttributes = new HashMap<>();

    public void extractValues(HttpServletRequest request) {
        requestParameters.putAll(request.getParameterMap());
        for (Enumeration<String> names = request.getAttributeNames(); names.hasMoreElements();) {
            String name = names.nextElement();
            requestAttributes.put(name, request.getAttribute(name));
        }
        HttpSession session = request.getSession();
        for (Enumeration<String> names = request.getSession().getAttributeNames(); names.hasMoreElements();) {
            String name = names.nextElement();
            sessionAttributes.put(name, session.getAttribute(name));
        }
    }

    public void insertValues(HttpServletRequest request) {
        for (Map.Entry<String, Object> attribute:requestAttributes.entrySet()) {
            request.setAttribute(attribute.getKey(), attribute.getValue());
        }
        for (Map.Entry<String, Object> attribute:sessionAttributes.entrySet()) {
            request.getSession().setAttribute(attribute.getKey(), attribute.getValue());
        }
    }

    public Object getRequestAttribute(String name) {
        return requestAttributes.get(name);
    }

    public void putRequestAttribute(String name, Object value) {
       requestAttributes.put(name, value);
    }

    public Optional<String[]> getParameterValues(String name) {
        return Optional.ofNullable(requestParameters.get(name));
    }

    public String getFirstParameterValue(String name) throws NoSuchParameterException {
        String[] values = Optional.ofNullable(requestParameters.get(name)).orElseThrow(() -> new NoSuchParameterException(name));
        return values[0];
    }

    public Object getSessionAttribute(String name) {
        return sessionAttributes.get(name);
    }

    public void putSessionAttribute(String name, Object value) {
        sessionAttributes.put(name, value);
    }
}
