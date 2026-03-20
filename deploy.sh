#!/bin/bash
set -e

echo "============================================"
echo "   College ERP — Deployment Script"
echo "============================================"

# Check .env exists
if [ ! -f .env ]; then
  echo ""
  echo "ERROR: .env file not found!"
  echo "Run: cp .env.example .env"
  echo "Then fill in your values and re-run this script."
  exit 1
fi

# Load .env
export $(grep -v '^#' .env | xargs)

echo ""
echo "Domain/IP : $DOMAIN"
echo "SSL       : $ENABLE_SSL"
echo "Database  : $MYSQL_DATABASE"
echo ""

# ---- Configure nginx ----
if [ "$ENABLE_SSL" = "true" ]; then
  echo ">> SSL enabled — configuring HTTPS nginx..."

  # Check certbot is installed
  if ! command -v certbot &> /dev/null; then
    echo ">> Installing certbot..."
    sudo apt install -y certbot
  fi

  # Get certificate if it doesn't exist yet
  if [ ! -d "/etc/letsencrypt/live/$DOMAIN" ]; then
    echo ">> Obtaining SSL certificate for $DOMAIN..."
    sudo certbot certonly --standalone -d "$DOMAIN" -d "www.$DOMAIN"
  else
    echo ">> SSL certificate already exists, skipping certbot."
  fi

  # Write SSL nginx config
  sed "s/DOMAIN_PLACEHOLDER/$DOMAIN/g" nginx/default-ssl.conf > nginx/default.conf
  echo ">> nginx configured for HTTPS."

else
  echo ">> SSL disabled — using HTTP nginx config (works with IP or domain)."
  cat > nginx/default.conf << 'EOF'
server {
    listen 80;
    server_name _;

    location / {
        proxy_pass         http://app:8080;
        proxy_set_header   Host $host;
        proxy_set_header   X-Real-IP $remote_addr;
        proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header   X-Forwarded-Proto $scheme;
    }
}
EOF
fi

# ---- Start Docker Compose ----
echo ""
echo ">> Building and starting containers (this may take a few minutes)..."
docker compose up -d --build

echo ""
echo ">> Waiting for app to start..."
sleep 10

docker compose ps

echo ""
echo "============================================"
if [ "$ENABLE_SSL" = "true" ]; then
  echo "  App is live at: https://$DOMAIN"
else
  echo "  App is live at: http://$DOMAIN"
fi
echo "  Login: admin / admin123"
echo "============================================"

# ---- Set up SSL auto-renewal if needed ----
if [ "$ENABLE_SSL" = "true" ]; then
  echo ""
  echo ">> Setting up SSL auto-renewal cron job..."
  CRON_JOB="0 3 * * * certbot renew --quiet && docker compose -f $(pwd)/docker-compose.yml restart nginx"
  (sudo crontab -l 2>/dev/null | grep -v "certbot renew"; echo "$CRON_JOB") | sudo crontab -
  echo ">> Auto-renewal configured."
fi

echo ""
echo "Done!"