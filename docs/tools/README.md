# Tools & Utilities

This section documents the various tools and utilities available in the DevOps environment, including database tools, monitoring utilities, and development helpers.

## Database Tools

### PostgreSQL + pgAdmin

A complete database management solution with PostgreSQL database and pgAdmin web interface.

#### Architecture

```
┌─────────────────┐    ┌─────────────────┐
│   pgAdmin       │    │   PostgreSQL    │
│   (Port: 80)    │◄──►│   (Port: 5432)  │
│   Web UI        │    │   Database      │
└─────────────────┘    └─────────────────┘
```

#### Configuration

**docker-compose.yml**:

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: clinica_db
      POSTGRES_USER: clinica_user
      POSTGRES_PASSWORD: secure_password
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - '5432:5432'

  pgadmin:
    image: dpage/pgadmin4
    environment:
      PGADMIN_DEFAULT_EMAIL: admin@clinica.com
      PGADMIN_DEFAULT_PASSWORD: admin123
    volumes:
      - pgadmin_data:/var/lib/pgadmin
    ports:
      - '80:80'
    depends_on:
      - postgres

volumes:
  postgres_data:
  pgadmin_data:
```

#### Usage

##### Start Services

```bash
cd docker-tools
docker-compose -f postgres-pgadmin.yaml up -d
```

##### Access pgAdmin

- **URL**: http://localhost:80
- **Email**: admin@clinica.com
- **Password**: admin123

##### Connect to Database

1. Open pgAdmin in browser
2. Right-click "Servers" → "Create" → "Server"
3. **General Tab**:
   - Name: Clinica DB
4. **Connection Tab**:
   - Host: postgres (or localhost if connecting externally)
   - Port: 5432
   - Username: clinica_user
   - Password: secure_password
   - Database: clinica_db

##### Basic Operations

```sql
-- Create database
CREATE DATABASE clinica_dev;

-- Create user
CREATE USER clinica_app WITH PASSWORD 'app_password';

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE clinica_dev TO clinica_app;
```

#### Backup & Restore

##### Backup

```bash
# Using docker
docker exec postgres pg_dump -U clinica_user clinica_db > backup.sql

# Using pg_dump directly
pg_dump -h localhost -U clinica_user -d clinica_db > backup.sql
```

##### Restore

```bash
# Using docker
docker exec -i postgres psql -U clinica_user -d clinica_db < backup.sql

# Using psql directly
psql -h localhost -U clinica_user -d clinica_db < backup.sql
```

## Development Tools

### Jenkins

A continuous integration server for building, testing, and deploying applications.

#### Current Setup

- **Container**: Docker-based Jenkins
- **Port**: 8001
- **Initial Password**: Retrieved from container logs

#### Access Jenkins

```bash
# Get initial admin password
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword

# Access via browser
http://192.168.10.111:8001
```

#### Configuration

- **Plugins**: Git, Pipeline, Docker, Kubernetes
- **Credentials**: DockerHub, GitHub, AWS
- **Jobs**: Pipeline jobs for microservices

### Nexus Repository

A repository manager for storing and distributing artifacts.

#### Current Setup

- **Container**: Docker-based Nexus
- **Port**: 8002
- **Default Credentials**:
  - Username: admin
  - Password: Retrieved from logs

#### Access Nexus

```bash
# Get initial admin password
docker logs nexus | grep "Initial admin password"

# Access via browser
http://192.168.10.111:8002
```

#### Repository Types

- **Maven**: Java artifacts
- **Docker**: Container images
- **npm**: JavaScript packages
- **PyPI**: Python packages

### SonarQube

Code quality and security analysis platform.

#### Current Setup

- **Container**: Docker-based SonarQube
- **Port**: 8003
- **Database**: PostgreSQL (internal)

#### Access SonarQube

```bash
# Access via browser
http://192.168.10.111:8003

# Default credentials
# Username: admin
# Password: admin
```

#### Configuration

- **Quality Profiles**: Java, JavaScript, Python
- **Quality Gates**: Critical/blocker issues blocking
- **Integrations**: GitHub Actions, Jenkins

## Infrastructure Tools

### Terraform

Infrastructure as Code tool for provisioning AWS resources.

#### Directory Structure

```
terraform/
├── main.tf          # Main configuration
├── variables.tf     # Input variables
├── outputs.tf       # Output values
└── terraform.tfstate # State file (S3 backend)
```

#### Key Resources

- **VPC**: Multi-AZ VPC with public/private subnets
- **EKS**: Kubernetes clusters for staging/production
- **IAM**: Roles and policies for EKS access
- **Security Groups**: Network security rules

#### Usage

```bash
# Initialize
terraform init

# Plan changes
terraform plan

# Apply changes
terraform apply

# Destroy resources
terraform destroy
```

### Ansible

Configuration management and application deployment tool.

#### Directory Structure

```
provisioning/
├── ansible/
│   ├── commons.yml          # Common configurations
│   ├── dev-server.yml       # Development server setup
│   └── tools-server.yml     # Tools server setup
└── ansible/roles/
    ├── commons/             # Common system configurations
    ├── docker-debian/       # Docker installation
    ├── jenkins/             # Jenkins setup
    ├── nexus/               # Nexus setup
    └── sonarqube/           # SonarQube setup
```

#### Key Playbooks

- **commons.yml**: Base system configuration
- **dev-server.yml**: Development environment setup
- **tools-server.yml**: CI/CD tools installation

#### Usage

```bash
# Run against all hosts
ansible-playbook -i hosts site.yml

# Run against specific host
ansible-playbook -i hosts -l dev-server dev-site.yml

# Check syntax
ansible-playbook --syntax-check site.yml
```

## Monitoring & Logging

### Basic Health Checks

#### Application Health

```bash
# Check application health
curl http://localhost:9091/actuator/health

# Check database connectivity
curl http://localhost:5432
```

#### Container Health

```bash
# List running containers
docker ps

# Check container logs
docker logs <container-name>

# Monitor resource usage
docker stats
```

#### Kubernetes Health

```bash
# Check cluster status
kubectl cluster-info

# Check pod status
kubectl get pods

# Check node status
kubectl get nodes
```

## Utility Scripts

### Environment Setup

```bash
# Start complete environment
vagrant up

# Provision with Ansible
ansible-playbook -i provisioning/ansible/hosts provisioning/ansible/site.yml

# Start database tools
cd docker-tools && docker-compose up -d
```

### Cleanup Scripts

```bash
# Stop all containers
docker stop $(docker ps -aq)

# Remove all containers
docker rm $(docker ps -aq)

# Remove unused images
docker image prune -a

# Clean Vagrant
vagrant destroy -f
```

## Troubleshooting

### Common Issues

#### Container Issues

```bash
# Check container status
docker ps -a

# View container logs
docker logs <container-name>

# Restart container
docker restart <container-name>
```

#### Network Issues

```bash
# Check port availability
netstat -tlnp | grep :9091

# Test connectivity
telnet localhost 9091
```

#### Permission Issues

```bash
# Fix Docker permissions
sudo usermod -aG docker $USER

# Fix file permissions
chmod +x scripts/*.sh
```

### Log Locations

#### Application Logs

- **Jenkins**: `/var/jenkins_home/logs/`
- **Nexus**: `/opt/sonatype/sonatype-work/nexus3/log/`
- **SonarQube**: `/opt/sonarsource/data/logs/`

#### System Logs

- **Docker**: `docker logs <container>`
- **Kubernetes**: `kubectl logs <pod>`
- **Vagrant**: `vagrant ssh` then check `/var/log/`

## Related Documentation

- [Infrastructure Setup](../infrastructure/)
- [Containerization Guide](../containerization/)
- [CI/CD Pipeline](../cicd/)
- [Applications](../applications/)
