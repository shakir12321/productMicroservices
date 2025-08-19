# Microservices Project - Product and Order Services

This project demonstrates a microservices architecture using Spring Boot with two main services: Product Service and Order Service.

## Architecture Overview

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────────┐    ┌─────────────────┐
│  Product Service│    │   Order Service │    │ Benefit Estimation  │    │  Payout Service │
│   (Port: 8081)  │    │   (Port: 8082)  │    │   Service (8083)    │    │   (Port: 8084)  │
└─────────────────┘    └─────────────────┘    └─────────────────────┘    └─────────────────┘
         │                       │                       │                       │
         │                       │                       │                       │
         └───────────────────────┼───────────────────────┼───────────────────────┘
                                 │                       │
                                 └───────────────────────┘
                                                    │
                                            REST API Calls
```

## Services

### Product Service
- **Port**: 8081
- **Database**: H2 (in-memory)
- **Features**:
  - Product CRUD operations
  - Stock management
  - Product search and filtering
  - Category-based product listing

### Order Service
- **Port**: 8082
- **Database**: H2 (in-memory)
- **Features**:
  - Order creation and management
  - Integration with Product Service
  - Stock reservation during order creation
  - Order status tracking

### Benefit Estimation Service
- **Port**: 8083
- **Database**: H2 (in-memory)
- **Features**:
  - Benefit calculation based on order details
  - Multiple benefit types (cashback, loyalty points, etc.)
  - Integration with Order Service
  - Benefit estimation status tracking

### Payout Service
- **Port**: 8084
- **Database**: H2 (in-memory)
- **Features**:
  - Payout processing and management
  - Multiple payout methods (bank transfer, PayPal, etc.)
  - Integration with Benefit Estimation Service
  - Payout status tracking and transaction management

## Technology Stack

- **Spring Boot**: 3.2.0
- **Java**: 17
- **Spring Data JPA**: For database operations
- **H2 Database**: In-memory database for development
- **Spring WebFlux**: For reactive HTTP client
- **Maven**: Build tool
- **Docker**: Containerization

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Docker and Docker Compose (optional)

### Running Locally

1. **Build the Product Service**:
   ```bash
   cd product-service
   mvn clean package
   java -jar target/product-service-1.0.0.jar
   ```

2. **Build the Order Service**:
   ```bash
   cd order-service
   mvn clean package
   java -jar target/order-service-1.0.0.jar
   ```

### Running with Docker

1. **Build and run both services**:
   ```bash
   docker-compose up --build
   ```

2. **Stop the services**:
   ```bash
   docker-compose down
   ```

## API Endpoints

### Product Service (http://localhost:8081)

#### Products
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create a new product
- `PUT /api/products/{id}` - Update a product
- `DELETE /api/products/{id}` - Delete a product

#### Product Search & Filtering
- `GET /api/products/category/{category}` - Get products by category
- `GET /api/products/search?name={name}` - Search products by name
- `GET /api/products/in-stock` - Get products with stock > 0

#### Stock Management
- `PATCH /api/products/{id}/stock?quantity={quantity}` - Update stock quantity
- `POST /api/products/{id}/reserve?quantity={quantity}` - Reserve stock

### Order Service (http://localhost:8082)

#### Orders
- `GET /api/orders` - Get all orders
- `GET /api/orders/{id}` - Get order by ID
- `POST /api/orders` - Create a new order
- `PUT /api/orders/{id}/status?status={status}` - Update order status
- `DELETE /api/orders/{id}` - Delete an order

#### Order Search & Filtering
- `GET /api/orders/customer/{email}` - Get orders by customer email
- `GET /api/orders/status/{status}` - Get orders by status
- `GET /api/orders/search?customerName={name}` - Search orders by customer name

### Benefit Estimation Service (http://localhost:8083)

#### Benefit Estimations
- `GET /api/benefit-estimations` - Get all benefit estimations
- `GET /api/benefit-estimations/{id}` - Get benefit estimation by ID
- `POST /api/benefit-estimations` - Create a new benefit estimation
- `PUT /api/benefit-estimations/{id}/status?status={status}` - Update estimation status
- `DELETE /api/benefit-estimations/{id}` - Delete a benefit estimation

#### Benefit Estimation Search & Filtering
- `GET /api/benefit-estimations/customer/{customerId}` - Get estimations by customer ID
- `GET /api/benefit-estimations/order/{orderId}` - Get estimations by order ID
- `GET /api/benefit-estimations/benefit-type/{benefitType}` - Get estimations by benefit type
- `GET /api/benefit-estimations/status/{status}` - Get estimations by status

### Payout Service (http://localhost:8084)

#### Payouts
- `GET /api/payouts` - Get all payouts
- `GET /api/payouts/{id}` - Get payout by ID
- `POST /api/payouts` - Create a new payout
- `POST /api/payouts/{id}/process` - Process a payout
- `PUT /api/payouts/{id}/status?status={status}` - Update payout status
- `DELETE /api/payouts/{id}` - Delete a payout

#### Payout Search & Filtering
- `GET /api/payouts/customer/{customerId}` - Get payouts by customer ID
- `GET /api/payouts/order/{orderId}` - Get payouts by order ID
- `GET /api/payouts/benefit-estimation/{benefitEstimationId}` - Get payouts by benefit estimation ID
- `GET /api/payouts/method/{payoutMethod}` - Get payouts by payout method
- `GET /api/payouts/status/{status}` - Get payouts by status

## Database Access

### H2 Console
- **Product Service**: http://localhost:8081/h2-console
  - JDBC URL: `jdbc:h2:mem:productdb`
  - Username: `sa`
  - Password: `password`

- **Order Service**: http://localhost:8082/h2-console
  - JDBC URL: `jdbc:h2:mem:orderdb`
  - Username: `sa`
  - Password: `password`

- **Benefit Estimation Service**: http://localhost:8083/h2-console
  - JDBC URL: `jdbc:h2:mem:benefitdb`
  - Username: `sa`
  - Password: `password`

- **Payout Service**: http://localhost:8084/h2-console
  - JDBC URL: `jdbc:h2:mem:payoutdb`
  - Username: `sa`
  - Password: `password`

## Sample Data

### Creating a Product
```bash
curl -X POST http://localhost:8081/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 15",
    "description": "Latest iPhone model",
    "price": 999.99,
    "stockQuantity": 50,
    "category": "Electronics"
  }'
```

### Creating an Order
```bash
curl -X POST http://localhost:8082/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "John Doe",
    "customerEmail": "john@example.com",
    "shippingAddress": "123 Main St, City, State",
    "orderItems": [
      {
        "productId": 1,
        "quantity": 2
      }
    ]
  }'
```

### Creating a Benefit Estimation
```bash
curl -X POST http://localhost:8083/api/benefit-estimations \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": 1,
    "customerId": "john@example.com",
    "preferredBenefitType": "CASHBACK"
  }'
```

### Creating a Payout
```bash
curl -X POST http://localhost:8084/api/payouts \
  -H "Content-Type: application/json" \
  -d '{
    "benefitEstimationId": 1,
    "payoutMethod": "BANK_TRANSFER",
    "additionalDetails": "Account: 1234567890"
  }'
```

## Service Communication

### Order Service → Product Service
- **WebClient**: Reactive HTTP client for non-blocking calls
- **REST APIs**: Standard HTTP endpoints
- **Stock Reservation**: Automatic stock reservation during order creation

### Benefit Estimation Service → Order Service
- **WebClient**: Reactive HTTP client for non-blocking calls
- **Order Details**: Retrieves order information for benefit calculation
- **Benefit Calculation**: Calculates benefits based on order total and type

### Payout Service → Benefit Estimation Service
- **WebClient**: Reactive HTTP client for non-blocking calls
- **Benefit Validation**: Validates benefit estimation before payout processing
- **Amount Retrieval**: Gets payout amount from benefit estimation

## Error Handling

Both services include comprehensive error handling:
- **Validation**: Input validation using Bean Validation
- **Business Logic**: Custom business rule validation
- **HTTP Status Codes**: Appropriate status codes for different scenarios
- **Exception Handling**: Centralized exception handling

## Development

### Project Structure
```
├── product-service/
│   ├── src/main/java/com/example/productservice/
│   │   ├── controller/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   ├── src/main/resources/
│   ├── pom.xml
│   └── Dockerfile
├── order-service/
│   ├── src/main/java/com/example/orderservice/
│   │   ├── controller/
│   │   ├── model/
│   │   ├── repository/
│   │   ├── service/
│   │   ├── client/
│   │   └── dto/
│   ├── src/main/resources/
│   ├── pom.xml
│   └── Dockerfile
├── benefit-estimation-service/
│   ├── src/main/java/com/example/benefitestimationservice/
│   │   ├── controller/
│   │   ├── model/
│   │   ├── repository/
│   │   ├── service/
│   │   ├── client/
│   │   └── dto/
│   ├── src/main/resources/
│   ├── pom.xml
│   └── Dockerfile
├── payout-service/
│   ├── src/main/java/com/example/payoutservice/
│   │   ├── controller/
│   │   ├── model/
│   │   ├── repository/
│   │   ├── service/
│   │   ├── client/
│   │   └── dto/
│   ├── src/main/resources/
│   ├── pom.xml
│   └── Dockerfile
├── docker-compose.yml
└── README.md
```

### Adding New Features

1. **Product Service**: Add new endpoints in `ProductController`
2. **Order Service**: Add new endpoints in `OrderController`
3. **Service Communication**: Update `ProductServiceClient` for new product service calls
4. **Database**: Add new entities and repositories as needed

## Monitoring and Logging

- **Application Logs**: Configured with DEBUG level for development
- **H2 Console**: Web-based database management
- **Health Checks**: Basic health endpoints available

## Future Enhancements

- **Service Discovery**: Implement Eureka or Consul
- **API Gateway**: Add Zuul or Spring Cloud Gateway
- **Circuit Breaker**: Implement resilience patterns
- **Distributed Tracing**: Add Sleuth and Zipkin
- **Message Queues**: Implement async communication with RabbitMQ/Kafka
- **Security**: Add authentication and authorization
- **Testing**: Comprehensive unit and integration tests
