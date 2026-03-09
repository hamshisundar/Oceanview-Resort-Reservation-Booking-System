# Fix: 404 / "Page isn't working" at http://localhost:8080/oceanview/

## Why you see 404

The **404** means the server on port 8080 has **no application at the path `/oceanview`**. Common causes:

1. **WAR not built** — `target/oceanview.war` doesn’t exist.
2. **WAR not deployed** — Tomcat’s `webapps/` doesn’t have `oceanview.war` or `oceanview/`.
3. **Tomcat not running** — Something else is on port 8080 (e.g. another app), and it doesn’t serve `/oceanview`.
4. **Deploy failed** — Tomcat started but failed to deploy the app (check logs).

---

## Step 1: Build the WAR

In a terminal, from the project root:

```bash
cd /Users/hamshi/Documents/OcenView
mvn clean package
```

- If **Maven isn’t installed**: install it, or use your IDE’s Maven support to run **clean** then **package**.
- When it succeeds you must see: **`target/oceanview.war`**.

If you don’t build this WAR, there is nothing to deploy, so `/oceanview` will always 404.

---

## Step 2: Use Apache Tomcat (not a simple static server)

This app is a **Java WAR** and only runs in a **servlet container** (e.g. **Apache Tomcat**).

- **Cursor’s built‑in “Open in Browser”** or a simple static server (e.g. `python -m http.server 8080`) **cannot** run a WAR. They will not serve `/oceanview` and you’ll get 404 or “invalid response”.
- You must install **Apache Tomcat**, deploy the WAR there, and start Tomcat.

**Install Tomcat (if needed):**

- macOS (Homebrew): `brew install tomcat@9` (or download from tomcat.apache.org).
- Set `CATALINA_HOME` to the Tomcat install directory (e.g. `/opt/homebrew/opt/tomcat@9/libexec` or where you extracted Tomcat).

---

## Step 3: Deploy the WAR to Tomcat

1. **Copy the WAR into Tomcat’s webapps folder:**

   ```bash
   cp /Users/hamshi/Documents/OcenView/target/oceanview.war $CATALINA_HOME/webapps/
   ```

   Replace `$CATALINA_HOME` with your Tomcat path if the variable isn’t set.

2. **Start Tomcat:**

   ```bash
   $CATALINA_HOME/bin/startup.sh
   ```

   On Windows: `%CATALINA_HOME%\bin\startup.bat`

3. **Wait 15–30 seconds** so Tomcat can unpack `oceanview.war` and create `webapps/oceanview/`.

4. **Check that the app is there:**

   ```bash
   ls $CATALINA_HOME/webapps/
   ```

   You should see either:
   - `oceanview.war` and/or  
   - a directory **`oceanview`** (with `WEB-INF`, `index.jsp`, etc.).

   If there is no `oceanview` folder and no `oceanview.war`, the 404 will continue.

---

## Step 4: Check Tomcat logs (if still 404)

If the WAR is in `webapps/` but you still get 404, the deployment may have **failed**. Check:

```bash
tail -100 $CATALINA_HOME/logs/catalina.out
# or
tail -100 $CATALINA_HOME/logs/localhost.*.log
```

Look for:

- **“Deployment of web application [oceanview] has finished”** → deploy OK; then the issue is URL or welcome file.
- **“FAILED” / “Exception” / “Error”** → read the message. Typical causes:
  - **Database**: MySQL not running or wrong `db.url` / `db.user` / `db.password` in `web.xml`. Fix config and redeploy.
  - **Missing class**: build again with `mvn clean package` and copy the new WAR.

---

## Step 5: Open the right URL in the browser

- **Correct:** **http://localhost:8080/oceanview/**  
  or **http://localhost:8080/oceanview/index.jsp**
- **Wrong:** http://localhost:8080/ (root only — no app there, can 404).

Use a **normal browser** (Chrome, Safari, Firefox) and type the URL; avoid relying only on Cursor’s built‑in browser if it behaves oddly.

---

## Step 6: Confirm what’s on port 8080

Make sure it’s really Tomcat:

```bash
lsof -i :8080
# or
netstat -an | grep 8080
```

If another process (not Java/Tomcat) is using 8080, either:

- Stop it and use 8080 for Tomcat, or  
- Change Tomcat’s port and use the new port in the URL (e.g. http://localhost:8081/oceanview/).

---

## Checklist

- [ ] `mvn clean package` completed and **target/oceanview.war** exists.
- [ ] **Apache Tomcat** is installed and `CATALINA_HOME` is set.
- [ ] **oceanview.war** is in **`$CATALINA_HOME/webapps/`**.
- [ ] Tomcat was **started** with `startup.sh` / `startup.bat`.
- [ ] After 15–30 seconds, **webapps/oceanview/** exists (or WAR is present and logs say deploy finished).
- [ ] You’re opening **http://localhost:8080/oceanview/** (or .../index.jsp) in a normal browser.
- [ ] Nothing else is bound to port 8080, or you’re using the correct Tomcat port.

If all are true and you still get 404, the next step is to paste the relevant lines from **catalina.out** or **localhost.*.log** (deployment and any errors) so we can see why the app isn’t registered at `/oceanview`.
