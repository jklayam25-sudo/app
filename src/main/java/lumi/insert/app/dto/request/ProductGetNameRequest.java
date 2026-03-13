package lumi.insert.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Schema(description = "Request to search products by name")
public class ProductGetNameRequest extends PaginationRequest {

    @NotBlank(message = "Name cannot be empty")
    @Schema(description = "Product name to search for", example = "Laptop")
    private String name;

    @Schema(description = "Last product ID for pagination", example = "100")
    private Long lastId;
}
