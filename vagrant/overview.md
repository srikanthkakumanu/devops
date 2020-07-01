
# Overview

  

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

## Box Settings
<HR></HR>

Note: To cusomize HDD size, you need to use <I><B>vagrant-disksize</B></I> Vagrant plug-in. Please be-aware that you cannot decrease disk size below 20GB.
Ex: `vagrant plugin install vagrant-disksize`

- **config.disksize.size**: To customize disk size.

        vagrant.configure('2') do |config|
            config.vm.box = 'debian/buster64'
            config.disksize.size = '50GB'
        end

## Provider Settings
<HR></HR>

Provider settings are useful to customize memory, CPUs, storage etc.

- **vb.name**: To set specific name for vagrant box.
- **vb.memory**: To set memory allocation.
- **vb.cpus**: To set no. of CPU cores/processors.

<I> Note: Please reload vagrant environment to see changes effect. </I>

## Network Settings
<HR></HR>
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

## Folder Settings
<HR></HR>

Please install Vagrant vbguest plugin to try Folder sync options.

    vagrant plugin install vagrant-vbguest

- **config.vm.synced_folder**: Syncs local machine folder with Vagrant Box folder. Here in below case, i synced local folder './apache2/data' to Vagrant Box folder (apache2 folder) '/var/www/html'. Network file system i.e. NFS improves performance of your Vagrant Box and mount_options to set permissions for directory mode and file mode.

        config.vm.synced_folder "./apache2/data", "/var/www/html" :nfs => {:mount_options => ["dmode=777", "fmode=666"]}

## Provision Settings
<HR></HR>

Vagrant provisioning offers several options to install software packages via shell and via package automation tools such as Ansible etc. Provisioners are run in three different scenarios during vagrant up, vagrant provision, vagrant reload --provision

Most basic provisioning is via shell as shown below. It updates apt package manager and installs tree command utility.

    config.vm.provision "shell", inline: <<-SHELL
        apt-get update
        apt-get install -y tree
    SHELL

Vagrant box can be provisioned using a script or by using automation tools such as Ansible. Here is the example for using Ansible for provisioning.

    config.vm.provision :ansible do |ansible| 
        ansible.playbook = "provisioning/playbook.yml"
        ansible.inventory_path = "provisioning/ansible_hosts"
    end

## Multi-Machine Environment
<HR></HR>

Instead of installing everything in one box, Vagrant can create multiple environments by creating multiple box images as shown below.

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
