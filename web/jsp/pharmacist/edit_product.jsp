<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.content"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="../../css/bootstrap.css" rel="stylesheet">
    <title><fmt:message key="title.edit.product"/></title>
  </head>
  <body>
    <c:set var="path" value="/jsp/products.jsp" scope="session"/>
    <%@include file="../common/header.jsp"%>

    <form name="new_product" action="${pageContext.request.contextPath}/controller" method="post">
      <input type="hidden" name="productId" value="<c:out value="${param.productId}"/>">

      <label for="productName"><fmt:message key="label.product.name"/></label>
      <input id="productName" type="text" name="productName" pattern=".{1,100}" required value="<c:out value="${param.productName}"/>">
      <c:if test="${not empty incorrectName}">
        ${incorrectName}
      </c:if>

      <label for="productPrice"><fmt:message key="label.product.price"/></label>
      <input id="productPrice" type="text" name="productPrice" pattern="[0-9]{1,8}\.[0-9]{0,2}" required
             placeholder="<fmt:message key="placeholder.money"/>" value="<c:out value="${param.productPrice}"/>">
      <c:if test="${not empty incorrectPrice}">
        ${incorrectPrice}
      </c:if>

      <fmt:message key="label.product.quantity.current"/>: <c:out value="${param.productQuantity}"/>
      <c:choose>

        <c:when test="${!param.isDeleted}">
          <label>
            <label for="modify"><fmt:message key="label.add"/></label>
            <input id="modify" type="radio" name="isAdd" required value="true">

            <label for="doNotModify"><fmt:message key="label.not.modify"/></label>
            <input id="doNotModify" type="radio" name="isAdd" required value="false">
          </label>
          <label>
            <input type="number" name="addedAmount" pattern="[0-9]{1,10}" required value="0">
          </label>
        </c:when>

        <c:otherwise>
          <label for="productQuantity"><fmt:message key="label.product.quantity.new"/></label>
          <input id="productQuantity" type="number" name="newQuantity" pattern="[0-9]{1,10}" required value="<c:out value="${param.productQuantity}"/>">
        </c:otherwise>

      </c:choose>
      <c:if test="${not empty incorrectQuantity}">
        ${incorrectQuantity}
      </c:if>

      <label for="productForm"><fmt:message key="label.product.form"/></label>
      <input id="productForm" type="text" name="productForm" pattern=".{1,100}" required value="<c:out value="${param.productForm}"/>">
      <c:if test="${not empty incorrectForm}">
        ${incorrectForm}
      </c:if>

      <label for="formDescription"><fmt:message key="label.product.form.description"/></label>
      <input id="formDescription" type="text" name="formDescription" pattern=".{0,100}" value="<c:out value="${param.formDescription}"/>">
      <c:if test="${not empty incorrectFormDescription}">
        ${incorrectFormDescription}
      </c:if>

      <label>
        <fmt:message key="label.product.prescription.required"/>
        <label for="prescriptionRequired"><fmt:message key="label.yes"/></label>
        <input id="prescriptionRequired" type="radio" name="isPrescriptionRequired" <c:if test="${param.isPrescriptionRequired eq true}">checked</c:if> required value="true">

        <label for="prescriptionNotRequired"><fmt:message key="label.no"/></label>
        <input id="prescriptionNotRequired" type="radio" name="isPrescriptionRequired" <c:if test="${param.isPrescriptionRequired eq false}">checked</c:if> required value="false">
      </label>

      <label>
        <fmt:message key="label.product.is_deleted"/>
        <label for="isDeleted"><fmt:message key="label.yes"/></label>
        <input id="isDeleted" type="radio" name="isDeleted" <c:if test="${param.isDeleted eq true}">checked</c:if> required value="true">

        <label for="isNotDeleted"><fmt:message key="label.no"/></label>
        <input id="isNotDeleted" type="radio" name="isDeleted" <c:if test="${param.isDeleted eq false}">checked</c:if> required value="false">
      </label>

      <c:choose>
        <c:when test="${!param.isDeleted}">
          <button type="submit" name="command" value="edit_product"><fmt:message key="label.save"/></button>
        </c:when>
        <c:otherwise>
          <button type="submit" name="command" value="edit_deleted_product"><fmt:message key="label.save"/></button>
        </c:otherwise>
      </c:choose>

      <c:if test="${not empty productError}">
        <c:out value="${productError}"/>
      </c:if>
    </form>
  </body>
</html>
