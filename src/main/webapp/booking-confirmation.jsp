<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Booking Confirmed — Ocean View Resort</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css?v=${resourceVersion}">
  <style media="print">
    .navbar, .footer, .no-print, .btn { display: none !important; }
    .confirmation-card { box-shadow: none; border: 1px solid #ccc; }
    body { background: #fff; }
  </style>
</head>
<body>
  <nav class="navbar">
    <div class="nav-inner">
      <a href="${pageContext.request.contextPath}/index.jsp" class="logo">Ocean View <span>Resort</span></a>
      <ul class="nav-links">
        <li><a href="${pageContext.request.contextPath}/index.jsp">Home</a></li>
        <li><a href="${pageContext.request.contextPath}/rooms">Rooms</a></li>
        <li><a href="${pageContext.request.contextPath}/my-bookings">My Bookings</a></li>
        <li><a href="${pageContext.request.contextPath}/help.jsp">Help</a></li>
        <li class="nav-user"><span class="nav-user-name">Hi, <c:out value="${sessionScope.user.name}"/></span><a href="${pageContext.request.contextPath}/logout" class="nav-logout">Logout</a></li>
      </ul>
    </div>
  </nav>

  <main class="container">
    <div class="confirmation-card">
      <div class="success-badge" aria-hidden="true">✅</div>
      <h1>Booking confirmed</h1>
      <p>Thank you for choosing Ocean View Resort.</p>
      <p class="reservation-number"><c:out value="${param.reservation}"/></p>
      <div class="confirmation-details print-bill-content">
        <p><strong>Guest:</strong> <c:out value="${param.guestName}"/></p>
        <p><strong>Room ID:</strong> <c:out value="${param.roomId}"/></p>
        <p><strong>Check-in:</strong> <c:out value="${param.checkIn}"/></p>
        <p><strong>Check-out:</strong> <c:out value="${param.checkOut}"/></p>
        <p><strong>Total bill:</strong> Rs. <fmt:formatNumber value="${param.total}" type="number" maxFractionDigits="0" groupingUsed="true"/></p>
      </div>
      <p>Keep your reservation number for your records.</p>
      <p class="no-print">
        <a href="${pageContext.request.contextPath}/rooms" class="btn">Book another room</a>
        <a href="${pageContext.request.contextPath}/my-bookings" class="btn" style="margin-left: 0.5rem;">View my bookings</a>
        <button type="button" class="btn" onclick="window.print();" style="margin-left: 0.5rem;">Print bill</button>
      </p>
    </div>
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
