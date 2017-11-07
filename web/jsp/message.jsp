<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.messages"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="../css/bootstrap.css" rel="stylesheet">
    <title><fmt:message key="title.${param.message}"/></title>
  </head>
  <body>
    <jsp:include page="common/header.jsp"/>
    <fmt:message key="${param.message}"/>
  </body>
</html>
