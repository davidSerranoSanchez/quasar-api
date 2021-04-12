#!/usr/bin/env bash

docker stop quasar-api
docker stop quasar-postgresql

./mvnw clean install -P docker

docker-compose down -v
docker rmi quasar-api:latest
docker-compose up -d
