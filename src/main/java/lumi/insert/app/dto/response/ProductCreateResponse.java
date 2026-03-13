package lumi.insert.app.dto.response;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Confirmation response after successfully creating a new product in the system")
public class ProductCreateResponse {

    @Schema(description = "The auto-generated ID for the newly created product", example = "1005")
    private Long id;

    @Schema(description = "Confirmed name of the product", example = "Oli Yamalube Super Sport 1L")
    private String name;

    @Schema(description = "Initial cost price saved in the system", example = "75000")
    private Long basePrice;

    @Schema(description = "Initial selling price saved in the system", example = "95000")
    private Long sellPrice;

    @Schema(description = "Starting inventory level for this product", example = "24")
    private Long stockQuantity;

    @Schema(description = "Safety stock threshold for low-stock alerts", example = "5")
    private Long stockMinimum;

    @Schema(description = "Simplified category details linked to the new product")
    private CategorySimpleResponse category;

    @Schema(description = "Server-generated timestamp of creation")
    private LocalDateTime createdAt;

    @Schema(description = "Server-generated timestamp of the last update")
    private LocalDateTime updatedAt; 

}