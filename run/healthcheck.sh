#!/usr/bin/env bash

url=https://krashidbuilt-api-prod.krashidbuilt.net/api/health
healthy() {

    start=$(date +%s%N | cut -b1-13)

    # curl and get status code to make sure it's good
    status=$(curl -s -o /dev/null -w "%{http_code}\n" $url)

    let ellapsed=$(date +%s%N | cut -b1-13)-$start

    if [ $status = "200" ]; then

        echo  "$(date) $ellapsed SUCCESSFUL HEALTH CHECK! $url"

        return 0; #return true
    else
        message="$(date) $ellapsed HEALTH CHECK FAILED! STATUS CODE $status $url"

        echo  $message
        echo $message | mail -s "krashidbuilt api health check failed" ben@krashidbuilt.com

        return 1; #return false
    fi
}

 while healthy
 do
    sleep 60s
 done


