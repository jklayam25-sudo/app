package lumi.insert.app.dto.response;

import java.util.UUID;

public record SupplyItemResponse(UUID id, ProductName product, Long price, Long quantity, String description) {
    
}
