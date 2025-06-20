package com.reztech.reservation_http_api.service.reservation;

import com.reztech.reservation_http_api.constant.error.ErrorCode;
import com.reztech.reservation_http_api.constant.error.ErrorMessage;
import com.reztech.reservation_http_api.core.exception.ResourceNotFoundException;
import com.reztech.reservation_http_api.model.api.request.CreateReservationSettingsRequest;
import com.reztech.reservation_http_api.model.entity.main.reservation.ReservationSettings;
import com.reztech.reservation_http_api.repository.business.BusinessRepository;
import com.reztech.reservation_http_api.repository.reservation.ReservationSettingsRepository;
import com.reztech.reservation_http_api.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Service class for ReservationSettings operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationSettingsService {
    
    private final ReservationSettingsRepository reservationSettingsRepository;
    private final BusinessRepository businessRepository;
    private final JsonUtils jsonUtils;
    
    /**
     * Create or update reservation settings for a business
     * @param request Create reservation settings request
     * @return Created/Updated reservation settings
     */
    public ReservationSettings createOrUpdateSettings(CreateReservationSettingsRequest request) {
        log.info("Creating/updating reservation settings for business: {}", request.getBusinessId());
        
        // Validate business exists
        if (!businessRepository.existsById(request.getBusinessId())) {
            throw new ResourceNotFoundException(ErrorCode.BUSINESS_NOT_FOUND, 
                String.format(ErrorMessage.BUSINESS_NOT_FOUND, request.getBusinessId()));
        }
        
        // Check if settings already exist
        ReservationSettings settings = reservationSettingsRepository
                .findByBusinessId(request.getBusinessId())
                .orElse(null);
        
        if (settings == null) {
            // Create new settings
            settings = jsonUtils.convert(request, ReservationSettings.class);
            settings.setCreatedAt(Instant.now());
        } else {
            // Update existing settings
            updateSettingsFromRequest(settings, request);
        }
        
        settings.setUpdatedAt(Instant.now());
        
        return reservationSettingsRepository.save(settings);
    }
    
    /**
     * Get reservation settings by business ID
     * @param businessId Business ID
     * @return Reservation settings
     */
    public ReservationSettings getSettingsByBusinessId(String businessId) {
        log.info("Getting reservation settings for business: {}", businessId);
        
        return reservationSettingsRepository.findByBusinessId(businessId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BUSINESS_NOT_FOUND, 
                    "Reservation settings not found for business: " + businessId));
    }
    
    /**
     * Get all reservation settings
     * @return List of all reservation settings
     */
    public List<ReservationSettings> getAllSettings() {
        log.info("Getting all reservation settings");
        return reservationSettingsRepository.findAll();
    }
    
    /**
     * Delete reservation settings by business ID
     * @param businessId Business ID
     */
    public void deleteSettingsByBusinessId(String businessId) {
        log.info("Deleting reservation settings for business: {}", businessId);
        
        if (!reservationSettingsRepository.existsByBusinessId(businessId)) {
            throw new ResourceNotFoundException(ErrorCode.BUSINESS_NOT_FOUND, 
                "Reservation settings not found for business: " + businessId);
        }
        
        reservationSettingsRepository.deleteByBusinessId(businessId);
    }
    
    /**
     * Get or create default settings for a business
     * @param businessId Business ID
     * @return Reservation settings (default if not exists)
     */
    public ReservationSettings getOrCreateDefaultSettings(String businessId) {
        log.info("Getting or creating default settings for business: {}", businessId);
        
        return reservationSettingsRepository.findByBusinessId(businessId)
                .orElseGet(() -> {
                    ReservationSettings defaultSettings = ReservationSettings.builder()
                            .businessId(businessId)
                            .createdAt(Instant.now())
                            .updatedAt(Instant.now())
                            .build();
                    
                    return reservationSettingsRepository.save(defaultSettings);
                });
    }
    
    /**
     * Update settings from request
     * @param settings Existing settings
     * @param request Update request
     */
    private void updateSettingsFromRequest(ReservationSettings settings, CreateReservationSettingsRequest request) {
        if (request.getDefaultStartTime() != null) {
            settings.setDefaultStartTime(request.getDefaultStartTime());
        }
        if (request.getDefaultEndTime() != null) {
            settings.setDefaultEndTime(request.getDefaultEndTime());
        }
        if (request.getSlotDurationMinutes() != null) {
            settings.setSlotDurationMinutes(request.getSlotDurationMinutes());
        }
        if (request.getMaxAdvanceBookingDays() != null) {
            settings.setMaxAdvanceBookingDays(request.getMaxAdvanceBookingDays());
        }
        if (request.getMinAdvanceBookingHours() != null) {
            settings.setMinAdvanceBookingHours(request.getMinAdvanceBookingHours());
        }
        if (request.getAcceptReservations() != null) {
            settings.setAcceptReservations(request.getAcceptReservations());
        }
        if (request.getAutoConfirm() != null) {
            settings.setAutoConfirm(request.getAutoConfirm());
        }
    }
} 