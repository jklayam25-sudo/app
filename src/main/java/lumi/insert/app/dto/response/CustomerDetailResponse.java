package lumi.insert.app.dto.response;

import java.util.UUID;

public record CustomerDetailResponse(UUID id, String name, String email, String contact, String shippingAddress, Long totalTransaction, Long totalUnpaid, Long totalPaid, Boolean isActive) {
    
}
