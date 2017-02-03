#!/bin/bash

echo Please enter the data dog API key that you would like to link.
read apikey

# Kill and delete all containers
docker rm -f $(docker ps -a -q)
# Delete all images
docker rmi -f $(docker images -q)

#https://app.datadoghq.com/account/settings#agent/docker
#datadog monitoring container
docker run -d --name dd-agent -v /var/run/docker.sock:/var/run/docker.sock:ro -v /proc/:/host/proc/:ro -v /cgroup/:/host/sys/fs/cgroup:ro -e API_KEY=$apikey datadog/docker-dd-agent:latest

# start nginx with the 3 volumes declared
docker run -d -p 80:80 -p 443:443 \
--name nginx \
-v /etc/nginx/certs:/etc/nginx/certs:ro \
-v /etc/nginx/vhost.d \
-v /usr/share/nginx/html \
-v /var/run/docker.sock:/tmp/docker.sock:ro \
jwilder/nginx-proxy


# start the letsencrypt container
docker run -d \
--name letsencrypt \
-v /etc/nginx/certs:/etc/nginx/certs:rw \
--volumes-from nginx \
-v /var/run/docker.sock:/var/run/docker.sock:ro \
jrcs/letsencrypt-nginx-proxy-companion
