<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.content"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="../../css/bootstrap.css" rel="stylesheet">
    <title><fmt:message key="title.prescriptions"/></title>
  </head>
  <body>
    <c:set var="path" value="/jsp/prescriptions.jsp" scope="session"/>
    <%@include file="../common/header.jsp"%>

    <div class="btn-group">
      <form name="actual_prescriptions" action="${pageContext.request.contextPath}/controller" method="post">
        <input type="hidden" name="isActual" value="true">
        <button type="submit" name="command" value="show_prescriptions"><fmt:message key="label.prescription.actual"/></button>
      </form>
      <form name="past_prescriptions" action="${pageContext.request.contextPath}/controller" method="post">
        <input type="hidden" name="isActual" value="false">
        <button type="submit" name="command" value="show_prescriptions"><fmt:message key="label.prescription.past"/></button>
      </form>
    </div>

    <c:choose>

      <c:when test="${not empty prescriptions}">
        <div>
          <table class="table table-striped">
            <thead>
              <tr>
                <th><fmt:message key="label.prescription.product.name"/></th>
                <th><fmt:message key="label.prescription.amount"/></th>
                <th><fmt:message key="label.prescription.doctor.name"/></th>
                <th><fmt:message key="label.prescription.doctor.email"/></th>
                <th><fmt:message key="label.prescription.expiration_date"/></th>

                <jsp:useBean id="date" class="java.util.Date" scope="page"/>

                <c:choose>
                  <c:when test="${!prescriptions[0].isUsed and prescriptions[0].expirationDate >= date}">
                    <th><fmt:message key="label.product.quantity"/></th>
                    <th><fmt:message key="label.product.price"/></th>
                  </c:when>

                  <c:otherwise>
                    <th><fmt:message key="label.prescription.invalidity.cause"/></th>
                  </c:otherwise>
                </c:choose>

              </tr>
            </thead>
            <tbody>
              <c:forEach var="prescription" items="${prescriptions}" varStatus="i">

                <form id="${prescription.productId} add-to-cart" action="${pageContext.request.contextPath}/controller" method="post">
                  <input type="hidden" name="productId" value="<c:out value="${prescription.productId}"/>">
                </form>

                <tr>
                  <td><c:out value="${prescription.productName}"/></td>

                  <td><c:out value="${prescription.amount}"/></td>

                  <td><c:out value="${prescription.doctorName}"/></td>

                  <td><c:out value="${prescription.doctorEmail}"/></td>

                  <td><c:out value="${prescription.expirationDate}"/></td>

                  <c:choose>
                    <c:when test="${!prescription.isUsed and prescription.expirationDate >= date}">
                      <c:choose>
                        <c:when test="${prescription.isProductAvailable}">
                          <td><c:out value="${prescription.productPrice}"/></td>
                          <td><c:out value="${prescription.productQuantity}"/></td>
                          <td>
                            <label for="amount-field"><fmt:message key="label.amount"/></label>
                            <input id="amount-field" form="${prescription.productId} add-to-cart" type="number" name="addedAmount"
                                   pattern="[1-9]{1}[0-9]*" min="1" max="${prescription.amount}"/>
                            <button form="${prescription.productId} add-to-cart" type="submit" name="command" value="add_to_cart">
                              <fmt:message key="label.cart.add"/>
                            </button>
                          </td>
                        </c:when>

                        <c:otherwise>
                          <td>--</td>
                          <td>--</td>
                          <td><fmt:message key="label.product.not_available"/></td>
                        </c:otherwise>
                      </c:choose>
                    </c:when>

                    <c:otherwise>
                      <c:choose>
                        <c:when test="${prescription.isUsed}">
                          <td><fmt:message key="label.prescription.used"/></td>
                        </c:when>

                        <c:otherwise>
                          <td><fmt:message key="label.prescription.overdue"/></td>
                        </c:otherwise>
                      </c:choose>
                    </c:otherwise>
                  </c:choose>
                </tr>
              </c:forEach>
            </tbody>
          </table>
            </div>
        </c:when>

        <c:when test="${prescriptions ne null}">
          <fmt:message key="message.prescriptions.not_found"/>
        </c:when>

    </c:choose>
  </body>
</html>
