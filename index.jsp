<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.oceanview.services.RoomService" %>
<%@ page import="com.oceanview.models.Room" %>
<%@ page import="java.util.List" %>
<%
  RoomService roomService = new RoomService();
  List<Room> rooms = roomService.getAllRooms();
  request.setAttribute("rooms", rooms);
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Ocean View Resort — Galle, Sri Lanka</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
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
            <c:if test="${sessionScope.user.role == 'ADMIN'}">
              <li><a href="${pageContext.request.contextPath}/admin/dashboard">Admin</a></li>
            </c:if>
            <li><a href="${pageContext.request.contextPath}/my-bookings">My Bookings</a></li>
            <li class="nav-user">
              <span class="nav-user-name">Hi, <c:out value="${sessionScope.user.name}"/></span>
              <a href="${pageContext.request.contextPath}/logout" class="nav-logout">Logout</a>
            </li>
          </c:when>
          <c:otherwise>
            <li><a href="${pageContext.request.contextPath}/login.jsp">Login</a></li>
            <li><a href="${pageContext.request.contextPath}/register.jsp" class="btn">Register</a></li>
          </c:otherwise>
        </c:choose>
      </ul>
    </div>
  </nav>

  <header class="hero hero-with-image">
    <img src="${pageContext.request.contextPath}/images/hero.png" alt="Ocean view at sunset — Ocean View Resort, Galle" class="hero-bg-image" onerror="this.style.display='none'">
    <div class="hero-overlay"></div>
    <div class="hero-content">
      <h1>Welcome to Ocean View Resort</h1>
      <p class="hero-tagline">Beachside comfort in Galle. Book your stay with ocean views, modern amenities, and warm hospitality.</p>
      <a href="${pageContext.request.contextPath}/rooms" class="btn btn-hero">Browse Rooms</a>
    </div>
  </header>

  <main class="container home-main">
    <section class="section section-why">
      <h2 class="section-title">Why Stay With Us</h2>
      <p class="section-lead">Comfortable rooms, sea views, pool access, and breakfast options. Check availability and reserve in a few clicks.</p>
    </section>
    <section class="section section-rooms">
      <h2 class="section-title">Featured Rooms</h2>
      <div class="room-grid">
        <c:forEach var="room" items="${rooms}">
          <article class="room-card">
            <div class="room-card-image placeholder" aria-hidden="true">🌊</div>
            <div class="room-card-body">
              <h3><c:out value="${room.roomName}"/></h3>
              <p class="room-type"><c:out value="${room.roomType}"/></p>
              <div class="amenities-inline">
                <c:if test="${room.hasWifi}"><span>📶 WiFi</span></c:if>
                <c:if test="${room.hasAc}"><span>❄️ AC</span></c:if>
                <c:if test="${room.hasSeaView}"><span>🌅 Sea View</span></c:if>
                <c:if test="${room.hasPoolAccess}"><span>🏊 Pool</span></c:if>
                <c:if test="${room.breakfastIncluded}"><span>🍳 Breakfast</span></c:if>
              </div>
              <div class="price-row">
                <span class="price">$<c:out value="${room.pricePerNight}"/> <small>/ night</small></span>
                <a href="${pageContext.request.contextPath}/room-details?id=${room.roomId}" class="btn">View</a>
              </div>
            </div>
          </article>
        </c:forEach>
      </div>
      <p class="text-center mt-1"><a href="${pageContext.request.contextPath}/rooms" class="btn">View all rooms</a></p>
    </section>
  </main>

  <footer class="footer">
    <div class="footer-inner">
      <div><h4>Ocean View Resort</h4><p>Galle, Sri Lanka</p></div>
      <div><h4>Contact</h4><p>Tel: +94 91 2 234 567</p><p><a href="mailto:info@oceanview.lk">info@oceanview.lk</a></p></div>
      <div><h4>Quick Links</h4><p><a href="${pageContext.request.contextPath}/rooms">Rooms</a></p><p><a href="${pageContext.request.contextPath}/help.jsp">Help</a></p></div>
    </div>
    <p class="footer-copy">&copy; Ocean View Resort. CIS6003.</p>
  </footer>
  <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
