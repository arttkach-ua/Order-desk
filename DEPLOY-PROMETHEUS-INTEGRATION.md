# Quick Deployment Guide - Prometheus Integration

## ✅ Changes Completed

All code changes are complete and the application has been built successfully. Here's what to do next:

---

## 🚀 Step-by-Step Deployment

### 1. Connect to K3s Server

```bash
ssh root@195.206.233.23
```

### 2. Navigate to Project Directory (if manifests are on server)

```bash
# If you need to upload the updated manifests first:
# Use scp or git pull to get the latest k8s/*.yaml files
```

### 3. Apply Updated Kubernetes Manifests

```bash
# Apply the updated ConfigMap (with Prometheus settings)
sudo k3s kubectl apply -f k8s/app-configmap.yaml

# Apply the updated Deployment (with Prometheus annotations)
sudo k3s kubectl apply -f k8s/app-deployment.yaml
```

### 4. Verify Deployment

```bash
# Watch pods restart with new configuration
sudo k3s kubectl get pods -n orders-desk -w

# Wait until both pods are Running and Ready (2/2)
# Press Ctrl+C to stop watching

# Verify pod annotations are correct
sudo k3s kubectl get pod -n orders-desk -l app=orders-app -o jsonpath='{.items[0].metadata.annotations}' | jq

# Should show:
# {
#   "prometheus.io/path": "/actuator/prometheus",
#   "prometheus.io/port": "8080",
#   "prometheus.io/scrape": "true"
# }
```

---

## 🔍 Immediate Verification

### Test Metrics Endpoint

```bash
# Get a running pod name
POD_NAME=$(sudo k3s kubectl get pod -n orders-desk -l app=orders-app -o jsonpath='{.items[0].metadata.name}')

# Test the Prometheus metrics endpoint
sudo k3s kubectl exec -n orders-desk $POD_NAME -- curl -s http://localhost:8080/actuator/prometheus | head -30

# You should see output like:
# # HELP jvm_memory_used_bytes The amount of used memory
# # TYPE jvm_memory_used_bytes gauge
# jvm_memory_used_bytes{area="heap",id="G1 Survivor Space",} 1234567.0
# ...
```

### Check Prometheus Targets

```bash
# Port-forward Prometheus service
sudo k3s kubectl port-forward -n monitoring svc/prometheus 9090:9090 &

# In another terminal or browser, open:
# http://localhost:9090/targets

# Look for "orders-app" job - should show 2 endpoints with status UP ✅
```

---

## 📊 Access Grafana Dashboard

### Open Grafana

```
URL: http://195.206.233.23:30300
Username: admin
Password: admin (change on first login!)
```

### View Pre-configured Dashboard

1. Navigate to **Dashboards** → **Browse**
2. Open **"Orders Desk - Spring Boot Metrics"**
3. You should see:
   - HTTP request rate per pod
   - CPU usage
   - JVM heap memory
   - Number of running pods

### Explore Metrics (Alternative)

1. Click **Explore** (compass icon)
2. Select **Prometheus** datasource
3. Try these queries:

```promql
# Application uptime
up{namespace="orders-desk", app="orders-app"}

# Memory usage
jvm_memory_used_bytes{namespace="orders-desk", area="heap"}

# HTTP request rate (after generating some traffic)
rate(http_server_requests_seconds_count{namespace="orders-desk"}[5m])
```

---

## 🧪 Generate Test Traffic

To see metrics populate in real-time:

```bash
# Port-forward the application service
sudo k3s kubectl port-forward -n orders-desk svc/orders-service 8080:8080 &

# Generate 100 requests
for i in {1..100}; do
  curl -s http://localhost:8080/api/v1/product-categories >/dev/null
  echo "Request $i"
  sleep 0.1
done

# Now check Grafana dashboard - you should see:
# - Request rate spike to ~10 req/sec
# - Response time metrics
# - Cache metrics (if endpoints use caching)
```

---

## ✅ Success Criteria

**You know it's working when:**

1. ✅ Both `orders-app` pods are Running in `orders-desk` namespace
2. ✅ Prometheus UI shows `orders-app` targets as **UP** (green)
3. ✅ Grafana dashboard displays JVM metrics, HTTP rates, CPU usage
4. ✅ Metrics endpoint responds: `curl http://POD_IP:8080/actuator/prometheus`
5. ✅ Application logs appear in Loki (Grafana Explore with Loki datasource)

---

## 🐛 Troubleshooting Quick Reference

### Issue: Pods won't start

```bash
# Check pod events
sudo k3s kubectl describe pod -n orders-desk <pod-name>

# Check logs
sudo k3s kubectl logs -n orders-desk <pod-name>
```

### Issue: Metrics endpoint returns 404

```bash
# Verify actuator endpoints
sudo k3s kubectl exec -n orders-desk $POD_NAME -- \
  curl -s http://localhost:8080/actuator | jq

# Should include prometheus in the list
```

### Issue: Prometheus not scraping

```bash
# 1. Check annotations
sudo k3s kubectl get pod -n orders-desk -o yaml | grep -A 5 "annotations:"

# 2. Check Prometheus logs
sudo k3s kubectl logs -n monitoring deployment/prometheus | tail -50

# 3. Verify network connectivity
POD_IP=$(sudo k3s kubectl get pod -n orders-desk -l app=orders-app -o jsonpath='{.items[0].status.podIP}')
sudo k3s kubectl run tmp --rm -i --image=curlimages/curl -- curl http://$POD_IP:8080/actuator/prometheus
```

---

## 📁 Modified Files Summary

```
✅ build.gradle                    - Added micrometer-registry-prometheus
✅ application.properties           - Enabled prometheus endpoint, changed app name
✅ k8s/app-deployment.yaml         - Added Prometheus annotations + env vars
✅ k8s/app-configmap.yaml          - Added actuator configuration
📄 PROMETHEUS-INTEGRATION-SUMMARY.md  - Full documentation
📄 DEPLOY-PROMETHEUS-INTEGRATION.md   - This quick guide
```

---

## 🎯 Next Steps After Deployment

1. **Monitor for 5-10 minutes** to see metrics populate
2. **Create custom dashboards** in Grafana for business metrics
3. **Set up alerting** (optional) with Prometheus AlertManager
4. **Add custom metrics** to track business events (orders, products, etc.)

---

## 📞 Support Resources

- **Full Implementation Details:** See `PROMETHEUS-INTEGRATION-SUMMARY.md`
- **Monitoring Stack Docs:** `k8s/monitoring/README.md`
- **Connection Instructions:** `docs/monitoring/connect_instructions.md`
- **Spring Boot Actuator:** https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html

---

**Status:** ✅ Ready to Deploy  
**Build Status:** ✅ Successful  
**Estimated Deployment Time:** 2-3 minutes  
**Rollback Plan:** Revert to previous ConfigMap/Deployment if needed

---

**Good luck with deployment! 🚀**

