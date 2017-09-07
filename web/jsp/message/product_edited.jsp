<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.content"/>
<html>
<head>
    <title><fmt:message key="title.product.edited"/></title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="../../css/bootstrap.css" rel="stylesheet"></head>
<body>
  <c:set var="path" value="/jsp/message/product_edited.jsp" scope="session"/>
  <%@include file="../common/header.jsp"%>
  <fmt:message key="message.product.edited"/>
</body>
</html>
