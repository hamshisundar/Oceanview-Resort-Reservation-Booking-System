<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/admin/inc/wrap-start.jsp"/>
    <p class="admin-note admin-content-wide" style="margin:0 0 1rem;">Directory of registered accounts. Deleting a user removes their booking history (cascade).</p>
    <div class="admin-card admin-table-wrap admin-content-wide">
      <table class="admin-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Role</th>
            <th style="text-align:right;">Actions</th>
          </tr>
        </thead>
        <tbody id="users-tbody"><tr><td colspan="5" style="color:#6b7280;">Loading…</td></tr></tbody>
      </table>
    </div>
<script>
(function () {
  var tbody = document.getElementById('users-tbody');
  function load() {
    OVAdmin.api('/api/users').then(function (r) {
      if (!r.ok || !Array.isArray(r.data)) {
        tbody.innerHTML = '<tr><td colspan="5">Could not load users.</td></tr>';
        return;
      }
      tbody.innerHTML = r.data.map(function (u) {
        var roleBadge = u.role === 'ADMIN' ? 'badge badge-completed' : 'badge badge-confirmed';
        var delBtn = u.isCurrentUser
          ? '<span style="color:#9ca3af;font-size:0.8rem;">You</span>'
          : '<button type="button" class="btn btn-danger btn-sm btn-del-user" data-id="' + u.id + '" data-name="' + escapeAttr(u.name) + '">Delete</button>';
        return '<tr><td>' + u.id + '</td><td>' + escapeHtml(u.name) + '</td><td>' + escapeHtml(u.email) + '</td>' +
          '<td><span class="' + roleBadge + '">' + escapeHtml(u.role) + '</span></td><td style="text-align:right;">' + delBtn + '</td></tr>';
      }).join('');
      tbody.querySelectorAll('.btn-del-user').forEach(function (btn) {
        btn.addEventListener('click', function () {
          var id = btn.getAttribute('data-id');
          var nm = btn.getAttribute('data-name');
          OVAdmin.confirmDialog('Delete user', 'Remove ' + nm + ' from the system?').then(function (ok) {
            if (!ok) return;
            OVAdmin.api('/api/users/' + id, { method: 'DELETE' }).then(function (res) {
              if (res.status === 204) { OVAdmin.toast('User deleted'); load(); }
              else OVAdmin.toast((res.data && res.data.error) || 'Delete failed', 'error');
            });
          });
        });
      });
    });
  }
  function escapeHtml(s) {
    if (!s) return '';
    return String(s).replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/"/g,'&quot;');
  }
  function escapeAttr(s) {
    return escapeHtml(s).replace(/'/g, '&#39;');
  }
  load();
})();
</script>
<jsp:include page="/admin/inc/wrap-end.jsp"/>
