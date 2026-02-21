package lumi.insert.app.dto.response;

import java.time.LocalDateTime;

public record AuthTokenResponse(String accessToken, String refreshToken, EmployeeResponse employee, LocalDateTime expiredAt, LocalDateTime createdAt) {
    
}
