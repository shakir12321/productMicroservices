package com.example.backingservice.controller;

import com.example.backingservice.model.BackingData;
import com.example.backingservice.service.BackingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/backing")
@Tag(name = "Backing Service", description = "Backing Service with Cache, Database, and Message Broker")
public class BackingController {

    @Autowired
    private BackingService backingService;

    @PostMapping
    @Operation(summary = "Save data", description = "Save data to database, cache, and send message")
    public ResponseEntity<BackingData> saveData(@RequestBody BackingData data) {
        BackingData savedData = backingService.saveData(data);
        return ResponseEntity.ok(savedData);
    }

    @GetMapping("/{key}")
    @Operation(summary = "Get data by key", description = "Get data from cache or database")
    public ResponseEntity<BackingData> getDataByKey(@PathVariable String key) {
        Optional<BackingData> data = backingService.getDataByKey(key);
        return data.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all data", description = "Get all data from database")
    public ResponseEntity<List<BackingData>> getAllData() {
        List<BackingData> data = backingService.getAllData();
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete data", description = "Delete data from database and cache")
    public ResponseEntity<Void> deleteData(@PathVariable Long id) {
        backingService.deleteData(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cache")
    @Operation(summary = "Clear cache", description = "Clear all cached data")
    public ResponseEntity<Void> clearCache() {
        backingService.clearCache();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the service is healthy")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Backing Service is healthy!");
    }
}
