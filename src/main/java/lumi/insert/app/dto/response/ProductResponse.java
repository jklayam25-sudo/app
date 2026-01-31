package lumi.insert.app.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lumi.insert.app.entity.Category;

@Builder
public record ProductResponse (Long id, String name, Long basePrice, Long sellPrice, Long stockQuantity, Long stockMinimum, Category category, LocalDateTime createdAt, LocalDateTime updatedAt) {

}
