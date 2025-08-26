# AWS ECR Deployment Guide

## Overview

This guide explains how to deploy the microservices application using Docker images stored in AWS ECR (Elastic Container Registry).

## ECR Repositories

All microservices and the frontend have been pushed to AWS ECR with the following repositories:

| Service                    | ECR Repository                       | Image URI                                                                                |
| -------------------------- | ------------------------------------ | ---------------------------------------------------------------------------------------- |
| Product Service            | `silbury/product-service`            | `897722700835.dkr.ecr.us-east-2.amazonaws.com/silbury/product-service:latest`            |
| Order Service              | `silbury/order-service`              | `897722700835.dkr.ecr.us-east-2.amazonaws.com/silbury/order-service:latest`              |
| Benefit Estimation Service | `silbury/benefit-estimation-service` | `897722700835.dkr.ecr.us-east-2.amazonaws.com/silbury/benefit-estimation-service:latest` |
| Payout Service             | `silbury/payout-service`             | `897722700835.dkr.ecr.us-east-2.amazonaws.com/silbury/payout-service:latest`             |
| Frontend                   | `silbury/frontend`                   | `897722700835.dkr.ecr.us-east-2.amazonaws.com/silbury/frontend:latest`                   |

## Prerequisites

1. **AWS CLI** installed and configured
2. **Docker** installed and running
3. **Docker Compose** installed
4. **AWS ECR Access** - Your AWS account should have access to the ECR repositories

## Authentication

Before pulling images from ECR, authenticate your Docker client:

```bash
aws ecr get-login-password --region us-east-2 | docker login --username AWS --password-stdin 897722700835.dkr.ecr.us-east-2.amazonaws.com
```

## Deployment Options

### Option 1: Using Docker Compose (Recommended)

1. **Use the ECR-compatible docker-compose file:**

   ```bash
   docker compose -f docker-compose-ecr.yml up -d
   ```

2. **Access the application:**
   - Frontend: http://localhost:3000
   - Product Service API: http://localhost:8081/api/products
   - Order Service API: http://localhost:8082/api/orders
   - Benefit Estimation API: http://localhost:8083/api/benefit-estimations
   - Payout Service API: http://localhost:8084/api/payouts

### Option 2: Manual Docker Run

You can also run each service individually:

```bash
# Pull and run Product Service
docker pull 897722700835.dkr.ecr.us-east-2.amazonaws.com/silbury/product-service:latest
docker run -d --name product-service -p 8081:8081 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/microservices_db \
  -e SPRING_DATASOURCE_USERNAME=microservices_user \
  -e SPRING_DATASOURCE_PASSWORD=microservices_pass \
  -e SPRING_REDIS_HOST=redis \
  -e SPRING_REDIS_PORT=6379 \
  897722700835.dkr.ecr.us-east-2.amazonaws.com/silbury/product-service:latest

# Pull and run Order Service
docker pull 897722700835.dkr.ecr.us-east-2.amazonaws.com/silbury/order-service:latest
docker run -d --name order-service -p 8082:8082 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e PRODUCT_SERVICE_URL=http://product-service:8081/api/products \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/microservices_db \
  -e SPRING_DATASOURCE_USERNAME=microservices_user \
  -e SPRING_DATASOURCE_PASSWORD=microservices_pass \
  -e SPRING_REDIS_HOST=redis \
  -e SPRING_REDIS_PORT=6379 \
  897722700835.dkr.ecr.us-east-2.amazonaws.com/silbury/order-service:latest

# Pull and run Benefit Estimation Service
docker pull 897722700835.dkr.ecr.us-east-2.amazonaws.com/silbury/benefit-estimation-service:latest
docker run -d --name benefit-estimation-service -p 8083:8083 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e ORDER_SERVICE_URL=http://order-service:8082/api/orders \
  -e PRODUCT_SERVICE_URL=http://product-service:8081/api/products \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/microservices_db \
  -e SPRING_DATASOURCE_USERNAME=microservices_user \
  -e SPRING_DATASOURCE_PASSWORD=microservices_pass \
  -e SPRING_REDIS_HOST=redis \
  -e SPRING_REDIS_PORT=6379 \
  897722700835.dkr.ecr.us-east-2.amazonaws.com/silbury/benefit-estimation-service:latest

# Pull and run Payout Service
docker pull 897722700835.dkr.ecr.us-east-2.amazonaws.com/silbury/payout-service:latest
docker run -d --name payout-service -p 8084:8084 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e BENEFIT_ESTIMATION_SERVICE_URL=http://benefit-estimation-service:8083/api/benefit-estimations \
  -e ORDER_SERVICE_URL=http://order-service:8082/api/orders \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/microservices_db \
  -e SPRING_DATASOURCE_USERNAME=microservices_user \
  -e SPRING_DATASOURCE_PASSWORD=microservices_pass \
  -e SPRING_REDIS_HOST=redis \
  -e SPRING_REDIS_PORT=6379 \
  897722700835.dkr.ecr.us-east-2.amazonaws.com/silbury/payout-service:latest

# Pull and run Frontend
docker pull 897722700835.dkr.ecr.us-east-2.amazonaws.com/silbury/frontend:latest
docker run -d --name frontend-app -p 3000:80 \
  897722700835.dkr.ecr.us-east-2.amazonaws.com/silbury/frontend:latest
```

## Database Setup

Make sure to run MySQL and Redis containers before starting the microservices:

```bash
# Run MySQL
docker run -d --name mysql-db -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=rootpassword \
  -e MYSQL_DATABASE=microservices_db \
  -e MYSQL_USER=microservices_user \
  -e MYSQL_PASSWORD=microservices_pass \
  mysql:8.0

# Run Redis
docker run -d --name redis-cache -p 6379:6379 redis:7-alpine
```

## Swagger Documentation

Each service includes Swagger/OpenAPI documentation:

- **Product Service Swagger:** http://localhost:8081/swagger-ui/index.html
- **Order Service Swagger:** http://localhost:8082/swagger-ui/index.html
- **Benefit Estimation Swagger:** http://localhost:8083/swagger-ui/index.html
- **Payout Service Swagger:** http://localhost:8084/swagger-ui/index.html

## Troubleshooting

### Common Issues

1. **Authentication Error:**

   ```bash
   # Re-authenticate with ECR
   aws ecr get-login-password --region us-east-2 | docker login --username AWS --password-stdin 897722700835.dkr.ecr.us-east-2.amazonaws.com
   ```

2. **Image Pull Error:**

   ```bash
   # Check if the image exists
   aws ecr describe-images --repository-name silbury/product-service --region us-east-2
   ```

3. **Service Communication Issues:**
   - Ensure all services are running
   - Check network connectivity between containers
   - Verify environment variables are correctly set

### Logs and Monitoring

```bash
# View service logs
docker logs product-service
docker logs order-service
docker logs benefit-estimation-service
docker logs payout-service
docker logs frontend-app

# Check service status
docker ps
```

## Scaling and Production Deployment

For production deployment, consider:

1. **Load Balancers:** Use AWS ALB or NLB for traffic distribution
2. **Auto Scaling:** Configure ECS or EKS for automatic scaling
3. **Monitoring:** Set up CloudWatch for monitoring and alerting
4. **Security:** Use IAM roles and security groups
5. **Backup:** Implement database backup strategies

## Cost Optimization

- Use ECR lifecycle policies to clean up old images
- Consider using ECR Public for public images
- Monitor ECR storage costs
- Use appropriate instance types for your workload

## Support

For issues or questions:

1. Check the service logs
2. Verify ECR authentication
3. Ensure all dependencies are running
4. Review the application configuration



