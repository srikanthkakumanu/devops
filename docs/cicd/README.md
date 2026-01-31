# CI/CD Pipeline

This directory contains a comprehensive CI/CD pipeline implementation for the DevOps environment, focusing on automated testing, security scanning, containerization, and deployment to AWS EKS.

## Overview

The CI/CD pipeline is designed to provide:

- **Automated Testing**: Unit, integration, acceptance, load, and performance testing
- **Code Quality**: SonarQube analysis with quality gates
- **Security Scanning**: Trivy vulnerability scanning and OWASP dependency checks
- **Containerization**: Multi-stage Docker builds with DockerHub integration
- **Infrastructure as Code**: Terraform-managed AWS EKS clusters
- **Progressive Deployment**: Staging to production deployment strategy

## Pipeline Architecture

### GitHub Actions Workflows

Located in `.github/workflows/`:

#### CI Pipeline (`ci.yml`)

- **Triggers**: Push/PR to main/develop branches
- **Stages**:
  1. Build & Test (Gradle, JUnit, JaCoCo)
  2. Integration Testing (PostgreSQL service)
  3. Acceptance Testing (RestAssured)
  4. Code Quality (SonarQube)
  5. Security Scanning (Trivy, OWASP)
  6. Container Testing (Docker health checks)

#### CD Pipeline (`cd.yml`)

- **Triggers**: Successful CI completion
- **Stages**:
  1. Build & Push (DockerHub)
  2. Deploy to Staging (EKS)
  3. Smoke & Performance Tests
  4. Deploy to Production (EKS)

### Testing Strategy

#### Unit Tests

- Framework: JUnit 5
- Coverage: JaCoCo (target: 80%)
- Location: `src/test/java/`

#### Integration Tests

- Database: PostgreSQL (GitHub Actions service)
- Location: `src/integrationTest/java/`

#### Acceptance Tests

- Framework: RestAssured
- API Contract Testing
- Location: `src/acceptanceTest/java/`

#### Load Tests

- Tool: Apache JMeter
- Configuration: `load-tests/clinica-load-test.jmx`
- Scenario: 50 concurrent users, 10 loops

#### Performance Tests

- Tool: Apache JMeter
- Configuration: `performance-tests/clinica-performance-test.jmx`
- Scenario: 20 concurrent users, 100 loops, <5s response time

### Quality Gates

#### SonarQube

- Project Key: `clinica-microservice`
- Quality Gate: Must pass before deployment
- Coverage Threshold: 80%
- Code Smell Priority: Blocker/Critical

#### Security Scanning

- **Trivy**: Container vulnerability scanning
- **OWASP Dependency Check**: Known vulnerabilities in dependencies
- **License Compliance**: Automated license checking

### Container Strategy

#### Multi-stage Dockerfile

```dockerfile
# Build stage
FROM openjdk:21-jdk-slim as build
# ... build commands ...

# Runtime stage
FROM openjdk:21-jre-slim
# ... runtime configuration ...
```

#### DockerHub Integration

- Registry: `docker.io`
- Repository: `srikanthkakumanu/clinica`
- Tagging Strategy: SHA, branch, latest

### Infrastructure

#### Terraform Configuration

Located in `terraform/`:

- **VPC**: Multi-AZ setup with public/private subnets
- **EKS Clusters**: Separate staging and production clusters
- **IAM**: Least-privilege access policies

#### Kubernetes Manifests

Located in `k8s/`:

- **Staging**: 2 replicas, basic resources
- **Production**: 3 replicas, production resources
- **ConfigMaps**: Environment-specific configurations

### Deployment Strategy

#### Progressive Deployment

1. **Staging**: Automated deployment after CI success
2. **Testing**: Smoke tests and performance validation
3. **Production**: Manual approval required
4. **Rollback**: Automated rollback on failures

#### Environment Configuration

- **Staging**: `clinica-staging-cluster`
- **Production**: `clinica-prod-cluster`
- **Namespaces**: Default (can be customized)

## Prerequisites

### Required Secrets (GitHub Repository)

```bash
# SonarQube
SONAR_TOKEN
SONAR_HOST_URL

# DockerHub
DOCKERHUB_USERNAME
DOCKERHUB_TOKEN

# AWS
AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY
```

### Required Tools

- Docker 20.10+
- kubectl 1.24+
- Terraform 1.0+
- JMeter 5.6+ (for local testing)
- Java 21
- Gradle 7.0+

## Usage

### Local Development

#### Run Tests Locally

```bash
# Unit tests
./gradlew test

# Integration tests
./gradlew integrationTest

# Acceptance tests
./gradlew acceptanceTest

# All tests
./gradlew build
```

#### Load Testing

```bash
# Install JMeter
brew install jmeter

# Run load tests
jmeter -n -t load-tests/clinica-load-test.jmx -l results.jtl
```

#### Performance Testing

```bash
jmeter -n -t performance-tests/clinica-performance-test.jmx -l perf-results.jtl
```

### Infrastructure Setup

#### Initialize Terraform

```bash
cd terraform
terraform init
terraform plan
terraform apply
```

#### Configure kubectl

```bash
# Staging
aws eks update-kubeconfig --name clinica-staging-cluster --region us-east-1

# Production
aws eks update-kubeconfig --name clinica-prod-cluster --region us-east-1
```

### Manual Deployment

#### Build and Push Docker Image

```bash
# Build
docker build -t clinica:latest .

# Tag for DockerHub
docker tag clinica:latest srikanthkakumanu/clinica:latest

# Push
docker push srikanthkakumanu/clinica:latest
```

#### Deploy to Kubernetes

```bash
# Staging
kubectl apply -f k8s/staging/

# Production
kubectl apply -f k8s/production/
```

## Monitoring & Troubleshooting

### Pipeline Monitoring

- **GitHub Actions**: Real-time logs and status
- **SonarQube**: Code quality dashboard
- **DockerHub**: Build and security scan results
- **AWS CloudWatch**: Infrastructure logs

### Common Issues

#### Pipeline Failures

- **SonarQube Quality Gate**: Check code coverage and code smells
- **Security Scans**: Review Trivy/OWASP reports
- **Docker Build**: Check build logs for dependency issues
- **EKS Deployment**: Verify AWS credentials and cluster access

#### Test Failures

- **Unit Tests**: Check test logs and assertions
- **Integration Tests**: Verify database connectivity
- **Acceptance Tests**: Check API endpoints and responses
- **Load/Performance**: Review JMeter results and thresholds

### Logs and Debugging

#### Application Logs

```bash
# Staging
kubectl logs -f deployment/clinica-staging

# Production
kubectl logs -f deployment/clinica-production
```

#### Pipeline Logs

- Available in GitHub Actions → Actions tab
- Filter by workflow (CI/CD)
- Download artifacts for detailed reports

## Interactive Animation

For a dynamic visualization of the CI/CD pipeline flow, open the [pipeline-animation.html](pipeline-animation.html) file in your web browser. The interactive animation highlights each step sequentially, providing a clear view of the complete pipeline execution.

## Best Practices

### Code Quality

- Maintain >80% test coverage
- Fix all critical/blocker SonarQube issues
- Regular dependency updates
- Security-first approach

### Infrastructure

- Use Infrastructure as Code (Terraform)
- Implement least-privilege IAM policies
- Regular security audits
- Multi-AZ deployment for high availability

### Deployment

- Progressive deployment strategy
- Automated rollback capabilities
- Comprehensive testing before production
- Monitoring and alerting setup

## Contributing

1. Follow GitFlow branching strategy
2. Ensure all tests pass locally
3. Update documentation for infrastructure changes
4. Test pipeline changes in feature branches
5. Get code review before merging to main

## Related Documentation

- [Infrastructure Setup](../infrastructure/)
- [Containerization Guide](../containerization/)
- [Kubernetes Orchestration](../orchestration/)
- [Application Deployment](../applications/)
