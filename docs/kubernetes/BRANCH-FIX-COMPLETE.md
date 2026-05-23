# CI/CD Pipeline Fix - Branch Trigger Issue RESOLVED

## Issue Found ✅
The GitHub Actions workflow was configured to trigger on `master` branch, but your repository uses `main` as the default branch. This is why the workflow didn't run when you merged your test PR.

## Changes Made

### 1. Updated Workflow Trigger (.github/workflows/deploy.yml)
**Before:**
```yaml
on:
  push:
    branches:
      - master
```

**After:**
```yaml
on:
  push:
    branches:
      - main
      - master  # Keep for compatibility if branch is renamed
```

### 2. Updated Docker Image Repository (k8s/app-deployment.yaml)
**Before:**
```yaml
image: ghcr.io/GITHUB_USERNAME/orders-desk:latest
```

**After:**
```yaml
image: ghcr.io/arttkach-ua/order-desk:latest
```

### 3. Updated Workflow sed Pattern
Updated the image replacement pattern to match your actual GitHub username:
```yaml
sed -i "s|image: ghcr.io/arttkach-ua/order-desk:latest|image: ${IMAGE_TAG}|g"
```

## Repository Information
- **GitHub Username**: arttkach-ua
- **Repository**: Order-desk
- **Default Branch**: main
- **Current Branch**: test-deployment
- **Docker Registry**: ghcr.io (GitHub Container Registry)

## Next Steps - ACTION REQUIRED

### Step 1: Commit the Changes
```powershell
# On your Windows machine (you're currently on test-deployment branch)
cd E:\Java-projects\orders-desk

git add .github/workflows/deploy.yml
git add k8s/app-deployment.yaml
git commit -m "fix: update CI/CD workflow to trigger on main branch"
```

### Step 2: Merge to Main Branch
You have two options:

**Option A: Create PR from test-deployment to main (Recommended)**
```powershell
# Push your changes
git push origin test-deployment

# Then create a PR on GitHub:
# https://github.com/arttkach-ua/Order-desk/compare/main...test-deployment
# When you merge it, the workflow will trigger automatically!
```

**Option B: Direct merge to main (if you have access)**
```powershell
# Switch to main branch
git checkout main

# Pull latest changes
git pull origin main

# Merge test-deployment
git merge test-deployment

# Push to main
git push origin main
```

### Step 3: Verify GitHub Secrets are Configured
Before the workflow can deploy, ensure these 4 secrets exist in:
**GitHub Repository → Settings → Secrets and variables → Actions**

| Secret Name | Status | Notes |
|-------------|--------|-------|
| `GHCR_TOKEN` | ⚠️ Check | GitHub Personal Access Token with `write:packages` scope |
| `K3S_KUBECONFIG` | ⚠️ Check | Base64-encoded kubeconfig from k3s server |
| `POSTGRES_PASSWORD` | ⚠️ Check | Same password used when creating postgres-secret |
| `K3S_HOST` | ⚠️ Check | Should be `195.206.233.23` |

**How to verify:**
Go to: https://github.com/arttkach-ua/Order-desk/settings/secrets/actions

You should see all 4 secrets listed. If not, create them following the guide in `docs/kubernetes/CI-CD-K3S-DEPLOYMENT-GUIDE.md`

### Step 4: Watch the Deployment
After pushing to `main`:

1. Go to: https://github.com/arttkach-ua/Order-desk/actions
2. You should see "Deploy to k3s" workflow running
3. Watch the progress (takes 5-10 minutes)
4. Green checkmark = Success! ✅

### Step 5: Access Your Application
Once deployed successfully:

```bash
# Application
http://195.206.233.23:30080

# Swagger UI (API Documentation)
http://195.206.233.23:30080/swagger-ui.html

# Health Check
http://195.206.233.23:30080/actuator/health
```

## Troubleshooting

### If Workflow Still Doesn't Trigger:
1. Verify you pushed to `main` branch (not `test-deployment` or other branch)
2. Check workflow triggers: https://github.com/arttkach-ua/Order-desk/blob/main/.github/workflows/deploy.yml
3. Try manual trigger: Actions → Deploy to k3s → Run workflow → Select "main" → Run

### If Workflow Fails with "unauthorized" Error:
- Check `GHCR_TOKEN` is valid and has `write:packages` scope
- Regenerate token if needed: GitHub Settings → Developer Settings → Personal Access Tokens

### If Workflow Fails with "Unable to connect to server":
- Verify `K3S_KUBECONFIG` is correctly base64-encoded
- Check `K3S_HOST` is `195.206.233.23`
- Ensure port 6443 is accessible from GitHub Actions runners

### If Pods Show ImagePullBackOff:
- First deployment will create the image, so this is expected initially
- Wait for Docker build step to complete first
- Check image exists: https://github.com/arttkach-ua?tab=packages

## Summary

### Files Modified: 2
- ✅ `.github/workflows/deploy.yml` - Added `main` branch trigger
- ✅ `k8s/app-deployment.yaml` - Updated image to use your GitHub username

### Root Cause:
Workflow configured for `master` branch, but repository uses `main` branch

### Solution:
Updated workflow to trigger on `main` branch (+ kept `master` for compatibility)

### Status:
🟢 **READY TO DEPLOY** - Commit changes and push to main!

---

**Next Action**: Run the commands in Step 1 & 2 above, then monitor GitHub Actions! 🚀

**Created**: 2026-05-24  
**Issue**: Workflow not triggering on merge  
**Resolution**: Branch name mismatch fixed

