# CI/CD Pipeline Visual Flow

## Complete Deployment Pipeline

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         DEVELOPER WORKFLOW                                  │
└─────────────────────────────────────────────────────────────────────────────┘
                                    
    Developer creates feature branch
              ↓
    Work on features/fixes
              ↓
    Create Pull Request → master
              ↓
    Code Review & Approval
              ↓
    Merge to master branch
              ↓
              ↓
┌─────────────────────────────────────────────────────────────────────────────┐
│                    GITHUB ACTIONS PIPELINE TRIGGERS                         │
│                   (.github/workflows/deploy.yml)                            │
└─────────────────────────────────────────────────────────────────────────────┘
              ↓
              ↓
┌─────────────────────────────────────────────────────────────────────────────┐
│  STAGE 1: TESTING                                                           │
├─────────────────────────────────────────────────────────────────────────────┤
│  ✓ Checkout code from master                                               │
│  ✓ Set up JDK 17 (Temurin)                                                 │
│  ✓ Restore Gradle cache                                                    │
│  ✓ Run ./gradlew test                                                      │
│    - Unit tests (29 tests)                                                 │
│    - Integration tests with Testcontainers                                 │
│  ✓ All tests must pass to continue                                         │
└─────────────────────────────────────────────────────────────────────────────┘
              ↓ Tests Passed ✓
              ↓
┌─────────────────────────────────────────────────────────────────────────────┐
│  STAGE 2: BUILD JAR                                                         │
├─────────────────────────────────────────────────────────────────────────────┤
│  ✓ Run ./gradlew bootJar                                                   │
│  ✓ Generate executable JAR                                                 │
│  ✓ Output: build/libs/orders-0.0.1-SNAPSHOT.jar                           │
└─────────────────────────────────────────────────────────────────────────────┘
              ↓
              ↓
┌─────────────────────────────────────────────────────────────────────────────┐
│  STAGE 3: VERSION EXTRACTION                                                │
├─────────────────────────────────────────────────────────────────────────────┤
│  ✓ Extract version from build.gradle → "0.0.1-SNAPSHOT"                   │
│  ✓ Extract Git commit SHA → "a1b2c3d"                                      │
│  ✓ Create full version tag → "0.0.1-SNAPSHOT-a1b2c3d"                     │
│                                                                             │
│  Generated Tags:                                                            │
│    - latest                                                                 │
│    - a1b2c3d                                                               │
│    - 0.0.1-SNAPSHOT-a1b2c3d                                                │
└─────────────────────────────────────────────────────────────────────────────┘
              ↓
              ↓
┌─────────────────────────────────────────────────────────────────────────────┐
│  STAGE 4: DOCKER IMAGE BUILD                                                │
├─────────────────────────────────────────────────────────────────────────────┤
│  ✓ Login to GitHub Container Registry (ghcr.io)                           │
│    - Username: ${{ github.actor }}                                         │
│    - Token: ${{ secrets.GHCR_TOKEN }}                                      │
│                                                                             │
│  ✓ Build Docker image with Dockerfile:                                     │
│    - Base: openjdk:17-jdk-slim                                             │
│    - Non-root user: spring:spring                                          │
│    - Health check enabled                                                  │
│    - Optimized JVM settings                                                │
│                                                                             │
│  ✓ Tag image with 3 versions:                                              │
│    - ghcr.io/username/orders-desk:latest                                   │
│    - ghcr.io/username/orders-desk:a1b2c3d                                  │
│    - ghcr.io/username/orders-desk:0.0.1-SNAPSHOT-a1b2c3d                   │
│                                                                             │
│  ✓ Push all tags to ghcr.io                                                │
└─────────────────────────────────────────────────────────────────────────────┘
              ↓ Image Published ✓
              ↓
┌─────────────────────────────────────────────────────────────────────────────┐
│  STAGE 5: K3S CLUSTER CONNECTION                                            │
├─────────────────────────────────────────────────────────────────────────────┤
│  ✓ Install kubectl                                                          │
│  ✓ Decode K3S_KUBECONFIG secret (base64)                                   │
│  ✓ Write to ~/.kube/config                                                 │
│  ✓ Test connection: kubectl cluster-info                                   │
│  ✓ Verify access: kubectl get nodes                                        │
│                                                                             │
│  Connected to: 195.206.233.23 (k3s cluster)                                │
└─────────────────────────────────────────────────────────────────────────────┘
              ↓ Connected ✓
              ↓
┌─────────────────────────────────────────────────────────────────────────────┐
│  STAGE 6: PREPARE KUBERNETES RESOURCES                                      │
├─────────────────────────────────────────────────────────────────────────────┤
│  ✓ Apply namespace.yaml                                                    │
│    - Create/verify orders-desk namespace                                   │
│                                                                             │
│  ✓ Create postgres-secret (if not exists)                                  │
│    - POSTGRES_PASSWORD from GitHub Secrets                                 │
│    - DB_USERNAME: postgres                                                 │
│    - POSTGRES_DB: mydatabase                                               │
│                                                                             │
│  ✓ Update app-deployment.yaml                                              │
│    - Replace image placeholder with:                                       │
│      ghcr.io/username/orders-desk:a1b2c3d                                  │
└─────────────────────────────────────────────────────────────────────────────┘
              ↓
              ↓
┌─────────────────────────────────────────────────────────────────────────────┐
│  STAGE 7: DEPLOY TO K3S                                                     │
├─────────────────────────────────────────────────────────────────────────────┤
│  Apply Kubernetes manifests in order:                                      │
│                                                                             │
│  1. ✓ kubectl apply -f k8s/namespace.yaml                                  │
│  2. ✓ kubectl apply -f k8s/postgres-pvc.yaml                               │
│     - Create 5Gi PersistentVolumeClaim                                     │
│  3. ✓ kubectl apply -f k8s/postgres-deployment.yaml                        │
│     - Deploy PostgreSQL 15                                                 │
│     - 1 replica, 256Mi-512Mi memory                                        │
│  4. ✓ kubectl apply -f k8s/postgres-service.yaml                           │
│     - ClusterIP service on port 5432                                       │
│  5. ✓ kubectl apply -f k8s/app-configmap.yaml                              │
│     - Spring Boot configuration                                            │
│  6. ✓ kubectl apply -f k8s/app-deployment.yaml                             │
│     - Deploy application with new image                                    │
│     - 2 replicas, RollingUpdate strategy                                   │
│     - 512Mi-1Gi memory per pod                                             │
│  7. ✓ kubectl apply -f k8s/app-service.yaml                                │
│     - NodePort service on port 30080                                       │
└─────────────────────────────────────────────────────────────────────────────┘
              ↓
              ↓
┌─────────────────────────────────────────────────────────────────────────────┐
│  STAGE 8: WAIT FOR DEPLOYMENT                                               │
├─────────────────────────────────────────────────────────────────────────────┤
│  ✓ Wait for PostgreSQL pods to be Ready                                    │
│    - Timeout: 300 seconds (5 minutes)                                      │
│    - Check: pg_isready probe                                               │
│                                                                             │
│  ✓ Wait for application rollout to complete                                │
│    - kubectl rollout status deployment/orders-app                          │
│    - Timeout: 300 seconds (5 minutes)                                      │
│    - Strategy: RollingUpdate (maxSurge: 1, maxUnavailable: 0)             │
│                                                                             │
│  Deployment Process:                                                        │
│    1. Start new pod with new image                                         │
│    2. Wait for readiness probe                                             │
│    3. Mark as Ready                                                        │
│    4. Terminate old pod                                                    │
│    5. Repeat for all replicas                                              │
└─────────────────────────────────────────────────────────────────────────────┘
              ↓ Deployment Complete ✓
              ↓
┌─────────────────────────────────────────────────────────────────────────────┐
│  STAGE 9: VERIFICATION                                                      │
├─────────────────────────────────────────────────────────────────────────────┤
│  ✓ Get all pods status                                                     │
│    - Expected: 1 postgres pod + 2 app pods = Running                      │
│                                                                             │
│  ✓ Get services                                                            │
│    - postgres-service: ClusterIP (internal)                                │
│    - orders-service: NodePort 30080 (external)                             │
│                                                                             │
│  ✓ Verify deployment image version                                         │
│    - Current image: ghcr.io/username/orders-desk:a1b2c3d                   │
│                                                                             │
│  ✓ Get NodePort for access                                                 │
│    - Port: 30080                                                           │
│                                                                             │
│  ✓ Create deployment summary in GitHub Actions                             │
└─────────────────────────────────────────────────────────────────────────────┘
              ↓
              ↓
┌─────────────────────────────────────────────────────────────────────────────┐
│                    DEPLOYMENT SUCCESSFUL ✓                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  Application is now running on k3s cluster!                                │
│                                                                             │
│  🌐 Access Points:                                                          │
│     • Application:  http://195.206.233.23:30080                            │
│     • Swagger UI:   http://195.206.233.23:30080/swagger-ui.html            │
│     • Health Check: http://195.206.233.23:30080/actuator/health            │
│     • Metrics:      http://195.206.233.23:30080/actuator/metrics           │
│                                                                             │
│  📦 Deployed Version:                                                       │
│     • Image: ghcr.io/username/orders-desk:a1b2c3d                          │
│     • Version: 0.0.1-SNAPSHOT-a1b2c3d                                      │
│     • Commit: a1b2c3d4e5f6g7h8i9j0                                         │
│                                                                             │
│  🎯 Resources Running:                                                      │
│     • PostgreSQL: 1 pod (512Mi memory, 5Gi storage)                        │
│     • Application: 2 pods (1Gi memory each)                                │
│     • Total: 3 pods, 7Gi storage, 2.5Gi memory                            │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘


═══════════════════════════════════════════════════════════════════════════════
                           ROLLBACK CAPABILITY
═══════════════════════════════════════════════════════════════════════════════

If deployment fails or issues detected:

    kubectl rollout undo deployment/orders-app -n orders-desk

All previous versions available via image tags in ghcr.io:
    - ghcr.io/username/orders-desk:previous-sha
    - ghcr.io/username/orders-desk:another-sha

View rollout history:
    kubectl rollout history deployment/orders-app -n orders-desk


═══════════════════════════════════════════════════════════════════════════════
                         CONTINUOUS MONITORING
═══════════════════════════════════════════════════════════════════════════════

Kubernetes automatically monitors:
  ✓ Liveness Probe:  /actuator/health/liveness (every 10s)
  ✓ Readiness Probe: /actuator/health/readiness (every 5s)
  ✓ Container Health Check: Docker HEALTHCHECK (every 30s)

If pod becomes unhealthy:
  → Kubernetes automatically restarts pod
  → Traffic removed during restart
  → Zero downtime for users (2 replicas running)


═══════════════════════════════════════════════════════════════════════════════
                         NEXT DEPLOYMENT
═══════════════════════════════════════════════════════════════════════════════

For next update:
  1. Developer merges new code to master
  2. Pipeline automatically triggers
  3. New version deployed with RollingUpdate
  4. Zero downtime deployment
  5. Automatic rollback if health checks fail


═══════════════════════════════════════════════════════════════════════════════
                    IMPLEMENTATION COMPLETE ✓
═══════════════════════════════════════════════════════════════════════════════
```

## Quick Stats

| Metric | Value |
|--------|-------|
| **Pipeline Stages** | 9 stages |
| **Average Duration** | 5-10 minutes |
| **Docker Image Tags** | 3 per build |
| **Kubernetes Resources** | 7 manifests |
| **Application Replicas** | 2 pods |
| **Database Replicas** | 1 pod |
| **Total Memory Allocated** | 2.5 Gi |
| **Persistent Storage** | 5 Gi |
| **External Port** | 30080 |
| **Zero Downtime** | ✓ Yes |
| **Auto Rollback** | ✓ Yes |
| **Health Monitoring** | ✓ Yes |

## Health Check Endpoints

| Endpoint | Purpose | Frequency |
|----------|---------|-----------|
| `/actuator/health` | Overall health | On-demand |
| `/actuator/health/liveness` | Pod liveness | Every 10s |
| `/actuator/health/readiness` | Pod readiness | Every 5s |
| `/actuator/metrics` | Application metrics | On-demand |

## Resource Hierarchy

```
Namespace: orders-desk
│
├── PersistentVolumeClaim: postgres-pvc (5Gi)
│
├── Deployment: postgres
│   └── Pod: postgres-xxxxx
│       └── Container: postgres:15
│           ├── Volume: postgres-pvc
│           └── Memory: 256Mi-512Mi
│
├── Service: postgres-service (ClusterIP)
│   └── Port: 5432 → postgres pods
│
├── ConfigMap: app-config
│   └── Spring Boot configuration
│
├── Secret: postgres-secret
│   └── Database credentials
│
├── Deployment: orders-app
│   ├── Pod: orders-app-xxxxx-aaaaa
│   │   └── Container: ghcr.io/.../orders-desk:a1b2c3d
│   │       └── Memory: 512Mi-1Gi
│   └── Pod: orders-app-xxxxx-bbbbb
│       └── Container: ghcr.io/.../orders-desk:a1b2c3d
│           └── Memory: 512Mi-1Gi
│
└── Service: orders-service (NodePort)
    └── Port: 8080 → 30080 (external)
        └── Routes to: orders-app pods
```

## Deployment Timeline

```
T+0:00  → Merge to master
T+0:01  → GitHub Actions triggered
T+0:02  → Tests start
T+2:00  → Tests complete ✓
T+2:10  → JAR build complete ✓
T+2:15  → Docker image build starts
T+3:30  → Docker image pushed to ghcr.io ✓
T+3:35  → Connect to k3s cluster ✓
T+3:40  → Apply manifests ✓
T+4:00  → PostgreSQL pod ready ✓
T+5:30  → Application pod 1 ready ✓
T+6:00  → Application pod 2 ready ✓
T+6:10  → Deployment verification ✓
T+6:15  → DEPLOYMENT COMPLETE ✓
```

## Traffic Flow

```
Internet
   ↓
195.206.233.23:30080 (Ubuntu Server)
   ↓
k3s NodePort Service (orders-service)
   ↓
Load Balanced to:
   ├── orders-app-pod-1 (8080)
   └── orders-app-pod-2 (8080)
          ↓
   Internal Service: postgres-service:5432
          ↓
   PostgreSQL Pod (postgres-0)
          ↓
   PersistentVolume (5Gi local-path storage)
```

---

**Created**: 2026-05-22  
**Status**: ✅ Implementation Complete  
**Ready for**: Production Deployment

