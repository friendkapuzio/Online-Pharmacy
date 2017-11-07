<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.content"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="../css/bootstrap.css" rel="stylesheet">
    <title><fmt:message key="title.registration"/></title>
  </head>
  <body>
    <c:set var="path" value="/jsp/registration.jsp" scope="session"/>
    <div class="container">
      <form name="registration" action="${pageContext.request.contextPath}/controller" method="post">

        <div class="form-group">
          <label for="email"><fmt:message key="label.email"/>:</label>
          <input id="email" name="email" type="email" class="form-control" pattern=".+@[a-z]{1,10}\.[a-z]{1,10}" required
                 placeholder="email@example.com" value="<c:out value="${email}"/>"/>
          <c:if test="${not empty incorrectEmail}">
            <c:out value="${incorrectEmail}"/>
          </c:if>
          <c:if test="${not empty existingEmail}" >
            <c:out value="${existingEmail}"/>
          </c:if>
        </div>

        <div class="form-group">
          <label for="name"><fmt:message key="label.name"/>:</label>
          <input id="name" name="name" type="text" class="form-control" pattern=".{1,32}" title="<fmt:message key="prompt.name"/>" required
                 placeholder="<fmt:message key="placeholder.name"/>" value="<c:out value="${name}"/>"/>
          <c:if test="${not empty incorrectName}">
            <c:out value="${incorrectName}"/>
          </c:if>
        </div>

        <div class="form-group">
          <label for="password"><fmt:message key="label.password"/>:</label>
          <input id="password" name="password" type="password" class="form-control" pattern=".{6,32}" title="<fmt:message key="prompt.password"/>" required
                 placeholder="<fmt:message key="placeholder.password"/>">
          <c:if test="${not empty incorrectPassword}">
            <c:out value="${incorrectPassword}"/>
          </c:if>
        </div>

        <div class="form-group">
          <label for="confirmPassword"><fmt:message key="label.password.confirm"/>:</label>
          <input id="confirmPassword" name="confirmPassword" type="password" class="form-control" pattern=".{6,32}" title="<fmt:message key="prompt.password"/>" required
                 placeholder="<fmt:message key="placeholder.password.confirm"/>">
          <c:if test="${not empty mismatchedPassword}">
            <c:out value="${mismatchedPassword}"/>
          </c:if>
        </div>

        <button type="submit" name="command" value="register" class="btn btn-primary"><fmt:message key="label.register"/></button>

      </form>
      <form name="products" action="${pageContext.request.contextPath}/index.jsp" method="post">
        <button class="btn btn-secondary" type="submit"><fmt:message key="label.home"/></button>
      </form>

      <c:if test="${not empty userError}">
        <c:out value="${userError}"/>
      </c:if>

    </div>
  </body>
</html>
