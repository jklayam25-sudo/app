package lumi.insert.app.dto.response;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Confirmation response after a product has been deactivated or soft-deleted")
public record ProductDeleteResponse (
    
    @Schema(description = "Unique identifier of the deleted product", example = "1005")
    Long id, 
    
    @Schema(description = "Name of the product for record confirmation", example = "Oli Yamalube Super Sport 1L")
    String name, 
    
    @Schema(description = "New status indicating if the product is no longer active in the system", example = "false")
    Boolean isActive, 
    
    @Schema(description = "Original timestamp when the product was created")
    LocalDateTime createdAt, 
    
    @Schema(description = "Timestamp when the deletion/deactivation occurred")
    LocalDateTime updatedAt
) {
}