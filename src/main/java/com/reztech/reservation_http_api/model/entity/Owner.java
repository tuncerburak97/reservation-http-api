package com.reztech.reservation_http_api.model.entity;

import com.reztech.reservation_http_api.model.enums.OwnerType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Owner entity for business management
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "owners")
public class Owner {
    
    @Id
    private String id;
    
    private String name;
    
    private String lastname;
    
    @Indexed
    private String gsm;
    
    @Indexed(unique = true)
    private String email;
    
    @Indexed
    private OwnerType ownerType;
} 