package lumi.insert.app.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lumi.insert.app.entity.nondatabase.SupplyStatus; 

public record SupplyDetailResponse(UUID id, String invoiceId, UUID supplierId, List<SupplyItemResponse> supplyItems, Long totalItems, Long totalFee, Long totalDiscount, Long subTotal, Long grandTotal, Long totalUnpaid, Long totalPaid, Long totalUnrefunded, Long totalRefunded, SupplyStatus status, UUID staffId, List<String> messages, LocalDateTime createdAt) {
    
}
