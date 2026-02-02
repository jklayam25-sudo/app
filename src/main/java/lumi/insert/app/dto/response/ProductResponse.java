package lumi.insert.app.dto.response;

import java.time.LocalDateTime;

import lombok.Builder; 

@Builder
public record ProductResponse (Long id, String name, Long basePrice, Long sellPrice, Long stockQuantity, Long stockMinimum, CategorySimpleResponse category, LocalDateTime createdAt, LocalDateTime updatedAt) {

}
