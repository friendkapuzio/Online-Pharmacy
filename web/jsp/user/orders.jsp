<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.content"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="../../css/bootstrap.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
    <title><fmt:message key="title.orders"/></title>
  </head>
  <body>
    <c:set var="path" value="/jsp/user/orders.jsp" scope="session"/>
    <%@include file="../common/header.jsp"%>

    <div class="container">
      <form class="form-inline" action="${pageContext.request.contextPath}/controller" method="post">
        <label for="year"><fmt:message key="label.orders.year"/></label>
        <select id="year" name="year" required>
          <jsp:useBean id="date" class="java.util.Date" scope="page"/>
          <fmt:formatDate value="${date}" var="currentYear" pattern="YYYY"/>
          <fmt:formatDate value="${registrationDate}" var="registrationYear" pattern="YYYY"/>

          <c:forEach var="optionYear" begin="${registrationYear}" end="${currentYear}">
            <option value="${optionYear}" <c:if test="${optionYear eq currentYear}">selected</c:if>>${optionYear}</option>
          </c:forEach>
        </select>

        <button class="btn btn-primary" type="submit" name="command" value="show_orders">
          <fmt:message key="label.orders.show"/>
        </button>
      </form>

      <c:choose>
        <c:when test="${not empty orders}">
          <table class="table table-striped">
            <thead>
              <tr>
                <th><fmt:message key="label.order.number"/></th>
                <th><fmt:message key="label.order.placement.date"/></th>
                <th><fmt:message key="label.order.products"/></th>
                <th><fmt:message key="label.order.status"/></th>
                <th><fmt:message key="label.order.price.total"/></th>
              </tr>
            </thead>

            <tbody>
              <c:forEach var="order" items="${orders}">
                <tr>
                  <td>
                    <div class="modal fade" id="${order.id}" tabindex="-1" role="dialog" aria-labelledby="${order.id}">
                      <%@include file="order_details.jsp"%>
                    </div>
                    <a href="#${order.id}" data-toggle="modal" data-target="#${order.id}"><c:out value="${order.id}"/></a>
                  </td>

                  <td><c:out value="${order.placementDate}"/></td>

                  <td>
                    <c:out value="${order.products[0].name}"/>
                    <c:if test="${fn:length(order.products) >= 2}">
                      <br>
                      <c:out value="${order.products[1].name}"/>
                    </c:if>
                    <c:if test="${fn:length(order.products) >= 3}">
                      <br>
                      <c:out value="${fn:length(order.products) - 2}"/>
                      <fmt:message key="label.order.products.more"/>
                    </c:if>
                  </td>

                  <td><fmt:message key="label.order.status.${order.status.toString().toLowerCase()}"/></td>

                  <td><c:out value="${order.totalPrice}"/></td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </c:when>

        <c:otherwise>
          <fmt:message key="message.orders.not_found"/>
        </c:otherwise>
      </c:choose>
    </div>

  </body>
</html>
