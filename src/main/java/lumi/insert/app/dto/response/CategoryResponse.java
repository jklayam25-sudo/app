package lumi.insert.app.dto.response;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detailed response representing a product category and its statistics")
public record CategoryResponse(
    
    @Schema(description = "Primary key of the category", example = "10")
    Long id, 
    
    @Schema(description = "Name of the category", example = "Motorcycle Accessories")
    String name, 
    
    @Schema(description = "Current count of active products linked to this category", example = "125")
    Long totalItems, 
    
    @Schema(description = "Status indicating if this category is visible for new products", example = "true")
    Boolean isActive, 
    
    @Schema(description = "Timestamp when the category was created")
    LocalDateTime createdAt, 
    
    @Schema(description = "Timestamp of the last update to category name or status")
    LocalDateTime updatedAt
) {

}