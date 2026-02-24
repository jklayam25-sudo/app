package lumi.insert.app.dto.request; 
import jakarta.validation.constraints.Email; 
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUpdateRequest{
     
    @Pattern(regexp = "^[a-zA-Z0-9.-_ ]{5,30}$", message = "name has to be 5-30 length")
    private String name;
 
    @Email(message = "email doesn't meet the email format")
    private String email;
 
    private String contact;
 
    private String shippingAddress;

    private Boolean isActive;

    private Double Latitude;

    private Double Longitude;

}
