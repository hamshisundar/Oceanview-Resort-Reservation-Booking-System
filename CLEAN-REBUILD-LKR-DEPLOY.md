# Full Clean Rebuild & LKR Deployment — Completed

**Date:** 2026-03-09

## Steps Executed

1. **Tomcat stopped** (manual stop may be needed if script could not connect to shutdown port).
2. **Old deployment removed:** `webapps/oceanview`, `webapps/oceanview.war` deleted.
3. **Tomcat cache cleared:** `work/*` removed.
4. **Project cleaned:** `target/` removed.
5. **WAR rebuilt:** `mvn clean package -DskipTests` — **SUCCESS**.
6. **Currency verified:** All JSPs use **Rs.** and `fmt:formatNumber` (no USD `$` on prices).
7. **New WAR deployed:** `target/oceanview.war` copied to `webapps/`.
8. **Tomcat started** — **SUCCESS**.
9. **Deployment verified:**
   - `webapps/oceanview/` (unpacked) and `webapps/oceanview.war` present.
   - `catalina.out`: "Deployment of web application archive ... oceanview.war has finished in [330] ms".
   - "[OceanView] Using built-in H2 database. No MySQL required." / "Database connection: OK".
   - Deployed `index.jsp` contains: `Rs. <fmt:formatNumber value="${room.pricePerNight}" .../>`.

## Test in Browser

1. Open: **http://localhost:8080/oceanview/** or **http://localhost:8080/oceanview/rooms**
2. Hard refresh: **Cmd+Shift+R** (Mac) or **Ctrl+Shift+R** (Windows).

## Expected Result (LKR)

| Room                 | Price (LKR) |
|----------------------|-------------|
| Standard Double      | Rs. 27,500  |
| Deluxe Ocean         | Rs. 39,000  |
| Ocean View Premium   | Rs. 53,500  |
| Luxury Suite         | Rs. 91,000  |

If you still see USD ($85, $120, etc.):

- Use a **private/incognito** window, or clear cache for `localhost`.
- Confirm you are hitting **http://localhost:8080/oceanview/** (not an old or cached URL).
