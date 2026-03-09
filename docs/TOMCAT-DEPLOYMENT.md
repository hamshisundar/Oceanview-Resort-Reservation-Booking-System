# Tomcat deployment – diagnosis and fix

## Why you saw HTTP 404 at http://localhost:8080/oceanview/

The WAR was present in `webapps/` and Tomcat had unpacked it to `webapps/oceanview/`, but the **oceanview context failed to start**. When a context fails to start, Tomcat does not serve that app, so every request to `/oceanview/` returns **404 – Not Found**.

### Root cause (from catalina.out)

```text
Caused by: java.lang.IllegalArgumentException: The servlets named [DbStatusServlet] and [com.oceanview.controllers.DbStatusServlet] are both mapped to the url-pattern [/db-status] which is not permitted
```

**DbStatusServlet** was registered twice for the same URL `/db-status`:

1. In **web.xml**: `<servlet>` + `<servlet-mapping>` for `/db-status`
2. On the class: **@WebServlet("/db-status")**

Tomcat does not allow two servlets to use the same url-pattern, so the context failed to start and the app was never available.

### Fix applied

The duplicate registration in **web.xml** was removed. Only the **@WebServlet("/db-status")** on `DbStatusServlet` is used. The WAR was rebuilt with this change.

---

## Deployment workflow (after the fix)

1. **Stop Tomcat**
   ```bash
   /opt/homebrew/opt/tomcat@9/bin/catalina stop
   ```

2. **Remove old deployment**
   ```bash
   rm -rf /opt/homebrew/opt/tomcat@9/libexec/webapps/oceanview
   rm -f  /opt/homebrew/opt/tomcat@9/libexec/webapps/oceanview.war
   ```
   *(Note: Homebrew may use `/opt/homebrew/Cellar/tomcat@9/9.0.115/libexec/webapps/`; both symlink to the same place.)*

3. **Build**
   ```bash
   cd /Users/hamshi/Documents/OcenView
   mvn clean package -DskipTests
   ```

4. **Deploy**
   ```bash
   cp target/oceanview.war /opt/homebrew/opt/tomcat@9/libexec/webapps/
   ```

5. **Start Tomcat**
   ```bash
   /opt/homebrew/opt/tomcat@9/bin/catalina start
   ```
   Or: `libexec/bin/startup.sh` (same effect when CATALINA_BASE is set.)

6. **Wait 15–20 seconds** for Tomcat to unpack the WAR and start the context.

7. **Verify**
   - Open: http://localhost:8080/oceanview/  
   - You should see the Ocean View home page, not 404.

---

## Verification commands

- **WAR exists and size**
  ```bash
  ls -la /Users/hamshi/Documents/OcenView/target/oceanview.war
  ```

- **WAR in webapps**
  ```bash
  ls -la /opt/homebrew/opt/tomcat@9/libexec/webapps/oceanview.war
  ```

- **Unpacked context**
  ```bash
  ls -la /opt/homebrew/opt/tomcat@9/libexec/webapps/oceanview/
  ```
  If this folder exists after startup, the WAR was unpacked.

- **Context started (no failure)**
  ```bash
  tail -100 /opt/homebrew/opt/tomcat@9/libexec/logs/catalina.out
  ```
  Look for:
  - `Deploying web application archive [.../oceanview.war]` then **no** `SEVERE` / `Error deploying` for oceanview.
  - `Server startup in [X] milliseconds` without a preceding oceanview deployment error.

---

## If deployment still fails

1. **Check logs**
   ```bash
   tail -200 /opt/homebrew/opt/tomcat@9/libexec/logs/catalina.out
   ```
   Look for `SEVERE`, `Exception`, or `Error deploying` related to `oceanview`.

2. **Confirm Tomcat and port**
   - Only one Tomcat should listen on 8080. If you start Tomcat from an IDE as well, that instance may be the one answering; deploy the WAR to that server’s webapps (or stop the IDE server and use only the Homebrew one).

3. **Clean deploy**
   - Stop Tomcat, delete `webapps/oceanview` and `webapps/oceanview.war`, copy the new WAR, start Tomcat, wait 15–20 seconds, then try http://localhost:8080/oceanview/ again.
