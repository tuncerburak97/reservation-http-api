package com.reztech.reservation_http_api.service.reservation;

import com.reztech.reservation_http_api.model.api.response.AvailableSlotResponse;
import com.reztech.reservation_http_api.model.entity.embedded.BusinessEmployee;
import com.reztech.reservation_http_api.model.entity.main.business.Business;
import com.reztech.reservation_http_api.model.entity.main.business.BusinessAvailability;
import com.reztech.reservation_http_api.model.entity.main.reservation.Reservation;
import com.reztech.reservation_http_api.model.entity.main.reservation.ReservationSettings;
import com.reztech.reservation_http_api.model.entity.embedded.TimeSlot;
import com.reztech.reservation_http_api.model.enums.ReservationDay;
import com.reztech.reservation_http_api.model.enums.SlotStatus;
import com.reztech.reservation_http_api.repository.business.BusinessAvailabilityRepository;
import com.reztech.reservation_http_api.repository.business.BusinessRepository;
import com.reztech.reservation_http_api.repository.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing business availability and calculating available time slots with employee-based booking
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AvailabilityService {
    
    private final BusinessAvailabilityRepository businessAvailabilityRepository;
    private final BusinessRepository businessRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationSettingsService reservationSettingsService;
    
    /**
     * Get available slots for a specific business and date with employee information
     * @param businessId Business ID
     * @param date Target date
     * @return Available slot response with all slot information and employee availability
     */
    public AvailableSlotResponse getAvailableSlots(String businessId, LocalDate date) {
        log.info("Getting available slots for business: {} on date: {}", businessId, date);
        
        // Get business with employees
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found with id: " + businessId));
        
        // Get active employees
        List<BusinessEmployee> activeEmployees = getActiveEmployees(business);
        
        // Get business reservation settings
        ReservationSettings settings = reservationSettingsService.getOrCreateDefaultSettings(businessId);
        
        // Generate all possible slots based on settings
        List<TimeSlot> allPossibleSlots = generateAllPossibleSlots(settings);
        
        // Get business availability rules for this date
        List<BusinessAvailability> availabilityRules = getAvailabilityRulesForDate(businessId, date);
        
        // Get existing reservations for this date
        List<Reservation> existingReservations = getExistingReservations(businessId, date);
        
        // Calculate slot statuses with employee information
        List<AvailableSlotResponse.SlotInfo> availableSlots = new ArrayList<>();
        List<AvailableSlotResponse.SlotInfo> blockedSlots = new ArrayList<>();
        List<AvailableSlotResponse.SlotInfo> bookedSlots = new ArrayList<>();
        
        for (TimeSlot slot : allPossibleSlots) {
            AvailableSlotResponse.SlotInfo slotInfo = determineSlotStatusWithEmployees(
                    slot, availabilityRules, existingReservations, activeEmployees, date);
            
            switch (slotInfo.getStatus()) {
                case AVAILABLE -> availableSlots.add(slotInfo);
                case BLOCKED -> blockedSlots.add(slotInfo);
                case BOOKED -> bookedSlots.add(slotInfo);
            }
        }
        
        // Sort all slot lists by start time (ascending)
        availableSlots.sort(Comparator.comparing(s -> s.getTimeSlot().getStartTime()));
        blockedSlots.sort(Comparator.comparing(s -> s.getTimeSlot().getStartTime()));
        bookedSlots.sort(Comparator.comparing(s -> s.getTimeSlot().getStartTime()));
        
        // Combine all slots into a single sorted list
        List<AvailableSlotResponse.SlotInfo> allSlots = new ArrayList<>();
        allSlots.addAll(availableSlots);
        allSlots.addAll(blockedSlots);
        allSlots.addAll(bookedSlots);
        
        // Sort combined list by start time
        allSlots.sort((s1, s2) -> s1.getTimeSlot().getStartTime().compareTo(s2.getTimeSlot().getStartTime()));
        
        return AvailableSlotResponse.builder()
                .businessId(businessId)
                .date(date)
                .availableSlots(availableSlots)
                .blockedSlots(blockedSlots)
                .bookedSlots(bookedSlots)
                .slots(allSlots)
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
     * Get active employees for a business
     * @param business Business entity
     * @return List of active employees
     */
    private List<BusinessEmployee> getActiveEmployees(Business business) {
        if (business.getEmployees() == null) {
            return new ArrayList<>();
        }
        
        return business.getEmployees().stream()
                .filter(BusinessEmployee::isActive)
                .collect(Collectors.toList());
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
        
        // Add safety check to prevent infinite loops
        int maxSlots = 48; // Maximum 48 slots per day (30-minute slots)
        int slotCount = 0;
        
        while (currentTime.isBefore(endTime) && slotCount < maxSlots) {
            LocalTime slotEndTime = currentTime.plusMinutes(slotDuration);
            
            // Check if we're crossing midnight (slotEndTime is before currentTime)
            if (slotEndTime.isBefore(currentTime) || slotEndTime.equals(LocalTime.MIDNIGHT)) {
                // We've crossed midnight, stop here
                break;
            }
            
            // Check if slotEndTime exceeds the business end time
            if (slotEndTime.isAfter(endTime)) {
                break;
            }
            
            slots.add(TimeSlot.of(currentTime, slotEndTime));
            currentTime = slotEndTime;
            slotCount++;
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
     * Determine the status of a specific time slot with employee availability
     * @param slot Time slot to check
     * @param availabilityRules Availability rules
     * @param existingReservations Existing reservations
     * @param activeEmployees Active employees
     * @param queryDate Date being queried for availability
     * @return Slot information with status and employee availability
     */
    private AvailableSlotResponse.SlotInfo determineSlotStatusWithEmployees(
            TimeSlot slot, 
            List<BusinessAvailability> availabilityRules,
            List<Reservation> existingReservations,
            List<BusinessEmployee> activeEmployees,
            LocalDate queryDate) {
        
        // Check if the time slot is in the past
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        
        // If query date is in the past, all slots are blocked
        if (queryDate.isBefore(currentDate)) {
            return AvailableSlotResponse.SlotInfo.builder()
                    .timeSlot(slot)
                    .status(SlotStatus.BLOCKED)
                    .reason("Date has already passed")
                    .isBookable(false)
                    .availableEmployeeUserIds(new ArrayList<>())
                    .build();
        }
        
        // If query date is today, check if time slot has passed
        if (queryDate.equals(currentDate) && slot.getStartTime().isBefore(currentTime)) {
            return AvailableSlotResponse.SlotInfo.builder()
                    .timeSlot(slot)
                    .status(SlotStatus.BLOCKED)
                    .reason("Time slot has already passed")
                    .isBookable(false)
                    .availableEmployeeUserIds(new ArrayList<>())
                    .build();
        }
        
        // If no active employees, mark as blocked
        if (activeEmployees.isEmpty()) {
            return AvailableSlotResponse.SlotInfo.builder()
                    .timeSlot(slot)
                    .status(SlotStatus.BLOCKED)
                    .reason("No active employees available")
                    .isBookable(false)
                    .availableEmployeeUserIds(new ArrayList<>())
                    .build();
        }
        
        // Check which employees are available for this slot
        List<String> availableEmployeeUserIds = new ArrayList<>();
        String reservedEmployeeUserId = null;
        
        for (BusinessEmployee employee : activeEmployees) {
            // Check if employee has an existing reservation at this time slot
            boolean isEmployeeBooked = existingReservations.stream()
                    .anyMatch(reservation -> 
                            reservation.getTimeSlot() != null && 
                            reservation.getTimeSlot().overlaps(slot) &&
                            employee.getUserId().equals(reservation.getAssignedEmployeeUserId()));
            
            if (isEmployeeBooked) {
                // Find which reservation has this employee
                reservedEmployeeUserId = existingReservations.stream()
                        .filter(reservation -> 
                                reservation.getTimeSlot() != null && 
                                reservation.getTimeSlot().overlaps(slot) &&
                                employee.getUserId().equals(reservation.getAssignedEmployeeUserId()))
                        .map(Reservation::getAssignedEmployeeUserId)
                        .findFirst()
                        .orElse(null);
            } else {
                // Employee is available for this slot
                availableEmployeeUserIds.add(employee.getUserId());
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
                                .availableEmployeeUserIds(new ArrayList<>())
                                .build();
                    }
                }
            }
        }
        
        // If all employees are booked, mark as booked
        if (availableEmployeeUserIds.isEmpty() && reservedEmployeeUserId != null) {
            return AvailableSlotResponse.SlotInfo.builder()
                    .timeSlot(slot)
                    .status(SlotStatus.BOOKED)
                    .reason("All employees are booked")
                    .isBookable(false)
                    .availableEmployeeUserIds(new ArrayList<>())
                    .reservedEmployeeUserId(reservedEmployeeUserId)
                    .build();
        }
        
        // Check if slot is explicitly available (if there are availability rules)
        if (!availabilityRules.isEmpty()) {
            boolean isExplicitlyAvailable = availabilityRules.stream()
                    .anyMatch(rule -> rule.getAvailableSlots() != null && 
                                    rule.getAvailableSlots().stream()
                                            .anyMatch(availableSlot -> availableSlot.overlaps(slot)));
            
            if (!isExplicitlyAvailable) {
                return AvailableSlotResponse.SlotInfo.builder()
                        .timeSlot(slot)
                        .status(SlotStatus.BLOCKED)
                        .reason("Not in available time slots")
                        .isBookable(false)
                        .availableEmployeeUserIds(new ArrayList<>())
                        .build();
            }
        }
        
        // Slot is available with some employees
        return AvailableSlotResponse.SlotInfo.builder()
                .timeSlot(slot)
                .status(SlotStatus.AVAILABLE)
                .reason(null)
                .isBookable(!availableEmployeeUserIds.isEmpty())
                .availableEmployeeUserIds(availableEmployeeUserIds)
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