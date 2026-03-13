package lumi.insert.app.dto.response;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response containing authentication tokens and user profile information upon successful login")
public record AuthTokenResponse(
    
    @Schema(description = "JWT Access Token for authorizing API requests", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String accessToken, 
    
    @Schema(description = "Token used to obtain a new access token without re-authenticating", example = "d8e9f0a1-b2c3-4d5e-6f7g-8h9i0j1k2l3m")
    String refreshToken, 
    
    @Schema(description = "Detailed profile of the employee currently logged in")
    EmployeeResponse employee, 
    
    @Schema(description = "Timestamp when the access token will expire")
    LocalDateTime expiredAt, 
    
    @Schema(description = "Timestamp when the tokens were issued")
    LocalDateTime createdAt
) {
    
}