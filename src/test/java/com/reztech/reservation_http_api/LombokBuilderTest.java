package com.reztech.reservation_http_api;

import com.reztech.reservation_http_api.model.entity.embedded.TimeSlot;
import com.reztech.reservation_http_api.model.entity.main.user.User;
import com.reztech.reservation_http_api.model.entity.main.business.Owner;
import com.reztech.reservation_http_api.model.enums.OwnerType;
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
        // Test User builder
        User user = User.builder()
                .name("Test")
                .surname("User")
                .email("test@example.com")
                .gsm("05551234567")
                .build();
        
        assertNotNull(user);
        assertEquals("Test", user.getName());
        assertEquals("User", user.getSurname());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("05551234567", user.getGsm());
    }
    
    @Test
    public void testOwnerBuilder() {
        // Test Owner builder
        Owner owner = Owner.builder()
                .name("Owner")
                .lastname("Test")
                .email("owner@example.com")
                .gsm("05559876543")
                .ownerType(OwnerType.ADMIN)
                .build();
        
        assertNotNull(owner);
        assertEquals("Owner", owner.getName());
        assertEquals("Test", owner.getLastname());
        assertEquals("owner@example.com", owner.getEmail());
        assertEquals("05559876543", owner.getGsm());
        assertEquals(OwnerType.ADMIN, owner.getOwnerType());
    }
} 