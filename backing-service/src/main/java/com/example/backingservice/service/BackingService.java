package com.example.backingservice.service;

import com.example.backingservice.model.BackingData;
import com.example.backingservice.repository.BackingDataRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.Set;

@Service
public class BackingService {

    @Autowired
    private BackingDataRepository backingDataRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String CACHE_PREFIX = "backing_data:";
    private static final long CACHE_TTL = 3600; // 1 hour

    public BackingData saveData(BackingData data) {
        // Save to database
        BackingData savedData = backingDataRepository.save(data);
        
        // Cache the data
        String cacheKey = CACHE_PREFIX + savedData.getKey();
        redisTemplate.opsForValue().set(cacheKey, savedData, CACHE_TTL, TimeUnit.SECONDS);
        
        // Send message to queue
        rabbitTemplate.convertAndSend("backing-service-exchange", "backing-service-routing-key", 
            "Data saved: " + savedData.getKey());
        
        return savedData;
    }

    public Optional<BackingData> getDataByKey(String key) {
        // Try to get from cache first
        String cacheKey = CACHE_PREFIX + key;
        Object cachedData = redisTemplate.opsForValue().get(cacheKey);
        
        if (cachedData != null) {
            return Optional.of((BackingData) cachedData);
        }
        
        // If not in cache, get from database
        Optional<BackingData> data = backingDataRepository.findByKey(key);
        
        // Cache the result if found
        if (data.isPresent()) {
            redisTemplate.opsForValue().set(cacheKey, data.get(), CACHE_TTL, TimeUnit.SECONDS);
        }
        
        return data;
    }

    public List<BackingData> getAllData() {
        return backingDataRepository.findAll();
    }

    public void deleteData(Long id) {
        Optional<BackingData> data = backingDataRepository.findById(id);
        if (data.isPresent()) {
            // Remove from cache
            String cacheKey = CACHE_PREFIX + data.get().getKey();
            redisTemplate.delete(cacheKey);
            
            // Delete from database
            backingDataRepository.deleteById(id);
            
            // Send message to queue
            rabbitTemplate.convertAndSend("backing-service-exchange", "backing-service-routing-key", 
                "Data deleted: " + data.get().getKey());
        }
    }

    public void clearCache() {
        // Clear all cache entries with our prefix
        Set<String> keys = redisTemplate.keys(CACHE_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
