<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.content"/>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
  <link href="../../css/bootstrap.css" rel="stylesheet">
  <title><fmt:message key="title.balance.recharge"/></title>
</head>
<body>
  <c:set var="path" value="/controller?command=account" scope="session"/>
  <%@include file="../common/header.jsp"%>
  <div class="container">
    <form id="rechargeBalance" action="${pageContext.request.contextPath}/controller" method="post">

      <div class="form-group-row">
        <label for="currentBalance"><fmt:message key="label.balance.current"/></label>
        <input id="currentBalance" readonly class="form-control-plaintext" value="<c:out value="${param.currentBalance}"/>">
      </div>

      <div class="form-group-row">
        <label for="card"><fmt:message key="label.card.number"/></label>
        <input id="card" class="form-control" name="cardNumber" type="text" pattern="[0-9]{16}" required
               placeholder="<fmt:message key="placeholder.card.number"/>" value="<c:out value="${cardNumber}"/>">
      </div>

      <jsp:useBean id="date" class="java.util.Date" scope="page"/>
      <fmt:formatDate value="${date}" var="curYear" pattern="YYYY"/>

      <div class="form-group-row">
        <label for="validUntil"><fmt:message key="label.card.valid_until"/></label>
        <div class="row" id="validUntil">
          <div class="col">
            <input class="form-control" name="validMonth" type="number" min="01" max="12" required
                   placeholder="<fmt:message key="placeholder.card.month"/>" value="<c:out value="${validMonth}"/>">
          </div>
          <div class="col">
            <input class="form-control" name="validYear" type="number" min="${curYear}" max="${curYear} + 30" required
                   placeholder="<fmt:message key="placeholder.card.year"/>" value="<c:out value="${validYear}"/>">
          </div>
        </div>
      </div>

      <div class="form-group-row">
        <label for="cvc/cvv"><fmt:message key="label.card.verification_code"/></label>
        <input id="cvc/cvv" class="form-control" name="cardVerificationCode" type="text" pattern="[0-9]{3}" required
               placeholder="<fmt:message key="placeholder.card.verification_code"/>" value="<c:out value="${cardVerificationCode}"/>">
      </div>

      <div class="form-group-row">
        <label for="cardHolder"><fmt:message key="label.card.holder"/></label>
        <input id="cardHolder" class="form-control" name="cardHolder" type="text" pattern="[a-zA-Z]{1,100} [a-zA-Z]{1,100}" required
               placeholder="<fmt:message key="placeholder.card.holder"/>" value="<c:out value="${cardHolder}"/>">
      </div>
      <c:if test="${not empty cardError}">
        ${cardError}
      </c:if>

      <div class="form-group-row">
        <label for="amount"><fmt:message key="label.money.amount"/></label>
        <input id="amount" class="form-control" name="amount" type="text" pattern="[0-9]{1,8}\.[0-9]{0,2}" required
               placeholder="<fmt:message key="placeholder.money"/>" value="<c:out value="${amount}"/>">
      </div>
      <c:if test="${not empty incorrectAmount}">
        ${incorrectAmount}
      </c:if>
    </form>

    <button form="rechargeBalance" class="btn btn-primary" name="command" value="recharge_balance">
      <fmt:message key="label.confirm"/>
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
