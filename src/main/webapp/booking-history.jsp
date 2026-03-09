<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>My Bookings — Ocean View Resort</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css?v=${resourceVersion}">
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
        <c:choose>
          <c:when test="${not empty sessionScope.user}">
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

  <main class="container section">
    <h1 class="section-title">Booking history</h1>
    <c:if test="${empty sessionScope.user}">
      <p class="text-center mb-2">Please <a href="${pageContext.request.contextPath}/login.jsp">log in</a> to view your bookings.</p>
    </c:if>
    <c:if test="${not empty sessionScope.user}">
      <p class="text-center mb-2">Your past and upcoming reservations.</p>
      <div class="table-wrap">
        <table>
          <thead>
            <tr>
              <th>Reservation #</th>
              <th>Room</th>
              <th>Check-in</th>
              <th>Check-out</th>
              <th>Total</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="b" items="${bookings}">
              <tr>
                <td><c:out value="${b.reservationNumber}"/></td>
                <td><c:out value="${roomNames[b.roomId] != null ? roomNames[b.roomId] : 'Room'}"/></td>
                <td><c:out value="${b.checkIn}"/></td>
                <td><c:out value="${b.checkOut}"/></td>
                <td>Rs. <fmt:formatNumber value="${b.totalPrice}" type="number" maxFractionDigits="0" groupingUsed="true"/></td>
                <td><span class="status-badge status-${b.status == 'CANCELLED' ? 'cancelled' : (b.status == 'PENDING' ? 'pending' : 'confirmed')}"><c:out value="${b.status}"/></span></td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
      <c:if test="${empty bookings}">
        <p class="text-center">You have no bookings yet. <a href="${pageContext.request.contextPath}/rooms">Book a room</a>.</p>
      </c:if>
      <p class="text-center mt-1"><a href="${pageContext.request.contextPath}/rooms" class="btn">Book a room</a></p>
    </c:if>
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
