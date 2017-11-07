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
    <title><fmt:message key="title.prescriptions.manage"/></title>
  </head>
  <body>
    <%@include file="../common/header.jsp"%>

    <div class="container">
      <c:choose>

        <c:when test="${not empty prescriptions}">
          <div>
            <table class="table table-striped">
              <thead>
                <tr>
                  <th><fmt:message key="label.prescription.product.name"/></th>
                  <th><fmt:message key="label.prescription.amount"/></th>
                  <th><fmt:message key="label.prescription.patient.name"/></th>
                  <th><fmt:message key="label.prescription.patient.email"/></th>
                  <th><fmt:message key="label.prescription.expiration_date"/></th>
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

                  <td><c:out value="${prescription.patientName}"/></td>

                  <td><c:out value="${prescription.patientEmail}"/></td>

                  <td><c:out value="${prescription.expirationDate}"/></td>

                  <c:choose>
                    <c:when test="${prescription.isUsed}">
                      <td><fmt:message key="label.prescription.used"/></td>
                    </c:when>

                    <c:otherwise>
                      <td>
                        <jsp:useBean id="date" class="java.util.Date" scope="page"/>
                        <fmt:formatDate value="${date}" var="curDate" pattern="YYYY-MM-dd"/>
                        <form id="change expiration date" class="form-inline" action="${pageContext.request.contextPath}/controller" method="post">
                          <div class="form-group">
                            <label for="expirationDate"><fmt:message key="label.prescription.expiration_date.new"/>: </label>
                            <input type="hidden" name="prescriptionId" value="<c:out value="${prescription.id}"/>">
                            <input id="expirationDate" class="form-control" name="newExpirationDate" type="date" min="${curDate}"
                                   value="<c:out value="${prescription.expirationDate}"/>" required>
                          </div>
                          <button type="submit" name="command" value="change_expiration_date"><fmt:message key="label.change"/></button>
                        </form>
                        <c:if test="${prescriptionId eq prescription.id}">
                          <c:if test="${not empty prescriptionError}">
                            <c:out value="${prescriptionError}"/>
                          </c:if>

                          <c:if test="${not empty incorrectDate}">
                            <c:out value="${incorrectDate}"/>
                          </c:if>
                        </c:if>
                      </td>
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
    </div>
  </body>
</html>
