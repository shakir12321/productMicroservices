# Microservices Project - Product and Order Services

This project demonstrates a microservices architecture using Spring Boot with two main services: Product Service and Order Service.

## Architecture Overview

```
┌─────────────────┐    ┌─────────────────┐
│  Product Service│    │   Order Service │
│   (Port: 8081)  │    │   (Port: 8082)  │
└─────────────────┘    └─────────────────┘
         │                       │
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

## Service Communication

The Order Service communicates with the Product Service using:
- **WebClient**: Reactive HTTP client for non-blocking calls
- **REST APIs**: Standard HTTP endpoints
- **Stock Reservation**: Automatic stock reservation during order creation

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
