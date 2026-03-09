# Report Structure (4000 words) — CIS6003 WRIT1

Suggested structure for the final submission report. Word count is approximate; adjust to fit the 4000-word requirement.

---

## 1. Introduction (~400 words)

- Brief on Ocean View Resort and the need for a web-based reservation system.
- Objectives of the project: replace manual booking, provide guest and admin functions.
- Scope: user auth, room browsing, booking, confirmation, history, admin CRUD and reports.
- Outline of the report sections.

---

## 2. System Design — UML Diagrams (~600 words) — **Task A**

- **Use Case Diagram:** Actors (Guest, Admin), use cases (Register, Login, Browse Rooms, View Room Details, Make Reservation, View Booking History, Manage Rooms, Manage Bookings, Reports). Include <<include>> and <<extend>> where relevant. Explain design decisions.
- **Class Diagram:** Main classes (User, Room, Booking, Admin), attributes, methods, relationships (e.g. User 1—* Booking, Room 1—* Booking). DAOs and Services. Public/private and multiplicity where appropriate.
- **Sequence Diagram:** Room booking process (User → Controller → BookingService → DAO → Database → response). Explain flow and assumptions.
- Reference: `docs/02-UML-DIAGRAMS.md`.

---

## 3. System Architecture (~500 words) — **Task B**

- **MVC:** Model (entities, DAOs, services), View (JSP/HTML/CSS/JS), Controller (Servlets). Request flow.
- **3-Tier:** Presentation (JSP, static assets), Business (services), Data (DAOs, MySQL). Separation of concerns.
- **Design patterns:** DAO, Service layer, Front controller (servlets), Singleton (DB connection), DTO/entity models.
- **Technology stack:** HTML5, CSS3, Vanilla JS, Java Servlets, JSP, JSTL, MySQL.
- Reference: `docs/01-ARCHITECTURE.md`.

---

## 4. Implementation (~800 words) — **Task B**

- **Database:** Tables (users, rooms, bookings, admin), key fields, relationships. Schema and sample data (`database/schema.sql`). Role-based access (users.role).
- **Backend:** Servlets (login, register, rooms, room-details, book-room, my-bookings, admin/*). Services and DAOs. Session and admin filter.
- **Frontend:** Pages (home, rooms, room-details, booking, confirmation, history, login, register, help). Admin pages (dashboard, rooms, bookings, reports). Responsive layout and theme.
- **Key features:** Registration/login, room filters, booking with date validation and conflict check, reservation number, confirmation, booking history, admin room CRUD, booking status update, reports (revenue, most booked room).

---

## 5. Testing (~700 words) — **Task C**

- **Test plan:** Objectives, scope, test types (unit, integration, system, UAT). Reference `docs/03-TEST-PLAN.md`.
- **Test cases:** Summary table (TC001–TC008) with module, input, expected result, result (Pass/Fail). Reference `docs/04-TEST-CASES.md`.
- **TDD:** How unit tests were used (e.g. price and nights calculation, validation). Tests written to define and lock behaviour; run with `mvn test`.
- **Test automation:** JUnit 5, Surefire; list of test classes (BookingServiceTest, UserServiceTest, model tests). Include screenshot or excerpt of `mvn test` output.
- **Manual testing:** End-to-end workflow (register → login → browse → book → confirm → history; admin dashboard → rooms → bookings → reports). Screenshots checklist.
- **Evaluation:** System tested with manual and automated tests; core behaviour validated; booking conflicts handled; any issues and resolutions.

---

## 6. GitHub and Version Control (~400 words) — **Task D**

- Repository (public); link in report.
- Commits showing progression: initial structure, frontend, backend, booking logic, admin dashboard, testing.
- Version control practices: meaningful commit messages, branch workflow if used.
- Deployment/workflow: how the project is built (Maven) and deployed (WAR to Tomcat). Screenshot of repo or workflow if applicable.

---

## 7. Conclusion (~300 words)

- Summary of what was built: full 3-tier MVC booking system with guest and admin roles.
- Achievement of assignment objectives (UML, design patterns, database, testing, version control).
- Limitations (e.g. plain-text passwords, no payment integration) and possible future work (e.g. charts, payment gateway, email notifications).
- Reflection on software engineering practices (design, testing, documentation).

---

## References and Appendices

- **References:** Harvard style; cite module materials, Java/Servlet docs, any external sources.
- **Appendices (optional):** Screenshots (home, rooms, room details, booking, confirmation, history, admin dashboard, room management, booking management, reports); key code snippets; full test case table if not in main body.

---

**Document references in project:**

- Architecture: `docs/01-ARCHITECTURE.md`
- UML: `docs/02-UML-DIAGRAMS.md`
- Test plan: `docs/03-TEST-PLAN.md`
- Test cases: `docs/04-TEST-CASES.md`
- Testing run: `TESTING.md`, `mvn test`
