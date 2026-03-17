package lumi.insert.app.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lumi.insert.app.core.repository.projection.ProductRefund;
import lumi.insert.app.core.repository.projection.ProductSale;

@Data
@Builder
public class TransactionItemStatisticResponse {
    
    private List<ProductSale> productSales;

    private List<ProductRefund> productRefunds;
}
