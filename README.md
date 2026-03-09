# Ocean View Hotel Booking System

A web-based reservation platform for **Ocean View Resort**, Galle, Sri Lanka. Built for CIS6003 Advanced Programming (ICBT Campus) as a real-world style hotel booking system.

## Tech Stack

- **Frontend**: HTML5, CSS3, Vanilla JavaScript (no React/Angular/Vue)
- **Backend**: Java Servlets + JSP
- **Database**: MySQL (with H2 in-memory fallback when MySQL is not available)
- **Architecture**: MVC, 3-Tier

## Program functionalities (assignment order)

The system provides the following in this order:

1. **User authentication (login)** — Username (email) and password for secure system access. Use **Login** to sign in; **Register** to create an account.
2. **Add new reservation** — Store guest and booking details: reservation number, guest name, contact number, room type, check-in and check-out dates. Book from room details with dates; system generates a unique reservation number.
3. **Display reservation details** — Retrieve and display complete booking information for a specific reservation. Shown on the confirmation page and under **My Bookings**.
4. **Calculate and print bill** — Total stay cost = number of nights × room rate. Shown on confirmation and booking history. Use **Print bill** on the confirmation page (or browser Print) to print.
5. **Help section** — Guidelines for new staff: **Help** in the menu opens `help.jsp` with step-by-step instructions for all six functionalities.
6. **Exit system** — **Logout** in the menu ends the session safely.

## Features

- User registration and login
- Room browsing with filters (price, type, sea view, availability)
- Room details with amenities and images
- Date-based availability and booking with reservation number
- Booking confirmation and history; optional cancellation
- Admin: room CRUD, image upload, booking management, reports
- Help section and professional ocean/beach-themed UI

## Project Structure

```
OcenView/
├── docs/                    # Architecture and UML documentation
│   ├── 01-ARCHITECTURE.md
│   └── 02-UML-DIAGRAMS.md
├── database/
│   └── schema.sql           # MySQL schema and sample data
├── src/main/
│   ├── java/com/oceanview/
│   │   ├── controllers/     # Servlets
│   │   ├── services/        # Business logic
│   │   ├── dao/             # Data access
│   │   ├── models/          # Entities
│   │   └── utils/           # DB, validation, etc.
│   └── webapp/
│       ├── WEB-INF/         # web.xml, JSPs
│       ├── css/
│       ├── js/
│       ├── images/
│       ├── index.html
│       ├── rooms.html / .jsp
│       ├── login.jsp, register.jsp
│       └── admin/
├── pom.xml                  # Maven build
└── README.md
```

## Setup

**Deploy first (fixes “page not working” / 404 on db-status):** From project root run `./deploy.sh` For manual steps see `docs/DEPLOYMENT-TOMCAT.md`. This builds the WAR, removes the old Tomcat app, deploys the new one, and restarts Tomcat so all pages (including `/oceanview/db-status`) work.

1. **MySQL**: Create database and run `database/schema.sql`:
   ```bash
   mysql -u root -p < database/schema.sql
   ```
2. **Configure**: Set `db.url`, `db.user`, `db.password` in `src/main/webapp/WEB-INF/web.xml` (or use context/config file).
3. **Build**: `mvn clean package`
4. **Deploy**: Copy `target/oceanview.war` to Tomcat `webapps/`. **Remove the existing `webapps/oceanview/` folder** (and old `oceanview.war` if present) before copying the new WAR so Tomcat serves the new version. Then start Tomcat.
5. **Access**: `http://localhost:8080/oceanview/`

**UI not updating after redeploy?** Stop Tomcat, remove `webapps/oceanview/` and `webapps/oceanview.war`, copy the new WAR, start Tomcat, then hard refresh (Cmd+Shift+R). The app uses timestamp-based cache busting on startup. See `docs/DEPLOYMENT-TOMCAT.md` for the full workflow.

**Admin login:** Use a user with role `ADMIN`. Schema includes: `admin@oceanview.com` / `admin123`.  
If your DB was created before the `role` column existed, run `database/migration-001-add-role.sql`.

**Registration uses the built-in H2 database by default** — no MySQL required. You can register, log in, and book as soon as you deploy. To use MySQL instead, set `db.forceH2` to `false` in `WEB-INF/web.xml` and configure `db.url`, `db.user`, `db.password`. Check **http://localhost:8080/oceanview/db-status** to see which database is active.

## Backend (Servlets & JSP)

| URL | Method | Description |
|-----|--------|-------------|
| `/index.jsp` | GET | Home with featured rooms from DB |
| `/rooms` | GET | Room list with filters → `rooms.jsp` |
| `/room-details?id=` | GET | Single room → `room-details.jsp` |
| `/login` | POST | User login (email + password) |
| `/register` | POST | User registration |
| `/logout` | GET | Invalidate session |
| `/book-room` | POST | Create booking (requires login) |
| `/my-bookings` | GET | Booking history → `booking-history.jsp` |
| `/admin/dashboard` | GET | Admin dashboard (requires ADMIN role) |
| `/admin/rooms` | GET/POST | Room list; add/edit/delete rooms |
| `/admin/bookings` | GET/POST | All bookings; update status |
| `/admin/reports` | GET | Reports (revenue, most booked room) |

Forms in `login.jsp` and `register.jsp` POST to `/login` and `/register`. Room-details booking form POSTs to `/book-room` when the user is logged in. **Admin:** log in with an ADMIN user to access `/admin/dashboard`; `AdminFilter` protects all `/admin/*` URLs.

## Testing Flow

1. **Register** → open `/register.jsp`, submit form → redirects to login.
2. **Login** → open `/login.jsp`, submit (use a user created in DB or after register).
3. **Browse rooms** → open `/rooms` (or home), apply filters.
4. **Room details** → click a room → `/room-details?id=1`, set check-in/check-out, click "Check availability & book".
5. **Confirmation** → after POST to `/book-room` you are redirected to `booking-confirmation.jsp` with reservation number.
6. **Booking history** → open `/my-bookings` to see all bookings for the logged-in user.

**Note:** Sample data in `schema.sql` includes rooms and an admin user; create guest users via Register. Passwords are stored in plain text for this demo (use BCrypt in production).

## Testing (Step 8 — Task C)

- **Test plan and cases:** `docs/03-TEST-PLAN.md`, `docs/04-TEST-CASES.md`.
- **Run unit tests:** `mvn test` (JUnit 5; no DB required for current tests).
- **Summary:** `TESTING.md` — test structure, TDD, report checklist, screenshots.

Unit tests cover: price calculation, nights calculation, login/register validation (null/empty), and model getters/setters. Manual/system testing covers full guest and admin workflows.

## Assignment Alignment (CIS6003 WRIT1)

- **Task A**: UML diagrams and design → `docs/02-UML-DIAGRAMS.md`
- **Task B**: Distributed web app, design patterns, database → this codebase
- **Task C**: Test plan, TDD, test automation → to be added under `src/test` and docs
- **Task D**: Git repository, version control, workflows → use this project in a public repo and document in report

## License

Educational project — ICBT Campus / Cardiff Met.
