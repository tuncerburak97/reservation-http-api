package com.reztech.reservation_http_api.model.entity.main.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * User entity for reservation system
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {
    
    @Id
    private String id;
    
    private String name;
    
    private String surname;
    
    //@Indexed
    private String gsm;
    
    //@Indexed(unique = true)
    private String email;

    //private UserRole role; // CUSTOMER, EMPLOYEE, OWNER
    //TODO password bilgisi olabilir mi ?

    //TODO kullanıcı üye olduysa eğer
    // verified

    // TODO Business kullanıcı tanımı yapılabilir
    //TODO Roller OWNER => WORKER1, WORKER2
    // Owner collectionu da kaldırılabilir bu collection kullanılabilir.
    // Müşteriler (Hizmet alanlar için Customer collectionu oluşturabilir)
} 