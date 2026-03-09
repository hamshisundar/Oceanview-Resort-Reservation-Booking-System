# Tomcat deployment diagnostic – Ocean View

## 1. Servlet mapping (verified)

**DbStatusServlet** is defined only once:

- **Class:** `com.oceanview.controllers.DbStatusServlet`  
  - `@WebServlet("/db-status")` on the class → single mapping for `/db-status`.

- **web.xml:**  
  - No `<servlet>` or `<servlet-mapping>` for DbStatusServlet (or any other servlet).  
  - Only context-params, listener, welcome-file-list, session-config.

So there is no duplicate mapping; the previous error was fixed by removing the web.xml registration.

---

## 2. WAR build (verified)

- Command: `mvn clean package -DskipTests`
- Output: `target/oceanview.war` (about 6.9 MB).
- WAR contains:
  - `index.jsp`, `WEB-INF/web.xml`, `WEB-INF/classes/`, servlets, JSPs, static resources.
- Packed `WEB-INF/web.xml` has no `<servlet>` / `<servlet-mapping>`.

---

## 3. Clean Tomcat deployment steps

Run from the project root.

**3.1 Stop Tomcat**
```bash
/opt/homebrew/opt/tomcat@9/bin/catalina stop
```
Wait a few seconds.

**3.2 Remove old deployment**
```bash
rm -rf /opt/homebrew/opt/tomcat@9/libexec/webapps/oceanview
rm -f  /opt/homebrew/opt/tomcat@9/libexec/webapps/oceanview.war
```

**3.3 Deploy new WAR**
```bash
cp /Users/hamshi/Documents/OcenView/target/oceanview.war /opt/homebrew/opt/tomcat@9/libexec/webapps/
```

**3.4 Start Tomcat**
```bash
/opt/homebrew/opt/tomcat@9/bin/catalina start
```

**3.5 Wait**
Wait **20 seconds** for Tomcat to unpack the WAR and start the `/oceanview` context.

---

## 4. Verify unpacked app

```bash
ls /opt/homebrew/opt/tomcat@9/libexec/webapps/
```

You should see:

- `oceanview.war`
- `oceanview/` (directory created when the WAR is unpacked)

If `oceanview/` is missing after 20 seconds, the context failed to start (see step 6).

---

## 5. Check logs

```bash
tail -100 /opt/homebrew/opt/tomcat@9/libexec/logs/catalina.out
```

**Success:** Look for lines like:

- `Deploying web application archive [.../oceanview.war]`
- No `SEVERE` or `Error deploying` for oceanview.
- `Server startup in [X] milliseconds`

**Failure:** Look for:

- `Error deploying web application archive` or `SEVERE` for oceanview.
- `Exception` / `Caused by:` – root cause of context startup failure.

**Typical causes if context still fails:**

| Log message / symptom | Meaning |
|----------------------|--------|
| `both mapped to the url-pattern` | Duplicate servlet mapping (web.xml + @WebServlet). Fix: only one definition (we use only @WebServlet). |
| `LifecycleException` / `Failed to start component` | Context failed to start; read the `Caused by:` that follows. |
| `ClassNotFoundException` / `NoClassDefFoundError` | Missing or wrong dependency (e.g. H2, JSTL); check `pom.xml` and WAR `WEB-INF/lib`. |
| `NoSuchMethodError` / `UnsupportedClassVersionError` | Java version mismatch; build and run with same Java (e.g. 11 or 17). |

---

## 6. Verify application URL

After a successful deployment:

- **Home:** http://localhost:8080/oceanview/  
  or http://localhost:8080/oceanview/index.jsp  
  → Should show the Ocean View home page, not 404.

- **DB status:** http://localhost:8080/oceanview/db-status  
  → Should show the database status page.

---

## One-command deploy

From project root:

```bash
./deploy.sh
```

This builds the WAR, stops Tomcat, removes `webapps/oceanview` and `webapps/oceanview.war`, copies the new WAR, starts Tomcat, waits 20 seconds, and prints whether `webapps/oceanview/` exists. Then open http://localhost:8080/oceanview/ and, if you get 404, run the `tail` command above on `catalina.out` and fix any remaining errors shown there.
