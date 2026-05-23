# Orders Desk Documentation

Welcome to the Orders Desk project documentation.

---

## 📁 Documentation Structure

### 🚀 Kubernetes & CI/CD Documentation
**Location**: [`kubernetes/`](kubernetes/)

Complete CI/CD pipeline implementation for deploying Orders Desk to k3s cluster.

**Quick Start**: [`kubernetes/START-HERE.md`](kubernetes/START-HERE.md)

**Contents**:
- Full deployment guide with step-by-step commands
- Build versioning and Docker registry setup
- Kubernetes manifests and configuration
- Troubleshooting and maintenance guides
- Visual pipeline flow diagrams
- Quick reference for daily operations

[**→ Browse Kubernetes Documentation**](kubernetes/)

---

## 📚 Other Documentation

### Project Documentation
- [`../AGENTS.md`](../AGENTS.md) - AI Agent development guide
- [`../API-GUIDE-FOR-UI.md`](../API-GUIDE-FOR-UI.md) - Frontend API integration guide
- [`../design.md`](../design.md) - Project design documentation
- [`../project.md`](../project.md) - Project overview

### Implementation Guides
- [`../IMPLEMENTATION-COMPLETE.md`](../IMPLEMENTATION-COMPLETE.md) - Feature implementation status
- [`../IMPLEMENTATION-CHECKLIST.md`](../IMPLEMENTATION-CHECKLIST.md) - Development checklist
- [`../PAGINATION-IMPLEMENTATION.md`](../PAGINATION-IMPLEMENTATION.md) - Pagination feature guide
- [`../PRICE-API-DOCUMENTATION.md`](../PRICE-API-DOCUMENTATION.md) - Price API documentation

### Setup & Configuration
- [`../ENV-SETUP.md`](../ENV-SETUP.md) - Environment setup guide
- [`../TESTCONTAINERS-SETUP.md`](../TESTCONTAINERS-SETUP.md) - Testing configuration
- [`../GITHUB-ACTIONS-FIX-SUMMARY.md`](../GITHUB-ACTIONS-FIX-SUMMARY.md) - GitHub Actions setup

---

## 🎯 Quick Links by Role

### For DevOps Engineers
- **Deploy to k3s**: [`kubernetes/START-HERE.md`](kubernetes/START-HERE.md)
- **Pipeline troubleshooting**: [`kubernetes/K3S-QUICK-REFERENCE.md`](kubernetes/K3S-QUICK-REFERENCE.md)
- **Complete setup guide**: [`kubernetes/CI-CD-K3S-DEPLOYMENT-GUIDE.md`](kubernetes/CI-CD-K3S-DEPLOYMENT-GUIDE.md)

### For Developers
- **AI development guide**: [`../AGENTS.md`](../AGENTS.md)
- **API integration**: [`../API-GUIDE-FOR-UI.md`](../API-GUIDE-FOR-UI.md)
- **Local setup**: [`../ENV-SETUP.md`](../ENV-SETUP.md)

### For Project Managers
- **Project overview**: [`../project.md`](../project.md)
- **Implementation status**: [`../IMPLEMENTATION-COMPLETE.md`](../IMPLEMENTATION-COMPLETE.md)

---

## 📦 Project Structure

```
orders-desk/
├── docs/
│   ├── README.md (you are here)
│   └── kubernetes/          # CI/CD & Kubernetes documentation
│       ├── README.md                           # Index
│       ├── START-HERE.md                       # Quick start
│       ├── CI-CD-K3S-DEPLOYMENT-GUIDE.md       # Full guide
│       ├── CI-CD-IMPLEMENTATION-SUMMARY.md     # Overview
│       ├── PIPELINE-VISUAL-FLOW.md             # Visual diagrams
│       ├── K3S-QUICK-REFERENCE.md              # Commands
│       └── FILES-CREATED.md                    # File inventory
├── k8s/                     # Kubernetes manifests
│   └── *.yaml               # Deployment configurations
├── .github/
│   └── workflows/
│       └── deploy.yml       # CI/CD pipeline
└── src/                     # Application source code
```

---

## 🚀 Getting Started

### New to the Project?
1. Read [`../project.md`](../project.md) for project overview
2. Follow [`../ENV-SETUP.md`](../ENV-SETUP.md) for local development
3. Review [`../AGENTS.md`](../AGENTS.md) for development patterns

### Need to Deploy?
1. Start with [`kubernetes/START-HERE.md`](kubernetes/START-HERE.md)
2. Follow the 3-step quick start guide
3. Access your deployed application

### Looking for API Docs?
1. Visit [`../API-GUIDE-FOR-UI.md`](../API-GUIDE-FOR-UI.md)
2. Or access Swagger UI: `http://localhost:8080/swagger-ui.html` (when running locally)

---

## 🔧 Technology Stack

- **Backend**: Spring Boot 3.3.4, Java 17
- **Database**: PostgreSQL 15
- **ORM**: JPA/Hibernate with Liquibase migrations
- **Caching**: Caffeine
- **Mapping**: MapStruct
- **Testing**: JUnit, Testcontainers
- **CI/CD**: GitHub Actions
- **Container**: Docker
- **Orchestration**: Kubernetes (k3s)
- **Monitoring**: Spring Boot Actuator

---

## 📞 Documentation Maintenance

**Last Updated**: 2026-05-22  
**Documentation Version**: 1.0

For documentation updates or issues, please create a GitHub issue.

---

**Quick Start Deployment**: [`kubernetes/START-HERE.md`](kubernetes/START-HERE.md) 🚀

