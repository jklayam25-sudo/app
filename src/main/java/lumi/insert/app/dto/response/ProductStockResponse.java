package lumi.insert.app.dto.response;

import lombok.Builder;

@Builder
public record ProductStockResponse (Long id, Long stockQuantity) {

}
