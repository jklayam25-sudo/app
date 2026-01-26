package lumi.insert.app.dto.request;
 
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class ProductGetByFilter extends PaginationRequest {
    String name;
    Long minPrice;
    Long maxPrice;
    Long categoryId;
    String sortBy;
    String sortDirection;


}
