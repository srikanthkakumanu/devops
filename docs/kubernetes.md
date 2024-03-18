<div style="text-align: justify">

# Kubernetes

## 1 — Overview


### 1.1 — What is Kubernetes

**Kubernetes a.k.a. K8s (_kaytes_)** originated from Google, and is the 3rd generation project (previous internal projects are Borg and Omega).

It is donated to Cloud Native Computing Foundation (CNCF).

- K8s is the leading **container orchestration tool** and an open source system for automating deployment, scaling and management of containerized applications.
- It is designed as a **loosely coupled collection of components centered around deploying, maintaining and scaling workloads**.
- **Vendor Neutral** — Runs on all cloud providers.
- Backed by huge community ecosystem.

### 1.2 — What K8s Can and Can't do



| **Can**                                                     | **Can't**                                                                                 |
|-------------------------------------------------------------|-------------------------------------------------------------------------------------------|
| Service discovery and load balancing                        | Does not deploy source code                                                               |
| Storage Orchestration (Local or cloud based)                | Does not build an application                                                             |
| Automated rollouts and rollbacks                            | Does not provide application level services such as Message buses, databases, caches etc. |
| **Self-healing** — It can monitor the health of containers  |                                                                                           |
| Secret and configuration management                         |                                                                                           |
| Use the same API across on-premise and every cloud provider |                                                                                           |


## 2 — K8s architecture

[Reference:-](https://kubernetes.io/docs/concepts/architecture/)

<img src="https://kubernetes.io/images/docs/kubernetes-cluster-architecture.svg" alt="K8s Cluster Architecture" />

The K8s architecture broadly classified into:

- A K8s cluster
- Master Node
- Worker Nodes

Important to note that: _Please refer to above diagram_.

- A **container** runs inside a **Pod**. 
- A **pod** runs in a **Node**.
- A **node** runs in a cluster.

![Kubernetes Cluster Structure](/home/skakumanu/personal/projects/devops/docs/Kubernetes_components.png)

### 2.1 — K8s Cluster

A K8s cluster consists of a **master node** and set of **worker nodes**.

### 2.2 — K8s Master Node

A **K8s master node a.k.a. Control Plane** consists of **Kube API server, etcd, scheduler, controller manager and cloud controller manager**.

#### 2.2.1 — Kube API server 

- It exposes the K8s REST API via the implementation _kube-apiserver_. 
- It is the frontend for K8s master node a.k.a. Control Plane.
- It can scale horizontally (deploying more instances). We can run several instances of _kube-apiserver_ and balance traffic between those instances.


#### 2.2.2 — etcd

- It is an open source key-value data store. 
- It is the single source of truth at any given point of time.
- It is built on top of **Raft consensus algorithm/protocol**. 
- It is used to store K8s config data, state data and metadata.
- It is fully replicated, reliably consistent, highly available, fast, secure and simple to use.
- An etcd cluster consists of a leader and couple of follower nodes. A leader election, replication works based on consensus and quorum.

**Raft consensus algorithm** 

Raft stands for **Replicated/Reliable And Fault Tolerant**,
is an algorithm, protocol and state machine.
It is a distributed protocol designed to achieve consensus in a 
replicated state machine. There are many consensus algorithms such as
Proof Of Work (used by Bitcoin), Proof Of Stake (PoS), Paxos etc.

#### 2.2.3 — Scheduler

A _kube-scheduler_ that watches for newly created _**pods**_ with no assigned **_node_** and selects a **_node_** for them to run on.


#### 2.2.4 — Controller Manager

A _kube-controller-manager_ that manages/runs several controller processes.
There are many different types of controllers  — Node controller, Job controller, EndpointSlice controller, ServiceAccount controller etc.

#### 2.2.5 — Cloud Controller Manager

A _cloud-controller-manager_ that embeds cloud-specific control logic. 
It lets you link your cluster into your cloud provider's API.
It only runs controllers that are specific to your cloud provider.

It has cloud provider dependencies such as Node controller, Route controller, Service controller.

### 2.3 — K8s Worker Nodes

Every worker node has these node components.
These node components run on every node, 
maintaining running pods and providing the K8s runtime environment.

Each node/worker node consists of —
- Kubelet
- Kube-Proxy
- Container Runtime

Basically, A **container** runs inside a **Pod**, 
a **pod** runs in a **Node**,
a **node** runs in a cluster, 
and all applications are to be containerized.

#### 2.3.1 — Kubelet

It is an agent that runs on every node in a cluster. 
It makes sure that
containers in a _pod_ are running and healthy.

#### 2.3.2 — Kube-Proxy

- Kube-Proxy runs on every node in a cluster. 
- maintains network rules on nodes.
- It allows network communication to a Pod from inside/outside a network.

#### 2.3.3 — Container Runtime

Every node consists of a Container Runtime where Pods are present. 

There are two important standards around containers — 

- Open Container Initiative (OCI) — 
  - A specification and set of standards for container images and running containers.
  - Describes the image format, runtime and distribution.
  - It also provides an OCI complaint tool called _runc_ (low level runtime) for creating and running container processes.
  - Besides runc, there are other OCI complaint low level runtime tools such as **_crun, firecracker-containerd, gVisor_** etc.

- Container Runtime Interface (CRI) in Kubernetes —
  - It is an API specification for Container runtime Engine.
  - It allows us to run different container runtimes in Kubernetes. 

There are Container runtimes such as 
- **_Dockershim_** — was a component of Kubernetes that added the required CRI abstraction in front of Docker Engine to make Kubernetes recognize Docker Engine as CRI compatible. _It has been deprecated_.
- **_containerd_** — is from Docker, it uses _runC_ under the hood for container execution.
- _**cri-o**_ — is from RedHat, IBM etc. It is a CRI implementation that enables using any OCI compatible runtimes.

## 3 — Running Kubernetes Locally

There are several ways to run Kubernetes on developer/local machine.

- **microk8s** — 
  - MicroK8s is a fully compliant Kubernetes distribution with a smaller CPU and memory footprint. 
  - It supports multiple worker nodes.
- **Docker desktop** — 
  - Limited to 1 node.
- **minikube** — 
  - Minikube is a tool that enables us to run a local, **single-node Kubernetes cluster** on our machine. 
  - It supports multiple worker nodes. 
  - It can also run on Docker Desktop (not mandatory), Hyper-V, Virtual Box, VMWare etc.
- **Kind** — 
  - Kind stands for **Kubernetes In Docker**.
  - Only requires Docker installed and no need of another VM installation.
  - **Installs the nodes as containers**.
  - It can emulate multiple control planes and multiple worker nodes. 

### 3.1 — kubectl CLI

- kubectl is Kubernetes CLI. 
- It communicates with _**kube-apiserver**_ (Kubernetes API server).
- Connection/configuration stored locally at `${HOME}/.kube/config`.


To verify the Kubernetes Installation: `kubectl cluster-info`

## 4 — K8s Context

- A context is a group of access parameters to a K8s cluster.
- Contains a K8s cluster name, a user, and a namespace.
- The current context is the cluster that is currently the default for kubectl.
- All kubectl commands run against that cluster.


Get the current context — `kubectl config current-context`
List all context — `kubectl config get-contexts`
Set the current context — `kubectl config use-context [contextName]`
Delete a context from the config file (`${HOME}/.kube/config`) — `kubectl config delete-context [contextName]`

## 5 — Declarative Vs. Imperative

### Imperative way 

Imperative way advocates the usage of _kubectl_.

Examples:-
```commandline
kubectl run mynginx --image=nginx --port=80
kubectl create deploy mynginx --image=nginx --port=80 --replicas=3
kubectl delete deploy mynginx
kubectl create service nodeport myservice --targetPort=8080
kubectl delete pod nginx
```

Creates the deployment — `kubectl create deployment mynginx --image=nginx`
List the deployments — `kubectl get deploy`
Cleanup — `kubectl delete deployment mynginx`

### Declarative Way 

Declarative way advocates the usage of YAML.

Example:-
```yaml
apiVersion: v1
kind: Pod
metadata:
  name: myapp-pod
  labels:
    app: myapp
    type: front-end
spec:
  containers:
    - name: nginx-container
      image: nginx
```


Create an object using YAML — `kubectl create -f [YAML file]`
Creates the deployment — `kubectl create -f deployment.xml`
To Apply the existing file — `kubectl apply -f https://k8s.io/examples/controllers/nginx-deployment.yaml`
Creating new YAML manifests using kubectl — 
`kubectl create deploy mynginx --image=nginx --port=80 --replicas=3 --dry-run=client -o yaml`


</div>