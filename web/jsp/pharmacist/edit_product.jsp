<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.content"/>
<html>
  <head>
    <title><fmt:message key="title.edit.product"/></title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="../../css/bootstrap.css" rel="stylesheet">
  </head>
  <body>
    <c:set var="path" value="/jsp/pharmacist/edit_product.jsp" scope="session"/>
    <%@include file="../common/header.jsp"%>

    <form name="new_product" action="${pageContext.request.contextPath}/controller" method="post">
      <input type="hidden" name="productId" value="${param.productId}">
      <label>
        <fmt:message key="label.product.name"/>
        <input type="text" name="productName" pattern=".{1,100}" required value="${param.productName}">
      </label>
      <c:if test="${not empty incorrectName}">
        ${incorrectName}
      </c:if>

      <label>
        <fmt:message key="label.product.price"/>
        <input type="text" name="productPrice" pattern="[0-9]{1,8}\.[0-9]{0,2}" required
               placeholder="<fmt:message key="placeholder.product.price"/>" value="${param.productPrice}">
      </label>
      <c:if test="${not empty incorrectPrice}">
        ${incorrectPrice}
      </c:if>

      <fmt:message key="label.product.quantity.current"/>: ${param.productQuantity}
      <label>
        <label>
          <fmt:message key="label.add"/>
          <input type="radio" name="quantityModification" required value="+">
        </label>
        <label>
          <fmt:message key="label.not.modify"/>
          <input type="radio" name="quantityModification" required value="">
        </label>
        <label>
          <fmt:message key="label.remove"/>
          <input type="radio" name="quantityModification" required value="-">
        </label>
      </label>
      <label>
        <input type="number" name="modificationAmount" pattern="[0-9]{1,10}" required value="0">
      </label>
      <c:if test="${not empty incorrectQuantity}">
        ${incorrectQuantity}
      </c:if>

      <label>
        <fmt:message key="label.product.form"/>
        <input type="text" name="productForm" pattern=".{1,100}" required value="${param.productForm}">
      </label>
      <c:if test="${not empty incorrectForm}">
        ${incorrectForm}
      </c:if>

      <label>
        <fmt:message key="label.product.form.description"/>
        <input type="text" name="formDescription" pattern=".{0,100}" value="${param.formDescription}"/>
      </label>
      <c:if test="${not empty incorrectFormDescription}">
        ${incorrectFormDescription}
      </c:if>

      <label>
        <fmt:message key="label.product.recipe.required"/>
        <label>
          <fmt:message key="label.yes"/>
          <input type="radio" name="isRecipeRequired" <c:if test="${param.isRecipeRequired eq true}">checked</c:if> required value="true">
        </label>
        <label>
          <fmt:message key="label.no"/>
          <input type="radio" name="isRecipeRequired" <c:if test="${param.isRecipeRequired eq false}">checked</c:if> required value="false">
        </label>
      </label>

      <c:if test="${not empty nonexisistentProduct}">
        ${nonexisistentProduct}
      </c:if>
      <button type="submit" name="command" value="edit_product"><fmt:message key="label.save"/></button>

    </form>
  </body>
</html>
