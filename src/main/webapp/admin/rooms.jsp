<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/admin/inc/wrap-start.jsp"/>
    <div class="admin-toolbar admin-content-wide">
      <p class="admin-note" style="margin:0;">Create, update, and retire rooms. Changes apply to the guest site immediately.</p>
      <div class="admin-toolbar-actions">
        <button type="button" class="btn btn-primary" id="btn-add-room">Add room</button>
      </div>
    </div>
    <div class="admin-card admin-table-wrap admin-content-wide">
      <table class="admin-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Type</th>
            <th>Price / night</th>
            <th>Available</th>
            <th style="text-align:right;">Actions</th>
          </tr>
        </thead>
        <tbody id="rooms-tbody">
          <tr><td colspan="6" style="color:#6b7280;">Loading…</td></tr>
        </tbody>
      </table>
    </div>

    <div id="room-modal" class="admin-modal-panel" hidden>
      <div class="admin-modal-card">
        <div class="admin-modal-head">
          <h2 id="room-modal-title">Add room</h2>
          <button type="button" class="btn btn-ghost btn-sm" id="room-modal-close" aria-label="Close">Close</button>
        </div>
        <div class="admin-modal-body">
          <input type="hidden" id="f-room-id" value="">
          <div class="admin-form-grid">
            <div class="admin-form-row">
              <label for="f-name">Name</label>
              <input type="text" id="f-name" required maxlength="100">
            </div>
            <div class="admin-form-row">
              <label for="f-type">Type</label>
              <input type="text" id="f-type" required maxlength="50" placeholder="e.g. Deluxe Room">
            </div>
            <div class="admin-form-row">
              <label for="f-price">Price per night (LKR)</label>
              <input type="number" id="f-price" min="0" step="0.01" required>
            </div>
            <div class="admin-form-row">
              <label for="f-image">Image path</label>
              <input type="text" id="f-image" placeholder="images/rooms/standard.png">
            </div>
            <div class="admin-form-row">
              <label for="f-image-file">Upload image (from your device)</label>
              <input type="file" id="f-image-file" accept="image/*">
            </div>
            <div class="admin-form-row">
              <label for="f-desc">Description</label>
              <textarea id="f-desc"></textarea>
            </div>
            <div class="admin-form-row" style="flex-direction:row;align-items:center;gap:0.75rem;">
              <label style="display:flex;align-items:center;gap:0.4rem;font-weight:500;color:#1a1d26;">
                <input type="checkbox" id="f-available" checked> Available for booking
              </label>
            </div>
            <fieldset style="border:1px solid var(--adm-border);border-radius:8px;padding:0.75rem 1rem;margin:0;">
              <legend style="font-size:0.75rem;font-weight:600;color:#6b7280;padding:0 0.35rem;">Amenities</legend>
              <div style="display:grid;grid-template-columns:1fr 1fr;gap:0.5rem;">
                <label style="display:flex;align-items:center;gap:0.35rem;font-size:0.85rem;"><input type="checkbox" id="f-sea"> Sea view</label>
                <label style="display:flex;align-items:center;gap:0.35rem;font-size:0.85rem;"><input type="checkbox" id="f-wifi" checked> Wi‑Fi</label>
                <label style="display:flex;align-items:center;gap:0.35rem;font-size:0.85rem;"><input type="checkbox" id="f-ac" checked> A/C</label>
                <label style="display:flex;align-items:center;gap:0.35rem;font-size:0.85rem;"><input type="checkbox" id="f-pool"> Pool access</label>
                <label style="display:flex;align-items:center;gap:0.35rem;font-size:0.85rem;"><input type="checkbox" id="f-breakfast"> Breakfast</label>
              </div>
            </fieldset>
          </div>
        </div>
        <div class="admin-modal-foot">
          <button type="button" class="btn btn-ghost" id="room-modal-cancel">Cancel</button>
          <button type="button" class="btn btn-primary" id="room-modal-save">Save</button>
        </div>
      </div>
    </div>
<script>
(function () {
  var tbody = document.getElementById('rooms-tbody');
  var modal = document.getElementById('room-modal');
  var titleEl = document.getElementById('room-modal-title');

  function openModal(room) {
    document.getElementById('f-room-id').value = room ? room.id : '';
    document.getElementById('f-name').value = room ? room.name : '';
    document.getElementById('f-type').value = room ? room.type : '';
    document.getElementById('f-price').value = room ? room.price : '';
    document.getElementById('f-image').value = room && room.imagePath ? room.imagePath : 'images/rooms/standard.png';
    document.getElementById('f-image-file').value = '';
    document.getElementById('f-desc').value = room && room.description ? room.description : '';
    document.getElementById('f-available').checked = !room || room.available !== false;
    document.getElementById('f-sea').checked = !!(room && room.hasSeaView);
    document.getElementById('f-wifi').checked = !room || room.hasWifi !== false;
    document.getElementById('f-ac').checked = !room || room.hasAc !== false;
    document.getElementById('f-pool').checked = !!(room && room.hasPoolAccess);
    document.getElementById('f-breakfast').checked = !!(room && room.breakfastIncluded);
    titleEl.textContent = room ? 'Edit room' : 'Add room';
    modal.hidden = false;
  }
  function closeModal() { modal.hidden = true; }

  function loadRooms() {
    OVAdmin.api('/api/rooms').then(function (r) {
      console.log('Rooms response:', r);
      if (!r.ok || !Array.isArray(r.data)) {
        tbody.innerHTML = '<tr><td colspan="6" style="color:#6b7280;">Could not load rooms. Showing empty state.</td></tr>';
        OVAdmin.toast('Failed to load rooms', 'error');
        return;
      }
      if (r.data.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" style="color:#6b7280;">No rooms yet. Add your first room.</td></tr>';
        return;
      }
      tbody.innerHTML = r.data.map(function (x) {
        return '<tr data-id="' + x.id + '">' +
          '<td>' + x.id + '</td>' +
          '<td>' + escapeHtml(x.name) + '</td>' +
          '<td>' + escapeHtml(x.type) + '</td>' +
          '<td>' + OVAdmin.formatMoney(x.price) + '</td>' +
          '<td>' + (x.available ? '<span class="badge badge-confirmed">Yes</span>' : '<span class="badge badge-cancelled">No</span>') + '</td>' +
          '<td style="text-align:right;">' +
          '<button type="button" class="btn btn-primary btn-sm btn-edit" data-id="' + x.id + '">Edit</button> ' +
          '<button type="button" class="btn btn-danger btn-sm btn-del" data-id="' + x.id + '">Delete</button>' +
          '</td></tr>';
      }).join('');
      tbody.querySelectorAll('.btn-edit').forEach(function (btn) {
        btn.addEventListener('click', function () {
          var id = parseInt(btn.getAttribute('data-id'), 10);
          var row = r.data.find(function (z) { return z.id === id; });
          if (row) openModal(row);
        });
      });
      tbody.querySelectorAll('.btn-del').forEach(function (btn) {
        btn.addEventListener('click', function () {
          var id = btn.getAttribute('data-id');
          OVAdmin.confirmDialog('Delete room', 'This cannot be undone if the room has no active dependencies. Continue?').then(function (ok) {
            if (!ok) return;
            OVAdmin.api('/api/rooms/' + id, { method: 'DELETE' }).then(function (res) {
              if (res.status === 204) { OVAdmin.toast('Room deleted'); loadRooms(); }
              else OVAdmin.toast((res.data && res.data.error) || 'Delete failed', 'error');
            });
          });
        });
      });
    }).catch(function (e) {
      console.log('Rooms fetch error:', e);
      tbody.innerHTML = '<tr><td colspan="6" style="color:#6b7280;">Could not load rooms. Showing empty state.</td></tr>';
      OVAdmin.toast('Failed to load rooms', 'error');
    });
  }

  function escapeHtml(s) {
    if (!s) return '';
    return String(s).replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/"/g,'&quot;');
  }

  function payloadFromForm(imagePathOverride) {
    var imgPath = imagePathOverride != null ? imagePathOverride : (document.getElementById('f-image').value.trim() || null);
    return JSON.stringify({
      name: document.getElementById('f-name').value.trim(),
      type: document.getElementById('f-type').value.trim(),
      price: parseFloat(document.getElementById('f-price').value) || 0,
      imagePath: imgPath,
      description: document.getElementById('f-desc').value,
      available: document.getElementById('f-available').checked,
      hasSeaView: document.getElementById('f-sea').checked,
      hasWifi: document.getElementById('f-wifi').checked,
      hasAc: document.getElementById('f-ac').checked,
      hasPoolAccess: document.getElementById('f-pool').checked,
      breakfastIncluded: document.getElementById('f-breakfast').checked
    });
  }

  function uploadSelectedImage() {
    var fileInput = document.getElementById('f-image-file');
    var file = fileInput.files && fileInput.files[0] ? fileInput.files[0] : null;
    if (!file) return Promise.resolve(null);
    var formData = new FormData();
    formData.append('image', file);
    return OVAdmin.api('/api/rooms/upload', {
      method: 'POST',
      body: formData
    }).then(function (res) {
      console.log('Room image upload response:', res);
      if (!res.ok || !res.data || !res.data.imagePath) {
        throw new Error((res.data && res.data.error) || 'Image upload failed');
      }
      return res.data.imagePath;
    });
  }

  document.getElementById('btn-add-room').addEventListener('click', function () { openModal(null); });
  document.getElementById('room-modal-close').addEventListener('click', closeModal);
  document.getElementById('room-modal-cancel').addEventListener('click', closeModal);
  modal.addEventListener('click', function (e) { if (e.target === modal) closeModal(); });
  document.getElementById('room-modal-save').addEventListener('click', function () {
    var id = document.getElementById('f-room-id').value;
    var method = id ? 'PUT' : 'POST';
    var path = id ? '/api/rooms/' + id : '/api/rooms';
    uploadSelectedImage().then(function (uploadedPath) {
      var body = payloadFromForm(uploadedPath);
      if (uploadedPath) {
        document.getElementById('f-image').value = uploadedPath;
      }
      return OVAdmin.api(path, { method: method, body: body });
    }).then(function (res) {
      if (res.ok) {
        OVAdmin.toast(id ? 'Room updated' : 'Room created');
        closeModal();
        loadRooms();
      } else {
        OVAdmin.toast((res.data && res.data.error) || 'Save failed', 'error');
      }
    }).catch(function (e) {
      console.log('Room save/upload error:', e);
      OVAdmin.toast(e && e.message ? e.message : 'Save failed', 'error');
    });
  });

  loadRooms();
})();
</script>
<jsp:include page="/admin/inc/wrap-end.jsp"/>
