package lumi.insert.app.utils.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import lumi.insert.app.dto.response.AuthTokenResponse; 
import lumi.insert.app.entity.AuthToken; 

@Mapper(componentModel = "spring", uses = EmployeeMapper.class)
public interface AuthMapper {
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "accessToken", source = "accessToken")
    AuthTokenResponse createDtoResponseFromEntity(String accessToken, AuthToken entity);

}
