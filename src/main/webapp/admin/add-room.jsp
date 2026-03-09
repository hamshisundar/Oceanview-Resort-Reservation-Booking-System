<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Add Room — Admin</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css?v=${resourceVersion}">
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
    <h1 class="section-title">Add Room</h1>
    <form action="${pageContext.request.contextPath}/admin/rooms" method="post" class="form-page" style="max-width: 560px;">
      <input type="hidden" name="action" value="add">
      <div class="form-group">
        <label for="roomName">Room name *</label>
        <input type="text" id="roomName" name="roomName" required>
      </div>
      <div class="form-group">
        <label for="roomType">Room type *</label>
        <select id="roomType" name="roomType" required>
          <option value="Standard Room">Standard Room</option>
          <option value="Deluxe Room">Deluxe Room</option>
          <option value="Ocean View Room">Ocean View Room</option>
          <option value="Luxury Suite">Luxury Suite</option>
        </select>
      </div>
      <div class="form-group">
        <label for="pricePerNight">Price per night (LKR) *</label>
        <input type="number" id="pricePerNight" name="pricePerNight" step="1" min="0" required>
      </div>
      <div class="form-group">
        <label for="description">Description</label>
        <textarea id="description" name="description" rows="3"></textarea>
      </div>
      <div class="form-group">
        <label><input type="checkbox" name="hasSeaView" value="1"> Sea view</label>
      </div>
      <div class="form-group">
        <label><input type="checkbox" name="hasWifi" value="1" checked> WiFi</label>
      </div>
      <div class="form-group">
        <label><input type="checkbox" name="hasAc" value="1" checked> AC</label>
      </div>
      <div class="form-group">
        <label><input type="checkbox" name="hasPoolAccess" value="1"> Pool access</label>
      </div>
      <div class="form-group">
        <label><input type="checkbox" name="breakfastIncluded" value="1"> Breakfast included</label>
      </div>
      <div class="form-group">
        <label for="imagePath">Image path</label>
        <input type="text" id="imagePath" name="imagePath" placeholder="images/rooms/room.png">
      </div>
      <button type="submit" class="btn-primary">Add Room</button>
      <p class="form-footer"><a href="${pageContext.request.contextPath}/admin/rooms">← Back to rooms</a></p>
    </form>
  </main>

  <footer class="footer">
    <div class="footer-inner"><div><h4>Ocean View Resort</h4></div></div>
    <p class="footer-copy">&copy; Ocean View Resort. CIS6003.</p>
  </footer>
</body>
</html>
