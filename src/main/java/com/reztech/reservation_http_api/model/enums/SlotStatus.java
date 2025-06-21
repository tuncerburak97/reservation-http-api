package com.reztech.reservation_http_api.model.enums;

/**
 * Status enumeration for time slots
 */
public enum SlotStatus {
    AVAILABLE,    // Slot is available for booking
    BOOKED,       // Slot is already booked
    BLOCKED,
    EXPIRED       // Slot is blocked by business owner
}

/* TODO slot statuler genişletilebililir
 AVAILABLE,      // Rezervasyona açık
    BOOKED,         // Rezervasyon yapılmış
    BLOCKED,        // İşletme tarafından bloke edilmiş
    HELD,           // Müşteri tarafından geçici tutuluyor (örn. ödeme sırasında)
    CANCELED,       // Önceden alınmış ama iptal edilmiş
    RESCHEDULED,    // Başka bir saate taşınmış
    EXPIRED,        // Zamanı geçti, işlem yapılmadı
    NO_SHOW,        // Randevuya gelinmedi
    MAINTENANCE     // Sistemsel engel (otomatik blok vb.)
 */