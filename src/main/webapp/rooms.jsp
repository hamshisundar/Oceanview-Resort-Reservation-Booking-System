<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Rooms — Ocean View Resort</title>
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
            <c:if test="${sessionScope.user.role == 'ADMIN'}">
              <li><a href="${pageContext.request.contextPath}/admin/dashboard">Admin</a></li>
            </c:if>
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
      <h1>Our Rooms</h1>
      <p>Choose from Standard, Deluxe, Ocean View, or Luxury Suite. Filter by price, type, and amenities.</p>
    </div>
  </header>

  <main class="container section">
    <form id="filters-form" class="filters-bar" method="get" action="${pageContext.request.contextPath}/rooms">
      <div class="filter-group">
        <label for="room-type">Room type</label>
        <select id="room-type" name="type">
          <option value="">All types</option>
          <option value="Standard Room" ${param.type == 'Standard Room' ? 'selected' : ''}>Standard Room</option>
          <option value="Deluxe Room" ${param.type == 'Deluxe Room' ? 'selected' : ''}>Deluxe Room</option>
          <option value="Ocean View Room" ${param.type == 'Ocean View Room' ? 'selected' : ''}>Ocean View Room</option>
          <option value="Luxury Suite" ${param.type == 'Luxury Suite' ? 'selected' : ''}>Luxury Suite</option>
        </select>
      </div>
      <div class="filter-group">
        <label for="min-price">Min price (LKR)</label>
        <input type="number" id="min-price" name="minPrice" min="0" step="1000" placeholder="0" value="${param.minPrice}">
      </div>
      <div class="filter-group">
        <label for="max-price">Max price (LKR)</label>
        <input type="number" id="max-price" name="maxPrice" min="0" step="1000" placeholder="100000" value="${param.maxPrice}">
      </div>
      <div class="filter-group">
        <label for="sea-view">Sea view</label>
        <select id="sea-view" name="seaView">
          <option value="">Any</option>
          <option value="1" ${param.seaView == '1' ? 'selected' : ''}>Yes</option>
          <option value="0" ${param.seaView == '0' ? 'selected' : ''}>No</option>
        </select>
      </div>
      <div class="filter-group">
        <label>&nbsp;</label>
        <button type="submit" class="btn">Apply filters</button>
      </div>
    </form>

    <div class="room-grid" id="room-list">
      <c:forEach var="room" items="${rooms}">
        <article class="room-card">
          <c:set var="roomImg" value="${room.imagePath}"/>
          <c:if test="${empty roomImg}">
            <c:choose>
              <c:when test="${room.roomName == 'Standard Double'}"><c:set var="roomImg" value="images/rooms/standard.png"/></c:when>
              <c:when test="${room.roomName == 'Deluxe Ocean'}"><c:set var="roomImg" value="images/rooms/deluxe.png"/></c:when>
              <c:when test="${room.roomName == 'Ocean View Premium'}"><c:set var="roomImg" value="images/rooms/oceanview.png"/></c:when>
              <c:when test="${room.roomName == 'Luxury Suite'}"><c:set var="roomImg" value="images/rooms/suite.png"/></c:when>
            </c:choose>
          </c:if>
          <c:choose>
            <c:when test="${not empty roomImg}">
              <img src="${pageContext.request.contextPath}/${roomImg}?v=${resourceVersion}" alt="${room.roomName}" class="room-card-image" onerror="this.style.display='none';this.nextElementSibling&&this.nextElementSibling.classList.remove('hide')">
              <div class="room-card-image placeholder hide" aria-hidden="true">🌊</div>
            </c:when>
            <c:otherwise>
              <div class="room-card-image placeholder" aria-hidden="true">🌊</div>
            </c:otherwise>
          </c:choose>
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
              <span class="price">Rs. <fmt:formatNumber value="${room.pricePerNight}" type="number" maxFractionDigits="0" groupingUsed="true"/> <small>/ night</small></span>
              <a href="${pageContext.request.contextPath}/room-details?id=${room.roomId}" class="btn">View &amp; book</a>
            </div>
          </div>
        </article>
      </c:forEach>
    </div>
    <c:if test="${empty rooms}">
      <p class="text-center">No rooms match your filters. Try adjusting the criteria.</p>
    </c:if>
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
