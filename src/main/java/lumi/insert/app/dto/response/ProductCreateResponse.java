package lumi.insert.app.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductCreateResponse {

    private Long id;

    private String name;

    private Long basePrice;

    private Long sellPrice;

    private Long stockQuantity;

    private Long stockMinimum;

    private CategorySimpleResponse category;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt; 

}
