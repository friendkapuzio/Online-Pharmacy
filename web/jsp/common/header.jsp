<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.content"/>
<nav class="navbar sticky-top navbar-light" style="background-color: #96fd4b;">
  <div class="container">
    <div class="row">
      <div class="col-sm">
  <a class="navbar-brand" href="${pageContext.request.contextPath}/index.jsp">
    <img src="../../images/miniLogo.png" alt="">
  </a>
      </div>
  <c:choose>

    <c:when test="${not empty username}">
      <div class="col-sm">
        <span class="navbar-text">
          <fmt:message key="message.hello"/>, ${username}.
        </span>
      </div>

      <div class="col-sm">
        <form class="form-inline" name="home" action="${pageContext.request.contextPath}/index.jsp" method="post">
          <button type="submit" class="btn btn-secondary"><fmt:message key="label.home"/></button>
        </form>
      </div>

      <div class="col-sm">
        <form class="form-inline" name="account" action="${pageContext.request.contextPath}/controller" method="post">
          <button type="submit" class="btn btn-secondary" name="command" value="account"><fmt:message key="label.account"/></button>
        </form>
      </div>

      <div class="col-sm">
        <form class="form-inline" name="products" action="${pageContext.request.contextPath}/jsp/products.jsp" method="post">
          <button type="submit" class="btn btn-secondary"><fmt:message key="label.products"/></button>
        </form>
      </div>

      <div class="col-sm">
      <form class="form-inline" name="orders" action="${pageContext.request.contextPath}/jsp/user/orders.jsp" method="post">
          <button type="submit" class="btn btn-secondary"><fmt:message key="label.orders"/></button>
        </form>
      </div>

      <div class="col-sm">
      <form class="form-inline" name="prescriptions" action="${pageContext.request.contextPath}/jsp/user/prescriptions.jsp" method="post">
          <button type="submit" class="btn btn-secondary"><fmt:message key="label.prescriptions"/></button>
        </form>
      </div>
        <ctg:doctor>
      <div class="col-sm">
      <form class="form-inline" name="users" action="${pageContext.request.contextPath}/controller" method="post">
            <button type="submit" class="btn btn-secondary" name="command" value="show_given_prescriptions"><fmt:message key="label.prescriptions.manage"/></button>
          </form>
      </div>
        </ctg:doctor>

        <ctg:admin>
      <div class="col-sm">
      <form class="form-inline" name="users" action="${pageContext.request.contextPath}/jsp/admin/users.jsp" method="post">
            <button type="submit" class="btn btn-secondary"><fmt:message key="label.users"/></button>
          </form>
      </div>

      <div class="col-sm">
      <form class="form-inline" name="manage orders" action="${pageContext.request.contextPath}/jsp/admin/manage_orders.jsp" method="post">
            <button type="submit" class="btn btn-secondary"><fmt:message key="label.orders.manage"/></button>
          </form>
      </div>
        </ctg:admin>

      <div class="col-sm">
        <form class="form-inline" name="sign_out" action="${pageContext.request.contextPath}/controller" method="post">
          <button type="submit" class="btn btn-secondary" name="command" value="sign_out"><fmt:message key="label.sign.out"/></button>
        </form>
      </div>

      <div class="col-sm">
      <form class="form-inline" name="cart" action="${pageContext.request.contextPath}/controller" method="post">
          <button type="submit" class="btn btn-secondary" name="command" value="cart">
            <fmt:message key="label.cart"/>: <c:out value="${cart.total}"/>
          </button>
        </form>
      </div>
    </c:when>

    <c:otherwise>
      <div class="col-sm-auto">
      <form class="form-inline" name="sign_in" action="${pageContext.request.contextPath}/controller" method="post">
          <input class="form-control" id="email" type="email" name="email" pattern=".+@[a-z]{1,10}\.[a-z]{1,10}" placeholder="email@example.com" required>

          <input class="form-control" id="password" type="password" name="password" pattern=".{6,32}"
                 placeholder="<fmt:message key="placeholder.password"/>" required>

          <button type="submit" class="btn btn-secondary" name="command" value="sign_in">
            <fmt:message key="label.sign.in"/>
          </button>
        </form>
        <c:out value="${signInErrorMessage}"/>
      </div>

      <div class="col-sm">
      <form class="form-inline" name="register" action="${pageContext.request.contextPath}/jsp/registration.jsp" method="post">
          <button type="submit" class="btn btn-secondary"><fmt:message key="label.register"/></button>
        </form>
      </div>

      <div class="col-sm">
      <form class="form-inline" name="home" action="${pageContext.request.contextPath}/index.jsp" method="post">
          <button type="submit" class="btn btn-secondary"><fmt:message key="label.home"/></button>
        </form>
      </div>

      <div class="col-sm">
        <form class="form-inline" name="products" action="${pageContext.request.contextPath}/jsp/products.jsp" method="post">
          <button type="submit" class="btn btn-secondary"><fmt:message key="label.products"/></button>
        </form>
      </div>

    </c:otherwise>

  </c:choose>
    </div>
  </div>
  <%--${pageContext.request.requestURI}--%>
  <%--<br>--%>
  <%--${pageContext.request.requestURL}--%>
  <%--<br>--%>
  <%--${pageContext.request.queryString}--%>
  <%--<br>--%>
  <%--${requestScope['javax.servlet.forward.request_uri']}--%>
</nav>
