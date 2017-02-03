#!/bin/bash

#exit the script if any errors occur
set -e

echo DEPLOYING THE TEST DEV ENV CONTAINER
./shell_dev-test-docker.sh

echo DEPLOYING THE DEV ENV CONTAINER
./shell_dev-deploy-docker.sh

echo DEPLOYING THE TEST ENV CONTAINER
./shell_test-deploy-docker.sh

echo DEPLOYING THE PROD ENV CONTAINER
./shell_prod-deploy-docker.sh


echo ALL DONE DEPLOYING EACH ENV


echo CLEAN UP ALL THE DANGLING DOCKER IMAGES
docker rmi $(docker images -q -f dangling=true)