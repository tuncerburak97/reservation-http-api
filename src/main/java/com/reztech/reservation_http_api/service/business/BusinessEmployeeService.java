package com.reztech.reservation_http_api.service.business;

import com.reztech.reservation_http_api.model.api.request.AddBusinessEmployeeRequest;
import com.reztech.reservation_http_api.model.api.request.UpdateBusinessEmployeeRequest;
import com.reztech.reservation_http_api.model.api.response.BusinessEmployeeResponse;
import com.reztech.reservation_http_api.model.entity.embedded.BusinessEmployee;
import com.reztech.reservation_http_api.model.entity.main.business.Business;
import com.reztech.reservation_http_api.model.entity.main.user.User;
import com.reztech.reservation_http_api.model.enums.UserType;
import com.reztech.reservation_http_api.repository.business.BusinessRepository;
import com.reztech.reservation_http_api.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for Business Employee operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessEmployeeService {
    
    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;
    
    /**
     * Add an employee to a business
     * @param businessId Business ID
     * @param request Add employee request
     * @return Updated business
     */
    public Business addEmployee(String businessId, AddBusinessEmployeeRequest request) {
        log.info("Adding employee {} to business {}", request.getUserId(), businessId);
        //TO DO: when jwt token added to app, validate requested user is owner of the business?

        // Find business
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found with id: " + businessId));
        
        // Find user to be added as employee
        User employee = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));
        
        // Check if user is suitable to be an employee (not a customer)
        if (employee.getUserType() == UserType.CUSTOMER) {
            throw new RuntimeException("Customer users cannot be added as business employees");
        }
        
        // Check if user is already an employee of this business
        boolean alreadyEmployee = business.getEmployees().stream()
                .anyMatch(emp -> emp.getUserId().equals(request.getUserId()));
        
        if (alreadyEmployee) {
            throw new RuntimeException("User is already an employee of this business");
        }
        
        // Create business employee
        BusinessEmployee businessEmployee = BusinessEmployee.builder()
                .userId(employee.getId())
                .joinedAt(LocalDateTime.now())
                .active(true)
                .build();
        
        // Add employee to business
        business.getEmployees().add(businessEmployee);
        
        return businessRepository.save(business);
    }
    
    /**
     * Update a business employee
     * @param businessId Business ID
     * @param userId Employee user ID
     * @param request Update employee request
     * @return Updated business
     */
    public Business updateEmployee(String businessId, String userId, UpdateBusinessEmployeeRequest request) {
        log.info("Updating employee {} in business {}", userId, businessId);
        
        // Find business
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found with id: " + businessId));
        
        // Find employee in business
        Optional<BusinessEmployee> employeeOpt = business.getEmployees().stream()
                .filter(emp -> emp.getUserId().equals(userId))
                .findFirst();
        
        if (employeeOpt.isEmpty()) {
            throw new RuntimeException("Employee not found in business");
        }

        BusinessEmployee employee = employeeOpt.get();

        if (request.getActive() != null) {
            employee.setActive(request.getActive());
        }
        
        return businessRepository.save(business);
    }
    
    /**
     * Remove an employee from a business
     * @param businessId Business ID
     * @param userId Employee user ID
     * @return Updated business
     */
    public Business removeEmployee(String businessId, String userId) {
        log.info("Removing employee {} from business {}", userId, businessId);
        
        // Find business
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found with id: " + businessId));
        
        // Remove employee from business
        boolean removed = business.getEmployees().removeIf(emp -> emp.getUserId().equals(userId));
        
        if (!removed) {
            throw new RuntimeException("Employee not found in business");
        }
        
        return businessRepository.save(business);
    }
    
    /**
     * Get all employees of a business
     * @param businessId Business ID
     * @return List of business employees
     */
    public List<BusinessEmployeeResponse> getBusinessEmployees(String businessId) {
        log.info("Getting employees for business {}", businessId);
        
        // Find business
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found with id: " + businessId));
        
        // Convert to response objects
        return business.getEmployees().stream()
                .map(this::convertToResponse)
                .toList();
    }
    
    /**
     * Get a specific employee of a business
     * @param businessId Business ID
     * @param userId Employee user ID
     * @return Business employee response
     */
    public BusinessEmployeeResponse getBusinessEmployee(String businessId, String userId) {
        log.info("Getting employee {} for business {}", userId, businessId);
        
        // Find business
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found with id: " + businessId));
        
        // Find employee
        BusinessEmployee employee = business.getEmployees().stream()
                .filter(emp -> emp.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Employee not found in business"));
        
        return convertToResponse(employee);
    }
    
    /**
     * Check if a user is an employee of a business
     * @param businessId Business ID
     * @param userId User ID
     * @return true if user is an employee
     */
    public boolean isEmployeeOfBusiness(String businessId, String userId) {
        log.info("Checking if user {} is employee of business {}", userId, businessId);
        
        Optional<Business> businessOpt = businessRepository.findById(businessId);
        return businessOpt.map(business -> business.getEmployees().stream()
                .anyMatch(emp -> emp.getUserId().equals(userId) && emp.isActive())).orElse(false);

    }
    
    /**
     * Convert BusinessEmployee to BusinessEmployeeResponse
     * @param employee Business employee
     * @return Business employee response
     */
    private BusinessEmployeeResponse convertToResponse(BusinessEmployee employee) {
        return BusinessEmployeeResponse.builder()
                .userId(employee.getUserId())
                .joinedAt(employee.getJoinedAt())
                .active(employee.isActive())
                .build();
    }
} 