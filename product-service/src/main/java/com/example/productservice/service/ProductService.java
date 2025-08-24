package com.example.productservice.service;

import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;
    
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Cacheable(value = "products", key = "'all'")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    @Cacheable(value = "products", key = "#id")
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    @CacheEvict(value = "products", allEntries = true)
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
    
    @CacheEvict(value = "products", allEntries = true)
    public Optional<Product> updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(productDetails.getName());
                    existingProduct.setDescription(productDetails.getDescription());
                    existingProduct.setPrice(productDetails.getPrice());
                    existingProduct.setStockQuantity(productDetails.getStockQuantity());
                    existingProduct.setCategory(productDetails.getCategory());
                    return productRepository.save(existingProduct);
                });
    }
    
    @CacheEvict(value = "products", allEntries = true)
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }
    
    public List<Product> getProductsInStock() {
        return productRepository.findByStockQuantityGreaterThan(0);
    }
    
    public boolean updateStockQuantity(Long productId, Integer quantity) {
        return productRepository.findById(productId)
                .map(product -> {
                    product.setStockQuantity(quantity);
                    productRepository.save(product);
                    return true;
                })
                .orElse(false);
    }
    
    public boolean reserveStock(Long productId, Integer quantity) {
        return productRepository.findById(productId)
                .map(product -> {
                    if (product.getStockQuantity() >= quantity) {
                        product.setStockQuantity(product.getStockQuantity() - quantity);
                        productRepository.save(product);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }
}
