<%@ page isErrorPage="true" contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.content"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="../../css/bootstrap.css" rel="stylesheet">
    <title><fmt:message key="title.error.404"/></title>
  </head>
  <body>
    <fmt:message key="message.error.404"/>
    ${pageContext.errorData.requestURI}
    <br/>
    <a href="${pageContext.request.contextPath}/index.jsp"><fmt:message key="label.home"/></a>
  </body>
</html>
