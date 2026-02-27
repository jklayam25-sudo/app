package lumi.insert.app.dto.request;
 

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemoUpdateRequest { 

    @Size(min = 1, message = "title length must be atleas 1")
    String title;
 
    @Size(min = 1, message = "body length must be atleas 1")
    String body;

    @Pattern(regexp = "FINANCE|CASHIER|WAREHOUSE|OWNER", message = "check documentation for role specification")
    private String role;
}
