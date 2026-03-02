package lumi.insert.app.utils.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
 
import lumi.insert.app.dto.request.SupplierUpdateRequest; 
import lumi.insert.app.dto.response.SupplierDetailResponse; 
import lumi.insert.app.entity.Supplier; 

@Mapper(componentModel = "spring")
public interface SupplierMapper {
     

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
    SupplierDetailResponse createDtoDetailResponseFromSupplier(Supplier entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE) 
    void updateEntityFromDto(SupplierUpdateRequest dto, @MappingTarget Supplier entity);
    
}
