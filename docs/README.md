# DevOps Documentation

This directory contains comprehensive documentation for the DevOps environment, covering all aspects from infrastructure setup to application deployment.

## 📚 Documentation Structure

### 🏗️ Infrastructure

Foundational infrastructure and automation tools.

- **[Vagrant](./infrastructure/Vagrant.md)** - Virtual machine environment management
- **[Ansible](./infrastructure/Ansible.md)** - Configuration management and automation

### 🐳 Containerization

Container technologies and orchestration.

- **[Docker](./containerization/docker.md)** - Container runtime, images, and Docker Compose

### 🎯 Orchestration

Container orchestration and cluster management.

- **[Kubernetes](./orchestration/kubernetes.md)** - Container orchestration platform

### 🔄 CI/CD Pipeline

Continuous Integration and Continuous Deployment.

- **[CI/CD Overview](./cicd/README.md)** - Complete pipeline documentation
- **[Pipeline Animation](./cicd/pipeline-animation.html)** - Interactive pipeline visualization

### 📱 Applications

Microservices and application deployments.

- **[Clinica Microservice](./applications/README.md)** - Spring Boot application guide

### 🛠️ Tools & Utilities

Development and operational tools.

- **[Database Tools](./tools/README.md)** - PostgreSQL, pgAdmin
- **[Development Tools](./tools/README.md)** - Jenkins, Nexus, SonarQube

## 🚀 Quick Navigation

| Category             | Primary Tools    | Key Documentation                                 |
| -------------------- | ---------------- | ------------------------------------------------- |
| **Infrastructure**   | Vagrant, Ansible | [Vagrant Guide](./infrastructure/Vagrant.md)      |
| **Containerization** | Docker, Compose  | [Docker Guide](./containerization/docker.md)      |
| **Orchestration**    | Kubernetes, EKS  | [Kubernetes Guide](./orchestration/kubernetes.md) |
| **CI/CD**            | GitHub Actions   | [Pipeline Guide](./cicd/README.md)                |
| **Applications**     | Spring Boot      | [Clinica Service](./applications/README.md)       |
| **Tools**            | Jenkins, Nexus   | [Tools Overview](./tools/README.md)               |

## 📖 Reading Order

For new users, we recommend reading in this order:

1. **[Infrastructure Setup](./infrastructure/)** - Understand the foundation
2. **[Containerization](./containerization/)** - Learn container basics
3. **[Orchestration](./orchestration/)** - Master Kubernetes
4. **[CI/CD Pipeline](./cicd/)** - Implement automation
5. **[Applications](./applications/)** - Deploy microservices
6. **[Tools](./tools/)** - Use development utilities

## 🔍 Search & Navigation

- Use the table of contents in each document for quick navigation
- All documents include cross-references to related sections
- Code examples are syntax-highlighted and executable
- Diagrams and animations provide visual learning aids

## 📝 Contributing to Documentation

When updating documentation:

1. Maintain the existing structure and formatting
2. Update cross-references when adding new sections
3. Include practical examples and commands
4. Test all links and code examples
5. Update the main README.md for significant changes

## 📞 Support

- Check the [troubleshooting sections](./cicd/README.md#troubleshooting) in each guide
- Review the [common issues](./tools/README.md#troubleshooting) documentation
- Use the [interactive pipeline animation](./cicd/pipeline-animation.html) for visual guidance

---

**[⬅️ Back to Main README](../README.md)** | **[CI/CD Pipeline Animation](./cicd/pipeline-animation.html)**
