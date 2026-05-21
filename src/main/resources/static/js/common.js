/* ────────────────────────────────────────────────────────────
   Classic Model Business — Common JS utilities
   Toast · Delete Modal · Tab Switching · Delete button delegation
   ──────────────────────────────────────────────────────────── */

/* ── Toast ─────────────────────────────────────────────────── */
function showToast(msg, type) {
    type = type || 'success';
    let container = document.querySelector('.toast-container');
    if (!container) {
        container = document.createElement('div');
        container.className = 'toast-container';
        document.body.appendChild(container);
    }
    const t = document.createElement('div');
    t.className = 'toast'
        + (type === 'error' ? ' toast-error' : '')
        + (type === 'warn'  ? ' toast-warn'  : '');
    t.textContent = msg;
    container.appendChild(t);
    setTimeout(() => {
        t.style.opacity = '0';
        setTimeout(() => t.remove(), 400);
    }, 2800);
}

/* ── Delete Confirm Modal ───────────────────────────────────── */
function confirmDelete(formId, entityName) {
    const overlay = document.createElement('div');
    overlay.className = 'modal-overlay';
    overlay.innerHTML =
        '<div class="modal-box">' +
        '  <div class="modal-title">⚠ Confirm Delete</div>' +
        '  <div class="modal-msg">Are you sure you want to delete <strong>' + entityName + '</strong>?<br>This action cannot be undone.</div>' +
        '  <div class="modal-actions">' +
        '    <button class="btn btn-danger" id="modal-confirm-btn">Delete</button>' +
        '    <button class="btn" id="modal-cancel-btn">Cancel</button>' +
        '  </div>' +
        '</div>';
    document.body.appendChild(overlay);
    document.getElementById('modal-confirm-btn').addEventListener('click', function() {
        document.getElementById(formId).submit();
        overlay.remove();
    });
    document.getElementById('modal-cancel-btn').addEventListener('click', function() {
        overlay.remove();
    });
    overlay.addEventListener('click', function(e) {
        if (e.target === overlay) overlay.remove();
    });
}

/* ── Tab Switching ──────────────────────────────────────────── */
function switchTab(prefix, tab, clickedEl) {
    document.querySelectorAll('[id^="' + prefix + '-"]').forEach(function(p) {
        p.classList.remove('on');
    });
    if (clickedEl) {
        clickedEl.closest('.tab-bar').querySelectorAll('.tab-item').forEach(function(t) {
            t.classList.remove('on');
        });
        clickedEl.classList.add('on');
    }
    var panel = document.getElementById(prefix + '-' + tab);
    if (panel) panel.classList.add('on');
}

/* ── DOMContentLoaded ───────────────────────────────────────── */
document.addEventListener('DOMContentLoaded', function() {

    /* Auto-show flash toasts from data-flash attributes */
    var flashEls = document.querySelectorAll('[data-flash]');
    flashEls.forEach(function(el) {
        var msg  = el.getAttribute('data-flash');
        var type = el.getAttribute('data-flash-type') || 'success';
        if (msg) showToast(msg, type);
    });

    /* Event delegation for delete buttons (avoids th:onclick security restriction) */
    document.addEventListener('click', function(e) {
        var btn = e.target.closest('.del-btn');
        if (!btn) return;
        var formId     = btn.getAttribute('data-form-id');
        var entityName = btn.getAttribute('data-entity-name');
        if (formId && entityName) {
            confirmDelete(formId, entityName);
        }
    });

});
