<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
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

        <form name="products" action="${pageContext.request.contextPath}/jsp/products.jsp" method="post">
          <button type="submit" class="btn btn-secondary"><fmt:message key="label.products"/></button>
        </form>

        <form name="orders" action="${pageContext.request.contextPath}/controller" method="post">
          <button type="submit" class="btn btn-secondary" name="command" value="orders"><fmt:message key="label.orders"/></button>
        </form>

        <form name="recipes" action="${pageContext.request.contextPath}/controller" method="post">
          <button type="submit" class="btn btn-secondary" name="command" value="recipes"><fmt:message key="label.recipes"/></button>
        </form>

        <form name="sign_out" action="${pageContext.request.contextPath}/controller" method="post">
          <button type="submit" class="btn btn-secondary" name="command" value="sign_out"><fmt:message key="label.sign_out"/></button>
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
            <fmt:message key="label.sign_in"/>
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
  <div class="btn-group">
    <form name="en_lang" action="${pageContext.request.contextPath}/controller" method="post">
      <input type="hidden" name="language" value="en-US">
      <button type="submit" name="command" value="change_language">English</button>
    </form>
    <form name="ru_lang" action="${pageContext.request.contextPath}/controller" method="post">
      <input type="hidden" name="language" value="ru-RU">
      <button type="submit" name="command" value="change_language">Русский</button>
    </form>
  </div>
</nav>
