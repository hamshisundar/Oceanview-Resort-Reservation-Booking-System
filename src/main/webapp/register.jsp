<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Register — Ocean View Resort</title>
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
    <form id="register-form" class="form-page" action="${pageContext.request.contextPath}/register" method="post">
      <h1>Create account</h1>
      <c:if test="${param.error == 'validation'}">
        <p class="error-msg text-center">Please fill in Full name, Email, and Password.</p>
      </c:if>
      <c:if test="${param.error == 'email'}">
        <p class="error-msg text-center">This email is already registered. Please <a href="${pageContext.request.contextPath}/login.jsp">log in</a> or use a different email address.</p>
      </c:if>
      <c:if test="${param.error == '1'}">
        <p class="error-msg text-center">Registration failed. Please try again. <a href="${pageContext.request.contextPath}/db-status">Check database status</a> if it keeps failing.</p>
      </c:if>
      <p class="text-center" style="color: var(--grey); margin-bottom: 1rem;">Register to book rooms and manage your reservations.</p>
      <div class="form-group">
        <label for="name">Full name *</label>
        <input type="text" id="name" name="name" required placeholder="Your full name">
      </div>
      <div class="form-group">
        <label for="email">Email *</label>
        <input type="email" id="email" name="email" required placeholder="your@email.com" autocomplete="email">
      </div>
      <div class="form-group">
        <label for="phone">Phone</label>
        <input type="tel" id="phone" name="phone" placeholder="+94 7x xxx xxxx">
      </div>
      <div class="form-group">
        <label for="password">Password *</label>
        <input type="password" id="password" name="password" required placeholder="••••••••" minlength="6" autocomplete="new-password">
      </div>
      <div class="form-group">
        <label for="password-confirm">Confirm password *</label>
        <input type="password" id="password-confirm" name="passwordConfirm" required placeholder="••••••••" autocomplete="new-password">
      </div>
      <button type="submit" class="btn-primary">Register</button>
      <p class="form-footer">Already have an account? <a href="${pageContext.request.contextPath}/login.jsp">Login here</a>.</p>
      <p class="text-center" style="margin-top:1rem;font-size:0.75rem;color:var(--grey);">Ocean View v<c:out value="${applicationScope.appVersion}" default="?"/></p>
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
  <script>
    (function () {
      var form = document.getElementById('register-form');
      if (!form) return;
      form.addEventListener('submit', function (e) {
        var p1 = document.getElementById('password');
        var p2 = document.getElementById('password-confirm');
        if (p1 && p2 && p1.value !== p2.value) {
          e.preventDefault();
          if (typeof OceanView !== 'undefined') OceanView.showFieldError('password-confirm', 'Passwords do not match.');
          return false;
        }
        // Allow form to submit to server; server validates required fields and DB
      });
    })();
  </script>
</body>
</html>
