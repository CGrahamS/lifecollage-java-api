@echo off
setlocal
cd ..
docker build --no-cache=true -t krashidbuilt-api-dev-test --file dev.Dockerfile .
docker rm -f krashidbuilt-api-dev-test
docker run -it --name krashidbuilt-api-dev-test -P --rm krashidbuilt-api-dev-test
