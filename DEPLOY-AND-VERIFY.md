# Ocean View — Build, Deploy & Verify Guide

Use this guide to build, deploy, and test the application locally before pushing to GitHub and taking report screenshots.

---

## 1. Verify Project Structure

**Expected layout:**

```
OcenView/
├── pom.xml
├── src/main/
│   ├── java/com/oceanview/
│   │   ├── controllers/          ✓ (Login, Register, Room, RoomDetails, Booking, BookingHistory, Logout)
│   │   ├── controllers/admin/    ✓ (AdminDashboard, AdminRoom, AdminBooking, AdminReport)
│   │   ├── services/             ✓ (User, Room, Booking)
│   │   ├── dao/                  ✓ (User, Room, Booking)
│   │   ├── models/               ✓ (User, Room, Booking)
│   │   ├── utils/                ✓ (DBConnection, DBContextListener)
│   │   └── filters/              ✓ (AdminFilter)
│   └── webapp/
│       ├── WEB-INF/web.xml       ✓
│       ├── index.jsp             ✓
│       ├── rooms.jsp             ✓
│       ├── room-details.jsp      ✓
│       ├── booking-confirmation.jsp ✓
│       ├── booking-history.jsp   ✓
│       ├── login.jsp             ✓
│       ├── register.jsp          ✓
│       ├── help.html             ✓ (Help is HTML, not JSP — links point to /help.html)
│       ├── admin/
│       │   ├── dashboard.jsp     ✓
│       │   ├── rooms.jsp         ✓
│       │   ├── add-room.jsp      ✓
│       │   ├── edit-room.jsp     ✓
│       │   ├── bookings.jsp      ✓
│       │   └── reports.jsp       ✓
│       ├── css/styles.css        ✓
│       └── js/app.js             ✓
├── database/
│   ├── schema.sql                ✓
│   └── migration-001-add-role.sql ✓
└── docs/
    ├── 03-TEST-PLAN.md           ✓
    ├── 04-TEST-CASES.md          ✓
    └── 05-REPORT-STRUCTURE.md    ✓
```

**Verification:** All of the above exist. Help is **help.html** (not help.jsp); the app already links to it correctly.

---

## 2. Setup Database

**2.1 Start MySQL** (if not running).

**2.2 Create database and load schema:**

```bash
cd /Users/hamshi/Documents/OcenView
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS oceanview_db;"
mysql -u root -p oceanview_db < database/schema.sql
```

If your MySQL user is not `root` or has no password, adjust:

- No password: `mysql -u root oceanview_db < database/schema.sql`
- With password: you will be prompted after `-p`.

**2.3 Verify tables:**

```bash
mysql -u root -p -e "USE oceanview_db; SHOW TABLES;"
```

Expected: `admin`, `bookings`, `payments`, `rooms`, `users`.

**2.4 Verify admin account:**

```bash
mysql -u root -p -e "USE oceanview_db; SELECT user_id, name, email, role FROM users WHERE role='ADMIN';"
```

Expected one row: **admin@oceanview.com**, role **ADMIN**. Password in DB is **admin123** (plain text for demo).

---

## 3. Verify Database Connection (web.xml)

**File:** `src/main/webapp/WEB-INF/web.xml`

Current config:

- **db.url:** `jdbc:mysql://localhost:3306/oceanview_db?useSSL=false&serverTimezone=UTC`
- **db.user:** `root`
- **db.password:** empty `</param-value>`

**If your MySQL root has a password:** edit `web.xml` and set:

```xml
<param-value>YOUR_MYSQL_PASSWORD</param-value>
```

inside the `db.password` context-param. Then rebuild (Step 4).

**DBContextListener:** Already configured; it runs on startup and loads these params into `DBConnection`.

---

## 4. Build the Project

```bash
cd /Users/hamshi/Documents/OcenView
mvn clean package
```

**Expected:**

- Build succeeds (no errors).
- WAR created: **target/oceanview.war**

**If build fails:**

- Check Java: `java -version` (need 11+).
- Check Maven: `mvn -v`.
- Read the error; often it’s a missing dependency (Maven will download if online).

---

## 5. Deploy to Tomcat

**5.1 Locate Tomcat** (e.g. `/opt/tomcat`, `~/apache-tomcat-9.x`, or `CATALINA_HOME`).

**5.2 Copy WAR:**

```bash
cp target/oceanview.war $CATALINA_HOME/webapps/
```

Or, for example:

```bash
cp target/oceanview.war /opt/tomcat/webapps/
```

**5.3 Start Tomcat:**

```bash
$CATALINA_HOME/bin/startup.sh    # macOS/Linux
# or
%CATALINA_HOME%\bin\startup.bat  # Windows
```

**5.4 Wait for deploy:** Tomcat unpacks `oceanview.war` into `webapps/oceanview/`. Wait 10–30 seconds.

**Where to check logs:**

- **Tomcat log:** `$CATALINA_HOME/logs/catalina.out` (or `localhost.*.log`).
- Look for: “Deployment of web application [oceanview] has finished” or similar.
- **Errors:** Same file; search for “Exception” or “Error”. If you see “DBContextListener” or “MySQL”, fix DB URL/user/password in `web.xml` and redeploy.

---

## 6. Launch the Application

**Base URL:**  
**http://localhost:8080/oceanview/**

(If Tomcat runs on another port, use that port.)

**Pages to verify:**

| Page | URL | What to check |
|------|-----|----------------|
| Home | http://localhost:8080/oceanview/ or .../index.jsp | Hero, featured rooms from DB |
| Rooms | http://localhost:8080/oceanview/rooms | Room list, filters |
| Room details | http://localhost:8080/oceanview/room-details?id=1 | One room, booking form |
| Login | http://localhost:8080/oceanview/login.jsp | Login form |
| Register | http://localhost:8080/oceanview/register.jsp | Register form |
| Booking confirmation | Shown after booking | Reservation number, summary |
| Booking history | http://localhost:8080/oceanview/my-bookings | After login |
| Help | http://localhost:8080/oceanview/help.html | Help content |
| Admin dashboard | http://localhost:8080/oceanview/admin/dashboard | After admin login |

If you get 404: confirm Tomcat is running and `oceanview.war` is under `webapps/` (and that the context path is `/oceanview`).

---

## 7. Test User Booking Flow

1. **Register:** Open **Register**, fill name, email, phone, password → Submit. Expect redirect to Login.
2. **Login:** Enter the same email and password → Submit. Expect redirect to Home (or index).
3. **Browse rooms:** Click **Rooms** (or open `/oceanview/rooms`). Try filters (type, price, sea view) → Apply. List should update.
4. **Room details:** Click a room (e.g. “Ocean View Premium”) → **room-details?id=2** (or similar). Check-in/check-out form visible.
5. **Select dates:** Choose check-in (today or future) and check-out (after check-in).
6. **Submit booking:** Click “Check availability & book”. Expect redirect to **booking-confirmation** with:
   - A **reservation number** (e.g. OV-YYYYMMDD-XXX)
   - Room, dates, total
7. **Booking history:** Open **My Bookings** (or `/oceanview/my-bookings`). New booking appears with same reservation number.

**Confirm in MySQL:**

```bash
mysql -u root -p -e "USE oceanview_db; SELECT booking_id, reservation_number, user_id, room_id, check_in, check_out, total_price, status FROM bookings ORDER BY booking_id DESC LIMIT 5;"
```

You should see the new row; `BookingService.createBooking()` and the DAO persist it.

---

## 8. Test Admin Panel

1. **Logout** (if logged in as guest).
2. **Login as admin:** Email **admin@oceanview.com**, Password **admin123** → Submit. Expect redirect to **Admin dashboard**.
3. **Dashboard:** Total rooms, total bookings, revenue this month, most booked room (numbers may be 0 if no/few bookings).
4. **Add room:** **Rooms** → **Add Room** → Fill form (name, type, price, etc.) → Submit. New room appears in **Rooms** list.
5. **Edit room:** **Rooms** → **Edit** on a room → Change name or price → Update. List shows updated data.
6. **Delete room:** **Rooms** → **Delete** on a room (prefer one you added for testing) → Confirm. Room disappears from list.
7. **View bookings:** **Bookings** → Table of all bookings (reservation #, room, guest, dates, status).
8. **Update status:** In **Bookings**, change a booking’s status (e.g. to CANCELLED or COMPLETED) → **Update**. Status updates in list and in DB.
9. **Reports:** **Reports** → Same kind of stats as dashboard (total rooms, bookings, revenue this month, most booked room).

---

## 9. Validate UI

- **Theme:** Ocean blue gradient hero, white/sand colours, room cards, amenity icons (WiFi, AC, Sea View, Pool, Breakfast).
- **Rooms page:** Filters (type, min/max price, sea view) and “Apply filters” work; list updates.
- **Booking form:** Invalid dates (e.g. check-out before check-in) should be rejected (client or server message).
- **Admin:** Tables (rooms, bookings) display correctly; forms (add/edit room, update status) work.

---

## 10. Final Verification Checklist

Before pushing to GitHub and finalizing the report, confirm:

- [ ] **Application started successfully** — Tomcat runs, no deployment errors in logs.
- [ ] **Database connection works** — Home and Rooms show data from MySQL; no DB errors in catalina.out.
- [ ] **Booking flow works** — Register → Login → Rooms → Room details → Book → Confirmation with reservation number → Booking history shows the booking.
- [ ] **Admin dashboard works** — Login as admin → Dashboard, Rooms (add/edit/delete), Bookings (view + update status), Reports.
- [ ] **UI loads correctly** — Ocean theme, responsive layout, filters and forms behave as above.

---

## Screenshots for Report (keep browser open)

Capture these after the app is running:

**User side:**  
Home page · Rooms page · Room details · Booking confirmation · Booking history · (optional: Login, Register, Help)

**Admin side:**  
Admin dashboard · Room management (list) · Add/Edit room · Booking management · Reports page

Use these in **Task B** (implementation) and **Task C** (testing) sections of your report.

---

## If Something Fails

| Problem | What to check |
|--------|----------------|
| **mvn: command not found** | Install Maven or use IDE Maven support to run `clean package`. |
| **Build failure** | `java -version` (11+), `mvn -v`; read error (compile vs test vs plugin). |
| **DB connection error** | MySQL running; `db.url`, `db.user`, `db.password` in web.xml; user can connect: `mysql -u root -p -e "SELECT 1"`. |
| **404 on /oceanview/** | Tomcat running; `oceanview.war` in `webapps/`; wait for deploy; try .../oceanview/index.jsp. |
| **500 or exception in browser** | Tomcat log (`logs/catalina.out` or `localhost.*.log`); stack trace points to servlet/DAO/DB. |
| **Admin redirect to login** | Log in with admin@oceanview.com / admin123; confirm `users.role = 'ADMIN'` in DB. |
| **No rooms on home** | Run `database/schema.sql` (it inserts sample rooms); confirm `rooms` table has rows. |

---

## Ready for GitHub and Report

When all items in **Section 10** are checked and you have the screenshots:

1. **Git:** Commit any last changes, push to your public repository. Ensure README, TESTING.md, and this guide (optional) are in the repo.
2. **Report:** Use the screenshots and test results (e.g. from TEST-PLAN and TEST-CASES) in the 4000-word report; add repository link for Task D.

Your Ocean View Hotel Booking System is then ready for submission.
