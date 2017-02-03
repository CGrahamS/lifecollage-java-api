#!/bin/bash

#exit the script if any errors occur
set -e

for i in $(seq 1 1 1000)
do
    # curl and get status code to make sure it's good
    status=$(curl -s -o /dev/null -w "%{http_code}\n" http://localhost:8080/api/health)

    if [ $status = "200" ]; then
       echo SUCCESSFUL HEALTCHECK!
    else
       echo HEALTCHECK FAILED! STATUS CODE $status
       exit 1
    fi
    sleep 0.25s
done


echo ALL DONE DEPLOYING EACH ENV