<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/admin/inc/wrap-start.jsp"/>
    <div class="admin-toolbar admin-content-wide">
      <div class="admin-toolbar-actions">
        <select class="admin-select" id="flt-status" aria-label="Filter by status">
          <option value="ALL">All statuses</option>
          <option value="PENDING">Pending</option>
          <option value="CONFIRMED">Confirmed</option>
          <option value="COMPLETED">Completed</option>
          <option value="CANCELLED">Cancelled</option>
        </select>
        <select class="admin-select" id="flt-sort" aria-label="Sort by">
          <option value="check_in">Check-in date</option>
          <option value="check_out">Check-out date</option>
          <option value="total_price">Total price</option>
          <option value="created_at">Recently created</option>
        </select>
        <select class="admin-select" id="flt-dir" aria-label="Sort direction">
          <option value="DESC">Newest / high first</option>
          <option value="ASC">Oldest / low first</option>
        </select>
      </div>
    </div>
    <div class="admin-card admin-table-wrap admin-content-wide">
      <table class="admin-table">
        <thead>
          <tr>
            <th>Reservation</th>
            <th>Guest</th>
            <th>Room</th>
            <th>Check-in</th>
            <th>Check-out</th>
            <th>Total</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody id="book-tbody"><tr><td colspan="8" style="color:#6b7280;">Loading…</td></tr></tbody>
      </table>
    </div>
<script>
(function () {
  var tbody = document.getElementById('book-tbody');
  var fltStatus = document.getElementById('flt-status');
  var fltSort = document.getElementById('flt-sort');
  var fltDir = document.getElementById('flt-dir');

  function load() {
    var q = '?sort=' + encodeURIComponent(fltSort.value) + '&dir=' + encodeURIComponent(fltDir.value);
    if (fltStatus.value !== 'ALL') q += '&status=' + encodeURIComponent(fltStatus.value);
    OVAdmin.api('/api/bookings' + q).then(function (r) {
      console.log('Bookings response:', r);
      if (!r.ok || !Array.isArray(r.data)) {
        tbody.innerHTML = '<tr><td colspan="8" style="color:#6b7280;">Could not load bookings. Showing empty state.</td></tr>';
        OVAdmin.toast('Failed to load bookings', 'error');
        return;
      }
      if (r.data.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" style="color:#6b7280;">No bookings match this filter.</td></tr>';
        return;
      }
      tbody.innerHTML = r.data.map(function (b) {
        var badge = OVAdmin.badgeClass(b.status);
        return '<tr>' +
          '<td><strong>' + escapeHtml(b.reservationNumber) + '</strong></td>' +
          '<td>' + escapeHtml(b.guestName) + '<br><span style="color:#6b7280;font-size:0.8rem;">' + escapeHtml(b.guestEmail) + '</span></td>' +
          '<td>' + escapeHtml(b.roomName) + '</td>' +
          '<td>' + escapeHtml(b.checkIn) + '</td>' +
          '<td>' + escapeHtml(b.checkOut) + '</td>' +
          '<td>' + OVAdmin.formatMoney(b.totalPrice) + '</td>' +
          '<td><span class="' + badge + '">' + escapeHtml(b.status) + '</span></td>' +
          '<td><select class="admin-select st-sel" data-id="' + b.id + '" style="min-width:8rem;">' +
          opt(b.status, 'PENDING') + opt(b.status, 'CONFIRMED') + opt(b.status, 'COMPLETED') + opt(b.status, 'CANCELLED') +
          '</select> <button type="button" class="btn btn-primary btn-sm btn-save-st">Update</button></td></tr>';
      }).join('');

      function opt(cur, v) {
        return '<option value="' + v + '"' + (cur === v ? ' selected' : '') + '>' + v + '</option>';
      }

      tbody.querySelectorAll('.btn-save-st').forEach(function (btn) {
        btn.addEventListener('click', function () {
          var tr = btn.closest('tr');
          var sel = tr.querySelector('.st-sel');
          var id = sel.getAttribute('data-id');
          var st = sel.value;
          OVAdmin.api('/api/bookings/' + id + '/status', {
            method: 'PUT',
            body: JSON.stringify({ status: st })
          }).then(function (res) {
            if (res.ok) OVAdmin.toast('Status updated');
            else OVAdmin.toast((res.data && res.data.error) || 'Update failed', 'error');
            load();
          });
        });
      });
    }).catch(function (e) {
      console.log('Bookings fetch error:', e);
      tbody.innerHTML = '<tr><td colspan="8" style="color:#6b7280;">Could not load bookings. Showing empty state.</td></tr>';
      OVAdmin.toast('Failed to load bookings', 'error');
    });
  }

  function escapeHtml(s) {
    if (!s) return '';
    return String(s).replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/"/g,'&quot;');
  }

  fltStatus.addEventListener('change', load);
  fltSort.addEventListener('change', load);
  fltDir.addEventListener('change', load);
  load();
})();
</script>
<jsp:include page="/admin/inc/wrap-end.jsp"/>
