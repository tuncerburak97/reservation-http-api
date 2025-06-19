package com.reztech.reservation_http_api.repository;

import com.reztech.reservation_http_api.model.entity.Owner;
import com.reztech.reservation_http_api.model.enums.OwnerType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Owner entity
 */
@Repository
public interface OwnerRepository extends MongoRepository<Owner, String> {
    
    /**
     * Find owner by email
     * @param email Owner email
     * @return Optional Owner
     */
    Optional<Owner> findByEmail(String email);
    
    /**
     * Find owners by type
     * @param ownerType Owner type
     * @return List of owners
     */
    List<Owner> findByOwnerType(OwnerType ownerType);
} 