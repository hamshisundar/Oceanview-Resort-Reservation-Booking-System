# Ocean View Hotel Booking System — Testing

This document describes how to run tests and how testing fits into the assignment (CIS6003 WRIT1).

## Quick start

```bash
mvn test
```

Runs all JUnit 5 tests in `src/test/java`. No database is required for the current unit tests.

## Test structure

| Location | Content |
|----------|---------|
| `docs/03-TEST-PLAN.md` | Test plan: objectives, scope, types, workflow, TDD explanation, evaluation |
| `docs/04-TEST-CASES.md` | Detailed test cases (TC001–TC008+) for report and execution |
| `src/test/java/com/oceanview/services/BookingServiceTest.java` | Price and nights calculation (TC007, TC008) |
| `src/test/java/com/oceanview/services/UserServiceTest.java` | Login/register validation (no DB) |
| `src/test/java/com/oceanview/models/BookingTest.java` | Booking model |
| `src/test/java/com/oceanview/models/RoomTest.java` | Room model |

## Test types

1. **Unit (automated)**  
   - `BookingService.calculateTotalPrice(nights, pricePerNight)`  
   - `BookingService.calculateNights(checkIn, checkOut)`  
   - `UserService` login/register with null/empty inputs  
   - Model getters/setters  

2. **System / manual**  
   - Register → Login → Browse rooms → Book → Confirmation → History  
   - Admin: Dashboard → Rooms (add/edit/delete) → Bookings (update status) → Reports  

3. **Integration (optional)**  
   - Service + DAO with real MySQL; run with DB available or add test profile later.  

## TDD (Test-Driven Development)

- **Unit tests** lock behaviour of price and nights calculation and validation rules.  
- For new rules (e.g. discounts), write a failing test first, then implement until it passes.  
- For this project, tests were added alongside existing code to document and protect behaviour.  

## Report checklist (Task C)

- [ ] **Test rationale** — See `docs/03-TEST-PLAN.md` (objectives, scope, types).  
- [ ] **Test plan** — Same document; include in report.  
- [ ] **Test data** — Sample users, rooms, dates in `docs/04-TEST-CASES.md` and schema.  
- [ ] **Test cases** — TC001–TC008 (and optional TC009–TC012) in `docs/04-TEST-CASES.md`.  
- [ ] **Application of test plan** — Run `mvn test`; run manual workflow; fill “Actual result” and “Pass/Fail” in test cases.  
- [ ] **Test automation** — JUnit 5; show `mvn test` output (screenshot or log) in report.  
- [ ] **Evaluation** — Use “Evaluation of Testing” in `docs/03-TEST-PLAN.md` for conclusion.  
- [ ] **Traceability** — Use “Traceability” table in test plan to map requirements to tests.  

## Screenshots for report

Suggested captures:

**User:** Home, Room listing, Room details, Booking form, Booking confirmation, Booking history.  
**Admin:** Dashboard, Room management (list + add/edit), Booking management, Reports.
