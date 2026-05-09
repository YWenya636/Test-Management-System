Write-Host "Starting MySQL container with Docker Compose..."
docker-compose -f docker-compose-test.yml up -d

Write-Host "Waiting for MySQL to be ready..."
Start-Sleep -Seconds 15

Write-Host "Running integration tests..."
mvn verify

Write-Host "Stopping MySQL container..."
docker-compose -f docker-compose-test.yml down