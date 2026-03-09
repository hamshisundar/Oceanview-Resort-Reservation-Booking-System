# Full clean deploy – completed

## Steps executed

| Step | Action | Result |
|------|--------|--------|
| 1 | Stop Tomcat | `catalina stop` |
| 2 | Remove old deployment | Deleted `webapps/oceanview`, `webapps/oceanview.war` |
| 3 | Clear Tomcat caches | Cleared `work/*` (temp was already empty) |
| 4 | Clean project | Removed `target/` |
| 5 | Verify images in source | `standard.png`, `deluxe.png`, `oceanview.png`, `suite.png` in `src/main/webapp/images/rooms/` |
| 6 | Build WAR | `mvn clean package -DskipTests` — WAR includes all four room images |
| 7 | Deploy WAR | Copied `target/oceanview.war` to `webapps/` |
| 8 | Start Tomcat | `catalina start`, waited 20 seconds |
| 9 | Verify | `webapps/oceanview/` and `webapps/oceanview.war` present; unpacked app contains `images/rooms/*.png` |

## Logs

- **Deploy:** `Deployment of web application archive [.../oceanview.war] has finished in [342] ms`
- **Startup:** `Server startup in [464] milliseconds`
- No SEVERE or "Error deploying" for oceanview.

## Step 10 – test in browser

1. Open: **http://localhost:8080/oceanview/rooms**
2. Hard refresh: **Cmd+Shift+R** (Mac) or **Ctrl+Shift+R** (Windows)

Room cards should show:

- **Standard Double** → standard.png  
- **Deluxe Ocean** → deluxe.png  
- **Ocean View Premium** → oceanview.png  
- **Luxury Suite** → suite.png  

If placeholders still appear, try a private/incognito window or clear the site’s cache for localhost.

## If the problem persists

Check logs:

```bash
tail -100 /opt/homebrew/opt/tomcat@9/libexec/logs/catalina.out
```

Look for `SEVERE`, `Error deploying`, or `Exception starting context` related to oceanview.
