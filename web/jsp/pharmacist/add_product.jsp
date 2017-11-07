<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.content"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="../../css/bootstrap.css" rel="stylesheet">
    <title><fmt:message key="title.product.add"/></title>
  </head>
  <body>
    <c:set var="path" value="/jsp/products.jsp" scope="session"/>
    <%@include file="../common/header.jsp"%>

    <form name="new_product" action="${pageContext.request.contextPath}/controller" method="post">
      <label>
        <fmt:message key="label.product.name"/>
        <input type="text" name="productName" pattern=".{1,100}" required value="<c:out value="${productName}"/>">
      </label>
      <c:if test="${not empty incorrectName}">
        <c:out value="${incorrectName}"/>
      </c:if>

      <label>
        <fmt:message key="label.product.price"/>
        <input type="text" name="productPrice" pattern="[0-9]{1,8}\.[0-9]{0,2}" required
               placeholder="<fmt:message key="placeholder.money"/>" value="<c:out value="${productPrice}"/>">
      </label>
      <c:if test="${not empty incorrectPrice}">
        <c:out value="${incorrectPrice}"/>
      </c:if>

      <label>
        <fmt:message key="label.product.quantity"/>
        <input type="number" name="productQuantity" pattern="[0-9]{1,10}" required value="<c:out value="${productQuantity}"/>">
      </label>
      <c:if test="${not empty incorrectQuantity}">
        <c:out value="${incorrectQuantity}"/>
      </c:if>

      <label>
        <fmt:message key="label.product.form"/>
        <input type="text" name="productForm" pattern=".{1,100}" required value="<c:out value="${productForm}"/>">
      </label>
      <c:if test="${not empty incorrectForm}">
        <c:out value="${incorrectForm}"/>
      </c:if>

      <label>
        <fmt:message key="label.product.form.description"/>
        <input type="text" name="formDescription" pattern=".{0,100}" value="<c:out value="${formDescription}"/>">
      </label>
      <c:if test="${not empty incorrectFormDescription}">
        <c:out value="${incorrectFormDescription}"/>
      </c:if>

      <label>
        <fmt:message key="label.product.prescription.required"/>
        <label>
          <fmt:message key="label.yes"/>
          <input type="radio" name="isPrescriptionRequired" <c:if test="${isPrescriptionRequired eq true}">checked</c:if> required value="true">
        </label>
        <label>
          <fmt:message key="label.no"/>
          <input type="radio" name="isPrescriptionRequired" <c:if test="${isPrescriptionRequired eq false}">checked</c:if> required value="false">
        </label>
      </label>

      <button type="submit" name="command" value="add_product"><fmt:message key="label.product.add"/></button>

      <c:if test="${not empty productError}">
        <c:out value="${productError}"/>
      </c:if>

    </form>
  </body>
</html>
