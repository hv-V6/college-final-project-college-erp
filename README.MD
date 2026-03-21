# College ERP — Deployment Guide

## Prerequisites
- A VPS running Ubuntu 22.04 or 24.04
- Docker installed ([install guide](https://docs.docker.com/engine/install/ubuntu/))
- Ports 80 and 443 open in your firewall
- If connecting to a domain, point your DNS 'A' record '@' to the IP address, and the same with the a 'www' 'A' record as well, and set the Tlk to 300 or 600 for both

## Quick Deploy

### 1. Clone / upload the project
```bash
git clone https://github.com/hv-V6/college-final-project-college-erp.git
cd /root/college-final-project-college-erp
```

### 2. Configure your environment
```bash
cp .env.example .env
nano .env
```

Fill in:
| Variable | Description | Example |
|---|---|---|
| `MYSQL_ROOT_PASSWORD` | MySQL root password | `MySecurePass123` |
| `MYSQL_PASSWORD` | App DB password | `AppPass456` |
| `DOMAIN` | Your domain or VPS IP | `myerp.com` or `103.45.67.89` |
| `ENABLE_SSL` | `true` for HTTPS (domain only) | `false` |

### 3. Run the deploy script
```bash
chmod +x deploy.sh
./deploy.sh
```

That's it. The script will:
- Configure nginx for your domain/IP
- Obtain a free SSL certificate (if `ENABLE_SSL=true`)
- Build and start all containers
- Set up SSL auto-renewal

### 4. Access the app
- **With domain + SSL:** `https://yourdomain.com`
- **With domain, no SSL:** `http://yourdomain.com`
- **With IP only:** `http://YOUR_VPS_IP`

Default login: `admin` / `admin123`

---

## Using your own MySQL (instead of Docker MySQL)

If you have MySQL already installed on the VPS:

1. Create the database manually:
```sql
CREATE DATABASE college_erp;
CREATE USER 'erp_user'@'%' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON college_erp.* TO 'erp_user'@'%';
FLUSH PRIVILEGES;
```

2. In `docker-compose.yml`, remove the `mysql` service entirely and update the `app` service environment:
```yaml
SPRING_DATASOURCE_URL: jdbc:mysql://host.docker.internal:3306/college_erp
```

---

## Useful Commands

| Task | Command |
|---|---|
| View logs | `docker compose logs app --tail=50` |
| Restart app | `docker compose restart app` |
| Stop everything | `docker compose down` |
| Rebuild after code change | `docker compose up -d --build` |
| Check container status | `docker compose ps` |
