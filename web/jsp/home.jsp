<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.content"/>
<html>
  <head>
    <title><fmt:message key="title.main"/></title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="../css/bootstrap.css" rel="stylesheet">
  </head>
  <body>
    <c:set var="path" value="/controller?command=index" scope="session"/>
    <%@include file="common/header.jsp"%>
    <form name="search" action="${pageContext.request.contextPath}/controller" method="post">
      <label>
        <fmt:message key="label.product.name"/>
        <input type="text" name="searchText" pattern=".{1,100}" required value="${productName}">
      </label>
      <button type="submit" name="command" value="search_product"><fmt:message key="label.search"/></button>
    </form>
    <table class="table">
      <thead>
        <tr>
          <th><fmt:message key="label.product.name"/></th>
          <th><fmt:message key="label.product.price"/></th>
          <th><fmt:message key="label.product.quantity"/></th>
          <th><fmt:message key="label.product.form"/></th>
          <th><fmt:message key="label.product.form.description"/></th>
          <th><fmt:message key="label.product.recipe.required"/></th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="product" items="${cheapestProducts}">
          <tr>
            <td>${product.name}</td>
            <td>${product.price}</td>
            <td>${product.availableQuantity}</td>
            <td>${product.productForm}</td>
            <td>${product.formDescription}</td>
            <td>
              <c:choose>
                <c:when test="${product.isRecipeRequired eq true}">
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
