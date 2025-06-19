#!/bin/bash

# Rezervasyon Yönetim Sistemi - Quick Start Script
echo "🚀 Starting Reservation Management System..."

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_step() {
    echo -e "${BLUE}[STEP]${NC} $1"
}

# Check prerequisites
print_step "Checking prerequisites..."

# Check Java
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2)
    print_status "Java found: $JAVA_VERSION"
else
    print_error "Java not found! Please install Java 21."
    exit 1
fi

# Check Maven
if command -v mvn &> /dev/null; then
    MVN_VERSION=$(mvn -version | head -n 1)
    print_status "Maven found: $MVN_VERSION"
else
    print_error "Maven not found! Please install Maven 3.6+."
    exit 1
fi

# Check Docker
if command -v docker &> /dev/null; then
    DOCKER_VERSION=$(docker --version)
    print_status "Docker found: $DOCKER_VERSION"
else
    print_error "Docker not found! Please install Docker."
    exit 1
fi

# Check Docker Compose
if command -v docker-compose &> /dev/null; then
    COMPOSE_VERSION=$(docker-compose --version)
    print_status "Docker Compose found: $COMPOSE_VERSION"
else
    print_error "Docker Compose not found! Please install Docker Compose."
    exit 1
fi

# Start MongoDB
print_step "Starting MongoDB with Docker Compose..."
if docker-compose up -d; then
    print_status "MongoDB started successfully!"
    print_status "MongoDB URL: mongodb://admin:admin123@localhost:27017/reservation_db"
    print_status "Mongo Express: http://localhost:8081 (admin/admin123)"
else
    print_error "Failed to start MongoDB!"
    exit 1
fi

# Wait for MongoDB to be ready
print_step "Waiting for MongoDB to be ready..."
sleep 10

# Check if MongoDB is running
if docker ps | grep -q "reservation-mongodb"; then
    print_status "MongoDB is running!"
else
    print_error "MongoDB failed to start!"
    exit 1
fi

# Build the application
print_step "Building the application..."
if mvn clean install; then
    print_status "Application built successfully!"
else
    print_error "Failed to build application!"
    exit 1
fi

# Start the application
print_step "Starting the Spring Boot application..."
print_status "Starting application in the background..."

# Set environment variables
export SPRING_PROFILES_ACTIVE=dev

# Start the application in background
nohup mvn spring-boot:run > application.log 2>&1 &
APP_PID=$!

print_status "Application PID: $APP_PID"
print_status "Application logs: tail -f application.log"

# Wait for application to start
print_step "Waiting for application to start..."
sleep 30

# Check if application is running
if curl -s http://localhost:8080/actuator/health &> /dev/null; then
    print_status "✅ Application is running successfully!"
    echo ""
    echo "🎉 Setup completed successfully!"
    echo ""
    echo "📱 Access URLs:"
    echo "   • Application: http://localhost:8080"
    echo "   • Swagger UI: http://localhost:8080/swagger-ui.html"
    echo "   • API Docs: http://localhost:8080/api-docs"
    echo "   • Health Check: http://localhost:8080/actuator/health"
    echo ""
    echo "🗄️  Database URLs:"
    echo "   • MongoDB: mongodb://admin:admin123@localhost:27017/reservation_db"
    echo "   • Mongo Express: http://localhost:8081 (admin/admin123)"
    echo ""
    echo "🔧 Management:"
    echo "   • Stop MongoDB: docker-compose down"
    echo "   • Stop Application: kill $APP_PID"
    echo "   • View Logs: tail -f application.log"
    echo ""
    echo "📖 For detailed setup instructions, see SETUP.md"
else
    print_error "❌ Application failed to start!"
    print_error "Check application.log for details:"
    echo "tail -f application.log"
    exit 1
fi 