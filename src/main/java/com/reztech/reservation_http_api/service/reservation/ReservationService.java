package com.reztech.reservation_http_api.service.reservation;

import com.reztech.reservation_http_api.constant.error.ErrorCode;
import com.reztech.reservation_http_api.constant.error.ErrorMessage;
import com.reztech.reservation_http_api.core.exception.ResourceNotFoundException;
import com.reztech.reservation_http_api.model.api.request.CreateReservationRequest;
import com.reztech.reservation_http_api.model.api.response.ReservationResponse;
import com.reztech.reservation_http_api.model.entity.main.business.Business;
import com.reztech.reservation_http_api.model.entity.main.reservation.Reservation;
import com.reztech.reservation_http_api.model.entity.main.user.User;
import com.reztech.reservation_http_api.repository.business.BusinessRepository;
import com.reztech.reservation_http_api.repository.reservation.ReservationRepository;
import com.reztech.reservation_http_api.repository.user.UserRepository;
import com.reztech.reservation_http_api.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Service class for Reservation operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {
    
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final JsonUtils jsonUtils;
    
    /**
     * Create a new reservation
     * @param request Create reservation request
     * @return Created reservation response
     */
    public ReservationResponse createReservation(CreateReservationRequest request) {
        log.info("Creating reservation for user {} and business {}", request.getUserId(), request.getBusinessId());

        //TODO user üye olmadan da rezervasyon yapabilir.
        // Find user and business
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND, 
                    String.format(ErrorMessage.USER_NOT_FOUND, request.getUserId())));
        
        Business business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BUSINESS_NOT_FOUND, 
                    String.format(ErrorMessage.BUSINESS_NOT_FOUND, request.getBusinessId())));

        //TODO rezervsayon uygunlu kontrol et
        // reservation saat bilgisi anlaşılır mı
        Reservation reservation = Reservation.builder()
                .user(user)
                .business(business)
                .reservationDate(request.getReservationDate())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        
        Reservation savedReservation = reservationRepository.save(reservation);
        
        return jsonUtils.convert(savedReservation, ReservationResponse.class);
    }
    
    /**
     * Update an existing reservation
     * @param id Reservation ID
     * @param request Update reservation request
     * @return Updated reservation response
     */
    public ReservationResponse updateReservation(String id, CreateReservationRequest request) {
        log.info("Updating reservation with id: {}", id);
        //TODO güncelleme için uygunluk kontrolü yap
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
        
        // Update fields
        if (request.getReservationDate() != null) {
            existingReservation.setReservationDate(request.getReservationDate());
        }
        
        existingReservation.setUpdatedAt(Instant.now());
        
        Reservation updatedReservation = reservationRepository.save(existingReservation);
        
        return jsonUtils.convert(updatedReservation, ReservationResponse.class);
    }
    
    /**
     * Get all reservations
     * @return List of all reservations
     */
    public List<ReservationResponse> getAllReservations() {
        log.info("Getting all reservations");
        
        List<Reservation> reservations = reservationRepository.findAll();
        
        return reservations.stream()
                .map(reservation -> jsonUtils.convert(reservation, ReservationResponse.class))
                .toList();
    }
    
    /**
     * Find reservation by ID
     * @param id Reservation ID
     * @return Reservation response
     */
    public ReservationResponse findById(String id) {
        log.info("Finding reservation by id: {}", id);
        
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
        
        return jsonUtils.convert(reservation, ReservationResponse.class);
    }
    
    /**
     * Find reservations by business ID
     * @param businessId Business ID
     * @return List of reservations for the business
     */
    public List<ReservationResponse> findByBusinessId(String businessId) {
        log.info("Finding reservations by business id: {}", businessId);
        
        List<Reservation> reservations = reservationRepository.findByBusinessId(businessId);
        
        return reservations.stream()
                .map(reservation -> jsonUtils.convert(reservation, ReservationResponse.class))
                .toList();
    }
    
    /**
     * Find reservations by user ID
     * @param userId User ID
     * @return List of reservations for the user
     */
    public List<ReservationResponse> findByUserId(String userId) {
        log.info("Finding reservations by user id: {}", userId);
        
        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        
        return reservations.stream()
                .map(reservation -> jsonUtils.convert(reservation, ReservationResponse.class))
                .toList();
    }
    
    /**
     * Delete reservation by ID
     * @param id Reservation ID
     */
    public void deleteReservation(String id) {
        log.info("Deleting reservation with id: {}", id);
        
        if (!reservationRepository.existsById(id)) {
            throw new RuntimeException("Reservation not found with id: " + id);
        }
        
        reservationRepository.deleteById(id);
    }
} 