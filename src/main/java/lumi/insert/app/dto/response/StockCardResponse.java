package lumi.insert.app.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
 
import lombok.Builder; 
import lumi.insert.app.entity.nondatabase.StockMove;

@Builder
public record StockCardResponse(UUID id, UUID referenceId, Long productId, String productName, Long quantity, Long oldStock, Long newStock, Long basePrice, StockMove type, String description, LocalDateTime createdAt) {
    
}
