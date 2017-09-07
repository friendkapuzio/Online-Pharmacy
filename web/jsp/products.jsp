<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.content"/>
<html>
  <head>
    <title><fmt:message key="title.products"/></title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="../css/bootstrap.css" rel="stylesheet">
  </head>
  <body>
    <c:set var="path" value="/jsp/products.jsp" scope="session"/>
    <%@include file="common/header.jsp"%>
    <div>
      <form name="search" action="${pageContext.request.contextPath}/controller" method="post">
        <label>
          <fmt:message key="label.product.name"/>
          <input type="text" name="searchText" pattern=".{1,100}" required value="${productName}">
        </label>
        <button type="submit" name="command" value="search_product"><fmt:message key="label.search"/></button>
      </form>
      <ctg:pharmacist>
        <a href="${pageContext.request.contextPath}/jsp/pharmacist/add_product.jsp"><fmt:message key="label.add.product"/></a>
      </ctg:pharmacist>
    </div>
    <div>
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
        <c:forEach var="product" items="${products}" varStatus="i">
          <form id="${i} controller" action="${pageContext.request.contextPath}/controller" method="post">
            <input type="hidden" name="productId" value="${product.id}">
          </form>

          <form id="${i} edit_product" action="${pageContext.request.contextPath}/jsp/pharmacist/edit_product.jsp" method="post">
            <input type="hidden" name="productId" value="${product.id}">
            <input type="hidden" name="productName" value="${product.name}">
            <input type="hidden" name="productPrice" value="${product.price}">
            <input type="hidden" name="productQuantity" value="${product.availableQuantity}">
            <input type="hidden" name="productForm" value="${product.productForm}">
            <input type="hidden" name="formDescription" value="${product.formDescription}">
            <input type="hidden" name="isRecipeRequired" value="${product.isRecipeRequired}">
          </form>

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
            <ctg:pharmacist>
              <td>
                <button form="${i} edit_product" name="command" value="edit_form_product">
                  <fmt:message key="label.edit"/>
                </button>
              </td>
              <td>
                <button form="${i} controller" name="command" value="delete">
                  <fmt:message key="label.delete"/>
                </button>
              </td>
            </ctg:pharmacist>
            <c:if test="${not empty user_id}">
              <td>
                <label>
                  <fmt:message key="label.amount"/>
                  <input form="${i} controller" type="number" pattern="[1-9]{1}[0-9]*" name="amount"/>
                </label>
                <button form="${i} controller" type="submit" name="command" value="add_to_cart">
                  <fmt:message key="label.cart.add"/>
                </button>
              </td>
            </c:if>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
  </body>
</html>
