<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${room.roomName} — Ocean View Resort</title>
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

  <main class="container section">
    <c:if test="${param.error == 'unavailable'}">
      <p class="error-msg">This room is not available for the selected dates. Please choose different dates.</p>
    </c:if>
    <div class="room-detail">
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
          <img src="${pageContext.request.contextPath}/${roomImg}?v=${resourceVersion}" alt="${room.roomName}" class="room-detail-image" onerror="this.style.display='none';this.nextElementSibling&&this.nextElementSibling.classList.remove('hide')">
          <div class="room-detail-image hide" aria-hidden="true">🌊</div>
        </c:when>
        <c:otherwise>
          <div class="room-detail-image" aria-hidden="true">🌊</div>
        </c:otherwise>
      </c:choose>
      <div class="room-detail-info">
        <h1><c:out value="${room.roomName}"/></h1>
        <p class="room-type"><c:out value="${room.roomType}"/></p>
        <p class="price">Rs. <fmt:formatNumber value="${room.pricePerNight}" type="number" maxFractionDigits="0" groupingUsed="true"/><small> per night</small></p>
        <ul class="amenities-list">
          <c:if test="${room.hasWifi}"><li><span class="amenity-icon">📶</span> WiFi</li></c:if>
          <c:if test="${room.hasAc}"><li><span class="amenity-icon">❄️</span> Air conditioning</li></c:if>
          <c:if test="${room.hasSeaView}"><li><span class="amenity-icon">🌅</span> Sea view</li></c:if>
          <c:if test="${room.hasPoolAccess}"><li><span class="amenity-icon">🏊</span> Pool access</li></c:if>
          <c:if test="${room.breakfastIncluded}"><li><span class="amenity-icon">🍳</span> Breakfast included</li></c:if>
        </ul>
        <p class="description"><c:out value="${room.description}"/></p>
        <div class="booking-box">
          <h3>Book this room</h3>
          <c:choose>
            <c:when test="${not empty sessionScope.user}">
              <form id="booking-form" action="${pageContext.request.contextPath}/book-room" method="post">
                <input type="hidden" name="roomId" value="${room.roomId}">
                <div class="form-row">
                  <div class="form-group">
                    <label for="check-in">Check-in</label>
                    <input type="date" id="check-in" name="checkIn" required>
                  </div>
                  <div class="form-group">
                    <label for="check-out">Check-out</label>
                    <input type="date" id="check-out" name="checkOut" required>
                  </div>
                </div>
                <div class="form-group">
                  <button type="submit" class="btn-primary">Check availability &amp; book</button>
                </div>
              </form>
            </c:when>
            <c:otherwise>
              <p>Please <a href="${pageContext.request.contextPath}/login.jsp?redirect=book">log in</a> to book this room.</p>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
    </div>
    <p><a href="${pageContext.request.contextPath}/rooms">← Back to all rooms</a></p>
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
