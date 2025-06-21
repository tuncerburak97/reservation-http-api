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
        List<AvailableSlotResponse.SlotInfo> expiredSlots = new ArrayList<>();
        
        for (TimeSlot slot : allPossibleSlots) {
            // Check if slot is blocked by business rules first
            boolean isBlockedByRules = isSlotBlockedByRules(slot, availabilityRules);
            
            if (isBlockedByRules) {
                blockedSlots.add(createBlockedSlotInfo(slot, availabilityRules));
                continue;
            }
            
            // Check if slot is in the past
            if (isSlotInPast(slot, date)) {
                expiredSlots.add(createPastSlotInfo(slot, date));
                continue;
            }
            
            // If no active employees, mark as blocked
            if (activeEmployees.isEmpty()) {
                blockedSlots.add(createNoEmployeesSlotInfo(slot));
                continue;
            }
            
            // Check each employee's availability for this slot
            List<String> availableEmployeeUserIds = new ArrayList<>();
            List<String> reservedEmployeeUserIds = new ArrayList<>();
            
            for (BusinessEmployee employee : activeEmployees) {
                boolean isEmployeeBooked = existingReservations.stream()
                        .anyMatch(reservation -> 
                                reservation.getTimeSlot() != null && 
                                reservation.getTimeSlot().overlaps(slot) &&
                                employee.getUserId().equals(reservation.getAssignedEmployeeUserId()));
                
                if (isEmployeeBooked) {
                    reservedEmployeeUserIds.add(employee.getUserId());
                } else {
                    availableEmployeeUserIds.add(employee.getUserId());
                }
            }
            
            // Create separate slot entries for available and booked employees
            if (!availableEmployeeUserIds.isEmpty()) {
                // Add slot for available employees
                availableSlots.add(AvailableSlotResponse.SlotInfo.builder()
                        .timeSlot(slot)
                        .status(SlotStatus.AVAILABLE)
                        .reason(null)
                        .isBookable(true)
                        .availableEmployeeUserIds(availableEmployeeUserIds)
                        .reservedEmployeeUserIds(reservedEmployeeUserIds)
                        .build());
            }
            
            if (!reservedEmployeeUserIds.isEmpty()) {
                // Add slot for booked employees
                bookedSlots.add(AvailableSlotResponse.SlotInfo.builder()
                        .timeSlot(slot)
                        .status(SlotStatus.BOOKED)
                        .reason("Employee(s) have existing reservations")
                        .isBookable(false)
                        .availableEmployeeUserIds(availableEmployeeUserIds)
                        .reservedEmployeeUserIds(reservedEmployeeUserIds)
                        .build());
            }
        }
        
        // Sort all slot lists by start time (ascending)
        availableSlots.sort(Comparator.comparing(s -> s.getTimeSlot().getStartTime()));
        blockedSlots.sort(Comparator.comparing(s -> s.getTimeSlot().getStartTime()));
        bookedSlots.sort(Comparator.comparing(s -> s.getTimeSlot().getStartTime()));
        expiredSlots.sort(Comparator.comparing(s -> s.getTimeSlot().getStartTime()));
        
        // Combine all slots into a single sorted list
        List<AvailableSlotResponse.SlotInfo> allSlots = new ArrayList<>();
        allSlots.addAll(availableSlots);
        allSlots.addAll(blockedSlots);
        allSlots.addAll(bookedSlots);
        allSlots.addAll(expiredSlots);
        
        // Sort combined list by start time
        allSlots.sort((s1, s2) -> s1.getTimeSlot().getStartTime().compareTo(s2.getTimeSlot().getStartTime()));
        
        return AvailableSlotResponse.builder()
                .businessId(businessId)
                .date(date)
                .availableSlots(availableSlots)
                .blockedSlots(blockedSlots)
                .bookedSlots(bookedSlots)
                .expiredSlots(expiredSlots)
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
     * Check if slot is blocked by business availability rules
     */
    private boolean isSlotBlockedByRules(TimeSlot slot, List<BusinessAvailability> availabilityRules) {
        for (BusinessAvailability rule : availabilityRules) {
            if (rule.getBlockedSlots() != null) {
                for (TimeSlot blockedSlot : rule.getBlockedSlots()) {
                    if (blockedSlot.overlaps(slot)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Check if slot is in the past
     */
    private boolean isSlotInPast(TimeSlot slot, LocalDate queryDate) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        
        if (queryDate.isBefore(currentDate)) {
            return true;
        }
        
        return queryDate.equals(currentDate) && slot.getStartTime().isBefore(currentTime);
    }
    
    /**
     * Create blocked slot info for business rules
     */
    private AvailableSlotResponse.SlotInfo createBlockedSlotInfo(TimeSlot slot, List<BusinessAvailability> availabilityRules) {
        String reason = "Blocked by business";
        for (BusinessAvailability rule : availabilityRules) {
            if (rule.getBlockedSlots() != null) {
                for (TimeSlot blockedSlot : rule.getBlockedSlots()) {
                    if (blockedSlot.overlaps(slot)) {
                        reason = rule.getBlockReason() != null ? rule.getBlockReason() : "Blocked by business";
                        break;
                    }
                }
            }
        }
        
        return AvailableSlotResponse.SlotInfo.builder()
                .timeSlot(slot)
                .status(SlotStatus.BLOCKED)
                .reason(reason)
                .isBookable(false)
                .availableEmployeeUserIds(new ArrayList<>())
                .reservedEmployeeUserIds(new ArrayList<>())
                .build();
    }
    
    /**
     * Create expired slot info for past time slots
     */
    private AvailableSlotResponse.SlotInfo createPastSlotInfo(TimeSlot slot, LocalDate queryDate) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        
        String reason;
        if (queryDate.isBefore(currentDate)) {
            reason = "Date has already passed";
        } else {
            reason = "Time slot has already passed";
        }
        
        return AvailableSlotResponse.SlotInfo.builder()
                .timeSlot(slot)
                .status(SlotStatus.EXPIRED)
                .reason(reason)
                .isBookable(false)
                .availableEmployeeUserIds(new ArrayList<>())
                .reservedEmployeeUserIds(new ArrayList<>())
                .build();
    }
    
    /**
     * Create blocked slot info when no employees are available
     */
    private AvailableSlotResponse.SlotInfo createNoEmployeesSlotInfo(TimeSlot slot) {
        return AvailableSlotResponse.SlotInfo.builder()
                .timeSlot(slot)
                .status(SlotStatus.BLOCKED)
                .reason("No active employees available")
                .isBookable(false)
                .availableEmployeeUserIds(new ArrayList<>())
                .reservedEmployeeUserIds(new ArrayList<>())
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