package com.reztech.reservation_http_api.service.business;

import com.reztech.reservation_http_api.model.api.request.CreateBusinessRequest;
import com.reztech.reservation_http_api.model.entity.main.business.Business;
import com.reztech.reservation_http_api.model.entity.main.business.Owner;
import com.reztech.reservation_http_api.repository.business.BusinessRepository;
import com.reztech.reservation_http_api.repository.owner.OwnerRepository;
import com.reztech.reservation_http_api.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for Business operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessService {
    
    private final BusinessRepository businessRepository;
    private final OwnerRepository ownerRepository;
    private final JsonUtils jsonUtils;
    
    /**
     * Create a new business
     * @param request Create business request
     * @return Created business
     */
    public Business createBusiness(CreateBusinessRequest request) {
        log.info("Creating business with name: {}", request.getName());
        
        // Find owner
        Owner owner = ownerRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found with id: " + request.getOwnerId()));
        
        Business business = Business.builder()
                .name(request.getName())
                .location(request.getLocation())
                .owner(owner)
                .contactInfo(request.getContactInfo())
                .build();
        
        return businessRepository.save(business);
    }
    
    /**
     * Update an existing business
     * @param id Business ID
     * @param request Update business request
     * @return Updated business
     */
    public Business updateBusiness(String id, CreateBusinessRequest request) {
        log.info("Updating business with id: {}", id);
        
        Business existingBusiness = businessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business not found with id: " + id));
        
        // Update fields
        if (request.getName() != null) {
            existingBusiness.setName(request.getName());
        }
        if (request.getLocation() != null) {
            existingBusiness.setLocation(request.getLocation());
        }
        if (request.getContactInfo() != null) {
            existingBusiness.setContactInfo(request.getContactInfo());
        }
        if (request.getOwnerId() != null) {
            Owner owner = ownerRepository.findById(request.getOwnerId())
                    .orElseThrow(() -> new RuntimeException("Owner not found with id: " + request.getOwnerId()));
            existingBusiness.setOwner(owner);
        }
        
        return businessRepository.save(existingBusiness);
    }
    
    /**
     * Get all businesses
     * @return List of all businesses
     */
    public List<Business> getAllBusinesses() {
        log.info("Getting all businesses");
        return businessRepository.findAll();
    }
    
    /**
     * Find business by ID
     * @param id Business ID
     * @return Business
     */
    public Business findById(String id) {
        log.info("Finding business by id: {}", id);
        
        return businessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business not found with id: " + id));
    }
    
    /**
     * Find businesses by owner ID
     * @param ownerId Owner ID
     * @return List of businesses
     */
    public List<Business> findByOwnerId(String ownerId) {
        log.info("Finding businesses by owner id: {}", ownerId);
        
        return businessRepository.findByOwnerId(ownerId);
    }
    
    /**
     * Search businesses by name
     * @param name Business name
     * @return List of businesses
     */
    public List<Business> searchByName(String name) {
        log.info("Searching businesses by name: {}", name);
        
        return businessRepository.findByNameContainingIgnoreCase(name);
    }
    
    /**
     * Delete business by ID
     * @param id Business ID
     */
    public void deleteBusiness(String id) {
        log.info("Deleting business with id: {}", id);
        
        if (!businessRepository.existsById(id)) {
            throw new RuntimeException("Business not found with id: " + id);
        }
        
        businessRepository.deleteById(id);
    }
} 