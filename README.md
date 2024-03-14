# DevOps

This project demonstrates and explain about Vagrant, Ansible, Docker, Kubernetes, Terraform etc. and its usage with examples.

1. [Vagrant](docs/Vagrant.md)
2. [Ansible](docs/Ansible.md)
3. [Docker](docs/docker.md)
4. [Kubernetes](docs/kubernetes.md)


# Important commands

- To start Vagrant image, go to vagrant folder and type: `vagrant up`
- To suspend Vagrant image: `vagrant suspend`
- To install Jenkins server and dev server (using ansible): `ansible-playbook -i provisioning/clusters/dev/hosts dev-site.yml`
- To know list of docker containers running in a box/machine: `docker ps`
- To Login to a particular docker container which is running, use the below command: `docker exec -it <container name> /bin/bash` and e.g. `docker exec -it jenkins-server cat /var/jenkins_home/secrets/initialAdminPassword`
- To start and stop docker container manually: `docker stop container-name` and `docker start container-name`

# Setup Information

Sandbox or Development environment contains two vagrant boxes:

- tool box
  - all common CLI tools
  - open JDK
  - Git
  - Ansible
  - jenkins docker image (http://192.168.10.111:8001)
  - nexus docker image (http://192.168.10.111:8002)
  - Sonarqube server (http://192.168.10.111:8003) - check sonar.properties

- dev box
  - all common CLI tools
  - open JDK
  - microservice app(s)

