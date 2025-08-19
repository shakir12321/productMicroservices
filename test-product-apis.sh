#!/bin/bash

# Product Service API Test Script
# Make sure the Product Service is running on http://localhost:8081

BASE_URL="http://localhost:8081/api/products"

echo "🧪 Testing Product Service CRUD APIs"
echo "====================================="
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✅ $2${NC}"
    else
        echo -e "${RED}❌ $2${NC}"
    fi
}

# Test 1: Create Product 1
echo -e "${BLUE}1. Creating Product 1 (iPhone 15)...${NC}"
PRODUCT1_RESPONSE=$(curl -s -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 15",
    "description": "Latest iPhone model with advanced features",
    "price": 999.99,
    "stockQuantity": 50,
    "category": "Electronics"
  }')

PRODUCT1_ID=$(echo $PRODUCT1_RESPONSE | grep -o '"id":[0-9]*' | cut -d':' -f2)
print_status $? "Product 1 created with ID: $PRODUCT1_ID"
echo "Response: $PRODUCT1_RESPONSE"
echo ""

# Test 2: Create Product 2
echo -e "${BLUE}2. Creating Product 2 (MacBook Pro)...${NC}"
PRODUCT2_RESPONSE=$(curl -s -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{
    "name": "MacBook Pro",
    "description": "Professional laptop for developers",
    "price": 1999.99,
    "stockQuantity": 25,
    "category": "Electronics"
  }')

PRODUCT2_ID=$(echo $PRODUCT2_RESPONSE | grep -o '"id":[0-9]*' | cut -d':' -f2)
print_status $? "Product 2 created with ID: $PRODUCT2_ID"
echo "Response: $PRODUCT2_RESPONSE"
echo ""

# Test 3: Create Product 3
echo -e "${BLUE}3. Creating Product 3 (Coffee Mug)...${NC}"
PRODUCT3_RESPONSE=$(curl -s -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Coffee Mug",
    "description": "Ceramic coffee mug for daily use",
    "price": 15.99,
    "stockQuantity": 100,
    "category": "Home & Kitchen"
  }')

PRODUCT3_ID=$(echo $PRODUCT3_RESPONSE | grep -o '"id":[0-9]*' | cut -d':' -f2)
print_status $? "Product 3 created with ID: $PRODUCT3_ID"
echo "Response: $PRODUCT3_RESPONSE"
echo ""

# Test 4: Get All Products
echo -e "${BLUE}4. Getting all products...${NC}"
ALL_PRODUCTS=$(curl -s -X GET $BASE_URL)
print_status $? "Retrieved all products"
echo "Response: $ALL_PRODUCTS"
echo ""

# Test 5: Get Product by ID
echo -e "${BLUE}5. Getting Product 1 by ID...${NC}"
PRODUCT_BY_ID=$(curl -s -X GET "$BASE_URL/$PRODUCT1_ID")
print_status $? "Retrieved Product 1"
echo "Response: $PRODUCT_BY_ID"
echo ""

# Test 6: Update Product
echo -e "${BLUE}6. Updating Product 1...${NC}"
UPDATED_PRODUCT=$(curl -s -X PUT "$BASE_URL/$PRODUCT1_ID" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 15 Pro",
    "description": "Updated iPhone 15 Pro with enhanced features",
    "price": 1099.99,
    "stockQuantity": 45,
    "category": "Electronics"
  }')
print_status $? "Updated Product 1"
echo "Response: $UPDATED_PRODUCT"
echo ""

# Test 7: Get Products by Category
echo -e "${BLUE}7. Getting products by category (Electronics)...${NC}"
ELECTRONICS_PRODUCTS=$(curl -s -X GET "$BASE_URL/category/Electronics")
print_status $? "Retrieved Electronics products"
echo "Response: $ELECTRONICS_PRODUCTS"
echo ""

# Test 8: Search Products by Name
echo -e "${BLUE}8. Searching products by name (iPhone)...${NC}"
SEARCH_RESULTS=$(curl -s -X GET "$BASE_URL/search?name=iPhone")
print_status $? "Searched for iPhone products"
echo "Response: $SEARCH_RESULTS"
echo ""

# Test 9: Get Products in Stock
echo -e "${BLUE}9. Getting products in stock...${NC}"
IN_STOCK_PRODUCTS=$(curl -s -X GET "$BASE_URL/in-stock")
print_status $? "Retrieved products in stock"
echo "Response: $IN_STOCK_PRODUCTS"
echo ""

# Test 10: Update Stock Quantity
echo -e "${BLUE}10. Updating stock quantity for Product 1...${NC}"
STOCK_UPDATE=$(curl -s -X PATCH "$BASE_URL/$PRODUCT1_ID/stock?quantity=40")
print_status $? "Updated stock quantity"
echo "Response: $STOCK_UPDATE"
echo ""

# Test 11: Reserve Stock
echo -e "${BLUE}11. Reserving stock for Product 1 (5 units)...${NC}"
RESERVE_STOCK=$(curl -s -X POST "$BASE_URL/$PRODUCT1_ID/reserve?quantity=5")
print_status $? "Reserved stock"
echo "Response: $RESERVE_STOCK"
echo ""

# Test 12: Get Updated Product
echo -e "${BLUE}12. Getting updated Product 1...${NC}"
UPDATED_PRODUCT_AGAIN=$(curl -s -X GET "$BASE_URL/$PRODUCT1_ID")
print_status $? "Retrieved updated Product 1"
echo "Response: $UPDATED_PRODUCT_AGAIN"
echo ""

# Test 13: Try to reserve more stock than available
echo -e "${BLUE}13. Trying to reserve more stock than available (100 units)...${NC}"
EXCESS_RESERVE=$(curl -s -X POST "$BASE_URL/$PRODUCT1_ID/reserve?quantity=100")
print_status $? "Attempted to reserve excess stock"
echo "Response: $EXCESS_RESERVE"
echo ""

# Test 14: Delete Product 3
echo -e "${BLUE}14. Deleting Product 3...${NC}"
DELETE_RESPONSE=$(curl -s -X DELETE "$BASE_URL/$PRODUCT3_ID")
print_status $? "Deleted Product 3"
echo "Response: $DELETE_RESPONSE"
echo ""

# Test 15: Verify Product 3 is deleted
echo -e "${BLUE}15. Verifying Product 3 is deleted...${NC}"
VERIFY_DELETE=$(curl -s -X GET "$BASE_URL/$PRODUCT3_ID")
print_status $? "Verified Product 3 deletion"
echo "Response: $VERIFY_DELETE"
echo ""

# Test 16: Get final list of products
echo -e "${BLUE}16. Getting final list of products...${NC}"
FINAL_PRODUCTS=$(curl -s -X GET $BASE_URL)
print_status $? "Retrieved final product list"
echo "Response: $FINAL_PRODUCTS"
echo ""

echo -e "${GREEN}🎉 Product Service API Testing Complete!${NC}"
echo ""
echo -e "${YELLOW}📊 Test Summary:${NC}"
echo "- Created 3 products"
echo "- Retrieved products by various criteria"
echo "- Updated product information"
echo "- Managed stock quantities"
echo "- Deleted a product"
echo ""
echo -e "${YELLOW}🔗 H2 Console: http://localhost:8081/h2-console${NC}"
echo "- JDBC URL: jdbc:h2:mem:productdb"
echo "- Username: sa"
echo "- Password: password"
