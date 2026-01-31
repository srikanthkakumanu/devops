# Kubernetes

This directory contains Kubernetes configuration files and resources for deploying and managing containerized applications.

## Overview

Kubernetes (K8s) is a container orchestration platform that automates deployment, scaling, and management of containerized applications. For detailed information about Kubernetes concepts and architecture, see [Kubernetes Documentation](../docs/kubernetes.md).

## Directory Structure

This directory is organized to contain:

- **Deployment manifests** - YAML files for deploying applications to Kubernetes clusters
- **Service definitions** - YAML files for exposing applications within and outside the cluster
- **Configuration files** - ConfigMaps, Secrets, and other configuration resources
- **StatefulSet definitions** - For applications requiring stable identities and persistent storage
- **Ingress rules** - For HTTP(S) routing to services

## Common Kubernetes Resources

- **Pod** - The smallest deployable unit in Kubernetes
- **Deployment** - Manages Pods and ensures desired replicas are running
- **Service** - Exposes Pods internally or externally
- **ConfigMap** - Stores configuration data
- **Secret** - Stores sensitive data like passwords and API keys
- **Ingress** - Manages HTTP(S) routing rules

## Getting Started

To apply a Kubernetes configuration file:

```bash
kubectl apply -f <filename.yaml>
```

To view resources:

```bash
kubectl get pods
kubectl get services
kubectl get deployments
```

## Related Documentation

- [Kubernetes Official Documentation](../docs/kubernetes.md)
- [Docker Guide](../docs/docker.md)
- [DevOps Overview](../README.md)
