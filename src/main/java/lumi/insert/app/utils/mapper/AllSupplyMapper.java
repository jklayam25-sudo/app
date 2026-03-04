package lumi.insert.app.utils.mapper;
 

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper; 
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import lumi.insert.app.dto.request.SupplyUpdateRequest;
import lumi.insert.app.dto.response.SupplyDetailResponse;
import lumi.insert.app.dto.response.SupplyItemResponse;
import lumi.insert.app.dto.response.SupplyResponse;
import lumi.insert.app.entity.Supply;
import lumi.insert.app.entity.SupplyItem;  

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface AllSupplyMapper {
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE) 
    SupplyResponse createSimpleDTO(Supply supply);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
    SupplyDetailResponse createDetailDTO(Supply supply);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE) 
    SupplyItemResponse createItemResponseDTO(SupplyItem supplyItem);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE) 
    void updateSupply(SupplyUpdateRequest request, @MappingTarget Supply supply);

    // @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    // @Mapping(target = "deleted", constant = "true")
    // TransactionItemDelete createTransactionItemDeleteResponseDto(TransactionItem transactionItem);

    // @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    // @Mapping(target = "transactionId", source = "transaction.id")
    // TransactionPaymentResponse createTransactionPaymentResponseDto(TransactionPayment transactionPayment);
}
