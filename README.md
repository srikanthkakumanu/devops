# DevOps
This project demonstrates and explain about Vagrant, Ansible, Docker, Kubernetes, Terraform etc. and its usage with examples.

# 1. Vagrant

Vagrant is a tool for **building** and **managing** virtual machine environments in a single workflow. Vagrant lowers development environment setup time and increases production parity.

Vagrant consists **five** main parts:

- **config.vm.box**: Operating system provision and hard disk size customization.

- **config.vm.provider**: virtual environment provider i.e. VirtualBox etc.

- **config.vm.network**: How your host computer (local) sees your box. Here is where you define IP address and ports etc.

- **config.vm.synced_folder**: How you access VM files from your host (local) computer. You can sync your host computer (local) folder with vagrant box folder

- **config.vm.provision**: What we want to setup i.e. installing software packages

Upon installing Vagrant on your local environment, you can create your first vagrant box environment using following command. It creates a **Vagrantfile** in your working directory.

   `vagrant init debian/buster64`

To manage(add/remove/update/list) Boxes to VirtualBox: `vagrant box add debian/buster64 --provider=virtualbox`

To start Vagrant environment:   `vagrant up`

To shutdown Vagrant environment:   `vagrant halt`

To save state and suspends Vagrant environment:   `vagrant suspend`

To resume Vagrant environment:      `vagrant resume`

To reload Vagrant environment:      `vagrant reload`

To provision running Vagrant environment:   `vagrant provision`

To destroy whole Vagrant environment:   `vagrant destroy`

To SSH login to Vagrant environment:    `vagrant ssh`

## 1.1 Box Settings

Note: To cusomize HDD size, you need to use <I><B>vagrant-disksize</B></I> Vagrant plug-in. Please be-aware that you cannot decrease disk size below 20GB.
Ex: `vagrant plugin install vagrant-disksize`

- **config.disksize.size**: To customize disk size.
```
        vagrant.configure('2') do |config|
            config.vm.box = 'debian/buster64'
            config.disksize.size = '50GB'
        end
```

## 1.2 Provider Settings

Provider settings are useful to customize memory, CPUs, storage etc.

- **vb.name**: To set specific name for vagrant box.
- **vb.memory**: To set memory allocation.
- **vb.cpus**: To set no. of CPU cores/processors.

<I> Note: Please reload vagrant environment to see changes effect. </I>

## 1.3 Network Settings

<i>
Note: Install apache2 on your vagrant box and experiment with config.vm.network and config.vm.network options alternatively. Then access apache2 index page. (localhost:8080 or 192.168.111.222)

To assign a local domain name to IP address, you need to modify /etc/hosts file in your host environment (local) and add private network IP to it as shown below.

In your local environment /etc/hosts file:

192.168.111.222 devops.local www.devops.local

Now you can access index.html by typing http://devops.local or www.devops.local URLs.
</i>

- **config.vm.network**: opens forwarded port for your vagrant box web server (If you have installed a web server already). <i><b>guest</b></i> is your vagrant box port and <i><b>host</b></i> is your local machine port.

    Example:

        config.vm.network "forwarded_port", guest: 80, host: 8080

- **config.vm.network**: To create a private network and assign a local IP which does not work outside of your local network.

        config.vm.network "private_network", ip: "192.168.111.222"

## 1.4 Folder Settings

Please install Vagrant vbguest plugin to try Folder sync options.

    `vagrant plugin install vagrant-vbguest`

- **config.vm.synced_folder**: Syncs local machine folder with Vagrant Box folder. Here in below case, i synced local folder './apache2/data' to Vagrant Box folder (apache2 folder) '/var/www/html'. Network file system i.e. NFS improves performance of your Vagrant Box and mount_options to set permissions for directory mode and file mode.

        `config.vm.synced_folder "./apache2/data", "/var/www/html" :nfs => {:mount_options => ["dmode=777", "fmode=666"]}`

## 1.5 Provision Settings

Vagrant provisioning offers several options to install software packages via shell and via package automation tools such as Ansible etc. Provisioners are run in three different scenarios during vagrant up, vagrant provision, vagrant reload --provision

Most basic provisioning is via shell as shown below. It updates apt package manager and installs tree command utility.

```
    config.vm.provision "shell", inline: <<-SHELL
        apt-get update
        apt-get install -y tree
    SHELL
```

Vagrant box can be provisioned using a script or by using automation tools such as Ansible. Here is the example for using Ansible for provisioning.

```
    config.vm.provision :ansible do |ansible|
        ansible.playbook = "provisioning/playbook.yml"
        ansible.inventory_path = "provisioning/ansible_hosts"
    end
```

## 1.6 Multi-Machine Environment

Instead of installing everything in one box, Vagrant can create multiple environments by creating multiple box images as shown below.

```
    config.vm.define "loadbalancer" do |loadbalancer|
        loadbalancer.vm.box = "debian/buster64"
        loadbalancer.vm.hostname = "loadbalancer"
        config.vm.network "private_network", ip: "192.168.10.111"

        config.vm.provider "virtualbox" do |vb|
            vb.linked_clone = true
            vb.memory = 1024
            vb.cpus = 2
        end
    end    

    config.vm.define "webserver" do |webserver|
        webserver.vm.box = "debian/buster64"
        webserver.vm.hostname = "webserver"
        config.vm.network "private_network", ip: "192.168.10.112"

        config.vm.provider "virtualbox" do |vb|
            vb.linked_clone = true
            vb.memory = 1024
            vb.cpus = 2
        end
    end
```

# 2. Ansible

Ansible is an automation engine/tool and platform enabling **Infrastructure as Code** (IaC)) and is agent-less.

It automates the following:

- **cloud provisioning**
- **configuration management**
- **application deployment**
- **intra-service orchestration**

Ansible scripts i.e. play book are written in YAML and inventory of all hosts i.e. managed nodes are defined in *hosts* file. Inventory file i.e. hosts follows INI style configurations.

*Note: Ansible is installed in controller VM image using Vagrant and SSH login in configured with other VM environments.*

## 2.1 Basic commands & Usage

To execute shell commands in all nodes:

`ansible -m ping all`

`ansible -i hosts -m ping all`

`ansible lb -i hosts -u vagrant -m setup`

`ansible -i hosts -m setup all`

`ssh -i /vagrant/insecure_private_key 192.168.10.12`

`ansible webservers -m command -a "/sbin/reboot -t now"`

`ansible lb -i hosts -m shell -a "cat /etc/passwd"`

- **-i**: Indicates inventory i.e. hosts file where all host addresses of nodes are declared.
- **-u**: User name to connect with node.
- **-m**: Module i.e. shell command to execute in node.
- **-a**: Arguments to be passed to a module.

To execute Ansible playbook:

`ansible-playbook -i hosts basic_playbook.yml`
`ansible-playbook -i devops/ansible/hosts devops/ansible/site.yml`

To create a role using ansible-galaxy:

`ansible-galaxy init nginx .`

## 2.2 Playbook

Playbooks are where we define Ansible's configuration, deployment with YAML convention. In short it is Ansible's scripting language. It consists one or more plays which map groups of hosts to well defined tasks. They can describe a policy you want your remote systems to enforce, or a set of steps in a general IT process. It tells Ansible what to execute. Ansible playbook contains list of ordered tasks i.e. steps which the user wants to execute on a particular machine and they run sequentially.

## 2.3 Role

A role enables the sharing and reuse of Ansible tasks. It contains Ansible playbook tasks, plus all the supporting files, variables, templates, and handlers needed to run the tasks. A role is a complete unit of automation that can be reused and shared.

Role is a set of tasks and additional files to configure host to serve for a certain role. It is a way of automatically loading certain vars_files, tasks and handles based on a known file structure.

Roles provide a framework for fully independent, or interdependent collections of variables, tasks, files, templates, and modules.

In Ansible, the role is the primary mechanism for breaking a playbook into multiple files. This simplifies writing complex playbooks, and it makes them easier to reuse.



## 2.4 Tasks

A task is an action to be performed on a host or hosts. Plays map hosts to tasks.

Tasks are **sequence of actions** performed against a group of hosts that match the pattern specified in a play. Each play typically contains multiple tasks that are run serially on each machine that matches the pattern.

Each action can use Ansible modules.

Example:

    tasks:
    - group:
        name: devops
        state: present

    - name: create devops user with admin privileges
      user:
        name: devops
        comment: "DevOps User"
        uid: 2001
        group: devops

    - name: install htop package
      action: apt name=htop state=present update_cache=yes

## 2.5 Module

Modules (also referred to as task plugins or library plugins) are discrete units of code that can be used from the command line or in a playbook task.

Ansible executes each module, usually on the remote target node, and collects return values.

All modules are idempotent except Command and Shell. Both neither take key-value pairs as parameters, nor are idempotent.

From command line:

`ansible webservers -m command -a "/sbin/reboot -t now"`

From Playbook:

```
        - name: reboot the servers
          action: command /sbin/reboot -t now
```

or

```
        - name: reboot the servers
          command: /sbin/reboot -t now
```

or

```
        - name: restart webserver
          service:
            name: httpd
            state: restarted
```

## 2.6 Important commands

- To start Vagrant image, go to vagrant folder and type: `vagrant up`
- To suspend Vagrant image: `vagrant suspend`
- To install Jenkins server and dev server (using ansible): `ansible-playbook -i provisioning/clusters/dev/hosts dev-site.yml`
- To know list of docker containers running in a box/machine: `docker ps`
- To Login to a particular docker container which is running, use the below command: `docker exec -it <container name> /bin/bash` and e.g. `docker exec -it jenkins-server cat /var/jenkins_home/secrets/initialAdminPassword`
