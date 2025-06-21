package com.reztech.reservation_http_api.model.entity.main.reservation;

import com.reztech.reservation_http_api.model.entity.embedded.TimeSlot;
import com.reztech.reservation_http_api.model.entity.main.user.User;
import com.reztech.reservation_http_api.model.entity.main.business.Business;
import com.reztech.reservation_http_api.model.enums.SlotStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Main Reservation entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "reservations")
/*
@CompoundIndexes({
    @CompoundIndex(name = "business_date_timeslot", def = "{'business.id': 1, 'reservationDate': 1, 'timeSlot.startTime': 1}", unique = true),
    @CompoundIndex(name = "user_date", def = "{'user.id': 1, 'reservationDate': 1}"),
    @CompoundIndex(name = "business_date", def = "{'business.id': 1, 'reservationDate': 1}")
})

 */
public class Reservation {
    
    @Id
    private String id;
    
    //@Indexed
    private User user;
    
    //@Indexed
    private Business business;
    
    /**
     * Date of the reservation
     */
    //@Indexed
    private LocalDate reservationDate;
    
    /**
     * Time slot for the reservation (30-minute interval)
     */
    private TimeSlot timeSlot;
    
    /**
     * The assigned employee userId for this reservation
     */
    private String assignedEmployeeUserId;
    
    /**
     * Status of the reservation slot
     */
    @Builder.Default
    //@Indexed
    private SlotStatus status = SlotStatus.BOOKED;
    
    /**
     * Confirmation status
     */
    @Builder.Default
    //@Indexed
    private Boolean isConfirmed = false;
    
    /**
     * Cancellation status
     */
    @Builder.Default
    //@Indexed
    private Boolean isCancelled = false;
    
    /**
     * Cancellation reason (if cancelled)
     */
    private String cancellationReason;
    
    /**
     * Notes for the reservation
     */
    private String notes;
    
    //@Indexed
    private Instant createdAt;
    
    private Instant updatedAt;

    //TODO updateBy bilgisi : Business owner / user
    //TODO createdBy bilgisi : Business owner/ user

    //TODO Rezervasyonları saaat aralıkları olmalı
} 