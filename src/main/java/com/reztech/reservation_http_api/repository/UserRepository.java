package com.reztech.reservation_http_api.repository;

import com.reztech.reservation_http_api.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entity
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    /**
     * Find user by email
     * @param email User email
     * @return Optional User
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find user by GSM number
     * @param gsm User GSM number
     * @return Optional User
     */
    Optional<User> findByGsm(String gsm);
} 