<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Database Status — Ocean View Resort</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css?v=${resourceVersion}">
</head>
<body>
  <nav class="navbar">
    <div class="nav-inner">
      <a href="${pageContext.request.contextPath}/index.jsp" class="logo">Ocean View <span>Resort</span></a>
      <ul class="nav-links">
        <li><a href="${pageContext.request.contextPath}/index.jsp">Home</a></li>
        <li><a href="${pageContext.request.contextPath}/rooms">Rooms</a></li>
        <li><a href="${pageContext.request.contextPath}/register.jsp">Register</a></li>
        <li><a href="${pageContext.request.contextPath}/login.jsp">Login</a></li>
      </ul>
    </div>
  </nav>

  <main class="container">
    <div class="form-page" style="max-width: 560px;">
      <h1>Database status</h1>
      <p class="text-center" style="margin-bottom: 1rem;">This page shows whether the app can connect to MySQL. Use it to fix registration issues.</p>

      <c:choose>
        <c:when test="${dbOk}">
          <div class="success-badge" style="font-size: 2rem; margin-bottom: 1rem;">✓</div>
          <p class="text-center" style="color: var(--ocean-blue); font-weight: 600;"><c:out value="${dbMessage}"/></p>
          <p class="text-center mt-1"><a href="${pageContext.request.contextPath}/register.jsp" class="btn">Try registration again</a></p>
        </c:when>
        <c:otherwise>
          <div style="background: #fff5f5; border: 1px solid #e0a0a0; border-radius: 8px; padding: 1rem; margin-bottom: 1rem;">
            <p style="color: #c00; font-weight: 600;"><c:out value="${dbMessage}"/></p>
            <c:if test="${not empty dbDetail}">
              <p style="margin-top: 0.5rem; font-size: 0.9rem; word-break: break-word;"><strong>Error:</strong> <c:out value="${dbDetail}"/></p>
            </c:if>
          </div>
          <div style="font-size: 0.95rem;">
            <p><strong>What to do:</strong></p>
            <ul style="margin-left: 1.25rem; line-height: 1.7;">
              <li><strong>“Access denied”</strong> → Set your MySQL root password in <code>src/main/webapp/WEB-INF/web.xml</code> (<code>db.password</code>), then restart Tomcat.</li>
              <li><strong>“Unknown database 'oceanview_db'”</strong> → Create the database: run <code>mysql -u root -p &lt; database/schema.sql</code> from the project folder.</li>
              <li><strong>“Communications link failure”</strong> → Start MySQL (e.g. <code>brew services start mysql</code> on Mac).</li>
              <li><strong>“Table 'users' doesn't exist”</strong> → Run the full schema: <code>mysql -u root -p &lt; database/schema.sql</code>.</li>
            </ul>
            <p class="mt-1"><a href="${pageContext.request.contextPath}/register.jsp" class="btn">Back to Register</a></p>
          </div>
        </c:otherwise>
      </c:choose>
    </div>
  </main>

  <footer class="footer">
    <div class="footer-inner">
      <p class="footer-copy">&copy; Ocean View Resort.</p>
    </div>
  </footer>
</body>
</html>
