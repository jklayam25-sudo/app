package lumi.insert.app.service;
 
import java.io.ByteArrayInputStream; 
import java.time.LocalDateTime;
import java.util.List;

import lumi.insert.app.core.repository.projection.ProductOutOfStock;
import lumi.insert.app.dto.response.SupplyDetailResponse;
import lumi.insert.app.dto.response.TransactionDetailResponse;
import lumi.insert.app.dto.response.TransactionItemStatisticResponse; 

public interface PdfService {
    
    ByteArrayInputStream exportSupplyWithItems(SupplyDetailResponse data);

    ByteArrayInputStream exportTransactionWithItems(TransactionDetailResponse data);

    ByteArrayInputStream exportProductsStatistic(TransactionItemStatisticResponse statistic,
            List<ProductOutOfStock> oosProducts, LocalDateTime minTime, LocalDateTime maxTime);

}
