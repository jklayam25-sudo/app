package lumi.insert.app.dto.response;

import java.util.UUID;

public record CustomerResponse(UUID id, String name, String email, String contact) {
    
}
