package lumi.insert.app.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionItemResponse(UUID id, UUID transactionId, Long productId, Long price, Long quantity, LocalDateTime createdAt, LocalDateTime updatedAt) {
    
}
