<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="../../css/bootstrap.css" rel="stylesheet">
    <title><fmt:message key="title.cart"/></title>
  </head>
  <body>
    <%--<c:set var="path" value="/jsp/user/orders.jsp" scope="session"/>--%>
    <%@include file="../common/header.jsp"%>
    <fmt:message key="message.cart"/>
    <c:if test="${not empty message}">
      <c:out value="${message}"/>
    </c:if>
    <div>
      <table class="table table-striped">
        <thead>
          <tr>
            <th><fmt:message key="label.product.name"/></th>
            <th><fmt:message key="label.product.quantity"/></th>
            <th><fmt:message key="label.product.form"/></th>
            <th><fmt:message key="label.product.form.description"/></th>
            <th><fmt:message key="label.product.prescription.required"/></th>
            <th><fmt:message key="label.product.price"/></th>
            <th><fmt:message key="label.product.amount"/></th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="entry" items="${cartProducts}">
            <form id="${entry.key.id} update delete" action="${pageContext.request.contextPath}/controller" method="post">
              <input type="hidden" name="productId" value="<c:out value="${entry.key.id}"/>">
            </form>
            <tr>
              <td><c:out value="${entry.key.name}"/></td>

              <td><c:out value="${entry.key.availableQuantity}"/></td>

              <td><c:out value="${entry.key.productForm}"/></td>

              <td><c:out value="${entry.key.formDescription}"/></td>

              <td>
                <c:choose>
                  <c:when test="${entry.key.isPrescriptionRequired eq true}">
                    <fmt:message key="label.yes"/>
                  </c:when>
                  <c:otherwise>
                    <fmt:message key="label.no"/>
                  </c:otherwise>
                </c:choose>
              </td>

              <td><c:out value="${entry.key.price}"/></td>

                <c:choose>
                  <c:when test="${cart.products.containsKey(entry.key.id) eq true}">
                    <td>
                      <input form="${entry.key.id} update delete" type="number" name="newAmount"
                             pattern="[1-9]{1}[0-9]*" min="1" max="${entry.key.availableQuantity}" value="${cart.products.get(entry.key.id)}" required/>
                      <button form="${entry.key.id} update delete" type="submit" name="command" value="update_cart_product">
                        <fmt:message key="label.cart.update"/>
                      </button>
                      <c:if test="${not empty entry.value}">
                        <c:out value="${entry.value}"/>
                      </c:if>
                    </td>
                    <td>
                      <button form="${entry.key.id} update delete" type="submit" name="command" value="remove_from_cart">
                        <fmt:message key="label.cart.remove"/>
                      </button>
                    </td>
                  </c:when>

                  <c:otherwise>
                    <td>
                      <c:if test="${not empty entry.value}">
                        <c:out value="${entry.value}"/>
                      </c:if>
                    </td>
                    <td>
                      <button form="${entry.key.id} update delete" type="submit" name="command" value="remove_from_cart" disabled>
                        <fmt:message key="label.cart.removed"/>
                      </button>
                    </td>
                  </c:otherwise>
                </c:choose>

            </tr>
          </c:forEach>
        </tbody>
      </table>
      <c:choose>
        <c:when test="${not empty cart.products}">
          <label><fmt:message key="label.cart.total"/>: <c:out value="${cart.total}"/></label>
          <form action="${pageContext.request.contextPath}/controller" method="post">
            <button class="btn btn-primary" type="submit" name="command" value="checkout">
              <fmt:message key="label.cart.checkout"/>
            </button>
          </form>
        </c:when>

        <c:otherwise>
          <label><fmt:message key="message.cart.empty"/></label>
        </c:otherwise>
      </c:choose>

    </div>
  </body>
</html>
