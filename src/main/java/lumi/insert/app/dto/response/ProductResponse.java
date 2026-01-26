package lumi.insert.app.dto.response;

import java.time.LocalDateTime;

import lumi.insert.app.entity.Category;

public record ProductResponse (Long id, String name, Long basePrice, Long sellPrice, Long stockQuantity, Long stockMinimum, Category category, LocalDateTime createdAt, LocalDateTime updatedAt) {

}
