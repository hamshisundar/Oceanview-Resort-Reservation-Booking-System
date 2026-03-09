<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Booking Management — Admin</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css?v=${resourceVersion}">
  <style>
    .admin-nav { background: var(--ocean-blue-dark); padding: 0.75rem 1.5rem; }
    .admin-nav a { color: #fff; margin-right: 1rem; }
    .status-form { display: inline; }
    .status-form select { padding: 0.25rem; margin-right: 0.25rem; }
  </style>
</head>
<body>
  <nav class="navbar">
    <div class="nav-inner">
      <a href="${pageContext.request.contextPath}/index.jsp" class="logo">Ocean View <span>Resort</span></a>
      <ul class="nav-links">
        <li><a href="${pageContext.request.contextPath}/admin/dashboard">Admin</a></li>
        <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
      </ul>
    </div>
  </nav>
  <div class="admin-nav">
    <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
    <a href="${pageContext.request.contextPath}/admin/rooms">Rooms</a>
    <a href="${pageContext.request.contextPath}/admin/bookings">Bookings</a>
    <a href="${pageContext.request.contextPath}/admin/reports">Reports</a>
  </div>

  <main class="container section">
    <h1 class="section-title">Booking Management</h1>
    <div class="table-wrap">
      <table>
        <thead>
          <tr>
            <th>Reservation #</th>
            <th>Room</th>
            <th>Guest</th>
            <th>Check-in</th>
            <th>Check-out</th>
            <th>Total</th>
            <th>Status</th>
            <th>Update</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="b" items="${bookings}">
            <tr>
              <td><c:out value="${b.reservationNumber}"/></td>
              <td><c:out value="${roomNames[b.roomId] != null ? roomNames[b.roomId] : 'Room'}"/></td>
              <td><c:out value="${userNames[b.userId] != null ? userNames[b.userId] : 'Guest'}"/></td>
              <td><c:out value="${b.checkIn}"/></td>
              <td><c:out value="${b.checkOut}"/></td>
              <td>Rs. <fmt:formatNumber value="${b.totalPrice}" type="number" maxFractionDigits="0" groupingUsed="true"/></td>
              <td><span class="status-badge status-${b.status == 'CANCELLED' ? 'cancelled' : (b.status == 'PENDING' ? 'pending' : 'confirmed')}"><c:out value="${b.status}"/></span></td>
              <td>
                <form action="${pageContext.request.contextPath}/admin/bookings" method="post" class="status-form">
                  <input type="hidden" name="action" value="updateStatus">
                  <input type="hidden" name="bookingId" value="${b.bookingId}">
                  <select name="status">
                    <option value="CONFIRMED" ${b.status == 'CONFIRMED' ? 'selected' : ''}>CONFIRMED</option>
                    <option value="PENDING" ${b.status == 'PENDING' ? 'selected' : ''}>PENDING</option>
                    <option value="COMPLETED" ${b.status == 'COMPLETED' ? 'selected' : ''}>COMPLETED</option>
                    <option value="CANCELLED" ${b.status == 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
                  </select>
                  <button type="submit" class="btn" style="padding: 0.25rem 0.5rem; font-size: 0.85rem;">Update</button>
                </form>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
    <c:if test="${empty bookings}">
      <p class="text-center">No bookings yet.</p>
    </c:if>
  </main>

  <footer class="footer">
    <div class="footer-inner"><div><h4>Ocean View Resort</h4></div></div>
    <p class="footer-copy">&copy; Ocean View Resort. CIS6003.</p>
  </footer>
</body>
</html>
