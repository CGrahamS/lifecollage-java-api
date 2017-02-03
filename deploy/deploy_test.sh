#!/bin/bash
cd ../
#exit the script if any errors occur
set -e

name=krashidbuilt-api-test
host=$name.krashidbuilt.net
email=ben@krashidbuilt.com

# DEPLOY PRODUCTION CONTAINER

# Build image for production API
docker build --no-cache=true -t $name --file test.Dockerfile .

# Kill and delete the current docker container
set +e
docker rm -f $(docker ps -a -q --filter name=$name)
set -e

docker run -d \
  --name $name \
  -P \
  -e "VIRTUAL_HOST=$host" \
  -e "LETSENCRYPT_HOST=$host" \
  -e "LETSENCRYPT_EMAIL=$email" \
  --restart always \
  -v /home/ec2-user/logs/$name:/usr/dev/krashidbuilt-java-api/logs \
  $name


echo waiting 30 seconds before a healthcheck is performed on the container...
sleep 30s

set +e
echo CLEAN UP ALL THE DANGLING DOCKER IMAGES
docker rmi $(docker images -q -f dangling=true)
set -e

# curl and get status code to make sure it's good
status=$(curl -s -o /dev/null -w "%{http_code}\n" https://$host/api/health)

if [ $status = "200" ]; then
   echo SUCCESSFUL HEALTCHECK!
else
   echo HEALTCHECK FAILED! STATUS CODE $status
   exit 1
fi