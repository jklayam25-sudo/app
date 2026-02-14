package lumi.insert.app.utils.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import lumi.insert.app.dto.response.EmployeeResponse; 
import lumi.insert.app.entity.Employee; 

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
    EmployeeResponse createDtoResponseFromEmployee(Employee entity);

}
