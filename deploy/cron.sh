#!/bin/bash
projName=krashidbuilt-java-api
echo -e "\n\n\nCrontab started at $(date)"

mkdir -p /home/ec2-user/logs/$projName
millis=$(date +%s)
cd "$(dirname "$0")"

git checkout develop
status=$(git pull)

echo DEVELOP BRANCH GIT STATUS = $status

if [[ -n $status && ! -z $status && $status != *"Already up-to-date."* && $status == *"Updating"* ]]
then
    echo CHANGES DETECTED IN DEVELOP BRANCH!
    echo RE-DEPLOYING THE DEV CONTAINER. [LOG: dev_$millis.log]
    ./deploy_dev.sh > /home/ec2-user/logs/$projName/dev_$millis.log 2>&1
else
    echo NO CHANGES DETECTED IN DEVELOP BRANCH
fi


git checkout master
status=$(git pull)

echo MASTER BRANCH GIT STATUS = $status

if [[ -n $status && ! -z $status && $status != *"Already up-to-date."* && $status == *"Updating"* ]]
then
    echo CHANGES DETECTED IN MASTER BRANCH!
    echo RE-DEPLOYING THE TEST CONTAINER. [LOG: test_$millis.log]
    ./deploy_test.sh > /home/ec2-user/logs/$projName/test_$millis.log 2>&1
else
    echo NO CHANGES DETECTED IN MASTER BRANCH
fi

################################################################################
# crontab -l
# crontab -e

# run the shell script every 10 minutes between the hours of 14:00 - 04:00  UTC...

# */10 14-23 * * * /home/ec2-user/krashidbuilt-java-api/deploy/cron.sh >> cron-krashidbuilt-java-api.log 2>&1
# 0,10 0 * * * /home/ec2-user/krashidbuilt-java-api/deploy/cron.sh >> cron-krashidbuilt-java-api.log 2>&1
# */10 1-4 * * * /home/ec2-user/krashidbuilt-java-api/deploy/cron.sh >> cron-krashidbuilt-java-api.log 2>&1
################################################################################
