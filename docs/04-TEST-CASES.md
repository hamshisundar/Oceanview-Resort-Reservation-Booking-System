# Ocean View Hotel Booking System — Test Cases

Detailed test cases for the report and for manual/automated execution.

---

## TC001 — User Registration

| Field | Value |
|-------|--------|
| **Test ID** | TC001 |
| **Module** | User Registration |
| **Precondition** | Application running; database with `users` table (with `role` column). |
| **Input** | Name: "Test User", Email: "test@example.com", Phone: "+94771234567", Password: "test123" |
| **Steps** | 1. Open `/register.jsp`. 2. Fill form and submit. |
| **Expected result** | User account created; redirect to `/login.jsp?registered=1`. New row in `users` with role `USER`. |
| **Actual result** | _(To be filled)_ |
| **Pass / Fail** | |

---

## TC002 — User Login

| Field | Value |
|-------|--------|
| **Test ID** | TC002 |
| **Module** | Login |
| **Precondition** | User exists (e.g. from TC001 or schema admin user). |
| **Input** | Email: "admin@oceanview.com", Password: "admin123" (admin) or valid guest credentials. |
| **Steps** | 1. Open `/login.jsp`. 2. Enter email and password; submit. |
| **Expected result** | Session created with `user` attribute; ADMIN → redirect to `/admin/dashboard`, USER → redirect to `/index.jsp`. |
| **Actual result** | _(To be filled)_ |
| **Pass / Fail** | |

---

## TC003 — Room Search (Filters)

| Field | Value |
|-------|--------|
| **Test ID** | TC003 |
| **Module** | Room Search |
| **Precondition** | Sample rooms in DB (from `schema.sql`). |
| **Input** | Filter: Room type = "Ocean View Room", or Min price = 100, Max price = 200, or Sea view = Yes. |
| **Steps** | 1. Open `/rooms`. 2. Set filters; click "Apply filters". |
| **Expected result** | Only rooms matching criteria are displayed. |
| **Actual result** | _(To be filled)_ |
| **Pass / Fail** | |

---

## TC004 — Booking Creation

| Field | Value |
|-------|--------|
| **Test ID** | TC004 |
| **Module** | Booking System |
| **Precondition** | User logged in; room exists; no overlapping booking for chosen dates. |
| **Input** | Room ID (e.g. 1), Check-in: future date, Check-out: after check-in. |
| **Steps** | 1. Open `/room-details?id=1`. 2. Select check-in and check-out. 3. Submit "Check availability & book". |
| **Expected result** | Booking saved in `bookings`; redirect to confirmation page with reservation number (e.g. OV-YYYYMMDD-XXX), room, dates, total. |
| **Actual result** | _(To be filled)_ |
| **Pass / Fail** | |

---

## TC005 — Booking Conflict (Overlapping Dates)

| Field | Value |
|-------|--------|
| **Test ID** | TC005 |
| **Module** | Booking Validation |
| **Precondition** | Existing booking for room X on dates A–B. |
| **Input** | Same room X, check-in/check-out overlapping A–B (e.g. same dates or within range). |
| **Steps** | 1. Create first booking (or use existing). 2. Attempt second booking for same room with overlapping dates. |
| **Expected result** | Second booking rejected; redirect to room-details with `?error=unavailable` or equivalent; no duplicate row in `bookings`. |
| **Actual result** | _(To be filled)_ |
| **Pass / Fail** | |

---

## TC006 — Admin Add Room

| Field | Value |
|-------|--------|
| **Test ID** | TC006 |
| **Module** | Admin Panel (Room CRUD) |
| **Precondition** | Admin logged in. |
| **Input** | Room name: "Test Room", Type: "Standard Room", Price: 99.00, description and amenities as desired. |
| **Steps** | 1. Open `/admin/rooms`. 2. Click "Add Room". 3. Fill form; submit. |
| **Expected result** | New row in `rooms`; redirect to `/admin/rooms`; new room appears in table. |
| **Actual result** | _(To be filled)_ |
| **Pass / Fail** | |

---

## TC007 — Unit Test: Price Calculation

| Field | Value |
|-------|--------|
| **Test ID** | TC007 |
| **Module** | Booking (business logic) |
| **Type** | Automated (JUnit) |
| **Input** | nights = 2, pricePerNight = 150 |
| **Expected result** | `BookingService.calculateTotalPrice(2, 150)` returns 300.0. |
| **Actual result** | Run `mvn test` — `BookingServiceTest.testCalculateTotalPrice`. |
| **Pass / Fail** | |

---

## TC008 — Unit Test: Nights Calculation

| Field | Value |
|-------|--------|
| **Test ID** | TC008 |
| **Module** | Booking (business logic) |
| **Type** | Automated (JUnit) |
| **Input** | checkIn = "2025-06-01", checkOut = "2025-06-04" |
| **Expected result** | `BookingService.calculateNights("2025-06-01", "2025-06-04")` returns 3. |
| **Actual result** | Run `mvn test` — `BookingServiceTest.testCalculateNights`. |
| **Pass / Fail** | |

---

## Additional Test Cases (optional for report)

- **TC009 — Admin Edit Room:** Edit name/price/amenities; save; verify in list and DB.
- **TC010 — Admin Delete Room:** Delete room (with confirm); verify removed from list and DB.
- **TC011 — Admin Update Booking Status:** Change status to CANCELLED/COMPLETED; verify in list and DB.
- **TC012 — Reports:** Dashboard and `/admin/reports` show correct total rooms, total bookings, revenue this month, most booked room.

---

## Screenshots Checklist (for report)

**User side:** Home, Room listing (with filters), Room details, Booking form, Booking confirmation, Booking history.  
**Admin side:** Admin dashboard, Room management (list + add/edit), Booking management, Reports page.
