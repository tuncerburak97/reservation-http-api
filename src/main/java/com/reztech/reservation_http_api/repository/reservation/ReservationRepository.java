package com.reztech.reservation_http_api.repository.reservation;

import com.reztech.reservation_http_api.model.entity.main.reservation.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Reservation entity
 */
@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {
    
    /**
     * Find reservations by business ID
     * @param businessId Business ID
     * @return List of reservations
     */
    @Query("{'business.id': ?0}")
    List<Reservation> findByBusinessId(String businessId);
    
    /**
     * Find reservations by user ID
     * @param userId User ID
     * @return List of reservations
     */
    @Query("{'user.id': ?0}")
    List<Reservation> findByUserId(String userId);
} 