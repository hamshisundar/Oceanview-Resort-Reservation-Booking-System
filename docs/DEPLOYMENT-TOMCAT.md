# Tomcat Deployment Guide — Ocean View

This guide ensures **UI and code changes appear** after you rebuild and redeploy.

---

## Root cause: why the old UI still appears

Two things usually cause “nothing changed” after redeploy:

1. **Browser cache**  
   The browser keeps using cached `styles.css` and `app.js`. New WAR or not, the browser may not request the updated files.

2. **Tomcat using an old exploded directory**  
   Tomcat unpacks `oceanview.war` into a folder `oceanview/` in `webapps/`. If you only copy a new `oceanview.war` and do **not** remove the existing `oceanview/` folder, Tomcat may keep serving from the old unpacked content instead of the new WAR.

---

## Fix 1: Cache busting (already in the project)

- In **`WEB-INF/web.xml`** the context parameter **`resourceVersion`** is set (e.g. `2`).
- All JSPs load CSS and JS with a query string:  
  `styles.css?v=${initParam.resourceVersion}` and `app.js?v=${initParam.resourceVersion}`.
- When you **increase `resourceVersion`** in `web.xml` and redeploy, the browser requests new URLs and loads fresh CSS/JS.

**When you change CSS or JS:** bump `resourceVersion` in `web.xml` (e.g. from `2` to `3`), rebuild, and redeploy.

---

## Fix 2: Correct deploy steps for Tomcat (Homebrew)

Your Tomcat webapps path: **`/opt/homebrew/opt/tomcat@9/libexec/webapps/`**

Use this order so the **new** WAR is what Tomcat runs:

```bash
# 1. Build the WAR
cd /Users/hamshi/Documents/OcenView
mvn clean package
```

```bash
# 2. Stop Tomcat (choose one)
# If you start Tomcat from Terminal:
# Press Ctrl+C in the terminal where Tomcat is running.

# If Tomcat runs as a service:
brew services stop tomcat@9
```

```bash
# 3. Remove old deployment (important)
rm -rf /opt/homebrew/opt/tomcat@9/libexec/webapps/oceanview
rm -f  /opt/homebrew/opt/tomcat@9/libexec/webapps/oceanview.war
```

```bash
# 4. Deploy the new WAR
cp /Users/hamshi/Documents/OcenView/target/oceanview.war \
   /opt/homebrew/opt/tomcat@9/libexec/webapps/
```

```bash
# 5. Start Tomcat
# If you run from Terminal:
# /opt/homebrew/opt/tomcat@9/bin/catalina run

# If Tomcat runs as a service:
brew services start tomcat@9
```

After Tomcat starts, it will unpack the new `oceanview.war` into a new `oceanview/` directory.

---

## Verify the new app is running

1. **Hard refresh the browser**  
   - Mac: **Cmd+Shift+R**  
   - Windows/Linux: **Ctrl+Shift+R** or **Ctrl+F5**

2. **Check the CSS URL**  
   - Open DevTools (F12) → Network.  
   - Reload the page.  
   - Find `styles.css` — the request URL should look like:  
     `http://localhost:8080/oceanview/css/styles.css?v=2`  
   - If you see `?v=2` (or your current `resourceVersion`), the new config is in effect.

3. **Confirm updated content**  
   - Make a small change in `styles.css`, set `resourceVersion` to `3` in `web.xml`, rebuild, redeploy (steps above), then hard refresh. The visual change should appear.

---

## Verify WAR contents (optional)

To confirm the built WAR contains your latest JSP and CSS:

```bash
unzip -l target/oceanview.war | grep -E "styles.css|index.jsp"
```

You should see paths like `css/styles.css` and `index.jsp` with recent timestamps.

---

## Development workflow (optional)

To avoid rebuilding the full WAR for every CSS/JS change:

- Run Tomcat with the **exploded** directory instead of the WAR (e.g. “exploded” deployment in your IDE, or copy `target/oceanview` to `webapps/oceanview` and point Tomcat at it).
- Configure your IDE so that **static files** under `src/main/webapp` are **copied/synced** to that exploded directory (e.g. “Update resources” or “Copy static resources”).
- Then only CSS/JS/static file changes are copied; you still need a full rebuild/redeploy for Java or JSP changes.

For a simple Maven-based deploy, rebuilding the WAR and following the steps above (including removing the old `oceanview/` folder) is enough.
