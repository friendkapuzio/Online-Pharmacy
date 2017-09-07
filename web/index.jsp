<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.content"/>
<html>
  <head>
    <title><fmt:message key="title.index"/></title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
  </head>
  <body>
    <jsp:forward page="${pageContext.request.contextPath}/controller?command=index"/>
  </body>
</html>
