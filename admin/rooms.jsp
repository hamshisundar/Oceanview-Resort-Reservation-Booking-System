<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Room Management — Admin</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
  <style>
    .admin-nav { background: var(--ocean-blue-dark); padding: 0.75rem 1.5rem; }
    .admin-nav a { color: #fff; margin-right: 1rem; }
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
    <h1 class="section-title">Room Management</h1>
    <p><a href="${pageContext.request.contextPath}/admin/rooms?action=add" class="btn">Add Room</a></p>
    <div class="table-wrap">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Type</th>
            <th>Price/night</th>
            <th>Available</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="room" items="${rooms}">
            <tr>
              <td>${room.roomId}</td>
              <td><c:out value="${room.roomName}"/></td>
              <td><c:out value="${room.roomType}"/></td>
              <td>$<fmt:formatNumber value="${room.pricePerNight}" type="number" maxFractionDigits="2"/></td>
              <td>${room.available ? 'Yes' : 'No'}</td>
              <td>
                <a href="${pageContext.request.contextPath}/admin/rooms?action=edit&id=${room.roomId}">Edit</a>
                &nbsp;|&nbsp;
                <a href="${pageContext.request.contextPath}/admin/rooms?action=delete&id=${room.roomId}" onclick="return confirm('Delete this room?');">Delete</a>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
    <c:if test="${empty rooms}">
      <p class="text-center">No rooms. <a href="${pageContext.request.contextPath}/admin/rooms?action=add">Add one</a>.</p>
    </c:if>
  </main>

  <footer class="footer">
    <div class="footer-inner"><div><h4>Ocean View Resort</h4><p>Admin</p></div></div>
    <p class="footer-copy">&copy; Ocean View Resort. CIS6003.</p>
  </footer>
</body>
</html>
