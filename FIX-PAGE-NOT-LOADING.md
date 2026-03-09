# Fix: "This page isn't working - localhost sent an invalid response"

Follow these steps **exactly**. Do not skip step 3.

---

## 1. Rebuild the project

In a terminal:

```bash
cd /Users/hamshi/Documents/OcenView
mvn clean package -DskipTests
```

Wait until you see `BUILD SUCCESS`.

---

## 2. Stop Tomcat

```bash
/opt/homebrew/opt/tomcat@9/bin/catalina stop
```

Wait a few seconds.

---

## 3. Remove the old app (required)

If you skip this, Tomcat keeps using old files and the new db-status page will not appear.

```bash
rm -rf /opt/homebrew/opt/tomcat@9/libexec/webapps/oceanview
rm -f  /opt/homebrew/opt/tomcat@9/libexec/webapps/oceanview.war
```

---

## 4. Copy the new WAR

```bash
cp /Users/hamshi/Documents/OcenView/target/oceanview.war /opt/homebrew/opt/tomcat@9/libexec/webapps/
```

---

## 5. Start Tomcat

```bash
/opt/homebrew/opt/tomcat@9/bin/catalina start
```

---

## 6. Wait and open the correct URL

- Wait **10–15 seconds** for Tomcat to start and unpack the WAR.
- Open in your browser (use this **exact** URL):

  **http://localhost:8080/oceanview/db-status**

- Not `http://localhost:8080/db-status` (missing `oceanview`).
- Not `http://localhost:8080/oceanview/` (that’s the home page).

---

## If it still fails

1. **Home page first**  
   Open **http://localhost:8080/oceanview/**  
   - If the home page loads but db-status does not: repeat from step 1 and make sure you did step 3 (delete `oceanview` folder and WAR).  
   - If the home page does not load: Tomcat may not be running or may be on a different port. Check with:  
     `lsof -i :8080`

2. **Check the WAR**  
   Run:  
   `jar tf /Users/hamshi/Documents/OcenView/target/oceanview.war | grep DbStatus`  
   You should see something like:  
   `WEB-INF/classes/com/oceanview/controllers/DbStatusServlet.class`

3. **Different Tomcat install**  
   If Tomcat is not from Homebrew, replace  
   `/opt/homebrew/opt/tomcat@9/libexec/webapps/`  
   with your actual Tomcat `webapps` directory in steps 3 and 4.
