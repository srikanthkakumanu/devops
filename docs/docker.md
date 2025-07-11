<div style="text-align: justify">

# Docker

## 1 — Overview

Docker desktop is a GUI tool that comes with the following.

- Docker Engine
- Docker CLI client
- Docker Compose
- Docker Scout (Additional subscription required)
- Docker Build
- Docker Extensions
- Docker Content Trust
- Kubernetes
- Credential Helper

## 2 — **Docker CLI**

- `docker info` — To show the overall installation info. of docker.
- `docker version` — Installed docker version info etc.
- `docker login` — To log in to Docker Hub (container registry) and docker desktop.

### 2.1 — **Running and Stopping**

- `docker pull [imageName]` — To pull a docker image from DockerHub (container registry).
- `docker run [imageName]` — To run containers.
- `docker run -d [imageName]` — To run containers in detached mode.
- `docker start [containerName]` — To start the containers that are stopped.
- `docker stop [containerName]` — To stop the running container.
- `docker ps` — Lists running containers
- `docker ps -a` — Lists running and stopped containers.
- `docker kill [containerName]` — Kills the container but this command is rarely used.
- `docker rm [containerName]` — Removes the container.
- `docker image inspect [imageName]` — Gets image information, useful for debugging purpose.
- `docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' container_name_or_id` — To know the IP address of a running container.

### 2.2 — **Limits**

- `docker run --memory="256m" --name _somename_ nginx` — To set name and Max memory
- `docker run --cpus=".5" nginx` — Max CPU
- `docker run --publish 80:80 --name _somename_ nginx` - To pull and run an image with a specific port number, name.

### 2.3 — **Attach Shell**

- `docker run -it nginx /bin/bash` — To attach shell.
- `docker container exec -it [containerName] bash` — To attach to a running container.

### 2.4 — **Clean-up**

- `docker rm $(docker ps -a -q)` — Removes all stopped containers.
- `docker images` — Lists the images.
- `docker rmi [imageName]` — Deletes the image.
- `docker system prune -a` — Removes all images not in use by any containers. Be **cautious to use this command**.

### 2.5 — **Building Docker Images**

- `docker build -t [name:tag]` — Builds an image using a Dockerfile located in the same folder.
- `docker build -t [name:tag] -f [filename]` — Builds an image using a Dockerfile located in different folder.
- `docker tag [imageName] [name:tag]` — Tag a name to an existing image.

_tag_  is a version number.

Example: `docker run -d -p 9000:9000 --memory="256m" --cpus=".5" --name _somename_ nginx`

### 2.6 — **Docker Volumes**

We can enable file system persistence by using docker volumes.

- `docker create volume [volumeName]` — Create a volume.
- `docker volume ls` — Lists all volumes.
- `docker volume inspect [volumeName]` — Display the volume information.
- `docker volume rm [volumeName]` — Deletes a volume.
- `docker volume prune` — Deletes all volumes that are not mounted.
- Run a container with a volume — `docker run -d -p 8080:80 --memory="256m" --cpus=".5" --name webserver -v myvolume:/app nginx:latest`.
  - _myvolume:/app_ is the mapping.
  - _myvolume_ is a logical folder. Docker maps this logical folder to host system's
      location `/var/lib/docker/volumes/myvolume`. We can specify the folder path directly instead of logical folder
      name (ex:- `/opt/folder/myfolder:/app`), but it is not a best practice.
  - _/app_ docker container volume folder.
- **Cleanup:** — Before removing the volume, running container should be stopped and removed.
  - `docker stop webserver` — Stops the running container.
  - `docker rm webserver` — Removes the container.
  - `docker volume rm myvolume` — Removes the volume.
  - `docker rm myvolume` — Removes the volume.

## 3 — **Docker Compose**

- `docker compose build` — Build the images
- `docker compose start` — Start the containers
- `docker compose stop` — Stop the containers
- `docker compose up -d` — Build and start the containers
- `docker compose ps` — List what's running
- `docker compose rm` — Remove from memory
- `docker compose down` — Stop and remove
- `docker compose down --volumes` — To remove all containers, networks, and volumes for a clean slate.

- `docker compose logs` — Get the logs
- `docker compose logs -f [serviceName]` — Look at the container logs of a particular service.
  - Ex:- `docker compose logs -f web-fe`
- `docker compose exec [container] bash` — Run a command in a container

**v2 Commands**

- `docker compose --project-name test1 up -d` — Run an instance as a project (we can run multiple instances with different project names).
- `docker compose -p test2 up -d` — Shortcut version of above command.
- `docker compose ls` — List running projects
- `docker compose cp` — Copy files from container
  - [containerID]:[SRC_PATH] [DEST_PATH]
- `docker compose up` — Copy files to the container
  - [SRC_PATH] [containerID]:[DEST_PATH]
-

### 3.1 — **Resource Limits**

It is always good practice to set the **resource limits** per image like below.

```yml
services:
  redis:
    image: redis:alpine
    deploy:
      resources:
        limits: // Can use upto. This is max capacity
          cpus: '0.50' // half of RAM capacity
          memory: 150M
        reservations: // This is initial allocation
          cpus: '0.25'
          memory: 20M
```

### 3.2 — **Environment Variables**

We can set the environment variables in different ways.

- We can set the variables in compose YAML itself like below.

  ```yml
    services:
    web:
      image: nginx:alpine
      environment:
        - DEBUG=1
        - FOO=BAR
  ```

  These environment variables further can be overriden via command line like below.

  `docker compose up -d -e DEBUG=0`

- We can set these variables at OS level like below:-

  ```text
    # set an environment variable in the box/machine/OS environment
    export POSTGRES_VERSION=14.3
  ```

  and then, we can refer them like below.

  ```yml
    services:
    db:
      image: "postgres:${POSTGRES_VERSION}"
  ```

- We can also create a .env file.

  A file named .env file located in the same folder where compose YAML is present:
    `POSTGRES_VERSION=14.3`

  The docker compose command automatically read the values from .env file.

### 3.3 — **Dependence**

We can define dependence between hosts aka services like below.

  ```yml
    services:
      app:
        image: myapp
          depends_on:
            - db
      db:
        image: postgres
        networks:
          - back-tier
  ```

### 3.4 — **Named Volumes**

We can define **named volumes** in compose YML.

```yml
  services:
    app:
      image: myapp
      depends_on
        - db
    db:
      image: postgres
      volumes: // mapping to a volume definition
        - db-data: /etc/data // optionally we can append with flags such as :ro, :rw
      networks:
        - back-tier
  
  volumes: // volume definition
    db-data:
```

Without named volumes, directly we can define the volumes at service level.

```yml
  services:
    app:
      image: myapp
      depends_on
        - db
    db:
      image: postgres
      volumes: 
        - ./db: /etc/data // optionally we can append with flags such as :ro, :rw
      networks:
        - back-tier
```

### 3.5 — **Restart Policy**

It is always a good practice to set a restart policy. 

- **no** — The default restart policy is _no_, means it does not restart a container under any circumstances.
- **always** — _always_ means, it always restarts the container until its removal.
- **on-failure** — _on-failure_ means, it restarts a container if the exit code indicates an error.
- **unless-stopped** — _unless-stopped_ means, restarts a container irrespective of exit code but will stop restarting when the service is stopped or removed.

```yml
services:
  app:
    image: myapp
    restart: always
    depends_on:
      - db
  db:
    image: postgres
    restart: always
```

## 4 — **Container registry**

- Central repositories for container images
- Private or/or public
- DockerHub
  - hub.docker.com
- Microsoft
  - Azure Container Registry
  - Microsoft Container/Artifact Registry (public images)
    - mcr.microsoft.com
- Amazon Elastic Container Registry
- Google Container Registry

- `docker login -u <username> -p <password>` — Login to DockerHub.
- `docker tag my_image <username>/firstImage:latest` — tag the image previously built. By default, it is your username or org. name.
- `docker push dbapp/firstImage:latest` — Push the image
- `docker pull dbapp/firstImage:latest` — Pull the image.

## 5 **Docker Networking**

There are various types of Docker Networking that we can use via command line or via Docker compose.

Examples are: *Bridge, Host, IPvlan, Macvlan, Overlay*.

For more details, watch this YouTube video: [https://www.youtube.com/watch?v=fBRgw5dyBd4](https://www.youtube.com/watch?v=fBRgw5dyBd4)
</div>

