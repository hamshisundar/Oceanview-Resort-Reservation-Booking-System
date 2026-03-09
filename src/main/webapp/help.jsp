<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Help — Ocean View Resort</title>
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

  <header class="hero">
    <div class="hero-content">
      <h1>Help &amp; guidelines</h1>
      <p>How to use the Ocean View reservation system — for guests and new staff</p>
    </div>
  </header>

  <main class="container section">
    <div class="help-content">
      <p class="help-intro">The system provides the following functionalities in this order:</p>

      <h2>1. User authentication (login)</h2>
      <p>Secure system access requires a <strong>username</strong> and <strong>password</strong>. Use <strong>Login</strong> and sign in with your <strong>email</strong> (as username) and password. New guests can <strong>Register</strong> to create an account. Keep your password secure.</p>

      <h2>2. Add new reservation</h2>
      <p>Store guest and booking details: reservation number, guest name, contact number, room type, check-in and check-out dates. From <strong>Rooms</strong>, choose a room and open its details. Select check-in and check-out dates and click <strong>Check availability &amp; book</strong>. Confirm to create the reservation; the system generates a unique reservation number (e.g. OV-YYYYMMDD-XXX).</p>

      <h2>3. Display reservation details</h2>
      <p>Retrieve and view complete booking information for a specific reservation. After booking, the <strong>confirmation page</strong> shows the reservation number, guest name, room, dates, and total cost. From <strong>My Bookings</strong> you can open each reservation to see full details.</p>

      <h2>4. Calculate and print bill</h2>
      <p>The total stay cost is computed from the <strong>number of nights</strong> and <strong>room rate per night</strong>. The confirmation page and booking history show the total. Use your browser’s <strong>Print</strong> option (e.g. Ctrl+P / Cmd+P) or the <strong>Print bill</strong> link on the confirmation page to print the bill.</p>

      <h2>5. Help section</h2>
      <p>This page provides guidelines on how to use the reservation system for new staff members. Share this link with colleagues: <a href="${pageContext.request.contextPath}/help.jsp">Help</a>.</p>

      <h2>6. Exit system</h2>
      <p>To leave the system safely, use <strong>Logout</strong> in the menu. This ends your session. Closing the browser may also end the session after a period of inactivity.</p>

      <hr class="help-divider">
      <h3>Quick reference</h3>
      <ul>
        <li><strong>Rooms</strong> — Browse and filter by type, price, sea view.</li>
        <li><strong>Room details</strong> — View amenities and book with chosen dates.</li>
        <li><strong>My Bookings</strong> — View all your reservations (after login).</li>
      </ul>
      <p><strong>Contact:</strong> <a href="mailto:info@oceanview.lk">info@oceanview.lk</a>, Tel: +94 91 2 234 567.</p>
    </div>
  </main>

  <footer class="footer">
    <div class="footer-inner">
      <div><h4>Ocean View Resort</h4><p>Galle, Sri Lanka</p></div>
      <div><h4>Contact</h4><p>+94 91 2 234 567</p><p><a href="mailto:info@oceanview.lk">info@oceanview.lk</a></p></div>
      <div><h4>Quick Links</h4><p><a href="${pageContext.request.contextPath}/rooms">Rooms</a></p><p><a href="${pageContext.request.contextPath}/help.jsp">Help</a></p></div>
    </div>
    <p class="footer-copy">&copy; Ocean View Resort. CIS6003.</p>
  </footer>
  <script src="${pageContext.request.contextPath}/js/app.js?v=${resourceVersion}"></script>
</body>
</html>
