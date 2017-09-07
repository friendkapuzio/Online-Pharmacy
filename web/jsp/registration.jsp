<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.content"/>
<html>
  <head>
    <title><fmt:message key="title.registration"/></title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="../css/bootstrap.css" rel="stylesheet">
  </head>
  <body>
    <div class="container-fluid">
      <form name="registration" action="${pageContext.request.contextPath}/controller" method="post">
        <div>
          <label>
            <fmt:message key="label.email"/>:
            <input type="email" name="email" <%--pattern=".+@[a-z]{1,10}\.[a-z]{1,10}"--%> required
                   placeholder="email@example.com" value="${email}">
          </label>
          <c:if test="${not empty incorrectEmail}">
            ${incorrectEmail}
          </c:if>
          <c:if test="${not empty existingEmail}" >
            ${existingEmail}
          </c:if>
        </div>
        <div>
          <label>
            <fmt:message key="label.name"/>:
            <input type="text" name="name" pattern=".{1,32}" title="<fmt:message key="prompt.name"/>" required
                   placeholder="<fmt:message key="placeholder.name"/>" value="${name}">
          </label>
          <c:if test="${not empty incorrectName}">
            ${incorrectName}
          </c:if>
        </div>
        <div>
          <label>
            <fmt:message key="label.password"/>:
            <input type="password" name="password" pattern=".{6,32}" title="<fmt:message key="prompt.password"/>" required
                   placeholder="<fmt:message key="placeholder.password"/>">
          </label>
          <c:if test="${not empty incorrectPassword}">
            ${incorrectPassword}
          </c:if>
        </div>
        <div>
          <label>
            <fmt:message key="label.confirm_password"/>:
            <input type="password" name="confirmPassword" pattern=".{6,32}" title="<fmt:message key="prompt.password"/>" required
                   placeholder="<fmt:message key="placeholder.confirm_password"/>">
          </label>
          <c:if test="${not empty mismatchedPassword}">
            ${mismatchedPassword}
          </c:if>
        </div>
        <button type="submit" name="command" value="register"><fmt:message key="label.register"/></button>
      </form>
      <form name="products" action="${pageContext.request.contextPath}/index.jsp" method="post">
        <button type="submit"><fmt:message key="label.home"/></button>
      </form>
    </div>
  </body>
</html>
