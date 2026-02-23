package lumi.insert.app.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern; 
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class CustomerCreateRequest {

    @NotBlank(message = "name cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9.]{5,30}$", message = "name has to be 5-30 length")
    private String name;
 
    @Email(message = "email doesn't meet the email format")
    private String email;

    @NotBlank(message = "shippingAddress cannot be empty")
    private String contact;

    @NotBlank(message = "shippingAddress cannot be empty")
    private String shippingAddress;

}
