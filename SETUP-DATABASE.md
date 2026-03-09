# Fix Registration — Database Setup (Step by Step)

Registration fails when the app cannot connect to MySQL or the database/tables are missing. Follow these steps **in order**.

---

## Step 1: Install MySQL (if not installed)

**macOS (Homebrew):**
```bash
brew install mysql
brew services start mysql
```

**Windows:** Download MySQL Installer from https://dev.mysql.com/downloads/installer/

**Linux (Ubuntu/Debian):**
```bash
sudo apt update && sudo apt install mysql-server
sudo systemctl start mysql
```

---

## Step 2: Create the database and tables

Open a terminal, go to your **project root** (the folder that contains `database/` and `pom.xml`), then run:

```bash
cd /Users/hamshi/Documents/OcenView
mysql -u root -p < database/schema.sql
```

When prompted, enter your **MySQL root password**. (If you never set one, press Enter.)

This creates:
- Database `oceanview_db`
- Table `users` (for registration/login)
- Table `rooms`, `bookings`, and sample data
- Default admin user: `admin@oceanview.com` / `admin123`

**If you get "command not found: mysql":** Add MySQL to your PATH or use the full path (e.g. `/opt/homebrew/opt/mysql/bin/mysql` on Mac with Homebrew).

---

## Step 3: Set the password in the app (if MySQL root has a password)

1. Open **`src/main/webapp/WEB-INF/web.xml`** in your editor.
2. Find the `db.password` context param:
   ```xml
   <context-param>
       <param-name>db.password</param-name>
       <param-value></param-value>
   </context-param>
   ```
3. Put your MySQL root password between the tags:
   ```xml
   <param-value>YOUR_MYSQL_PASSWORD</param-value>
   ```
4. Save the file.

If MySQL root has **no** password, leave `<param-value></param-value>` empty.

---

## Step 4: Rebuild and restart Tomcat

```bash
cd /Users/hamshi/Documents/OcenView
mvn clean package -DskipTests
```

Then:
- Stop Tomcat.
- Remove `webapps/oceanview` and `webapps/oceanview.war`.
- Copy `target/oceanview.war` to Tomcat `webapps/`.
- Start Tomcat.

---

## Step 5: Check that it worked

1. Open in your browser: **http://localhost:8080/oceanview/db-status**
2. You should see: **"Database connection: OK. Table 'users' exists."**
3. If you see an error, the page will show the **exact message** and what to do (e.g. "Access denied" → set password in web.xml; "Unknown database" → run schema.sql).
4. Then go to **Register** and create an account.

---

## Quick reference

| Problem | Fix |
|--------|-----|
| **Access denied for user 'root'@'localhost'** | Set `db.password` in `WEB-INF/web.xml` and restart Tomcat. |
| **Unknown database 'oceanview_db'** | Run `mysql -u root -p < database/schema.sql` from project root. |
| **Communications link failure** | Start MySQL (`brew services start mysql` or start the MySQL service). |
| **Table 'users' doesn't exist** | Run `database/schema.sql` (Step 2). |

After fixing, always open **/oceanview/db-status** first to confirm the database is OK, then try registration again.
