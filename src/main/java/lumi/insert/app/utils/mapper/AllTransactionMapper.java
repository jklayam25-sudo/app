package lumi.insert.app.utils.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
 
import lumi.insert.app.dto.response.TransactionItemDelete;
import lumi.insert.app.dto.response.TransactionItemResponse;
import lumi.insert.app.dto.response.TransactionPaymentResponse;
import lumi.insert.app.dto.response.TransactionResponse;
import lumi.insert.app.entity.Transaction;
import lumi.insert.app.entity.TransactionItem;
import lumi.insert.app.entity.TransactionPayment;

@Mapper(componentModel = "spring")
public interface AllTransactionMapper {
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
    TransactionResponse createTransactionResponseDto(Transaction transaction);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "productId", source = "product.id")
    TransactionItemResponse createTransactionItemResponseDto(TransactionItem transactionItem);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "deleted", constant = "true")
    TransactionItemDelete createTransactionItemDeleteResponseDto(TransactionItem transactionItem);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "transactionId", source = "transaction.id")
    TransactionPaymentResponse createTransactionPaymentResponseDto(TransactionPayment transactionPayment);
}
