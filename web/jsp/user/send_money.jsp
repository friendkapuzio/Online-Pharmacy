<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.content"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="../../css/bootstrap.css" rel="stylesheet">
    <title><fmt:message key="title.money.send"/></title>
  </head>
  <body>
    <c:set var="path" value="/controller?command=account" scope="session"/>
    <%@include file="../common/header.jsp"%>
    <div class="container">
      <form id="sendMoney" action="${pageContext.request.contextPath}/controller" method="post">

        <div class="form-group-row">
          <label for="currentBalance"><fmt:message key="label.balance.current"/></label>
          <input id="currentBalance" readonly class="form-control-plaintext" value="<c:out value="${param.currentBalance}"/>">
        </div>

        <div class="form-group-row">
          <label for="receiverEmail"><fmt:message key="label.money.receiver.email"/></label>
          <input id="receiverEmail" class="form-control" name="receiverEmail" type="email" pattern=".+@[a-z]{1,10}\.[a-z]{1,10}" required
                 placeholder="email@example.com" value="<c:out value="${receiverEmail}"/>">
        </div>
        <c:if test="${not empty incorrectEmail}">
          ${incorrectEmail}
        </c:if>
        <c:if test="${not empty incorrectUser}">
          ${incorrectUser}
        </c:if>

        <div class="form-group-row">
          <label for="amount"><fmt:message key="label.money.amount"/></label>
          <input id="amount" class="form-control" name="amount" type="text" pattern="[0-9]{1,8}\.[0-9]{0,2}" required
                 placeholder="<fmt:message key="placeholder.money"/>" value="<c:out value="${amount}"/>">
        </div>
        <c:if test="${not empty incorrectAmount}">
          ${incorrectAmount}
        </c:if>

        <div class="form-group-row">
          <label for="password"><fmt:message key="label.password.current"/></label>
            <input id="password" name="password" type="password" class="form-control" pattern=".{6,32}" required
                   title="<fmt:message key="prompt.password"/>">
            <c:if test="${not empty incorrectPassword}">
              ${incorrectPassword}
            </c:if>
        </div>
      </form>

      <button form="sendMoney" class="btn btn-primary" name="command" value="send_money">
        <fmt:message key="label.send"/>
      </button>

      <form name="cancel" action="${pageContext.request.contextPath}/controller" method="post">
        <button type="submit" class="btn btn-secondary" name="command" value="account"><fmt:message key="label.cancel"/></button>
      </form>

      <c:if test="${not empty userError}">
        <c:out value="${userError}"/>
      </c:if>

    </div>
  </body>
</html>
