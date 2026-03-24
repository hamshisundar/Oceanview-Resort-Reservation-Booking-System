<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="cp" value="${pageContext.request.contextPath}"/>
<c:set var="nav" value="${adminNav}"/>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title><c:out value="${pageTitle}"/> · Ocean View Admin</title>
  <link rel="stylesheet" href="${cp}/admin/css/admin-shell.css?v=${resourceVersion}">
  <script src="${cp}/admin/js/admin-core.js?v=${resourceVersion}"></script>
</head>
<body class="admin-body" data-context="${cp}">
  <header class="admin-header">
    <div class="admin-header-inner">
      <a href="${cp}/index.jsp" class="logo">Ocean View <span>Resort</span></a>
      <nav class="admin-header-right" aria-label="Admin account">
        <a href="${cp}/admin/dashboard">Admin</a>
        <a href="${cp}/logout">Logout</a>
      </nav>
    </div>
  </header>
  <nav class="admin-subnav" aria-label="Admin navigation">
    <div class="admin-subnav-inner">
      <a class="${nav == 'dashboard' ? 'is-active' : ''}" href="${cp}/admin/dashboard">Dashboard</a>
      <a class="${nav == 'rooms' ? 'is-active' : ''}" href="${cp}/admin/rooms">Rooms</a>
      <a class="${nav == 'bookings' ? 'is-active' : ''}" href="${cp}/admin/bookings">Bookings</a>
      <a class="${nav == 'reports' ? 'is-active' : ''}" href="${cp}/admin/reports">Reports</a>
    </div>
  </nav>
  <div class="admin-main">
    <div class="admin-page-head"><h1 class="admin-page-heading"><c:out value="${pageTitle}"/></h1></div>
    <main class="admin-scroll" id="admin-page-content">
