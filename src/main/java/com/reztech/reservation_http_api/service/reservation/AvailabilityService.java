package com.reztech.reservation_http_api.service.reservation;

import com.reztech.reservation_http_api.model.api.response.AvailableSlotResponse;
import com.reztech.reservation_http_api.model.entity.main.business.BusinessAvailability;
import com.reztech.reservation_http_api.model.entity.main.reservation.Reservation;
import com.reztech.reservation_http_api.model.entity.main.reservation.ReservationSettings;
import com.reztech.reservation_http_api.model.entity.embedded.TimeSlot;
import com.reztech.reservation_http_api.model.enums.ReservationDay;
import com.reztech.reservation_http_api.model.enums.SlotStatus;
import com.reztech.reservation_http_api.repository.business.BusinessAvailabilityRepository;
import com.reztech.reservation_http_api.repository.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing business availability and calculating available time slots
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AvailabilityService {
    
    private final BusinessAvailabilityRepository businessAvailabilityRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationSettingsService reservationSettingsService;
    
    /**
     * Get available slots for a specific business and date
     * @param businessId Business ID
     * @param date Target date
     * @return Available slot response with all slot information
     */
    public AvailableSlotResponse getAvailableSlots(String businessId, LocalDate date) {
        log.info("Getting available slots for business: {} on date: {}", businessId, date);
        
        // Get business reservation settings
        ReservationSettings settings = reservationSettingsService.getOrCreateDefaultSettings(businessId);
        
        // Generate all possible slots based on settings
        List<TimeSlot> allPossibleSlots = generateAllPossibleSlots(settings);
        
        // Get business availability rules for this date
        List<BusinessAvailability> availabilityRules = getAvailabilityRulesForDate(businessId, date);
        
        // Get existing reservations for this date
        List<Reservation> existingReservations = getExistingReservations(businessId, date);
        
        // Calculate slot statuses
        List<AvailableSlotResponse.SlotInfo> availableSlots = new ArrayList<>();
        List<AvailableSlotResponse.SlotInfo> blockedSlots = new ArrayList<>();
        List<AvailableSlotResponse.SlotInfo> bookedSlots = new ArrayList<>();
        
        for (TimeSlot slot : allPossibleSlots) {
            AvailableSlotResponse.SlotInfo slotInfo = determineSlotStatus(
                    slot, availabilityRules, existingReservations);
            
            switch (slotInfo.getStatus()) {
                case AVAILABLE -> availableSlots.add(slotInfo);
                case BLOCKED -> blockedSlots.add(slotInfo);
                case BOOKED -> bookedSlots.add(slotInfo);
            }
        }
        
        return AvailableSlotResponse.builder()
                .businessId(businessId)
                .date(date)
                .availableSlots(availableSlots)
                .blockedSlots(blockedSlots)
                .bookedSlots(bookedSlots)
                .build();
    }
    
    /**
     * Get available slots for a date range
     * @param businessId Business ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of available slot responses
     */
    public List<AvailableSlotResponse> getAvailableSlotsForRange(
            String businessId, LocalDate startDate, LocalDate endDate) {
        
        log.info("Getting available slots for business: {} from {} to {}", 
                businessId, startDate, endDate);
        
        List<AvailableSlotResponse> responses = new ArrayList<>();
        LocalDate currentDate = startDate;
        
        while (!currentDate.isAfter(endDate)) {
            responses.add(getAvailableSlots(businessId, currentDate));
            currentDate = currentDate.plusDays(1);
        }
        
        return responses;
    }
    
    /**
     * Generate all possible time slots based on business settings
     * @param settings Reservation settings
     * @return List of all possible time slots
     */
    private List<TimeSlot> generateAllPossibleSlots(ReservationSettings settings) {
        List<TimeSlot> slots = new ArrayList<>();
        
        LocalTime currentTime = settings.getDefaultStartTime();
        LocalTime endTime = settings.getDefaultEndTime();
        int slotDuration = settings.getSlotDurationMinutes();
        
        // Handle midnight crossing (e.g., 22:00 to 02:00)
        if (endTime.equals(LocalTime.MIDNIGHT) || endTime.isBefore(currentTime)) {
            endTime = LocalTime.of(23, 59); // End at 23:59 for same day
        }
        
        while (currentTime.isBefore(endTime)) {
            LocalTime slotEndTime = currentTime.plusMinutes(slotDuration);
            if (slotEndTime.isAfter(endTime)) {
                break;
            }
            
            slots.add(TimeSlot.of(currentTime, slotEndTime));
            currentTime = slotEndTime;
        }
        
        return slots;
    }
    
    /**
     * Get availability rules that apply to a specific date
     * @param businessId Business ID
     * @param date Target date
     * @return List of applicable availability rules
     */
    private List<BusinessAvailability> getAvailabilityRulesForDate(String businessId, LocalDate date) {
        List<BusinessAvailability> rules = new ArrayList<>();
        
        // Get weekly recurring rules for this day of week
        ReservationDay dayOfWeek = convertDayOfWeek(date.getDayOfWeek());
        rules.addAll(businessAvailabilityRepository
                .findByBusinessIdAndDayOfWeekAndIsActive(businessId, dayOfWeek, true));
        
        // Get specific date rules
        rules.addAll(businessAvailabilityRepository
                .findByBusinessIdAndSpecificDateAndIsActive(businessId, date, true));
        
        // Get date range rules
        rules.addAll(businessAvailabilityRepository
                .findByBusinessIdAndDateRangeContaining(businessId, date, true));
        
        return rules;
    }
    
    /**
     * Get existing reservations for a specific date
     * @param businessId Business ID
     * @param date Target date
     * @return List of existing reservations
     */
    private List<Reservation> getExistingReservations(String businessId, LocalDate date) {
        return reservationRepository.findByBusinessId(businessId)
                .stream()
                .filter(reservation -> reservation.getReservationDate().equals(date))
                .filter(reservation -> !reservation.getIsCancelled())
                .collect(Collectors.toList());
    }
    
    /**
     * Determine the status of a specific time slot
     * @param slot Time slot to check
     * @param availabilityRules Availability rules
     * @param existingReservations Existing reservations
     * @return Slot information with status
     */
    private AvailableSlotResponse.SlotInfo determineSlotStatus(
            TimeSlot slot, 
            List<BusinessAvailability> availabilityRules,
            List<Reservation> existingReservations) {
        
        // Check if slot is booked
        for (Reservation reservation : existingReservations) {
            if (reservation.getTimeSlot() != null && 
                reservation.getTimeSlot().overlaps(slot)) {
                
                return AvailableSlotResponse.SlotInfo.builder()
                        .timeSlot(slot)
                        .status(SlotStatus.BOOKED)
                        .reason("Already booked")
                        .isBookable(false)
                        .build();
            }
        }
        
        // Check availability rules (blocked slots have priority)
        for (BusinessAvailability rule : availabilityRules) {
            if (rule.getBlockedSlots() != null) {
                for (TimeSlot blockedSlot : rule.getBlockedSlots()) {
                    if (blockedSlot.overlaps(slot)) {
                        return AvailableSlotResponse.SlotInfo.builder()
                                .timeSlot(slot)
                                .status(SlotStatus.BLOCKED)
                                .reason(rule.getBlockReason() != null ? 
                                        rule.getBlockReason() : "Blocked by business")
                                .isBookable(false)
                                .build();
                    }
                }
            }
        }
        
        // Check if slot is explicitly available
        boolean isExplicitlyAvailable = availabilityRules.stream()
                .anyMatch(rule -> rule.getAvailableSlots() != null && 
                                rule.getAvailableSlots().stream()
                                        .anyMatch(availableSlot -> availableSlot.overlaps(slot)));
        
        // If there are availability rules but slot is not explicitly available, block it
        if (!availabilityRules.isEmpty() && !isExplicitlyAvailable) {
            return AvailableSlotResponse.SlotInfo.builder()
                    .timeSlot(slot)
                    .status(SlotStatus.BLOCKED)
                    .reason("Not in available time slots")
                    .isBookable(false)
                    .build();
        }
        
        // Slot is available
        return AvailableSlotResponse.SlotInfo.builder()
                .timeSlot(slot)
                .status(SlotStatus.AVAILABLE)
                .reason(null)
                .isBookable(true)
                .build();
    }
    
    /**
     * Convert Java DayOfWeek to our ReservationDay enum
     * @param dayOfWeek Java DayOfWeek
     * @return ReservationDay enum
     */
    private ReservationDay convertDayOfWeek(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> ReservationDay.MONDAY;
            case TUESDAY -> ReservationDay.TUESDAY;
            case WEDNESDAY -> ReservationDay.WEDNESDAY;
            case THURSDAY -> ReservationDay.THURSDAY;
            case FRIDAY -> ReservationDay.FRIDAY;
            case SATURDAY -> ReservationDay.SATURDAY;
            case SUNDAY -> ReservationDay.SUNDAY;
        };
    }
} 