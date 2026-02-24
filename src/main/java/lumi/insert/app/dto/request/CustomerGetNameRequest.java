package lumi.insert.app.dto.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
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
public class CustomerGetNameRequest extends PaginationRequest{
    
    @NotBlank(message = "Name cannot be empty")
    @Length(min = 3, message = "Request length cannot be lesser than 3")
    private String name;

}
