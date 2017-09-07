<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.content"/>
<html>
  <head>
    <title><fmt:message key="title.registration.completed"/></title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="../../css/bootstrap.css" rel="stylesheet">
  </head>
  <body>
    <c:set var="path" value="/jsp/message/registration_completed.jsp" scope="session"/>
    <%@include file="../common/header.jsp"%>
    <fmt:message key="message.registration.completed"/>
    <a href="${pageContext.request.contextPath}/index.jsp"><fmt:message key="label.home"/></a>
  </body>
</html>
