# Environment Variables Setup

## Overview
Database credentials have been extracted from `application.properties` to environment variables for improved security and environment-specific configuration management.

## Files Modified
- **application.properties**: Uses `${DB_USERNAME:postgres}` and `${DB_PASSWORD:postgrespassword}` with fallback defaults
- **docker-compose.yml**: Loads environment variables from `.env` file
- **.gitignore**: Added `.env` to prevent committing sensitive credentials
- **.env**: Contains actual database credentials (NOT committed to git)
- **.env.example**: Template file for team members (committed to git)

## Local Development Setup

### First Time Setup
1. Copy `.env.example` to `.env`:
   ```bash
   Copy-Item .env.example .env
   ```

2. Edit `.env` with your local credentials:
   ```ini
   DB_USERNAME=postgres
   DB_PASSWORD=your_password_here
   POSTGRES_DB=mydatabase
   ```

3. Start PostgreSQL via Docker Compose:
   ```bash
   docker-compose up -d
   ```

4. Run the application:
   ```bash
   ./gradlew bootRun
   ```

### Running in IntelliJ IDEA
The application will automatically use environment variables from `.env` when running via Gradle. 

**Option 1: Use Gradle bootRun** (Recommended)
- Run Configuration: Gradle → bootRun task
- Environment variables will be loaded from system environment

**Option 2: Use Spring Boot Run Configuration**
- Install [EnvFile Plugin](https://plugins.jetbrains.com/plugin/7861-envfile) for IntelliJ IDEA
- In Run Configuration → EnvFile tab → Add `.env` file
- OR manually set environment variables in Run Configuration → Environment Variables

**Option 3: Set System Environment Variables**
- Windows: Set via System Properties → Environment Variables
- Or set in PowerShell session:
  ```powershell
  $env:DB_USERNAME = "postgres"
  $env:DB_PASSWORD = "postgrespassword"
  ```

## Production Deployment

### Docker Deployment
Environment variables are automatically loaded from `.env` file in the same directory as `docker-compose.yml`.

### Cloud Deployment (AWS/Azure/GCP)
Use cloud-native secret management:
- **AWS**: AWS Secrets Manager or Parameter Store
- **Azure**: Azure Key Vault
- **GCP**: Google Secret Manager

Set environment variables in your deployment configuration:
```bash
export DB_USERNAME=your_production_username
export DB_PASSWORD=your_production_password
export POSTGRES_DB=your_production_database
```

### CI/CD Pipeline
Add environment variables as secrets in your CI/CD platform:
- **GitHub Actions**: Repository Secrets
- **GitLab CI**: CI/CD Variables (masked)
- **Jenkins**: Credentials Plugin

## Environment Variables Reference

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `DB_USERNAME` | PostgreSQL username | `postgres` | Yes |
| `DB_PASSWORD` | PostgreSQL password | `postgrespassword` | Yes |
| `POSTGRES_DB` | PostgreSQL database name | `mydatabase` | Yes |

## Security Best Practices

1. **Never commit `.env` file** - Already added to `.gitignore`
2. **Use strong passwords in production** - Default password is only for local development
3. **Rotate credentials regularly** - Especially in production environments
4. **Use different credentials per environment** - Dev/Staging/Production should have separate credentials
5. **Limit database user permissions** - Grant only necessary privileges

## Troubleshooting

### Issue: Application can't connect to database
**Solution**: Verify environment variables are set correctly
```bash
# Check environment variables in PowerShell
Get-ChildItem Env: | Where-Object { $_.Name -like "DB_*" }

# Verify docker-compose loads variables correctly
docker-compose config
```

### Issue: IntelliJ IDEA doesn't load .env file
**Solutions**:
- Install EnvFile plugin and configure it in Run Configuration
- Use `./gradlew bootRun` instead of Spring Boot run configuration
- Manually set environment variables in Run Configuration

### Issue: Docker Compose fails to start
**Solution**: Ensure `.env` file exists and contains all required variables
```bash
# Verify .env file exists
Test-Path .env

# Check content
Get-Content .env
```

## Migration Notes
- Old hardcoded values: `username=postgres`, `password=postgrespassword`
- New format: Uses Spring's `${VARIABLE:default}` syntax
- Fallback defaults ensure backward compatibility if environment variables are not set

