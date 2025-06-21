package com.reztech.reservation_http_api.model.entity.main.business;

import com.reztech.reservation_http_api.model.entity.embedded.ContactInfo;
import com.reztech.reservation_http_api.model.entity.embedded.Location;
import com.reztech.reservation_http_api.model.entity.main.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
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
    
    //@TextIndexed(weight = 2)
    //@Indexed
    private String name;

    //TODO sektör collectionu içerebilir
    // private Sektör string ( Sektör yönetimi bizim tarafımızdan olmalı)
    // Business : {Berber, Güzellik Merkezi ,Diş Polikilinliği ...vs}
    
    private Location location;
    
    //@Indexed
    //TODO
    // List<Worker> workers; Bu workerlardan biri OWNER tipinde olabilir. ( OWNER == USER_ADMIN )
    //TODO OWNER sisteme yeni kullanıcı ekler ve siler.
    private User owner;
    
    private ContactInfo contactInfo;
} 