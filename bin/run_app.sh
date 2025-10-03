NETWORK_NAME="kafka_dashboard"

# check for network
if docker network inspect "${NETWORK_NAME}" > /dev/null 2>&1; then
    echo "Docker network '${NETWORK_NAME}' already exists."
else 
    echo "Docker network '${NETWORK_NAME}' does not exist. Creating..."
    docker network create "${NETWORK_NAME}"
fi

# compose up
docker compose -f docker-compose.client.yml up -d --build --force-recreate 
docker compose -f docker-compose.server.yml up -d --build --force-recreate 
