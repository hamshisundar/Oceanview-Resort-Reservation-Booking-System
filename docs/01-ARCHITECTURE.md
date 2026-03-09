# Ocean View Hotel Booking System — Architecture

## 1. System Overview

**Ocean View Resort** is a beachside hotel in Galle, Sri Lanka. This system is a web-based reservation platform that replaces manual booking management. It allows guests to browse rooms, check availability, make reservations, and view booking history. Administrators can manage rooms, upload images, and view reports.

---

## 2. Architecture Styles

### 2.1 MVC (Model–View–Controller)

- **Model**: Domain entities and business logic (User, Room, Booking, etc.) and data access (DAOs).
- **View**: JSP pages and static HTML/CSS/JS that render the UI.
- **Controller**: Servlets that handle HTTP requests, call services, and forward to views or redirect.

Flow: **Browser → Servlet (Controller) → Service (Model/Business) → DAO (Model/Data) → DB**. Response: **Servlet → JSP/HTML (View) → Browser**.

### 2.2 3-Tier Architecture

| Tier | Layer | Technology | Responsibility |
|------|--------|------------|----------------|
| **1** | Presentation | HTML5, CSS3, Vanilla JS, JSP | UI, forms, client-side validation, display |
| **2** | Business Logic | Java services, filters, utils | Validation, booking rules, pricing, availability |
| **3** | Data | DAOs, JDBC, MySQL | Persistence, queries, transactions |

- **Presentation** does not access the database.
- **Business Logic** does not know about HTTP or JSP.
- **Data** layer only handles SQL and connections.

---

## 3. Layer Responsibilities

### Presentation Layer
- Servlets: map URLs, parse request params, call services, set request/session attributes, forward/redirect.
- JSP/HTML: show pages (home, room list, room detail, booking form, confirmation, login, register, admin).
- CSS/JS: layout, responsiveness, client-side validation, AJAX for availability/search if needed.

### Business Logic Layer
- **UserService**: register, login, session checks.
- **RoomService**: list rooms, get by id, filter by type/price/availability.
- **BookingService**: check availability, create booking, compute total, cancel.
- **AdminService**: CRUD rooms, manage bookings, reports.
- DTOs/VOs for passing data between layers.

### Data Layer
- **UserDAO**, **RoomDAO**, **BookingDAO**, **AdminDAO**: JDBC access to MySQL.
- **DBConnection**: connection pool or single connection management.
- No business rules in DAOs; only SQL and result mapping.

---

## 4. Design Patterns Used

| Pattern | Use in system |
|--------|----------------|
| **MVC** | Separation of Model (entities + DAOs + services), View (JSP/HTML), Controller (Servlets). |
| **DAO** | Each entity has a DAO (e.g. `BookingDAO`) to encapsulate all DB access. |
| **Service Layer** | Services encapsulate business rules and use DAOs; Servlets use services only. |
| **Front Controller** | Optional single servlet with action parameter; or one servlet per main feature (e.g. `BookingServlet`). |
| **Singleton** | DB connection manager (e.g. one `DataSource` or `Connection` manager). |
| **DTO/VO** | Data transfer between layers (e.g. `BookingDTO` with check-in, check-out, total price). |

---

## 5. Technology Stack

- **Frontend**: HTML5, CSS3, Vanilla JavaScript (no React/Angular/Vue).
- **Backend**: Java Servlets, JSP.
- **Database**: MySQL.
- **Server**: Any Servlet container (e.g. Apache Tomcat 9+).
- **Build**: Maven (optional) for dependencies and WAR packaging.

---

## 6. Security and Session Management

- **Sessions**: Login stores user id (and role) in `HttpSession`; filters protect guest and admin URLs.
- **Passwords**: Stored hashed (e.g. BCrypt or SHA-256 with salt); never plain text.
- **Validation**: Server-side validation for all inputs (dates, room id, guest details); client-side for UX.
- **Admin**: Separate admin login and admin-only servlets/pages; role checked in filter or servlet.

---

## 7. Deployment View

- **WAR** deployed on Tomcat.
- **MySQL** runs separately; connection via JDBC URL in config (e.g. context params or properties file).
- **Static assets**: served from `/webapp/` (e.g. `/css/`, `/js/`, `/images/`).

This document is the basis for the UML diagrams (Step 2), database design (Step 3), and implementation (Steps 4–8).
