<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.content"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="../../css/bootstrap.css" rel="stylesheet">
    <title><fmt:message key="title.transactions"/></title>
  </head>
  <body>
    <%@include file="../common/header.jsp"%>

    <div class="container">
      <form class="form-inline" action="${pageContext.request.contextPath}/controller" method="post">
        <label for="year"><fmt:message key="label.transactions.year"/></label>
        <select id="year" name="transactionYear" required>
          <jsp:useBean id="date" class="java.util.Date" scope="page"/>
          <fmt:formatDate value="${date}" var="currentYear" pattern="YYYY"/>
          <fmt:formatDate value="${registrationDate}" var="registrationYear" pattern="YYYY"/>

          <c:forEach var="optionYear" begin="${registrationYear}" end="${currentYear}">
            <option value="${optionYear}" <c:if test="${optionYear eq transactionYear}">selected</c:if>>${optionYear}</option>
          </c:forEach>
        </select>

        <button class="btn btn-primary" type="submit" name="command" value="show_transactions">
          <fmt:message key="label.transactions.show"/>
        </button>
      </form>

      <c:choose>

        <c:when test="${not empty transactions}">
          <div>
            <table class="table table-striped">
              <thead>
              <tr>
                <th><fmt:message key="label.transaction.sender"/></th>
                <th><fmt:message key="label.transaction.receiver"/></th>
                <th><fmt:message key="label.transaction.date"/></th>
                <th><fmt:message key="label.transaction.amount"/></th>
              </tr>
              </thead>
              <tbody>
              <c:forEach var="transaction" items="${transactions}">

                <tr>
                  <td><c:out value="${transaction.senderEmail}"/></td>

                  <td><c:out value="${transaction.receiverEmail}"/></td>

                  <td><c:out value="${transaction.date}"/></td>

                  <td><c:out value="${transaction.amount}"/></td>
                </tr>
              </c:forEach>
              </tbody>
            </table>
          </div>
        </c:when>

        <c:when test="${transactions ne null}">
          <fmt:message key="message.transactions.not_found"/>
        </c:when>

      </c:choose>
    </div>
  </body>
</html>
