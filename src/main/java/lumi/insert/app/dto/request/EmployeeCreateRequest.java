package lumi.insert.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "Request to create a new employee account")
public class EmployeeCreateRequest {
    
    @NotBlank(message = "username cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9]{5,30}$", message = "Username has to be 5-30 length and doesn't contain unique char")
    @Schema(description = "Unique username for login", example = "johndoe")
    private String username;

    @NotBlank(message = "fullname cannot be empty")
    @Schema(description = "Full name of the employee", example = "John Doe")
    private String fullname;

    @NotBlank(message = "password cannot be empty")
    @Pattern(regexp = "^(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{5,50}$", message = "password has to be 5-50 length and has atleast 1 unique char")
    @Schema(description = "Password (5-50 chars with at least one special character)", example = "Pass@123")
    private String password;

    @NotNull(message = "joinDate cannot be empty")
    @Schema(description = "Join date of the employee", example = "2024-12-31T23:59:59")
    private LocalDateTime joinDate;

}
