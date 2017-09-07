<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="resources.content"/>
<fmt:setLocale value="${locale}"/>
<html>
<head>
  <title><fmt:message key="title.wrong.request"/></title>
  <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
  <link href="../css/bootstrap.css" rel="stylesheet">
</head>
<body>
  <fmt:message key="message.wrong.request"/>
  <a href="${pageContext.request.contextPath}/index.jsp"><fmt:message key="label.home"/></a>
</body>
</html>
