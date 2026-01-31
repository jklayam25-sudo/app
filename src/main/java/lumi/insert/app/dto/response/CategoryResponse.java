package lumi.insert.app.dto.response;

import java.time.LocalDateTime;

public record CategoryResponse(Long id, String name, Long totalItems, LocalDateTime createdAt, LocalDateTime updatedAt) {

}