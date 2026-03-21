package lumi.insert.app.dto.response;

import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Simplified response containing only the basic identity of a supplier")
public record SupplierNameResponse(
    
    @Schema(description = "Unique identifier of the supplier", example = "c0a80101-8b7c-1d2e-9f0a-112233445566")
    UUID id, 
    
    @Schema(description = "Legal or trade name of the supplier company", example = "PT. Maju Jaya Perkasa")
    String name
    
)  implements Identifiable {

    @Override
    public String getId() {
        return String.valueOf(this.id);
    }

}