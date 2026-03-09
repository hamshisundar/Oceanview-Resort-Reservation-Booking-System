# See Your Changes — Rebuild & Redeploy

If the app **still looks the same** after code updates, the server is usually still running the **old** version. Do this so the new code is used:

---

## 1. Rebuild the WAR (already done if you ran below)

```bash
cd /Users/hamshi/Documents/OcenView
mvn clean package
```

The new WAR is: **`target/oceanview.war`** (or `target/oceanview-booking-*.war` — check `target/`).

---

## 2. Deploy the new WAR to Tomcat

**Option A – Replace WAR and restart**

1. **Stop Tomcat** (e.g. run `shutdown.bat` or stop from Services/IDE).
2. **Delete** the old app folder from Tomcat:
   - Remove `webapps/oceanview` (folder) if it exists.
   - Remove `webapps/oceanview.war` if you want a clean deploy.
3. **Copy** the new WAR from `target/oceanview.war` (or the name in `target/`) into Tomcat’s **`webapps/`** folder.
4. **Start Tomcat** again. It will unpack the new WAR and run the updated app.

**Option B – If you use an IDE (e.g. IntelliJ, Eclipse)**

- Use **Run → Restart** or **Redeploy** for the Tomcat run configuration so it picks up the new build from `target/`.

---

## 3. Hard refresh the browser

- **Windows/Linux:** `Ctrl + Shift + R` or `Ctrl + F5`
- **Mac:** `Cmd + Shift + R`

Or clear the cache for `localhost` so you don’t see old CSS/JS/JSP.

---

## 4. How to confirm the new code is running

- **Registration page:** If you leave name/email/password blank and submit, you should see: **“Please fill in Full name, Email, and Password.”** (validation message). The old page did not show this.
- **Registration error (when DB fails):** The red message should mention **“db.password”** and **“WEB-INF/web.xml”** — that’s the new text.
- **Help:** Click **Help** in the menu. The URL should be **`.../help.jsp`** and the first line should say **“The system provides the following functionalities in this order:”**.
- **After booking:** On the confirmation page you should see a **“Print bill”** button next to “View my bookings”.

If you see these, the new build is in use.

---

## 5. If registration still fails

1. Set MySQL root password in **`src/main/webapp/WEB-INF/web.xml`** → context param **`db.password`**.
2. Create the database:  
   `mysql -u root -p < database/schema.sql`
3. Rebuild, redeploy, and try registering again.
