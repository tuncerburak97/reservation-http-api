package com.reztech.reservation_http_api.repository;

import com.reztech.reservation_http_api.model.entity.ReservationSettings;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for ReservationSettings entity
 */
@Repository
public interface ReservationSettingsRepository extends MongoRepository<ReservationSettings, String> {
    
    /**
     * Find reservation settings by business ID
     * @param businessId Business ID
     * @return Optional ReservationSettings
     */
    Optional<ReservationSettings> findByBusinessId(String businessId);
    
    /**
     * Check if reservation settings exist for business
     * @param businessId Business ID
     * @return true if exists
     */
    boolean existsByBusinessId(String businessId);
    
    /**
     * Delete reservation settings by business ID
     * @param businessId Business ID
     */
    void deleteByBusinessId(String businessId);
} 