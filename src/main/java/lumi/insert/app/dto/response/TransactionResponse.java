package lumi.insert.app.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lumi.insert.app.entity.nondatabase.TransactionStatus;

public record TransactionResponse(UUID id, String invoiceId, UUID customerId, Long totalItems, Long totalFee, Long totalDiscount, Long subTotal, Long grandTotal, Long totalUnpaid, Long totalPaid, Long totalUnrefunded, Long totalRefunded, TransactionStatus status, UUID staffId, List<String> messages, LocalDateTime createdAt, LocalDateTime updatedAt) {
    
}
