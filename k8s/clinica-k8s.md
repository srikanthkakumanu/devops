# Clinica Microservice Kubernetes Deployment Guide

## Overview

The Clinica microservice is a secure Spring Boot application for managing clinics. It provides a RESTful API for CRUD operations on clinic records with features including:

- **CRUD Operations**: Create, read, update, and delete clinic records
- **Pagination**: Support for paginated API responses
- **Doctor Integration**: Association with doctors via external doctor microservice
- **HATEOAS Support**: Hypermedia links for API discoverability
- **Input Validation**: Comprehensive validation with meaningful error messages
- **Security**: HTTP Basic Authentication with OWASP best practices
- **API Documentation**: Swagger UI for interactive API exploration

### Technology Stack

- **Language**: Java 21
- **Framework**: Spring Boot 3.4.4
- **Database**: H2 (in-memory) for development/testing
- **Build Tool**: Gradle 9.3.1
- **Container**: Docker
- **Orchestration**: Kubernetes

## Prerequisites

Before deploying the Clinica microservice, ensure you have:

1. **Kubernetes Cluster**: A running Kubernetes cluster (e.g., Minikube, EKS, GKE)
2. **kubectl**: Configured to access your cluster
3. **Docker Image**: The Clinica Docker image built and available (e.g., `srikanthkakumanu/clinica:latest`)
4. **Ingress Controller**: NGINX Ingress Controller installed in your cluster
5. **Doctor Service**: The external doctor microservice deployed and accessible (optional for basic functionality)

## Step-by-Step Deployment Instructions

### 1. Clone and Build the Application (if needed)

```bash
git clone https://github.com/srikanthkakumanu/clinica.git
cd clinica
./gradlew build
docker build -t srikanthkakumanu/clinica:latest .
docker push srikanthkakumanu/clinica:latest
```

### 2. Deploy to Kubernetes

Apply the Kubernetes manifests in order:

```bash
# Navigate to the deployment directory
cd /Users/skakumanu/practice/devops/k8s/apps/clinica

# Apply ConfigMap
kubectl apply -f configmap.yaml

# Apply Deployment
kubectl apply -f deployment.yaml

# Apply Service
kubectl apply -f service.yaml

# Apply Ingress (optional, configure host first)
kubectl apply -f ingress.yaml
```

### 3. Verify Deployment

```bash
# Check pod status
kubectl get pods -l app=clinica

# Check service
kubectl get svc clinica-service

# Check ingress
kubectl get ingress clinica-ingress

# View logs
kubectl logs -l app=clinica
```

### 4. Access the Application

- **Internal Cluster**: `http://clinica-service:9091`
- **Via Ingress**: `http://clinica.example.com` (configure DNS or hosts file)
- **Swagger UI**: `http://clinica.example.com/swagger-ui.html`
- **H2 Console**: `http://clinica.example.com/h2` (if exposed)

## Important kubectl Commands

### Monitoring and Debugging

```bash
# Get pod details
kubectl describe pod <pod-name>

# View real-time logs
kubectl logs -f <pod-name>

# Execute commands in pod
kubectl exec -it <pod-name> -- /bin/bash

# Check resource usage
kubectl top pods

# Get events
kubectl get events --sort-by=.metadata.creationTimestamp
```

### Scaling and Updates

```bash
# Scale deployment
kubectl scale deployment clinica-deployment --replicas=3

# Update image
kubectl set image deployment/clinica-deployment clinica=srikanthkakumanu/clinica:v2.0

# Rolling restart
kubectl rollout restart deployment/clinica-deployment

# Check rollout status
kubectl rollout status deployment/clinica-deployment
```

### Cleanup

```bash
# Delete all resources
kubectl delete -f .

# Delete namespace (if using custom namespace)
kubectl delete namespace <namespace>
```

## Environment-Specific Configurations

### Development Environment

```yaml
# configmap.yaml
data:
  SPRING_PROFILES_ACTIVE: 'dev'
  DOCTOR_SERVICE_URL: 'http://doctor-service-dev:9092'
  JAVA_OPTS: '-Xmx256m -Xms128m'
```

### Production Environment

```yaml
# configmap.yaml
data:
  SPRING_PROFILES_ACTIVE: 'prod'
  DOCTOR_SERVICE_URL: 'http://doctor-service-prod:9092'
  DOCTOR_SERVICE_USERNAME: 'prod-user'
  DOCTOR_SERVICE_PASSWORD: 'secure-password'
  JAVA_OPTS: '-Xmx1g -Xms512m'
```

### Database Configuration

The application currently uses H2 in-memory database. For production, consider:

1. **External Database**: Modify ConfigMap to use PostgreSQL/MySQL
2. **Persistent Volume**: Add PVC for data persistence
3. **Database Deployment**: Deploy database as separate service

Example for PostgreSQL:

```yaml
# Add to configmap.yaml
SPRING_DATASOURCE_URL: 'jdbc:postgresql://postgres-service:5432/clinica'
SPRING_DATASOURCE_USERNAME: 'clinica'
SPRING_DATASOURCE_PASSWORD: 'password'
SPRING_JPA_HIBERNATE_DDL_AUTO: 'update'
```

## Troubleshooting

### Common Issues

1. **Pods not starting**
   - Check image availability: `docker pull srikanthkakumanu/clinica:latest`
   - Verify resource limits
   - Check logs: `kubectl logs <pod-name>`

2. **Service not accessible**
   - Verify service selector matches deployment labels
   - Check firewall/security groups
   - Test internal access: `kubectl port-forward svc/clinica-service 9091:9091`

3. **Ingress not working**
   - Ensure Ingress Controller is installed
   - Check ingress class annotation
   - Verify DNS/host configuration

4. **Authentication issues**
   - Default credentials: admin/admin123 or user/user123
   - Check ConfigMap for correct profile settings

5. **Doctor service integration**
   - Ensure doctor service is deployed and accessible
   - Verify DOCTOR_SERVICE_URL in ConfigMap
   - Check network policies for cross-service communication

### Health Checks

The application includes health endpoints:

- **Liveness**: `/actuator/health` - Container restart if fails
- **Readiness**: `/actuator/health` - Traffic routing if fails
- **Startup**: `/actuator/health` - Initial startup check

### Logs and Monitoring

```bash
# Enable debug logging
kubectl set env deployment/clinica-deployment DEBUG=true

# View application logs
kubectl logs -l app=clinica --tail=100

# Monitor resource usage
kubectl top pods -l app=clinica
```

### Performance Tuning

- **Memory**: Adjust JAVA_OPTS in ConfigMap
- **CPU**: Modify resource requests/limits in deployment.yaml
- **Replicas**: Scale based on load: `kubectl scale deployment clinica-deployment --replicas=5`

## Security Considerations

1. **Secrets Management**: Use Kubernetes Secrets for sensitive data instead of ConfigMap
2. **Network Policies**: Implement network segmentation
3. **RBAC**: Configure Role-Based Access Control
4. **TLS**: Enable HTTPS via Ingress TLS configuration
5. **Image Security**: Scan images for vulnerabilities

## Backup and Recovery

Since the application uses in-memory database, data is ephemeral. For production:

1. **Database Backup**: Implement backup strategies for external database
2. **Config Backup**: Backup ConfigMap and deployment configurations
3. **Disaster Recovery**: Document recovery procedures

## Next Steps

- Integrate with monitoring solutions (Prometheus, Grafana)
- Set up CI/CD pipelines for automated deployments
- Implement horizontal pod autoscaling
- Add service mesh (Istio) for advanced traffic management
- Configure external database for data persistence
