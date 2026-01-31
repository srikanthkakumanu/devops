# Applications & Microservices

This section documents the applications and microservices deployed in the DevOps environment, including their architecture, deployment configurations, and operational procedures.

## Clinica Microservice

### Overview

The Clinica microservice is a Spring Boot-based REST API that provides clinic management functionality with HATEOAS support and comprehensive security features.

### Technical Stack

- **Framework**: Spring Boot 3.4.4
- **Language**: Java 21
- **Build Tool**: Gradle
- **Database**: H2 (in-memory) / PostgreSQL (production)
- **Security**: HTTP Basic Authentication
- **API**: REST with HATEOAS
- **Health Monitoring**: Sidecar health check pattern

### Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Clinica API   │    │ Health Sidecar  │    │   Doctor API    │
│   (Port: 9091)  │◄──►│   Monitor       │    │   (External)    │
│                 │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                        │
         └────────────────────────┼─────────────────────────────┘
                                  ▼
                         ┌─────────────────┐
                         │     H2 DB       │
                         │   (In-memory)   │
                         └─────────────────┘
```

**Sidecar Health Check Pattern:**

- Dedicated monitoring container runs alongside main application
- Performs continuous health checks every 30 seconds
- Independent health assessment for enhanced reliability
- Comprehensive logging and status reporting

**Kubernetes Multi-Container Pod:**
This deployment demonstrates the **sidecar pattern** where multiple containers run in the same Kubernetes pod. The main Clinica application container and the health check sidecar container share:

- Network namespace (localhost communication)
- Volumes (shared health status)
- Lifecycle (created/destroyed together)
- Resource limits and scheduling

### API Endpoints

#### Core Resources

- `GET /clinics` - List all clinics
- `POST /clinics` - Create new clinic
- `GET /clinics/{id}` - Get clinic by ID
- `PUT /clinics/{id}` - Update clinic
- `DELETE /clinics/{id}` - Delete clinic

#### Health & Monitoring

- `GET /actuator/health` - Application health
- `GET /actuator/info` - Application info
- `GET /actuator/metrics` - Application metrics

### Configuration

#### Environment Variables

```bash
# Application
SPRING_PROFILES_ACTIVE=production
SERVER_PORT=9091

# Database (if using external)
SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/clinica
SPRING_DATASOURCE_USERNAME=clinica_user
SPRING_DATASOURCE_PASSWORD=secure_password

# External Services
DOCTOR_SERVICE_URL=http://doctor-service:8080
```

#### Security

- **Default Credentials**:
  - Username: `admin`
  - Password: `admin`
- **Production**: Use environment variables or Kubernetes secrets

### Deployment Configurations

#### Staging Environment

- **Replicas**: 2
- **Resources** (Main Container):
  - CPU: 250m - 500m
  - Memory: 512Mi - 1Gi
- **Resources** (Sidecar Container):
  - CPU: 50m - 100m
  - Memory: 32Mi - 64Mi
- **Health Checks**:
  - Readiness: `/actuator/health` (main) + sidecar status check
  - Liveness: `/actuator/health` (main) + sidecar status check
  - Initial Delay: 60s

#### Production Environment

- **Replicas**: 3
- **Resources** (Main Container):
  - CPU: 500m - 1000m
  - Memory: 1Gi - 2Gi
- **Resources** (Sidecar Container):
  - CPU: 50m - 100m
  - Memory: 32Mi - 64Mi
- **Health Checks**: Same as staging
- **Scaling**: Horizontal Pod Autoscaler (HPA)

### Kubernetes Manifests

#### Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: clinica-production
spec:
  replicas: 3
  selector:
    matchLabels:
      app: clinica
  template:
    metadata:
      labels:
        app: clinica
    spec:
      containers:
        - name: clinica
          image: srikanthkakumanu/clinica:latest
          ports:
            - containerPort: 9091
          env:
            - name: SPRING_PROFILES_ACTIVE
              value: 'production'
          resources:
            requests:
              memory: '1Gi'
              cpu: '500m'
            limits:
              memory: '2Gi'
              cpu: '1000m'
        - name: healthcheck-sidecar
          image: curlimages/curl:8.6.0
          resources:
            requests:
              memory: '32Mi'
              cpu: '50m'
            limits:
              memory: '64Mi'
              cpu: '100m'
```

#### Service

```yaml
apiVersion: v1
kind: Service
metadata:
  name: clinica-service
spec:
  selector:
    app: clinica
  ports:
    - port: 80
      targetPort: 9091
  type: LoadBalancer
```

#### ConfigMap

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: clinica-config
data:
  doctor.service.url: 'http://doctor-service.production.svc.cluster.local:8080'
  spring.profiles.active: 'production'
```

### CI/CD Integration

The Clinica microservice is fully integrated with the CI/CD pipeline:

#### Automated Testing

- **Unit Tests**: JUnit 5 with JaCoCo coverage
- **Integration Tests**: Spring Boot Test with PostgreSQL
- **Acceptance Tests**: RestAssured API testing
- **Load Tests**: JMeter with 50 concurrent users
- **Performance Tests**: JMeter with response time validation

#### Quality Gates

- **SonarQube**: Code quality analysis
- **Security**: Trivy container scanning
- **Dependencies**: OWASP vulnerability checks

#### Deployment Pipeline

1. **Build**: Gradle build with tests
2. **Container**: Multi-stage Docker build
3. **Staging**: Automated deployment and testing
4. **Production**: Manual approval deployment

### Monitoring & Observability

#### Health Checks

- **Application Health**: Spring Boot Actuator (`/actuator/health`)
- **Sidecar Health Check**: Dedicated monitoring container with curl-based checks
- **Container Health**: Docker health checks
- **Kubernetes**: Readiness and liveness probes on both main and sidecar containers

**Sidecar Health Check Features:**

- Continuous monitoring every 30 seconds
- Independent health assessment
- Comprehensive logging (`/tmp/health.log`)
- Status reporting via shared volume
- Extensible for additional checks (database, external services)

#### Metrics

- **JVM Metrics**: Micrometer integration
- **Custom Metrics**: Application-specific counters
- **Database Metrics**: Connection pool statistics

#### Logging

- **Structured Logging**: JSON format
- **Log Levels**: Configurable via environment
- **Centralized**: Integration with ELK stack

### Troubleshooting

#### Common Issues

##### Application Startup Failures

```bash
# Check pod logs
kubectl logs -f deployment/clinica-production

# Check events
kubectl describe pod <pod-name>

# Verify configuration
kubectl exec -it <pod-name> -- env
```

##### Database Connection Issues

```bash
# Test database connectivity
kubectl exec -it <pod-name> -- curl -f http://db:5432

# Check database logs
kubectl logs -f deployment/database-deployment
```

##### High Memory/CPU Usage

```bash
# Check resource usage
kubectl top pods

# Review JVM settings
kubectl exec -it <pod-name> -- java -XX:+PrintFlagsFinal | grep -i heap
```

##### API Response Issues

```bash
# Test API endpoints
kubectl exec -it <pod-name> -- curl -f http://localhost:9091/actuator/health

# Check application logs
kubectl logs -f deployment/clinica-production --tail=100
```

##### Sidecar Health Check Issues

```bash
# Check sidecar health status
kubectl exec <pod-name> -c healthcheck-sidecar -- cat /tmp/health-status

# View sidecar logs
kubectl logs -f <pod-name> -c healthcheck-sidecar

# Test main application health from sidecar
kubectl exec <pod-name> -c healthcheck-sidecar -- curl -f http://localhost:9091/actuator/health

# Check shared volume
kubectl exec <pod-name> -c healthcheck-sidecar -- ls -la /tmp/
```

### Scaling Strategies

#### Horizontal Scaling

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: clinica-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: clinica-production
  minReplicas: 3
  maxReplicas: 10
  metrics:
    - type: Resource
      resource:
        name: cpu
        target:
          type: Utilization
          averageUtilization: 70
```

#### Vertical Scaling

- Increase resource limits in deployment
- Monitor and adjust based on usage patterns
- Consider JVM heap size adjustments

### Backup & Recovery

#### Database Backups

- **Automated**: Daily backups using Kubernetes CronJobs
- **Manual**: Database dump commands
- **Storage**: AWS S3 or persistent volumes

#### Application Backups

- **Configuration**: GitOps with ArgoCD
- **Images**: Immutable Docker images
- **Rollback**: Version-based deployments

### Security Considerations

#### Container Security

- **Non-root user**: Run as non-privileged user
- **Minimal base image**: Use distroless or minimal images
- **Security scanning**: Regular Trivy scans

#### Network Security

- **Service Mesh**: Istio integration
- **Network Policies**: Kubernetes network policies
- **TLS**: End-to-end encryption

#### Access Control

- **RBAC**: Kubernetes role-based access
- **Secrets Management**: External secret management
- **API Security**: JWT tokens, rate limiting

## Future Enhancements

### Planned Features

- **Microservices Architecture**: Break down into smaller services
- **Event-Driven**: Implement event sourcing
- **API Gateway**: Kong or Traefik integration
- **Service Mesh**: Full Istio implementation
- **Observability**: Complete ELK stack integration

### Technology Upgrades

- **Java 21**: Latest LTS version
- **Spring Boot 3.x**: Latest major version
- **Kubernetes**: Latest stable version
- **Database**: PostgreSQL with connection pooling

## Related Documentation

- [CI/CD Pipeline](../cicd/)
- [Kubernetes Orchestration](../orchestration/)
- [Infrastructure Setup](../infrastructure/)
- [Containerization Guide](../containerization/)
