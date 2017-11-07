<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.content"/>
<nav>
  <c:choose>

    <c:when test="${not empty username}">
      <div class="btn-group">

        <fmt:message key="message.hello"/>, ${username}.

        <form name="home" action="${pageContext.request.contextPath}/index.jsp" method="post">
          <button type="submit" class="btn btn-secondary"><fmt:message key="label.home"/></button>
        </form>

        <form name="account" action="${pageContext.request.contextPath}/controller" method="post">
          <button type="submit" class="btn btn-secondary" name="command" value="account"><fmt:message key="label.account"/></button>
        </form>

        <form name="products" action="${pageContext.request.contextPath}/jsp/products.jsp" method="post">
          <button type="submit" class="btn btn-secondary"><fmt:message key="label.products"/></button>
        </form>

        <form name="orders" action="${pageContext.request.contextPath}/jsp/user/orders.jsp" method="post">
          <button type="submit" class="btn btn-secondary"><fmt:message key="label.orders"/></button>
        </form>

        <form name="prescriptions" action="${pageContext.request.contextPath}/jsp/user/prescriptions.jsp" method="post">
          <button type="submit" class="btn btn-secondary"><fmt:message key="label.prescriptions"/></button>
        </form>

        <ctg:doctor>
          <form name="users" action="${pageContext.request.contextPath}/controller" method="post">
            <button type="submit" class="btn btn-secondary" name="command" value="show_given_prescriptions"><fmt:message key="label.prescriptions.manage"/></button>
          </form>
        </ctg:doctor>

        <ctg:admin>
          <form name="users" action="${pageContext.request.contextPath}/jsp/admin/users.jsp" method="post">
            <button type="submit" class="btn btn-secondary"><fmt:message key="label.users"/></button>
          </form>
          <form name="manage orders" action="${pageContext.request.contextPath}/jsp/admin/manage_orders.jsp" method="post">
            <button type="submit" class="btn btn-secondary"><fmt:message key="label.orders.manage"/></button>
          </form>
        </ctg:admin>

        <form name="sign_out" action="${pageContext.request.contextPath}/controller" method="post">
          <button type="submit" class="btn btn-secondary" name="command" value="sign_out"><fmt:message key="label.sign.out"/></button>
        </form>

        <form name="cart" action="${pageContext.request.contextPath}/controller" method="post">
          <button type="submit" class="btn btn-secondary" name="command" value="cart">
            <fmt:message key="label.cart"/>: <c:out value="${cart.total}"/>
          </button>
        </form>

      </div>
    </c:when>

    <c:otherwise>
      <div class="btn-group">

        <form name="sign_in" action="${pageContext.request.contextPath}/controller" method="post">
          <label>
            <fmt:message key="label.email"/>:
            <input type="email" name="email" pattern=".+@[a-z]{1,10}\.[a-z]{1,10}" placeholder="email@example.com" required>
          </label>
          <label>
            <fmt:message key="label.password"/>:
            <input type="password" name="password" pattern=".{6,32}" required>
          </label>
          <button type="submit" class="btn btn-secondary" name="command" value="sign_in">
            <fmt:message key="label.sign.in"/>
          </button>
        </form>
        ${signInErrorMessage}

        <form name="register" action="${pageContext.request.contextPath}/jsp/registration.jsp" method="post">
          <button type="submit" class="btn btn-secondary"><fmt:message key="label.register"/></button>
        </form>

        <form name="home" action="${pageContext.request.contextPath}/index.jsp" method="post">
          <button type="submit" class="btn btn-secondary"><fmt:message key="label.home"/></button>
        </form>

        <form name="products" action="${pageContext.request.contextPath}/jsp/products.jsp" method="post">
          <button type="submit" class="btn btn-secondary"><fmt:message key="label.products"/></button>
        </form>

      </div>
    </c:otherwise>

  </c:choose>

  <%--${pageContext.request.requestURI}--%>
  <%--<br>--%>
  <%--${pageContext.request.requestURL}--%>
  <%--<br>--%>
  <%--${pageContext.request.queryString}--%>
  <%--<br>--%>
  <%--${requestScope['javax.servlet.forward.request_uri']}--%>
</nav>
