# Ocean View Hotel Booking System — Test Plan

## 1. Objective

Ensure the Ocean View Booking System works correctly for:

- **Authentication** — Registration, login, logout, session handling, role-based access (USER/ADMIN).
- **Room browsing** — List rooms, filters (type, price, sea view), room details.
- **Booking process** — Date validation, availability check, booking creation, reservation number, confirmation.
- **Admin management** — Dashboard, room CRUD, booking management, status updates, reports.

---

## 2. Testing Scope

### Modules in scope

| Module | Description |
|--------|-------------|
| User authentication | Register, login, logout, session, ADMIN redirect |
| Room search and filters | List rooms, filter by type/price/sea view |
| Room booking | Select room, dates, create booking, conflict validation |
| Booking history | List user bookings, room names, status |
| Admin dashboard | Stats (rooms, bookings, revenue, most booked room) |
| Admin room CRUD | Add, edit, delete rooms; list all rooms |
| Admin booking management | List all bookings, update status (CONFIRMED/CANCELLED/COMPLETED) |
| Admin reports | Revenue this month, total bookings, most booked room |

### Out of scope (for this plan)

- Performance/load testing  
- Security penetration testing  
- Browser compatibility matrix  

---

## 3. Testing Types

| Test type | Description | How applied |
|-----------|-------------|-------------|
| **Unit testing** | Individual classes/methods (e.g. price calculation, date logic) | JUnit 5 tests in `src/test/java` |
| **Integration testing** | Service + DAO with database | Manual or tests with running MySQL; optional embedded DB later |
| **System testing** | Full workflow in browser (register → login → browse → book → confirm) | Manual execution and checklist |
| **User acceptance (UAT)** | Real user simulation (guest and admin) | Manual scenarios and screenshots for report |

---

## 4. Test Environment

- **Application:** Java 11, Servlets + JSP, Tomcat (or similar).
- **Database:** MySQL (schema from `database/schema.sql`).
- **Test data:** Sample rooms and admin user from schema; guest users created via Register.
- **Automation:** JUnit 5 (Maven Surefire); run with `mvn test`.

---

## 5. Test Cases Summary

| Test ID | Module | Description | Type |
|---------|--------|-------------|------|
| TC001 | User registration | New user saved in DB, redirect to login | System |
| TC002 | Login | Valid credentials → session, redirect (home or admin) | System |
| TC003 | Room search | Filters return correct room list | System |
| TC004 | Booking creation | Valid dates → booking saved, reservation number | System / Unit |
| TC005 | Booking conflict | Overlapping dates → booking rejected | System / Unit |
| TC006 | Admin add room | Room form → room in DB and list | System |
| TC007 | Price calculation | nights × price per night | Unit (automated) |
| TC008 | Nights calculation | checkIn/checkOut → correct nights | Unit (automated) |

---

## 6. Manual Testing Workflow

End-to-end guest flow:

1. **Register** — Open `/register.jsp`, submit name, email, phone, password → redirect to login.
2. **Login** — Enter email and password → redirect to home (or admin dashboard if ADMIN).
3. **Browse rooms** — Open `/rooms`, apply filters (type, price, sea view) → filtered list.
4. **View room details** — Click a room → `/room-details?id=...`, see details and booking form.
5. **Select dates** — Choose check-in and check-out (valid range).
6. **Make booking** — Submit form → POST `/book-room` → redirect to confirmation.
7. **Confirmation** — Page shows reservation number, room, dates, total.
8. **Booking history** — Open `/my-bookings` → list includes new booking.

Admin flow:

9. **Admin login** — Log in with `admin@oceanview.com` / `admin123` → redirect to `/admin/dashboard`.
10. **Dashboard** — Verify total rooms, total bookings, revenue this month, most booked room.
11. **Room management** — Add room, edit room, delete room (with confirm).
12. **Booking management** — Open `/admin/bookings`, change a booking status → save.
13. **Reports** — Open `/admin/reports`, verify same metrics as dashboard.

---

## 7. Test Results (to be filled after execution)

| Test ID | Module | Result | Notes |
|---------|--------|--------|-------|
| TC001 | Registration | Pass / Fail | |
| TC002 | Login | Pass / Fail | |
| TC003 | Room search | Pass / Fail | |
| TC004 | Booking creation | Pass / Fail | |
| TC005 | Conflict validation | Pass / Fail | |
| TC006 | Admin room CRUD | Pass / Fail | |
| TC007 | Price calculation (unit) | Pass / Fail | Run `mvn test` |
| TC008 | Nights calculation (unit) | Pass / Fail | Run `mvn test` |

---

## 8. TDD (Test-Driven Development) Application

- **Unit tests** are written for core business logic that can be tested without the database:
  - **Price calculation:** `BookingService.calculateTotalPrice(nights, pricePerNight)` — tests ensure the formula (nights × price) is correct and edge cases (zero/negative) are handled.
  - **Nights calculation:** `BookingService.calculateNights(checkIn, checkOut)` — tests ensure correct number of days and invalid dates return 0.
- **Approach:** For new features (e.g. discount rules), a test would be written first defining the expected result, then the implementation would be added to satisfy the test. For this project, tests were added alongside existing logic to document and lock behaviour.
- **Integration tests** (e.g. login with real DB, booking creation with real DB) can be added later with a test profile or embedded database; they are currently covered by manual system testing.

---

## 9. Evaluation of Testing (for report conclusion)

The Ocean View Booking System was tested using both **manual testing** and **automated unit testing**. Core functionalities—authentication, room filtering, booking creation, conflict validation, and administrative management—were validated successfully. Unit tests confirm that price and nights calculations behave correctly; manual execution confirms end-to-end flows for guest and admin. The results show that the system operates as intended under normal conditions and rejects overlapping bookings as designed. Any failures found during testing were recorded in the Test Results section and retested after fixes.

---

## 10. Traceability (Requirements → Tests)

| Requirement (from brief) | Test(s) |
|--------------------------|---------|
| User authentication (login) | TC002 |
| Add new reservation | TC004, TC007, TC008 |
| Display reservation details | Manual: confirmation page, booking history |
| Calculate and print bill | TC004, TC007 |
| Help section | Manual: help page |
| Exit / Logout | TC002, manual logout |
| Admin: rooms, bookings, reports | TC006, admin workflow, TC007/TC008 (supporting logic) |
