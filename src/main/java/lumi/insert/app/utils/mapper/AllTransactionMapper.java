package lumi.insert.app.utils.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import lumi.insert.app.dto.response.TransactionResponse;
import lumi.insert.app.entity.Transaction;

@Mapper(componentModel = "spring")
public interface AllTransactionMapper {
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
    TransactionResponse createTransactionResponseDto(Transaction transaction);
}
