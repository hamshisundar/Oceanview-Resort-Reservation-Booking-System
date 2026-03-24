<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin Login · Ocean View</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-login.css?v=${resourceVersion}">
</head>
<body class="admin-login-page">
  <div class="admin-login-card">
    <h1>Admin Login</h1>
    <p class="sub">Ocean View Resort — administration</p>
    <c:if test="${not empty error}">
      <div class="admin-login-err"><c:out value="${error}"/></div>
    </c:if>
    <form action="${pageContext.request.contextPath}/admin/login" method="post">
      <div class="admin-login-field">
        <label for="email">Email</label>
        <input id="email" name="email" type="email" autocomplete="username" required placeholder="admin@oceanview.com">
      </div>
      <div class="admin-login-field">
        <label for="password">Password</label>
        <input id="password" name="password" type="password" autocomplete="current-password" required>
      </div>
      <button type="submit" class="admin-login-submit">Sign in</button>
    </form>
    <p class="admin-login-foot">Guest access? <a href="${pageContext.request.contextPath}/login.jsp">Customer login</a></p>
  </div>
</body>
</html>
