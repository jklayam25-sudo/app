package lumi.insert.app.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object confirming the deletion or removal of an item from a transaction")
public record TransactionItemDelete(
    
    @Schema(description = "Unique identifier of the transaction item that was handled", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    UUID id, 
    
    @Schema(description = "ID of the transaction the item belonged to", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID transactionId, 
    
    @Schema(description = "ID of the product that was removed", example = "1001")
    Long productId, 
    
    @Schema(description = "Flag indicating if the item has been successfully deleted/deactivated", example = "true")
    boolean deleted, 
    
    @Schema(description = "Timestamp when the deletion status was last updated")
    LocalDateTime updatedAt
) {
    
}