# Backing Service

A Spring Boot microservice that provides backing services including:

- **Database**: CockroachDB (PostgreSQL-compatible distributed database)
- **Cache**: Redis for high-performance caching
- **Message Broker**: RabbitMQ for asynchronous messaging

## Features

- RESTful API with OpenAPI documentation
- Database persistence with JPA/Hibernate
- Redis caching with TTL (Time To Live)
- RabbitMQ message publishing
- Health check endpoints
- Docker containerization
- Kubernetes deployment ready

## Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Backing       │    │   CockroachDB   │    │     Redis       │
│   Service       │◄──►│   (Database)    │    │    (Cache)      │
│                 │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │
         ▼
┌─────────────────┐
│   RabbitMQ      │
│ (Message Broker)│
│                 │
└─────────────────┘
```

## API Endpoints

- `POST /api/backing` - Save data (database + cache + message)
- `GET /api/backing/{key}` - Get data by key (cache first, then database)
- `GET /api/backing` - Get all data from database
- `DELETE /api/backing/{id}` - Delete data (database + cache + message)
- `DELETE /api/backing/cache` - Clear all cached data
- `GET /api/backing/health` - Health check

## Prerequisites

- Java 17+
- Maven 3.6+
- Docker
- Kubernetes cluster (Minikube)

## Dependencies

- Spring Boot 3.2.0
- Spring Data JPA
- Spring Data Redis
- Spring AMQP (RabbitMQ)
- PostgreSQL Driver (for CockroachDB)
- OpenAPI 3

## Running Locally

1. Start the required services:
   ```bash
   # Start CockroachDB
   kubectl apply -f ../k8s/cockroachdb-deployment.yaml
   
   # Start Redis
   kubectl apply -f ../k8s/redis-deployment.yaml
   
   # Start RabbitMQ
   kubectl apply -f ../k8s/rabbitmq-deployment.yaml
   ```

2. Build the application:
   ```bash
   mvn clean package
   ```

3. Run the application:
   ```bash
   java -jar target/backing-service-0.0.1-SNAPSHOT.jar
   ```

## Docker

1. Build the Docker image:
   ```bash
   docker build -t backing-service:latest .
   ```

2. Run the container:
   ```bash
   docker run -p 8084:8084 backing-service:latest
   ```

## Kubernetes Deployment

1. Deploy to Kubernetes:
   ```bash
   kubectl apply -f ../k8s/backing-service-deployment.yaml
   ```

2. Check the deployment:
   ```bash
   kubectl get pods -l app=backing-service
   kubectl get services -l app=backing-service
   ```

## Configuration

The service uses different configuration profiles:

- `application.yml` - Default configuration
- `application-docker.yml` - Docker-specific configuration

Key configuration properties:

- Database URL: `jdbc:postgresql://cockroachdb-public:26257/backing_service`
- Redis Host: `redis-service:6379`
- RabbitMQ Host: `rabbitmq-service:5672`
- Server Port: `8084`

## Monitoring

- OpenAPI UI: `http://localhost:8084/swagger-ui.html`
- Health Check: `http://localhost:8084/api/backing/health`
- RabbitMQ Management: `http://localhost:15672` (guest/guest)
- CockroachDB Admin: `http://localhost:8080`
