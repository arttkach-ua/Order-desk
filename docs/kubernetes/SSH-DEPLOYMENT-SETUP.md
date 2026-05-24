# SSH-Based Deployment Fix

## Issue Resolved ✅

**Problem**: GitHub Actions cannot connect to k3s API (port 6443) because it's not exposed to the internet (which is correct for security).

**Error**: `dial tcp ***:6443: i/o timeout`

**Root Cause**: k3s API server is only accessible locally (127.0.0.1:6443) and should NOT be exposed to the internet for security reasons.

## Solution: SSH-Based Deployment

Changed the deployment strategy from direct kubectl connection to **SSH-based deployment**:

### How It Works Now:

1. **GitHub Actions** builds Docker image
2. **Pushes** to GitHub Container Registry
3. **SSHs into k3s server** using SSH key
4. **Runs kubectl commands** directly on the server
5. **Deploys** application to k3s cluster

### Security Benefits:
- ✅ k3s API stays private (not exposed to internet)
- ✅ Uses SSH key authentication (more secure)
- ✅ No need to configure external firewall rules
- ✅ Standard DevOps practice

---

## 🔧 Required Setup: Add SSH Key Secret

You need to create one additional GitHub Secret for SSH authentication.

### Step 1: Generate SSH Key on k3s Server (if you don't have one)

```bash
# SSH to your k3s server
ssh root@195.206.233.23

# Generate SSH key pair (skip if you already have one)
ssh-keygen -t ed25519 -C "github-actions" -f ~/.ssh/github-actions-key -N ""

# Add public key to authorized_keys
cat ~/.ssh/github-actions-key.pub >> ~/.ssh/authorized_keys
chmod 600 ~/.ssh/authorized_keys

# Display the private key (copy this entire output)
cat ~/.ssh/github-actions-key
```

### Step 2: Add SSH Key to GitHub Secrets

1. Go to: **GitHub Repository → Settings → Secrets and variables → Actions**
2. Click: **New repository secret**
3. **Name**: `K3S_SSH_KEY`
4. **Value**: Paste the entire private key content (from Step 1)
5. Click: **Add secret**

### Example Private Key Format:
```
-----BEGIN OPENSSH PRIVATE KEY-----
b3BlbnNzaC1rZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAAAMwAAAAtzc2gtZW
...many lines...
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx==
-----END OPENSSH PRIVATE KEY-----
```

---

## 📋 Updated GitHub Secrets Required

| Secret Name | Value | Status |
|-------------|-------|--------|
| `GHCR_TOKEN` | GitHub PAT with `write:packages` | ✅ Should exist |
| `K3S_KUBECONFIG` | Base64 kubeconfig | ⚠️ No longer needed but harmless |
| `POSTGRES_PASSWORD` | Database password | ✅ Should exist |
| `K3S_HOST` | `195.206.233.23` | ✅ Should exist |
| **`K3S_SSH_KEY`** | SSH private key | ⚠️ **NEW - REQUIRED** |

---

## 🔄 Workflow Changes Summary

### What Changed:

**Before** (Direct kubectl connection):
```yaml
- name: Configure kubectl with k3s
  run: |
    echo "${{ secrets.K3S_KUBECONFIG }}" | base64 -d > $HOME/.kube/config
    kubectl apply -f k8s/
```

**After** (SSH-based deployment):
```yaml
- name: Deploy to k3s via SSH
  uses: appleboy/ssh-action@v1.0.0
  with:
    host: ${{ secrets.K3S_HOST }}
    key: ${{ secrets.K3S_SSH_KEY }}
    script: |
      sudo k3s kubectl apply -f /tmp/manifests/
```

### Deployment Steps Now:

1. ✅ Build & test application
2. ✅ Build Docker image
3. ✅ Push to GitHub Container Registry
4. ✅ **SSH to k3s server**
5. ✅ **Copy k8s manifests to server**
6. ✅ **Update image tag in manifests**
7. ✅ **Run kubectl commands on server**
8. ✅ **Wait for deployment rollout**
9. ✅ **Verify deployment**
10. ✅ **Show deployment status**

---

## ✅ After Adding SSH Key Secret

Once you add the `K3S_SSH_KEY` secret to GitHub:

1. **Commit and push changes** (or have them committed - pending your approval)
2. **Push will trigger deployment** automatically
3. **Workflow will SSH into server** and deploy
4. **Check GitHub Actions** for progress

### Verify Deployment:

```bash
# On your k3s server
sudo k3s kubectl get pods -n orders-desk
sudo k3s kubectl get svc -n orders-desk

# Test application
curl http://localhost:30080/actuator/health
```

### Access Application:

- **Application**: http://195.206.233.23:30080
- **Swagger UI**: http://195.206.233.23:30080/swagger-ui.html
- **Health Check**: http://195.206.233.23:30080/actuator/health

---

## 🆘 Troubleshooting

### If SSH connection fails:

**Check SSH key format**:
- Must include `-----BEGIN OPENSSH PRIVATE KEY-----`
- Must include `-----END OPENSSH PRIVATE KEY-----`
- No extra spaces or characters
- Copy entire key including headers

**Test SSH connection locally**:
```bash
# Save your private key to a file
echo "YOUR_PRIVATE_KEY" > test-key
chmod 600 test-key

# Test SSH connection
ssh -i test-key root@195.206.233.23 "sudo k3s kubectl get nodes"

# If successful, the key works!
```

### If deployment still fails:

1. Check GitHub Actions logs for specific error
2. Verify all 5 secrets are configured correctly
3. Test SSH access manually as shown above
4. Ensure root user has sudo access to k3s kubectl

---

## 📊 Files Changed

1. ✅ **Dockerfile**: Updated to `eclipse-temurin:17-jre-alpine`
2. ✅ **`.github/workflows/deploy.yml`**: Changed to SSH-based deployment
3. ✅ **AGENTS.md**: Added Git control rules

---

## 🎯 Next Steps

1. **Generate SSH key** on k3s server (if don't have one)
2. **Add `K3S_SSH_KEY` secret** to GitHub repository
3. **Let me know when ready** - I'll commit and push the changes
4. **Monitor deployment** in GitHub Actions

---

**Created**: 2026-05-24  
**Issue**: k3s API timeout from GitHub Actions  
**Solution**: SSH-based deployment  
**Status**: Waiting for SSH key secret to be configured

