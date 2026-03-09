# Fix: Page not working / 404 on db-status / Registration fails

The **only** reason `http://localhost:8080/oceanview/db-status` shows 404 is that Tomcat is still running an **old** copy of the app (without the db-status servlet). Fix it by deploying the current project once, in the right order.

---

## Option A: One-command deploy (recommended)

In a terminal, from anywhere, run:

```bash
cd /Users/hamshi/Documents/OcenView
chmod +x deploy.sh
./deploy.sh
```

The script will:

1. Build the WAR (`mvn clean package`)
2. Stop Tomcat
3. **Remove** the old `oceanview` folder and `oceanview.war` (so the new app is used)
4. Copy the new `oceanview.war` into Tomcat
5. Start Tomcat and wait 15 seconds

Then open:

- **http://localhost:8080/oceanview/** (home)
- **http://localhost:8080/oceanview/db-status** (database status; use this if registration fails)

If db-status still returns 404, wait another 20 seconds and reload (Tomcat may still be unpacking the WAR).

---

## Option B: Manual steps (if you prefer or script fails)

Run these in order in a terminal. **Do not skip step 3.**

```bash
# 1. Go to project and build
cd /Users/hamshi/Documents/OcenView
mvn clean package -DskipTests

# 2. Stop Tomcat
/opt/homebrew/opt/tomcat@9/bin/catalina stop

# 3. Remove old app (required – if you skip this, db-status will keep giving 404)
rm -rf /opt/homebrew/opt/tomcat@9/libexec/webapps/oceanview
rm -f  /opt/homebrew/opt/tomcat@9/libexec/webapps/oceanview.war

# 4. Deploy new WAR
cp target/oceanview.war /opt/homebrew/opt/tomcat@9/libexec/webapps/

# 5. Start Tomcat
/opt/homebrew/opt/tomcat@9/bin/catalina start
```

Wait 15–20 seconds, then open **http://localhost:8080/oceanview/db-status**.

---

## Check that the new app is deployed

After deploy, you can confirm the servlet is in the WAR:

```bash
jar tf /Users/hamshi/Documents/OcenView/target/oceanview.war | grep DbStatus
```

You should see a line like: `WEB-INF/classes/com/oceanview/controllers/DbStatusServlet.class`

---

## If Tomcat is not from Homebrew

If your Tomcat is installed elsewhere, edit `deploy.sh` and change:

- `WEBAPPS` to your Tomcat `webapps` directory (e.g. `$CATALINA_HOME/webapps`)
- `CATALINA` to your `catalina` script (e.g. `$CATALINA_HOME/bin/catalina`)

Or run the manual steps (Option B) using your own paths for `webapps` and `catalina`.

---

## After db-status loads

- If it shows **“Database connection: OK”** → try registration again; it should work (or show a clear error like “email already registered”).
- If it shows **“Database connection FAILED”** → follow the message on the page (e.g. set `db.password` in `WEB-INF/web.xml`, run `mysql -u root -p < database/schema.sql`). See **SETUP-DATABASE.md** for full steps.
