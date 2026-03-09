<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Login — Ocean View Resort</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css?v=${resourceVersion}">
</head>
<body>
  <nav class="navbar">
    <div class="nav-inner">
      <a href="${pageContext.request.contextPath}/index.jsp" class="logo">Ocean View <span>Resort</span></a>
      <ul class="nav-links">
        <li><a href="${pageContext.request.contextPath}/index.jsp">Home</a></li>
        <li><a href="${pageContext.request.contextPath}/rooms">Rooms</a></li>
        <li><a href="${pageContext.request.contextPath}/help.jsp">Help</a></li>
        <c:choose>
          <c:when test="${not empty sessionScope.user}">
            <li><a href="${pageContext.request.contextPath}/my-bookings">My Bookings</a></li>
            <li class="nav-user"><span class="nav-user-name">Hi, <c:out value="${sessionScope.user.name}"/></span><a href="${pageContext.request.contextPath}/logout" class="nav-logout">Logout</a></li>
          </c:when>
          <c:otherwise>
            <li><a href="${pageContext.request.contextPath}/login.jsp">Login</a></li>
            <li><a href="${pageContext.request.contextPath}/register.jsp" class="btn">Register</a></li>
          </c:otherwise>
        </c:choose>
      </ul>
    </div>
  </nav>

  <main class="container">
    <form id="login-form" class="form-page" action="${pageContext.request.contextPath}/login" method="post">
      <h1>Guest Login</h1>
      <c:if test="${param.admin == '1'}">
        <p class="text-center" style="color: var(--ocean-blue);">Please log in with an admin account to access the admin panel.</p>
      </c:if>
      <c:if test="${param.error == '1'}">
        <p class="error-msg text-center">Invalid email or password. Please try again.</p>
      </c:if>
      <c:if test="${param.registered == '1'}">
        <p class="text-center" style="color: var(--ocean-blue);">Registration successful. Please log in.</p>
      </c:if>
      <c:if test="${param.redirect == 'book'}">
        <p class="text-center" style="color: var(--grey);">Please log in to complete your booking.</p>
      </c:if>
      <p class="text-center" style="color: var(--grey); margin-bottom: 1rem;">Sign in to view your bookings and make reservations.</p>
      <div class="form-group">
        <label for="email">Email *</label>
        <input type="text" id="email" name="email" required placeholder="your@email.com" autocomplete="username">
      </div>
      <div class="form-group">
        <label for="password">Password *</label>
        <input type="password" id="password" name="password" required placeholder="••••••••" autocomplete="current-password">
      </div>
      <button type="submit" class="btn-primary">Login</button>
      <p class="form-footer">Don't have an account? <a href="${pageContext.request.contextPath}/register.jsp">Register here</a>.</p>
    </form>
  </main>

  <footer class="footer">
    <div class="footer-inner">
      <div><h4>Ocean View Resort</h4><p>Galle, Sri Lanka</p></div>
      <div><h4>Contact</h4><p>+94 91 2 234 567</p></div>
      <div><h4>Quick Links</h4><p><a href="${pageContext.request.contextPath}/rooms">Rooms</a></p><p><a href="${pageContext.request.contextPath}/help.jsp">Help</a></p></div>
    </div>
    <p class="footer-copy">&copy; Ocean View Resort. CIS6003.</p>
  </footer>
  <script src="${pageContext.request.contextPath}/js/app.js?v=${resourceVersion}"></script>
</body>
</html>
