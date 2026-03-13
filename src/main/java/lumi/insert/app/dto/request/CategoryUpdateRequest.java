package lumi.insert.app.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "Request to update an existing category")
public class CategoryUpdateRequest { 

    @NotNull
    @Schema(description = "Category ID to update", example = "1")
    private Long id;

    @Schema(description = "Updated category name", example = "Electronics")
    private String name;
    
}
