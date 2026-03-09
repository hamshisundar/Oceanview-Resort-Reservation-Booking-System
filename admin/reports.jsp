<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Reports — Admin</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
  <style>
    .admin-nav { background: var(--ocean-blue-dark); padding: 0.75rem 1.5rem; }
    .admin-nav a { color: #fff; margin-right: 1rem; }
    .stats-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1rem; margin: 1.5rem 0; }
    .stat-card { background: var(--white); padding: 1.25rem; border-radius: var(--radius); box-shadow: var(--shadow); }
    .stat-card h3 { font-size: 0.9rem; color: var(--grey); margin-bottom: 0.25rem; }
    .stat-card .value { font-size: 1.5rem; font-weight: 700; color: var(--ocean-blue); }
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
    <h1 class="section-title">Booking &amp; Revenue Reports</h1>
    <div class="stats-grid">
      <div class="stat-card">
        <h3>Total Rooms</h3>
        <p class="value">${totalRooms}</p>
      </div>
      <div class="stat-card">
        <h3>Total Bookings</h3>
        <p class="value">${totalBookings}</p>
      </div>
      <div class="stat-card">
        <h3>Revenue This Month</h3>
        <p class="value">$<fmt:formatNumber value="${revenueThisMonth}" type="number" maxFractionDigits="2"/></p>
      </div>
      <div class="stat-card">
        <h3>Most Booked Room</h3>
        <p class="value" style="font-size: 1rem;"><c:out value="${mostBookedRoomName}"/></p>
      </div>
    </div>
    <p><a href="${pageContext.request.contextPath}/admin/dashboard" class="btn">← Dashboard</a></p>
  </main>

  <footer class="footer">
    <div class="footer-inner"><div><h4>Ocean View Resort</h4></div></div>
    <p class="footer-copy">&copy; Ocean View Resort. CIS6003.</p>
  </footer>
</body>
</html>
