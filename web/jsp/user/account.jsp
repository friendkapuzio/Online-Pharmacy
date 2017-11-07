<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.content"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="../../css/bootstrap.css" rel="stylesheet">
    <title><fmt:message key="title.account"/></title>
  </head>
  <body>
    <c:set var="path" value="/controller?command=account" scope="session"/>
    <%@include file="../common/header.jsp"%>

    <div class="container">
      <form name="email" action="${pageContext.request.contextPath}/controller" method="post">
        <div class="form-group row">
          <label for="email"><fmt:message key="label.email"/>:</label>
          <input id="email" readonly class="form-control-plaintext" value="<c:out value="${user.email}"/>">
        </div>

        <div class="form-group row">
          <label for="newEmail"><fmt:message key="label.email.new"/></label>
          <input id="newEmail" name="newEmail" type="email" class="form-control" pattern=".+@[a-z]{1,10}\.[a-z]{1,10}" required
                 placeholder="email@example.com" value="<c:out value="${newEmail}"/>">
          <c:if test="${not empty incorrectEmail}">
            ${incorrectEmail}
          </c:if>
          <c:if test="${not empty existingEmail}">
            ${existingEmail}
          </c:if>
        </div>

        <div class="form-group row">
          <button class="btn btn-primary" type="submit" name="command" value="change_email"><fmt:message key="label.email.change"/></button>
        </div>
      </form>


      <form name="name" action="${pageContext.request.contextPath}/controller" method="post">
        <div class="form-group row">
          <label for="name"><fmt:message key="label.name"/>:</label>
          <input id="name" readonly class="form-control-plaintext" value="<c:out value="${user.name}"/>">
        </div>

        <div class="form-group row">
          <label for="newName"><fmt:message key="label.name.new"/>:</label>
          <input id="newName" name="newName" type="text" class="form-control" pattern=".{1,32}" title="<fmt:message key="prompt.name"/>" required
                 placeholder="<fmt:message key="placeholder.name"/>" value="<c:out value="${newName}"/>">
          <c:if test="${not empty incorrectName}">
            ${incorrectName}
          </c:if>
        </div>

        <div class="form-group row">
          <button class="btn btn-primary" type="submit" name="command" value="change_name"><fmt:message key="label.name.change"/></button>
        </div>
      </form>


      <form>
        <div class="form-group row">
          <label for="registrationDate"><fmt:message key="label.registration_date"/></label>
          <input id="registrationDate" type="date" readonly class="form-control-plaintext" value="<c:out value="${user.registrationDate}"/>">
        </div>

        <div class="form-group row">
          <label for="role"><fmt:message key="label.role"/></label>
          <input id="role" readonly class="form-control-plaintext" value="<fmt:message key="label.user.role.${user.role.toString().toLowerCase()}"/>">
        </div>
      </form>


      <form name="balance" method="post">
        <div class="form-group row">
          <label for="balance"><fmt:message key="label.balance.current"/></label>
          <input id="balance" readonly class="form-control-plaintext" name="currentBalance" value="<c:out value="${user.balance}"/>">
        </div>

        <div class="form-group row">
          <button class="btn btn-primary"  type="submit" formaction="${pageContext.request.contextPath}/jsp/user/recharge_balance.jsp">
            <fmt:message key="label.balance.recharge"/>
          </button>

          <button class="btn btn-primary"  type="submit" formaction="${pageContext.request.contextPath}/jsp/user/withdraw_money.jsp">
            <fmt:message key="label.money.withdraw"/>
          </button>

          <button class="btn btn-primary" type="submit" formaction="${pageContext.request.contextPath}/jsp/user/send_money.jsp">
            <fmt:message key="label.money.send"/>
          </button>
          <button class="btn btn-primary" type="submit" formaction="${pageContext.request.contextPath}/jsp/user/transactions.jsp">
            <fmt:message key="label.transactions"/>
          </button>
        </div>
      </form>


      <form name="password" action="${pageContext.request.contextPath}/controller" method="post">
        <div class="form-group row">
          <label for="password"><fmt:message key="label.password.current"/></label>
          <input id="password" name="password" type="password" class="form-control" pattern=".{6,32}" required
                 title="<fmt:message key="prompt.password"/>">
          <c:if test="${not empty incorrectPassword}">
            ${incorrectPassword}
          </c:if>
        </div>

        <div class="form-group row">
          <label for="newPassword"><fmt:message key="label.password.new"/></label>
          <input id="newPassword" name="newPassword" type="password" class="form-control" pattern=".{6,32}" required
                 placeholder="<fmt:message key="placeholder.password"/>">
          <c:if test="${not empty incorrectNewPassword}">
            ${incorrectNewPassword}
          </c:if>
        </div>

        <div class="form-group row">
          <label for="confirmNewPassword"><fmt:message key="label.password.new.confirm"/></label>
          <input id="confirmNewPassword" name="confirmNewPassword" type="password" class="form-control" pattern=".{6,32}" required
                 placeholder="<fmt:message key="placeholder.password.confirm"/>">
        </div>
        <c:if test="${not empty mismatchedPassword}">
          ${mismatchedPassword}
        </c:if>

        <div class="form-group row">
          <button class="btn btn-primary" type="submit" name="command" value="change_password">
            <fmt:message key="label.password.change"/>
          </button>
        </div>
      </form>

      <c:if test="${not empty userError}">
        <c:out value="${userError}"/>
      </c:if>

    </div>
  </body>
</html>
