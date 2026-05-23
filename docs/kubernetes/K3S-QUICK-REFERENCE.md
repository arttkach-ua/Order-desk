# K3S Deployment - Quick Reference Card

## 🚀 Quick Deploy Commands (On k3s Server)

### First-Time Setup
```bash
# 1. Create namespace
sudo k3s kubectl create namespace orders-desk

# 2. Create database secret
sudo k3s kubectl create secret generic postgres-secret \
  --from-literal=POSTGRES_PASSWORD=your_secure_password \
  --from-literal=DB_USERNAME=postgres \
  --from-literal=DB_PASSWORD=your_secure_password \
  --from-literal=POSTGRES_DB=mydatabase \
  -n orders-desk

# 3. Extract kubeconfig (for GitHub Actions)
sudo k3s kubectl config view --raw | base64 -w 0 > ~/kubeconfig-base64.txt
```

### Deploy Application
```bash
# Apply all manifests
sudo k3s kubectl apply -f k8s/

# Watch deployment
sudo k3s kubectl get pods -n orders-desk -w
```

## 📊 Status Check Commands

```bash
# All resources
sudo k3s kubectl get all -n orders-desk

# Pods only
sudo k3s kubectl get pods -n orders-desk

# Services
sudo k3s kubectl get svc -n orders-desk

# Get NodePort
sudo k3s kubectl get svc orders-service -n orders-desk -o jsonpath='{.spec.ports[0].nodePort}'
```

## 🔍 Debugging Commands

```bash
# View logs
sudo k3s kubectl logs -f deployment/orders-app -n orders-desk

# Describe pod
sudo k3s kubectl describe pod <pod-name> -n orders-desk

# Shell into pod
sudo k3s kubectl exec -it <pod-name> -n orders-desk -- sh

# Check events
sudo k3s kubectl get events -n orders-desk --sort-by='.lastTimestamp'
```

## 🔄 Update/Rollback Commands

```bash
# Update image
sudo k3s kubectl set image deployment/orders-app \
  orders-app=ghcr.io/username/orders-desk:v1.2.3 \
  -n orders-desk

# Rollback
sudo k3s kubectl rollout undo deployment/orders-app -n orders-desk

# Restart
sudo k3s kubectl rollout restart deployment/orders-app -n orders-desk

# Check status
sudo k3s kubectl rollout status deployment/orders-app -n orders-desk
```

## 🗄️ Database Commands

```bash
# Connect to PostgreSQL
sudo k3s kubectl exec -it postgres-<pod-id> -n orders-desk -- psql -U postgres -d mydatabase

# Backup database
sudo k3s kubectl exec postgres-<pod-id> -n orders-desk -- \
  pg_dump -U postgres mydatabase > backup.sql

# View database logs
sudo k3s kubectl logs -f deployment/postgres -n orders-desk
```

## 🌐 Access URLs

```bash
# Get NodePort
NODEPORT=$(sudo k3s kubectl get svc orders-service -n orders-desk -o jsonpath='{.spec.ports[0].nodePort}')

# Application: http://195.206.233.23:${NODEPORT}
# Swagger UI: http://195.206.233.23:${NODEPORT}/swagger-ui.html
# Health: http://195.206.233.23:${NODEPORT}/actuator/health
```

## 🧹 Cleanup Commands

```bash
# Delete all resources
sudo k3s kubectl delete -f k8s/

# Delete namespace (deletes everything)
sudo k3s kubectl delete namespace orders-desk

# Delete specific resource
sudo k3s kubectl delete deployment orders-app -n orders-desk
```

## 🔐 GitHub Secrets Required

| Secret Name | Value Example | Description |
|-------------|---------------|-------------|
| `GHCR_TOKEN` | `ghp_xxxxxxxxxxxx` | GitHub Personal Access Token |
| `K3S_KUBECONFIG` | `<base64-string>` | Base64 kubeconfig from k3s |
| `POSTGRES_PASSWORD` | `secure_password_123` | Database password |
| `K3S_HOST` | `195.206.233.23` | k3s server IP |

## 📋 Pre-Deployment Checklist

- [ ] k3s installed and running
- [ ] Namespace created
- [ ] Database secret created
- [ ] Kubeconfig extracted
- [ ] GitHub secrets configured
- [ ] Firewall ports open (6443, 30000-32767)
- [ ] k8s manifests in repository
- [ ] GitHub Actions workflow created

## 🎯 Testing After Deployment

```bash
# 1. Check pods
sudo k3s kubectl get pods -n orders-desk

# 2. Check services
sudo k3s kubectl get svc -n orders-desk

# 3. Test health endpoint
NODEPORT=$(sudo k3s kubectl get svc orders-service -n orders-desk -o jsonpath='{.spec.ports[0].nodePort}')
curl http://localhost:${NODEPORT}/actuator/health

# 4. Test from external
curl http://195.206.233.23:${NODEPORT}/actuator/health

# 5. Access Swagger UI in browser
echo "http://195.206.233.23:${NODEPORT}/swagger-ui.html"
```

## 🚨 Common Issues & Fixes

### Pods Pending
```bash
# Check storage class
sudo k3s kubectl get storageclass

# Describe PVC
sudo k3s kubectl describe pvc postgres-pvc -n orders-desk
```

### Image Pull Error
```bash
# Check if image exists in registry
# Manually pull to test
sudo k3s crictl pull ghcr.io/username/orders-desk:latest
```

### Database Connection Failed
```bash
# Check postgres pod
sudo k3s kubectl get pod -l app=postgres -n orders-desk

# Check logs
sudo k3s kubectl logs deployment/postgres -n orders-desk
```

### Can't Access from Browser
```bash
# Check firewall
sudo ufw status

# Open NodePort range
sudo ufw allow 30000:32767/tcp
sudo ufw reload
```

## 📞 Support Commands

```bash
# Cluster info
sudo k3s kubectl cluster-info

# Node status
sudo k3s kubectl get nodes -o wide

# System pods
sudo k3s kubectl get pods -n kube-system

# Resource usage
sudo k3s kubectl top nodes
sudo k3s kubectl top pods -n orders-desk
```

