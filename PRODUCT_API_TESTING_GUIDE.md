# Product Service API Testing Guide

## 🚀 Quick Start

### Option 1: Using Docker (Recommended)
```bash
# Start the Product Service
docker compose up product-service --build

# Or start all services
docker compose up --build
```

### Option 2: Local Development
```bash
# Make sure you have Java 17+ installed
java -version

# Build and run the Product Service
cd product-service
mvn clean package -DskipTests
java -jar target/product-service-1.0.0.jar
```

## 🧪 Manual API Testing

### Base URL
```
http://localhost:8081/api/products
```

## 📋 Test Scenarios

### 1. Create Products

#### Create iPhone 15
```bash
curl -X POST http://localhost:8081/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 15",
    "description": "Latest iPhone model with advanced features",
    "price": 999.99,
    "stockQuantity": 50,
    "category": "Electronics"
  }'
```

#### Create MacBook Pro
```bash
curl -X POST http://localhost:8081/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "MacBook Pro",
    "description": "Professional laptop for developers",
    "price": 1999.99,
    "stockQuantity": 25,
    "category": "Electronics"
  }'
```

#### Create Coffee Mug
```bash
curl -X POST http://localhost:8081/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Coffee Mug",
    "description": "Ceramic coffee mug for daily use",
    "price": 15.99,
    "stockQuantity": 100,
    "category": "Home & Kitchen"
  }'
```

### 2. Read Operations

#### Get All Products
```bash
curl -X GET http://localhost:8081/api/products
```

#### Get Product by ID (replace {id} with actual ID)
```bash
curl -X GET http://localhost:8081/api/products/1
```

#### Get Products by Category
```bash
curl -X GET http://localhost:8081/api/products/category/Electronics
```

#### Search Products by Name
```bash
curl -X GET "http://localhost:8081/api/products/search?name=iPhone"
```

#### Get Products in Stock
```bash
curl -X GET http://localhost:8081/api/products/in-stock
```

### 3. Update Operations

#### Update Product (replace {id} with actual ID)
```bash
curl -X PUT http://localhost:8081/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 15 Pro",
    "description": "Updated iPhone 15 Pro with enhanced features",
    "price": 1099.99,
    "stockQuantity": 45,
    "category": "Electronics"
  }'
```

#### Update Stock Quantity
```bash
curl -X PATCH "http://localhost:8081/api/products/1/stock?quantity=40"
```

### 4. Stock Management

#### Reserve Stock
```bash
curl -X POST "http://localhost:8081/api/products/1/reserve?quantity=5"
```

#### Try to Reserve More Than Available (Should Fail)
```bash
curl -X POST "http://localhost:8081/api/products/1/reserve?quantity=100"
```

### 5. Delete Operations

#### Delete Product (replace {id} with actual ID)
```bash
curl -X DELETE http://localhost:8081/api/products/3
```

#### Verify Deletion
```bash
curl -X GET http://localhost:8081/api/products/3
```

## 🎯 Expected Responses

### Successful Product Creation
```json
{
  "id": 1,
  "name": "iPhone 15",
  "description": "Latest iPhone model with advanced features",
  "price": 999.99,
  "stockQuantity": 50,
  "category": "Electronics"
}
```

### Product List
```json
[
  {
    "id": 1,
    "name": "iPhone 15",
    "description": "Latest iPhone model with advanced features",
    "price": 999.99,
    "stockQuantity": 50,
    "category": "Electronics"
  },
  {
    "id": 2,
    "name": "MacBook Pro",
    "description": "Professional laptop for developers",
    "price": 1999.99,
    "stockQuantity": 25,
    "category": "Electronics"
  }
]
```

### Stock Reservation Success
```http
HTTP/1.1 200 OK
```

### Stock Reservation Failure
```http
HTTP/1.1 400 Bad Request
```

## 🔧 Database Access

### H2 Console
- **URL**: http://localhost:8081/h2-console
- **JDBC URL**: `jdbc:h2:mem:productdb`
- **Username**: `sa`
- **Password**: `password`

## 🚨 Error Scenarios to Test

### 1. Invalid Product Data
```bash
curl -X POST http://localhost:8081/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "",
    "price": -100,
    "stockQuantity": -5
  }'
```

### 2. Non-existent Product
```bash
curl -X GET http://localhost:8081/api/products/999
```

### 3. Invalid Stock Update
```bash
curl -X PATCH "http://localhost:8081/api/products/1/stock?quantity=-10"
```

## 📊 Test Checklist

- [ ] Create multiple products
- [ ] Retrieve all products
- [ ] Get product by ID
- [ ] Update product information
- [ ] Search products by name
- [ ] Filter products by category
- [ ] Get products in stock
- [ ] Update stock quantity
- [ ] Reserve stock successfully
- [ ] Fail to reserve excess stock
- [ ] Delete a product
- [ ] Verify deletion
- [ ] Test validation errors
- [ ] Test non-existent resources

## 🎉 Success Criteria

- All CRUD operations work correctly
- Stock management functions properly
- Validation errors are handled appropriately
- Search and filtering work as expected
- Database operations are successful
- API responses are properly formatted

## 🔗 Related Services

Once Product Service is working, you can test the complete workflow:

1. **Product Service** (Port 8081) - ✅ You are here
2. **Order Service** (Port 8082) - Creates orders using products
3. **Benefit Estimation Service** (Port 8083) - Calculates benefits from orders
4. **Payout Service** (Port 8084) - Processes payouts from benefits

## 🛠️ Troubleshooting

### Common Issues

1. **Port Already in Use**
   ```bash
   # Check what's using port 8081
   lsof -i :8081
   # Kill the process if needed
   kill -9 <PID>
   ```

2. **Docker Issues**
   ```bash
   # Check Docker status
   docker --version
   docker ps
   
   # Restart Docker if needed
   # On macOS: Restart Docker Desktop
   ```

3. **Java Version Issues**
   ```bash
   # Check Java version
   java -version
   # Should be Java 17 or higher
   ```

4. **Maven Issues**
   ```bash
   # Clean and rebuild
   mvn clean package -DskipTests
   ```

### Health Check
```bash
# Check if service is running
curl -X GET http://localhost:8081/api/products

# Expected: Empty array [] or list of products
```
