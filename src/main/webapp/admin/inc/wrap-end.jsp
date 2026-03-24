<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    </main>
  </div>
  <footer class="admin-footer">
    <div class="admin-footer-inner">
      <div><h4>Ocean View Resort</h4><p>Admin</p></div>
    </div>
    <p class="admin-footer-copy">&copy; Ocean View Resort. CIS6003.</p>
  </footer>
<div id="ov-toast-host" class="ov-toast-host" aria-live="polite"></div>
<div id="ov-modal-overlay" class="ov-modal-overlay" hidden>
  <div class="ov-modal" role="dialog" aria-modal="true" aria-labelledby="ov-modal-title">
    <h2 id="ov-modal-title" class="ov-modal-title"></h2>
    <p id="ov-modal-body" class="ov-modal-body"></p>
    <div class="ov-modal-actions">
      <button type="button" class="btn btn-ghost" data-ov-modal-cancel>Cancel</button>
      <button type="button" class="btn btn-danger" data-ov-modal-confirm>Confirm</button>
    </div>
  </div>
</div>
</body>
</html>
