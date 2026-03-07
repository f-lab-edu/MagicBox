docker-compose -f docker-compose.local.yml --env-file env/infra.local.env down
docker ps -aq | xargs -r docker rm -f
docker-compose -f docker-compose.local.yml --env-file env/infra.local.env up -d