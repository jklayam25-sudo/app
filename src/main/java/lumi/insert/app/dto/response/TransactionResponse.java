package lumi.insert.app.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lumi.insert.app.entity.TransactionStatus;

public record TransactionResponse(UUID id, String invoiceId, UUID customerId, Long totalItems, Long totalFee, Long totalDiscount, Long subTotal, Long grandTotal, Long totalUnpaid, Long totalPaid, TransactionStatus status, UUID staffId, LocalDateTime createdAt, LocalDateTime updatedAt) {
    
}
