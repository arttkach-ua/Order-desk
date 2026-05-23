# CI/CD Pipeline for k3s Deployment - Complete Implementation Guide

## Overview
This guide provides **step-by-step instructions** to set up a complete CI/CD pipeline that:
- ✅ Builds versioned Docker images on every master branch merge
- ✅ Automatically deploys to k3s cluster on Ubuntu server (195.206.233.23)
- ✅ Uses Git SHA for image versioning and rollback capability
- ✅ Includes PostgreSQL database in k3s with persistent storage
- ✅ Exposes application via NodePort for external access

**Tech Stack**: GitHub Actions, Docker, k3s, PostgreSQL, Spring Boot

---

## Architecture Overview

```
GitHub (master branch merge)
    ↓
GitHub Actions Workflow
    ↓
1. Run Tests (./gradlew test)
    ↓
2. Build JAR (./gradlew bootJar)
    ↓
3. Build Docker Image (tagged with Git SHA)
    ↓
4. Push to GitHub Container Registry (ghcr.io)
    ↓
5. Connect to k3s Cluster (195.206.233.23)
    ↓
6. Update Deployment with new image version
    ↓
7. Apply Kubernetes Manifests
    ↓
8. Wait for Rollout Completion
    ↓
Application Running on k3s ✅
```

---

## PART 1: k3s Cluster Preparation

### Step 1.1 - Connect to Ubuntu Server

```bash
# SSH into your Ubuntu server
ssh root@195.206.233.23
# Or if using a specific user:
ssh your_username@195.206.233.23
```

### Step 1.2 - Verify k3s Installation

```bash
# Check if k3s is installed and running
sudo systemctl status k3s

# Verify k3s version
k3s --version

# Check if kubectl is available
sudo k3s kubectl version

# If k3s is not installed, install it:
# curl -sfL https://get.k3s.io | sh -
```

**Expected Output**: k3s should be active and running.

### Step 1.3 - Create orders-desk Namespace

```bash
# Create namespace for the application
sudo k3s kubectl create namespace orders-desk

# Verify namespace creation
sudo k3s kubectl get namespaces
```

### Step 1.4 - Create Database Secret

```bash
# Create a secret for PostgreSQL credentials
# Replace 'your_secure_password' with a strong password
sudo k3s kubectl create secret generic postgres-secret \
  --from-literal=POSTGRES_PASSWORD=your_secure_password \
  --from-literal=DB_USERNAME=postgres \
  --from-literal=DB_PASSWORD=your_secure_password \
  --from-literal=POSTGRES_DB=mydatabase \
  -n orders-desk

# Verify secret creation
sudo k3s kubectl get secrets -n orders-desk
```

**Important**: Save the password you used - you'll need it for GitHub Secrets later.

### Step 1.5 - Verify k3s Storage Class

```bash
# Check if local-path storage class exists (default in k3s)
sudo k3s kubectl get storageclass

# You should see 'local-path (default)' in the output
```

**Expected Output**:
```
NAME                   PROVISIONER             RECLAIMPOLICY   VOLUMEBINDINGMODE
local-path (default)   rancher.io/local-path   Delete          WaitForFirstConsumer
```

### Step 1.6 - Extract Kubeconfig for GitHub Actions

```bash
# STEP 1: Extract and save kubeconfig to a file
sudo k3s kubectl config view --raw > ~/orders-desk-kubeconfig.yaml

# STEP 2: Make it readable only by you (security)
chmod 600 ~/orders-desk-kubeconfig.yaml

# STEP 3: Test kubectl access with the kubeconfig (optional verification)
KUBECONFIG=~/orders-desk-kubeconfig.yaml kubectl get nodes
```

**Expected Output**: Should show your k3s node(s) with STATUS "Ready"

### Step 1.7 - Encode Kubeconfig for GitHub Secrets

```bash
# STEP 1: Base64 encode the kubeconfig file
cat ~/orders-desk-kubeconfig.yaml | base64 -w 0 > ~/kubeconfig-base64.txt

# STEP 2: Display the encoded content (copy this entire string)
cat ~/kubeconfig-base64.txt

# You should see one long base64-encoded string
```

**Action Required**: Copy the entire base64-encoded string for GitHub Secrets setup.

**Troubleshooting**:
- If you get "No such file or directory" error, make sure Step 1.6 was completed first
- The file `~/orders-desk-kubeconfig.yaml` must exist before encoding it
- Verify file exists: `ls -lh ~/orders-desk-kubeconfig.yaml`

### Step 1.8 - Configure Firewall (if needed)

```bash
# Check if firewall is active
sudo ufw status

# If firewall is active, ensure k3s API port is open (for kubectl from GitHub Actions)
# This is only needed if accessing from external IPs
sudo ufw allow 6443/tcp

# Allow NodePort range for application access (30000-32767)
sudo ufw allow 30000:32767/tcp

# Reload firewall
sudo ufw reload
```

### Step 1.9 - Verify k3s Node Status

```bash
# Check node status
sudo k3s kubectl get nodes -o wide

# Check system pods
sudo k3s kubectl get pods -n kube-system
```

**All system pods should be Running**.

---

## PART 2: GitHub Repository Configuration

### Step 2.1 - Add GitHub Container Registry Token

```bash
# On your local machine or GitHub UI:
# 1. Go to GitHub Settings → Developer Settings → Personal Access Tokens → Tokens (classic)
# 2. Click "Generate new token (classic)"
# 3. Name: "k3s-deployment-token"
# 4. Select scopes:
#    ✅ write:packages
#    ✅ read:packages
#    ✅ delete:packages (optional)
# 5. Generate token and COPY IT (you won't see it again)
```

### Step 2.2 - Configure GitHub Repository Secrets

Go to your GitHub repository: **Settings → Secrets and Variables → Actions → New repository secret**

Add the following secrets:

| Secret Name | Value | Description |
|-------------|-------|-------------|
| `GHCR_TOKEN` | `ghp_xxxxxxxxxxxxx` | GitHub Personal Access Token from Step 2.1 |
| `K3S_KUBECONFIG` | Base64 string from Step 1.7 | k3s cluster access credentials |
| `POSTGRES_PASSWORD` | Same password from Step 1.4 | PostgreSQL database password |
| `K3S_HOST` | `195.206.233.23` | k3s server IP address |

**Verification**: You should have 4 secrets configured.

---

## PART 3: Kubernetes Manifests

All manifest files are located in the `k8s/` directory. They will be applied automatically by the GitHub Actions workflow.

### Manifest Files Created:

1. **namespace.yaml** - Creates the `orders-desk` namespace
2. **postgres-pvc.yaml** - Persistent Volume Claim for database storage
3. **postgres-deployment.yaml** - PostgreSQL database deployment
4. **postgres-service.yaml** - Database service (internal)
5. **app-configmap.yaml** - Application configuration
6. **app-deployment.yaml** - Orders application deployment (versioned)
7. **app-service.yaml** - Application service (NodePort for external access)

### Manual Deployment (Optional - For Testing Before CI/CD)

**Note**: This step is **optional**. GitHub Actions will automatically apply these manifests when you push to master. Only do this if you want to test manually first.

#### Option A: Apply from local machine with kubectl

```bash
# On your Windows/local machine with kubectl configured
# Set KUBECONFIG to the file from Step 1.6
$env:KUBECONFIG="path\to\orders-desk-kubeconfig.yaml"

# Test connection
kubectl get nodes

# Apply manifests from your local repository
kubectl apply -f k8s/

# Check deployment status
kubectl get all -n orders-desk
```

#### Option B: Apply from Ubuntu k3s server

```bash
# FIRST: Copy k8s directory to Ubuntu server
# On Windows (PowerShell):
scp -r E:\Java-projects\orders-desk\k8s root@195.206.233.23:~/

# OR: Clone the repository on the server
# On Ubuntu server:
cd ~
git clone https://github.com/YOUR_USERNAME/orders-desk.git
cd orders-desk

# THEN: Apply all manifests on the server
sudo k3s kubectl apply -f k8s/

# Check deployment status
sudo k3s kubectl get all -n orders-desk

# Watch pods coming up
sudo k3s kubectl get pods -n orders-desk -w
```

**Important**: The k8s/ directory must be present on whichever machine you're running the kubectl command from.

---

## PART 4: Build Versioning Strategy

### Version Tagging Scheme

Each Docker image is tagged with:
1. **Git Commit SHA** (short, 7 characters): `ghcr.io/your-username/orders-desk:a1b2c3d`
2. **Latest tag** (for master branch): `ghcr.io/your-username/orders-desk:latest`
3. **Build number** (optional): `ghcr.io/your-username/orders-desk:build-123`

### Version Format Examples:

```
ghcr.io/your-username/orders-desk:0.0.1-a1b2c3d
ghcr.io/your-username/orders-desk:latest
ghcr.io/your-username/orders-desk:0.0.1-SNAPSHOT-a1b2c3d
```

### Rollback Process:

```bash
# List all image versions
sudo k3s kubectl get deployment orders-app -n orders-desk -o jsonpath='{.spec.template.spec.containers[0].image}'

# Rollback to previous version
sudo k3s kubectl rollout undo deployment/orders-app -n orders-desk

# Rollback to specific version
sudo k3s kubectl set image deployment/orders-app \
  orders-app=ghcr.io/your-username/orders-desk:a1b2c3d \
  -n orders-desk

# Check rollout status
sudo k3s kubectl rollout status deployment/orders-app -n orders-desk
```

---

## PART 5: GitHub Actions Workflow

The deployment workflow is located at `.github/workflows/deploy.yml`.

### Workflow Triggers:
- ✅ Push to `master` branch only
- ❌ Does NOT run on pull requests
- ✅ Can be manually triggered via workflow_dispatch

### Workflow Steps:

1. **Checkout Code** - Get latest master code
2. **Set up JDK 17** - Prepare Java environment
3. **Run Tests** - Execute `./gradlew test` with Testcontainers
4. **Build JAR** - Execute `./gradlew bootJar`
5. **Extract Version** - Get Git SHA and project version
6. **Build Docker Image** - Create versioned container
7. **Login to GitHub Container Registry** - Authenticate with GHCR
8. **Push Docker Image** - Upload to ghcr.io with version tags
9. **Configure kubectl** - Set up k3s cluster access
10. **Update Deployment Manifest** - Inject new image version
11. **Apply Kubernetes Manifests** - Deploy to k3s
12. **Wait for Rollout** - Verify deployment success

---

## PART 6: Testing the Pipeline

### Step 6.1 - Make a Test Change

```bash
# On your local machine
git checkout master
git pull origin master

# Make a small change (e.g., add a comment)
echo "# CI/CD Pipeline Active" >> README.md

# Commit and push
git add README.md
git commit -m "test: trigger CI/CD pipeline"
git push origin master
```

### Step 6.2 - Monitor GitHub Actions

```bash
# Open your browser:
# https://github.com/YOUR_USERNAME/orders-desk/actions

# Watch the "Deploy to k3s" workflow execute
# Expected duration: 5-10 minutes
```

### Step 6.3 - Verify Deployment on k3s

```bash
# SSH to k3s server
ssh root@195.206.233.23

# Check pods
sudo k3s kubectl get pods -n orders-desk

# Expected output:
# NAME                          READY   STATUS    RESTARTS   AGE
# postgres-0                    1/1     Running   0          5m
# orders-app-xxxxxxxxxx-xxxxx   1/1     Running   0          2m

# Check services
sudo k3s kubectl get svc -n orders-desk

# Expected output shows NodePort (e.g., 30080)
```

### Step 6.4 - Test Application Access

```bash
# Get the NodePort
NODEPORT=$(sudo k3s kubectl get svc orders-service -n orders-desk -o jsonpath='{.spec.ports[0].nodePort}')

# Test locally on server
curl http://localhost:$NODEPORT/actuator/health

# Test externally (from your machine)
curl http://195.206.233.23:$NODEPORT/actuator/health
```

**Expected Response**:
```json
{"status":"UP"}
```

### Step 6.5 - Access Swagger UI

```bash
# Open in browser:
http://195.206.233.23:<NODEPORT>/swagger-ui.html

# Example:
http://195.206.233.23:30080/swagger-ui.html
```

### Step 6.6 - Check Application Logs

```bash
# Get pod name
POD_NAME=$(sudo k3s kubectl get pods -n orders-desk -l app=orders-app -o jsonpath='{.items[0].metadata.name}')

# View logs
sudo k3s kubectl logs $POD_NAME -n orders-desk

# Follow logs
sudo k3s kubectl logs -f $POD_NAME -n orders-desk
```

### Step 6.7 - Verify Database Connection

```bash
# Connect to PostgreSQL pod
sudo k3s kubectl exec -it postgres-0 -n orders-desk -- psql -U postgres -d mydatabase

# Run SQL commands
\dt  # List tables (should show Liquibase tables)
\q   # Quit
```

---

## PART 7: Troubleshooting

### Issue: GitHub Actions Can't Connect to k3s

**Symptoms**: `error: Unable to connect to the server`

**Solutions**:
```bash
# On k3s server - check if API is accessible externally
sudo netstat -tlnp | grep 6443

# Verify kubeconfig has correct server URL
sudo k3s kubectl config view | grep server

# Should show: server: https://127.0.0.1:6443
# For external access, you may need to change to public IP
```

### Issue: Pods Stuck in "Pending" State

**Symptoms**: `kubectl get pods` shows STATUS as "Pending"

**Solutions**:
```bash
# Check pod events
sudo k3s kubectl describe pod <POD_NAME> -n orders-desk

# Check storage class
sudo k3s kubectl get storageclass

# Check PVC status
sudo k3s kubectl get pvc -n orders-desk
```

### Issue: Image Pull Errors

**Symptoms**: `ErrImagePull` or `ImagePullBackOff`

**Solutions**:
```bash
# Verify image exists in registry
# Go to: https://github.com/YOUR_USERNAME?tab=packages

# Check image pull secret (if using private registry)
sudo k3s kubectl get secrets -n orders-desk

# Manually pull image to test
sudo k3s crictl pull ghcr.io/your-username/orders-desk:latest
```

### Issue: Database Connection Failed

**Symptoms**: Application logs show "Connection refused" or "Unknown host"

**Solutions**:
```bash
# Check PostgreSQL is running
sudo k3s kubectl get pods -n orders-desk | grep postgres

# Test database connectivity from app pod
POD=$(sudo k3s kubectl get pods -n orders-desk -l app=orders-app -o jsonpath='{.items[0].metadata.name}')
sudo k3s kubectl exec -it $POD -n orders-desk -- sh
# Inside pod:
nc -zv postgres-service 5432
exit
```

### Issue: NodePort Not Accessible

**Symptoms**: Can't access application from browser

**Solutions**:
```bash
# Check service
sudo k3s kubectl get svc -n orders-desk

# Check firewall
sudo ufw status

# Test locally first
curl http://localhost:<NODEPORT>/actuator/health

# Check if port is listening
sudo netstat -tlnp | grep <NODEPORT>
```

---

## PART 8: Maintenance & Operations

### View Deployment History

```bash
# Check rollout history
sudo k3s kubectl rollout history deployment/orders-app -n orders-desk

# View specific revision
sudo k3s kubectl rollout history deployment/orders-app -n orders-desk --revision=2
```

### Scale Application

```bash
# Scale to 3 replicas
sudo k3s kubectl scale deployment/orders-app --replicas=3 -n orders-desk

# Auto-scale (requires metrics-server)
sudo k3s kubectl autoscale deployment/orders-app --min=2 --max=5 --cpu-percent=80 -n orders-desk
```

### Update Configuration

```bash
# Edit ConfigMap
sudo k3s kubectl edit configmap app-config -n orders-desk

# Restart pods to pick up changes
sudo k3s kubectl rollout restart deployment/orders-app -n orders-desk
```

### Backup Database

```bash
# Create database backup
sudo k3s kubectl exec postgres-0 -n orders-desk -- pg_dump -U postgres mydatabase > backup.sql

# Restore database
cat backup.sql | sudo k3s kubectl exec -i postgres-0 -n orders-desk -- psql -U postgres mydatabase
```

### Monitor Resources

```bash
# Check resource usage
sudo k3s kubectl top nodes
sudo k3s kubectl top pods -n orders-desk

# Check events
sudo k3s kubectl get events -n orders-desk --sort-by='.lastTimestamp'
```

---

## PART 9: Production Enhancements (Optional)

### Add Ingress with SSL

```yaml
# k8s/ingress.yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: orders-ingress
  namespace: orders-desk
  annotations:
    cert-manager.io/cluster-issuer: letsencrypt-prod
spec:
  rules:
  - host: orders.yourdomain.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: orders-service
            port:
              number: 8080
  tls:
  - hosts:
    - orders.yourdomain.com
    secretName: orders-tls
```

### Add Health Checks to Dockerfile

```dockerfile
# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1
```

### Add Resource Limits

Already included in `app-deployment.yaml`:
```yaml
resources:
  requests:
    memory: "512Mi"
    cpu: "250m"
  limits:
    memory: "1Gi"
    cpu: "500m"
```

### Add Deployment Notifications

Add to GitHub Actions workflow:
```yaml
- name: Notify Deployment Success
  if: success()
  run: |
    curl -X POST https://api.telegram.org/bot${{ secrets.TELEGRAM_BOT_TOKEN }}/sendMessage \
      -d chat_id=${{ secrets.TELEGRAM_CHAT_ID }} \
      -d text="✅ Deployment successful: orders-desk:${{ steps.version.outputs.sha }}"
```

---

## PART 10: Quick Reference Commands

### Deployment Commands

```bash
# Deploy/update application
sudo k3s kubectl apply -f k8s/

# Check status
sudo k3s kubectl get all -n orders-desk

# View logs
sudo k3s kubectl logs -f deployment/orders-app -n orders-desk

# Restart deployment
sudo k3s kubectl rollout restart deployment/orders-app -n orders-desk

# Rollback deployment
sudo k3s kubectl rollout undo deployment/orders-app -n orders-desk
```

### Database Commands

```bash
# Connect to database
sudo k3s kubectl exec -it postgres-0 -n orders-desk -- psql -U postgres -d mydatabase

# Backup database
sudo k3s kubectl exec postgres-0 -n orders-desk -- pg_dump -U postgres mydatabase > backup-$(date +%Y%m%d).sql

# Check database pod
sudo k3s kubectl get pod postgres-0 -n orders-desk
```

### Debugging Commands

```bash
# Describe pod
sudo k3s kubectl describe pod <POD_NAME> -n orders-desk

# Shell into pod
sudo k3s kubectl exec -it <POD_NAME> -n orders-desk -- sh

# Port forward for local testing
sudo k3s kubectl port-forward svc/orders-service 8080:8080 -n orders-desk

# Check events
sudo k3s kubectl get events -n orders-desk
```

---

## Summary Checklist

### Before First Deployment:

- [ ] k3s installed and running on Ubuntu server
- [ ] Namespace `orders-desk` created
- [ ] PostgreSQL secret created with password
- [ ] Kubeconfig extracted and base64-encoded
- [ ] GitHub secrets configured (GHCR_TOKEN, K3S_KUBECONFIG, POSTGRES_PASSWORD, K3S_HOST)
- [ ] All k8s manifest files created in `k8s/` directory
- [ ] GitHub Actions workflow created at `.github/workflows/deploy.yml`
- [ ] Firewall configured (ports 6443, 30000-32767)

### After First Deployment:

- [ ] GitHub Actions workflow completed successfully
- [ ] Docker image pushed to GitHub Container Registry
- [ ] Pods running in `orders-desk` namespace
- [ ] Application accessible via NodePort
- [ ] Swagger UI accessible at http://195.206.233.23:<NODEPORT>/swagger-ui.html
- [ ] Database connection verified
- [ ] Application logs show no errors

---

## Support & Next Steps

### Further Reading:
- k3s Documentation: https://docs.k3s.io/
- Kubernetes Documentation: https://kubernetes.io/docs/
- GitHub Actions: https://docs.github.com/en/actions

### Enhancements to Consider:
1. Set up Ingress with custom domain and SSL
2. Implement horizontal pod autoscaling
3. Add monitoring with Prometheus/Grafana
4. Set up automated database backups
5. Implement blue-green or canary deployments
6. Add deployment notifications (Slack/Telegram)

---

**Document Version**: 1.0  
**Last Updated**: 2026-05-22  
**Author**: Orders Desk DevOps Team

