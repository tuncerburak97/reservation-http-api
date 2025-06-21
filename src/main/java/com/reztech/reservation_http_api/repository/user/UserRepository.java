package com.reztech.reservation_http_api.repository.user;

import com.reztech.reservation_http_api.model.entity.main.user.User;
import com.reztech.reservation_http_api.model.enums.UserType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    /**
     * Find users by type
     * @param userType User type
     * @return List of users
     */
    List<User> findByUserType(UserType userType);

    /**
     * Find users by email and user type
     * @param email User email
     * @param userType User type
     * @return Optional User
     */
    Optional<User> findByEmailAndUserType(String email, UserType userType);
} 