package com.reztech.reservation_http_api;

import com.reztech.reservation_http_api.model.entity.embedded.TimeSlot;
import com.reztech.reservation_http_api.model.entity.main.user.User;
import com.reztech.reservation_http_api.model.entity.main.business.Business;
import com.reztech.reservation_http_api.model.enums.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to verify Lombok builder functionality
 */
@SpringBootTest
public class LombokBuilderTest {

    @Test
    public void testTimeSlotBuilder() {
        // Test TimeSlot builder
        TimeSlot timeSlot = TimeSlot.builder()
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(9, 30))
                .build();
        
        assertNotNull(timeSlot);
        assertEquals(LocalTime.of(9, 0), timeSlot.getStartTime());
        assertEquals(LocalTime.of(9, 30), timeSlot.getEndTime());
    }
    
    @Test
    public void testTimeSlotOfMethod() {
        // Test TimeSlot.of() static method
        TimeSlot timeSlot = TimeSlot.of(LocalTime.of(10, 0));
        
        assertNotNull(timeSlot);
        assertEquals(LocalTime.of(10, 0), timeSlot.getStartTime());
        assertEquals(LocalTime.of(10, 30), timeSlot.getEndTime());
    }
    
    @Test
    public void testUserBuilder() {
        // Test User builder with different user types
        User businessOwner = User.builder()
                .name("BusinessOwner")
                .surname("Test")
                .email("owner@example.com")
                .gsm("05559876543")
                .userType(UserType.BUSINESS_OWNER)
                .build();

        assertNotNull(businessOwner);
        assertEquals("BusinessOwner", businessOwner.getName());
        assertEquals("Test", businessOwner.getSurname());
        assertEquals("owner@example.com", businessOwner.getEmail());
        assertEquals("05559876543", businessOwner.getGsm());
        assertEquals(UserType.BUSINESS_OWNER, businessOwner.getUserType());

        // Test Customer user
        User customer = User.builder()
                .name("Customer")
                .surname("Test")
                .email("customer@example.com")
                .gsm("05551234567")
                .userType(UserType.CUSTOMER)
                .build();

        assertNotNull(customer);
        assertEquals(UserType.CUSTOMER, customer.getUserType());
    }
    
    @Test
    public void testBusinessBuilder() {
        // Create a business owner user for testing
        User owner = User.builder()
                .name("Business")
                .surname("Owner")
                .email("business@example.com")
                .gsm("05559876543")
                .userType(UserType.BUSINESS_OWNER)
                .build();

        // Test Business builder
        Business business = Business.builder()
                .name("Test Business")
                .owner(owner)
                .build();

        assertNotNull(business);
        assertEquals("Test Business", business.getName());
        assertNotNull(business.getOwner());
        assertEquals("Business", business.getOwner().getName());
        assertEquals("Owner", business.getOwner().getSurname());
        assertEquals(UserType.BUSINESS_OWNER, business.getOwner().getUserType());
    }
} 