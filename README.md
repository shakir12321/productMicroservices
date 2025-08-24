# 🚀 Microservices E-Commerce Platform

A comprehensive e-commerce platform built with Spring Boot microservices and a Next.js frontend, featuring product management, order processing, benefit estimation, and payout services.

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    Next.js Frontend                         │
│                    (Port 3000)                              │
└─────────────────────┬───────────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────────┐
│                    Microservices                            │
├─────────────────────────────────────────────────────────────┤
│  Product Service    │  Order Service    │  Benefit Service  │
│  (Port 8081)       │  (Port 8082)      │  (Port 8083)      │
└─────────────────────┼───────────────────┼───────────────────┘
                      │                   │
┌─────────────────────▼───────────────────▼───────────────────┐
│                    Payout Service                           │
│                    (Port 8084)                              │
└─────────────────────┬───────────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────────┐
│                    Data Layer                               │
├─────────────────────────────────────────────────────────────┤
│  MySQL Database     │  Redis Cache                          │
│  (Port 3306)       │  (Port 6379)                          │
└─────────────────────────────────────────────────────────────┘
```

## 🛠️ Technology Stack

### Backend (Spring Boot Microservices)

- **Java 17** - Modern Java with LTS support
- **Spring Boot 3.2.0** - Rapid application development
- **Spring Data JPA** - Data access layer
- **Spring WebFlux** - Reactive HTTP client
- **MySQL 8.0** - Primary database
- **Redis 7** - Caching layer
- **Docker** - Containerization

### Frontend (Next.js)

- **Next.js 14** - React framework
- **TypeScript** - Type safety
- **Tailwind CSS** - Utility-first CSS
- **React Query** - Data fetching and caching
- **Axios** - HTTP client
- **Lucide React** - Icon library

## 📦 Services

### 1. Product Service (Port 8081)

- **Purpose**: Product catalog management
- **Features**:
  - CRUD operations for products
  - Stock management
  - Category-based filtering
  - Search functionality
  - Redis caching for performance

### 2. Order Service (Port 8082)

- **Purpose**: Order processing and management
- **Features**:
  - Create and manage orders
  - Order status tracking
  - Integration with Product Service for stock validation
  - Customer information management

### 3. Benefit Estimation Service (Port 8083)

- **Purpose**: Calculate customer benefits and loyalty rewards
- **Features**:
  - Loyalty points calculation
  - Cashback estimation
  - Discount calculations
  - Free shipping eligibility

### 4. Payout Service (Port 8084)

- **Purpose**: Process payouts and manage payment methods
- **Features**:
  - Payout processing
  - Multiple payment methods (Bank Transfer, Credit Card, Digital Wallet)
  - Payout status tracking
  - Integration with Benefit Estimation Service

### 5. Frontend Application (Port 3000)

- **Purpose**: User interface for all services
- **Features**:
  - Modern, responsive dashboard
  - Real-time data management
  - Interactive forms and modals
  - Service integration

## 🚀 Quick Start

### Prerequisites

- Docker and Docker Compose
- Node.js 18+ (for local frontend development)
- Java 17+ (for local backend development)

### 1. Clone the Repository

```bash
git clone <repository-url>
cd springboot
```

### 2. Start All Services

```bash
# Start all services with Docker Compose
docker compose up --build

# Or start in background
docker compose up -d --build
```

### 3. Access the Application

- **Frontend Dashboard**: http://localhost:3000
- **Product Service API**: http://localhost:8081/api/products
- **Order Service API**: http://localhost:8082/api/orders
- **Benefit Service API**: http://localhost:8083/api/benefit-estimations
- **Payout Service API**: http://localhost:8084/api/payouts
- **MySQL Database**: localhost:3306
- **Redis Cache**: localhost:6379

## 📋 API Endpoints

### Product Service

```http
GET    /api/products              # Get all products
GET    /api/products/{id}         # Get product by ID
POST   /api/products              # Create new product
PUT    /api/products/{id}         # Update product
DELETE /api/products/{id}         # Delete product
GET    /api/products/category/{category}  # Get products by category
GET    /api/products/search?name={name}   # Search products
GET    /api/products/in-stock     # Get products in stock
PATCH  /api/products/{id}/stock?quantity={qty}  # Update stock
POST   /api/products/{id}/reserve?quantity={qty} # Reserve stock
```

### Order Service

```http
GET    /api/orders                # Get all orders
GET    /api/orders/{id}           # Get order by ID
POST   /api/orders                # Create new order
PUT    /api/orders/{id}           # Update order
DELETE /api/orders/{id}           # Delete order
GET    /api/orders/status/{status} # Get orders by status
```

### Benefit Estimation Service

```http
GET    /api/benefit-estimations                    # Get all estimations
GET    /api/benefit-estimations/{id}               # Get estimation by ID
POST   /api/benefit-estimations                    # Create new estimation
GET    /api/benefit-estimations/order/{orderId}    # Get estimations by order
GET    /api/benefit-estimations/status/{status}    # Get estimations by status
```

### Payout Service

```http
GET    /api/payouts                               # Get all payouts
GET    /api/payouts/{id}                          # Get payout by ID
POST   /api/payouts                               # Create new payout
GET    /api/payouts/status/{status}               # Get payouts by status
GET    /api/payouts/estimation/{estimationId}     # Get payouts by estimation
```

## 🗄️ Database Schema

### MySQL Database (`microservices_db`)

- **products** - Product catalog
- **orders** - Order information
- **order_items** - Order line items
- **benefit_estimations** - Benefit calculations
- **payouts** - Payout records

### Redis Cache

- **Product caching** - 30-minute TTL
- **Session data** - Temporary storage
- **Rate limiting** - API protection

## 🔧 Configuration

### Environment Variables

```bash
# Database
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/microservices_db
SPRING_DATASOURCE_USERNAME=microservices_user
SPRING_DATASOURCE_PASSWORD=microservices_pass

# Redis
SPRING_REDIS_HOST=redis
SPRING_REDIS_PORT=6379

# Service URLs
PRODUCT_SERVICE_URL=http://product-service:8081/api/products
ORDER_SERVICE_URL=http://order-service:8082/api/orders
BENEFIT_ESTIMATION_SERVICE_URL=http://benefit-estimation-service:8083/api/benefit-estimations
PAYOUT_SERVICE_URL=http://payout-service:8084/api/payouts
```

## 🧪 Testing

### API Testing with curl

```bash
# Test Product Service
curl -X GET http://localhost:8081/api/products
curl -X POST http://localhost:8081/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"iPhone 15","price":999.99,"stockQuantity":50,"category":"Electronics"}'

# Test Order Service
curl -X GET http://localhost:8082/api/orders
curl -X POST http://localhost:8082/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerName":"John Doe","customerEmail":"john@example.com","items":[],"totalAmount":0,"status":"PENDING"}'
```

### Frontend Testing

1. Open http://localhost:3000
2. Navigate through different tabs
3. Test CRUD operations for each service
4. Verify real-time updates

## 📊 Monitoring and Logs

### View Service Logs

```bash
# View all service logs
docker compose logs

# View specific service logs
docker compose logs product-service
docker compose logs frontend

# Follow logs in real-time
docker compose logs -f
```

### Health Checks

```bash
# Check service health
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
curl http://localhost:8084/actuator/health
```

## 🚀 Deployment

### Production Deployment

1. Update environment variables for production
2. Configure external databases
3. Set up reverse proxy (nginx)
4. Configure SSL certificates
5. Set up monitoring and logging

### Scaling

```bash
# Scale specific services
docker compose up --scale product-service=3
docker compose up --scale order-service=2
```

## 🔒 Security Considerations

- **Input Validation**: All inputs are validated using Bean Validation
- **CORS Configuration**: Configured for frontend integration
- **Database Security**: Separate user accounts for each service
- **Redis Security**: Network isolation within Docker network
- **API Security**: Consider adding authentication/authorization

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📝 License

This project is licensed under the MIT License.

## 🆘 Troubleshooting

### Common Issues

1. **Port Already in Use**

   ```bash
   # Check what's using the port
   lsof -i :8081
   # Kill the process or change port in docker-compose.yml
   ```

2. **Database Connection Issues**

   ```bash
   # Check if MySQL is running
   docker compose ps mysql
   # Check MySQL logs
   docker compose logs mysql
   ```

3. **Frontend Not Loading**

   ```bash
   # Check if frontend is built correctly
   docker compose logs frontend
   # Rebuild frontend
   docker compose build frontend
   ```

4. **Service Communication Issues**
   ```bash
   # Check network connectivity
   docker network ls
   docker network inspect springboot_microservices-network
   ```

### Performance Optimization

1. **Enable Redis Caching**: Already configured
2. **Database Indexing**: Add indexes for frequently queried fields
3. **Connection Pooling**: Configured in application properties
4. **Load Balancing**: Use nginx for multiple service instances

---

**Happy Coding! 🎉**
