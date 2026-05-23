# Kubernetes Manifests for Orders Desk

This directory contains all Kubernetes manifests for deploying the Orders Desk application to k3s.

## 📁 Files Overview

| File | Description | Resource Type |
|------|-------------|---------------|
| `namespace.yaml` | Creates the `orders-desk` namespace | Namespace |
| `postgres-pvc.yaml` | Persistent storage claim for PostgreSQL (5Gi) | PersistentVolumeClaim |
| `postgres-deployment.yaml` | PostgreSQL 15 database deployment | Deployment |
| `postgres-service.yaml` | Internal service for database access | Service (ClusterIP) |
| `app-configmap.yaml` | Application configuration (Spring properties) | ConfigMap |
| `app-deployment.yaml` | Orders application deployment (2 replicas) | Deployment |
| `app-service.yaml` | External service for application access | Service (NodePort:30080) |

## 🚀 Manual Deployment

### Prerequisites
- k3s cluster running on Ubuntu server
- kubectl configured to access the cluster
- PostgreSQL secret created (see deployment guide)

### Deploy All Resources

```bash
# Apply all manifests in order
kubectl apply -f namespace.yaml
kubectl apply -f postgres-pvc.yaml
kubectl apply -f postgres-deployment.yaml
kubectl apply -f postgres-service.yaml
kubectl apply -f app-configmap.yaml
kubectl apply -f app-deployment.yaml
kubectl apply -f app-service.yaml

# Or apply all at once
kubectl apply -f .
```

### Verify Deployment

```bash
# Check all resources
kubectl get all -n orders-desk

# Check specific resources
kubectl get pods -n orders-desk
kubectl get svc -n orders-desk
kubectl get pvc -n orders-desk

# Watch pods starting up
kubectl get pods -n orders-desk -w
```

## 🔧 Configuration

### Database Secret (Required)

Before deploying, create the database secret:

```bash
kubectl create secret generic postgres-secret \
  --from-literal=POSTGRES_PASSWORD=your_secure_password \
  --from-literal=DB_USERNAME=postgres \
  --from-literal=DB_PASSWORD=your_secure_password \
  --from-literal=POSTGRES_DB=mydatabase \
  -n orders-desk
```

### Application Image

The `app-deployment.yaml` uses a placeholder image path that will be replaced by GitHub Actions:

```yaml
image: ghcr.io/GITHUB_USERNAME/orders-desk:latest
```

**Manual deployment**: Update this line with your actual image repository before applying.

## 📊 Resource Allocation

### PostgreSQL
- **Requests**: 256Mi memory, 250m CPU
- **Limits**: 512Mi memory, 500m CPU
- **Storage**: 5Gi persistent volume (local-path)

### Application
- **Replicas**: 2 pods
- **Requests**: 512Mi memory, 250m CPU per pod
- **Limits**: 1Gi memory, 500m CPU per pod
- **Strategy**: RollingUpdate (maxSurge: 1, maxUnavailable: 0)

## 🌐 Service Access

### Application Service
- **Type**: NodePort
- **Port**: 8080 (internal)
- **NodePort**: 30080 (external)
- **Access**: `http://<k3s-node-ip>:30080`

### PostgreSQL Service
- **Type**: ClusterIP (internal only)
- **Port**: 5432
- **DNS**: `postgres-service.orders-desk.svc.cluster.local`

## 🔍 Health Checks

### Liveness Probe
- **Endpoint**: `/actuator/health/liveness`
- **Initial Delay**: 60s
- **Period**: 10s
- **Timeout**: 5s

### Readiness Probe
- **Endpoint**: `/actuator/health/readiness`
- **Initial Delay**: 30s
- **Period**: 5s
- **Timeout**: 3s

## 🛠️ Common Operations

### Update Application Image

```bash
# Set new image version
kubectl set image deployment/orders-app \
  orders-app=ghcr.io/your-username/orders-desk:v1.2.3 \
  -n orders-desk

# Watch rollout
kubectl rollout status deployment/orders-app -n orders-desk
```

### Scale Application

```bash
# Scale to 3 replicas
kubectl scale deployment/orders-app --replicas=3 -n orders-desk

# Verify
kubectl get pods -n orders-desk
```

### View Logs

```bash
# Application logs
kubectl logs -f deployment/orders-app -n orders-desk

# Database logs
kubectl logs -f deployment/postgres -n orders-desk

# Specific pod
kubectl logs -f <pod-name> -n orders-desk
```

### Restart Deployment

```bash
# Restart application (recreates all pods)
kubectl rollout restart deployment/orders-app -n orders-desk

# Restart database
kubectl rollout restart deployment/postgres -n orders-desk
```

### Rollback Deployment

```bash
# Rollback to previous version
kubectl rollout undo deployment/orders-app -n orders-desk

# Check rollout history
kubectl rollout history deployment/orders-app -n orders-desk
```

### Debug Pod Issues

```bash
# Describe pod for events
kubectl describe pod <pod-name> -n orders-desk

# Shell into running pod
kubectl exec -it <pod-name> -n orders-desk -- sh

# Port forward for local testing
kubectl port-forward svc/orders-service 8080:8080 -n orders-desk
```

## 🗄️ Database Operations

### Connect to PostgreSQL

```bash
# Connect to database pod
kubectl exec -it postgres-<pod-id> -n orders-desk -- psql -U postgres -d mydatabase

# Run SQL commands
\dt              # List tables
\d table_name    # Describe table
SELECT * FROM product_categories;
\q               # Quit
```

### Backup Database

```bash
# Create backup
kubectl exec postgres-<pod-id> -n orders-desk -- \
  pg_dump -U postgres mydatabase > backup-$(date +%Y%m%d).sql

# Restore from backup
cat backup.sql | kubectl exec -i postgres-<pod-id> -n orders-desk -- \
  psql -U postgres mydatabase
```

### Check Database Storage

```bash
# Check PVC status
kubectl get pvc postgres-pvc -n orders-desk

# Describe PVC for details
kubectl describe pvc postgres-pvc -n orders-desk

# Check actual usage in pod
kubectl exec postgres-<pod-id> -n orders-desk -- df -h /var/lib/postgresql/data
```

## 🔐 Security Notes

### Secrets
- PostgreSQL credentials are stored in Kubernetes Secret
- Never commit secrets to version control
- Secret is created manually or by CI/CD pipeline

### Image Pull Policy
- Set to `Always` to ensure latest image version
- Images pulled from GitHub Container Registry (ghcr.io)
- Requires image pull secret for private repositories

### Non-root User
- Application runs as non-root user in container
- Enhanced security posture in Kubernetes

## 📈 Monitoring

### Check Resource Usage

```bash
# Node resources
kubectl top nodes

# Pod resources
kubectl top pods -n orders-desk

# Detailed pod metrics
kubectl describe pod <pod-name> -n orders-desk | grep -A 5 "Resources"
```

### Check Events

```bash
# All events in namespace
kubectl get events -n orders-desk --sort-by='.lastTimestamp'

# Watch events in real-time
kubectl get events -n orders-desk -w
```

## 🚨 Troubleshooting

### Pods Not Starting

```bash
# Check pod status
kubectl get pods -n orders-desk

# Check pod events
kubectl describe pod <pod-name> -n orders-desk

# Check logs
kubectl logs <pod-name> -n orders-desk
```

### Image Pull Errors

```bash
# Verify image exists
# Check GitHub packages: https://github.com/YOUR_USERNAME?tab=packages

# Check image pull secret
kubectl get secrets -n orders-desk

# Manually pull image on node
sudo k3s crictl pull ghcr.io/your-username/orders-desk:latest
```

### Database Connection Issues

```bash
# Check database pod
kubectl get pod -l app=postgres -n orders-desk

# Test database connectivity
kubectl run tmp-shell --rm -i --tty --image postgres:15 -n orders-desk -- \
  psql -h postgres-service -U postgres -d mydatabase
```

### Service Not Accessible

```bash
# Check service
kubectl get svc orders-service -n orders-desk

# Check endpoints
kubectl get endpoints orders-service -n orders-desk

# Test from within cluster
kubectl run tmp-shell --rm -i --tty --image curlimages/curl -n orders-desk -- \
  curl http://orders-service:8080/actuator/health
```

## 🔄 CI/CD Integration

These manifests are automatically applied by the GitHub Actions workflow (`.github/workflows/deploy.yml`) when code is merged to the master branch.

The workflow:
1. Builds versioned Docker image
2. Pushes to GitHub Container Registry
3. Updates `app-deployment.yaml` with new image version
4. Applies all manifests to k3s cluster
5. Waits for rollout completion

## 📝 Notes

- **Storage Class**: Uses k3s default `local-path` provisioner
- **Namespace**: All resources are isolated in `orders-desk` namespace
- **Network Policy**: Not configured (consider adding for production)
- **Ingress**: Not configured (using NodePort for simplicity)
- **TLS**: Not configured (consider adding cert-manager for HTTPS)

## 📚 Additional Resources

- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [k3s Documentation](https://docs.k3s.io/)
- [kubectl Cheat Sheet](https://kubernetes.io/docs/reference/kubectl/cheatsheet/)

