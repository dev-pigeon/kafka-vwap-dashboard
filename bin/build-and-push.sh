#!/bin/bash

# Backend services
docker compose -f docker-compose.server.yml build
docker push jackyoungdev/backend-kafka:0.1
docker push jackyoungdev/kafka-backend-api:0.1

# Frontend
docker compose -f docker-compose.client.yml build
docker push jackyoungdev/kafka-dashboard-client:0.1