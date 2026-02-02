package lumi.insert.app.dto.request;
 
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ProductGetByFilter extends PaginationRequest {

    @Builder.Default
    String name = "";

    @Builder.Default
    Long minPrice = 0L;

    @Builder.Default
    Long maxPrice = 999999999L;
    
    Long categoryId;

    @Builder.Default
    String sortBy = "sellPrice";

    String sortDirection;

}
