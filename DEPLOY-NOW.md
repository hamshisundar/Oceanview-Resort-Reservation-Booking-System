# Deploy so registration works (H2 only — no MySQL)

The app now uses **only** the built-in H2 database. There is no MySQL code path. You **must deploy the new WAR** or you will keep seeing the old error.

## 1. Build
```bash
cd /Users/hamshi/Documents/OcenView
mvn clean package -DskipTests
```
Wait for `BUILD SUCCESS`. New file: `target/oceanview.war`.

## 2. Stop Tomcat
- Homebrew: `brew services stop tomcat@9` or `/opt/homebrew/opt/tomcat@9/bin/catalina stop`
- Or stop from your IDE.

## 3. Remove old app (required)
```bash
rm -rf /opt/homebrew/opt/tomcat@9/libexec/webapps/oceanview
rm -f  /opt/homebrew/opt/tomcat@9/libexec/webapps/oceanview.war
```
*(If Tomcat is elsewhere, change the path.)*

## 4. Deploy new WAR
```bash
cp /Users/hamshi/Documents/OcenView/target/oceanview.war /opt/homebrew/opt/tomcat@9/libexec/webapps/
```

## 5. Start Tomcat
```bash
/opt/homebrew/opt/tomcat@9/bin/catalina start
```
Or: `brew services start tomcat@9` or start from IDE.

## 6. Check you’re on the new app
Wait **15–20 seconds**, then:

1. Open **http://localhost:8080/oceanview/register.jsp**
2. **Scroll to the bottom of the page.** You should see **“Ocean View v2”**.
   - **If you see “Ocean View v2”** → New app is running. Register; it should work.
   - **If you see “Ocean View v?” or the old MySQL error** → You’re still on the old deployment. Repeat steps 2–5 and make sure the `oceanview` folder and `oceanview.war` were deleted before copying the new WAR.

3. **Hard refresh** the page (Cmd+Shift+R) so the browser doesn’t use cache.

---

**One command (Homebrew Tomcat):**
```bash
cd /Users/hamshi/Documents/OcenView && ./deploy.sh
```
Then wait 15 seconds and open the register page. You must see **“Ocean View v2”** at the bottom.
