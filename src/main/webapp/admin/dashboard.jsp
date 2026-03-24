<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/admin/inc/wrap-start.jsp"/>
    <div class="admin-kpi-grid admin-content-narrow" id="kpi-root">
      <div class="admin-kpi"><div class="admin-kpi-label">Total rooms</div><div class="admin-kpi-value">0</div></div>
      <div class="admin-kpi"><div class="admin-kpi-label">Total bookings</div><div class="admin-kpi-value">0</div></div>
      <div class="admin-kpi"><div class="admin-kpi-label">Guests in-house</div><div class="admin-kpi-value">0</div></div>
      <div class="admin-kpi"><div class="admin-kpi-label">Revenue (this month)</div><div class="admin-kpi-value">Rs. 0</div></div>
      <div class="admin-kpi"><div class="admin-kpi-label">Lifetime revenue</div><div class="admin-kpi-value">Rs. 0</div></div>
      <div class="admin-kpi"><div class="admin-kpi-label">Top room</div><div class="admin-kpi-value" style="font-size:1rem;">—</div></div>
    </div>
    <p class="admin-note admin-content-narrow">Simple overview of current hotel data from the database.</p>
<script>
(function () {
  var grid = document.getElementById('kpi-root');
  function renderKpis(data) {
    var d = data || {};
    var rows = [
      ['Total rooms', (d.totalRooms != null ? d.totalRooms : 0)],
      ['Total bookings', (d.totalBookings != null ? d.totalBookings : 0)],
      ['Guests in-house', (d.activeGuests != null ? d.activeGuests : 0)],
      ['Revenue (this month)', OVAdmin.formatMoney(d.revenueThisMonth != null ? d.revenueThisMonth : 0)],
      ['Lifetime revenue', OVAdmin.formatMoney(d.revenueAllTime != null ? d.revenueAllTime : 0)],
      ['Top room', d.mostBookedRoomName || '—']
    ];
    grid.innerHTML = rows.map(function (pair) {
      return '<div class="admin-kpi"><div class="admin-kpi-label">' + pair[0] + '</div><div class="admin-kpi-value">' + pair[1] + '</div></div>';
    }).join('');
  }
  OVAdmin.api('/api/admin/stats').then(function (r) {
    console.log('Admin stats response:', r);
    if (!r.ok || !r.data) {
      OVAdmin.toast('Could not load dashboard metrics', 'error');
      renderKpis(null);
      return;
    }
    renderKpis(r.data);
  }).catch(function (e) {
    console.log('Admin stats fetch error:', e);
    OVAdmin.toast('Could not load dashboard metrics', 'error');
    renderKpis(null);
  });
})();
</script>
<jsp:include page="/admin/inc/wrap-end.jsp"/>
