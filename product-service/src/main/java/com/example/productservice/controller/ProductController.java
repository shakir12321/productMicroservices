package com.example.productservice.controller;

import com.example.productservice.model.Product;
import com.example.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
@Tag(name = "Product Management", description = "APIs for managing products in the e-commerce platform")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve a list of all products in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved products",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved product"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Product> getProductById(
            @Parameter(description = "ID of the product to retrieve", required = true)
            @PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new product", description = "Create a new product in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Product> createProduct(
            @Parameter(description = "Product object to create", required = true)
            @Valid @RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product", description = "Update an existing product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "ID of the product to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated product object", required = true)
            @Valid @RequestBody Product productDetails) {
        Optional<Product> updatedProduct = productService.updateProduct(id, productDetails);
        return updatedProduct.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Delete a product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID of the product to delete", required = true)
            @PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get products by category", description = "Retrieve all products in a specific category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved products by category"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Product>> getProductsByCategory(
            @Parameter(description = "Category name to filter by", required = true)
            @PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    @Operation(summary = "Search products by name", description = "Search for products by name (case-insensitive)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved matching products"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Product>> searchProducts(
            @Parameter(description = "Product name to search for", required = true)
            @RequestParam String name) {
        List<Product> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/in-stock")
    @Operation(summary = "Get products in stock", description = "Retrieve all products that have stock quantity greater than 0")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved products in stock"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Product>> getProductsInStock() {
        List<Product> products = productService.getProductsInStock();
        return ResponseEntity.ok(products);
    }

    @PatchMapping("/{id}/stock")
    @Operation(summary = "Update product stock", description = "Update the stock quantity of a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock updated successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid quantity"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> updateStock(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable Long id,
            @Parameter(description = "New stock quantity", required = true)
            @RequestParam Integer quantity) {
        boolean updated = productService.updateStockQuantity(id, quantity);
        return updated ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/reserve")
    @Operation(summary = "Reserve product stock", description = "Reserve a specific quantity of stock for a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock reserved successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "400", description = "Insufficient stock"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> reserveStock(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable Long id,
            @Parameter(description = "Quantity to reserve", required = true)
            @RequestParam Integer quantity) {
        boolean reserved = productService.reserveStock(id, quantity);
        return ResponseEntity.ok(reserved);
    }
}
