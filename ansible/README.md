
# Overview

Ansible is an automation engine/tool and platform enabling **Infrastructure as Code** (IaC)) and is agent-less.

It automates the following:

- **cloud provisioning**
- **configuration management**
- **application deployment**
- **intra-service orchestration**

Ansible scripts i.e. play book are written in YAML and inventory of all hosts i.e. managed nodes are defined in *hosts* file. Inventory file i.e. hosts follows INI style configurations.

*Note: Ansible is installed in controller VM image using Vagrant and SSH login in configured with other VM environments.*

# Basic commands & Usage

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

To create a role using ansible-galaxy:

`ansible-galaxy init nginx .`

# Playbook

Playbooks are where we define Ansible's configuration, deployment with YAML convention. In short it is Ansible's scripting language. It consists one or more plays which map groups of hosts to well defined tasks. They can describe a policy you want your remote systems to enforce, or a set of steps in a general IT process. It tells Ansible what to execute. Ansible playbook contains list of ordered tasks i.e. steps which the user wants to execute on a particular machine and they run sequentially.

# Role

A role enables the sharing and reuse of Ansible tasks. It contains Ansible playbook tasks, plus all the supporting files, variables, templates, and handlers needed to run the tasks. A role is a complete unit of automation that can be reused and shared.

Role is a set of tasks and additional files to configure host to serve for a certain role. It is a way of automatically loading certain vars_files, tasks and handles based on a known file structure. 

Roles provide a framework for fully independent, or interdependent collections of variables, tasks, files, templates, and modules. 

In Ansible, the role is the primary mechanism for breaking a playbook into multiple files. This simplifies writing complex playbooks, and it makes them easier to reuse.



# Tasks

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

# Module

Modules (also referred to as task plugins or library plugins) are discrete units of code that can be used from the command line or in a playbook task.

Ansible executes each module, usually on the remote target node, and collects return values.

All modules are idempotent except Command and Shell. Both neither take key-value pairs as parameters, nor are idempotent.

From command line:

`ansible webservers -m command -a "/sbin/reboot -t now"`

From Playbook:

        - name: reboot the servers
          action: command /sbin/reboot -t now

or

        - name: reboot the servers
          command: /sbin/reboot -t now

or

        - name: restart webserver
          service:
            name: httpd
            state: restarted
