package com.reztech.reservation_http_api.repository;

import com.reztech.reservation_http_api.model.entity.Business;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Business entity
 */
@Repository
public interface BusinessRepository extends MongoRepository<Business, String> {
    
    /**
     * Find businesses by owner ID
     * @param ownerId Owner ID
     * @return List of businesses
     */
    @Query("{'owner.id': ?0}")
    List<Business> findByOwnerId(String ownerId);
    
    /**
     * Find businesses by name containing (case insensitive)
     * @param name Business name
     * @return List of businesses
     */
    List<Business> findByNameContainingIgnoreCase(String name);
} 