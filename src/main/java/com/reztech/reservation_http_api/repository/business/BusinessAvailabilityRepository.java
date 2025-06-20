package com.reztech.reservation_http_api.repository.business;

import com.reztech.reservation_http_api.model.entity.main.business.BusinessAvailability;
import com.reztech.reservation_http_api.model.enums.AvailabilityType;
import com.reztech.reservation_http_api.model.enums.ReservationDay;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for BusinessAvailability entity
 */
@Repository
public interface BusinessAvailabilityRepository extends MongoRepository<BusinessAvailability, String> {
    
    /**
     * Find business availability by business ID
     * @param businessId Business ID
     * @return List of business availability
     */
    List<BusinessAvailability> findByBusinessId(String businessId);
    
    /**
     * Find business availability by business ID and active status
     * @param businessId Business ID
     * @param isActive Active status
     * @return List of business availability
     */
    List<BusinessAvailability> findByBusinessIdAndIsActive(String businessId, Boolean isActive);
    
    /**
     * Find weekly recurring availability by business ID and day of week
     * @param businessId Business ID
     * @param dayOfWeek Day of week
     * @param isActive Active status
     * @return List of business availability
     */
    List<BusinessAvailability> findByBusinessIdAndDayOfWeekAndIsActive(
            String businessId, ReservationDay dayOfWeek, Boolean isActive);
    
    /**
     * Find specific date availability by business ID and date
     * @param businessId Business ID
     * @param specificDate Specific date
     * @param isActive Active status
     * @return List of business availability
     */
    List<BusinessAvailability> findByBusinessIdAndSpecificDateAndIsActive(
            String businessId, LocalDate specificDate, Boolean isActive);
    
    /**
     * Find date range availability by business ID and date range
     * @param businessId Business ID
     * @param date Target date
     * @param isActive Active status
     * @return List of business availability
     */
    @Query("{'businessId': ?0, 'availabilityType': 'DATE_RANGE', " +
           "'startDate': {$lte: ?1}, 'endDate': {$gte: ?1}, 'isActive': ?2}")
    List<BusinessAvailability> findByBusinessIdAndDateRangeContaining(
            String businessId, LocalDate date, Boolean isActive);
    
    /**
     * Find availability by type and business ID
     * @param businessId Business ID
     * @param availabilityType Availability type
     * @param isActive Active status
     * @return List of business availability
     */
    List<BusinessAvailability> findByBusinessIdAndAvailabilityTypeAndIsActive(
            String businessId, AvailabilityType availabilityType, Boolean isActive);
    
    /**
     * Delete availability by business ID
     * @param businessId Business ID
     */
    void deleteByBusinessId(String businessId);
} 