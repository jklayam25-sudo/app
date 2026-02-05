package lumi.insert.app.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionItemDelete(UUID id, UUID transactionId, Long productId, boolean deleted, LocalDateTime updatedAt) {
    
}
