package lumi.insert.app.service.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat; 
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
 
import lumi.insert.app.dto.request.EmployeeCreateRequest;
import lumi.insert.app.dto.response.EmployeeResponse;
import lumi.insert.app.entity.Employee;
import lumi.insert.app.exception.DuplicateEntityException; 
 
public class EmployeeServiceCreateTest extends BaseEmployeeServiceTest{
    
    @Test
    @DisplayName("Should return employee DTO when saved successfully")
    void createEmployee_validRequest_returnEmployeeResponseDTO(){
        when(employeeRepositoryMock.save(any(Employee.class))).thenAnswer(ans -> ans.getArgument(0));
        when(passwordEncoder.encode(setupEmployee.getPassword())).thenReturn("decodedSECRET");

        EmployeeCreateRequest employeeCreateRequest = EmployeeCreateRequest.builder()
        .username(setupEmployee.getUsername())
        .fullname(setupEmployee.getFullname())
        .password(setupEmployee.getPassword())
        .build();

        EmployeeResponse employeeDTO = employeeServiceMock.createEmployee(employeeCreateRequest);
        assertEquals(setupEmployee.getUsername(), employeeDTO.username());
        assertEquals(setupEmployee.getFullname(), employeeDTO.fullname());
        verify(employeeRepositoryMock).save(argThat(arg -> arg.getPassword().equals("decodedSECRET")));
    }

    @Test
    @DisplayName("Should throw duplicate entity exception when request username exists")
    void createEmployee_duplicateEntity_throwDuplicate(){
        when(employeeRepositoryMock.existsByUsername(anyString())).thenReturn(true);

        assertThrows(DuplicateEntityException.class, () -> employeeServiceMock.createEmployee(EmployeeCreateRequest.builder().username("test").build()));
    }

}
