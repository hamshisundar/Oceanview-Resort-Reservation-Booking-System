# Deployment and UI Update Guide — Ocean View

## Root cause: why UI changes didn’t appear

1. **Browser cache**  
   CSS and JS were requested with a fixed query string (e.g. `?v=2` from `initParam.resourceVersion`). After redeploy, the URL stayed the same, so the browser kept serving the old file from cache.

2. **Tomcat may not re-extract the WAR**  
   If you overwrite `oceanview.war` while Tomcat is running, or if the exploded directory `oceanview/` already exists, Tomcat might keep using the old extracted content. You must **remove the exploded app directory** (or trigger a clean redeploy) and **restart** so the new WAR is extracted.

3. **Static `resourceVersion`**  
   With `resourceVersion` fixed in `web.xml`, every deploy still used the same `?v=2`, so cache busting didn’t help.

---

## Fixes applied in this project

### 1. Cache busting on every restart

- **`DBContextListener`** now sets  
  `servletContext.setAttribute("resourceVersion", String.valueOf(System.currentTimeMillis()));`  
  so each Tomcat start gets a new value (e.g. `?v=1734567890123`).
- All JSPs use `${resourceVersion}` for CSS/JS links (e.g.  
  `href="${pageContext.request.contextPath}/css/styles.css?v=${resourceVersion}"`).

After a **full restart**, the app serves new URLs for CSS/JS, so the browser loads the updated files.

### 2. Resource paths

- All JSPs already use `${pageContext.request.contextPath}` for CSS, JS, and links (e.g. `/oceanview/css/styles.css`), so paths are correct for a context path `/oceanview`.

### 3. Maven WAR packaging

- **`pom.xml`** uses `warSourceDirectory`: `src/main/webapp`. Everything under that (including `css/`, `js/`, `images/`, `*.jsp`) is included in the WAR by default. No exclusions are applied to those.

---

## Reliable deploy steps (so UI updates appear)

1. **Stop Tomcat**
   ```bash
   # Homebrew Tomcat 9 (macOS)
   /opt/homebrew/opt/tomcat@9/bin/catalina stop
   ```

2. **Remove the exploded app** (so Tomcat re-extracts the new WAR)
   ```bash
   rm -rf /opt/homebrew/opt/tomcat@9/libexec/webapps/oceanview
   rm -f  /opt/homebrew/opt/tomcat@9/libexec/webapps/oceanview.war
   ```

3. **Build**
   ```bash
   cd /Users/hamshi/Documents/OcenView
   mvn clean package -DskipTests
   ```

4. **Copy the new WAR**
   ```bash
   cp target/oceanview.war /opt/homebrew/opt/tomcat@9/libexec/webapps/
   ```

5. **Start Tomcat**
   ```bash
   /opt/homebrew/opt/tomcat@9/bin/catalina start
   ```

6. **Confirm in browser**
   - Open `http://localhost:8080/oceanview/`
   - Do a **hard refresh**: `Cmd+Shift+R` (Mac) or `Ctrl+Shift+R` (Windows/Linux), or DevTools → Network → “Disable cache” then refresh.

---

## Verification: timestamp in page source

After restart, the HTML page source **must** show a timestamp in the CSS/JS URLs:

1. Open `http://localhost:8080/oceanview/`
2. **View Page Source** (right‑click → View Page Source, or `Ctrl+U` / `Cmd+Option+U`)
3. Find the stylesheet link. It should look like:
   ```html
   /oceanview/css/styles.css?v=1736503943434
   ```
   and the script:
   ```html
   /oceanview/js/app.js?v=1736503943434
   ```
4. The number after `?v=` is the startup timestamp. **After each Tomcat restart it must change.** If it stays the same (e.g. `?v=2`), the old cache-busting is still in use; ensure `DBContextListener` sets `resourceVersion` and all JSPs use `${resourceVersion}`.

---

## Verify that the new UI is in the WAR and on disk

**1. Check that CSS and JSP are inside the WAR**
```bash
jar tf target/oceanview.war | grep -E '^css/|\.jsp$'
```
You should see `css/styles.css`, `index.jsp`, `rooms.jsp`, etc.

**2. After deploy, check that Tomcat extracted the new files**
```bash
ls -la /opt/homebrew/opt/tomcat@9/libexec/webapps/oceanview/css/
cat /opt/homebrew/opt/tomcat@9/libexec/webapps/oceanview/css/styles.css | head -5
```
Compare with `src/main/webapp/css/styles.css` to confirm it’s the current version.

**3. Confirm the app and port**
- In `catalina.out` or `localhost.*.log` you should see something like: `Deployment of web application directory ... oceanview has finished`.
- Only one Tomcat (or app server) should be listening on 8080. Check with:  
  `lsof -i :8080` (or `netstat -an | grep 8080`).

---

## Browser cache during development

- Use **hard refresh** after each deploy: `Cmd+Shift+R` / `Ctrl+Shift+R`.
- Or open DevTools (F12) → **Network** → check **“Disable cache”**, then refresh.
- With the new `${resourceVersion}` (timestamp), a **restart of Tomcat** is enough for the next load to use a new `?v=...` and fetch fresh CSS/JS.

---

## Optional development improvement: exploded deployment

Using an **exploded deployment** lets you update UI files (CSS, JS, JSP) **without running `mvn package` every time**. Tomcat runs from an unpacked directory instead of the WAR; you copy or sync only the webapp files and restart (or let the IDE sync), so iterations are faster.

**Option A — Exploded deploy once, then sync only webapp**

1. First time: build and explode:
   ```bash
   mvn clean package -DskipTests
   mkdir -p /opt/homebrew/opt/tomcat@9/libexec/webapps/oceanview
   unzip -o target/oceanview.war -d /opt/homebrew/opt/tomcat@9/libexec/webapps/oceanview
   ```
2. Restart Tomcat so it picks up the new app.
3. For later **UI-only** changes, sync only the webapp content:
   ```bash
   rsync -av --delete src/main/webapp/ /opt/homebrew/opt/tomcat@9/libexec/webapps/oceanview/ \
     --exclude WEB-INF/web.xml
   ```
   (Avoid overwriting `WEB-INF/web.xml` if you changed server-specific config there.)  
   Then **restart Tomcat** (or rely on Tomcat’s reload/autoDeploy if configured) so the new `resourceVersion` is set and JSPs are recompiled.

**Option B — Run Tomcat from IDE**

- Run the app from your IDE (e.g. “Run on Tomcat”) using an **exploded** artifact. Many IDEs auto-publish changes under `src/main/webapp` without a full Maven package. Still do a hard refresh or rely on `${resourceVersion}` after restart.

**Option C — Full WAR for production**

- For production or when changing Java code or `web.xml`, always:
  `mvn clean package` → replace WAR in `webapps/` → remove `webapps/oceanview/` → restart Tomcat.

---

## Summary

| Issue | Fix |
|-------|-----|
| Browser cache | `${resourceVersion}` set at startup to current timestamp; all CSS/JS use `?v=${resourceVersion}`. |
| Old WAR not re-extracted | Remove `webapps/oceanview/` and `webapps/oceanview.war` before copying new WAR, then restart. |
| Wrong server/port | Confirm only one Tomcat on 8080 and that it deployed `oceanview`. |
| Verify new UI | `jar tf target/oceanview.war \| grep css` and inspect `webapps/oceanview/css/` after deploy; hard refresh in browser. |

After these steps, rebuilding, redeploying with a clean explode, and restarting Tomcat will show updated CSS/JSP in the browser.
