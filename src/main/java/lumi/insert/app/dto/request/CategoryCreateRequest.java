package lumi.insert.app.dto.request;

import jakarta.validation.constraints.NotBlank; 
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "Request to create a new product category")
public class CategoryCreateRequest {

    @NotBlank(message = "Name cannot be empty")
    @Schema(description = "Name of the category", example = "Electronics")
    private String name;
    
}
