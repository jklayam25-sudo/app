package lumi.insert.app.dto.response;
import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record ProductDeleteResponse (Long id, String name, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
