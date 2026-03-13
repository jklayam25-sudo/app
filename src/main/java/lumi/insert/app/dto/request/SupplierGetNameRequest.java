package lumi.insert.app.dto.request;

import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to search suppliers by name")
public class SupplierGetNameRequest extends PaginationRequest{
    
    @NotBlank(message = "Name cannot be empty")
    @Length(min = 3, message = "Request length cannot be lesser than 3")
    @Schema(description = "Supplier name to search for", example = "PT ABC Trading")
    private String name;

    @Schema(description = "Last supplier ID for pagination", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID lastId;

}
