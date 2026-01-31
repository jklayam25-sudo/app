package lumi.insert.app.utils.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import lumi.insert.app.dto.response.CategoryResponse;
import lumi.insert.app.dto.response.CategorySimpleResponse;
import lumi.insert.app.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
    CategoryResponse createDtoResponseFromCategory(Category entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
    CategorySimpleResponse createDtoSimpleResponseFromCategory(Category entity);

}
