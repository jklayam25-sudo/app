package lumi.insert.app.utils.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
 
import lumi.insert.app.dto.response.StockCardResponse; 
import lumi.insert.app.entity.StockCard;

@Mapper(componentModel = "spring")
public interface StockCardMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
    StockCardResponse createDtoResponseFromStockCard(StockCard entity);

}
