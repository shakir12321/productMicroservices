package com.example.backingservice.repository;

import com.example.backingservice.model.BackingData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BackingDataRepository extends JpaRepository<BackingData, Long> {
    Optional<BackingData> findByKey(String key);
}
