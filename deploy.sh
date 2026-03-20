#!/bin/bash
set -e

echo "============================================"
echo "   College ERP — Deployment Script"
echo "============================================"

# ---- Check .env exists ----
if [ ! -f .env ]; then
  echo ""
  echo "ERROR: .env file not found!"
  echo "Run: cp .env.example .env"
  echo "Then fill in your values and re-run this script."
  exit 1
fi

# ---- Load .env ----
set -a
source .env
set +a

echo ""
echo "Domain/IP : $DOMAIN"
echo "SSL       : $ENABLE_SSL"
echo "Database  : $MYSQL_DATABASE"
echo ""

# ---- Validate required vars ----
if [ -z "$DOMAIN" ] || [ "$DOMAIN" = "your-domain-or-ip" ]; then
  echo "ERROR: DOMAIN is not set in .env. Please set it to your domain or VPS IP."
  exit 1
fi

if [ -z "$MYSQL_ROOT_PASSWORD" ] || [ "$MYSQL_ROOT_PASSWORD" = "change_this_root_password" ]; then
  echo "ERROR: MYSQL_ROOT_PASSWORD is not set in .env. Please change it from the default."
  exit 1
fi

if [ -z "$MYSQL_PASSWORD" ] || [ "$MYSQL_PASSWORD" = "change_this_db_password" ]; then
  echo "ERROR: MYSQL_PASSWORD is not set in .env. Please change it from the default."
  exit 1
fi

# ---- Install Docker if missing ----
if ! command -v docker &> /dev/null; then
  echo ">> Docker not found. Installing Docker..."
  apt-get update -y
  apt-get install -y ca-certificates curl gnupg
  install -m 0755 -d /etc/apt/keyrings
  curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg
  chmod a+r /etc/apt/keyrings/docker.gpg
  echo \
    "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] \
    https://download.docker.com/linux/ubuntu \
    $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
    tee /etc/apt/sources.list.d/docker.list > /dev/null
  apt-get update -y
  apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin
  systemctl enable docker
  systemctl start docker
  echo ">> Docker installed successfully."
fi

# ---- Configure nginx ----
if [ "$ENABLE_SSL" = "true" ]; then
  echo ">> SSL enabled — configuring HTTPS..."

  # Check domain is not an IP address
  if [[ "$DOMAIN" =~ ^[0-9]+\.[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
    echo "ERROR: ENABLE_SSL=true but DOMAIN looks like an IP address ($DOMAIN)."
    echo "SSL requires a real domain name. Set ENABLE_SSL=false to use HTTP with an IP."
    exit 1
  fi

  # Install certbot if missing
  if ! command -v certbot &> /dev/null; then
    echo ">> Installing certbot..."
    apt-get install -y certbot
  fi

  # Write SSL nginx config by replacing DOMAIN_PLACEHOLDER
  sed "s/DOMAIN_PLACEHOLDER/$DOMAIN/g" nginx/default-ssl.conf > nginx/default.conf
  echo ">> nginx SSL config written for $DOMAIN"

  # Start only mysql and app first (NOT nginx — port 80 must be free for certbot)
  echo ">> Starting database and app containers..."
  docker compose up -d mysql app

  echo ">> Waiting for app to be ready..."
  sleep 20

  # Get SSL certificate
  if [ ! -d "/etc/letsencrypt/live/$DOMAIN" ]; then
    echo ">> Obtaining SSL certificate for $DOMAIN..."
    certbot certonly --standalone -d "$DOMAIN" -d "www.$DOMAIN" --non-interactive --agree-tos --email "admin@$DOMAIN"
    echo ">> SSL certificate obtained successfully."
  else
    echo ">> SSL certificate already exists, skipping certbot."
  fi

  # Now start nginx with the cert in place
  echo ">> Starting nginx..."
  docker compose up -d nginx

  # Set up auto-renewal cron job
  CRON_JOB="0 3 * * * certbot renew --quiet && docker compose -f $(pwd)/docker-compose.yml restart nginx"
  (crontab -l 2>/dev/null | grep -v "certbot renew"; echo "$CRON_JOB") | crontab -
  echo ">> SSL auto-renewal cron job configured."

else
  echo ">> SSL disabled — using HTTP config (works with IP or domain)."

  # Write plain HTTP nginx config
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

  # Start all containers
  docker compose up -d --build
fi

# ---- Wait and show status ----
echo ""
echo ">> Waiting for all containers to be ready..."
sleep 15

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