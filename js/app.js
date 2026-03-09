/**
 * Ocean View Hotel Booking System - Shared front-end logic
 * Vanilla JS only (no frameworks).
 */

(function () {
  'use strict';

  // ----- Date helpers (for validation and display) -----
  window.OceanView = window.OceanView || {};

  OceanView.todayISO = function () {
    return new Date().toISOString().slice(0, 10);
  };

  OceanView.parseDate = function (str) {
    if (!str) return null;
    var d = new Date(str);
    return isNaN(d.getTime()) ? null : d;
  };

  OceanView.daysBetween = function (startStr, endStr) {
    var start = OceanView.parseDate(startStr);
    var end = OceanView.parseDate(endStr);
    if (!start || !end || end <= start) return 0;
    var ms = end - start;
    return Math.floor(ms / (24 * 60 * 60 * 1000));
  };

  // ----- Client-side validation -----
  OceanView.validateBookingDates = function (checkIn, checkOut) {
    var today = OceanView.todayISO();
    if (!checkIn || checkIn < today) {
      return { valid: false, message: 'Check-in date must be today or in the future.' };
    }
    if (!checkOut || checkOut <= checkIn) {
      return { valid: false, message: 'Check-out date must be after check-in.' };
    }
    return { valid: true };
  };

  OceanView.validateRequired = function (value, fieldName) {
    if (value == null || String(value).trim() === '') {
      return fieldName + ' is required.';
    }
    return null;
  };

  OceanView.validateEmail = function (email) {
    var re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(String(email).trim()) ? null : 'Please enter a valid email address.';
  };

  // ----- Show error in form -----
  OceanView.showFieldError = function (inputId, message) {
    var input = document.getElementById(inputId);
    if (!input) return;
    var existing = input.parentNode.querySelector('.error-msg');
    if (existing) existing.remove();
    if (message) {
      var span = document.createElement('span');
      span.className = 'error-msg';
      span.textContent = message;
      input.parentNode.appendChild(span);
    }
  };

  // ----- Booking form handler (for booking.html / room-details with form) -----
  function initBookingForm() {
    var form = document.getElementById('booking-form');
    if (!form) return;

    var checkIn = document.getElementById('check-in');
    var checkOut = document.getElementById('check-out');
    var today = OceanView.todayISO();

    if (checkIn && !checkIn.min) checkIn.min = today;
    if (checkIn && checkIn.value) {
      if (checkOut && !checkOut.min) checkOut.min = checkIn.value;
    }
    if (checkIn) {
      checkIn.addEventListener('change', function () {
        if (checkOut) {
          checkOut.min = checkIn.value;
          if (checkOut.value && checkOut.value <= checkIn.value) checkOut.value = '';
        }
      });
    }

    form.addEventListener('submit', function (e) {
      var cin = checkIn && checkIn.value;
      var cout = checkOut && checkOut.value;
      var result = OceanView.validateBookingDates(cin, cout);
      if (!result.valid) {
        e.preventDefault();
        OceanView.showFieldError('check-in', result.message);
        OceanView.showFieldError('check-out', result.message);
        return false;
      }
    });
  }

  // ----- Filter form (rooms page) - optional submit on change -----
  function initFilters() {
    var form = document.getElementById('filters-form');
    if (!form) return;
    var autoSubmit = form.querySelector('[data-auto-submit]');
    if (autoSubmit) {
      form.querySelectorAll('select, input').forEach(function (el) {
        el.addEventListener('change', function () { form.submit(); });
      });
    }
  }

  // ----- Login/Register form validation -----
  function initAuthForms() {
    document.querySelectorAll('form[id="login-form"], form[id="register-form"]').forEach(function (form) {
      form.addEventListener('submit', function (e) {
        var valid = true;
        form.querySelectorAll('[required]').forEach(function (input) {
          var msg = OceanView.validateRequired(input.value, input.labels && input.labels[0] ? input.labels[0].textContent : 'Field');
          OceanView.showFieldError(input.id, msg);
          if (msg) valid = false;
        });
        if (form.id === 'register-form') {
          var email = form.querySelector('#email');
          if (email && email.value) {
            var emailErr = OceanView.validateEmail(email.value);
            if (emailErr) {
              OceanView.showFieldError('email', emailErr);
              valid = false;
            }
          }
        }
        if (!valid) e.preventDefault();
      });
    });
  }

  // ----- Run inits when DOM ready -----
  function init() {
    initBookingForm();
    initFilters();
    initAuthForms();
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }
})();
