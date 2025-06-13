#!/bin/bash

# Exit on any error
set -e

# Function for logging
log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1"
}

# Function to get port from docker-compose
get_app_port() {
    local port=$(grep -A1 "ports:" docker-compose.yml | grep -o "[0-9]*:8080" | cut -d':' -f1)
    echo $port
}

# Function to determine docker compose command
get_docker_compose_cmd() {
    if command -v docker-compose &> /dev/null; then
        echo "docker-compose"
    else
        echo "docker compose"
    fi
}

DOCKER_COMPOSE_CMD=$(get_docker_compose_cmd)
APP_PORT=$(get_app_port)
if [ -z "$APP_PORT" ]; then
    APP_PORT=8080
    log "WARNING: Could not detect port from docker-compose.yml, using default: $APP_PORT"
fi

# Function to get test results
get_test_results() {
    local test_output=$(./mvnw test)
    echo "$test_output"
}

log "Starting test process..."

# Build the application
log "Building application..."
if ./mvnw clean package -DskipTests; then
    log "Build successful!"
else
    log "ERROR: Build failed!"
    exit 1
fi

# Start the test environment
log "Starting test environment..."
if $DOCKER_COMPOSE_CMD -f docker-compose.test.yml up -d; then
    log "Test environment started successfully!"
else
    log "ERROR: Failed to start test environment!"
    exit 1
fi

# Wait for services to be ready
log "Waiting for services to be ready..."
sleep 10

# Run the tests
log "Running tests..."
TEST_OUTPUT=$(get_test_results)
TEST_EXIT_CODE=$?

# Always clean up test environment first
log "Cleaning up test environment..."
if $DOCKER_COMPOSE_CMD -f docker-compose.test.yml down -v; then
    log "Test environment cleaned up successfully!"
else
    log "WARNING: Failed to clean up test environment completely"
fi

# Check test results
if [ $TEST_EXIT_CODE -eq 0 ]; then
    log "✅ Tests completed successfully!"
    log "Test Results:"
    echo "$TEST_OUTPUT" | grep "Tests run:"
    echo "$TEST_OUTPUT" | grep "Failures:"
    echo "$TEST_OUTPUT" | grep "Errors:"
    echo "$TEST_OUTPUT" | grep "Skipped:"
    
    # Only deploy if tests passed
    log "Deploying application..."
    if $DOCKER_COMPOSE_CMD up -d; then
        log "✅ Application deployed successfully!"
        log "Deployment Summary:"
        log "- App container: Running"
        log "- Database container: Running"
        log "- Network: Created"
        log "- Volumes: Created"
        log "You can access the application at: http://localhost:$APP_PORT"
    else
        log "❌ Deployment failed!"
        exit 1
    fi
else
    log "❌ Tests failed!"
    log "Test Results:"
    echo "$TEST_OUTPUT" | grep "Tests run:"
    echo "$TEST_OUTPUT" | grep "Failures:"
    echo "$TEST_OUTPUT" | grep "Errors:"
    echo "$TEST_OUTPUT" | grep "Skipped:"
    log "Stopping deployment due to test failures"
    exit 1
fi 