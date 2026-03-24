(function () {
  'use strict';

  function getContextPath() {
    var body = document.body;
    if (body) {
      var fromAttr = body.getAttribute('data-context');
      if (fromAttr != null) return fromAttr;
    }
    // Fallback: infer from URL (e.g. /oceanview/admin/dashboard -> /oceanview)
    var path = window.location.pathname || '';
    var parts = path.split('/').filter(Boolean);
    return parts.length > 0 ? '/' + parts[0] : '';
  }

  function toast(message, type) {
    var host = document.getElementById('ov-toast-host');
    if (!host) return;
    var el = document.createElement('div');
    el.className = 'ov-toast ov-toast-' + (type === 'error' ? 'error' : 'success');
    el.textContent = message;
    host.appendChild(el);
    setTimeout(function () {
      el.style.opacity = '0';
      el.style.transition = 'opacity 0.25s';
      setTimeout(function () { el.remove(); }, 280);
    }, 4200);
  }

  function confirmDialog(title, bodyText) {
    return new Promise(function (resolve) {
      var overlay = document.getElementById('ov-modal-overlay');
      if (!overlay) {
        resolve(window.confirm(bodyText));
        return;
      }
      var t = overlay.querySelector('#ov-modal-title');
      var b = overlay.querySelector('#ov-modal-body');
      var cancel = overlay.querySelector('[data-ov-modal-cancel]');
      var ok = overlay.querySelector('[data-ov-modal-confirm]');
      if (t) t.textContent = title;
      if (b) b.textContent = bodyText;
      overlay.hidden = false;

      function cleanup(result) {
        overlay.hidden = true;
        cancel.removeEventListener('click', onCancel);
        ok.removeEventListener('click', onOk);
        overlay.removeEventListener('click', onBackdrop);
        resolve(result);
      }
      function onCancel() { cleanup(false); }
      function onOk() { cleanup(true); }
      function onBackdrop(ev) {
        if (ev.target === overlay) cleanup(false);
      }
      cancel.addEventListener('click', onCancel);
      ok.addEventListener('click', onOk);
      overlay.addEventListener('click', onBackdrop);
    });
  }

  function api(path, options) {
    var opts = options || {};
    var headers = opts.headers || {};
    if (!headers['Content-Type'] && opts.body && typeof opts.body === 'string') {
      headers['Content-Type'] = 'application/json';
    }
    var ctx = getContextPath();
    return fetch(ctx + path, {
      credentials: 'same-origin',
      headers: headers,
      method: opts.method || 'GET',
      body: opts.body
    }).then(function (res) {
      var ct = res.headers.get('Content-Type') || '';
      var isJson = ct.indexOf('application/json') !== -1;
      return res.text().then(function (text) {
        var data = null;
        if (text && isJson) {
          try { data = JSON.parse(text); } catch (e) { data = null; }
        }
        return { ok: res.ok, status: res.status, data: data, raw: text };
      });
    });
  }

  function formatMoney(n) {
    var x = Number(n);
    if (isNaN(x)) x = 0;
    return 'Rs. ' + Math.round(x).toLocaleString('en-LK');
  }

  function badgeClass(status) {
    var s = (status || '').toUpperCase();
    if (s === 'PENDING') return 'badge badge-pending';
    if (s === 'CANCELLED') return 'badge badge-cancelled';
    if (s === 'COMPLETED') return 'badge badge-completed';
    return 'badge badge-confirmed';
  }

  window.OVAdmin = {
    contextPath: getContextPath(),
    api: api,
    toast: toast,
    confirmDialog: confirmDialog,
    formatMoney: formatMoney,
    badgeClass: badgeClass
  };
})();
