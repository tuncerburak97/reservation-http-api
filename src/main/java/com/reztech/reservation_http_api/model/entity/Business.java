package com.reztech.reservation_http_api.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Business entity for reservation system
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "businesses")
public class Business {
    
    @Id
    private String id;
    
    @TextIndexed(weight = 2)
    @Indexed
    private String name;
    
    private Location location;
    
    @Indexed
    private Owner owner;
    
    private ContactInfo contactInfo;
} 