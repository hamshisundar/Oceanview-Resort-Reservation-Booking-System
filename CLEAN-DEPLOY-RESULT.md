# Clean deploy result

A full clean rebuild and redeployment was performed.

## Steps executed

1. **Tomcat stopped** – `catalina stop`
2. **Old deployment removed** – `webapps/oceanview`, `webapps/oceanview.war` deleted
3. **Cache cleared** – `work/*`, `temp/*` under Tomcat libexec
4. **Project rebuilt** – `mvn clean package -DskipTests` → `target/oceanview.war` (≈6.9 MB)
5. **WAR contents verified** – `index.jsp`, `WEB-INF/web.xml`, `css/styles.css`, `js/app.js` present
6. **WAR deployed** – copied to `webapps/`
7. **Tomcat started** – `catalina start`, waited 20 seconds

## Verification

- **webapps/** now contains: `ROOT`, `docs`, `examples`, `manager`, `host-manager`, **oceanview**, **oceanview.war**
- **catalina.out:**  
  - `Deploying web application archive [.../oceanview.war]`  
  - `Deployment of web application archive [...] has finished in [336] ms` (no SEVERE)  
  - `[OceanView] Using built-in H2 database. No MySQL required.`  
  - `[OceanView] Database connection: OK`  
  - `Server startup in [448] milliseconds`

## Test the application

- **Home:** http://localhost:8080/oceanview/  
  or http://localhost:8080/oceanview/index.jsp  
- **Register:** http://localhost:8080/oceanview/register.jsp  
- **DB status:** http://localhost:8080/oceanview/db-status  

Registration uses the built-in H2 database; no MySQL setup is required.
