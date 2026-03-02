package lumi.insert.app.dto.response;

import java.util.UUID;

public record SupplierDetailResponse(UUID id, String name, String email, String contact, Long totalTransaction, Long totalUnpaid, Long totalPaid, Boolean isActive) {
    
}
