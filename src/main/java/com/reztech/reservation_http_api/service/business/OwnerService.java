package com.reztech.reservation_http_api.service.business;

import com.reztech.reservation_http_api.model.api.request.CreateOwnerRequest;
import com.reztech.reservation_http_api.model.entity.main.business.Owner;
import com.reztech.reservation_http_api.model.enums.OwnerType;
import com.reztech.reservation_http_api.repository.owner.OwnerRepository;
import com.reztech.reservation_http_api.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for Owner operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerService {
    
    private final OwnerRepository ownerRepository;
    private final JsonUtils jsonUtils;
    
    /**
     * Create a new owner
     * @param request Create owner request
     * @return Created owner
     */
    public Owner createOwner(CreateOwnerRequest request) {
        log.info("Creating owner with email: {}", request.getEmail());
        
        // Check if owner already exists with same email
        if (ownerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Owner already exists with email: " + request.getEmail());
        }
        
        Owner owner = jsonUtils.convert(request, Owner.class);
        
        return ownerRepository.save(owner);
    }
    
    /**
     * Update an existing owner
     * @param id Owner ID
     * @param request Update owner request
     * @return Updated owner
     */
    public Owner updateOwner(String id, CreateOwnerRequest request) {
        log.info("Updating owner with id: {}", id);
        
        Owner existingOwner = ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner not found with id: " + id));
        
        // Update fields
        if (request.getName() != null) {
            existingOwner.setName(request.getName());
        }
        if (request.getLastname() != null) {
            existingOwner.setLastname(request.getLastname());
        }
        if (request.getGsm() != null) {
            existingOwner.setGsm(request.getGsm());
        }
        if (request.getEmail() != null) {
            existingOwner.setEmail(request.getEmail());
        }
        if (request.getOwnerType() != null) {
            existingOwner.setOwnerType(request.getOwnerType());
        }
        
        return ownerRepository.save(existingOwner);
    }
    
    /**
     * Get all owners
     * @return List of all owners
     */
    public List<Owner> getAllOwners() {
        log.info("Getting all owners");
        return ownerRepository.findAll();
    }
    
    /**
     * Find owner by ID
     * @param id Owner ID
     * @return Owner
     */
    public Owner findById(String id) {
        log.info("Finding owner by id: {}", id);
        
        return ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner not found with id: " + id));
    }
    
    /**
     * Find owner by email
     * @param email Owner email
     * @return Owner
     */
    public Owner findByEmail(String email) {
        log.info("Finding owner by email: {}", email);
        
        return ownerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Owner not found with email: " + email));
    }
    
    /**
     * Find owners by type
     * @param ownerType Owner type
     * @return List of owners
     */
    public List<Owner> findByOwnerType(OwnerType ownerType) {
        log.info("Finding owners by type: {}", ownerType);
        
        return ownerRepository.findByOwnerType(ownerType);
    }
    
    /**
     * Delete owner by ID
     * @param id Owner ID
     */
    public void deleteOwner(String id) {
        log.info("Deleting owner with id: {}", id);
        
        if (!ownerRepository.existsById(id)) {
            throw new RuntimeException("Owner not found with id: " + id);
        }
        
        ownerRepository.deleteById(id);
    }
} 