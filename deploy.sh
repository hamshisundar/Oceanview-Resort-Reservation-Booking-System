#!/bin/bash
# Ocean View - Full rebuild and deploy to Tomcat 9 (Homebrew)
# Run from project root: ./deploy.sh

set -e
PROJECT_ROOT="/Users/hamshi/Documents/OcenView"
WEBAPPS="/opt/homebrew/opt/tomcat@9/libexec/webapps"
CATALINA="/opt/homebrew/opt/tomcat@9/bin/catalina"
WAR_NAME="oceanview"

echo "=== 1. Building WAR ==="
cd "$PROJECT_ROOT"
mvn clean package -DskipTests -q
echo "Build OK."

echo ""
echo "=== 2. Stopping Tomcat ==="
"$CATALINA" stop 2>/dev/null || true
sleep 2

echo ""
echo "=== 3. Removing old deployment (required for new pages to appear) ==="
rm -rf "$WEBAPPS/$WAR_NAME"
rm -f  "$WEBAPPS/$WAR_NAME.war"
echo "Old app removed."

echo ""
echo "=== 4. Deploying new WAR ==="
cp "$PROJECT_ROOT/target/$WAR_NAME.war" "$WEBAPPS/"
echo "WAR copied."

echo ""
echo "=== 5. Starting Tomcat ==="
"$CATALINA" start
echo ""
echo "Waiting 20 seconds for Tomcat to unpack WAR and start context..."
sleep 20

echo ""
echo "=== Verify deployment ==="
if [ -d "$WEBAPPS/$WAR_NAME" ]; then
  echo "  OK: $WEBAPPS/$WAR_NAME exists (WAR was unpacked)."
else
  echo "  WARN: $WEBAPPS/$WAR_NAME missing. Check: tail -80 $WEBAPPS/../logs/catalina.out"
fi

echo ""
echo "=== Done ==="
echo "Open: http://localhost:8080/oceanview/"
echo "If 404, check logs: tail -100 /opt/homebrew/opt/tomcat@9/libexec/logs/catalina.out"
