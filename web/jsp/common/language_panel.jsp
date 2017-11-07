<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
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