package lumi.insert.app.utils.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import lumi.insert.app.dto.request.ProductEditRequest;
import lumi.insert.app.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateProductFromDto(ProductEditRequest dto, @MappingTarget Product entity);
}
