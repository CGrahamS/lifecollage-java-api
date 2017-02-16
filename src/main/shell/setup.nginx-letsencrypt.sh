#!/bin/bash

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
