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
    <title><fmt:message key="title.find.patient"/></title>
  </head>
  <body>
    <c:set var="path" value="/jsp/products.jsp" scope="session"/>
    <%@include file="../common/header.jsp"%>

    <div class="container">
      <form id="create-prescription" class="form-inline" action="${pageContext.request.contextPath}/controller" method="post">
        <input form="create-prescription" type="hidden" name="command" value="create_prescription">
        <input type="hidden" name="productId" value="${product.id}">
        <div class="form-group">
          <label for="productName"><fmt:message key="label.product.name"/>: </label>
          <input id="productName" class="form-control-plaintext" value="<c:out value="${product.name}"/>" readonly>
        </div>
        <div class="form-group">
          <label for="productPrice"><fmt:message key="label.product.price"/>: </label>
          <input id="productPrice" class="form-control-plaintext" value="<c:out value="${product.price}"/>" readonly>
        </div>
        <div class="form-group">
          <label for="productQuantity"><fmt:message key="label.product.quantity"/>: </label>
          <input id="productQuantity" class="form-control-plaintext" value="<c:out value="${product.availableQuantity}"/>" readonly>
        </div>
        <div class="form-group">
          <label for="productForm"><fmt:message key="label.product.form"/>: </label>
          <input id="productForm" class="form-control-plaintext" value="<c:out value="${product.productForm}"/>" readonly>
        </div>
        <div class="form-group">
          <label for="formDescription"><fmt:message key="label.product.form.description"/>: </label>
          <input id="formDescription" class="form-control-plaintext" value="<c:out value="${product.formDescription}"/>" readonly>
        </div>
        <div class="form-group">
          <label for="productAmount"><fmt:message key="label.prescription.amount"/>: </label>
          <input id="productAmount" class="form-control" name="productAmount" value="${productAmount}" type="number" min="1" required>
          <c:if test="${not empty incorrectAmount}">
            <c:out value="${incorrectAmount}"/>
          </c:if>
        </div>

        <jsp:useBean id="date" class="java.util.Date" scope="page"/>
        <fmt:formatDate value="${date}" var="curDate" pattern="YYYY-MM-dd"/>
        <div class="form-group">
          <label for="expirationDate"><fmt:message key="label.prescription.expiration_date"/>: </label>
          <input id="expirationDate" class="form-control" name="expirationDate" type="date" min="${curDate}" value="${expirationDate}" required>
          <c:if test="${not empty incorrectDate}">
            <c:out value="${incorrectDate}"/>
          </c:if>
        </div>
        <c:if test="${not empty prescriptionError}">
          <c:out value="${prescriptionError}"/>
        </c:if>
      </form>
    </div>

    <div class="btn-group">
      <form action="${pageContext.request.contextPath}/controller" method="post">
        <input type="hidden" name="productId" value="${product.id}">
        <label for="search-field"><fmt:message key="label.prescription.patient.email"/></label>
        <input id="search-field" type="text" name="searchText" pattern=".{0,100}"
               placeholder="<fmt:message key="placeholder.search"/>" value="<c:out value="${searchText}"/>">
        <button class="btn btn-secondary" type="submit" name="command" value="find_patient">
          <fmt:message key="label.search"/>
        </button>
      </form>
    </div>

    <c:choose>

      <c:when test="${not empty users}">
        <div>
          <table class="table table-striped">
            <thead>
              <tr>
                <th><fmt:message key="label.user.name"/></th>
                <th><fmt:message key="label.user.email"/></th>
                <th><fmt:message key="label.registration_date"/></th>
                <th><fmt:message key="label.role"/></th>
                <th><fmt:message key="label.prescription.create"/></th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="user" items="${users}">
                <tr>
                  <td><c:out value="${user.name}"/></td>

                  <td><c:out value="${user.email}"/></td>

                  <td><c:out value="${user.registrationDate}"/></td>

                  <td><c:out value="${user.role}"/></td>

                  <td>
                    <button form="create-prescription" type="submit" name="patientId" value="${user.id}">
                      <fmt:message key="label.prescription.create"/>
                    </button>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
      </c:when>

      <c:when test="${users ne null}">
        <fmt:message key="message.products.not_found"/>
      </c:when>

    </c:choose>
  </body>
</html>
