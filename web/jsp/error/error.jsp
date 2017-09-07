<%@ page isErrorPage="true" contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.content"/>
<html>
  <head>
    <title><fmt:message key="title.error"/></title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="../css/bootstrap.css" rel="stylesheet">
  </head>
  <body>
    ${pageContext.errorData.requestURI}
    <br/>
    ${pageContext.errorData.servletName}
    <br/>
    ${pageContext.errorData.statusCode}
    <br/>
    ${pageContext.errorData.throwable.message}
    <br/>
    ${pageContext.exception}
    <a href="${pageContext.request.contextPath}/index.jsp"><fmt:message key="label.home"/></a>
  </body>
</html>
