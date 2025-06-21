package com.reztech.reservation_http_api.service.user;

import com.reztech.reservation_http_api.constant.error.ErrorCode;
import com.reztech.reservation_http_api.constant.error.ErrorMessage;
import com.reztech.reservation_http_api.core.exception.BusinessException;
import com.reztech.reservation_http_api.core.exception.ResourceNotFoundException;
import com.reztech.reservation_http_api.model.api.request.CreateUserRequest;
import com.reztech.reservation_http_api.model.entity.main.user.User;
import com.reztech.reservation_http_api.model.enums.UserType;
import com.reztech.reservation_http_api.repository.user.UserRepository;
import com.reztech.reservation_http_api.repository.business.BusinessRepository;
import com.reztech.reservation_http_api.model.entity.main.business.Business;
import com.reztech.reservation_http_api.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for User operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final JsonUtils jsonUtils;
    
    /**
     * Create a new user
     * @param request Create user request
     * @return Created user
     */
    public User createUser(CreateUserRequest request) {
        log.info("Creating user with email: {}", request.getEmail());
        
        // Check if user already exists with same email
        //TODO check if user already exists with same gsm
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException(ErrorCode.USER_EMAIL_ALREADY_EXISTS, 
                String.format(ErrorMessage.USER_ALREADY_EXISTS, request.getEmail()));
        }
        
        User user = jsonUtils.convert(request, User.class);
        
        return userRepository.save(user);
    }
    
    /**
     * Update an existing user
     * @param id User ID
     * @param request Update user request
     * @return Updated user
     */
    public User updateUser(String id, CreateUserRequest request) {
        log.info("Updating user with id: {}", id);
        
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND, 
                    String.format(ErrorMessage.USER_NOT_FOUND, id)));
        
        // Update fields
        if (request.getName() != null) {
            existingUser.setName(request.getName());
        }
        if (request.getSurname() != null) {
            existingUser.setSurname(request.getSurname());
        }
        if (request.getGsm() != null) {
            existingUser.setGsm(request.getGsm());
        }
        if (request.getEmail() != null) {
            existingUser.setEmail(request.getEmail());
        }
        if (request.getUserType() != null) {
            existingUser.setUserType(request.getUserType());
        }
        
        return userRepository.save(existingUser);
    }
    
    /**
     * Get all users
     * @return List of all users
     */
    public List<User> getAllUsers() {
        log.info("Getting all users");
        return userRepository.findAll();
    }
    
    /**
     * Find user by ID
     * @param id User ID
     * @return User
     */
    public User findById(String id) {
        log.info("Finding user by id: {}", id);
        
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND, 
                    String.format(ErrorMessage.USER_NOT_FOUND, id)));
    }
    
    /**
     * Find user by email
     * @param email User email
     * @return User
     */
    public User findByEmail(String email) {
        log.info("Finding user by email: {}", email);
        
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND, 
                    String.format(ErrorMessage.USER_NOT_FOUND_BY_EMAIL, email)));
    }

    /**
     * Find users by type
     * @param userType User type
     * @return List of users
     */
    public List<User> findByUserType(UserType userType) {
        log.info("Finding users by type: {}", userType);
        
        return userRepository.findByUserType(userType);
    }

    /**
     * Find user by email and user type
     * @param email User email
     * @param userType User type
     * @return User
     */
    public User findByEmailAndUserType(String email, UserType userType) {
        log.info("Finding user by email: {} and type: {}", email, userType);
        
        return userRepository.findByEmailAndUserType(email, userType)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND, 
                    String.format("User not found with email: %s and type: %s", email, userType)));
    }
    
    /**
     * Find businesses where user is an employee
     * @param userId User ID
     * @return List of businesses where user is an employee
     */
    public List<Business> findBusinessesByEmployeeUserId(String userId) {
        log.info("Finding businesses where user {} is an employee", userId);
        
        // Use optimized repository method
        return businessRepository.findByEmployeeUserId(userId);
    }

    /**
     * Delete user by ID
     * @param id User ID
     */
    public void deleteUser(String id) {
        log.info("Deleting user with id: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND, 
                String.format(ErrorMessage.USER_NOT_FOUND, id));
        }
        
        userRepository.deleteById(id);
    }
} 