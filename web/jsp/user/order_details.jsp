<div class="modal-dialog modal-lg" style="max-width: 1000px" role="document">
  <div class="modal-content">
    <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      <h4 class="modal-title" id="${order.id}"><fmt:message key="label.order.number"/>#${order.id} </h4>
    </div>
    <div class="modal-body">

    <div class="container">
      <form id="find-patient create-prescription" class="form-inline" action="${pageContext.request.contextPath}/controller" method="post">
        <div class="form-group">
          <label for="orderId"><fmt:message key="label.order.number"/>: </label>
          <input id="orderId" class="form-control-plaintext" value="<c:out value="${order.id}"/>" readonly>
        </div>
        <div class="form-group">
          <label for="placementDate"><fmt:message key="label.order.placement.date"/>: </label>
          <input id="placementDate" class="form-control-plaintext" value="<c:out value="${order.placementDate}"/>" readonly>
        </div>
        <div class="form-group">
          <label for="productsAmount"><fmt:message key="label.order.products.amount"/>: </label>
          <input id="productsAmount" class="form-control-plaintext" value="<c:out value="${fn:length(order.products)}"/>" readonly>
        </div>
        <div class="form-group">
          <label for="totalPrice"><fmt:message key="label.order.price.total"/>: </label>
          <input id="totalPrice" class="form-control-plaintext" value="<c:out value="${order.totalPrice}"/>" readonly>
        </div>
        <div class="form-group">
          <label for="orderStatus"><fmt:message key="label.order.status"/>: </label>
          <input id="orderStatus" class="form-control-plaintext" value="<c:out value="${order.status}"/>" readonly>
        </div>
      </form>
    </div>

    <div style="overflow-x: auto">
      <table class="table table-striped">
        <thead>
          <tr>
            <th><fmt:message key="label.product.name"/></th>
            <th><fmt:message key="label.product.price"/></th>
            <th><fmt:message key="label.product.form"/></th>
            <th><fmt:message key="label.product.form.description"/></th>
            <th><fmt:message key="label.product.prescription.required"/></th>
            <th><fmt:message key="label.product.amount"/></th>
          </tr>
        </thead>
        <tbody>
        <c:forEach var="product" items="${order.products}">
          <tr>
            <td><c:out value="${product.name}"/></td>

            <td><c:out value="${product.price}"/></td>

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

            <td><c:out value="${product.amount}"/></td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
    </div>
  </div>
</div>
</div>
