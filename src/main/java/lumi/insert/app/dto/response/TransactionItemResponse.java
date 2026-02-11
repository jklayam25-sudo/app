package lumi.insert.app.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionItemResponse(UUID id, UUID transactionId, Long productId,String description, Long price, Long quantity, LocalDateTime createdAt, LocalDateTime updatedAt) {
    
}
