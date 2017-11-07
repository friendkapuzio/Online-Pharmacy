<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="resources.content"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href="../../css/bootstrap.css" rel="stylesheet">
    <title><fmt:message key="title.users"/></title>
  </head>
  <body>
    <c:set var="path" value="/jsp/admin/users.jsp" scope="session"/>
    <%@include file="../common/header.jsp"%>

    <div class="btn-group">
      <form id="search" action="${pageContext.request.contextPath}/controller" method="post">
        <label>
          <fmt:message key="label.user.email"/>
          <input type="text" name="searchText" pattern=".{0,100}" placeholder="<fmt:message key="placeholder.search"/>"
                 value="<c:out value="${searchText}"/>">
        </label>
      </form>
      <button class="btn btn-secondary" form="search" type="submit" name="command" value="search_user">
        <fmt:message key="label.search"/>
      </button>

      <button class="btn btn-secondary" form="search" type="submit" name="command" value="search_blocked_user">
        <fmt:message key="label.search.blocked"/>
      </button>

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
              <th><fmt:message key="label.balance"/></th>
              <th><fmt:message key="label.role.change"/></th>
              <th>
                  <c:choose>

                    <c:when test="${!users[0].isBlocked}">
                      <fmt:message key="label.block"/>
                    </c:when>

                    <c:otherwise>
                      <fmt:message key="label.unblock"/>
                    </c:otherwise>

                  </c:choose>
              </th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="user" items="${users}">
              <form id="${user.id} blocking" action="${pageContext.request.contextPath}/controller" method="post">
                <input type="hidden" name="userId" value="<c:out value="${user.id}"/>">
              </form>

              <tr>
                <td><c:out value="${user.name}"/></td>

                <td><c:out value="${user.email}"/></td>

                <td><c:out value="${user.registrationDate}"/></td>

                <td><fmt:message key="label.user.role.${user.role.toString().toLowerCase()}"/></td>

                <td><c:out value="${user.balance}"/></td>

                <td>
                  <form action="${pageContext.request.contextPath}/controller" method="post">
                    <input type="hidden" name="userId" value="<c:out value="${user.id}"/>">
                    <select name="newRole">
                      <option value="user"><fmt:message key="label.user.role.user"/></option>
                      <option value="pharmacist"><fmt:message key="label.user.role.pharmacist"/></option>
                      <option value="doctor"><fmt:message key="label.user.role.doctor"/></option>
                      <option value="administrator"><fmt:message key="label.user.role.administrator"/></option>
                    </select>
                    <button type="submit" name="command" value="change_user_role">
                      <fmt:message key="label.change"/>
                    </button>
                  </form>
                </td>

                <td>
                  <c:choose>

                    <c:when test="${!users[0].isBlocked}">
                      <button form="${user.id} blocking" type="submit" name="command" value="block_user">
                        <fmt:message key="label.block"/>
                      </button>
                    </c:when>

                    <c:otherwise>
                      <button form="${user.id} blocking" type="submit" name="command" value="unblock_user">
                        <fmt:message key="label.unblock"/>
                      </button>
                    </c:otherwise>

                  </c:choose>
                </td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
        </div>
      </c:when>

      <c:when test="${users ne null}">
        <fmt:message key="message.users.not_found"/>
      </c:when>

    </c:choose>
  </body>
</html>
