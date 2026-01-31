# DevOps Environment & Tools

This repository provides a comprehensive DevOps environment demonstrating modern infrastructure automation, containerization, orchestration, and CI/CD practices. It includes examples and configurations for various tools and technologies used in modern software development and deployment pipelines.

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Quick Start](#quick-start)
- [Documentation](#documentation)
- [Tools & Technologies](#tools--technologies)
- [Contributing](#contributing)

## 🎯 Overview

This DevOps environment showcases:

- **Infrastructure as Code**: Vagrant, Ansible, Terraform
- **Containerization**: Docker & Docker Compose
- **Orchestration**: Kubernetes (local and cloud)
- **CI/CD Pipeline**: GitHub Actions with comprehensive testing
- **Microservices**: Spring Boot applications
- **Database Solutions**: PostgreSQL with management tools
- **Monitoring & Security**: Code quality, vulnerability scanning

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Development Environment                  │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐     │
│  │  Tool Box   │    │  Dev Box    │    │   Local     │     │
│  │             │    │             │    │   K8s       │     │
│  │ • Jenkins   │    │ • Java 21   │    │ • Minikube  │     │
│  │ • Nexus     │    │ • Git       │    │ • Kind      │     │
│  │ • SonarQube │    │ • Apps      │    │             │     │
│  └─────────────┘    └─────────────┘    └─────────────┘     │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   Cloud Environment (AWS)                   │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐     │
│  │  Staging    │    │ Production  │    │   CI/CD     │     │
│  │   EKS       │    │   EKS       │    │ Pipeline    │     │
│  │             │    │             │    │             │     │
│  │ • 2 Replicas│    │ • 3 Replicas│    │ • GitHub    │     │
│  │ • Basic Res │    │ • Full Res  │    │ • Actions   │     │
│  └─────────────┘    └─────────────┘    └─────────────┘     │
└─────────────────────────────────────────────────────────────┘
```

## 🚀 Quick Start

### Prerequisites

- **VirtualBox** or **VMware**
- **Vagrant** (2.0+)
- **Git**
- **Docker Desktop** (optional, for local development)

### Basic Setup

1. **Clone the repository**

   ```bash
   git clone https://github.com/srikanthkakumanu/devops.git
   cd devops
   ```

2. **Start Vagrant environment**

   ```bash
   vagrant up
   ```

3. **Provision with Ansible**

   ```bash
   ansible-playbook -i provisioning/ansible/hosts provisioning/ansible/dev-site.yml
   ```

4. **Access tools**
   - **Jenkins**: http://192.168.10.111:8001
   - **Nexus**: http://192.168.10.111:8002
   - **SonarQube**: http://192.168.10.111:8003

### Database Setup

```bash
cd docker-tools
docker-compose -f postgres-pgadmin.yaml up -d
```

## 📚 Documentation

### 📖 Core Documentation

| Section                 | Description                     | Link                                         |
| ----------------------- | ------------------------------- | -------------------------------------------- |
| **🏗️ Infrastructure**   | Vagrant, Ansible, Terraform     | [Infrastructure](./docs/infrastructure/)     |
| **🐳 Containerization** | Docker, Docker Compose          | [Containerization](./docs/containerization/) |
| **🎯 Orchestration**    | Kubernetes, EKS                 | [Orchestration](./docs/orchestration/)       |
| **🔄 CI/CD Pipeline**   | GitHub Actions, Testing         | [CI/CD](./docs/cicd/)                        |
| **📱 Applications**     | Microservices, Deployment       | [Applications](./docs/applications/)         |
| **🛠️ Tools**            | Database, Monitoring, Utilities | [Tools](./docs/tools/)                       |

### 📋 Detailed Guides

#### Infrastructure Setup

- [Vagrant Environment](./docs/infrastructure/Vagrant.md) - Virtual machine management
- [Ansible Automation](./docs/infrastructure/Ansible.md) - Configuration management
- [Terraform AWS](./docs/cicd/README.md#infrastructure) - Infrastructure as Code

#### Containerization

- [Docker Guide](./docs/containerization/docker.md) - Container fundamentals
- [Docker Compose](./docs/containerization/docker.md#docker-compose) - Multi-container applications

#### Orchestration

- [Kubernetes Guide](./docs/orchestration/kubernetes.md) - Container orchestration
- [EKS Deployment](./docs/cicd/README.md#deployment-strategy) - AWS Kubernetes service

#### CI/CD Pipeline

- [Pipeline Overview](./docs/cicd/README.md) - Complete CI/CD workflow
- [Testing Strategy](./docs/cicd/README.md#testing-strategy) - Comprehensive testing
- [Interactive Animation](./docs/cicd/pipeline-animation.html) - Visual pipeline flow

#### Applications

- [Clinica Microservice](./docs/applications/README.md) - Spring Boot application
- [Deployment Guide](./docs/applications/README.md#deployment-configurations) - K8s deployments

#### Tools & Utilities

- [Database Tools](./docs/tools/README.md#database-tools) - PostgreSQL + pgAdmin
- [Development Tools](./docs/tools/README.md#development-tools) - Jenkins, Nexus, SonarQube

## 🛠️ Tools & Technologies

### Infrastructure & Automation

- **Vagrant**: Development environment management
- **Ansible**: Configuration management and deployment
- **Terraform**: Infrastructure as Code for AWS

### Containerization & Orchestration

- **Docker**: Container runtime and image management
- **Docker Compose**: Multi-container application orchestration
- **Kubernetes**: Container orchestration platform
- **AWS EKS**: Managed Kubernetes service

### CI/CD & Quality

- **GitHub Actions**: CI/CD pipeline automation
- **SonarQube**: Code quality and security analysis
- **Trivy**: Container vulnerability scanning
- **OWASP Dependency Check**: Dependency vulnerability analysis

### Development Tools

- **Jenkins**: Continuous integration server
- **Nexus Repository**: Artifact repository manager
- **PostgreSQL + pgAdmin**: Database management
- **Java 21 + Spring Boot**: Application development

### Testing & Monitoring

- **JUnit 5**: Unit testing framework
- **RestAssured**: API testing library
- **JMeter**: Load and performance testing
- **JaCoCo**: Code coverage analysis

## 📊 Environment Details

### Development Environment (Vagrant)

| Component       | IP Address     | Port | Description              |
| --------------- | -------------- | ---- | ------------------------ |
| **Tool Server** | 192.168.10.111 | -    | CI/CD tools and services |
| **Dev Server**  | 192.168.10.112 | -    | Application development  |
| **Jenkins**     | 192.168.10.111 | 8001 | CI/CD automation         |
| **Nexus**       | 192.168.10.111 | 8002 | Artifact repository      |
| **SonarQube**   | 192.168.10.111 | 8003 | Code quality             |

### Cloud Environment (AWS EKS)

| Environment    | Cluster Name            | Replicas | Resources           |
| -------------- | ----------------------- | -------- | ------------------- |
| **Staging**    | clinica-staging-cluster | 2        | 512Mi RAM, 0.25 CPU |
| **Production** | clinica-prod-cluster    | 3        | 1Gi RAM, 0.5 CPU    |

## 🔧 Important Commands

### Vagrant Management

```bash
# Start environment
vagrant up

# SSH into VM
vagrant ssh

# Suspend environment
vagrant suspend

# Destroy environment
vagrant destroy
```

### Ansible Provisioning

```bash
# Provision development server
ansible-playbook -i provisioning/ansible/hosts provisioning/ansible/dev-site.yml

# Provision tools server
ansible-playbook -i provisioning/ansible/hosts provisioning/ansible/tools-server.yml
```

### Docker Operations

```bash
# List running containers
docker ps

# Access container shell
docker exec -it <container-name> /bin/bash

# View container logs
docker logs <container-name>
```

### Kubernetes Operations

```bash
# Check cluster status
kubectl cluster-info

# List pods
kubectl get pods

# View logs
kubectl logs <pod-name>
```

## 🤝 Contributing

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

### Development Guidelines

- Follow the existing code structure
- Update documentation for any changes
- Test thoroughly before submitting PR
- Use meaningful commit messages

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Spring Boot community
- Kubernetes community
- Docker community
- AWS and cloud-native communities

---

**Note**: This environment is designed for learning and demonstration purposes. For production use, additional security measures and configurations should be implemented.
