package com.reztech.reservation_http_api.repository.business;

import com.reztech.reservation_http_api.model.entity.main.business.Business;
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
     * Find businesses by owner (user) ID
     * @param userId User ID (business owner)
     * @return List of businesses
     */
    @Query("{'owner.id': ?0}")
    List<Business> findByOwnerId(String userId);
    
    /**
     * Find businesses by name containing (case insensitive)
     * @param name Business name
     * @return List of businesses
     */
    List<Business> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find businesses where user is an employee
     * @param userId User ID
     * @return List of businesses
     */
    @Query("{'employees.userId': ?0, 'employees.active': true}")
    List<Business> findByEmployeeUserId(String userId);
    
    /**
     * Find businesses where user is an employee with specific role
     * @param userId User ID
     * @param role Employee role
     * @return List of businesses
     */
    @Query("{'employees.userId': ?0, 'employees.role': ?1, 'employees.active': true}")
    List<Business> findByEmployeeUserIdAndRole(String userId, String role);
} 