package lumi.insert.app.dto.response;

import lombok.Builder;
import lumi.insert.app.entity.nondatabase.EmployeeRole;

@Builder
public record MemoResponse(Long id, String title, String body, String[] images, EmployeeRole role, Boolean isRead) {
    
}
