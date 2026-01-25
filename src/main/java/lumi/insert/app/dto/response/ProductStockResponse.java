package lumi.insert.app.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductStockResponse {
    
    private Long id;

    private Long stockQuantity;

}
