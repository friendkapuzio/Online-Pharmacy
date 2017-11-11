<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.content"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="../css/bootstrap.css" rel="stylesheet">
    <title><fmt:message key="title.main"/></title>
  </head>
  <body>
    <c:set var="path" value="/index.jsp" scope="session"/>
    <%@include file="common/header.jsp"%>
    <%@include file="common/language_panel.jsp"%>

    <div class="container">
    <form class="form-inline" name="search" action="${pageContext.request.contextPath}/controller" method="post">
      <label>
        <fmt:message key="label.product.name"/>
        <input type="text" name="searchText" pattern=".{0,100}" placeholder="<fmt:message key="placeholder.search"/>">
      </label>
      <button type="submit" name="command" value="search_product"><fmt:message key="label.search"/></button>
    </form>
    </div>

    <table class="table table-striped">
      <thead>
        <tr>
          <th><fmt:message key="label.product.name"/></th>
          <th><fmt:message key="label.product.price"/></th>
          <th><fmt:message key="label.product.quantity"/></th>
          <th><fmt:message key="label.product.form"/></th>
          <th><fmt:message key="label.product.form.description"/></th>
          <th><fmt:message key="label.product.prescription.required"/></th>
        </tr>
      </thead>

      <tbody>
        <c:forEach var="product" items="${cheapestProducts}">
          <tr>
            <td><c:out value="${product.name}"/></td>
            <td><c:out value="${product.price}"/></td>
            <td><c:out value="${product.availableQuantity}"/></td>
            <td><c:out value="${product.productForm}"/></td>
            <td><c:out value="${product.formDescription}"/></td>
            <td>
              <c:choose>
                <c:when test="${product.isPrescriptionRequired eq true}">
                  <fmt:message key="label.yes"/>
                </c:when>
                <c:otherwise>
                  <fmt:message key="label.no"/>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </body>
</html>
