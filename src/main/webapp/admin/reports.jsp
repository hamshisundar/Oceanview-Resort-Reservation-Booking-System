<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/admin/inc/wrap-start.jsp"/>
    <div id="rep-kpis" class="admin-kpi-grid admin-content-narrow">
      <div class="admin-kpi"><div class="admin-kpi-label">Total rooms</div><div class="admin-kpi-value">0</div></div>
      <div class="admin-kpi"><div class="admin-kpi-label">Registered users</div><div class="admin-kpi-value">0</div></div>
      <div class="admin-kpi"><div class="admin-kpi-label">Total bookings</div><div class="admin-kpi-value">0</div></div>
      <div class="admin-kpi"><div class="admin-kpi-label">Revenue (month)</div><div class="admin-kpi-value">Rs. 0</div></div>
      <div class="admin-kpi"><div class="admin-kpi-label">Lifetime revenue</div><div class="admin-kpi-value">Rs. 0</div></div>
      <div class="admin-kpi"><div class="admin-kpi-label">Most booked room</div><div class="admin-kpi-value" style="font-size:1rem;">—</div></div>
    </div>
    <div class="admin-card admin-content-narrow" style="padding:1.25rem;">
      <h2 style="margin:0 0 1rem;font-size:1rem;">Bookings by status</h2>
      <div id="rep-by-status" style="color:#6b7280;">Loading…</div>
    </div>
<script>
(function () {
  var kpis = document.getElementById('rep-kpis');
  var bySt = document.getElementById('rep-by-status');
  OVAdmin.api('/api/admin/reports').then(function (r) {
    if (!r.ok || !r.data) {
      OVAdmin.toast('Could not load reports', 'error');
      return;
    }
    var d = r.data;
    var rows = [
      ['Total rooms', d.totalRooms],
      ['Registered users', d.totalUsers],
      ['Total bookings', d.totalBookings],
      ['Revenue (month)', OVAdmin.formatMoney(d.revenueThisMonth)],
      ['Lifetime revenue', OVAdmin.formatMoney(d.revenueAllTime)],
      ['Most booked room', d.mostBookedRoomName || '—']
    ];
    kpis.innerHTML = rows.map(function (p) {
      return '<div class="admin-kpi"><div class="admin-kpi-label">' + p[0] + '</div><div class="admin-kpi-value">' + p[1] + '</div></div>';
    }).join('');
    var m = d.bookingsByStatus || {};
    var keys = Object.keys(m);
    if (keys.length === 0) bySt.textContent = 'No booking data yet.';
    else {
      bySt.innerHTML = '<ul style="margin:0;padding-left:1.2rem;line-height:1.8;">' +
        keys.map(function (k) { return '<li><strong>' + k + '</strong>: ' + m[k] + '</li>'; }).join('') + '</ul>';
    }
  });
})();
</script>
<jsp:include page="/admin/inc/wrap-end.jsp"/>
