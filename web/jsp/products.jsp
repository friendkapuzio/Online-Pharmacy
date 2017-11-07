<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.content"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="../css/bootstrap.css" rel="stylesheet">
    <title><fmt:message key="title.products"/></title>
  </head>
  <body>
    <c:set var="path" value="/jsp/products.jsp" scope="session"/>
    <%@include file="common/header.jsp"%>

    <div class="btn-group">
      <form id="search" action="${pageContext.request.contextPath}/controller" method="post">
        <label for="search-field"><fmt:message key="label.product.name"/></label>
        <input id="search-field" type="text" name="searchText" pattern=".{0,100}"
               placeholder="<fmt:message key="placeholder.search"/>" value="<c:out value="${searchText}"/>">
      </form>
      <button class="btn btn-secondary" form="search" type="submit" name="command" value="search_product">
        <fmt:message key="label.search"/>
      </button>

      <ctg:pharmacist>
        <button class="btn btn-secondary" form="search" type="submit" name="command" value="search_deleted_product">
          <fmt:message key="label.search.deleted"/>
        </button>
        <form name="add_product" action="${pageContext.request.contextPath}/jsp/pharmacist/add_product.jsp" method="post">
          <button class="btn btn-secondary" type="submit"><fmt:message key="label.product.add"/></button>
        </form>
      </ctg:pharmacist>
    </div>

    <c:choose>

      <c:when test="${not empty products}">
        <div>
          <table class="table table-striped">
            <thead>
            <tr>
              <th><fmt:message key="label.product.name"/></th>
              <th><fmt:message key="label.product.price"/></th>
              <th><fmt:message key="label.product.quantity"/></th>
              <th><fmt:message key="label.product.form"/></th>
              <th><fmt:message key="label.product.form.description"/></th>
              <th><fmt:message key="label.product.prescription.required"/></th>
              <ctg:pharmacist>
                <th><fmt:message key="label.edit"/></th>
                <th>
                  <form id="delete-restore" action="${pageContext.request.contextPath}/controller" method="post">
                    <c:choose>

                      <c:when test="${!products[0].isDeleted}">
                        <button type="submit" name="command" value="delete_products">
                          <fmt:message key="label.delete"/>
                        </button>
                      </c:when>

                      <c:otherwise>
                        <button type="submit" name="command" value="restore_products">
                          <fmt:message key="label.restore"/>
                        </button>
                      </c:otherwise>

                    </c:choose>

                  </form>
                </th>
              </ctg:pharmacist>

              <ctg:doctor>
                <th><fmt:message key="label.prescription.create"/></th>
              </ctg:doctor>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="product" items="${products}">

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

                <ctg:pharmacist>
                  <td>
                    <form id="${product.id} edit" action="${pageContext.request.contextPath}/jsp/pharmacist/edit_product.jsp" method="post">
                      <input type="hidden" name="productId" value="<c:out value="${product.id}"/>">
                      <input type="hidden" name="productName" value="<c:out value="${product.name}"/>">
                      <input type="hidden" name="productPrice" value="<c:out value="${product.price}"/>">
                      <input type="hidden" name="productQuantity" value="<c:out value="${product.availableQuantity}"/>">
                      <input type="hidden" name="productForm" value="<c:out value="${product.productForm}"/>">
                      <input type="hidden" name="formDescription" value="<c:out value="${product.formDescription}"/>">
                      <input type="hidden" name="isPrescriptionRequired" value="<c:out value="${product.isPrescriptionRequired}"/>">
                      <input type="hidden" name="isDeleted" value="<c:out value="${product.isDeleted}"/>">
                      <button type="submit">
                        <fmt:message key="label.edit"/>
                      </button>
                    </form>
                  </td>
                  <td>
                    <input form="delete-restore" type="checkbox" name="products" value="${product.id}">
                  </td>
                </ctg:pharmacist>

                <ctg:doctor>
                  <td>
                    <c:choose>
                      <c:when test="${!product.isPrescriptionRequired}">
                        <button type="button" disabled>
                          <fmt:message key="label.prescription.not.required"/>
                        </button>
                      </c:when>

                      <c:otherwise>
                        <form id="${product.id} create-prescription" action="${pageContext.request.contextPath}/controller" method="post">
                          <input type="hidden" name="productId" value="<c:out value="${product.id}"/>">
                          <button type="submit" name="command" value="create_prescription_form">
                            <fmt:message key="label.prescription.create"/>
                          </button>
                        </form>
                      </c:otherwise>
                    </c:choose>
                  </td>
                </ctg:doctor>

                <c:if test="${not empty userId and !product.isDeleted}">
                  <td>
                    <form id="${product.id} add-to-cart" action="${pageContext.request.contextPath}/controller" method="post">
                      <input type="hidden" name="productId" value="<c:out value="${product.id}"/>">
                      <label for="amount-field"><fmt:message key="label.amount"/></label>
                      <input id="amount-field" type="number" name="addedAmount"
                             pattern="[1-9]{1}[0-9]*" min="1" max="${product.availableQuantity}" required/>
                      <button type="submit" name="command" value="add_to_cart">
                        <fmt:message key="label.cart.add"/>
                      </button>
                    </form>
                  </td>
                </c:if>
              </tr>
            </c:forEach>
            </tbody>
          </table>
        </div>
      </c:when>

      <c:when test="${products ne null}">
        <fmt:message key="message.products.not_found"/>
      </c:when>

    </c:choose>
  </body>
</html>
