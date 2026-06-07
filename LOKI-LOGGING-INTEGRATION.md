# Loki Logging Integration - Implementation Summary

**Date**: 2026-06-01  
**Status**: ✅ **COMPLETE - Ready for Deployment**

## 🎯 Overview

Successfully integrated Orders Desk application with the Loki logging stack (Loki + Promtail + Grafana). The application now outputs structured JSON logs that are automatically collected by Promtail, stored in Loki, and visualizable in Grafana.

---

## ✅ Changes Implemented

### 1. **Structured Logging Configuration** (`application.properties`)

**Added:**
```properties
# Logging Configuration for Loki Integration
logging.level.root=INFO
logging.level.com.ta.orders=DEBUG
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
spring.output.ansi.enabled=ALWAYS
```

**Benefits:**
- Consistent log format with timestamps
- Thread information for debugging
- Color-coded logs for local development
- Application-level debug logging

---

### 2. **JSON Structured Logging** (`build.gradle`)

**Added dependency:**
```gradle
implementation 'net.logstash.logback:logstash-logback-encoder:7.4'
```

**Benefits:**
- Logs in JSON format for better parsing in Loki
- Structured fields (timestamp, level, logger, message, stacktrace)
- Custom fields for application metadata

---

### 3. **Logback Configuration** (`logback-spring.xml`)

**Created new file:**
- **Production profile**: JSON-formatted logs for Loki
- **Local/dev profile**: Colored pattern logs for console
- **Custom fields**: `application=orders-desk`, `environment=production`
- **Profile-aware**: Switches format based on Spring profile

**Benefits:**
- Optimized for Loki in Kubernetes (JSON)
- Readable for developers locally (colored pattern)
- Automatic profile detection

---

### 4. **Kubernetes Deployment Labels** (`k8s/app-deployment.yaml`)

**Added labels:**
```yaml
labels:
  app: orders-app
  version: v1
  component: backend
```

**Benefits:**
- Better log filtering in Grafana: `{app="orders-app"}`
- Version tracking: `{version="v1"}`
- Component classification: `{component="backend"}`
- Multi-dimensional log queries

---

## 📊 How It Works

### Architecture Flow

```
Orders Desk Application
    ↓ (logs to stdout/stderr in JSON format)
Kubernetes Pod
    ↓ (pod logs stored in /var/log/pods/)
Promtail DaemonSet
    ↓ (scrapes logs from all pods)
Loki Service (monitoring namespace)
    ↓ (stores time-series logs)
Grafana Dashboards
    ↓ (visualize & query logs)
You! 🎉
```

### Automatic Log Collection

1. **Application logs to stdout** (Spring Boot default)
2. **Promtail discovers pods** (DaemonSet runs on every node)
3. **Promtail reads pod logs** (from `/var/log/pods/`)
4. **Promtail sends to Loki** (`http://loki.monitoring.svc.cluster.local:3100`)
5. **Loki stores logs** (with labels: app, namespace, pod, container)
6. **Grafana queries Loki** (via Loki datasource)

---

## 🚀 Deployment

### After Merging to Main Branch:

**GitHub Actions will automatically:**
1. ✅ Build application with new dependencies
2. ✅ Create Docker image with logback-spring.xml
3. ✅ Push image to GHCR
4. ✅ Deploy to K8s with updated labels
5. ✅ Pods restart with JSON logging enabled
6. ✅ Promtail starts collecting logs immediately

**No manual steps required!**

---

## 📝 Querying Logs in Grafana

### Access Grafana

**URL**: `http://195.206.233.23:30300/`  
**Login**: `admin` / (your Grafana password)

### Common Log Queries (LogQL)

#### View All Application Logs
```logql
{app="orders-app"}
```

#### Filter by Namespace
```logql
{namespace="orders-desk"}
```

#### View Only Errors
```logql
{app="orders-app"} |= "ERROR"
```

#### View Errors or Warnings
```logql
{app="orders-app"} |~ "ERROR|WARN"
```

#### Search for Specific Text
```logql
{app="orders-app"} |= "Exception"
```

#### Count Errors per Minute
```logql
rate({app="orders-app"} |= "ERROR" [1m])
```

#### Filter by Pod
```logql
{app="orders-app", pod="orders-app-5d98f449f5-bnwww"}
```

#### Parse JSON Logs
```logql
{app="orders-app"} 
| json 
| level="ERROR"
| line_format "{{.timestamp}} - {{.logger}}: {{.message}}"
```

#### Top 10 Errors in Last Hour
```logql
topk(10, sum by (message) (count_over_time({app="orders-app"} |= "ERROR" [1h])))
```

---

## 🧪 Testing Locally

### Before Deployment - Test JSON Logging:

```bash
# 1. Build application
./gradlew clean build

# 2. Run with production profile
./gradlew bootRun --args='--spring.profiles.active=production'

# 3. In another terminal, trigger some logs
curl http://localhost:8080/actuator/health
curl http://localhost:8080/api/v1/product-categories

# 4. Check logs are in JSON format
# Output should look like:
# {"@timestamp":"2026-06-01T10:30:45.123+00:00","level":"INFO","logger":"com.ta.orders...","message":"...","application":"orders-desk","environment":"production"}
```

### Test with Dev Profile (colored logs):

```bash
# Run with default profile
./gradlew bootRun

# Logs will be colored and human-readable
# Output like:
# 2026-06-01 10:30:45.123 [main] INFO  com.ta.orders.OrdersApplication - Starting OrdersApplication
```

---

## 🔍 Verification After Deployment

### 1. Check Pods are Running
```bash
ssh root@195.206.233.23
sudo k3s kubectl get pods -n orders-desk -l app=orders-app
```

### 2. Verify Logs in Kubernetes
```bash
# View pod logs directly
sudo k3s kubectl logs -n orders-desk -l app=orders-app --tail=20

# Should see JSON-formatted logs like:
# {"@timestamp":"...","level":"INFO","logger":"...","message":"..."}
```

### 3. Check Promtail is Collecting Logs
```bash
# Check Promtail status
sudo k3s kubectl get pods -n monitoring -l app.kubernetes.io/name=promtail

# View Promtail logs
sudo k3s kubectl logs -n monitoring -l app.kubernetes.io/name=promtail --tail=50 | grep orders-app
```

### 4. Query in Grafana

1. Open Grafana: `http://195.206.233.23:30300/`
2. Click **Explore** (compass icon)
3. Select **Loki** datasource
4. Enter query: `{app="orders-app"}`
5. Click **Run query**

**Expected result**: Logs appear with timestamps, levels, and messages

---

## 📈 Log Metrics Dashboard (Optional Next Step)

### Create Log-Based Metrics Dashboard:

**Panel Ideas:**

1. **Log Volume by Level**
   ```logql
   sum by (level) (rate({app="orders-app"} [5m]))
   ```

2. **Error Rate**
   ```logql
   sum(rate({app="orders-app"} |= "ERROR" [5m]))
   ```

3. **Recent Errors Table**
   ```logql
   {app="orders-app"} |= "ERROR"
   ```

4. **HTTP Request Logs**
   ```logql
   {app="orders-app"} |~ "GET|POST|PUT|DELETE"
   ```

5. **Database Query Logs**
   ```logql
   {app="orders-app"} |~ "Hibernate|SQL"
   ```

---

## 🎁 Log Examples

### Sample JSON Log Output (Production):

```json
{
  "@timestamp": "2026-06-01T10:30:45.123+00:00",
  "level": "INFO",
  "thread": "http-nio-8080-exec-1",
  "logger": "com.ta.orders.service.impl.ProductCategoryServiceImpl",
  "message": "Retrieved all product categories",
  "application": "orders-desk",
  "environment": "production"
}
```

### Sample Colored Log Output (Local Dev):

```
2026-06-01 10:30:45.123 [http-nio-8080-exec-1] INFO  com.ta.orders.service.impl.ProductCategoryServiceImpl - Retrieved all product categories
```

---

## 🛠️ Troubleshooting

### Issue: No logs appearing in Grafana

**Solution:**
```bash
# 1. Check pod is running and logging
sudo k3s kubectl logs -n orders-desk -l app=orders-app --tail=50

# 2. Verify Promtail is running
sudo k3s kubectl get pods -n monitoring -l app.kubernetes.io/name=promtail

# 3. Check Loki service is up
sudo k3s kubectl get svc -n monitoring loki

# 4. Check Promtail logs for errors
sudo k3s kubectl logs -n monitoring -l app.kubernetes.io/name=promtail | grep -i error
```

### Issue: Logs are not in JSON format

**Solution:**
```bash
# Check Spring profile is set correctly
sudo k3s kubectl get pods -n orders-desk -l app=orders-app -o yaml | grep SPRING_PROFILES_ACTIVE

# If not set, add to deployment or configmap:
SPRING_PROFILES_ACTIVE=production
```

### Issue: Cannot query logs in Grafana

**Solution:**
1. Verify Loki datasource is configured: **Settings → Data sources → Loki**
2. URL should be: `http://loki.monitoring.svc.cluster.local:3100`
3. Click **Save & test** to verify connection

---

## 📋 Files Modified

| File | Change | Purpose |
|------|--------|---------|
| `build.gradle` | Added `logstash-logback-encoder:7.4` | JSON logging dependency |
| `application.properties` | Added logging configuration | Structured log format |
| `logback-spring.xml` | Created new file | Profile-aware logging (JSON/Pattern) |
| `k8s/app-deployment.yaml` | Added `version` and `component` labels | Better log filtering |

---

## 🎯 What You Get

### Automatic Log Collection
✅ All stdout/stderr logs collected  
✅ JSON-formatted for easy parsing  
✅ Structured with metadata (logger, level, thread)  
✅ Custom application and environment tags  

### Grafana Integration
✅ Query logs by app, namespace, pod, level  
✅ Search by text or regex patterns  
✅ Create log-based metrics and alerts  
✅ Visualize error rates and log volumes  

### Developer Experience
✅ Colored logs for local development  
✅ JSON logs in Kubernetes/production  
✅ Profile-aware configuration  
✅ No code changes required in services  

---

## 📚 Quick Reference

| What | Where | How |
|------|-------|-----|
| **View logs** | Grafana Explore | Query: `{app="orders-app"}` |
| **Grafana URL** | `http://195.206.233.23:30300/` | Login: `admin` |
| **Loki service** | `loki.monitoring.svc.cluster.local:3100` | Internal K8s DNS |
| **Pod logs** | `kubectl logs -n orders-desk -l app=orders-app` | Direct K8s logs |
| **Promtail status** | `kubectl get pods -n monitoring -l app.kubernetes.io/name=promtail` | Check log collector |
| **Log format** | JSON in production, Pattern in dev | Auto-switched by profile |

---

## 🚀 Next Steps

1. ✅ **Merge changes to main branch**
2. ✅ **GitHub Actions deploys automatically**
3. ✅ **Wait 5-7 minutes for deployment**
4. ✅ **Open Grafana and query logs**
5. ⏭️ Create custom log dashboards
6. ⏭️ Set up log-based alerts (high error rate)
7. ⏭️ Add MDC context to logs (request IDs, user IDs)

---

## 📖 Additional Resources

- **Logging Guide**: `docs/java-app-logging-guide.md`
- **LogQL Documentation**: https://grafana.com/docs/loki/latest/logql/
- **Grafana Explore**: http://195.206.233.23:30300/explore
- **Prometheus Integration**: `PROMETHEUS-INTEGRATION-SUMMARY.md`

---

## ✅ Summary

**Status**: Ready to deploy!

**What happens after merge:**
1. Application builds with JSON logging support
2. Deploys to K8s with structured logs
3. Promtail automatically collects logs
4. Loki stores logs with proper labels
5. Grafana allows querying and visualization

**Total integration time**: ~5-7 minutes after merge

**No manual configuration needed** - everything is automated! 🎉

---

**Logging integration complete!** 🚀  
Merge to main to activate automatic log collection.

