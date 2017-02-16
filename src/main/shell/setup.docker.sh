#!/bin/bash
#RUN AS ROOT!
if [[ $EUID -ne 0 ]]; then
   echo "This script must be run as root" 1>&2
   exit 1
fi

echo "SETTING UP EC2 INSTANCE WITH DOCKER AND GIT"

#Update the installed packages and package cache
yum update -y

#Update the installed packages and package cache
yum upgrade -y

#Install git on the instance
yum install -y git

#Install docker on the instance
yum install -y docker

#Start docker as a service on the instance
service docker start

#start docker service at each system boot
chkconfig docker on

#allow ec2-user to execute docker commands
usermod -a -G docker ec2-user

#check to see that docker installed
docker info

#needed directories for nginx docker container deployment
mkdir /etc/nginx/certs
touch /etc/nginx/vhost.d
mkdir /usr/share/nginx/html


echo "EC2 docker setup is complete. You will be logged out and will need to log back in for changes to take effect. PRESS ENTER TO CONTINUE..."

#Wait for user to acknowledge
read keypressed

#Logout user
pkill -KILL -u ec2-user
