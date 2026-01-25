package lumi.insert.app.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductGetNameRequest {

    @NotNull
    private String name;

    private Integer page;

    private Integer size;
}
