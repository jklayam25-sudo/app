package lumi.insert.app.dto.response;

import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Lightweight response containing only the basic identity of a customer")
public record CustomerNameResponse(
    
    @Schema(description = "Unique identifier of the customer", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
    UUID id, 
    
    @Schema(description = "Full name of the customer for display purposes", example = "Budi Santoso")
    String name
) {
    
}